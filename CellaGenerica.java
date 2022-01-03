import java.io.Serializable;

/**	
 * 	@author Enrico Marras
 * 	@version Java SE Development Kit 17
 * 	<body style="background-color:#A4BEDA;"></body>
 */ 
public class CellaGenerica implements Serializable
{
	
	private static final long serialVersionUID = 1L;
	
	private String contCell;

	public String getContCell()
	{
		return contCell;
	}
	
	public void setContCell(String text)
	{
		contCell = text;
	}
	
	public CellaGenerica() //costruttore
	{
		contCell = "";
	}
	
	public String toString()
	{
		return "";
	}

	public int getContCellInt() 
	{
		return 0;
	}
	


}
