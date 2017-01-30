package ru.rambler.plugins.gitflow.utils.tags;

public interface Tag {
    String getNextRcTagName();
    String getTagType();
    String getMainPath();

    int getTagNumber();
}