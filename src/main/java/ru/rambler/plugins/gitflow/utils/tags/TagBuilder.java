package ru.rambler.plugins.gitflow.utils.tags;

import org.jetbrains.annotations.Nullable;

public class TagBuilder {
    public static final String RELEASE_TAG_REGEX = "^\\d+\\.\\d+\\.\\d+$";
    public static final String RC_TAG_REGEX = "^\\d+\\.\\d+\\.\\d+\\.rc\\d+$";

    public static boolean isReleaseTag(String tagName) {
        return tagName.matches(RELEASE_TAG_REGEX);
    }

    public static boolean isRcTag(String tagName) {
        return tagName.matches(RC_TAG_REGEX);
    }

    @Nullable
    public static Tag getTag(String tagName) {
        if (isReleaseTag(tagName)) {
            return new ReleaseTag(tagName);
        }

        if (isRcTag(tagName)) {
            return new RcTag(tagName);
        }

        return null;
    }
}