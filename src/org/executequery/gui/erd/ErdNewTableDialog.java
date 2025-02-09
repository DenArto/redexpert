/*
 * ErdNewTableDialog.java
 *
 * Copyright (C) 2002-2017 Takis Diakoumis
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.executequery.gui.erd;

import org.executequery.GUIUtilities;
import org.executequery.gui.DefaultPanelButton;
import org.executequery.gui.browser.ColumnData;
import org.executequery.gui.table.CreateTableFunctionPanel;
import org.executequery.gui.text.SimpleSqlTextPanel;
import org.executequery.localization.Bundles;
import org.underworldlabs.util.MiscUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

/**
 * @author Takis Diakoumis
 */
public class ErdNewTableDialog extends ErdPrintableDialog {

    /**
     * The ERD parent panel
     */
    private final ErdViewerPanel parent;

    /**
     * The <code>ErdTable</code> representing this dialog
     */
    private ErdTable erdTable;

    /**
     * The common create table panel
     */
    private CreateTablePanel createPanel;

    /**
     * A new line character
     */
    private static final char NEW_LINE_CHAR = '\n';

    public ErdNewTableDialog(ErdViewerPanel parent) {
        super("New Table", false);
        this.parent = parent;

        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        sqlText = createPanel.getSQLTextAreal();
        display();
        createPanel.setFocusComponent();
    }

    public ErdNewTableDialog(ErdViewerPanel parent, ErdTable erdTable) {
        this(parent);
        this.setTitle("Table: " + erdTable.getTableName());
        this.erdTable = erdTable;

        createPanel.setTableName(erdTable.getTableName());

        ColumnData[] cda = erdTable.getTableColumns();
        createPanel.setTableColumnData(cda);
        Vector ccv = new Vector();

        if (cda != null) {
            for (int i = 0; i < cda.length; i++) {

                Vector _ccv = cda[i].getColumnConstraintsVector();

                if (_ccv == null)
                    continue;

                for (int j = 0, k = _ccv.size(); j < k; j++) {
                    ccv.add(_ccv.elementAt(j));
                }

            }
        }

        createPanel.setColumnConstraintVector(ccv);
        createPanel.resetSQLText();
        createPanel.setSQLTextCaretPosition(0);
    }

    private void jbInit() throws Exception {
        Container c = this.getContentPane();
        c.setLayout(new BorderLayout());

        JButton cancelButton = new DefaultPanelButton(Bundles.get("common.cancel.button"));
        cancelButton.setActionCommand("Cancel");
        JButton okButton = new DefaultPanelButton(Bundles.get("common.create.button"));
        okButton.setActionCommand("Create");

        ActionListener btnListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buttons_actionPerformed(e);
            }
        };

        cancelButton.addActionListener(btnListener);
        okButton.addActionListener(btnListener);

        JPanel btnPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets.top = 5;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 1.0;
        btnPanel.add(okButton, gbc);
        gbc.weightx = 0;
        gbc.gridx = 1;
        gbc.insets.left = 7;
        btnPanel.add(cancelButton, gbc);

        createPanel = new CreateTablePanel(false);
        createPanel.addButtonsPanel(btnPanel);
        createPanel.setPreferredSize(new Dimension(700, 550));
        c.add(createPanel, BorderLayout.CENTER);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    private void buttons_actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("Cancel")) {
            dispose();
        } else if (command.equals("Create")) {
            if (MiscUtils.isNull(createPanel.getTableName())) {
                GUIUtilities.displayErrorMessage("Table name can not be empty");
            } else {
                createTable();
                //dispose();
            }
        }
    }

    public void createTable() {
        createPanel.fireEditingStopped();

        String tableName = createPanel.getTableName();
        ColumnData[] cda = createPanel.getTableColumnDataAndConstraints();

        for (int i = 0; i < cda.length; i++) {
            cda[i].setTableName(tableName);
            cda[i].setNamesToUpper();
        }


        if (erdTable == null) {
            ErdTable table = new ErdTable(tableName, cda, parent);
            table.setCreateTableScript(sqlText.getSQLText());
            table.setNewTable(true);
            table.setEditable(true);
            if (!parent.addNewTable(table)) {
                GUIUtilities.displayErrorMessage("This table name already exists");
                return;
            }
        } else {
            erdTable.setTableColumns(cda);
            erdTable.setTableName(tableName);
            erdTable.setCreateTableScript(sqlText.getSQLText());
            erdTable.setNewTable(true);
            erdTable.setEditable(true);
            erdTable.tableColumnsChanged();
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                parent.updateTableRelationships();
            }
        });

        dispose();
    }

    class CreateTablePanel extends CreateTableFunctionPanel {

        public CreateTablePanel(boolean temporary) {
            super(temporary);
        }

        public void addButtonsPanel(JPanel buttonsPanel) {
            super.addButtonsPanel(buttonsPanel);
        }

        public void setFocusComponent() {
            super.setFocusComponent();
        }

        public void fireEditingStopped() {
            super.fireEditingStopped();
        }

        public void setColumnConstraintVector(Vector ccv) {
            super.setColumnConstraintVector(ccv, true);
        }

        public void setTableColumnData(ColumnData[] cda) {
            super.setColumnDataArray(cda);
        }

        public void setTableName(String tableName) {
            nameField.setText(tableName);
        }

        public void resetSQLText() {
            super.resetSQLText();
        }

        public void setSQLText() {
            super.setSQLText();
        }

        public SimpleSqlTextPanel getSQLTextAreal() {
            return sqlText;
        }

        public Vector getHostedSchemasVector() {
            return new Vector(0);
        }

        public Vector<String> getSchemaTables(String schemaName) {
            Vector _tables = parent.getAllComponentsVector();

            int size = _tables.size();
            Vector<String> tableNames = new Vector<String>(size);

            for (int i = 0; i < size; i++) {
                tableNames.add(_tables.elementAt(i).toString());
            }

            return tableNames;
        }

        public Vector<String> getColumnNamesVector(String tableName, String schemaName) {
            return parent.getTableColumnsVector(tableName);
        }

    }

}
















