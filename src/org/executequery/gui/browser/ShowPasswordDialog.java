/*
 * ShowPasswordDialog.java
 *
 * Copyright (C) 2002-2015 Takis Diakoumis
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

package org.executequery.gui.browser;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import org.executequery.GUIUtilities;
import org.executequery.gui.BaseDialog;
import org.executequery.gui.WidgetFactory;

public class ShowPasswordDialog extends BaseDialog implements ActionListener {

    private static final String COPY_COMMAND = "copyPassword";
    private String password;

    public ShowPasswordDialog(String connectionName, String password) {

        super("Password", true);
        this.password = password;
        
        JButton okButton = WidgetFactory.createPanelButton("Close", null, this);
        JButton copyButton = WidgetFactory.createPanelButton("Copy to Clipboard", 
                "Copies the selected query to the system clipboard", this, COPY_COMMAND);

        JPanel basePanel = new JPanel(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        basePanel.add(new JLabel(createPanelText(connectionName, password), 
                UIManager.getIcon("OptionPane.informationIcon"), JLabel.CENTER), gbc);
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.weighty = 1.0;
        gbc.insets.left = 65;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        basePanel.add(copyButton, gbc);
        gbc.gridx++;
        gbc.insets.left = 0;
        gbc.anchor = GridBagConstraints.WEST;
        basePanel.add(okButton, gbc);
        
        basePanel.setPreferredSize(new Dimension(450, 180));
        basePanel.setBorder(BorderFactory.createEtchedBorder());

        setResizable(false);
        addDisplayComponentWithEmptyBorder(basePanel);
        
        pack();

        setLocation(GUIUtilities.getLocationForDialog(getSize()));
        setVisible(true);
    }

    private String createPanelText(String connectionName, String password) {

        StringBuilder sb = new StringBuilder();
        sb.append("<html><table><tr><td align='center'>Password for connection<br/>[ ")
            .append(connectionName)
            .append(" ]</td></tr>")
            .append("<tr><td align='center'><br/><b>")
            .append(password)
            .append("</b></td></tr>")
            .append("</table></html>");

        return sb.toString();
    }

    public void actionPerformed(ActionEvent e) {

        if (COPY_COMMAND.equals(e.getActionCommand())) {

            GUIUtilities.copyToClipBoard(password);
        }
        
        dispose();
    }
    
}

