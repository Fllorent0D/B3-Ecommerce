/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package applicationmail;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;

/**
 *
 * @author florentcardoen
 */
public class InboxCellRenderer extends JPanel implements ListCellRenderer{
 
		//private static final long serialVersionUID = -3215690257749168708L;
		public JLabel name ;
		public JTextArea description;

		public JPanel container;
		public JScrollPane scrollPane ;
 
		public InboxCellRenderer() {
 
			super( new BorderLayout() );
                   
			setOpaque( false );
 
 
                           setBorder( BorderFactory.createCompoundBorder(
					BorderFactory.createMatteBorder( 0, 0, 1, 0, Color.lightGray ),
					BorderFactory.createEmptyBorder( 1, 1, 1, 1 ) ) );
			setOpaque( false );
 
			Font styleSimple = (new JLabel( "font" )).getFont();
			Font styleGras = new Font( styleSimple.getName(),
					Font.BOLD, styleSimple.getSize() );
 
 
			name = new JLabel();
			name.setFont( styleGras );
			name.setOpaque( false );
 
			
 
			description = new JTextArea();
			description.setBorder( BorderFactory.createEmptyBorder( 5, 0, 0, 0 ) );
			description.setEditable( false );
			description.setFont( (new JLabel()).getFont() );
			description.setOpaque( false );
			description.setLineWrap( true );
			description.setWrapStyleWord( true );						

 
 
			JPanel p1 = new JPanel( new BorderLayout() );
			p1.add( name, BorderLayout.NORTH );
			p1.setOpaque( false );
 
 
 
			JPanel p2 = new JPanel( new BorderLayout() );
			p2.add( p1, BorderLayout.WEST );
 
			p2.setOpaque( false );
 
 
			container = new JPanel( new BorderLayout() );
 
			container.add( p2, BorderLayout.NORTH );
			container.add( description, BorderLayout.SOUTH );
			container.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
 
			/*Dimension d = description.getPreferredSize();
			d.setSize( 250, d.getHeight() );
			description.setPreferredSize( d );
 */
			add( container, BorderLayout.CENTER );
		}
 
 
		public Component getListCellRendererComponent(JList list, Object value,int index, boolean isSelected, boolean cellHasFocus)
		{
 
 
                    
                    try {
                        Message s = (Message) value;

                        Enumeration headers = s.getAllHeaders();
                        while (headers.hasMoreElements()) {
                          Header h = (Header) headers.nextElement();
                          if(h.getName().equals("Return-Path"))
                                name.setText(h.getValue());

                        }

                        //name.setText(s.getFrom()[0].toString());
//System.out.println(s.getFrom()[0].toString());
                        
                        //category.setText(s.getCategory());
                        if(s.getSubject() != null)
                            description.setText(s.getSubject());
                        else
                            description.setText("Pas de sujet...");

                        
                        
                        
                        
                        if( isSelected || list.getSelectedIndex()==index ) {
                            name.setForeground( list.getSelectionForeground() );
                            description.setForeground( list.getSelectionForeground() );
                            container.setBackground( list.getSelectionBackground() );

                        } else {
                            name.setForeground( (new JLabel()).getForeground() );
                            description.setForeground( (new JLabel()).getForeground() );
                            container.setBackground( list.getBackground() );
                           
                        }
                        
                        
                        return this;
                    } catch (MessagingException ex) {
                        Logger.getLogger(InboxCellRenderer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    return this;
		}
	
}
