package ru.rambler.plugins.gitflow.utils.tags;

public class ReleaseTag implements Tag {
    public static final String RELEASE_TAG_TYPE = "release_tag";

    private int major;
    private int minor;
    private int patch;

    public ReleaseTag(String tagName) {
        String[] paramsArray = tagName.split("\\.");
        parseTagParams(paramsArray);
    }

    @Override
    public String getNextRcTagName() {
        return "" + major + "." + minor + "." + (patch + 1) + ".rc0";
    }

    @Override
    public String getTagType() {
        return RELEASE_TAG_TYPE;
    }

    @Override
    public String getMainPath() {
        return "" + major + "." + minor + "." + patch;
    }

    @Override
    public int getTagNumber() {
        return major * 10000 + minor * 100 + patch;
    }

    @Override
    public String toString() {
        return getMainPath();
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getPatch() {
        return patch;
    }

    void parseTagParams(String[] params) {
        major = Integer.parseInt(params[0]);
        minor = Integer.parseInt(params[1]);
        patch = Integer.parseInt(params[2]);
    }
}