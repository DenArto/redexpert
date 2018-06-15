package org.executequery.gui.browser.managment.tracemanager;


import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;


public class CustomTableCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(final JTable table, final Object value,
                                                   final boolean isSelected, final boolean hasFocus, final int row, final int column) {
        assert table != null;

        final CustomTableCellRenderer component = (CustomTableCellRenderer) super.getTableCellRendererComponent(table,
                value, isSelected, hasFocus, row, column);

        final int columnModelIndex = table.convertColumnIndexToModel(column);

        final ResultSetDataModel dataModel = (ResultSetDataModel) table.getModel();
        /*if (LogConstants.STMT_TYPE_COLUMN.equals(dataModel.getColumnName(columnModelIndex))) {
            final int modelRowIndex = table.convertRowIndexToModel(row);
            /*final StatementType statementType = (StatementType) dataModel.getValueAt(modelRowIndex, columnModelIndex);
            assert statementType != null;
            switch (statementType) {
            case BASE_NON_PREPARED_STMT:
                component.setForeground(Color.ORANGE);
                component.setText("S");
                break;
            case NON_PREPARED_QUERY_STMT:
                component.setForeground(Color.RED);
                component.setText("Q");
                break;
            case NON_PREPARED_BATCH_EXECUTION:
                component.setForeground(Color.MAGENTA);
                component.setText("B");
                break;
            case PREPARED_BATCH_EXECUTION:
                component.setForeground(Color.BLUE);
                component.setText("PB");
                break;
            case BASE_PREPARED_STMT:
                component.setForeground(Color.CYAN);
                component.setText("PS");
                break;
            case PREPARED_QUERY_STMT:
                component.setForeground(Color.GREEN);
                component.setText("PQ");
                break;
            case TRANSACTION:
                component.setForeground(Color.DARK_GRAY);
                component.setText("TX");
                break;
            }

            component.setToolTipText(value.toString());

        } else if (value != null) {
            component.setToolTipText(value.toString());
        }*/

        return component;
    }
}
