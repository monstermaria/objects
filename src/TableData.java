import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class TableData {
    ArrayList<String> headers = new ArrayList<String>();
    ArrayList<ArrayList<String>> rows = new ArrayList<ArrayList<String>>();

    ArrayList<SampleRowObject> rowObjects = new ArrayList<SampleRowObject>();
    String[][] rowArrays;
    String[] headerArray;

    public TableData(String fileName) {
        File csvFile = new File(fileName);

        try {
            Scanner scanner = new Scanner(csvFile);
            readData(scanner);
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

        this.rowArrays = this.getRows();
        this.rowObjects = this.getSampleRowObjects();
        this.headerArray = this.getHeaders();
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

    private String[] getHeaders() {
        int nbrOfColumns = this.headers.size();
        String[] headers = new String[nbrOfColumns];

        // convert headers arraylist to array
        for (int i = 0; i < nbrOfColumns; i++) {
            headers[i] = this.headers.get(i);
        }
        return headers;
    }

    private String[][] getRows() {
        int nbrOfColumns = this.headers.size();
        int nbrOfRows = this.rows.size();

        String[][] data = new String[nbrOfRows][nbrOfColumns];

        // convert rows arraylist of arraylists to a two-dimensional string array
        for (int i = 0; i < nbrOfRows; i++) {
            for (int j = 0; j < nbrOfColumns; j++) {
                try {
                    data[i][j] = this.rows.get(i).get(j);
                } catch (Exception e) {
                    System.out.print("Indexing failed at row " + i + " column " + j + ": ");
                    System.out.println(e.getMessage());
                    data[i][j] = ""; // add this here or when parsing the csv file?
                }
            }
        }

        return data;
    }

    private ArrayList<SampleRowObject> getSampleRowObjects() {
        ArrayList<SampleRowObject> objects = new ArrayList<>();

        for (String[] row : this.rowArrays) {
            objects.add(new SampleRowObject(row[0], row[1], row[2], row[3], row[4], row[5], row[6], row[7]));
        }

        return objects;
    }

    void addRow() {
        // add a new object to the array list that the custom table model uses
        SampleRowObject newRowObject = new SampleRowObject("", "", "", "", "", "", "", "");
        this.rowObjects.add(newRowObject);
    }

    void saveCsvFile() {
        // gather the headers
        StringBuilder output = new StringBuilder();
        for (String header : this.headers) {
            output.append(header);
            output.append(",");
        }
        // remove the last comma on the header row, and add a line break
        output.deleteCharAt(output.length() - 1);
        output.append("\n");

        // add a row for every object (row) in the table model
        for (SampleRowObject row : this.rowObjects) {
            output.append(row.orderDate + ",");
            output.append(row.region + ",");
            output.append(row.rep1 + ",");
            output.append(row.rep2 + ",");
            output.append(row.item + ",");
            output.append(row.units + ",");
            output.append(row.unitCost + ",");
            output.append(row.total + "\n");
        }
        // remove the last line break
        output.deleteCharAt(output.length() - 1);

        try {
            File saveFile = new File("modified_sample.csv");
            FileWriter writer = new FileWriter(saveFile);
            writer.write(output.toString());
            writer.close();
        } catch (Exception e) {
            System.out.println("Save failed");
        }
    }

    String getObjectFieldName(int column) {
        // get the name of the column
        String columnName = this.headers.get(column);
        // replace the uppercase letter at the beginning with the corresponding
        // lowercase letter, to get the corresponding field name in the row object
        return columnName.substring(0, 1).toLowerCase() + columnName.substring(1);
    }

    void sortObjects(int column) {
        String fieldName = this.getObjectFieldName(column);

        // i'm using the sort method of the class Collections to implement sorting of
        // the table model objects. i use a custom Comparator to be able to sort the
        // objects on the content of a specific field. to make this work for any
        // field/column, i use java reflection
        Collections.sort(this.rowObjects, new Comparator<SampleRowObject>() {
            public int compare(SampleRowObject o1, SampleRowObject o2) {
                try {
                    // get a field object that corresponds to the column we want to sort on
                    Field columnField = SampleRowObject.class.getDeclaredField(fieldName);
                    // use the field object to get the values of that field from the two objects to
                    // compare
                    String valueToCompareO1 = (String) columnField.get(o1);
                    String valueToCompareO2 = (String) columnField.get(o2);
                    // use the built in method for comparison on string objects to make a comparison
                    return valueToCompareO1.compareToIgnoreCase(valueToCompareO2);
                } catch (Exception e) {
                    System.out.println(e);
                    // if a comparison can't be made, return 0
                    // (this means that the two objects are considered equal)
                    // NOTE: maybe it is better to let the program crash if this happens
                    return 0;
                }
            }
        });
    }
}
