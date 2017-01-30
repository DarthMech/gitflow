package ru.rambler.plugins.gitflow.utils.tags;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TagBuilderTest {

    @Test
    public void testIsRCTag_1() throws Exception {
        boolean res = TagBuilder.isRcTag("3.2.1.rc0");

        assertTrue(res);
    }

    @Test
    public void testIsReleaseTag_1() throws Exception {
        boolean res = TagBuilder.isReleaseTag("3.2.1.rc0");

        assertFalse(res);
    }

    @Test
    public void testIsRCTag_2() throws Exception {
        boolean res = TagBuilder.isRcTag("3.2.1");

        assertFalse(res);
    }

    @Test
    public void testIsReleaseTag_2() throws Exception {
        boolean res = TagBuilder.isReleaseTag("3.2.1");

        assertTrue(res);
    }

    @Test
    public void testIsRCTag_3() throws Exception {
        boolean res = TagBuilder.isRcTag("3.2.1.rc8888");

        assertTrue(res);
    }

    @Test
    public void testIsReleaseTag_3() throws Exception {
        boolean res = TagBuilder.isReleaseTag("3.2.1.rc8888");

        assertFalse(res);
    }

    @Test
    public void testIsRCTag_4() throws Exception {
        boolean res = TagBuilder.isRcTag("3.2.1.rc8888r");

        assertFalse(res);
    }

    @Test
    public void testIsReleaseTag_4() throws Exception {
        boolean res = TagBuilder.isReleaseTag("3.2.1.rc8888r");

        assertFalse(res);
    }

    @Test
    public void testIsRCTag_5() throws Exception {
        boolean res = TagBuilder.isRcTag("99999.99999.99999");

        assertFalse(res);
    }

    @Test
    public void testIsReleaseTag_5() throws Exception {
        boolean res = TagBuilder.isReleaseTag("99999.99999.99999");

        assertTrue(res);
    }

    @Test
    public void testIsRCTag_6() throws Exception {
        boolean res = TagBuilder.isRcTag("3.2.");

        assertFalse(res);
    }

    @Test
    public void testIsReleaseTag_6() throws Exception {
        boolean res = TagBuilder.isReleaseTag("3.2.");

        assertFalse(res);
    }

    @Test
    public void testIsRCTag_7() throws Exception {
        boolean res = TagBuilder.isRcTag("3.2.3.rc");

        assertFalse(res);
    }

    @Test
    public void testIsReleaseTag_7() throws Exception {
        boolean res = TagBuilder.isReleaseTag("3.2.3.rc");

        assertFalse(res);
    }
}