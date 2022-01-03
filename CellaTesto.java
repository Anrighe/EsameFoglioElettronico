/**	
 * 	@author Enrico Marras
 * 	@version Java SE Development Kit 17
 * 	<body style="background-color:#A4BEDA;"></body>
 */ 
public class CellaTesto extends CellaGenerica
{
	private static final long serialVersionUID = 1L;

	public CellaTesto(String cont)
	{
		super();
		super.setContCell(cont);		
	}
	
	public String toString()
	{
		return super.getContCell();
	}
}
