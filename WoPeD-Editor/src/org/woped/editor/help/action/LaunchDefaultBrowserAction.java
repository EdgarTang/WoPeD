/*
 * 
 * Copyright (C) 2004-2005, see @author in JavaDoc for the author 
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *
 * For contact information please visit http://woped.dhbw-karlsruhe.de
 *
 */
package org.woped.editor.help.action;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import org.woped.core.utilities.Platform;
import org.woped.gui.translations.Messages;

/**
 * A simple, static class to display a URL in the system browser.
 * 
 * Under Unix, the system browser is hard-coded to be 'netscape'. Netscape must
 * be in your PATH for this to work. This has been tested with the following
 * platforms: AIX, HP-UX and Solaris.
 * 
 * Under Windows, this will bring up the default browser under windows, usually
 * either Netscape or Microsoft IE. The default browser is determined by the OS.
 * This has been tested under Windows 95/98/NT.
 * 
 * Note - you must include the url type -- either "http://" or "file://".
 * 17.01.2005
 */

public class LaunchDefaultBrowserAction extends MouseAdapter
{
    /**
     * Display a file in the system browser. If you want to display a file, you
     * must include the absolute path name.
     * 
     * @param url
     *            the file's url (the url must start with either "http://" or
     *            "file://").
     */

    private static final String WIN_PATH    = "rundll32";
    // The flag to display a url.
    private static final String WIN_FLAG    = "url.dll,FileProtocolHandler";
    // The default browser under unix.
//    private static final String UNIX_PATH[] = { "netscape", "firefox", "mozilla", "galeon", "opera", "epiphany", "safari", "konqueror", "lynx" };
    // The flag to display a url.
//    private static final String UNIX_FLAG   = "-remote openURL";

    private URL                 url;
    private JComponent          jComp;

    public LaunchDefaultBrowserAction(String url, JComponent jComp)
    {
        this.jComp = jComp;
        try
        {
            this.url = new URL(url);
        } catch (MalformedURLException e)
        {
            JOptionPane.showMessageDialog(null, Messages.getString("Help.Message.MalformedURL") + " " + url);
        }
    }

    public void mouseReleased(MouseEvent e)
    {
        displayURL();
    }

    public void mouseEntered(MouseEvent e)
    {
        if (jComp != null) jComp.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public void mouseExited(MouseEvent e)
    {
        if (jComp != null) jComp.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    public void displayURL()
    {
    	String cmd = null;
//        int exitCode;
//        boolean success = false;

        try
        {
           if (Platform.isWindows()) {
                cmd = WIN_PATH + " " + WIN_FLAG + " " + url;
                Runtime.getRuntime().exec(cmd);
            } else {
            	java.awt.Desktop.getDesktop().browse(java.net.URI.create(url.toString()));
            }
/*                if (Platform.isMac())
                {
                } else
                {
                    // try to find locate a linux browser and launch it remotely
                    for (int i = 0; i < UNIX_PATH.length && !success; i++)
                    {
                        try
                        {
                            cmd = UNIX_PATH[i] + " " + UNIX_FLAG + "(\"" + url + "\")";
                            Process p = Runtime.getRuntime().exec(cmd);
                            exitCode = p.waitFor();
                            if (exitCode == 0) success = true;
                        } catch (Exception e)
                        {}
                    }
                }
                if (!success)
                {
                    // Command failed, try to launch it without remote option
                    for (int i = 0; i < UNIX_PATH.length && !success; i++)
                    {
                        try
                        {
                            cmd = UNIX_PATH[i] + " " + url;
                            Process p = Runtime.getRuntime().exec(cmd);
                            exitCode = p.waitFor();
                            if (exitCode == 0) success = true;
                        } catch (Exception e)
                        {}
                    }
                }
            }*/
        }

        catch (IOException e)
        {
            // couldn't exec browser
            JOptionPane.showMessageDialog(null, Messages.getString("Help.Message.noDefaultBrowser") + "\nException: " + e);
        }
    }
}