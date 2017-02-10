package ru.rambler.plugins.gitflow.view;

import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.rambler.plugins.gitflow.utils.tags.Tag;
import ru.rambler.plugins.gitflow.utils.tags.TagBuilder;
import ru.rambler.plugins.gitflow.utils.view.TagFormatter;

import javax.swing.*;
import java.awt.event.*;
import java.text.ParseException;
import java.util.List;
import java.util.regex.Pattern;

public class PushNewRcTagView extends DialogWrapper {
    private JPanel mainPanel;
    private JPanel expectedTagNamePanel;
    private JSpinner majorSpinner;
    private JSpinner minorSpinner;
    private JSpinner patchSpinner;
    private JSpinner rcSpinner;
    private JPanel commentPanel;
    private JTextField commentTextField;
    private JComboBox<String> branchesNamesComboBox;
    private JCheckBox pushTagCheckBox;
    private JPanel branchNamePanel;
    private JPanel actualTagNamePanel;
    private JFormattedTextField expectedTagNameTextField;
    private JCheckBox manualCheckBox;
    private JLabel errorLabel;

    private TagFormatter formater;
    private String tagName;

    public PushNewRcTagView() {
        super(null);

        init();
        setTitle("New tag");

        setModal(true);

        manualCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeTagFieldEnabled();
            }
        });

        expectedTagNameTextField.addKeyListener(new KeyAdapter() {
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
    public JComponent getPreferredFocusedComponent() {
        return commentTextField;
    }

    @Nullable
    @Override
    public JComponent createCenterPanel() {
        return mainPanel;
    }

    @NotNull
    @Override
    protected Action getOKAction() {
        return new PushAction();
    }

    public void setTag(Tag tag) {
        tagName = tag.getNextRcTagName();
        expectedTagNameTextField.setValue(tagName);
    }

    public TagData getTagData() {

        int index = branchesNamesComboBox.getSelectedIndex();
        String releaseBranch = branchesNamesComboBox.getItemAt(index);

        return new TagData(commentTextField.getText().trim(),
                expectedTagNameTextField.getText(),
                pushTagCheckBox.isSelected(),
                releaseBranch);
    }

    public void setBranchesNames(List<String> namesList) {
        String[] namesArray = new String[namesList.size()];
        namesList.toArray(namesArray);
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(namesArray);
        branchesNamesComboBox.setModel(model);
    }

    // -------------------------------------------- Listeners ---------------------------------------------------------
    private void createUIComponents() {
        Pattern pattern = Pattern.compile(TagBuilder.RC_TAG_REGEX, Pattern.CASE_INSENSITIVE);

        expectedTagNameTextField = new JFormattedTextField(new TagFormatter(pattern));
        expectedTagNameTextField.setFocusLostBehavior(JFormattedTextField.COMMIT);
    }

    private void changeTagFieldEnabled() {
        boolean isSelected = manualCheckBox.isSelected();
        expectedTagNameTextField.setEnabled(isSelected);

        if (!isSelected) {
            expectedTagNameTextField.setValue(tagName);
            try {
                expectedTagNameTextField.commitEdit();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void hideErrorLabel() {
        errorLabel.setVisible(false);
    }

    // ----------------------------------------------------------------------------------------------------------------
    public class TagData {
        private String comment;
        private String tagName;
        private boolean pushTag;
        private String branchName;

        public TagData(String comment, String tagName, boolean pushTag, String branchName) {
            this.comment = comment;
            this.tagName = tagName;
            this.pushTag = pushTag;
            this.branchName = branchName;
        }

        public String getComment() {
            return comment;
        }

        public String getTagName() {
            return tagName;
        }

        public boolean isPushTag() {
            return pushTag;
        }

        public String getBranchName() {
            return branchName;
        }
    }

    private class PushAction extends DialogWrapper.OkAction {
        protected void doAction(ActionEvent e) {
            String tagName = expectedTagNameTextField.getText();
            Object tagValue = expectedTagNameTextField.getValue();

            if (tagName == null || tagName.equals(tagValue.toString())) {
                super.doAction(e);
            } else {
                errorLabel.setVisible(true);
            }
        }
    }
}