public class CellaNumeri  extends CellaGenerica
{
	private static final long serialVersionUID = 1L;
	
	private int contCellInt;
	
	@Override
	public int getContCellInt()
	{
		return contCellInt;
	}
	
	public CellaNumeri(String cont) //costruttore
	{
		super();
        contCellInt = Integer.parseInt(cont); //conversione da String a int	
	}
	
	public String toString()
	{
		return contCellInt + "";
	}
}
