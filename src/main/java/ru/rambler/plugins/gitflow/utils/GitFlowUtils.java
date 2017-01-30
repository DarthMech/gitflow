package ru.rambler.plugins.gitflow.utils;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.vcs.log.Hash;
import git4idea.GitLocalBranch;
import git4idea.repo.GitRepository;
import ru.rambler.plugins.gitflow.services.settings.GitFlowSettings;
import ru.rambler.plugins.gitflow.services.settings.GitFlowSettingsService;
import ru.rambler.plugins.gitflow.utils.tags.ReleaseTag;
import ru.rambler.plugins.gitflow.utils.tags.Tag;
import ru.rambler.plugins.gitflow.utils.tags.TagBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GitFlowUtils {

    private GitFlowSettingsService service;
    private GitRepository repo;

    public GitFlowUtils(GitRepository repo) {
        this.repo = repo;
        service = ServiceManager.getService(repo.getProject(), GitFlowSettingsService.class);
    }

    public GitFlowUtils(GitRepository repo, GitFlowSettingsService service) {
        this.repo = repo;
        this.service = service;
    }

    public GitFlowSettingsService getService() {
        return service;
    }

    // --------------------------------------------------------------------------------
    public String getMasterBranchName() {
        return getService().getState().getMasterBranchName();
    }

    public String getDevBranchName() {
        return getService().getState().getDevBranchName();
    }

    /**
     * Возвращает true, если есть хоть одна релизная ветка
     * @return
     */
    public boolean isReleaseBranchExist() {
        Map<GitLocalBranch, Hash> localBranches = repo.getInfo().getLocalBranchesWithHashes();

        GitFlowSettings settings = service.getState();

        for (GitLocalBranch branch : localBranches.keySet()) {
            if (branch.getName().startsWith(settings.releasePrefix)) {
                return true;
            }
        }

        return false;
    }

    // --------------------------------------------------------------------------------
    public List<String> getReleaseBranchesNames() {
        GitFlowSettings settings = service.getState();
        return getBranchesNamesByPrefix(settings.getReleasePrefix());
    }

    public List<String> getFeatureBranchesNames() {
        GitFlowSettings settings = service.getState();
        return getBranchesNamesByPrefix(settings.getFeaturePrefix());
    }

    public List<String> getHotfixBranchesNames() {
        GitFlowSettings settings = service.getState();
        return getBranchesNamesByPrefix(settings.getHotfixPrefix());
    }

    // ----------------------------------------------------------------------------------------------------------------
    /**
     * Может быть dev или рилизные ветки или фичи
     * @return
     */
    public List<String> getBranchesForFeature() {
        List<String> branchesNames = new ArrayList<>();

        Map<GitLocalBranch, Hash> localBranches = repo.getInfo().getLocalBranchesWithHashes();
        GitFlowSettings settings = service.getState();

        // первой найдем ветку dev чтобы она выдавлась первой:
        for (GitLocalBranch branch : localBranches.keySet()) {
            if (branch.getName().startsWith(settings.getDevBranchName())) {
                branchesNames.add(branch.getName());
                break;
            }
        }

        // теперь найдем и добавим остальные.
        for (GitLocalBranch branch : localBranches.keySet()) {
            String branchName = branch.getName();
            if (branchName.startsWith(settings.getReleasePrefix()) ||
                    branchName.startsWith(settings.getFeaturePrefix())) {
                branchesNames.add(branch.getName());
            }
        }

        return branchesNames;
    }

    /**
     * Релизы мы делаем только от веток dev
     * @return
     */
    public List<String> getBranchesForRelease() {
        List<String> branchesNames = new ArrayList<>();

        Map<GitLocalBranch, Hash> localBranches = repo.getInfo().getLocalBranchesWithHashes();

        GitFlowSettings settings = service.getState();

        for (GitLocalBranch branch : localBranches.keySet()) {
            if (branch.getName().startsWith(settings.getDevBranchName())) {
                branchesNames.add(branch.getName());
            }
        }

        return branchesNames;
    }

    /**
     * Хотфиксы делаем для мастер и релиз веток
     *
     * Мастер ветку поставим первой
     * @return
     */
    public List<String> getBranchesForHotfix() {
        List<String> branchesNames = new ArrayList<>();

        Map<GitLocalBranch, Hash> localBranches = repo.getInfo().getLocalBranchesWithHashes();

        GitFlowSettings settings = service.getState();

        // первой найдем ветку master чтобы она выдавлась первой:
        for (GitLocalBranch branch : localBranches.keySet()) {
            if (branch.getName().startsWith(settings.getMasterBranchName())) {
                branchesNames.add(branch.getName());
                break;
            }
        }

        for (GitLocalBranch branch : localBranches.keySet()) {
            String branchName = branch.getName();
            if (branchName.startsWith(settings.getReleasePrefix())) {
                branchesNames.add(branch.getName());
            }
        }

        return branchesNames;
    }

    public Tag getLastTag(List<String> tagsList) {
        if (tagsList.isEmpty()) {
            return null;
        }

        Tag tagWithParam = null;

        String[] tagsArray = new String[tagsList.size()];
        tagsList.toArray(tagsArray);

        int index = tagsArray.length - 1;

        do {
            tagWithParam = TagBuilder.getTag(tagsArray[index]);
            index--;
        } while (tagWithParam == null && index > -1);

        if (tagWithParam != null) {
            if (tagWithParam.getTagType().equals(ReleaseTag.RELEASE_TAG_TYPE)) {
                return tagWithParam;
            }

            Tag releaseTag = getLastReleaseTag(tagsArray, index, tagWithParam);

            if (releaseTag == null) {
                return tagWithParam;
            } else {
                return releaseTag;
            }
        }

        return null;
    }

    // ----------------------------------------------------------------------------------------------------------------
    private Tag getLastReleaseTag(String[] tagsArray, int beginIndex, Tag expectedTag) {
        Tag newerTag = null;
        Tag tagWithMaxNumber = expectedTag;

        for (int i = beginIndex; i > -1; i--) {
            Tag currentTag = TagBuilder.getTag(tagsArray[i]);

            if (currentTag != null) {
                String expectedTagName = expectedTag.getMainPath();

                // главный тег
                if (currentTag.getTagType().equals(ReleaseTag.RELEASE_TAG_TYPE) &&
                        currentTag.getMainPath().equals(expectedTagName)) {
                    newerTag = currentTag;
                }

                // возможно попадется тег по номеру больший текущего
                if (currentTag.getTagNumber() > tagWithMaxNumber.getTagNumber()) {
                    newerTag = currentTag;
                    tagWithMaxNumber = currentTag;
                }

                // пора выходить!
                if (!currentTag.getMainPath().equals(expectedTagName)) {
                    break;
                }
            }
        }

        return newerTag;
    }

    private List<String> getBranchesNamesByPrefix(String prefix) {
        List<String> branchesNames = new ArrayList<>();

        Map<GitLocalBranch, Hash> localBranches = repo.getInfo().getLocalBranchesWithHashes();

        for (GitLocalBranch branch : localBranches.keySet()) {
            if (branch.getName().startsWith(prefix)) {
                branchesNames.add(branch.getName());
            }
        }

        return branchesNames;
    }
}