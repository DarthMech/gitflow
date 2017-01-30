package ru.rambler.plugins.gitflow.services.gitcommands;

import git4idea.commands.Git;
import git4idea.commands.GitCommandResult;
import git4idea.commands.GitLineHandlerListener;
import git4idea.repo.GitRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ExpansionGit extends Git {
    GitCommandResult getLastCommitsForBranch(@NotNull GitRepository repository,
                                             @NotNull String branchName,
                                             @Nullable int count);

    GitCommandResult getTags(@NotNull GitRepository repository,
                             @Nullable String regex);

    GitCommandResult pushNewTag(@NotNull GitRepository repository,
                                @NotNull String remote,
                                @NotNull String tagName,
                                @Nullable String comment,
                                @Nullable String startPoint,
                                boolean pushTag,
                                boolean updateTracking, // ?
                                @Nullable GitLineHandlerListener... listeners);

    GitCommandResult createNewTag(@NotNull GitRepository repository,
                                  @NotNull String tagName,
                                  @Nullable String comment,
                                  @Nullable String startPoint,
                                  @Nullable GitLineHandlerListener listener);

    GitCommandResult pushTag(@NotNull GitRepository repository,
                             @NotNull String remote,
                             @NotNull String tagName,
                             boolean updateTracking, // ?
                             @Nullable GitLineHandlerListener... listeners);

    GitCommandResult checkoutNewBranch(@NotNull GitRepository repository,
                                       @NotNull String branchName,
                                       @Nullable String startPoint,
                                       @Nullable GitLineHandlerListener listener);

    GitCommandResult pushBranch(@NotNull GitRepository repository,
                                @NotNull String remote,
                                @NotNull String branchName,
                                boolean updateTracking, // ?
                                @Nullable GitLineHandlerListener... listeners);

    GitCommandResult status(@NotNull GitRepository repository);

    GitCommandResult getNotPushInfo(@NotNull GitRepository repository,
                                    @NotNull String remote,
                                    @NotNull String branchName);

    GitCommandResult mergeReleaseBranch(@NotNull GitRepository repository,
                                        @NotNull String releaseBranch,
                                        @NotNull String receiveBranch,
                                        boolean pushChange);
}