package ru.rambler.plugins.gitflow.actions.end;

import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EndHotfixAction extends AbstractEndAction {

    public EndHotfixAction() {
        super("End hotfix");
    }

    @Override
    public void gitActionPerformed(AnActionEvent anActionEvent) {
        super.gitActionPerformed(anActionEvent);
    }

    @Override
    protected boolean isGitActionEnabled(@NotNull AnActionEvent event) {
        List<String> releaseBranchesList = getGitFlowUtils().getHotfixBranchesNames();
        return !(releaseBranchesList == null || releaseBranchesList.isEmpty());
    }

    @Override
    String getDialogTitle() {
        return "Завершить заплатку";
    }

    @Override
    List<String> getBranchesNames() {
        return getGitFlowUtils().getHotfixBranchesNames();
    }
}