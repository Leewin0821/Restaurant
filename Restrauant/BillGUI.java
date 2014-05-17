package Restrauant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * design of GUI and response of mouse click
 */
public class BillGUI extends JFrame implements ActionListener {
    private OrderCollection orderCollection;
    private JTextField searchField;
    private JButton search;
    private JTextArea displayList;

    public BillGUI(OrderCollection orderCollection)
    {
        this.orderCollection = orderCollection;
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("orderList");
        setupCenterPanel();
        setupSouthPanel();
        pack();
        setVisible(true);
    }

    private void setupCenterPanel(){
        JPanel centerPanel = new JPanel();
        displayList = new JTextArea(20,50);
        displayList.setFont(new Font(Font.MONOSPACED,Font.PLAIN,14));
        displayList.setEditable(false);
        displayList.setVisible(true);
        JScrollPane scrollPane = new JScrollPane(displayList);
        centerPanel.add(scrollPane);
        this.add(centerPanel,BorderLayout.CENTER);
    }

    private void setupSouthPanel(){
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new GridLayout(1,3));
        searchPanel.add(new JLabel("Enter Table Number."));
        searchField = new JTextField(5);
        searchPanel.add(searchField);
        search = new JButton("Search");
        searchPanel.add(search);
        search.addActionListener(this);
        this.add(searchPanel,BorderLayout.SOUTH);
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == search){
            search();
        }
    }

    public void search(){
        String message = "";
        try{
            String id = searchField.getText().trim();
            int tableId = Integer.parseInt(id);
            message = orderCollection.findById(tableId);
        } catch (NumberFormatException nfe){
            System.out.println("Incorrect input at search method.\n"+nfe);
            JOptionPane.showMessageDialog(null,"Incorrect Id Format!");
        }
        if (message != null){
            displayList.setText(message);
        } else {
            JOptionPane.showMessageDialog(null,"No such Table");
        }

    }

}
