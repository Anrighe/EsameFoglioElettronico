/**	Cella che contiene testo.
 * 	<p>Per essere convertita al tipo <b>CellaTesto</b>, una cella deve contenere almeno un carattere non numerico</p>
 * 	@author Enrico Marras
 * 	@version Java SE Development Kit 17
 * 	<body style="background-color:#A4BEDA;"></body>
 */ 
public class CellaTesto extends CellaGenerica
{
	private static final long serialVersionUID = 1L;

	/**	Richiama il costruttore della classe CellaGenerica passando il contenuto della cella che e' stata modificata
	 * 	@param cont Contenuto della cella modificata
	 */
	public CellaTesto(String cont)
	{
		super();
		super.setContCell(cont);		
	}
	
	/** Ritorna il contenuto della cella */
	public String toString()
	{
		return super.getContCell();
	}
}
