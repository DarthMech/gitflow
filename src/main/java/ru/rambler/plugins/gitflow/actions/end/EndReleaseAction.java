package ru.rambler.plugins.gitflow.actions.end;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import git4idea.commands.GitCommandResult;
import git4idea.repo.GitRepository;
import org.jetbrains.annotations.NotNull;
import ru.rambler.plugins.gitflow.utils.tags.Tag;
import ru.rambler.plugins.gitflow.utils.tags.TagBuilder;
import ru.rambler.plugins.gitflow.view.EndReleaseView;

import java.util.List;

public class EndReleaseAction extends AbstractEndAction {

    public EndReleaseAction() {
        super("End release");
    }

    @Override
    public void gitActionPerformed(AnActionEvent anActionEvent) {
        List<String> releaseBranchesList = getBranchesNames();

        if (releaseBranchesList == null || releaseBranchesList.isEmpty()) {
            showErrorMessage("Нет подходяящих веток", "Список веток пуст", anActionEvent.getProject());
            return;
        }

        EndReleaseView.EndReleaseData data = showEndReleaseDialog(releaseBranchesList);

        if (data != null) {
            acceptEndReleaseAction(data, anActionEvent.getProject());
        }
    }

    @Override
    protected boolean isGitActionEnabled(@NotNull AnActionEvent event) {
        List<String> releaseBranchesList = getGitFlowUtils().getReleaseBranchesNames();
        return !(releaseBranchesList == null || releaseBranchesList.isEmpty());
    }

    @Override
    String getDialogTitle() {
        return "Завершить релиз";
    }

    @Override
    List<String> getBranchesNames() {
        return getGitFlowUtils().getReleaseBranchesNames();
    }

    // ---------------------------------------------------------------------------------------------
    private EndReleaseView.EndReleaseData showEndReleaseDialog(List<String> releaseBranchesList) {
        EndReleaseView dialog = new EndReleaseView();

        Tag master = getLastMasterTag();
        dialog.setBranchesNames(releaseBranchesList);
        dialog.setMasterTag(master);

        dialog.show();

        int exitCode = dialog.getExitCode();
        if (exitCode == 0) {
            return dialog.getDataView();
        } else {
            return null;
        }
    }

    private Tag getLastMasterTag() {
        GitRepository repo = getRepo();

        GitCommandResult resultTagLists = getGit().getTags(repo, null);

        Tag tag = null;
        if (resultTagLists.success()) {
            List<String> tagsList = resultTagLists.getOutput();

            String[] tagsArray = new String[tagsList.size()];
            tagsList.toArray(tagsArray);

            int index = tagsArray.length - 1;

            do {
                tag = TagBuilder.getTag(tagsArray[index]);
                index--;
            } while (tag == null && index > -1);
        }

        return tag;
    }

    private void acceptEndReleaseAction(EndReleaseView.EndReleaseData data, Project project) {
        String devBranch = getGitFlowUtils().getDevBranchName();
        GitCommandResult devMergeRes = getGit().mergeReleaseBranch(getRepo(), data.getReleaseBranchName(), devBranch, data.isPushRemote());

        String masterBranch = getGitFlowUtils().getMasterBranchName();
        GitCommandResult masterMergeRes =  getGit().mergeReleaseBranch(getRepo(), data.getReleaseBranchName(), masterBranch, data.isPushRemote());

        GitCommandResult status = getGit().status(getRepo());

        if (masterMergeRes.success()) {
            GitCommandResult resultPushTag = getGit().pushNewTag(getRepo(),
                    getSettings().getRepoName(),
                    data.getTagName(),
                    null,
                    null,
                    data.isPushRemote(),
                    true,
                    null);

            if (data.isDeleteBranch()) {
                GitCommandResult deleteRes = getGit().branchDelete(getRepo(), data.getReleaseBranchName(), false);
            }
        } else {
            showErrorMessage("Не получилось смержить релиз в мастер", "Ошибка мержа", project);
        }
    }

//    public static void showEditorHint(final String info, final Editor editor) {
//        final JLabel label = new JLabel(info);
//        label.setBorder(BorderFactory.createCompoundBorder(
//                BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.WHITE, Gray._128),
//                BorderFactory.createEmptyBorder(3, 5, 3, 5)));
//        label.setForeground(JBColor.foreground());
//        label.setBackground(HintUtil.INFORMATION_COLOR);
//        label.setOpaque(true);
//        label.setFont(label.getFont().deriveFont(Font.BOLD));
//
//        final LightweightHint h = new LightweightHint(label);
//        final Point point = editor.visualPositionToXY(editor.getCaretModel().getVisualPosition());
//        SwingUtilities.convertPointToScreen(point, editor.getContentComponent());
//
//        /* === HintManager API Info ===
//
//            public void showEditorHint(final LightweightHint hint,
//                                        final Editor editor,
//                                        Point p,
//                                        int flags,
//                                        int timeout,
//                                        boolean reviveOnEditorChange)
//
//
//            reviveOnEditorChange means hint should stay even if active editor have been changed. It's should rarely be true.
//
//            possible flags are:
//                public static final int HIDE_BY_ESCAPE = 0x01;
//                public static final int HIDE_BY_ANY_KEY = 0x02;
//                public static final int HIDE_BY_LOOKUP_ITEM_CHANGE = 0x04;
//                public static final int HIDE_BY_TEXT_CHANGE = 0x08;
//                public static final int HIDE_BY_OTHER_HINT = 0x10;
//                public static final int HIDE_BY_SCROLLING = 0x20;
//                public static final int HIDE_IF_OUT_OF_EDITOR = 0x40;
//                public static final int UPDATE_BY_SCROLLING = 0x80;
//        */
//        final int flags = HintManager.HIDE_BY_ANY_KEY | HintManager.HIDE_BY_SCROLLING;
////        HintManagerImpl.getInstanceImpl().showEditorHint(h, editor, point, flags, 0, false);
//        HintManagerImpl.getInstanceImpl().showErrorHint(editor, "sdfdsagfasd");
//
////        Notifications.Bus.notify();
//    }
}