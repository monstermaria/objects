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

        TableData td = new TableData("sample.csv");

        // sort on arbitrary column
        // td.sortOnColumn(td.getRows(), 4);

        JLabel headerSimpleSolution = new JLabel("Simple Solution");
        container.add(headerSimpleSolution);

        JTable table = new JTable(td.getRows(), td.getHeaders());
        table.setAutoCreateRowSorter(true);
        container.add(new JScrollPane(table));

        JLabel headerObjectSolution = new JLabel("Object Solution");
        container.add(headerObjectSolution);

        SampleTableModel tm = new SampleTableModel(td);

        JTable tableWithObjects = new JTable(tm);
        JScrollPane scrollPane = new JScrollPane(tableWithObjects);
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
                // System.out.println("Save!");
                td.saveCsvFile();
            }
        });
        container.add(saveCsv);

    }
}
