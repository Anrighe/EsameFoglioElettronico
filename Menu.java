import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;

public class Menu 
{
	private ArrayList<CellaGenerica> newMatrice[];
	
	public Menu(DefaultTableModel dati, JTable table, Frame finestra, JMenuItem opzioneMenuFile1, JMenuItem opzioneMenuFile2, JMenuItem opzioneMenuFile3, JMenuItem opzioneMenuFile4, JMenuItem opzioneMenuHelp1, JMenuItem opzioneMenuHelp2, ArrayList<CellaGenerica> matrice[], Displayer sottoMatrice)
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
		    		FileInputStream fis;
		    		ObjectInputStream ois;
		    		try 
		    		{
						fis = new FileInputStream(fileOpener.getSelectedFile().getAbsolutePath());
						ois = new ObjectInputStream(fis);
						
						System.out.println("Contenuto struttura dati prima del caricamento:");
						for (int i = 0; i < 27; ++i)
							System.out.println(matrice[i]);

						ArrayList<CellaGenerica> newMatrice[] = (ArrayList<CellaGenerica>[]) ois.readObject();
						
						for (int i = 0; i < 27; ++i)
							matrice[i] = newMatrice[i];
						
						
						System.out.println("Contenuto struttura dati caricata:");
						for (int i = 0; i < 27; ++i)
							System.out.println(matrice[i]);
						
						//TODO: fixare
						//è necessario ripetere due volte la procedura affinché vada a buon fine: 
						//la prima volta darà un out of bounds exception perché non avendo celle selezionate
						//getSelectedColumn e getSelectedRows ritornano -1
						
						//table.selectAll();
						System.out.println("matrice 2 2 prima di selection: " + matrice[2].get(2).toString());
						//table.changeSelection(27, 27, false, false);
						//System.out.println("matrice 2 2 dopo selection: " + matrice[2].get(2).toString());
						System.out.println("colonna selezionata: " + table.getSelectedColumn());
						System.out.println("riga selezionata: " + table.getSelectedRow());
						//table.changeSelection(1, 1, false, false);
						//dati.setValueAt(matrice[2].get(2).toString(), 3, 3);
						//System.out.println("matrice 2 2 dopo inserimento: " + matrice[2].get(2).toString());
						
						//dati.fireTableDataChanged();
						//dati.fireTableCellUpdated(2, 2);
						
						//senza questo setValueAt solleva un ArrayOutOfBoundsException
						table.setColumnSelectionInterval(1, 1);
						table.setRowSelectionInterval(0, 0);
						
						for (int i = 0; i < 27; ++i)
						{
							for (int j = 1; j < 27; ++j) 
								dati.setValueAt(matrice[i].get(j).toString(), i, j);
						}
						//for (int i = 0; i < 27; i++)
							//dati.setValueAt(i+1, i, 0);
						
						//table.clearSelection();
						//table.changeSelection(2, 3, false, false);
						//dati.setValueAt("ciao", 2, 2);
						//dati.setValueAt(newMatrice[table.getSelectedRow()].get(table.getSelectedColumn()-1).toString(), 4, 4);
						//System.out.println("Press enter to continue...");try{        System.in.read();}catch(Exception e2){	e2.printStackTrace();}
						ois.close();
		    		}
		    		catch (IOException e1)
		    		{
		    			System.out.println("IOEXEPTION");
		    			e1.printStackTrace();
		    		} 
		    		catch (ClassNotFoundException e1) 
		    		{
						e1.printStackTrace();
					}
		    		catch (ArrayIndexOutOfBoundsException e1)
		    		{
		    			e1.printStackTrace();
		    		}

		    		
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
		    		FileOutputStream fos;
		    		ObjectOutputStream oos;
					try 
					{
						fos = new FileOutputStream(fileSaver.getSelectedFile().getAbsolutePath());
						oos = new ObjectOutputStream(fos);
						oos.writeObject(matrice);
						oos.close();
					} 
					
					catch (FileNotFoundException e1) 
					{
						//e1.printStackTrace();
					}
					catch (IOException e1) 
					{
						//e1.printStackTrace();
					}
		    		


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