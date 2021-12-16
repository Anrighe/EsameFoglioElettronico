import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

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
		
		

		table.setCellSelectionEnabled(true);
		table.setBackground(new Color(232, 255, 250));
		table.setRowHeight(20);
		
		//setta la prima colonna con background grigio e testo allineato al centro e imposta la barra di scorrimento
		JScrollPane sp = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    DefaultTableCellRenderer setPrimaColonna = new DefaultTableCellRenderer();
	    setPrimaColonna.setBackground(new Color(238, 238, 238, 255));
	    setPrimaColonna.setHorizontalAlignment(JLabel.CENTER);
	    table.getColumnModel().getColumn(0).setCellRenderer(setPrimaColonna);

	    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); //JTable must not auto-resize the columns by setting the AUTO_RESIZE_OFF mode
	    
	    Frame finestra = new Frame(operationDisplayer, sp);
	    finestra.getF().setVisible(true);

	}
}