package ru.rambler.plugins.gitflow.services.settings;

import com.intellij.lifecycle.PeriodicalTasksCloser;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;

@State(name = "GitFlowSettings", storages = {@Storage(StoragePathMacros.WORKSPACE_FILE)})
public class GitFlowSettingsService implements PersistentStateComponent<GitFlowSettings> {

    private GitFlowSettings settings = new GitFlowSettings();

    public static GitFlowSettingsService getInstance(Project project) {
        return PeriodicalTasksCloser.getInstance().safeGetService(project, GitFlowSettingsService.class);
    }

    @Nullable
    @Override
    public GitFlowSettings getState() {
        return settings;
    }

    @Override
    public void loadState(GitFlowSettings state) {
        settings = state;
    }
}