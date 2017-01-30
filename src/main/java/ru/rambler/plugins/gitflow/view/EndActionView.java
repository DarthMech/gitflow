package ru.rambler.plugins.gitflow.view;

import javax.swing.*;
import java.util.List;

public class EndActionView {
    private JPanel mainPanel;
    private JCheckBox deleteCheckBox;
    private JComboBox<String> branchesComboBox;

    public JComponent getMainPanel() {
        return mainPanel;
    }

    public void setBranchesNames(List<String> namesList) {
        String[] namesArray = new String[namesList.size()];
        namesList.toArray(namesArray);
        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>(namesArray);
        branchesComboBox.setModel(comboBoxModel);
    }

    public EndActionData getData() {
        boolean deleteBranch = deleteCheckBox.isSelected();

        int index = branchesComboBox.getSelectedIndex();
        String branchName = branchesComboBox.getItemAt(index);

        return new EndActionData(branchName, deleteBranch);
    }

    public class EndActionData {
        private String endBranchName;
        private boolean deleteBranch;

        public EndActionData(String endBranchName, boolean deleteBranch) {
            this.endBranchName = endBranchName;
            this.deleteBranch = deleteBranch;
        }

        public String getEndBranchName() {
            return endBranchName;
        }

        public boolean isDeleteBranch() {
            return deleteBranch;
        }
    }
}
