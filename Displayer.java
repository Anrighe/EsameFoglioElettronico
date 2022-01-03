import java.io.Serializable;

/**	Classe che crea la struttura dati secondaria utilizzata per memorizzare cos'era precedentemente una cella
 * 	<p>Nel caso di una cella di tipo operazione memorizza la stringa che ha determinato un risultato e in tutti gli altri casi memorizza il contenuto attuale della cella</p>
 * 	@author Enrico Marras
 * 	@version Java SE Development Kit 17
 * 	<body style="background-color:#A4BEDA;"></body>
 */ 
public class Displayer implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String displayer[][];
	
	/**	Ritorna la matrice di String displayer
	 * 	@return displayer
	 */
	public String[][] getDisplayer()
	{
		return displayer;
	}
	
	/**	Crea la matrice vuota di String di dimensione <b>dim</b> * <b>dim</b>
	 * 	@param dim Dimensione di altezza e larghezza del foglio elettronico
	 */
	public Displayer(int dim)
	{
		displayer = new String[dim][dim];
		
		for (int i = 0; i < dim; i++)
		{
			for (int j = 0; j < dim; j++)
			{
				displayer[i][j] = "";
			}
		}
	}
}
