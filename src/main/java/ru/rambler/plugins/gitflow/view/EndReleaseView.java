package ru.rambler.plugins.gitflow.view;

import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.rambler.plugins.gitflow.utils.tags.Tag;
import ru.rambler.plugins.gitflow.utils.tags.TagBuilder;
import ru.rambler.plugins.gitflow.utils.view.TagFormatter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.regex.Pattern;

public class EndReleaseView extends DialogWrapper {
    private JPanel mainPanel;
    private JPanel branchPanel;
    private JComboBox<String> releaseBranchComboBox;
    private JPanel tagNamePanel;
    private JCheckBox deleteBranchCheckBox;
    private JCheckBox pushRemoteCheckBox;
    private JCheckBox manualCheckBox;
    private JFormattedTextField tagNameTextField;
    private JLabel errorLabel;

    private String masterTagName = "0.0.0";

    public EndReleaseView() {
        super(null);

        init();
        setTitle("End release");

        setModal(true);

        manualCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeTagFieldEnabled();
            }
        });

        tagNameTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                hideErrorLabel();
            }

            @Override
            public void keyPressed(KeyEvent e) {
                hideErrorLabel();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                hideErrorLabel();
            }
        });
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return mainPanel;
    }

    @NotNull
    @Override
    protected Action getOKAction() {
        return new PushAction();
    }

    public void setBranchesNames(List<String> branchesNamesList) {
        String[] namesArray = new String[branchesNamesList.size()];
        branchesNamesList.toArray(namesArray);
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(namesArray);
        releaseBranchComboBox.setModel(model);
    }

    public void setMasterTag(Tag masterTag) {
        masterTagName = masterTag.getMainPath();
        tagNameTextField.setValue(masterTagName);
    }

    public EndReleaseData getDataView() {
        boolean deleteBranch = deleteBranchCheckBox.isSelected();
        boolean pushRemote = pushRemoteCheckBox.isSelected();

        int index = releaseBranchComboBox.getSelectedIndex();
        String startBranch = releaseBranchComboBox.getItemAt(index);

        return new EndReleaseData(startBranch, deleteBranch, pushRemote, tagNameTextField.getText().trim());
    }

    // ----------------------------------------------------------------------------------------------------------------
    private void createUIComponents() {
        Pattern tag = Pattern.compile(TagBuilder.RELEASE_TAG_REGEX, Pattern.CASE_INSENSITIVE);
        tagNameTextField = new JFormattedTextField(new TagFormatter(tag));
        tagNameTextField.setFocusLostBehavior(JFormattedTextField.COMMIT);
    }

    private void changeTagFieldEnabled() {
        boolean isSelected = manualCheckBox.isSelected();
        tagNameTextField.setEnabled(isSelected);

        if (!isSelected) {
            tagNameTextField.setValue(masterTagName);
        }
    }

    private void hideErrorLabel() {
        errorLabel.setVisible(false);
    }

    // ----------------------------------------------------------------------------------------------------------------
    public class EndReleaseData {
        private String releaseBranchName;
        private boolean deleteBranch;
        private boolean pushRemote;
        private String tagName;

        public EndReleaseData(String releaseBranchName, boolean deleteBranch, boolean pushRemote, String tagName) {
            this.releaseBranchName = releaseBranchName;
            this.deleteBranch = deleteBranch;
            this.pushRemote = pushRemote;
            this.tagName = tagName;
        }

        public String getReleaseBranchName() {
            return releaseBranchName;
        }

        public boolean isDeleteBranch() {
            return deleteBranch;
        }

        public boolean isPushRemote() {
            return pushRemote;
        }

        public String getTagName() {
            return tagName;
        }

        public String toString() {
            return releaseBranchName + " " + deleteBranch + " " + pushRemote + " " + tagName;
        }
    }

    private class PushAction extends DialogWrapper.OkAction {
        protected void doAction(ActionEvent e) {
            String tagName = tagNameTextField.getText();
            Object tagValue = tagNameTextField.getValue();

            if (tagName == null || tagName.equals(tagValue.toString())) {
                super.doAction(e);
            } else {
                errorLabel.setVisible(true);
            }
        }
    }
}