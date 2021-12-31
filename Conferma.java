import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Conferma 
{
	private JFrame popUpConferma;
	private JPanel pannelloSud;
	private JLabel label;
	private JButton bottoneSi;
	private JButton bottoneNo;
	
	
	public JFrame getPopUpConferma()
	{
		return popUpConferma;
	}
	
	public JPanel getPannelloSud()
	{
		return pannelloSud;
	}
	
	public JButton getBottoneSi()
	{
		return bottoneSi;
	}
	
	public JButton getBottoneNo()
	{
		return bottoneNo;
	}
	
	
	
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
