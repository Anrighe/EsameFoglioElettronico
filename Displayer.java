import java.io.Serializable;

public class Displayer implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String displayer[][];
	
	public String[][] getDisplayer()
	{
		return displayer;
	}
	
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
