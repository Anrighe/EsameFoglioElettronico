import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**	Gestisce la creazione e la configurazione del JPanel
 * 	@author Enrico Marras
 * 	@version Java SE Development Kit 17
 * 	<body style="background-color:#A4BEDA;"></body>
 */ 
public class Frame 
{	
	private JFrame f;
	
	/** Ritorna il JFrame
	 * 	@return f JFrame */
	public JFrame getF()
	{
		return f;
	}
	
	/**	Crea il JFrame, gestisce i parametri della finestra e inserisce al suo interno il JPanel <b>pannelloNord</b> e il JScrollPane <b>sp</b>
	 * 	@param pannelloNord Contiene il menu e il JTextField
	 * 	@param sp Contiene il JTable
	 */
	public Frame(JPanel pannelloNord, JScrollPane sp)
	{
		f = new JFrame("Esame Programmazione a Oggetti 2021/2022: Foglio Elettronico - Enrico Marras - Matricola: 152336");
		f.setLayout(new BorderLayout());
		
		f.add(pannelloNord, BorderLayout.NORTH);
	    f.add(sp, BorderLayout.CENTER);
		
		f.pack();
		f.setBounds(100, 100, 1280, 720);
		f.setLocationRelativeTo(null);

		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
