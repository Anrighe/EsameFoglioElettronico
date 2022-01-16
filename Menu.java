import java.awt.Color;
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
import javax.swing.table.DefaultTableModel;
import javax.swing.*;

/**	Classe che gestisce le funzionalita' del menu
 * 	@author Enrico Marras
 * 	@version Java SE Development Kit 17
 * 	<body style="background-color:#A4BEDA;"></body>
 */ 
public class Menu 
{		
	/** Usata per ottenere l'attuale percorso assoluto */
	private File currentPath;
	
	/** Gestisce il caricamento interattivo dei dati da file  */
	private JFileChooser fileOpener;
	
	
	private FileInputStream fis;
	private ObjectInputStream ois;
	private FileInputStream fis2;
	private ObjectInputStream ois2;
	
	/** Gestisce il salvataggio interattivo su file */
	private JFileChooser fileSaver;
	
	private FileOutputStream fos;
	private ObjectOutputStream oos;
	private FileOutputStream fos2;
	private ObjectOutputStream oos2;
	
	/** Usata per aprire la traccia d'esame */
	private Desktop desktop;
	
	/** Utilizzata per ottenere il percorso assoluto corrente */
	private File path;
	
	/** Utilizzata per aprire la documentazione */
	private File docPath;
	
	/** Sistema operativo correntemente in uso */
	private String OS;
	
	/** Nome del corretto percorso della documentazione */
	private String percorsoDocumentazione;
	
	/**	Configurazione delle varie funzionalita' del menu:
	 * 	<p>- &emsp;<b>Nuovo</b>: termina la sessione corrente e apre una nuova finestra di foglio vuoto</p>
	 *	<p>- &emsp;<b>Apri</b>: permette di caricare un foglio elettronico da un file binario tramite un prompt interattivo</p>
	 *	<p>- &emsp;<b>Salva con nome</b>: permette di salvare l'attuale foglio elettronico su un file binario tramite un prompt interattivo</p>
	 *	<p>- &emsp;<b>Esci</b>: termina la sessione corrente</p>
	 *	<p>- &emsp;<b>Tema</b>: permette di personalizzare il tema</p>
	 *	<p>- &emsp;<b>Documentazione</b>: apre la documentazione del progetto</p>
	 *	<p>- &emsp;<b>Traccia d'esame</b>: apre la traccia d'esame</p>
	 * @param dim Dimensione di altezza e larghezza del foglio elettronico
	 * @param dati DefaultTableModel
	 * @param table JTable
	 * @param finestra Gestore del JFrame
	 * @param opzioneMenuFile1 Opzione <b>Nuovo</b> del menu <b>File</b>
	 * @param opzioneMenuFile2 Opzione <b>Apri</b> del menu <b>File</b>
	 * @param opzioneMenuFile3 Opzione <b>Salva con nome</b> del menu <b>File</b>
	 * @param opzioneMenuFile4 Opzione <b>Esci</b> del menu <b>File</b>
	 * @param opzioneSottomenuTema1 Opzione <b>Tema 1</b> del sottomenu <b>Tema</b>
	 * @param opzioneSottomenuTema2 Opzione <b>Tema 2</b> del sottomenu <b>Tema</b>
	 * @param opzioneMenuHelp1 Opzione <b>Documentazione</b> del menu <b>Aiuto</b>
	 * @param opzioneMenuHelp2 Opzione <b>Traccia d'esame</b> del menu <b>Aiuto</b>
	 * @param matrice Struttra dati primaria
	 * @param sottoMatrice Struttra dati secondaria
	 */
	public Menu(int dim, DefaultTableModel dati, JTable table, Frame finestra, 
			JMenuItem opzioneMenuFile1, JMenuItem opzioneMenuFile2, 
			JMenuItem opzioneMenuFile3, JMenuItem opzioneMenuFile4,
			JMenuItem opzioneSottomenuTema1, JMenuItem opzioneSottomenuTema2,
			JMenuItem opzioneMenuHelp1, JMenuItem opzioneMenuHelp2, 
			ArrayList<CellaGenerica> matrice[], Displayer sottoMatrice)
	{
		opzioneMenuFile1.addActionListener(new ActionListener() 
		{
			/**	Termina la sessione corrente e apre un nuovo foglio elettronico
			 * 	@param e
			 */
		    public void actionPerformed(ActionEvent e)
		    {
		    	System.out.println("BUTTON NEW"); //debug
		    	finestra.getF().dispose();
				new Inizializzatore();
		    }
		});
		
		opzioneMenuFile2.addActionListener(new ActionListener() 
		{
			/**	Apre un prompt interattivo che permette di scegliere il file binario da aprire per caricare il foglio elettronico.
			 * 	E' necessario aprire il file del nome esatto che si e' salvato e il file secondario con suffiso "<b>.sottoMatrice</b>"
			 * 	@param e
			 */
		    public void actionPerformed(ActionEvent e)
		    {
		    	System.out.println("BUTTON OPEN"); //debug
		    	
		    	//** Imposta il prompt interattivo per l'apertura del file */
		    	fileOpener = new JFileChooser();
		    	currentPath = new File(System.getProperty("user.dir"));
		    	fileOpener.setCurrentDirectory(currentPath);
		    	fileOpener.setDialogTitle("Apri");
		    	fileOpener.setApproveButtonText("Apri");
		    	
		    	if (fileOpener.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
		    	{
		    		System.out.println("E' stato selezionato il percorso " + fileOpener.getSelectedFile().getAbsolutePath()); //debug

		    		try 
		    		{
						fis = new FileInputStream(fileOpener.getSelectedFile().getAbsolutePath());
						ois = new ObjectInputStream(fis);
						
						System.out.println("Contenuto struttura dati prima del caricamento:"); //debug
						for (int i = 0; i < dim; ++i) 
							System.out.println(matrice[i]); 

						/** ArrayList di appoggio usato per copiare la struttura dati principale dal file binario */
						@SuppressWarnings("unchecked")
						ArrayList<CellaGenerica> newMatrice[] = (ArrayList<CellaGenerica>[]) ois.readObject();
						
						/** Assegnamento della struttura dati principale da quella di appoggio */
						for (int i = 0; i < dim; ++i)
							matrice[i] = newMatrice[i];
						
						System.out.println("Contenuto struttura dati caricata:"); //debug
						for (int i = 0; i < dim; ++i)
							System.out.println(matrice[i]);
						
						//TODO: commento da eliminare?
						//e' necessario ripetere due volte la procedura affinche' vada a buon fine: 
						//la prima volta dara' un out of bounds exception perche' non avendo celle selezionate
						//getSelectedColumn e getSelectedRows ritornano -1
						
						/** Reset della cella selezionata per evitare l'ArrayOutOfBoundsException sollevato dal 
						 * 	metodo setValueAt solleva un ArrayOutOfBoundsException */
						table.setColumnSelectionInterval(1, 1);
						table.setRowSelectionInterval(0, 0);
						
						for (int i = 0; i < dim; ++i)
						{
							for (int j = 1; j < dim; ++j) 
								dati.setValueAt(matrice[i].get(j).toString(), i, j);
						}
						ois.close();
						
						fis2 = new FileInputStream(fileOpener.getSelectedFile().getAbsolutePath() + ".sottoMatrice");
						ois2 = new ObjectInputStream(fis2);
						
						/** Displayer di appoggio usata per caricare la struttura dati secondaria dal file binario con 
						 * suffisso <b>.sottoMatrice</b> */
						Displayer sottoMatriceTmp = (Displayer) ois2.readObject();
						
						/** Assegnamento della struttura dati secondaria da quella di appoggio */
						for (int i = 0; i < dim; ++i)
						{
							for (int j = 1; j < dim; ++j) 
							{
								sottoMatrice.getDisplayer()[i][j] = sottoMatriceTmp.getDisplayer()[i][j];
								System.out.print(sottoMatrice.getDisplayer()[i][j] + ",");
							}
							System.out.println();
						}
						ois2.close();
		    		}
		    		catch (IOException e1)
		    		{
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
			/**	Apre un prompt interattivo che permette di scegliere dove salvare e che nome dare al file binario su cui salvare l'attuale foglio elettronico  da aprire per caricare il foglio elettronico.
			 * 	<p>Dato un <b>[NOME_FILE]</b> verranno generati i seguenti file: </p>
			 * 	<p>- &emsp;<b>[NOME_FILE]</b>: contiene la struttura dati principale</p>
			 * 	<p>- &emsp;<b>[NOME_FILE].sottoMatrice</b>: contiene la struttura dati secondaria</p>
			 * 	@param e
			 */
		    public void actionPerformed(ActionEvent e)
		    {
		    	System.out.println("BUTTON SAVE AS"); //debug
		    	
		    	//** Imposta il prompt di salvataggio interattivo */
		    	fileSaver = new JFileChooser();
		    	currentPath = new File(System.getProperty("user.dir"));
		    	fileSaver.setCurrentDirectory(currentPath);
		    	fileSaver.setDialogTitle("Salva con nome");
		    	fileSaver.setApproveButtonText("Salva");
		    	
		    	
		    	if (fileSaver.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
		    	{
		    		/** Controllo dell'esistenza di un file dello stesso nome del file che si sta cercando di salvare 
		    		 * 	nel percorso selezionato */
		    		boolean esisteFileMatrice = new File(fileSaver.getSelectedFile() + "").exists();
		    		
		    		System.out.println("FILE SELEZIONATO: " + fileSaver.getSelectedFile()); //debug
		    		
		    		/** Se e' gia' presente un file con lo stesso nome nel percorso selezionato viene chiesto all'utente
		    		 * 	se desidera sovrascrivere il file 
		    		 */
		    		if (esisteFileMatrice == true)
		    		{
		    			System.out.println("IL FILE ESISTE GIA'"); //debug
		    			
		    			Conferma promptConferma = new Conferma();
		    			
		    			
		    			promptConferma.getBottoneSi().addActionListener(new ActionListener()
		    			{
							@Override
							public void actionPerformed(ActionEvent e) 
							{
								System.out.println("PREMUTO SI"); //debug
								promptConferma.getPopUpConferma().dispose();

								System.out.println("SOVRASCRIVO"); //debug
								salvataggio(fos, oos, fos2, oos2, matrice, sottoMatrice, fileSaver);
							}
		    			});
		    			
		    			promptConferma.getBottoneNo().addActionListener(new ActionListener()
		    			{
		    				@Override
							public void actionPerformed(ActionEvent e) 
							{
								System.out.println("PREMUTO NO"); //debug
								promptConferma.getPopUpConferma().dispose();
							}
		    			});
		    		}
		    		else
		    		{
		    			System.out.println("ENTRO ELSE SALVATAGGIO"); //debug
		    			salvataggio(fos, oos, fos2, oos2, matrice, sottoMatrice, fileSaver);
		    			System.out.println("E' stato selezionato il percorso " + fileSaver.getSelectedFile().getAbsolutePath()); //debug
		    		}
		    	}
		    }
		});
	    
		opzioneMenuFile4.addActionListener(new ActionListener() 
		{
			/**	Termina l'applicazione
			 * 	@param e
			 */
		    public void actionPerformed(ActionEvent e)
		    {
		    	System.out.println("BUTTON ESCI"); //debug
		    	System.exit(0);
		    }
		});
		
		opzioneSottomenuTema1.addActionListener(new ActionListener() 
		{
			/** Imposta il <b>tema 1</b>
			 *  @param e
			 */
		    public void actionPerformed(ActionEvent e)
		    {
		    	System.out.println("BUTTON TEMA1"); //debug
		    	table.setBackground(new Color(232, 255, 250));
		    }
		});
		
		opzioneSottomenuTema2.addActionListener(new ActionListener() 
		{
			/** Imposta il <b>tema 2</b>
			 *  @param e
			 */
		    public void actionPerformed(ActionEvent e)
		    {
		    	System.out.println("BUTTON TEMA2"); //debug
		    	table.setBackground(new Color(169, 185, 224));
		    }
		});
		
		opzioneMenuHelp1.addActionListener(new ActionListener() 
		{
			/**	Cerca la documentazione nel percorso assoluto in cui e' stato compilato il progetto.
			 * 	In caso non sia presente la documentazione viene segnalato all'utente
			 * 	@param e
			 */
		    public void actionPerformed(ActionEvent e)
		    {
		    	System.out.println("BUTTON DOCUMENTAZIONE"); //debug
		    	
    			/** In base al sistema operativo in uso cambio il percorso alla documentazione */
    			OS = System.getProperty("os.name");
    			System.out.println("OS: " + OS); //debug
    			
    			if (OS.contains("Windows") || OS.contains("Windows 10") || OS.contains("Windows 11"))
    				percorsoDocumentazione = "\\Documentazione\\allclasses-index.html";
    			else
    				percorsoDocumentazione = "/Documentazione/allclasses-index.html";
		    	
		    	
		    	desktop = Desktop.getDesktop();
		    	path = new File("");
		    	docPath = new File(path.getAbsolutePath() + percorsoDocumentazione);
		    	try 
		    	{
					desktop.open(docPath);
				} 
		    	catch (IllegalArgumentException e1)
		    	{
		    		//TODO: aggiungere l'opzione per accedere a documentazione online
		    		
		    		/** Popup che segnala all'utente che non e' stata trovata la documentazione e 
		    		 * 	in che percorso e' stata cercata */
		    		e1.printStackTrace();
		    		String messaggio = "File documentazione non trovato nel percorso \n" + path.getAbsolutePath();
		    		JOptionPane.showMessageDialog(null, messaggio, "Errore", JOptionPane.INFORMATION_MESSAGE);        
		    	}

		    	catch (IOException e1) 
		    	{
					e1.printStackTrace();
				}
		    }
		});
		
		opzioneMenuHelp2.addActionListener(new ActionListener() 
		{
			/**	Apre la traccia d'esame nel visualizzatore standard per pdf del sistema operativo corrente.
			 *  Se questa feature non e' disponibile viene segnalato all'utente 
			 * 	@param e
			 */
		    public void actionPerformed(ActionEvent e)
		    {
		    	System.out.println("BUTTON TRACCIA D'ESAME"); //debug
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
	

	/**	Salva la struttura dati principale e secondaria su file binari aventi i nomi e i path specificati nel JFileChoser
	 * 	@param fos FileOutputStream
	 * 	@param oos ObjectOutputStream
	 * 	@param fos2 FileOutputStream
	 * 	@param oos2 ObjectOutputStream
	 * 	@param matrice Struttura dati principale
	 * 	@param sottoMatrice Struttura dati secondaria
	 * 	@param fileSaver Gestore del prompt interattivo per il salvataggio
	 */
	public void salvataggio(FileOutputStream fos, ObjectOutputStream oos, FileOutputStream fos2, ObjectOutputStream oos2, 
			ArrayList<CellaGenerica> matrice[], Displayer sottoMatrice, JFileChooser fileSaver)
	{
		try 
		{			
			fos = new FileOutputStream(fileSaver.getSelectedFile().getAbsolutePath());
			oos = new ObjectOutputStream(fos);
			oos.writeObject(matrice);
			oos.close();
			
			/** Per differenziare la struttura dati secondaria dalla primaria verra' aggiunto un suffisso <b>".sottoMatrice"</b> */
			fos2 = new FileOutputStream(fileSaver.getSelectedFile().getAbsolutePath() + ".sottoMatrice");
			oos2 = new ObjectOutputStream(fos2);
			oos2.writeObject(sottoMatrice);
			oos2.close();
		}
		catch (FileNotFoundException e1) 
		{
			e1.printStackTrace();
		}
		catch (IOException e1) 
		{
			e1.printStackTrace();
		}
	}
}