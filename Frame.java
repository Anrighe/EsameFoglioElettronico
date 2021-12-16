import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class Frame 
{	
	private JFrame f;
	
	public JFrame getF()
	{
		return f;
	}
	
	public Frame(JPanel pannelloNord, JScrollPane sp)
	{
		f = new JFrame("Esame Programmazione a Oggetti 2021/2022: Foglio Elettronico - Enrico Marras - Matricola: 152336");
		f.setLayout(new BorderLayout());
		
		f.add(pannelloNord, BorderLayout.NORTH);
	    f.add(sp, BorderLayout.CENTER);

		
		f.pack();
		f.setLocationRelativeTo(null);

		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setBounds(100, 100, 1280, 720);
		
		
		
	}
}
