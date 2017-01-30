package ru.rambler.plugins.gitflow.actions.start;

import com.intellij.openapi.actionSystem.AnActionEvent;

import java.util.List;

public class StartReleaseAction extends AbstractStartAction {
    public StartReleaseAction() {
        super("Start release");
    }

    @Override
    public void gitActionPerformed(AnActionEvent anActionEvent) {
        super.gitActionPerformed(anActionEvent);
    }

    @Override
    String getBranchName() {
        return "";
    }

    @Override
    String getBranchPrefix() {
        return getSettings().getReleasePrefix();
    }

    @Override
    String getDialogTitle() {
        return "Начать релиз";
    }

    @Override
    List<String> getStartBranchesNames() {
        return getGitFlowUtils().getBranchesForRelease();
    }
}