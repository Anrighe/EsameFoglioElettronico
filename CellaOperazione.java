import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.JTable;

/**	
 * 	@author Enrico Marras
 * 	@version Java SE Development Kit 17
 * 	<body style="background-color:#A4BEDA;"></body>
 */ 
public class CellaOperazione extends CellaGenerica
{
	private static final long serialVersionUID = 1L;
	
	private int result;
	private boolean addizione = false;
	private boolean operazioneValida;
	private String azioneOperazione;
	
	private int colCell1;
	private int rowCell1;
	private int colCell2;
	private int rowCell2;
	private int posSegno;
	
	private String classeCella1;
	private String classeCella2;
	private String messaggio;
	
	private String cell1;
	private String cell2;

	private String tmp1 = "";
	private String tmp2 = "";


	private ArrayList<CellaGenerica>[] rifMatrice;
	
	public boolean isValidOperation(ArrayList<CellaGenerica> matrice[])
	{	
		if (matrice[rowCell1-1].get(colCell1).getClass() == CellaNumeri.class && matrice[rowCell2-1].get(colCell2).getClass() == CellaNumeri.class)
			return true;
		else
			return false;
	}
	
	public CellaOperazione(ArrayList<CellaGenerica> matrice[], JTable table, String text)
	{
		super();
		
		boolean ritMatcherOperazioniNumeri = false;
		
		Pattern patternOperazioniNumeri = Pattern.compile("^=[0-9]++[\\+|-][0-9]++$");
		Matcher matcherOperazioniNumeri = patternOperazioniNumeri.matcher(text);
		ritMatcherOperazioniNumeri = matcherOperazioniNumeri.find();
		
		
		System.out.println("ENTRO IN CONVERSIONE CELLA OPERAZIONE");
		
		int plus = text.indexOf("+");
		int minus = text.indexOf("-");
		
		if (plus != -1) //è un'addizione
		{
			addizione = true;
			posSegno = plus;
			azioneOperazione = "sommare";
		}
		else //è una sottrazione
		{
			addizione = false;
			posSegno = minus;
			azioneOperazione = "sottrarre";
		}
		
		if (ritMatcherOperazioniNumeri == true)
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
		else
		{
			System.out.println("OPERAZIONE TRA INDIRIZZI - " + text); //debug
			rifMatrice = matrice;
			
			
			cell1 = String.valueOf(text.charAt(1));
			cell2 = String.valueOf(text.charAt(posSegno+1));
			
			for (int i = 2; i < posSegno; i++)
				cell1 = cell1 + String.valueOf(text.charAt(i));
			
			for (int i = posSegno+2; i < text.length(); i++)
				cell2 = cell2 + String.valueOf(text.charAt(i));
			
			colCell1 = (Integer.valueOf(cell1.charAt(0))) - Integer.valueOf('A') + 1;
			colCell2 = (Integer.valueOf(cell2.charAt(0))) - Integer.valueOf('A') + 1;
			
			for (int i = 1; i < cell1.length(); i++)
				tmp1 = tmp1 + String.valueOf(cell1.charAt(i));
			
			for (int i = 1; i < cell2.length(); i++)
				tmp2 = tmp2 + String.valueOf(cell2.charAt(i));
			
			System.out.println("tmp1: " + tmp1); //tmp1 è ancora String va convertito in int e assegnato a rowCell1
			System.out.println("tmp2: " + tmp2); //tmp2 è ancora String va convertito in int e assegnato a rowCell2
			
			rowCell1 = Integer.valueOf(tmp1); //conversione di tmp1 ad intero
			rowCell2 = Integer.valueOf(tmp2); //conversione di tmp2 ad intero
			
			System.out.println("cell1: " + cell1); //debug
			System.out.println("cell2: " + cell2); //debug
			System.out.println("colCell1:" + colCell1); //debug
			System.out.println("colCell2:" + colCell2); //debug
			System.out.println("rowCell1:" + rowCell1); //debug
			System.out.println("rowCell2:" + rowCell2); //debug
			
			System.out.println("operatore1: " + matrice[rowCell1-1].get(colCell1).getContCell());
			System.out.println("operatore2: " + matrice[rowCell2-1].get(colCell2).getContCell());
			
			operazioneValida = isValidOperation(matrice);
			
			if (operazioneValida == true)
			{
				if (addizione == true) //l'operazione è un'addizione
				{
					result = matrice[rowCell1-1].get(colCell1).getContCellInt() + matrice[rowCell2-1].get(colCell2).getContCellInt();
					System.out.println("Result: " + result);
				}
				else //l'operazione è una sottrazione
				{
					result = matrice[rowCell1-1].get(colCell1).getContCellInt() - matrice[rowCell2-1].get(colCell2).getContCellInt();
					System.out.println("Result: " + result);
				}
			}
		}
	}
	
	//se l'operazione è tra due Celle di numeri ritorna il risultato convertito da intero a stringa
	//altrimenti gestisce l'errore segnalando quali classi si è cercato di sommare e ritornando la stringa "Errore"
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
