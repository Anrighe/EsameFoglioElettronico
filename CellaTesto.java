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
