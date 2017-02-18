/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package applicationmail;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;

/**
 *
 * @author florentcardoen
 */
public class PieceJointe {
    private MimeBodyPart pieceJointe;
    private byte[] digest;
    public PieceJointe()
    {
        
    }
    
    public void setPieceJointe(String path) throws MessagingException
    {
        pieceJointe = new MimeBodyPart();
        DataSource ds = new FileDataSource(path);

        pieceJointe.setDataHandler(new DataHandler(ds));
        pieceJointe.setFileName(path);
        generateDigest(path);
        //pieceJointe.setText(jfc.getSelectedFile().getName());
    }
    public byte[] getDigest()
    {
        return digest;
    }
    private void generateDigest(String path)
    {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            try (InputStream is = Files.newInputStream(Paths.get(path));
                    DigestInputStream dis = new DigestInputStream(is, md))
            {
                
            } catch (IOException ex) { 
                Logger.getLogger(PieceJointe.class.getName()).log(Level.SEVERE, null, ex);
            }
            digest = md.digest();
            //System.out.println(digest);
            //digest = Base64.getEncoder().encode(digest);
            System.out.println(digest);

        }   catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(PieceJointe.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public MimeBodyPart getBodyPart()
    {
        return pieceJointe;
    }
    
}
