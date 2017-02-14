package ru.rambler.plugins.gitflow.actions.end;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.DialogBuilder;
import git4idea.commands.GitCommandResult;
import git4idea.repo.GitRepository;
import org.jetbrains.annotations.NotNull;
import ru.rambler.plugins.gitflow.actions.GitFlowGitAction;
import ru.rambler.plugins.gitflow.view.EndActionView;

import java.util.List;

public abstract class AbstractEndAction extends GitFlowGitAction {

    public AbstractEndAction(String aText) {
        super(aText);
    }

    @Override
    public void gitActionPerformed(AnActionEvent anActionEvent) {
        List<String> branchesNamesList = getBranchesNames();

        if (branchesNamesList == null || branchesNamesList.isEmpty()) {
            showErrorMessage("Нет подходяящих веток", "Список веток пуст", anActionEvent.getProject());
            return;
        }

        EndActionView.EndActionData data = showEndActionDialog(branchesNamesList);

        if (data != null) {
            pushSelectedBranch(data.getEndBranchName(), anActionEvent);

            if (data.isDeleteBranch()) {
                GitCommandResult deleteRes = getGit().branchDelete(getRepo(), data.getEndBranchName(), false);
            }
        }
    }

    @Override
    protected boolean isGitActionEnabled(@NotNull AnActionEvent anActionEvent) {
        return false;
    }

    protected void pushSelectedBranch(String branchName, AnActionEvent anActionEvent) {
        GitRepository repo = getRepo();

        GitCommandResult resultBranchPush = getGit().pushBranch(repo,
                getSettings().getRepoName(),
                branchName,
                false,
                null);

        if (!resultBranchPush.success()) {
            showErrorMessage(resultBranchPush.getErrorOutputAsJoinedString(), "End action", anActionEvent.getProject());
        } else {
            showInfoMessage("Изменения запушены на сервер", "End action", anActionEvent.getProject());
        }
    }

    //-----------------------------------------------------------------------------------------------------------------
    abstract String getDialogTitle();

    abstract List<String> getBranchesNames();

    // ----------------------------------------------------------------------------------------------------------------
    private EndActionView.EndActionData showEndActionDialog(List<String> releaseBranchesList) {
        EndActionView view = new EndActionView();
        view.setBranchesNames(releaseBranchesList);

        DialogBuilder builder = new DialogBuilder();
        builder.setTitle(getDialogTitle());
        builder.setCenterPanel(view.getMainPanel());

        int exitCode = builder.show();
        if (exitCode == 0) {
            return view.getData();
        } else {
            return null;
        }
    }
}