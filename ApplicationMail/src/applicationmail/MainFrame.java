/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package applicationmail;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.mail.Session;
import javax.mail.Store;

/**
 *
 * @author florentcardoen
 */
public class MainFrame extends javax.swing.JFrame {
    LoginFrame lf;
    private Session mailSession;
    private Store mailStore;
    private Properties mailProp;
    private String user;
    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
        java.net.URL url = ClassLoader.getSystemResource("icon.png");
        Toolkit kit = Toolkit.getDefaultToolkit();
        Image img = kit.createImage(url);
        setIconImage(img);
        
        lf = new LoginFrame(this);
        lf.setVisible(true);
        //changeLayout("Login");
    }
    public Properties loadProperties(String server)
    {
        /* Fichier properties */
        String pathProperties = "settings/"+server+"_prop.txt";
        Properties properties = new Properties();
        File dossierProp = new File("settings");
        
        if(!dossierProp.exists())
            dossierProp.mkdir();

        try
        {
            FileInputStream Oread = new FileInputStream(pathProperties);
            properties.load(Oread);
        }
        catch(FileNotFoundException ex)
        {
            try 
            {
                FileOutputStream Oflux = new FileOutputStream(pathProperties);
                
        
                switch(server){
                    case "U2" :
                        properties.put("mail.pop3.host", "10.59.26.134");
                        properties.put("mail.pop3.port", "110");
                        properties.put("mail.smtp.host", "10.59.26.134");
                        properties.put("mail.smtp.port", "25");
                        properties.put("mail.disable.top", "true");
                        properties.put("store", "pop3");
                        
                    break;
                    case "Gmail":
                        properties.put("mail.pop3.starttls.enable", "true");
                        properties.put("mail.pop3.host", "pop.gmail.com");
                        properties.put("mail.pop3.port", "995");
                        properties.put("mail.pop3.starttls.enable", "true");
                        properties.put("mail.smtp.auth", "true");
                        properties.put("mail.smtp.starttls.enable", "true");
                        properties.put("mail.smtp.host", "smtp.gmail.com");
                        properties.put("mail.smtp.port", "587");
                        properties.put("store", "pop3s");
                        break;
                }
                //paramCo.setProperty("MAIL_HOST", "10.59.26.134");
                try {
                    properties.store(Oflux, null);
                }
                catch (IOException ex1) {
                    System.err.println(ex1.getStackTrace());
                    System.exit(0);
                }
            } 
            catch (FileNotFoundException ex1) 
            {
                System.err.println(ex1.getStackTrace());
                System.exit(0);
            }
        }
        catch(IOException ex)
        {
            System.err.println(ex.getStackTrace());
            System.exit(0);
        }

        return properties;
    }
    private void setProperties(Properties pr)
    {
        mailProp = pr;
    }
    public Properties getProperties()
    {
        return mailProp;
    }
    void connect(final String user, final String pass, String server) throws NoSuchProviderException, MessagingException {
        Store st = null;
        String u = user;
        setProperties(loadProperties(server));
        
        
        //A test à l'école
        if(server.equals("Gmail"))
            setSession(Session.getInstance(getProperties(),
            new javax.mail.Authenticator() {
               protected PasswordAuthentication getPasswordAuthentication() {
                  return new PasswordAuthentication(user, pass);
               }
            }));
        else
            setSession(Session.getDefaultInstance(getProperties()));
            
        st = getSession().getStore(getProperties().getProperty("store"));
           
        /* Bizarre */
        st.connect(getProperties().getProperty("mail.pop3.host"), user, pass);
        
        if(server.equals("U2"))
            u += "@u2.tech.hepl.local";
        
        setUser(u);
        setStore(st);
    
    
    }
    public void setUser(String u)
    {
        user = u;
    }
    public void setStore(Store s)
    {
        mailStore = s;

        inboxPanel.createDirectory(user);

        //On envoie le store au panneau inbox (le store contient, entre autres, les messages reçus)
        
        inboxPanel.setStore(s);
        inboxPanel.setSession(mailSession);
        inboxPanel.setUser(user);
        this.setTitle(user);
        
        lf.dispose();
        this.setVisible(true);
        
        ThreadRefresh tp = new ThreadRefresh(inboxPanel);
        tp.start();

    }
 
    public void setSession(Session sess)
    {
        mailSession = sess;
    }
    public Session getSession(){
        return mailSession;
    }

    public void changeLayout(String nomCard)
    {
        CardLayout card = (CardLayout) this.getContentPane().getLayout();
        card.show(this.getContentPane(), nomCard);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu1 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        inboxPanel = new applicationmail.InboxPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();

        jMenu1.setText("jMenu1");

        jMenuItem2.setText("jMenuItem2");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBounds(new java.awt.Rectangle(0, 0, 0, 0));
        getContentPane().setLayout(new java.awt.CardLayout());

        inboxPanel.setPreferredSize(null);
        getContentPane().add(inboxPanel, "Inbox");

        jMenu2.setText("Fichiers");

        jMenuItem1.setText("Déconnexion");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);

        jMenuItem3.setText("Quitter");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        this.dispose();
        try {
            if(inboxPanel.folderMail.isOpen())
                inboxPanel.folderMail.close(true);
      
            mailStore.close();
            lf = new LoginFrame(this);
            lf.setVisible(true);
        } catch (MessagingException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
        System.exit(1);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    System.setProperty("apple.laf.useScreenMenuBar", "true");
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }

                MainFrame mainFrame = new MainFrame();
                
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private applicationmail.InboxPanel inboxPanel;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    // End of variables declaration//GEN-END:variables


}
