package com.fbt.openapi.tests;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;

import chrriis.common.UIUtils;
import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserAdapter;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserNavigationEvent;
 
/**
 * OAuth2授权
 * @author 蜗牛都知道
 * @author CALM
 * @see <a href="http://binary.duapp.com/"></a>
 */
public class RenrenOAuth2Demo {
    private String access_token;
    private String lastURL;
    public RenrenOAuth2Demo() {
        final JDialog dialog = new JDialog();
        dialog.setTitle("授权");
        dialog.setResizable(false);
        dialog.setBounds(30, 30, 608, 522);
        JWebBrowser webBrowser = new JWebBrowser();
        webBrowser.setBarsVisible(false);
        webBrowser.setButtonBarVisible(false);
        webBrowser.setDefaultPopupMenuRegistered(false);
        
        lastURL = "https://graph.renren.com/oauth/authorize?client_id=231253&redirect_uri=http://graph.renren.com/oauth/login_success.html&response_type=token&display=popup";
		webBrowser.navigate(lastURL);
        
        webBrowser.addWebBrowserListener(new WebBrowserAdapter() {
            @Override
            public void locationChanged(WebBrowserNavigationEvent arg0) {
                String site = arg0.getWebBrowser().getResourceLocation();
                if(lastURL.equals(site)){
                    return;
                }
                
                lastURL = site;
                if(site != null){
                    dialog.dispose();
                }
                
                System.out.println(site);
                //access_token = site.substring(site.lastIndexOf("access_token=") + 13, site.lastIndexOf("expires_in"));
                System.out.println(access_token);
                
            }
 
        });
        dialog.add(webBrowser, BorderLayout.CENTER);
        dialog.setVisible(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    public static void main(String[] args){
        NativeInterface.open();	
        UIUtils.setPreferredLookAndFeel();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new RenrenOAuth2Demo();
            }
 
        });
        NativeInterface.runEventPump();
    }
}