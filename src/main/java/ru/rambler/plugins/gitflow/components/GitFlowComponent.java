package ru.rambler.plugins.gitflow.components;

import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import org.jetbrains.annotations.NotNull;
import ru.rambler.plugins.gitflow.GitFlowStatusBarWidget;

public class GitFlowComponent implements ProjectComponent {
    private Project project;
    private GitFlowStatusBarWidget gitflowWidget;

    public GitFlowComponent(Project project) {
        this.project = project;
    }

    // ---------------------------------------- ProjectComponent ------------------------------------------------------
    @Override
    public void initComponent() {
    }

    @Override
    public void disposeComponent() {
        // TODO: insert component disposal logic here
    }

    @NotNull
    @Override
    public String getComponentName() {
        return GitFlowComponent.class.getName();
    }

    @Override
    public void projectOpened() {
        gitflowWidget = new GitFlowStatusBarWidget(project);
        StatusBar statusBar = WindowManager.getInstance().getStatusBar(project);
        if (statusBar != null) {
            statusBar.addWidget(gitflowWidget,
                    "before ReadOnlyAttribute",
                    project);

            gitflowWidget.subscribeToRepoChangeEvents(project);
        }
    }

    @Override
    public void projectClosed() {
    }
}