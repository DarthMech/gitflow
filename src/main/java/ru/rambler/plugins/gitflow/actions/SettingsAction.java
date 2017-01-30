package ru.rambler.plugins.gitflow.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogBuilder;
import ru.rambler.plugins.gitflow.services.settings.GitFlowSettingsService;
import ru.rambler.plugins.gitflow.view.SettingsView;

public class SettingsAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        SettingsView view = new SettingsView();

        Project prj = anActionEvent.getProject();
        GitFlowSettingsService service = ServiceManager.getService(prj, GitFlowSettingsService.class);
        view.setSettings(service.getState());

        DialogBuilder builder = new DialogBuilder();
        builder.setTitle("Настройки");
        builder.setCenterPanel(view.getMainPanel());

        int exitCode = builder.show();
        if (exitCode == 0) {
            service.loadState(view.getSettings());
        }
    }
}