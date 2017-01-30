package ru.rambler.plugins.gitflow.services.settings;

import org.jetbrains.annotations.NotNull;
import ru.rambler.plugins.gitflow.utils.GitFlowConst;

public class GitFlowSettings {
    @NotNull
    public String featurePrefix;
    @NotNull
    public String releasePrefix;
    @NotNull
    public String hotfixPrefix;

    @NotNull
    public String masterBranchName;
    @NotNull
    public String devBranchName;
    @NotNull
    public String repoName;

    public GitFlowSettings() {
        featurePrefix = GitFlowConst.FEATURE_PREFIX;
        releasePrefix = GitFlowConst.RELEASE_PREFIX;
        hotfixPrefix = GitFlowConst.HOTFIX_PREFIX;

        masterBranchName = GitFlowConst.MASTER_BRANCH_NAME;
        devBranchName = GitFlowConst.DEV_BRANCH_NAME;

        repoName = GitFlowConst.ORIGIN_NAME;
    }

    public GitFlowSettings(@NotNull String featurePrefix,
                           @NotNull String releasePrefix,
                           @NotNull String hotfixPrefix,
                           @NotNull String masterBranchName,
                           @NotNull String devBranchName,
                           @NotNull String repoName) {
        this.featurePrefix = featurePrefix;
        this.releasePrefix = releasePrefix;
        this.hotfixPrefix = hotfixPrefix;
        this.masterBranchName = masterBranchName;
        this.devBranchName = devBranchName;
        this.repoName = repoName;
    }

    @NotNull
    public String getFeaturePrefix() {
        return featurePrefix;
    }

    @NotNull
    public String getReleasePrefix() {
        return releasePrefix;
    }

    @NotNull
    public String getHotfixPrefix() {
        return hotfixPrefix;
    }

    @NotNull
    public String getMasterBranchName() {
        return masterBranchName;
    }

    @NotNull
    public String getDevBranchName() {
        return devBranchName;
    }

    @NotNull
    public String getRepoName() {
        return repoName;
    }
}