package ru.rambler.plugins.gitflow.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.rambler.plugins.gitflow.utils.tags.Tag;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNull;


public class GitFlowUtilsTest {
    GitFlowUtils utils;

    @Before
    public void init() {
        utils = new GitFlowUtils(null, null);
    }

    @Test
    public void testGetLastRCTag_emty() throws Exception {
        Tag tag = utils.getLastTag(new ArrayList<>());

        assertNull(tag);
    }

    @Test
    public void testGetLastRCTag_1() throws Exception {
        Tag tag = utils.getLastTag(getTagsList1());

        Assert.assertEquals("2.0.0.rc5", tag.toString());
        Assert.assertEquals("2.0.0.rc6", tag.getNextRcTagName());
    }

    @Test
    public void testGetLastRCTag_2() throws Exception {
        Tag tag = utils.getLastTag(getTagsList2());

        Assert.assertEquals("2.0.0", tag.toString());
        Assert.assertEquals("2.0.1.rc0", tag.getNextRcTagName());
    }

    @Test
    public void testGetLastRCTag_3() throws Exception {
        Tag tag = utils.getLastTag(getTagsList3());

        Assert.assertEquals("9999999.9999999.9999999", tag.toString());
        Assert.assertEquals("9999999.9999999.10000000.rc0", tag.getNextRcTagName());
    }

    @Test
    public void testGetLastRCTag_4() throws Exception {
        Tag tag = utils.getLastTag(getTagsList4());

        Assert.assertEquals("2.3.3", tag.toString());
        Assert.assertEquals("2.3.4.rc0", tag.getNextRcTagName());
    }

    @Test
    public void testGetLastRCTag_5() throws Exception {
        Tag tag = utils.getLastTag(getTagsList5());

        Assert.assertEquals("2.3.3.rc20", tag.toString());
        Assert.assertEquals("2.3.3.rc21", tag.getNextRcTagName());
    }

    @Test
    public void testGetLastRCTag_6() throws Exception {
        Tag tag = utils.getLastTag(getTagsList6());

        Assert.assertEquals("4.4.444", tag.toString());
        Assert.assertEquals("4.4.445.rc0", tag.getNextRcTagName());
    }

    @Test
    public void testGetLastRCTag_7() throws Exception {
        Tag tag = utils.getLastTag(getTagsList7());

        assertNull(tag);
    }
    // -------------------------------------------------------------------------------------------------------
    private List<String> getTagsList1() {
        List<String> tagsNames = new ArrayList<>();
        tagsNames.add("1.0.0");
        tagsNames.add("1.0.0.rc0");
        tagsNames.add("1.0.0.rc1");
        tagsNames.add("1.0.0.rc2");
        tagsNames.add("1.0.0.rc3");
        tagsNames.add("1.0.1.");
        tagsNames.add("1.0.1.rc0");
        tagsNames.add("2.0.0.");
        tagsNames.add("2.0.0.rc0");
        tagsNames.add("2.0.0.rc1");
        tagsNames.add("2.0.0.rc2");
        tagsNames.add("2.0.0.rc3");
        tagsNames.add("2.0.0.rc4");
        tagsNames.add("2.0.0.rc5");

        return tagsNames;
    }

    private List<String> getTagsList2() {
        List<String> tagsNames = new ArrayList<>();
        tagsNames.add("1.0.0");
        tagsNames.add("1.0.0.rc0");
        tagsNames.add("1.0.0.rc1");
        tagsNames.add("1.0.0.rc2");
        tagsNames.add("1.0.0.rc3");
        tagsNames.add("1.0.1.");
        tagsNames.add("1.0.1.rc0");
        tagsNames.add("2.0.0");
        tagsNames.add("2.0.0.rc0");
        tagsNames.add("2.0.0.rc1");
        tagsNames.add("2.0.0.rc2");
        tagsNames.add("2.0.0.rc3");
        tagsNames.add("2.0.0.rc4");
        tagsNames.add("2.0.0.rc5");

        return tagsNames;
    }

    private List<String> getTagsList3() {
        List<String> tagsNames = new ArrayList<>();

        tagsNames.add("0.0.0");
        tagsNames.add("0.0.1");
        tagsNames.add("3.2.1");
        tagsNames.add("1.2.3");
        tagsNames.add("333.222.111");
        tagsNames.add("3.2000.10000");
        tagsNames.add("30000.0.0");
        tagsNames.add("9999999.9999999.9999999");

        return tagsNames;
    }

    private List<String> getTagsList4() {
        List<String> tagsNames = new ArrayList<>();

        tagsNames.add("2.2.2");
        tagsNames.add("2.2.2.rc0");
        tagsNames.add("2.2.2.rc1");
        tagsNames.add("2.3.3");
        tagsNames.add("2.3.3.rc0");
        tagsNames.add("2.3.3.rc1");
        tagsNames.add("2.3.3.rc10");
        tagsNames.add("2.3.3.rc2");
        tagsNames.add("2.3.3.rc20");
        tagsNames.add("2.3.3.rc3");

        return tagsNames;
    }

    private List<String> getTagsList5() {
        List<String> tagsNames = new ArrayList<>();

        tagsNames.add("2.2.2");
        tagsNames.add("2.2.2.rc0");
        tagsNames.add("2.2.2.rc1");
        tagsNames.add("2.3.3.rc0");
        tagsNames.add("2.3.3.rc1");
        tagsNames.add("2.3.3.rc10");
        tagsNames.add("2.3.3.rc2");
        tagsNames.add("2.3.3.rc20");
        tagsNames.add("2.3.3.rc3");

        return tagsNames;
    }

    private List<String> getTagsList6() {
        List<String> tagsNames = new ArrayList<>();
        tagsNames.add("1.0.0");
        tagsNames.add("1.0.0.rc0");
        tagsNames.add("1.0.0.rc1");
        tagsNames.add("1.0.0.rc2");
        tagsNames.add("1.0.0.rc3");
        tagsNames.add("1.0.1.");
        tagsNames.add("1.0.1.rc0");
        tagsNames.add("2.0.0.");
        tagsNames.add("2.0.0.rc0");
        tagsNames.add("2.0.0.rc1");
        tagsNames.add("2.0.0.rc2");
        tagsNames.add("2.0.0.rc3");
        tagsNames.add("2.0.0.rc4");
        tagsNames.add("2.0.0.rc5");
        tagsNames.add("4.4.444");

        return tagsNames;
    }

    private List<String> getTagsList7() {
        List<String> tagsNames = new ArrayList<>();

        tagsNames.add("0.0");
        tagsNames.add("dsfgfd.dfgh.fgh");
        tagsNames.add("o.o.o");
        tagsNames.add(".rc6");
        tagsNames.add("333.222.rc111");
        tagsNames.add("r3.2000.10000");
        tagsNames.add("0");
        tagsNames.add("9999999.9999999.9999999.rc");
        tagsNames.add("qwerty");

        return tagsNames;
    }
}