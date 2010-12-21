package org.woped.core.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.List;

import org.woped.core.controller.IEditor;
import org.woped.core.qualanalysis.IReachabilityGraph;

public interface IUserInterface extends IEditorAware
{
    public void hideEditor(IEditor editor);

    public IEditor getEditorFocus();

    public List<IEditor> getAllEditors();

    public void quit();

    public Rectangle getBounds();

    public Component getComponent();

    public Component getPropertyChangeSupportBean();

    public void cascadeFrames();

    public void arrangeFrames();
    
    public void refreshFocusOnFrames();

    /* * some component methods * */

    public int getX();

    public int getY();

    public Dimension getSize();
    
    public boolean isMaximized();

    public void setVisible(boolean visible);
    
    public void updateRecentMenu();
    
    public IReachabilityGraph getReachGraphFocus();
    
    //public StatusBarVC getStatusBar();

    //public TaskBarVC getTaskBar();

    public IToolBar getToolBar();
    
    // functions concerning to switch the Toolbars
    
    public Container getContentPane();
    
    public void switchToolBar(boolean change);
    
    public void removeToolBar();
    
    public void setSimulatorBar(Object simulatorBar);
    
    public void setFirstTransitionActive();
    
}
