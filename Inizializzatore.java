import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.Desktop;

public class Inizializzatore 
{
	private int dim = 27;

	public Inizializzatore()
	{
		@SuppressWarnings("unchecked")
		ArrayList<CellaGenerica> matrice[] = new ArrayList[dim];

		Displayer sottoMatrice = new Displayer(dim);
		
		for (int i = 0; i < dim; i++)
		{
			matrice[i] = new ArrayList<CellaGenerica>();
			for (int j = 0; j < dim; j++)
			{
				matrice[i].add(new CellaGenerica());
			}
		}
		
		String nomeColonne[] = new String[dim]; //array di stringhe che verrà utilizzato per avere i nomi delle colonne ordinati da lettere

		for(int i = 0; i < dim; i++)
		{
			if (i == 0)
				nomeColonne[i] = "";
			else
				nomeColonne[i] = String.valueOf((char)(64 + i));
		}

		
		DefaultTableModel dati = new DefaultTableModel(nomeColonne, dim)
		{
			private static final long serialVersionUID = 1L;

			@Override //del metodo isCellEditable di DefaultTableModel
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
		
		//JTextField che mostra la vera operazione che avviene nella cella
		JTextField operationDisplayer = new JTextField();
		operationDisplayer.setBackground(new Color(199, 217, 252));
		operationDisplayer.setEditable(false);
		operationDisplayer.setFont(new Font("Calibri", Font.BOLD, 18));
		
		JTable table = new JTable(dati);
		
		TableModelListener e;
		table.getModel().addTableModelListener(e = new TableModelListener()
		{
			public void tableChanged(TableModelEvent e) 
			{
				System.out.println("Contenuto cella modificato: " + table.getValueAt(table.getSelectedRow(), table.getSelectedColumn()));
				
				System.out.println("RIGA SELEZIONATA: " + table.getSelectedRow());
				System.out.println("COLONNA SELEZIONATA: " + table.getSelectedColumn());
				
				String text;
				text = (String) table.getValueAt(table.getSelectedRow(), table.getSelectedColumn());
				System.out.println("text: " + text);
				
				boolean ritMatcherOperazione = false;
				boolean ritMatcherNumeri = false;
				boolean ritMatcherTesto = false;
				
				if (!text.equals(""))
				{
					Pattern patternOperazioni = Pattern.compile("^=[A-Z][1-2]{0,1}[0-9][\\+|-][A-Z][1-2]{0,1}[0-9]$");
					Matcher matcherOperazioni = patternOperazioni.matcher(text);
					ritMatcherOperazione = matcherOperazioni.find();
					System.out.println("Operazione: " + ritMatcherOperazione); // stampa true se è un'operazione
					
					
					Pattern patternNumeri = Pattern.compile("^-{0,1}[0-9]+$");
					Matcher matcherNumeri = patternNumeri.matcher(text);
					ritMatcherNumeri = matcherNumeri.find();	
					System.out.println("Numeri: " + ritMatcherNumeri); // stampa true se sono numeri
					
					
					if (ritMatcherNumeri == false && ritMatcherOperazione == false) //se entra è già testo e non è necessario effettuare controlli
					{	
						ritMatcherTesto = true;
						//Pattern patternTesto = Pattern.compile("^[A-Za-z0-9]+$"); 
						//Matcher matcherTesto = patternTesto.matcher(text);
						System.out.println("Testo : " + ritMatcherTesto); // stampa true se è testo
					}
				}
				
				//assegna il valore nella struttura dati alla stringa temporanea text
				matrice[table.getSelectedRow()].get(table.getSelectedColumn()).contCell = text;

				
				if (ritMatcherTesto == true) //conversione della cella a tipo CellaTesto
				{
					//assegna il valore nella sottomatrice
					sottoMatrice.getDisplayer()[table.getSelectedRow()][table.getSelectedColumn()] = text;
					
					matrice[table.getSelectedRow()].set(table.getSelectedColumn(), new CellaTesto( 
								matrice[table.getSelectedRow()].get(table.getSelectedColumn()).contCell));
				}
				
				if(ritMatcherNumeri == true) //conversione della cella a tipo CellaNumeri
				{
					if(sottoMatrice.getDisplayer()[table.getSelectedRow()][table.getSelectedColumn()].contains("=") == false)
					{
						//assegna il valore nella sottomatrice
						sottoMatrice.getDisplayer()[table.getSelectedRow()][table.getSelectedColumn()] = text;
					}
					

					System.out.println("ENTRO IN CONVERSIONE CELLA NUMERICA");
					matrice[table.getSelectedRow()].set(table.getSelectedColumn(), new CellaNumeri(matrice[table.getSelectedRow()].get(table.getSelectedColumn()).contCell));
					
					
				}
				
				if(ritMatcherOperazione == true) //conversione della cella a tipo CellaOperazione
				{
					//assegna il valore nella sottomatrice
					sottoMatrice.getDisplayer()[table.getSelectedRow()][table.getSelectedColumn()] = text;
					
					matrice[table.getSelectedRow()].set(table.getSelectedColumn(),
							new CellaOperazione(matrice, table, text));
					
					//TODO: da revisionare
					dati.setValueAt(matrice[table.getSelectedRow()].get(table.getSelectedColumn()).toString(), table.getSelectedRow(), table.getSelectedColumn());

					
				}
				
				if(ritMatcherTesto == false && ritMatcherNumeri == false && ritMatcherOperazione == false) //se non è una cella di testo, o una cella numero o una cella operazione allora è una cella vuota e deve essere riconvertita al tipo CellaGenerica
				{
					System.out.println("La cella di riga: " + table.getSelectedColumn() + " e colonna: " + table.getSelectedColumn() + " diventa una cella generica");
					matrice[table.getSelectedRow()].set(table.getSelectedColumn(), new CellaGenerica());
				}
				
				
				System.out.println("Aggiorno il contenuto della cella di riga " + table.getSelectedRow() + " e colonna " + table.getSelectedColumn() + " a: " + text);
				
				System.out.println("Contenuto struttura dati:");
				for (int i = 0; i < dim; ++i)
					System.out.println(matrice[i]);

				operationDisplayer.setText(sottoMatrice.getDisplayer()[table.getSelectedRow()][table.getSelectedColumn()]);
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
	    
	    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); //JTable must not auto-resize the columns by setting the AUTO_RESIZE_OFF mode
	    
	    Frame finestra = new Frame(pannelloNord, sp);
	    finestra.getF().setVisible(true);
	    
	    
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