package ru.rambler.plugins.gitflow.actions.start;

import com.intellij.openapi.actionSystem.AnActionEvent;

import java.util.List;

public class StartFeatureAction extends AbstractStartAction {

    public StartFeatureAction() {
        super("Start feature");
    }

    @Override
    public void gitActionPerformed(AnActionEvent anActionEvent) {
        super.gitActionPerformed(anActionEvent);
    }

    @Override
    String getBranchPrefix() {
        return getSettings().getFeaturePrefix();
    }

    @Override
    String getDialogTitle() {
        return "Начать доработку";
    }

    @Override
    List<String> getStartBranchesNames() {
        return getGitFlowUtils().getBranchesForFeature();
    }
}
