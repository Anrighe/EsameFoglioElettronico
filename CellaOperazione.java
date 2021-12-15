import java.util.ArrayList;

import javax.swing.JTable;

public class CellaOperazione extends CellaGenerica
{
	public int result;
	private boolean addizione = false;
	private boolean sottrazione = false;
	
	private int colCell1;
	private int rowCell1;
	private int colCell2;
	private int rowCell2;
	private int posSegno;
	

	private String displayText; 
	
	public CellaOperazione(ArrayList<CellaGenerica> matrice[], JTable table, String text)
	{
		super();
		
		displayText = text;
		
		System.out.println("ENTRO IN CONVERSIONE CELLA OPERAZIONE");
		
		String cell1;
		String cell2;


		String tmp1 = "";
		String tmp2 = "";
		
		int plus = text.indexOf("+");
		int minus = text.indexOf("-");
				
		if (plus != -1) //è un'addizione
		{
			addizione = true;
			posSegno = plus;
		}
		else //è una sottrazione
		{
			sottrazione = true;
			posSegno = minus;
		}
		
		
		//TODO:
		//si potrebbe implementare la divisione di righe e colonne degli 
		//operandi in interi direttamente qua senza passare da sottostringhe


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
		System.out.println("tmp2: " + tmp2);
		
		rowCell1 = Integer.valueOf(tmp1);
		rowCell2 = Integer.valueOf(tmp2);
		

		System.out.println("cell1: " + cell1); //debug
		System.out.println("cell2: " + cell2); //debug
		System.out.println("colCell1:" + colCell1); //debug
		System.out.println("colCell2:" + colCell2); //debug
		System.out.println("rowCell1:" + rowCell1); //debug
		System.out.println("rowCell2:" + rowCell2); //debug
		
		System.out.println("operatore1: " + matrice[rowCell1-1].get(colCell1).getContCell());
		System.out.println("operatore2: " + matrice[rowCell2-1].get(colCell2).getContCell());
		
		if (addizione == true)
		{
			result = matrice[rowCell1-1].get(colCell1).getContCell() + matrice[rowCell2-1].get(colCell2).getContCell();
			System.out.println("Result: " + result);
		}
		else
		{
			result = matrice[rowCell1-1].get(colCell1).getContCell() - matrice[rowCell2-1].get(colCell2).getContCell();
			System.out.println("Result: " + result);
		}
		
	}
	/*public String getDisplayer()
	{
		return displayText;
	}*/
	
	
	//ritorna il risultato dell'operazione
	public String toString()
	{
		return String.valueOf(result);
	}
	
}
