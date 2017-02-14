package ru.rambler.plugins.gitflow.actions.end;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import git4idea.commands.GitCommandResult;
import git4idea.repo.GitRepository;
import org.jetbrains.annotations.NotNull;
import ru.rambler.plugins.gitflow.utils.tags.Tag;
import ru.rambler.plugins.gitflow.utils.tags.TagBuilder;
import ru.rambler.plugins.gitflow.view.EndReleaseView;

import java.util.List;

public class EndReleaseAction extends AbstractEndAction {

    public EndReleaseAction() {
        super("End release");
    }

    @Override
    public void gitActionPerformed(AnActionEvent anActionEvent) {
        List<String> releaseBranchesList = getBranchesNames();

        if (releaseBranchesList == null || releaseBranchesList.isEmpty()) {
            showErrorMessage("Нет подходяящих веток", "Список веток пуст", anActionEvent.getProject());
            return;
        }

        EndReleaseView.EndReleaseData data = showEndReleaseDialog(releaseBranchesList);

        if (data != null) {
            acceptEndReleaseAction(data, anActionEvent.getProject());
        }
    }

    @Override
    protected boolean isGitActionEnabled(@NotNull AnActionEvent event) {
        List<String> releaseBranchesList = getGitFlowUtils().getReleaseBranchesNames();
        return !(releaseBranchesList == null || releaseBranchesList.isEmpty());
    }

    @Override
    String getDialogTitle() {
        return "Завершить релиз";
    }

    @Override
    List<String> getBranchesNames() {
        return getGitFlowUtils().getReleaseBranchesNames();
    }

    // ---------------------------------------------------------------------------------------------
    private EndReleaseView.EndReleaseData showEndReleaseDialog(List<String> releaseBranchesList) {
        EndReleaseView dialog = new EndReleaseView();

        Tag master = getLastMasterTag();
        dialog.setBranchesNames(releaseBranchesList);
        dialog.setMasterTag(master);

        dialog.show();

        int exitCode = dialog.getExitCode();
        if (exitCode == 0) {
            return dialog.getDataView();
        } else {
            return null;
        }
    }

    private Tag getLastMasterTag() {
        GitRepository repo = getRepo();

        GitCommandResult resultTagLists = getGit().getTags(repo, null);

        Tag tag = null;
        if (resultTagLists.success()) {
            List<String> tagsList = resultTagLists.getOutput();

            String[] tagsArray = new String[tagsList.size()];
            tagsList.toArray(tagsArray);

            int index = tagsArray.length - 1;

            do {
                tag = TagBuilder.getTag(tagsArray[index]);
                index--;
            } while (tag == null && index > -1);
        }

        return tag;
    }

    private void acceptEndReleaseAction(EndReleaseView.EndReleaseData data, Project project) {
        String devBranch = getGitFlowUtils().getDevBranchName();
        GitCommandResult devMergeRes = getGit().mergeReleaseBranch(getRepo(), data.getReleaseBranchName(), devBranch, data.isPushRemote());

        String masterBranch = getGitFlowUtils().getMasterBranchName();
        GitCommandResult masterMergeRes =  getGit().mergeReleaseBranch(getRepo(), data.getReleaseBranchName(), masterBranch, data.isPushRemote());

        GitCommandResult status = getGit().status(getRepo());

        if (masterMergeRes.success()) {
            GitCommandResult resultPushTag = getGit().pushNewTag(getRepo(),
                    getSettings().getRepoName(),
                    data.getTagName(),
                    null,
                    null,
                    data.isPushRemote(),
                    null);

            if (data.isDeleteBranch()) {
                GitCommandResult deleteRes = getGit().branchDelete(getRepo(), data.getReleaseBranchName(), false);
            }
        } else {
            showErrorMessage("Не получилось смержить релиз в мастер", "Ошибка мержа", project);
        }
    }
}