import java.io.Serializable;

/**	Cella di tipo generico.
 * 	<p>Per essere convertita al tipo <b>CellaGenerica</b>, una cella non deve nulla</p>
 * 	@author Enrico Marras
 * 	@version Java SE Development Kit 17
 * 	<body style="background-color:#A4BEDA;"></body>
 */ 
public class CellaGenerica implements Serializable
{
	
	private static final long serialVersionUID = 1L;
	
	/** Contenuto della cella */
	private String contCell;
	
	/**	
	 * 	@return Il contenuto della cella
	 */
	public String getContCell()
	{
		return contCell;
	}
	
	/**	Imposta un contenuto nella cella
	 * 	@param text Il contenuto che si desidera impostare nella cella
	 */
	public void setContCell(String text)
	{
		contCell = text;
	}
	
	/** Imposta il contenuto della cella ad una stringa vuota */
	public CellaGenerica()
	{
		contCell = "";
	}
	
	/** Ritorna una stringa vuota, in quanto la CellaGenerica non deve contenere alcuna informazione */
	public String toString()
	{
		return "";
	}
}
