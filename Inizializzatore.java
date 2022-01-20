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
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
/**	Gestisce la creazione le componenti del foglio elettronico
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
	
	/** Struttura dati <b>primaria</b> utilizzata per l'effettivo contenuto di una cella dopo le modifiche apportate alla JTable. 
	 *	<p>Utilizzo dei generics: all'interno dell'array di ArrayList e' possibile inserire qualsiasi cella che ha un qualsiasi rapporto di parentela discendente con la classe <b>CellaGenerica</b> */	
	private ArrayList<CellaGenerica> matrice[];
	
	/** Struttura dati <b>secondaria</b> utilizzata per memorizzare cos'era precedentemente una cella.
	 * 	<p>Per maggiori informazioni visionare la classe <b>Displayer</b></p> */ 
	private Displayer sottoMatrice; 
	
	/** Contiene i nomi delle colonne del DefaultTableModel a lettere ordinate alfabeticamente */
	private String nomeColonne[];
	
	private DefaultTableModel dati;
	
	/** Mostra l'operazione effettuata e il tipo della cella selezionata */
	private JTextField operationDisplayer;
	
	private JTable table;
	
	/** Listener per l'avvenuta modifica del JTable*/
	@SuppressWarnings("unused")
	private TableModelListener listenerModifica; 
	
	/** Pattern per il contenuto cella di tipo operazione.
	 * 	<p>Regex utilizzata: <b>^=[A-Z][1-2]{0,1}[0-9][\\+|-][A-Z][1-2]{0,1}[0-9]$</b></p> */ 
	private Pattern patternOperazioni;
	
	/** Matcher utilizzato per confrontare il contenuto della cella con il <b>patternOperazioni</b> */
	private Matcher matcherOperazioni;
	
	/** Pattern per il contenuto cella di tipo numerico.
	 * 	<p>Regex utilizzata: <b>^-{0,1}[0-9]+$</b></p> */ 
	private Pattern patternNumeri;
	
	/** Matcher utilizzato per confrontare il contenuto della cella con il <b>patternNumeri</b> */
	private Matcher matcherNumeri;
	
	/** True se la cella e' di tipo <b>CellaTesto</b> */
	private boolean ritMatcherTesto = false; 
	
	/** True se la cella e' di tipo <b>CellaNumeri</b> */
	private boolean ritMatcherNumeri = false; 
	
	/** True se la cella e' di tipo <b>CellaOperazione</b> */
	private boolean ritMatcherOperazione = false; 
	
	/** Monitora se e' gia' stato creato un thread */
	private boolean threadCreato = false; 
	
	/** Delay in secondi del salvataggio automatico */
	private final int delaySalvataggio = 5;
	
	/** Intervallo di secondi che separa ogni salvataggio automatico */
	private final int timerAutosalvataggio = 20; 
	
	/** Sistema operativo correntemente in uso */
	private String OS;
	
	/** Nome dell'estensione del file binario per il salvataggio automatico */
	private String nomeAutosalvataggio;
	
	/** Attuale percorso assoluto */
	private File percorsoCorrente; 
	
	/** Contenitore per le opzioni del menu <b>File</b> */ 
	private JMenuBar barraFile;
    
	/** Menu <b>File</b> */ 
	private JMenu menuFile;
	
    /** Opzione <b>Nuovo</b> del menu <b>File</b> */
	private JMenuItem opzioneMenuFile1;
	
	/** Opzione <b>Apri</b> del menu <b>File</b> */
	private JMenuItem opzioneMenuFile2;
	
	/** Opzione <b>Salva con nome</b> del menu <b>File</b> */
	private JMenuItem opzioneMenuFile3;
	
	/** Opzione <b>Esci</b> del menu <b>File</b> */
	private JMenuItem opzioneMenuFile4;
	
	/** Contenitore per le opzioni del menu <b>Finestra</b> */ 
	private JMenuBar barraFinestra;
	
	/** Menu <b>Finestra</b> */ 
	private JMenu menuFinestra;
	
	/** Menu <b>Tema</b> */ 
	private JMenu sottomenuTema;
	
    /** Opzione <b>Tema 1</b> del sottomenu <b>Tema</b> */
	private JMenuItem opzioneSottomenuTema1;
	
    /** Opzione <b>Tema 1</b> del sottomenu <b>Tema</b> */
	private JMenuItem opzioneSottomenuTema2;
	
	/** Contenitore per le opzioni del menu <b>Aiuto</b> */ 
	private JMenuBar barraHelp;
	
	/** Menu <b>Aiuto</b> */ 
	private JMenu menuHelp;
	
	/** Opzione <b>Documentazione</b> del menu <b>Aiuto</b> */
	private JMenuItem opzioneMenuHelp1;
	
	/** Opzione <b>Traccia d'esame</b> del menu <b>Aiuto</b> */
	private JMenuItem opzioneMenuHelp2;
	
	/** Contiene il menu */
	private JPanel pannelloNordUpper;
	
	/** Contiene il JPanel pannelloNordUpper e il JTextField */
	private JPanel pannelloNord;
    
	/** Imposta l'allineamento a sinistra del pannello <b>pannelloNordUpper</b> */
	private FlowLayout leftAlignment;
	
	/** Implementare le barre di scorrimento orizzontali e verticali */
	private JScrollPane sp;
	
	/** Impostare colore e allineamento testo della prima colonna */
	private DefaultTableCellRenderer setPrimaColonna;
	
	/** Crea ed imposta il JFrame.
	 * 	<p>Per maggiori informazioni visionare la classe <b>Frame.java</b></p> */
	private Frame finestra;
	
	
	/** Costruttore che inizializza tutte le componenti del foglio elettronico.
	 * 	<p>- &emsp;Istanzia un oggetto per la struttura dati principale <b>matrice</b> e per quella secondaria <b>sottomatrice</b></p>
	 * 	<p>- &emsp;Configura ed istanzia il DefaultTableModel e il JTable</p>
	 * 	<p>- &emsp;Istanzia il listener delle modifiche avvenute al JTable, il quale:</p>
	 * 	<p>  &emsp;&emsp;-&emsp;Controlla se la cella modificata contiene testo, numeri, un'operazione o se e' vuota</p>
	 * 	<p>  &emsp;&emsp;-&emsp;Converte la cella modificata alla tipo coerente con la modifica, aggiornando le due strutture dati</p>
	 *  <p>  &emsp;&emsp;-&emsp;Gestisce la procedura di autosalvataggio con thread su file temporaneo</p>
	 * 	<p>- &emsp;Configura ed inizializza le componenti grafiche</p>
	 */
	@SuppressWarnings("unchecked")
	public Inizializzatore()
	{
		/** Inserimento nella struttura dati principale di elementi di tipo CellaGenerica e creazione della struttura dati secondaria */
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
					return false; 
				else
					return true;
			}
		};
		
		/** Configurazione del DefaultTableModel <b>dati</b> */
		for (int i = 0; i < dim; i++)
		{
			for (int j = 0; j < dim; j++)
			{
				if (j == 0)
					dati.setValueAt(i+1, i, 0);
				else
				{
					for (int cicla = 0; cicla < dim; cicla++)
						dati.setValueAt(matrice[j].get(cicla).toString(), i, j); /** Comportamento polimorfico di toString() -> esecuzione di toString() di CellaGenerica.java */
				}
			}
		}
		
		/** Configurazione del JTextField <b>operationDisplayer</b> */
		operationDisplayer = new JTextField();
		operationDisplayer.setBackground(new Color(199, 217, 252));
		operationDisplayer.setEditable(false);
		operationDisplayer.setFont(new Font("Calibri", Font.BOLD, 18));
		
		table = new JTable(dati);

		table.getModel().addTableModelListener(listenerModifica = new TableModelListener()
		{	
			
			/** Metodo invocato ad ogni modifica del JTable
			 *  @param listenerModifica 
			 */
			public void tableChanged(TableModelEvent listenerModifica) 
			{
				/** Reimposta i flag del contenuto cella per prepararli al controllo */
				ritMatcherTesto = false;
				ritMatcherNumeri = false;
				ritMatcherOperazione = false;
				
				
				/** Cattura il testo inserito in una cella del JTable */
				String text;
				text = (String) table.getValueAt(table.getSelectedRow(), table.getSelectedColumn());
				
				
				if (!text.equals(""))
				{
					/** Controlla se nella cella e' contenuta un'addizione o una sottrazione del tipo <i>=A1±A2</i> */
					patternOperazioni = Pattern.compile("^=[A-Z][1-2]{0,1}[0-9][\\+|-][A-Z][1-2]{0,1}[0-9]$|^=[0-9]++[\\+|-][0-9]++$");
					matcherOperazioni = patternOperazioni.matcher(text);
					ritMatcherOperazione = matcherOperazioni.find();
					
					/** Controllo se la cella contiene solo numeri */
					patternNumeri = Pattern.compile("^-{0,1}[0-9]+$");
					matcherNumeri = patternNumeri.matcher(text);
					ritMatcherNumeri = matcherNumeri.find();	
					
					/** Se il contenuto della cella non e' nullo e non contiene formule o numeri, allora la cella contiene del testo */
					if (ritMatcherNumeri == false && ritMatcherOperazione == false)
					{	
						ritMatcherTesto = true;
					}
				}
				
				/** Assegnamento del valore assegnato alla cella modificata nella struttura dati */
				matrice[table.getSelectedRow()].get(table.getSelectedColumn()).setContCell(text);

				/** Conversione della cella a tipo <b>CellaTesto</b> */
				if (ritMatcherTesto == true) 
				{
					/** Assegnamento del testo nella sottomatrice */
					sottoMatrice.getDisplayer()[table.getSelectedRow()][table.getSelectedColumn()] = text;
					
					matrice[table.getSelectedRow()].set(table.getSelectedColumn(), new CellaTesto( 
								matrice[table.getSelectedRow()].get(table.getSelectedColumn()).getContCell()));
				}
				
				/** Conversione della cella a tipo <b>CellaNumeri</b> */
				if(ritMatcherNumeri == true) 
				{
					if(sottoMatrice.getDisplayer()[table.getSelectedRow()][table.getSelectedColumn()].contains("=") == false)
					{
						/** Assegnamento del valore numerico nella sottomatrice */
						sottoMatrice.getDisplayer()[table.getSelectedRow()][table.getSelectedColumn()] = text;
					}
					
					/** Comportamento polimorfico di getContCell() -> esecuzione di getContCell() di CellaNumeri.java
					 * : ritorna il contenuto della cella */
					matrice[table.getSelectedRow()].set(table.getSelectedColumn(), new CellaNumeri(matrice[table.getSelectedRow()].get(table.getSelectedColumn()).getContCell()));
				}
				
				/** Conversione della cella a tipo <b>CellaOperazione</b> */
				if(ritMatcherOperazione == true) 
				{
					/** Assegnamento dell'operazione originaria nella sottomatrice */
					sottoMatrice.getDisplayer()[table.getSelectedRow()][table.getSelectedColumn()] = text;
					
					/** Creazione della nuova cella di tipo CellaOperazione */
					matrice[table.getSelectedRow()].set(table.getSelectedColumn(),
							new CellaOperazione(matrice, table, text));
					
					/** Comportamento polimorfico di toString() -> esecuzione di toString() di CellaOperazione.java: 
					 * Assegnazione del dato di ritorno della CellaOperazione al DefaultTableModel */
					dati.setValueAt(matrice[table.getSelectedRow()].get(table.getSelectedColumn()).toString(), table.getSelectedRow(), table.getSelectedColumn());
				}
				
				/** Se la cella non contiene testo, o numeri, o formule allora si tratta di una cella vuota, e dovra' essere riconvertita al tipo <b>CellaGenerica</b> */
				if(ritMatcherTesto == false && ritMatcherNumeri == false && ritMatcherOperazione == false) 
				{
					matrice[table.getSelectedRow()].set(table.getSelectedColumn(), new CellaGenerica());
				}

				/** Stampa del contenuto e del tipo della cella selezionata nel JTextField */
				classeCellaSelezionata = matrice[table.getSelectedRow()].get(table.getSelectedColumn()).getClass() + "";
				classeCellaSelezionata = "     \t\t\tTipo cella selezionato: " + classeCellaSelezionata.substring(6);
				operationDisplayer.setText("Contenuto cella: " + sottoMatrice.getDisplayer()[table.getSelectedRow()][table.getSelectedColumn()] + classeCellaSelezionata);
				
				/** Creazione di un Thread predisposto al salvataggio delle due strutture dati se non e' ancora stato creato uno in precedenza */
				if (threadCreato == false)
				{
					threadCreato = true;
					
				    percorsoCorrente = new File("");
				    
				    Runnable salvataggioAutomatico = new Runnable() 
				    {
				    	/**	Metodo eseguito dal Thread */
				        public synchronized void run() 
				        {
				        	if (!table.isEditing())
				        	{				        		
				        		try 
				        		{
				        			/** In base al sistema operativo in uso cambio il percorso per il salvataggio automatico */
				        			OS = System.getProperty("os.name");
				        			
				        			if (OS.contains("Windows") || OS.contains("Windows 10") || OS.contains("Windows 11"))
				        				nomeAutosalvataggio = "\\autosave";
				        			else
				        				nomeAutosalvataggio = "/autosave";
				        			
				        			/** Salvataggio la struttura dati principale su file binario con nome "autosave" */
				        			FileOutputStream fos = new FileOutputStream(percorsoCorrente.getAbsolutePath() + nomeAutosalvataggio);
				        			ObjectOutputStream oos = new ObjectOutputStream(fos);
				        			oos.writeObject(matrice);
				        			oos.close();
				        			
				        			/** Salvataggio la sottomatrice su file binario con suffisso "autosave.sottomatrice" */
				        			FileOutputStream fos2 = new FileOutputStream(percorsoCorrente.getAbsolutePath() + nomeAutosalvataggio + ".sottoMatrice");
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
				        }
				    };
				    
				    /** Creazione di un pool formato da un singolo Thread */
				    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
				    
				    /** Abilitazione dell'esecuzione del salvataggio automatico dopo il ritardo iniziale di [<b>delaySalvataggio</b>] secondi, a periodi regolari definiti dalla variabile timerAutosalvataggio */
				    executor.scheduleAtFixedRate(salvataggioAutomatico, delaySalvataggio, timerAutosalvataggio, TimeUnit.SECONDS);
				    
				    /** Se viene aperta una nuova finestra attraverso il bottone "Nuovo" interrompi i thread correntemente in uso */
					opzioneMenuFile1.addActionListener(new ActionListener() 
					{
					    public void actionPerformed(ActionEvent e)
					    {
					    	executor.shutdownNow();
					    }
					});
				}
			}	
		});
		
		/** Inizializzazione menu File */
		barraFile = new JMenuBar();
		menuFile = new JMenu("File");
		opzioneMenuFile1 = new JMenuItem("Nuovo");
		opzioneMenuFile2 = new JMenuItem("Apri");
		opzioneMenuFile3 = new JMenuItem("Salva con nome");
		opzioneMenuFile4 = new JMenuItem("Esci");
		menuFile.add(opzioneMenuFile1);
        menuFile.add(opzioneMenuFile2);
        menuFile.add(opzioneMenuFile3);
        menuFile.add(opzioneMenuFile4);
        barraFile.add(menuFile);
        
        /** Inizializzazione menu Finestra */
        barraFinestra = new JMenuBar();
        menuFinestra = new JMenu("Finestra");
        sottomenuTema = new JMenu("Tema");
        opzioneSottomenuTema1 = new JMenuItem("Tema 1");
        opzioneSottomenuTema2 = new JMenuItem("Tema 2");
        sottomenuTema.add(opzioneSottomenuTema1);
        sottomenuTema.add(opzioneSottomenuTema2);
        menuFinestra.add(sottomenuTema);
        barraFinestra.add(menuFinestra);

        /** Inizializzazione menu Aiuto */
        barraHelp = new JMenuBar();
        menuHelp = new JMenu("Aiuto");
        opzioneMenuHelp1 = new JMenuItem("Documentazione");
        opzioneMenuHelp2 = new JMenuItem("Traccia d'esame");
        menuHelp.add(opzioneMenuHelp1);
        menuHelp.add(opzioneMenuHelp2);
        barraHelp.add(menuHelp);
        
        /** Inizializzazione dei JPanel 
         * 	pannelloNordUpper: contiene il menu
         *  pannelloNord: contiene pannelloNordUpper e il JTextField */
        pannelloNordUpper = new JPanel();
        pannelloNord = new JPanel();
        leftAlignment = new FlowLayout();
        leftAlignment.setAlignment(FlowLayout.LEFT);
        pannelloNord.setLayout(new BorderLayout());
        pannelloNordUpper.setLayout(leftAlignment);
        pannelloNordUpper.add(barraFile);
        pannelloNordUpper.add(barraFinestra);
        pannelloNordUpper.add(barraHelp);
        pannelloNord.add(pannelloNordUpper, BorderLayout.NORTH);
        pannelloNord.add(operationDisplayer, BorderLayout.SOUTH);
        pannelloNordUpper.setBackground(new Color(188, 208, 251));

        /** Inizializzazione JTable e JScrollPane */
		table.setCellSelectionEnabled(true);
		table.setBackground(new Color(232, 255, 250));
		table.setRowHeight(22);
		
		/** Imposta la prima colonna con background grigio e testo allineato al centro e le barre di scorrimento */
		sp = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		setPrimaColonna = new DefaultTableCellRenderer();
	    setPrimaColonna.setBackground(new Color(238, 238, 238, 255)); 
	    setPrimaColonna.setHorizontalAlignment(JLabel.CENTER);
	    table.getColumnModel().getColumn(0).setCellRenderer(setPrimaColonna);
	    table.getTableHeader().setReorderingAllowed(false);
	    table.getColumnModel().getColumn(0).setMaxWidth(30);
	    
	    /** Evita che la JTable non ridimensioni automaticamente la colonne al variare della finestra */
	    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); 
	    
	    /** Abilita la JTable a selezionare più righe e colonne contigue con il tasto CTRL */
	    table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
	    
	    finestra = new Frame(pannelloNord, sp);
	    finestra.getF().setVisible(true);
	    
	    /** Creazione dell'oggetto incaricato dell'implementazione delle funzionalita' del menu */
	    new Menu(dim, dati, table, finestra, 
	    		opzioneMenuFile1, opzioneMenuFile2, opzioneMenuFile3, opzioneMenuFile4,
	    		opzioneSottomenuTema1, opzioneSottomenuTema2,
	    		opzioneMenuHelp1, opzioneMenuHelp2, 
	    		matrice, sottoMatrice);
	}
}