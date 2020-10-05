import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class TableData {
    ArrayList<String> headers = new ArrayList<String>();
    ArrayList<ArrayList<String>> rows = new ArrayList<ArrayList<String>>();

    public TableData(String fileName) {
        File csvFile = new File(fileName);
        try {
            Scanner scanner = new Scanner(csvFile);
            readData(scanner);
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void readData(Scanner scanner) {
        // read headers
        if (scanner.hasNext()) {
            headers = readRow(scanner.nextLine());
        }

        // read rows
        while (scanner.hasNext()) {
            rows.add(readRow(scanner.nextLine()));
        }
    }

    private ArrayList<String> readRow(String row) {
        ArrayList<String> values = new ArrayList<String>();
        Scanner rowScanner = new Scanner(row);

        rowScanner.useDelimiter(",");
        while (rowScanner.hasNext()) {
            String value = rowScanner.next();
            // handle values with comma encased in citation marks
            // if no closing citation mark is found, the rest of the row will be added to
            // one value
            if (value.startsWith("\"")) {
                value = value.substring(1);
                while (rowScanner.hasNext()) {
                    String valuePart2 = rowScanner.next();
                    value += valuePart2;
                    if (value.endsWith("\"")) {
                        value = value.substring(0, value.length() - 2);
                        break;
                    }
                }
            }
            values.add(value);
        }
        rowScanner.close();

        // handle missing data?

        return values;
    }

    String[] getHeaders() {
        int nbrOfColumns = this.headers.size();
        String[] headers = new String[nbrOfColumns];

        // convert headers arraylist to array
        for (int i = 0; i < nbrOfColumns; i++) {
            headers[i] = this.headers.get(i);
        }
        return headers;
    }

    String[][] getRows() {
        int nbrOfColumns = this.headers.size();
        int nbrOfRows = this.rows.size();

        String[][] data = new String[nbrOfRows][nbrOfColumns];

        // convert rows arraylist of arraylists to a two-dimensional string array
        for (int i = 0; i < nbrOfRows; i++) {
            for (int j = 0; j < nbrOfColumns; j++) {
                try {
                    data[i][j] = this.rows.get(i).get(j);
                } catch (Exception e) {
                    System.out.println("Indexing failed at row " + i + " column " + j);
                    System.out.println(e.getMessage());
                    data[i][j] = ""; // add this here or when parsing the csv file?
                }
            }
        }

        return data;
    }

    ArrayList<SampleRowObject> getSampleRowObjects() {
        ArrayList<SampleRowObject> objects = new ArrayList<>();

        for (String[] row : this.getRows()) {
            objects.add(new SampleRowObject(row[0], row[1], row[2], row[3], row[4], row[5], row[6], row[7]));
        }

        return objects;
    }

    void addRow() {
        System.out.println("Add row!");
        ArrayList<String> newRow = new ArrayList<String>();
        for (int i = 0; i < this.headers.size(); i++) {
            newRow.add("");
        }
        this.rows.add(newRow);
    }

    void saveCsvFile() {
        System.out.println("Save CSV!");
        StringBuilder output = new StringBuilder();
        for (String header : this.headers) {
            output.append(header);
            output.append(",");
        }
        output.deleteCharAt(output.length() - 1);
        output.append("\n");
        // output.append(this.headers);
        for (ArrayList<String> row : this.rows) {
            for (String value : row) {
                output.append(value);
                output.append(",");
            }
            output.deleteCharAt(output.length() - 1);
            output.append("\n");
        }
        // output.append(this.rows);
        // System.out.println(output);
        try {
            File saveFile = new File("modified_sample.csv");
            FileWriter writer = new FileWriter(saveFile);
            writer.write(output.toString());
            writer.close();
        } catch (Exception e) {
            System.out.println("Save failed");
        }

    }

    String[][] sortOnColumn(String[][] stringMatrix, int columnIndex) {

        // class MyComparator implements Comparator<String[]> {
        // int column = 0;

        // MyComparator(int column) {
        // this.column = column;
        // }

        // // @Override
        // // int compare(Object a, Object b) {
        // // return 0;
        // // }
        // }
        return stringMatrix;
    }
}
