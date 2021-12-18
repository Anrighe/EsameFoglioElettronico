import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class Menu 
{
	public Menu(Frame finestra, JMenuItem opzioneMenuFile1, JMenuItem opzioneMenuFile2, JMenuItem opzioneMenuFile3, JMenuItem opzioneMenuFile4, JMenuItem opzioneMenuHelp1, JMenuItem opzioneMenuHelp2)
	{
		opzioneMenuFile1.addActionListener(new ActionListener() 
		{
		    public void actionPerformed(ActionEvent e)
		    {
		    	System.out.println("BUTTON NEW");
		    	finestra.getF().dispose();
		    	@SuppressWarnings("unused")
				Inizializzatore foglioElettronico = new Inizializzatore();
		    }
		});
		
		opzioneMenuFile2.addActionListener(new ActionListener() 
		{
		    public void actionPerformed(ActionEvent e)
		    {
		    	System.out.println("BUTTON OPEN");
		    	
		    	JFileChooser fileOpener = new JFileChooser();
		    	
		    	
		    	
		    	File currentPath = new File(System.getProperty("user.dir"));
		    	fileOpener.setCurrentDirectory(currentPath);
		    	fileOpener.setDialogTitle("Apri");
		    	fileOpener.setApproveButtonText("Apri");
		    	
		    	if (fileOpener.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
		    	{
		    		
		    		System.out.println("È stato selezionato il percorso " + fileOpener.getSelectedFile().getAbsolutePath());
		    	}
		    }
		});
	    
		opzioneMenuFile3.addActionListener(new ActionListener() 
		{
		    public void actionPerformed(ActionEvent e)
		    {
		    	System.out.println("BUTTON SAVE AS");
		    	
		    	JFileChooser fileSaver = new JFileChooser();
		    	
		    	File currentPath = new File(System.getProperty("user.dir"));
		    	fileSaver.setCurrentDirectory(currentPath);
		    	fileSaver.setDialogTitle("Salva con nome");

		    	fileSaver.setApproveButtonText("Salva");
		    	if (fileSaver.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
		    	{
		    		System.out.println("È stato selezionato il percorso " + fileSaver.getSelectedFile().getAbsolutePath());
		    		
		    	}
		    }
		});
	    
		opzioneMenuFile4.addActionListener(new ActionListener() 
		{
		    public void actionPerformed(ActionEvent e)
		    {
		    	System.out.println("BUTTON EXIT");
		    	finestra.getF().dispose();
		    }
		});
		
		opzioneMenuHelp2.addActionListener(new ActionListener() 
		{
		    public void actionPerformed(ActionEvent e)
		    {
		    	System.out.println("BUTTON TRACCIA");
		    	if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) 
		    	{
		    	    try 
		    	    {
						Desktop.getDesktop().browse(new URI("http://didattica.agentgroup.unimore.it/wiki/images/b/b6/Tesina2122.pdf"));
					} 
		    	    catch (IOException e1) 
		    	    {
						e1.printStackTrace();
					} 
		    	    catch (URISyntaxException e1) 
		    	    {
						e1.printStackTrace();
					}
		    	}
		    	else
		    	{
		    		String messaggio = "Feature non supportata dal sistema operativo corrente";
		    		JOptionPane.showMessageDialog(null, messaggio, "Errore", JOptionPane.INFORMATION_MESSAGE);
		    	}
		    }
		});

	}
	
}
