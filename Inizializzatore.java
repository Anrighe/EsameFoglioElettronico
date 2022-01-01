import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**	Classe che gestisce la creazione le componenti del foglio elettronico
 * 	@author Enrico Marras
 * 	@version Java SE Development Kit 17
 * 	<body style="background-color:#A4BEDA;"></body>
 */ 
public class Inizializzatore 
{
	/** Dimensione di altezza e larghezza del foglio elettronico */
	private final int dim = 27;
	
	/** Memorizza il tipo della classe della cella selezionata nel JTable */
	private String classeCellaSelezionata;
	
	/** Struttura dati <b>primaria</b> utilizzata per l'effettivo contenuto di una cella dopo le modifiche apportate alla JTable */	
	private ArrayList<CellaGenerica> matrice[];
	
	/** Struttura dati <b>secondaria</b> utilizzata per memorizzare cos'era precedentemente una cella.
	 * 	<p>Nel caso di una cella di tipo operazione memorizza la stringa che ha determinato un risultato</p> */
	private Displayer sottoMatrice; 
	
	/** Array utilizzato per impostare i nomi delle colonne del DefaultTableModel a lettere ordinate alfabeticamente */
	private String nomeColonne[];
	
	private DefaultTableModel dati;
	
	/** Mostra l'operazione effettuata e il tipo della cella selezionata */
	private JTextField operationDisplayer;
	
	private JTable table;
	
	/** Listener per l'avvenuta modifica del JTable*/
	@SuppressWarnings("unused")
	private TableModelListener listenerModifica; 
	
	/** True se la cella e' di tipo <b>CellaTesto</b> */
	private boolean ritMatcherTesto = false; 
	
	/** True se la cella e' di tipo <b>CellaNumeri</b> */
	private boolean ritMatcherNumeri = false; 
	
	/** True se la cella e' di tipo <b>CellaOperazione</b> */
	private boolean ritMatcherOperazione = false; 
	
	/** Monitora che monitora se e' gia' stato creato un thread */
	private boolean threadCreato = false; 
	
	/** Intervallo di secondi che separa ogni salvataggio automatico */
	private final int timerAutosalvataggio = 30; 
	
	/** Usata per ottenere l'attuale percorso assoluto */
	private File percorsoCorrente; 
	
	/** Costruttore che inizializza tutte le componenti del foglio elettronico.
	 * <p>- Istanzia un oggetto per la struttura dati principale [<b>matrice</b>] e per quella secondaria [<b>sottomatrice</b>]</p>
	 * <p>- Configura ed istanzia il DefaultTableModel [<b>dati</b>]</p>
	 * <p></p>
	 */
	@SuppressWarnings("unchecked")
	public Inizializzatore()
	{
		matrice = new ArrayList[dim];
		sottoMatrice = new Displayer(dim);
		for (int i = 0; i < dim; i++)
		{
			matrice[i] = new ArrayList<CellaGenerica>();
			for (int j = 0; j < dim; j++)
			{
				matrice[i].add(new CellaGenerica());
			}
		}
		
		nomeColonne = new String[dim]; 

		for(int i = 0; i < dim; i++)
		{
			if (i == 0)
				nomeColonne[i] = "";
			else
				nomeColonne[i] = String.valueOf((char)(64 + i));
		}

		dati = new DefaultTableModel(nomeColonne, dim)
		{
			private static final long serialVersionUID = 1L;

			/** Imposta la prima colonna del DefaultTableModel come non modificabile
			 * @Override Override del metodo isCellEditable di DefaultTableModel
			 * @param row indice di colonna
			 * @param column indice di colonna
			 * @return true se si trova nella prima colonna, false altrimenti
			 */
			public boolean isCellEditable(int row, int column) 
			{
				if (column == 0)
					return false; //Fa in modo che tutte le celle della prima colonna non siano editabili
				else
					return true;
			}
		};
		
		//configurazione del DefaultTableModel
		for (int i = 0; i < dim; i++)
		{
			for (int j = 0; j < dim; j++)
			{
				if (j == 0)
					dati.setValueAt(i+1, i, 0);
				else
				{
					for (int cicla = 0; cicla < dim; cicla++)
						dati.setValueAt(matrice[j].get(cicla).toString(), i, j);
				}
			}
		}
		
		//Configurazione dell'operationDisplayer
		operationDisplayer = new JTextField();
		operationDisplayer.setBackground(new Color(199, 217, 252));
		operationDisplayer.setEditable(false);
		operationDisplayer.setFont(new Font("Calibri", Font.BOLD, 18));
		
		table = new JTable(dati);

		table.getModel().addTableModelListener(listenerModifica = new TableModelListener()
		{
			public void tableChanged(TableModelEvent listenerModifica) 
			{
				System.out.println("Contenuto cella modificato: " + table.getValueAt(table.getSelectedRow(), table.getSelectedColumn())); //debug
				
				System.out.println("RIGA SELEZIONATA: " + table.getSelectedRow()); //debug
				System.out.println("COLONNA SELEZIONATA: " + table.getSelectedColumn()); //debug
				
				/** Cattura il testo inserito in una cella del JTable */
				String text;
				text = (String) table.getValueAt(table.getSelectedRow(), table.getSelectedColumn());
				
				System.out.println("text: " + text); //debug
				
				if (!text.equals(""))
				{
					Pattern patternOperazioni = Pattern.compile("^=[A-Z][1-2]{0,1}[0-9][\\+|-][A-Z][1-2]{0,1}[0-9]$");
					Matcher matcherOperazioni = patternOperazioni.matcher(text);
					ritMatcherOperazione = matcherOperazioni.find();
					System.out.println("Operazione: " + ritMatcherOperazione); //debug: stampa true se è un'operazione
					
					
					Pattern patternNumeri = Pattern.compile("^-{0,1}[0-9]+$");
					Matcher matcherNumeri = patternNumeri.matcher(text);
					ritMatcherNumeri = matcherNumeri.find();	
					System.out.println("Numeri: " + ritMatcherNumeri); //debug: stampa true se sono numeri
					
					
					if (ritMatcherNumeri == false && ritMatcherOperazione == false) //se entra è già testo e non è necessario effettuare controlli
					{	
						ritMatcherTesto = true;
						//Pattern patternTesto = Pattern.compile("^[A-Za-z0-9]+$"); 
						//Matcher matcherTesto = patternTesto.matcher(text);
						System.out.println("Testo : " + ritMatcherTesto); //debug: stampa true se è testo
					}
				}
				
				//assegna il valore nella struttura dati alla stringa temporanea text
				matrice[table.getSelectedRow()].get(table.getSelectedColumn()).setContCell(text);

				
				if (ritMatcherTesto == true) //conversione della cella a tipo CellaTesto
				{
					//assegna il valore nella sottomatrice
					sottoMatrice.getDisplayer()[table.getSelectedRow()][table.getSelectedColumn()] = text;
					
					matrice[table.getSelectedRow()].set(table.getSelectedColumn(), new CellaTesto( 
								matrice[table.getSelectedRow()].get(table.getSelectedColumn()).getContCell()));
				}
				
				if(ritMatcherNumeri == true) //conversione della cella a tipo CellaNumeri
				{
					if(sottoMatrice.getDisplayer()[table.getSelectedRow()][table.getSelectedColumn()].contains("=") == false)
					{
						//assegna il valore nella sottomatrice
						sottoMatrice.getDisplayer()[table.getSelectedRow()][table.getSelectedColumn()] = text;
					}
					
					System.out.println("ENTRO IN CONVERSIONE CELLA NUMERICA"); //debug
					matrice[table.getSelectedRow()].set(table.getSelectedColumn(), new CellaNumeri(matrice[table.getSelectedRow()].get(table.getSelectedColumn()).getContCell()));
					
				}
				
				if(ritMatcherOperazione == true) //conversione della cella a tipo CellaOperazione
				{
					//assegna il valore nella sottomatrice
					sottoMatrice.getDisplayer()[table.getSelectedRow()][table.getSelectedColumn()] = text;
					
					//crea la nuova cella di tipo CellaOperazione
					matrice[table.getSelectedRow()].set(table.getSelectedColumn(),
							new CellaOperazione(matrice, table, text));
					
					//assegna il dato di ritorno della CellaOperazione al DefaultTableModel
					dati.setValueAt(matrice[table.getSelectedRow()].get(table.getSelectedColumn()).toString(), table.getSelectedRow(), table.getSelectedColumn());
					
				}
				
				if(ritMatcherTesto == false && ritMatcherNumeri == false && ritMatcherOperazione == false) //se non è una cella di testo, o una cella numero o una cella operazione allora è una cella vuota e deve essere riconvertita al tipo CellaGenerica
				{
					System.out.println("La cella di riga: " + table.getSelectedColumn() + " e colonna: " + table.getSelectedColumn() + " diventa una cella generica"); //debug
					matrice[table.getSelectedRow()].set(table.getSelectedColumn(), new CellaGenerica());
				}
				
				System.out.println("Aggiorno il contenuto della cella di riga " + table.getSelectedRow() + " e colonna " + table.getSelectedColumn() + " a: " + text); //debug
				
				System.out.println("Contenuto struttura dati:"); //debug
				for (int i = 0; i < dim; ++i)
					System.out.println(matrice[i]);

				classeCellaSelezionata = matrice[table.getSelectedRow()].get(table.getSelectedColumn()).getClass() + "";
				classeCellaSelezionata = "     \t\t\tTipo cella selezionato: " + classeCellaSelezionata.substring(6);
				operationDisplayer.setText("Contenuto cella: " + sottoMatrice.getDisplayer()[table.getSelectedRow()][table.getSelectedColumn()] + classeCellaSelezionata);
				
				
				if (threadCreato == false)
				{
					threadCreato = true;
					
				    //variabile usata per ottenere il percorso assoluto corrente
				    percorsoCorrente = new File("");
				    
				    Runnable salvataggioAutomatico = new Runnable() 
				    {
				        public void run() 
				        {
				            System.out.println("SALVATAGGIO AUTOMATICO"); //debug
				            System.out.println(percorsoCorrente.getAbsolutePath()); //debug
				    		
				            try 
				    		{
				            	System.out.println("ENTRO TRY CATCH AUTOSAVE");
				            	
				            	//salvataggio la struttura dati principale su file binario con suffisso ".autosave"
				            	FileOutputStream fos = new FileOutputStream(percorsoCorrente.getAbsolutePath() + "\\.autosave");
				    			ObjectOutputStream oos = new ObjectOutputStream(fos);
				    			oos.writeObject(matrice);
				    			oos.close();
				            	
				    			//salvataggio la sottomatrice su file binario con suffisso ".autosave.sottomatrice"
				            	FileOutputStream fos2 = new FileOutputStream(percorsoCorrente.getAbsolutePath() + "\\.autosave.sottomatrice");
				    			ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
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
				    };
				    //crea un pool formato da un thread
				    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
				    //abilita l'esecuzione di del salvataggio automatico dopo il ritardo iniziale di 0 secondi, a periodi regolari definiti dalla variabile timerAutosalvataggio
				    executor.scheduleAtFixedRate(salvataggioAutomatico, 0, timerAutosalvataggio, TimeUnit.SECONDS);
				}
			}	
		});
		
		//barra menu File
		JMenuBar barraFile = new JMenuBar();
        
		//Crea il menu
		JMenu menuFile = new JMenu("File");
		
        //Crea gli elementi del menu
		JMenuItem opzioneMenuFile1 = new JMenuItem("Nuovo");
		JMenuItem opzioneMenuFile2 = new JMenuItem("Apri");
		JMenuItem opzioneMenuFile3 = new JMenuItem("Salva con nome");
		JMenuItem opzioneMenuFile4 = new JMenuItem("Esci");
		
		menuFile.add(opzioneMenuFile1);
        menuFile.add(opzioneMenuFile2);
        menuFile.add(opzioneMenuFile3);
        menuFile.add(opzioneMenuFile4);
        barraFile.add(menuFile);

        //barra menu Help
        JMenuBar barraHelp = new JMenuBar();
        JMenu menuHelp = new JMenu("Aiuto");
        JMenuItem opzioneMenuHelp1 = new JMenuItem("Documentazione");
        JMenuItem opzioneMenuHelp2 = new JMenuItem("Traccia d'esame");
        menuHelp.add(opzioneMenuHelp1);
        menuHelp.add(opzioneMenuHelp2);
        barraHelp.add(menuHelp);
        
        JPanel pannelloNordUpper = new JPanel();
        JPanel pannelloNord = new JPanel();
        
        FlowLayout leftAlignment = new FlowLayout();
        leftAlignment.setAlignment(FlowLayout.LEFT);
        
        pannelloNord.setLayout(new BorderLayout());
        pannelloNordUpper.setLayout(leftAlignment);
        
        pannelloNordUpper.add(barraFile);
        pannelloNordUpper.add(barraHelp);
        pannelloNord.add(pannelloNordUpper, BorderLayout.NORTH);
        pannelloNord.add(operationDisplayer, BorderLayout.SOUTH);

		table.setCellSelectionEnabled(true);
		table.setBackground(new Color(232, 255, 250));
		table.setRowHeight(22);
		
		//setta la prima colonna con background grigio e testo allineato al centro e imposta la barra di scorrimento
		JScrollPane sp = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    DefaultTableCellRenderer setPrimaColonna = new DefaultTableCellRenderer();
	    setPrimaColonna.setBackground(new Color(238, 238, 238, 255));
	    setPrimaColonna.setHorizontalAlignment(JLabel.CENTER);
	    table.getColumnModel().getColumn(0).setCellRenderer(setPrimaColonna);
	    table.getTableHeader().setReorderingAllowed(false);
	    
	    table.getColumnModel().getColumn(0).setMaxWidth(30);
	    
	    //Imposta la JTable affinché non ridimensioni automaticamente la dimensione delle colonne 
	    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); 
	    
	    Frame finestra = new Frame(pannelloNord, sp);
	    finestra.getF().setVisible(true);
	    
	    new Menu(dim, dati, table, finestra, 
	    		opzioneMenuFile1, opzioneMenuFile2, opzioneMenuFile3, opzioneMenuFile4, 
	    		opzioneMenuHelp1, opzioneMenuHelp2, 
	    		matrice, sottoMatrice);
	}
}