package se.gritacademy;

import se.gritacademy.controller.TableData;
import se.gritacademy.model.TablesModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class UserInterface implements Runnable {

    JFrame frame;

    public UserInterface() {
    }

    @Override
    public void run() {
        frame = new JFrame("Objects");
        frame.setPreferredSize(new Dimension(1000, 500));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        createComponents(frame.getContentPane());

        frame.pack();
        frame.setVisible(true);
    }

    private void createComponents(Container container) {
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        // The TableData object is used for all showcases
        TableData td = new TableData("sample.csv");

        /*
         * simple solution uses the sort function built into JTable
         */
        JLabel headerSimpleSolution = new JLabel("Simple Solution");
        container.add(headerSimpleSolution);

        JTable table = new JTable(td.rowArrays, td.headerArray);
        table.setAutoCreateRowSorter(true);
        container.add(new JScrollPane(table));

        /*
         * object solution shows how to add a row and save the data NOTE: the new row
         * might not show up unless you scroll the table
         */
        JLabel headerObjectSolution = new JLabel("Object Solution");
        container.add(headerObjectSolution);

        // create a custom table model
        TablesModel tm = new TablesModel(td);

        // create a JTable with the custom tabel model
        JTable tableWithObjects = new JTable(tm);

        // add listener to sort the table on the clicked column header
        tableWithObjects.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = table.columnAtPoint(e.getPoint());
                td.sortObjects(col);
            }
        });

        // put the table in a scroll pane and add that to the container
        JScrollPane scrollPane = new JScrollPane(tableWithObjects);
        container.add(scrollPane);

        // button for adding a row to the table
        JButton addRow = new JButton("Add a new row");
        addRow.addActionListener(ae -> td.addRow());
        container.add(addRow);

        // button to save the current state of the table
        JButton saveCsv = new JButton("Save changes");
        saveCsv.addActionListener(ae -> td.saveCsvFile());
        container.add(saveCsv);
    }
}
