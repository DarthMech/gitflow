package ru.rambler.plugins.gitflow.view;

import ru.rambler.plugins.gitflow.services.settings.GitFlowSettings;
import ru.rambler.plugins.gitflow.utils.GitFlowConst;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class SettingsView {
    private JPanel mainPanel;
    private JTextField releaseTextField;
    private JTextField hotfixTextField;
    private JTextField featureTextField;
    private JButton refreshButton;
    private JPanel centerPanel;
    private JPanel refreshPanel;
    private JTextField masterBranchTextField;
    private JTextField devBranchTextField;
    private JTextField repoNameTextField;

    public JComponent getMainPanel() {
        return mainPanel;
    }

    public void setSettings(GitFlowSettings settings) {
        featureTextField.setText(settings.getFeaturePrefix().trim());
        releaseTextField.setText(settings.getReleasePrefix().trim());
        hotfixTextField.setText(settings.getHotfixPrefix().trim());
        masterBranchTextField.setText(settings.getMasterBranchName().trim());
        devBranchTextField.setText(settings.getDevBranchName().trim());
        repoNameTextField.setText(settings.getRepoName().trim());
    }

    public GitFlowSettings getSettings() {
        String featurePrefix = featureTextField.getText().trim();
        String releasePrefix = releaseTextField.getText().trim();
        String hotfixPrefix  = hotfixTextField.getText().trim();
        String masterName    = masterBranchTextField.getText().trim();
        String developName   = devBranchTextField.getText().trim();
        String repoName      = repoNameTextField.getText().trim();

        return new GitFlowSettings(featurePrefix, releasePrefix, hotfixPrefix, masterName, developName, repoName);
    }

    private void createUIComponents() {
        refreshButton = new JButton("R");
        refreshButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                featureTextField.setText(GitFlowConst.FEATURE_PREFIX);
                releaseTextField.setText(GitFlowConst.RELEASE_PREFIX);
                hotfixTextField.setText(GitFlowConst.HOTFIX_PREFIX);
                masterBranchTextField.setText(GitFlowConst.MASTER_BRANCH_NAME);
                devBranchTextField.setText(GitFlowConst.DEV_BRANCH_NAME);
                repoNameTextField.setText(GitFlowConst.ORIGIN_NAME);
            }
        });
    }
}