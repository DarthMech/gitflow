package ru.rambler.plugins.gitflow.services.gitcommands;

import com.intellij.openapi.util.Key;
import git4idea.commands.*;
import git4idea.repo.GitRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExpansionGitImpl extends GitImpl implements ExpansionGit {
    /**
     * {@code git log -1 --pretty=format:"%H" &lt;branchName&gt;}
     */
    @NotNull
    @Override
    public GitCommandResult getLastCommitsForBranch(@NotNull GitRepository repository,
                                                    @NotNull String branchName,
                                                    @Nullable int count) {
        final GitLineHandler h = new GitLineHandler(repository.getProject(), repository.getRoot(), GitCommand.LOG);
        h.addParameters("-" + count);
        h.addParameters("--pretty=format:\"%H\"");
        h.addParameters(branchName);

        return runCommand(h);
    }

    @NotNull
    @Override
    public GitCommandResult getTags(@NotNull GitRepository repository,
                                    @Nullable String regex) {
        final GitLineHandler h = new GitLineHandler(repository.getProject(), repository.getRoot(), GitCommand.TAG);

        if (regex != null && !regex.isEmpty()) {
            h.addParameters("-l");
            h.addParameters(regex);
        }

        return runCommand(h);
    }

    @Override
    public GitCommandResult pushNewTag(@NotNull GitRepository repository,
                                       @NotNull String remote,
                                       @NotNull String tagName,
                                       @Nullable String comment,
                                       @Nullable String startPoint,
                                       boolean pushTag,
                                       boolean updateTracking,
                                       @Nullable GitLineHandlerListener... listeners) {
        GitCommandResult resultAddTag = createNewTag(repository,
                tagName,
                comment,
                startPoint,
                null);

        if (resultAddTag.success() && pushTag) {
            GitCommandResult resultPushTag = pushTag(repository,
                    remote,
                    tagName,
                    updateTracking,
                    listeners);

            return resultPushTag;
        } else {
            return resultAddTag;
        }
    }

    @NotNull
    @Override
    public GitCommandResult createNewTag(@NotNull GitRepository repository,
                                         @NotNull String tagName,
                                         @Nullable String comment,
                                         @Nullable String startPoint,
                                         @Nullable GitLineHandlerListener listeners) {
        final GitLineHandler h = new GitLineHandler(repository.getProject(), repository.getRoot(), GitCommand.TAG);
        h.setSilent(false);
        h.addParameters("-a");
        h.addParameters(tagName);

        h.addParameters("-m");
        if (comment != null && !comment.isEmpty()) {
            h.addParameters(comment);
        } else {
            h.addParameters(tagName);
        }

        if (startPoint != null && !startPoint.isEmpty()) {
            h.addParameters(startPoint);
        }

        return runCommand(h);
    }

    /**
     * {@code git push origin &lt;tagName&gt;}
     *
     * @param repository
     * @param remote
     * @param tagName
     * @param updateTracking
     * @param listeners
     * @return
     */
    @NotNull
    @Override
    public GitCommandResult pushTag(@NotNull GitRepository repository,
                                    @NotNull String remote,
                                    @NotNull String tagName,
                                    boolean updateTracking,
                                    @Nullable GitLineHandlerListener... listeners) {
        final GitLineHandler h = new GitLineHandler(repository.getProject(), repository.getRoot(), GitCommand.PUSH);
        h.addParameters(remote);
        h.addParameters(tagName);

        return runCommand(h);
    }

    @Override
    public GitCommandResult checkoutNewBranch(@NotNull GitRepository repository,
                                              @NotNull String branchName,
                                              @Nullable String startPoint,
                                              @Nullable GitLineHandlerListener listener) {
        final GitLineHandler h = new GitLineHandler(repository.getProject(), repository.getRoot(), GitCommand.CHECKOUT.readLockingCommand());
        h.setSilent(false);
        h.setStdoutSuppressed(false);
        h.addParameters("-b");
        h.addParameters(branchName);
        if (startPoint != null && !startPoint.isEmpty()) {
            h.addParameters(startPoint);
        }
        if (listener != null) {
            h.addLineListener(listener);
        }
        return runCommand(h);
    }

    @Override
    public GitCommandResult pushBranch(@NotNull GitRepository repository,
                                       @NotNull String remote,
                                       @NotNull String branchName,
                                       boolean updateTracking, // ?
                                       @Nullable GitLineHandlerListener... listeners) {
        final GitLineHandler h = new GitLineHandler(repository.getProject(), repository.getRoot(), GitCommand.PUSH);
        h.addParameters(remote);
        h.addParameters(branchName);

        return runCommand(h);
    }

    @Override
    public GitCommandResult status(@NotNull GitRepository repository) {
        final GitLineHandler h = new GitLineHandler(repository.getProject(), repository.getRoot(), GitCommand.STATUS);
        return runCommand(h);
    }

    @Override
    public GitCommandResult getNotPushInfo(@NotNull GitRepository repository,
                                           @NotNull String remote,
                                           @NotNull String branchName) {
        final GitLineHandler h = new GitLineHandler(repository.getProject(), repository.getRoot(), GitCommand.LOG);

        String logString = remote + "/" + branchName + ".." + branchName;

        h.addParameters(logString);
        //h.addParameters(branchName);

        return runCommand(h);
    }

    @Override
    public GitCommandResult mergeReleaseBranch(@NotNull GitRepository repository,
                                               @NotNull String releaseBranch,
                                               @NotNull String receiveBranch,
                                               boolean pushChange) {

        GitCommandResult res = checkout(repository, receiveBranch, null, true, false, new GitCommandEmptyListener());
        res = merge(repository, releaseBranch, null, new GitCommandEmptyListener());

        return res;
    }

    private static class GitCommandEmptyListener implements GitLineHandlerListener {

        @Override
        public void onLineAvailable(String s, Key key) {

        }

        @Override
        public void processTerminated(int i) {

        }

        @Override
        public void startFailed(Throwable throwable) {

        }
    }
}