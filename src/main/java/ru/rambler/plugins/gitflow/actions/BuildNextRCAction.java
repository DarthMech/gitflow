package ru.rambler.plugins.gitflow.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.DialogWrapper;
import git4idea.commands.GitCommandResult;
import git4idea.repo.GitRepository;
import org.jetbrains.annotations.NotNull;
import ru.rambler.plugins.gitflow.utils.tags.Tag;
import ru.rambler.plugins.gitflow.view.PushNewRcTagView;

import java.util.List;

public class BuildNextRCAction extends GitFlowGitAction {

    public BuildNextRCAction() {
        super("Build next rc");
    }

    @Override
    public void gitActionPerformed(AnActionEvent anActionEvent) {
        boolean isReleaseBranchExist = getGitFlowUtils().isReleaseBranchExist();

        if (!isReleaseBranchExist) {
            showErrorMessage("Нет релизных веток", "Не возможно создать метку", anActionEvent.getProject());
            return;
        }

        GitRepository repo = getRepo();

        GitCommandResult resultTagLists = getGit().getTags(repo, null);

        Tag tag = null;
        if (resultTagLists.success()) {
            tag = getGitFlowUtils().getLastTag(resultTagLists.getOutput());
        }

        if (tag != null) {
            PushNewRcTagView.TagData data = showExtendedAddTagDialog(tag);

            //@ToDo: надо сделать pull на ветку перед созданием метки
            if (data != null) {
                boolean isPushed = isBranchPushCompleted(data.getBranchName());

                if (!isPushed) {
                    showErrorMessage("Ветка незапушена на сервер!", "Push tag", anActionEvent.getProject());
                    return;
                }

                GitCommandResult resultPushTag = getGit().pushNewTag(repo,
                        getSettings().getRepoName(),
                        data.getTagName(),
                        data.getComment(),
                        null,
                        data.isPushTag(),
                        null);
            }
        }
    }

    @Override
    protected boolean isGitActionEnabled(@NotNull AnActionEvent anActionEvent) {
        return true;
    }

    // --------------------------------- Inner Methods -------------------------------------------------
    /**
     * git log origin/feature/812_quests_tickets..feature/812_quests_tickets
     *
     * @param branchName
     * @return
     */
    private boolean isBranchPushCompleted(String branchName) {
        GitCommandResult resultPushTag = getGit().getNotPushInfo(getRepo(), getSettings().getRepoName(), branchName);

        return resultPushTag.getOutput().isEmpty();
    }

    private PushNewRcTagView.TagData showExtendedAddTagDialog(Tag tag) {
        List<String> releaseBranches = getGitFlowUtils().getReleaseBranchesNames();

        PushNewRcTagView dialog = new PushNewRcTagView();
        dialog.setTag(tag);
        dialog.setBranchesNames(releaseBranches);

        dialog.show();

        int exitCode = dialog.getExitCode();
        if (exitCode == DialogWrapper.OK_EXIT_CODE) {
            return dialog.getTagData();
        } else {
            return null;
        }
    }
}