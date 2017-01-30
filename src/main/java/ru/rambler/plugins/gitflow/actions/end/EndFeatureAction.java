package ru.rambler.plugins.gitflow.actions.end;

import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EndFeatureAction extends AbstractEndAction {

    public EndFeatureAction() {
        super("End feature");
    }

    @Override
    public void gitActionPerformed(AnActionEvent anActionEvent) {
        super.gitActionPerformed(anActionEvent);
    }

    @Override
    protected boolean isGitActionEnabled(@NotNull AnActionEvent event) {
        List<String> releaseBranchesList = getGitFlowUtils().getFeatureBranchesNames();
        return !(releaseBranchesList == null || releaseBranchesList.isEmpty());
    }

    @Override
    String getDialogTitle() {
        return "Завершить разработку";
    }

    @Override
    List<String> getBranchesNames() {
        return getGitFlowUtils().getFeatureBranchesNames();
    }
}