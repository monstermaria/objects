import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

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
        System.out.println("Creating components");

        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        // The TableData object is used for all showcases
        TableData td = new TableData("sample.csv");

        /*
         * simple solution uses the sort function built into JTable
         */
        JLabel headerSimpleSolution = new JLabel("Simple Solution");
        container.add(headerSimpleSolution);

        JTable table = new JTable(td.getRows(), td.getHeaders());
        table.setAutoCreateRowSorter(true);
        container.add(new JScrollPane(table));

        /*
         * array list solution shows how to add a row and save the data NOTE: the new
         * row might not show up unless you scroll the table
         */
        JLabel headerArrayListSolution = new JLabel("ArrayList Solution");
        container.add(headerArrayListSolution);

        SampleTableModel tm = new SampleTableModel(td);

        JTable tableWithArrayList = new JTable(tm);
        JScrollPane scrollPane = new JScrollPane(tableWithArrayList);
        container.add(scrollPane);

        JButton addRow = new JButton("Add a new row");
        addRow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                td.addRow();
            }
        });
        container.add(addRow);

        JButton saveCsv = new JButton("Save changes");
        saveCsv.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                td.saveCsvFile();
            }
        });
        container.add(saveCsv);

    }
}
