package ru.rambler.plugins.gitflow.actions.start;

import com.intellij.openapi.actionSystem.AnActionEvent;

import java.util.List;

public class StartHotfixAction extends AbstractStartAction {

    public StartHotfixAction() {
        super("Start hotfix");
    }

    @Override
    public void gitActionPerformed(AnActionEvent anActionEvent) {
        super.gitActionPerformed(anActionEvent);
    }

    @Override
    String getBranchPrefix() {
        return getSettings().getHotfixPrefix();
    }

    @Override
    String getDialogTitle() {
        return "Начать заплатку";
    }

    @Override
    List<String> getStartBranchesNames() {
        return getGitFlowUtils().getBranchesForHotfix();
    }
}