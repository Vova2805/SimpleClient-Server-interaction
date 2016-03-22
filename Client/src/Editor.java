import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Component;

import javax.swing.SpringLayout;
import javax.swing.JTable;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.Rectangle;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import com.sun.javafx.scene.layout.region.Margins.Converter;

import javax.swing.UIManager;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.Dimension;
import javax.swing.JSpinner;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.awt.event.ActionEvent;
import java.util.Vector;
import javax.swing.JComboBox;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Editor {

    private JFrame frame;
    private JMenuBar menuBar;
    private JTable table;
    private JTextField textField;
    public JComboBox comboBox;
    public Socket socket;
    DataOutputStream toServer;
    DataInputStream in;
    DefaultTableModel tmodel ;
    int it=0;
    int Column=0;
    private SpringLayout springLayout;
    private JButton btnSubmit;
    private JButton btnUpdate;
    private JSpinner spinner_1;
    private JScrollPane scrollPane;
    private JSpinner spinner;
    private JButton btnAddNew;
    private  JButton btnHideQuery;
    private JButton btnShowQuery;


    public Editor() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    initialize();
                    frame.setVisible(true);


                    int serverPort = 1111;
                    String myAddress = "127.0.0.1";
                    InetAddress ipAddress = InetAddress.getByName(myAddress);
                    //Creating new socket
                    Socket socket = new Socket(ipAddress,serverPort);

                    InputStream sin = socket.getInputStream();
                    OutputStream sout = socket.getOutputStream();

                    toServer = new DataOutputStream(sout);
                    in = new DataInputStream(sin);

                    String message="Connect to DB";

                    String []Answer;
                    toServer.writeUTF(message);
                    toServer.flush(); //end of sending
                    //receiving message
                    message = in.readUTF();
                    if(it==0)
                    {
                        Answer = message.split("\\s+");
                        while(Answer.length>it)
                        {
                            comboBox.addItem(Answer[it]);
                            it++;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public boolean SendQuery(String query)
    {
         textField.setText(query);
         try {
             toServer.writeUTF(query);
             toServer.flush();
             String answ = in.readUTF();
             if(!answ.equals(""))
             {
             String[] Answ = answ.split("`div`");
             Column =Integer.parseInt(Answ[0]);
             if(Answ[0].equals("0")&&Answ.length==1)
             {
            	 String item = (String) comboBox.getSelectedItem();
                 String query1 = "select * from [Lab2TestDB].dbo.[" + item + "];";
                 SendQuery(query1);
                 return true;
             }
             if(Answ[0].equals("-1"))
             {
            	 answ = answ.substring(7, answ.length());
            	 JOptionPane.showMessageDialog(frame, answ);
             }
             else
             {
            	 while(tmodel.getRowCount() > 0){
                	   for(int i = 0 ; i < tmodel.getRowCount();i++){
                	      tmodel.removeRow(i);
                	   }
                	}
             tmodel.setColumnCount(Column);
             int rows =0,cols=0;
             tmodel.setNumRows((Answ.length-1-Column)/Column);
             for(int i=1;i<=Column;i++)
             {
             	table.getColumnModel().getColumn(i-1).setHeaderValue(Answ[i]);
             }
             for(int i=Column+1;i<Answ.length;i++)
             {
                 tmodel.setValueAt(Answ[i],rows,cols++);
                 if(cols==Column)
                 {
                     rows++;
                     cols=0;
                 }
             } 
             }
             }
             else
             {
            	 JOptionPane.showMessageDialog(frame, "Answer is empty");
             }
         } catch (IOException e1) {
             e1.printStackTrace();
         }
		return true;
    }
    private void initialize() {
        frame = new JFrame();
        frame.getContentPane().setBounds(new Rectangle(10, 10, 10, 10));
        frame.getContentPane().setBackground(new Color(0, 153, 255));
        frame.setBounds(100, 100, 710, 411);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        springLayout = new SpringLayout();
        frame.getContentPane().setLayout(springLayout);
        
        
        scrollPane = new JScrollPane();
        springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 10, SpringLayout.NORTH, frame.getContentPane());
        springLayout.putConstraint(SpringLayout.WEST, scrollPane, 216, SpringLayout.WEST, frame.getContentPane());
        springLayout.putConstraint(SpringLayout.EAST, scrollPane, -10, SpringLayout.EAST, frame.getContentPane());
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(UIManager.getBorder("InternalFrame.border"));
        frame.getContentPane().add(scrollPane);

        tmodel = new DefaultTableModel();
        table = new JTable(tmodel);
        table.setBorder(new LineBorder(new Color(0, 0, 0)));
        table.setSelectionBackground(new Color(0, 102, 102));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent arg0) {
        		if(arg0.getClickCount()==1)
        		{
        			spinner.setValue(table.getSelectedRow()+1);
        			spinner_1.setValue(table.getSelectedRow()+1);
        		}
        		if(arg0.getClickCount()==2)
        		{
        			AddNew Update = new AddNew();
        		}
        	}
        });
        table.addPropertyChangeListener(new PropertyChangeListener() {
        	public void propertyChange(PropertyChangeEvent e) {}
        });
        table.setAlignmentX(Component.RIGHT_ALIGNMENT);
        table.setFillsViewportHeight(true);
        scrollPane.setViewportView(table);
        springLayout.putConstraint(SpringLayout.NORTH, table, 10, SpringLayout.NORTH, frame.getContentPane());

        btnSubmit = new JButton("Send");
        springLayout.putConstraint(SpringLayout.EAST, btnSubmit, 210, SpringLayout.WEST, frame.getContentPane());
        btnSubmit.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		String query = textField.getText();
        		SendQuery(query);
        	}
        });
        springLayout.putConstraint(SpringLayout.WEST, btnSubmit, 114, SpringLayout.WEST, frame.getContentPane());
        btnSubmit.setPreferredSize(new Dimension(60, 23));
        frame.getContentPane().add(btnSubmit);
        btnAddNew = new JButton("Add new");
        springLayout.putConstraint(SpringLayout.NORTH, btnAddNew, 51, SpringLayout.NORTH, frame.getContentPane());
        btnAddNew.setPreferredSize(new Dimension(75, 25));
        springLayout.putConstraint(SpringLayout.WEST, btnAddNew, 10, SpringLayout.WEST, frame.getContentPane());
        springLayout.putConstraint(SpringLayout.EAST, btnAddNew, -115, SpringLayout.WEST, scrollPane);
        btnAddNew.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                AddNew New = new AddNew();
                New.model.setColumnCount(2);
                New.model.setNumRows(Column);
                New.Back = textField;
                New.operation = true; // Add
                New.selectedTable = comboBox.getSelectedItem().toString();
                for(int i=0;i<Column;i++)
                {
                	New.model.setValueAt(table.getColumnModel().getColumn(i).getHeaderValue(),i,0);
                	New.model.setValueAt("NULL",i,1);
                }
            }
        });
        frame.getContentPane().add(btnAddNew);

        JButton btnRefrech = new JButton("Refresh");
        btnRefrech.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		String item = (String) comboBox.getSelectedItem();
                String query = "select * from [Lab2TestDB].dbo.[" + item + "];";
                SendQuery(query);
        	}
        });
        springLayout.putConstraint(SpringLayout.NORTH, btnRefrech, 0, SpringLayout.NORTH, btnAddNew);
        springLayout.putConstraint(SpringLayout.WEST, btnRefrech, 6, SpringLayout.EAST, btnAddNew);
        springLayout.putConstraint(SpringLayout.EAST, btnRefrech, -6, SpringLayout.WEST, scrollPane);
        btnRefrech.setPreferredSize(new Dimension(75, 25));
        frame.getContentPane().add(btnRefrech);
        
        JButton btnRemove = new JButton("Remove");
        springLayout.putConstraint(SpringLayout.SOUTH, btnAddNew, -3, SpringLayout.NORTH, btnRemove);
        springLayout.putConstraint(SpringLayout.NORTH, btnRemove, 77, SpringLayout.NORTH, frame.getContentPane());
        btnRemove.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		String query = "delete from [Lab2TestDB].dbo.[" + comboBox.getSelectedItem()+ "] where "+table.getColumnModel().getColumn(0).getHeaderValue() +"="+"'"+tmodel.getValueAt((int) spinner_1.getValue()-1, 0) +"'"+";";
        		SendQuery(query);
        	}
        });
        btnRemove.setPreferredSize(new Dimension(75, 25));
        springLayout.putConstraint(SpringLayout.EAST, btnRemove, 101, SpringLayout.WEST, frame.getContentPane());
        springLayout.putConstraint(SpringLayout.WEST, btnRemove, 10, SpringLayout.WEST, frame.getContentPane());
        frame.getContentPane().add(btnRemove);

        btnUpdate = new JButton("Update");
        springLayout.putConstraint(SpringLayout.NORTH, btnUpdate, 7, SpringLayout.SOUTH, btnRemove);
        btnUpdate.setPreferredSize(new Dimension(75, 25));
        springLayout.putConstraint(SpringLayout.WEST, btnUpdate, 10, SpringLayout.WEST, frame.getContentPane());
        springLayout.putConstraint(SpringLayout.EAST, btnUpdate, 101, SpringLayout.WEST, frame.getContentPane());
        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	AddNew New = new AddNew();
                New.model.setColumnCount(2);
                New.model.setNumRows(Column);
                New.Back = textField;
                New.operation = false; //Update
                New.selectedTable = comboBox.getSelectedItem().toString();
                for(int i=0;i<Column;i++)
                {
                	New.model.setValueAt(table.getColumnModel().getColumn(i).getHeaderValue(),i,0);
                	New.model.setValueAt(table.getValueAt((int)spinner.getValue()-1, i),i,1);
                }
            }
        });
        frame.getContentPane().add(btnUpdate);

        btnShowQuery = new JButton("Show query");
        springLayout.putConstraint(SpringLayout.WEST, btnShowQuery, 10, SpringLayout.WEST, frame.getContentPane());
        springLayout.putConstraint(SpringLayout.EAST, btnShowQuery, -6, SpringLayout.WEST, btnSubmit);
        btnShowQuery.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		textField.setVisible(true);
        		btnHideQuery.setVisible(true);
        	    btnShowQuery.setEnabled(false);
        	}
        });
        btnShowQuery.setEnabled(false);
        frame.getContentPane().add(btnShowQuery);

        spinner_1 = new JSpinner();
        spinner_1.setValue(1);
        springLayout.putConstraint(SpringLayout.NORTH, spinner_1, 2, SpringLayout.NORTH, btnRemove);
        springLayout.putConstraint(SpringLayout.WEST, spinner_1, 6, SpringLayout.EAST, btnRemove);
        springLayout.putConstraint(SpringLayout.EAST, spinner_1, -6, SpringLayout.WEST, scrollPane);
        frame.getContentPane().add(spinner_1);
        
        spinner = new JSpinner();
        springLayout.putConstraint(SpringLayout.NORTH, spinner, 2, SpringLayout.NORTH, btnUpdate);
        spinner.setValue(1);
        springLayout.putConstraint(SpringLayout.WEST, spinner, 6, SpringLayout.EAST, btnUpdate);
        springLayout.putConstraint(SpringLayout.EAST, spinner, -6, SpringLayout.WEST, scrollPane);
        frame.getContentPane().add(spinner);

        textField = new JTextField();
        springLayout.putConstraint(SpringLayout.NORTH, btnSubmit, -30, SpringLayout.NORTH, textField);
        springLayout.putConstraint(SpringLayout.SOUTH, btnSubmit, -6, SpringLayout.NORTH, textField);
        springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, -6, SpringLayout.NORTH, textField);
        springLayout.putConstraint(SpringLayout.NORTH, textField, -42, SpringLayout.SOUTH, frame.getContentPane());
        springLayout.putConstraint(SpringLayout.EAST, textField, -10, SpringLayout.EAST, frame.getContentPane());
        textField.setEditable(false);
        springLayout.putConstraint(SpringLayout.SOUTH, textField, -17, SpringLayout.SOUTH, frame.getContentPane());
        frame.getContentPane().add(textField);
        textField.setColumns(10);

        btnHideQuery = new JButton("Hide query");
        springLayout.putConstraint(SpringLayout.WEST, textField, 6, SpringLayout.EAST, btnHideQuery);
        springLayout.putConstraint(SpringLayout.SOUTH, btnShowQuery, -8, SpringLayout.NORTH, btnHideQuery);
        springLayout.putConstraint(SpringLayout.EAST, btnHideQuery, 108, SpringLayout.WEST, frame.getContentPane());
        springLayout.putConstraint(SpringLayout.NORTH, btnHideQuery, -41, SpringLayout.SOUTH, frame.getContentPane());
        springLayout.putConstraint(SpringLayout.SOUTH, btnHideQuery, -17, SpringLayout.SOUTH, frame.getContentPane());
        btnHideQuery.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		textField.setVisible(false);
        		btnHideQuery.setVisible(false);
        	    btnShowQuery.setEnabled(true);
        	}
        });
        springLayout.putConstraint(SpringLayout.WEST, btnHideQuery, 10, SpringLayout.WEST, frame.getContentPane());
        frame.getContentPane().add(btnHideQuery);

        comboBox = new JComboBox();
        springLayout.putConstraint(SpringLayout.EAST, comboBox, -6, SpringLayout.WEST, scrollPane);
        comboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String item = (String) comboBox.getSelectedItem();
                String query = "select * from [Lab2TestDB].dbo.[" + item + "];";
                SendQuery(query);
            }
        });
        
        springLayout.putConstraint(SpringLayout.NORTH, comboBox, 14, SpringLayout.NORTH, frame.getContentPane());
        springLayout.putConstraint(SpringLayout.WEST, comboBox, 10, SpringLayout.WEST, frame.getContentPane());
        frame.getContentPane().add(comboBox);

        menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        JMenu mnFile = new JMenu("File");
        menuBar.add(mnFile);

        JMenuItem mntmMakeQuery = new JMenuItem("Make query");
        mntmMakeQuery.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		textField.requestDefaultFocus();
        	}
        });
        mnFile.add(mntmMakeQuery);
        
        JMenuItem mntmDisconnect = new JMenuItem("Disconnect");
        mntmDisconnect.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		SendQuery("Disconnect");
        	}
        });
        mnFile.add(mntmDisconnect);

        JMenuItem mntmExit = new JMenuItem("Exit");
        mntmExit.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		frame.setVisible(false); 
        		frame.dispose(); 
        	}
        });
        mnFile.add(mntmExit);

        JMenu mnAbout = new JMenu("About");
        menuBar.add(mnAbout);
    }
}
