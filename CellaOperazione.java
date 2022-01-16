import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**	Cella che contiene una formula tra coordinate di celle o tra valori numerici.
 * 	<p>Per essere convertita al tipo <b>CellaOperazione</b>, una cella deve contenere una delle due seguenti opzioni:</p>
 * 	<p>- &emsp;Addizioni e sottrazioni tra coordinate di celle del tipo: <b>=A1+A2</b></p>
 * 	<p>- &emsp;Addizioni e sottrazioni tra valori numerici del tipo: <b>=300+22</b></p>
 * 	@author Enrico Marras
 * 	@version Java SE Development Kit 17
 * 	<body style="background-color:#A4BEDA;"></body>
 */ 
public class CellaOperazione extends CellaGenerica
{
	private static final long serialVersionUID = 1L;
	
	/** Memorizza il risultato delle operazioni */
	private int result;
	
	/** True se l'operazione e' un'addizione, false se si tratta di una sottrazione */
	private boolean addizione = false;
	
	/** Memorizza il ritorno del metodo isValidOperaion */
	private boolean operazioneValida;
	
	/** In caso di errore memorizza l'operazione effettuata per comunicarla all'utente */
	private String azioneOperazione;
	
	/** Colonna del primo operando */
	private int colCell1;
	
	/** Riga del primo operando */
	private int rowCell1;
	
	/** Colonna del secondo operando */
	private int colCell2;
	
	/** Riga del secondo operando */
	private int rowCell2;
	
	/** Posizione del segno all'interno della stringa dell'operazione */
	private int posSegno;
	
	/** Classe della cella del primo operando */
	private String classeCella1;
	
	/** Classe della cella del secondo operando */	
	private String classeCella2;
	
	/** Messaggio da comunicare all'utente in caso di errore */
	private String messaggio;
	
	/** Coordinata del primo operando */
	private String cell1;
	
	/** Coordinata del secondo operando */
	private String cell2;

	/** Riga del primo operando */
	private String tmp1 = "";
	
	/** Riga del secondo operando */
	private String tmp2 = "";

	/** Riferimento alla struttura dati principale */
	private ArrayList<CellaGenerica>[] rifMatrice;
	
	/**	Se entrambe le delle celle sono di tipo <b>CellaNumeri</b> ritorna true, altrimenti false
	 * 	@param matrice Struttura dati principale
	 * 	@return true se l'operazione e' valida, false altrimenti
	 */
	public boolean isValidOperation(ArrayList<CellaGenerica> matrice[])
	{	
		if (matrice[rowCell1-1].get(colCell1).getClass() == CellaNumeri.class && matrice[rowCell2-1].get(colCell2).getClass() == CellaNumeri.class)
			return true;
		else
			return false;
	}
	
	/** Verifica se si tratta di un'operazione valida e gestisce le possibili casistiche ritornando il risultato.
	 * 	<p>- &emsp;Gestisce le operazioni tra coordinate di celle e tra interi numerici (positivi e negativi)</p>
	 * 	<p>- &emsp;In caso di una somma tra celle di tipo non corretto, viene segnalato all'utente un errore</p>
	 * 	@param matrice Struttura dati principale
	 * 	@param table Riferimento alla JTable
	 * 	@param text Formula contenuta all'interno dell'operazione
	 */
	public CellaOperazione(ArrayList<CellaGenerica> matrice[], JTable table, String text)
	{
		super();
		
		/** Controllo per verificare se si tratti di un'operazione tra interi */
		boolean ritMatcherOperazioniNumeri = false;
		
		Pattern patternOperazioniNumeri = Pattern.compile("^=[0-9]++[\\+|-][0-9]++$");
		Matcher matcherOperazioniNumeri = patternOperazioniNumeri.matcher(text);
		ritMatcherOperazioniNumeri = matcherOperazioniNumeri.find();
		
		System.out.println("ENTRO IN CONVERSIONE CELLA OPERAZIONE");
		
		int plus = text.indexOf("+");
		int minus = text.indexOf("-");
		
		if (plus != -1) /** L'operazione e' un'addizione */
		{
			addizione = true;
			posSegno = plus;
			azioneOperazione = "sommare";
		}
		else /** L'operazione e' una sottrazione */
		{
			addizione = false;
			posSegno = minus;
			azioneOperazione = "sottrarre";
		}
		
		if (ritMatcherOperazioniNumeri == true) /** Operazione tra numeri */
		{
			System.out.println("OPERAZIONE TRA NUMERI - " + text); //debug
			//TODO: ridurre l'uso del codice doppiato da sotto
			
			operazioneValida = true;
			System.out.println("Plus: " + plus); //debug
			System.out.println("Minus: " + minus); //debug
			
			cell1 = String.valueOf(text.charAt(1));
			cell2 = String.valueOf(text.charAt(posSegno+1));
			
			for (int i = 2; i < posSegno; i++)
				cell1 = cell1 + String.valueOf(text.charAt(i));
			
			for (int i = posSegno+2; i < text.length(); i++)
				cell2 = cell2 + String.valueOf(text.charAt(i));
			
			System.out.println("Cell1: " + cell1); //debug
			System.out.println("Cell2: " + cell2); //debug
			
			if (addizione == true)
				result = Integer.valueOf(cell1) + Integer.valueOf(cell2);
			else
				result = Integer.valueOf(cell1) - Integer.valueOf(cell2);
		}
		else /** Operazione tra coordinate di celle */
		{
			System.out.println("OPERAZIONE TRA INDIRIZZI - " + text); //debug
			rifMatrice = matrice;
			
			/*	Nel caso di =A1+A2
			 * 	cell1 conterra': A1
			 * 	cell2 conterra': A2
			 */
			cell1 = String.valueOf(text.charAt(1));
			cell2 = String.valueOf(text.charAt(posSegno+1));
			
			for (int i = 2; i < posSegno; i++)
				cell1 = cell1 + String.valueOf(text.charAt(i));
			
			for (int i = posSegno+2; i < text.length(); i++)
				cell2 = cell2 + String.valueOf(text.charAt(i));
			
			/** Conversione della colonna in numero */
			colCell1 = (Integer.valueOf(cell1.charAt(0))) - Integer.valueOf('A') + 1;
			colCell2 = (Integer.valueOf(cell2.charAt(0))) - Integer.valueOf('A') + 1;
			
			for (int i = 1; i < cell1.length(); i++)
				tmp1 = tmp1 + String.valueOf(cell1.charAt(i));
			
			for (int i = 1; i < cell2.length(); i++)
				tmp2 = tmp2 + String.valueOf(cell2.charAt(i));
			
			
			System.out.println("cell1: " + cell1);
			System.out.println("cell2: " + cell2);
			System.out.println("tmp1: " + tmp1); //tmp1 e' ancora String va convertito in int e assegnato a rowCell1
			System.out.println("tmp2: " + tmp2); //tmp2 e' ancora String va convertito in int e assegnato a rowCell2
			
			rowCell1 = Integer.valueOf(tmp1); //conversione di tmp1 ad intero
			rowCell2 = Integer.valueOf(tmp2); //conversione di tmp2 ad intero
			
			System.out.println("cell1: " + cell1); //debug
			System.out.println("cell2: " + cell2); //debug
			System.out.println("colCell1:" + colCell1); //debug
			System.out.println("colCell2:" + colCell2); //debug
			System.out.println("rowCell1:" + rowCell1); //debug
			System.out.println("rowCell2:" + rowCell2); //debug
			
			System.out.println("operatore1: " + matrice[rowCell1-1].get(colCell1).getContCellInt()); //debug
			System.out.println("operatore2: " + matrice[rowCell2-1].get(colCell2).getContCellInt()); //debug
			
			operazioneValida = isValidOperation(matrice);
			
			if (operazioneValida == true)
			{
				if (addizione == true) /** L'operazione e' un'addizione */
				{
					result = matrice[rowCell1-1].get(colCell1).getContCellInt() + matrice[rowCell2-1].get(colCell2).getContCellInt();
					System.out.println("Result: " + result); //debug
				}
				else /** L'operazione e' una sottrazione */
				{
					result = matrice[rowCell1-1].get(colCell1).getContCellInt() - matrice[rowCell2-1].get(colCell2).getContCellInt();
					System.out.println("Result: " + result); //debug
				}
			}
		}
	}
	

	/**	Se l'operazione effettuata e' tra due Celle di tipo <b>CellaNumeri</b> o due cifre numeriche ritorna il risultato convertito da intero a stringa.
	 * 	In caso contrario segnala quali tipi di celle si e' cercato di sommare, ritornando la stringa "Errore"
	 */
	public String toString()
	{
		if(operazioneValida == true)
			return String.valueOf(result);
		else
		{
			classeCella1 = rifMatrice[rowCell1-1].get(colCell1).getClass() + "";
			classeCella1 = classeCella1.substring(6);

			classeCella2 = rifMatrice[rowCell2-1].get(colCell2).getClass() + "";
			classeCella2 = classeCella2.substring(6);
			messaggio = "<html>Impossibile " + azioneOperazione + " due celle di tipo <font color=\"red\">" + classeCella1 + "</font> e " + "<font color=\"red\">" + classeCella2 + "</font>";
			
    		JOptionPane.showMessageDialog(null, messaggio, "Errore", JOptionPane.INFORMATION_MESSAGE);
			return "Errore";
		}
	}
	
}
