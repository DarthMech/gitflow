package ru.rambler.plugins.gitflow.utils.view;

import javax.swing.text.DefaultFormatter;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TagFormatter extends DefaultFormatter {
    private final Pattern pattern;

    public TagFormatter(Pattern regex) {
        pattern = regex;
    }

    @Override
    public Object stringToValue(String text) throws ParseException {
        if (pattern != null) {
            Matcher matcher = pattern.matcher(text);

            if (matcher.matches()) {
                return super.stringToValue(text);
            }
            throw new ParseException("Pattern did not match", 0);
        }
        return text;
    }
}