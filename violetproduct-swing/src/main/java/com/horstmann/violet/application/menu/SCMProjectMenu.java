/*
 Violet - A program for editing UML diagrams.

 Copyright (C) 2007 Cay S. Horstmann (http://horstmann.com)
 Alexandre de Pellegrin (http://alexdp.free.fr);

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.horstmann.violet.application.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.logging.ErrorManager;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import com.horstmann.violet.application.gui.MainFrame;
import com.horstmann.violet.application.help.AboutDialog;
import com.horstmann.violet.application.help.HelpManager;
import com.horstmann.violet.application.help.ShortcutDialog;
import com.horstmann.violet.framework.injection.resources.ResourceBundleInjector;
import com.horstmann.violet.framework.injection.resources.annotation.ResourceBundleBean;
import com.horstmann.violet.product.diagram.classes.node.ClassNode;
import com.horstmann.violet.workspace.IWorkspace;
import com.horstmann.violet.workspace.editorpart.IEditorPart;
import com.horstmann.violet.workspace.editorpart.IEditorPartBehaviorManager;
import com.horstmann.violet.workspace.editorpart.behavior.UndoRedoCompoundBehavior;

import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;


/**
 * Help menu
 * 
 * @author Alexandre de Pellegrin
 * 
 */
@ResourceBundleBean(resourceReference = MenuFactory.class)
public class SCMProjectMenu extends JMenu
{

    /**
     * Default constructor
     * 
     * @param mainFrame where this menu is atatched
     * @param factory to access to external resources such as texts, icons
     */
    @ResourceBundleBean(key = "SCMProject")
    public SCMProjectMenu(MainFrame mainFrame)
    {
        ResourceBundleInjector.getInjector().inject(this);
        this.mainFrame = mainFrame;
        this.createMenu();
    }

    /**
     * Initializes menu
     */
    private void createMenu()
    {

        Feature1Item.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                IWorkspace activeWorkspace = mainFrame.getActiveWorkspace();
                IEditorPart activeEditor = activeWorkspace.getEditorPart();
                activeEditor.setFeature1(Feature1Item.isSelected());
                
                
            }

        });
        this.add(Feature1Item);

        Feature2Item.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                IWorkspace activeWorkspace = mainFrame.getActiveWorkspace();
                IEditorPart activeEditor = activeWorkspace.getEditorPart();
                activeEditor.setFeature2(Feature2Item.isSelected());
            }

        });
        this.add(Feature2Item);
        
        Feature3Item.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                Collection<INode> objects = mainFrame.getActiveWorkspace().getGraphFile().getGraph().getAllNodes();
                Collection<IEdge> links = mainFrame.getActiveWorkspace().getGraphFile().getGraph().getAllEdges();
                ArrayList<INode> objectsArray = new ArrayList<>(objects);
                ArrayList<IEdge> linksArray = new ArrayList<>(links);

                Map<String, String> dict = new HashMap<>();
                for(INode object : objects){
                    ClassNode classNode = (ClassNode) object;
                    dict.put(classNode.getName().toEdit(), String.valueOf(classNode.getConnectedEdges().size()));
                }

                String result = "<html>";
                result+="<style>table, th,td { border: 1px solid black; border-collapse: collapse; }</style>";
                result+="<p>Objects: "+objectsArray.size()+" ";
                result+="Connections: "+linksArray.size()+"</p><hr><br>";
                result+="<table><tr><th>Names</th><th>Connections</th></tr>";
                Iterator it = dict.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry)it.next();
                    result+="<tr><td>"+pair.getKey()+"</td><td>"+pair.getValue()+"</td></tr>";
                    it.remove();
                }
                result+="</table>";
                result+="</html>";
                JOptionPane.showMessageDialog(null, result);
            }

        });
        this.add(Feature3Item);

    }

    private boolean isThereAnyWorkspaceDisplayed()
    {
        return mainFrame.getWorkspaceList().size() > 0;
    }
    
    private IEditorPart getActiveEditorPart()
    {
        return this.mainFrame.getActiveWorkspace().getEditorPart();
    }

    /**
     * Main app frame where this menu is attached to
     */
    private MainFrame mainFrame;
    
    @ResourceBundleBean(key = "SCMProject.Feature1")
    private JCheckBoxMenuItem Feature1Item;
    
    @ResourceBundleBean(key = "SCMProject.Feature2")
    private JCheckBoxMenuItem Feature2Item;
    
    @ResourceBundleBean(key = "SCMProject.Feature3")
    private JMenuItem Feature3Item;


}
