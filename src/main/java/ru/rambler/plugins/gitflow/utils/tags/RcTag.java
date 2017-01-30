package ru.rambler.plugins.gitflow.utils.tags;

public class RcTag extends ReleaseTag {
    public static final String RC_TAG_TYPE = "rc_tag";

    private int rc;

    public RcTag(String tagName) {
        super(tagName);
    }

    @Override
    public String getNextRcTagName() {
        return "" + getMajor() + "." + getMinor() + "." + getPatch() + ".rc" + (rc + 1);
    }

    @Override
    public String getTagType() {
        return RC_TAG_TYPE;
    }

    @Override
    public int getTagNumber() {
        return super.getTagNumber() * 100 + rc;
    }

    @Override
    public String toString() {
        return "" + getMajor() + "." + getMinor() + "." + getPatch() + ".rc" + getRc();
    }

    public int getRc() {
        return rc;
    }

    @Override
    protected void parseTagParams(String[] params) {
        super.parseTagParams(params);
        rc = Integer.parseInt(params[3].replace("rc", ""));
    }
}
