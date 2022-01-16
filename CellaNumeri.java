/**	Cella che contiene valori numerici positivi e negativi.
 * 	<p>Per essere convertita al tipo <b>CellaNumeri</b>, una cella deve contenere esclusivamente numeri</p>
 * 	<p>Massimo valore inseribile: <b>2147483647</b></p>
 * 	<p>Minimo valore inseribile: <b>-2147483648</b></p>
 * 	@author Enrico Marras
 * 	@version Java SE Development Kit 17
 * 	<body style="background-color:#A4BEDA;"></body>
 */ 
public class CellaNumeri  extends CellaGenerica
{
	private static final long serialVersionUID = 1L;
	
	/** Valore contenuto nella cella */
	private int contCellInt;
	
	@Override
	public int getContCellInt()
	{
		return contCellInt;
	}
	
	/** Converte il contenuto della cella appena modificata da String a int e lo salva nella variabile contCellInt */
	public CellaNumeri(String cont)
	{
		super();
        contCellInt = Integer.parseInt(cont); //conversione da String a int	
	}
	
	/** Ritorna il valore numerico contenuto nella cella convertito a String */
	public String toString()
	{
		return contCellInt + "";
	}
}
