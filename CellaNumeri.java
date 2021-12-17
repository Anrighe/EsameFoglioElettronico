public class CellaNumeri  extends CellaGenerica
{
	private int contCell;
	
	public int getContCell()
	{
		return contCell;
	}
	
	public CellaNumeri(String cont) //costruttore
	{
		super();
        contCell = Integer.parseInt(cont); //conversione da String a int	
	}
	
	public String toString()
	{
		return contCell + "";
	}
}
