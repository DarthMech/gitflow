package ru.rambler.plugins.gitflow.actions.start;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.DialogBuilder;
import git4idea.commands.GitCommandResult;
import git4idea.repo.GitRepository;
import org.jetbrains.annotations.NotNull;
import ru.rambler.plugins.gitflow.actions.GitFlowGitAction;
import ru.rambler.plugins.gitflow.view.StartActionView;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractStartAction extends GitFlowGitAction {

    public AbstractStartAction(String aText) {
        super(aText);
    }

    @Override
    public void gitActionPerformed(AnActionEvent anActionEvent) {
        List<String> branchesNamesList = getStartBranchesNames();

        if (branchesNamesList == null || branchesNamesList.isEmpty()) {
            showErrorMessage("Нет подходяящих веток", "Список веток пуст", anActionEvent.getProject());
            return;
        }

        StartActionView.StartActionData data = showCreateBranchDialog(branchesNamesList);

        if (data != null) {
            String newBranchName = data.getNewBranchName();

            if (newBranchName != null && !newBranchName.isEmpty()) {
                String startBranch = data.getStartBranchName();
                String startPoint = "";

                GitRepository repo = getRepo();
                if (startBranch != null && !startBranch.isEmpty()) {
                    GitCommandResult getLastCommitRes = getGit().getLastCommitsForBranch(repo, startBranch, 1);

                    if (getLastCommitRes.success() && !getLastCommitRes.getOutput().isEmpty()) {
                        startPoint = getLastCommitRes.getOutput().get(0).replace("\"", "");
                    }

                    String fullCurrentBranchName = getBranchPrefix() + newBranchName;
                    GitCommandResult newBranchRes = getGit().checkoutNewBranch(repo,
                            fullCurrentBranchName,
                            startPoint, null);

                    if (newBranchRes.success() && data.isPushBranch()) {
                        GitCommandResult pushBranchRes = getGit().pushBranch(repo,
                                getSettings().getRepoName(),
                                fullCurrentBranchName,
                                true,
                                null);

                        if (!pushBranchRes.success()) {
                            showErrorMessage(pushBranchRes.getErrorOutputAsJoinedString(), "Ошибка пуша на сервер", anActionEvent.getProject());
                        }
                    }
                }
            } else {
                showErrorMessage("Название веки не может быть пустым", "Неверное название ветки", anActionEvent.getProject());
            }
        }
    }

    @Override
    protected boolean isGitActionEnabled(@NotNull AnActionEvent anActionEvent) {
        return true;
    }

    // --------------------------------------------------------------------------------------------------
    String getBranchName() {
        return "";
    }

    abstract String getBranchPrefix();

    abstract String getDialogTitle();

    List<String> getStartBranchesNames() {
        return new ArrayList<>();
    }

    // ---------------------------------------------------------------------------------------------------
    private StartActionView.StartActionData showCreateBranchDialog(List<String> branches) {
        StartActionView view = new StartActionView();
        view.setBranchesNames(branches);
        view.setPrefixName(getBranchPrefix());
        view.setStartBranchName(getBranchName());

        DialogBuilder builder = new DialogBuilder();
        builder.setTitle(getDialogTitle());
        builder.setCenterPanel(view.getMainPanel());
        builder.setPreferredFocusComponent(view.getFocusComponent());

        int exitCode = builder.show();
        if (exitCode == 0) {
            return view.getData();
        } else {
            return null;
        }
    }
}