package ru.rambler.plugins.gitflow.actions;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import git4idea.actions.GitAction;

import git4idea.branch.GitBranchUtil;
import git4idea.repo.GitRepository;
import org.jetbrains.annotations.NotNull;
import ru.rambler.plugins.gitflow.services.gitcommands.ExpansionGit;
import ru.rambler.plugins.gitflow.services.settings.GitFlowSettings;
import ru.rambler.plugins.gitflow.utils.GitFlowUtils;

public abstract class GitFlowGitAction extends GitAction {
    private static final String GIT_FLOW_NOTIFICATION_ERROR_ID = "GitFlowError";
    private static final String GIT_FLOW_NOTIFICATION_INFO_ID  = "GitFlowInfo";

    @NotNull
    private final ExpansionGit gitImpl;

    private GitFlowUtils gitFlowUtils;
    private GitRepository repo;

    public GitFlowGitAction(String label) {
        getTemplatePresentation().setText(label);
        gitImpl = ServiceManager.getService(ExpansionGit.class);
    }

    @Override
    public final void actionPerformed(AnActionEvent anActionEvent) {
        repo = GitBranchUtil.getCurrentRepository(anActionEvent.getProject());
        gitFlowUtils = new GitFlowUtils(repo);
        gitActionPerformed(anActionEvent);

        repo.update();
    }

    @Override
    protected final boolean isEnabled(@NotNull AnActionEvent anActionEvent) {
        repo = GitBranchUtil.getCurrentRepository(anActionEvent.getProject());
        gitFlowUtils = new GitFlowUtils(repo);
        return isGitActionEnabled(anActionEvent);
    }

    protected void showInfoMessage(@NotNull String message, @NotNull String title, @NotNull Project project) {
        showMessage(message, title, GIT_FLOW_NOTIFICATION_INFO_ID, NotificationType.INFORMATION, project);
    }

    protected void showErrorMessage(@NotNull String message, @NotNull String title, @NotNull Project project) {
        showMessage(message, title, GIT_FLOW_NOTIFICATION_ERROR_ID, NotificationType.ERROR, project);
    }

    protected abstract void gitActionPerformed(@NotNull AnActionEvent anActionEvent);
    protected abstract boolean isGitActionEnabled(@NotNull AnActionEvent anActionEvent);

    // ----------------------------------------------------------------------------------------------------
    protected ExpansionGit getGit() {
        return gitImpl;
    }

    protected GitRepository getRepo() {
        return repo;
    }

    protected GitFlowUtils getGitFlowUtils() {
        return gitFlowUtils;
    }

    protected GitFlowSettings getSettings() {
        return gitFlowUtils.getService().getState();
    }

    // ------------------------------------------- inner methods ------------------------------------------------------
    private void showMessage(@NotNull String message,
                             @NotNull String title,
                             @NotNull String groupDisplayId,
                             @NotNull NotificationType type,
                             @NotNull Project project) {
        final Notification notification = new Notification(groupDisplayId, title, message, type);
        Notifications.Bus.notify(notification, project);
    }
}