package ru.rambler.plugins.gitflow;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.impl.SimpleDataContext;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.ListPopup;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.ui.popup.PopupFactoryImpl;
import com.intellij.util.Consumer;
import git4idea.GitLocalBranch;
import git4idea.branch.GitBranchUtil;
import git4idea.repo.GitRepository;
import org.jetbrains.annotations.CalledInAwt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.rambler.plugins.gitflow.actions.*;
import ru.rambler.plugins.gitflow.actions.end.EndFeatureAction;
import ru.rambler.plugins.gitflow.actions.end.EndHotfixAction;
import ru.rambler.plugins.gitflow.actions.end.EndReleaseAction;
import ru.rambler.plugins.gitflow.actions.start.StartFeatureAction;
import ru.rambler.plugins.gitflow.actions.start.StartHotfixAction;
import ru.rambler.plugins.gitflow.actions.start.StartReleaseAction;
import ru.rambler.plugins.gitflow.utils.GitFlowConst;

import java.awt.event.MouseEvent;

public class GitFlowStatusBarWidget implements StatusBarWidget {
    private GitFlowWidgetPresentation presentation;
    private Project project;
    private GitRepository repo;

    private StatusBar statusBar;

    private String labelText = "Git flow";
    private String toolTip = "Плагинчег";

    public GitFlowStatusBarWidget(Project project) {
        this.project = project;
        repo = GitBranchUtil.getCurrentRepository(project);
    }

    @NotNull
    @Override
    public String ID() {
        return GitFlowStatusBarWidget.class.getName();
    }

    @Nullable
    @Override
    public WidgetPresentation getPresentation(@NotNull PlatformType platformType) {
        if (presentation == null) {
            presentation = new GitFlowWidgetPresentation();
        }

        return presentation;
    }

    @Override
    public void install(@NotNull StatusBar statusBar) {
        this.statusBar = statusBar;
    }

    @Override
    public void dispose() {

    }

    public void subscribeToRepoChangeEvents(@NotNull Project project) {
        project.getMessageBus().connect().subscribe(GitRepository.GIT_REPO_CHANGE, repository -> {
            updateLater();
        });
    }

    // ------------------------------------------------------------------------------------------------
    private static String getLabelText(String branchName) {
        if (branchName.startsWith(GitFlowConst.FEATURE_PREFIX)) {
            return "Feature";
        } else if (branchName.startsWith(GitFlowConst.RELEASE_PREFIX)) {
            return "Release";
        } else if (branchName.startsWith(GitFlowConst.HOTFIX_PREFIX)) {
            return "Hotfix";
        } else if (branchName.startsWith(GitFlowConst.DEV_BRANCH_NAME)) {
            return "Dev";
        } else if (branchName.startsWith(GitFlowConst.MASTER_BRANCH_NAME)) {
            return "Master";
        }

        return "Git flow";
    }

    private void updateLater() {
        ApplicationManager.getApplication().invokeLater(() -> {
            update();
        });
    }

    @CalledInAwt
    private void update() {
        repo = GitBranchUtil.getCurrentRepository(project);

        if (repo == null) {
            labelText = "Git flow";
        } else {
            GitLocalBranch branch = repo.getCurrentBranch();
            labelText = getLabelText(branch.getName());
        }

        if (statusBar != null) {
            statusBar.updateWidget(ID());
//            statusBar.updateWidget(GitBranchWidget.class.getName());
        }
    }

    private ListPopup getWidgetPopupStep() {
        ListPopup list = new PopupFactoryImpl.ActionGroupPopup("Menu",
                getActions(),
                SimpleDataContext.getProjectContext(project),
                false, false, true, true, null, -1, null, null);

        return list;
    }

    private ActionGroup getActions() {
        DefaultActionGroup group = new DefaultActionGroup();
        group.addSeparator("Feature");
        group.add(new StartFeatureAction());
        group.add(new EndFeatureAction());
        group.addSeparator("Release");
        group.add(new StartReleaseAction());
        group.add(new EndReleaseAction());
        group.addSeparator("Hotfix");
        group.add(new StartHotfixAction());
        group.add(new EndHotfixAction());
        group.addSeparator("Build");
        group.add(new BuildNextRCAction());

        return group;
    }

    private class GitFlowWidgetPresentation implements MultipleTextValuesPresentation {
        @Nullable
        @Override
        public ListPopup getPopupStep() {
            return getWidgetPopupStep();
        }

        @Nullable
        @Override
        public String getSelectedValue() {
            return labelText;
        }

        @NotNull
        @Override
        public String getMaxValue() {
            return "";
        }

        @Nullable
        @Override
        public String getTooltipText() {
            return toolTip;
        }

        @Nullable
        @Override
        public Consumer<MouseEvent> getClickConsumer() {
            return null;
        }
    }
}