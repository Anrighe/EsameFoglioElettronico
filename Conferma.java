import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**	Gestisce il popup di conferma per la sovrascrittura durante il salvataggio di un file
 * 	@author Enrico Marras
 * 	@version Java SE Development Kit 17
 * 	<body style="background-color:#A4BEDA;"></body>
 */ 
public class Conferma 
{
	/** JFrame del popup di conferma */
	private JFrame popUpConferma;
	
	/** Contiene i due bottoni */
	private JPanel pannelloSud;
	
	/** Informazioni sulla presenza di un altro file con lo stesso nome */
	private JLabel label;
	
	/** Per procedere con la sovrascrittura */
	private JButton bottoneSi;
	
	/** Per annullare la procedura di salvataggio */
	private JButton bottoneNo;
	
	/**	@return Ritorna il JFrame del popup di conferma
	 */
	public JFrame getPopUpConferma()
	{
		return popUpConferma;
	}
	
	/**	@return Ritorna il JPanel del pannello contenente i due bottoni */
	public JPanel getPannelloSud()
	{
		return pannelloSud;
	}
	
	/**	@return Ritorna il bottone per confermare la procedura di sovrascrittura */
	public JButton getBottoneSi()
	{
		return bottoneSi;
	}
	
	/**	@return Ritorna il bottone per cancellare la procedura di sovrascrittura */
	public JButton getBottoneNo()
	{
		return bottoneNo;
	}
	
	/**	Crea un JFrame contenente le le informazioni sullo stato del salvataggio nel caso di una sovrascrittura imminente, lasciando decidere all'utente come procedere */
	public Conferma()
	{
		label = new JLabel("<html><br/>È già presente un file con lo stesso nome, <br/>si desidera sovrascrivelo?<html>", SwingConstants.CENTER);		
		
		pannelloSud = new JPanel();
		pannelloSud.setLayout(new FlowLayout());
		
		bottoneSi = new JButton("Si");
		bottoneNo = new JButton("No");
		
		pannelloSud.add(bottoneSi);
		pannelloSud.add(bottoneNo);
		
		popUpConferma = new JFrame("Conferma salvataggio");
		popUpConferma.setLayout(new BorderLayout());
		popUpConferma.add(label, BorderLayout.NORTH);
		popUpConferma.add(pannelloSud, BorderLayout.SOUTH);

		popUpConferma.pack();
		
		popUpConferma.setBounds(100, 100, 325, 150);
		popUpConferma.setResizable(false);
		popUpConferma.setLocationRelativeTo(null);
		popUpConferma.setVisible(true);
	}
}
