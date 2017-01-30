package ru.rambler.plugins.gitflow.view;

import javax.swing.*;
import java.util.List;

public class StartActionView {
    private JPanel mainPanel;
    private JTextField branchNameTextField;
    private JComboBox<String> branchesComboBox;
    private JPanel startBranchNamePanel;
    private JPanel newBranchName;
    private JLabel prefixName;
    private JCheckBox pushCheckBox;

    public JComponent getMainPanel() {
        return mainPanel;
    }

    public JComponent getFocusComponent() {
        return branchNameTextField;
    }

    public void setBranchesNames(List<String> namesList) {
        String[] namesArray = new String[namesList.size()];
        namesList.toArray(namesArray);
        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>(namesArray);
        branchesComboBox.setModel(comboBoxModel);
    }

    public void setPrefixName(String prefix) {
        prefixName.setText(prefix);
    }

    public void setStartBranchName(String name) {
        branchNameTextField.setText(name);
    }

    public StartActionData getData() {
        int index = branchesComboBox.getSelectedIndex();
        String startBranch = branchesComboBox.getItemAt(index);

        return new StartActionData(branchNameTextField.getText().trim(), startBranch, pushCheckBox.isSelected());
    }

    public class StartActionData {
        private String newBranchName;
        private String startBranchName;
        private boolean pushBranch;

        public StartActionData(String newBranchName, String startBranchName, boolean pushBranch) {
            this.newBranchName = newBranchName;
            this.startBranchName = startBranchName;
            this.pushBranch = pushBranch;
        }

        public String getNewBranchName() {
            return newBranchName;
        }

        public String getStartBranchName() {
            return startBranchName;
        }

        public boolean isPushBranch() {
            return pushBranch;
        }
    }
}