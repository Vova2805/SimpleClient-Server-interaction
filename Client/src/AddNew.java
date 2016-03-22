import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.SpringLayout;
import javax.swing.table.DefaultTableModel;

import java.awt.FlowLayout;
import net.miginfocom.swing.MigLayout;
import java.awt.Dimension;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import javax.swing.SwingConstants;

public class AddNew {

	public static JFrame frame;
	public static JTable table;
	public static DefaultTableModel model = new DefaultTableModel(3,3);;
	public static JTextField Back;
	public static boolean operation;
	public static String selectedTable;

	public AddNew()  {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					initialize();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(0, 153, 255));
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String query = "";
				if(operation)
				{
					query+="insert into [Lab2TestDB].dbo.["+selectedTable+"] Values (" ;
					for(int i=0;i<model.getRowCount();i++)
					{
						query+="'"+table.getValueAt(i, 1)+"'";
						if(i!=model.getRowCount()-1)
							query +=",";
					}
					query+=");";
				}
				else
				{
					query+="update [Lab2TestDB].dbo.["+selectedTable+"] set " ;
					for(int i=0;i<table.getRowCount();i++)
					{
						query+=table.getValueAt(i, 0)+" = ";
						query+="'"+table.getValueAt(i, 1)+"'";
						if(i!=model.getRowCount()-1)
							query +=",";
					}
					query+=" where "+table.getValueAt(0, 0)+"= "+table.getValueAt(0, 1);
					query+=";";
				}
				Back.setText(query);
				frame.setVisible(false);
				frame.dispose();
			}
		});
		btnSubmit.setPreferredSize(new Dimension(65, 50));
		frame.getContentPane().add(btnSubmit, BorderLayout.SOUTH);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(0, 153, 255));
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		SpringLayout sl_panel = new SpringLayout();
		panel.setLayout(sl_panel);
		
		JLabel lblFillAllFields = new JLabel("Fill all fields");
		lblFillAllFields.setHorizontalAlignment(SwingConstants.CENTER);
		lblFillAllFields.setHorizontalTextPosition(SwingConstants.CENTER);
		sl_panel.putConstraint(SpringLayout.NORTH, lblFillAllFields, 24, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, lblFillAllFields, 106, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, lblFillAllFields, 49, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, lblFillAllFields, -113, SpringLayout.EAST, panel);
		lblFillAllFields.setFont(new Font("Tahoma", Font.BOLD, 20));
		panel.add(lblFillAllFields);
		
		JScrollPane scrollPane = new JScrollPane();
		sl_panel.putConstraint(SpringLayout.NORTH, scrollPane, 67, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, scrollPane, -25, SpringLayout.SOUTH, panel);
		scrollPane.setBorder(new LineBorder(new Color(130, 135, 144)));
		sl_panel.putConstraint(SpringLayout.WEST, scrollPane, 29, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.EAST, scrollPane, -30, SpringLayout.EAST, panel);
		panel.add(scrollPane);
		
		table = new JTable(model);
		table.setRowSelectionAllowed(false);
		scrollPane.setViewportView(table);
		sl_panel.putConstraint(SpringLayout.NORTH, table, 6, SpringLayout.SOUTH, lblFillAllFields);
		sl_panel.putConstraint(SpringLayout.WEST, table, 39, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, table, 248, SpringLayout.SOUTH, lblFillAllFields);
		sl_panel.putConstraint(SpringLayout.EAST, table, -133, SpringLayout.EAST, panel);
		frame.setBounds(100, 100, 356, 402);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
}
