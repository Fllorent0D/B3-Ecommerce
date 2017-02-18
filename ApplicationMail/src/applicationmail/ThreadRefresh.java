/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package applicationmail;

import java.util.ArrayList;
import javax.mail.Message;

/**
 *
 * @author florentcardoen
 */
public class ThreadRefresh extends Thread{
    private InboxPanel panel;
    
    public ThreadRefresh(InboxPanel ip)
    {
        panel = ip;
        //panel.refreshMailList();
    }

    @Override
    public void run()
    {
        while(true)
        {
            ArrayList<Message> tmp = new ArrayList<>();
            tmp = panel.downloadMails();
            panel.refreshJlist(tmp);

            try
            {
                Thread.sleep(30000);    
            }
            catch (InterruptedException ex)
            {
                System.err.println("Sleep interrompu");
            }
            //System.out.println(1);
        }
    }
}
