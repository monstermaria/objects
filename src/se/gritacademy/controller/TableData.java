package se.gritacademy.controller;

import se.gritacademy.model.RowModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class TableData {
    public ArrayList<String> headers = new ArrayList<>();
    public ArrayList<ArrayList<String>> rows = new ArrayList<>();

    public ArrayList<RowModel> rowObjects;
    public String[][] rowArrays;
    public String[] headerArray;

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
            this.headers = readRow(scanner.nextLine());
        }

        // read rows
        while (scanner.hasNext()) {
            this.rows.add(readRow(scanner.nextLine()));
        }
    }

    private ArrayList<String> readRow(String row) {
        ArrayList<String> values = new ArrayList<>();
        Scanner rowScanner = new Scanner(row);

        rowScanner.useDelimiter(",");
        while (rowScanner.hasNext()) {
            StringBuilder value = new StringBuilder(rowScanner.next());
            // handle values with comma encased in citation marks
            // if no closing citation mark is found, the rest of the row will be added to
            // one value

            if (value.toString().startsWith("\"")) {
                value = new StringBuilder(value.substring(1));
                while (rowScanner.hasNext()) {
                    String valuePart2 = rowScanner.next();
                    value.append(valuePart2);
                    if (value.toString().endsWith("\"")) {
                        value = new StringBuilder(value.substring(0, value.length() - 2));
                        break;
                    }
                }
            }

            values.add(value.toString());

        }
        // Bad attempt at handling missing fields. It works, but pretty sure it's bad code
        for (int i = 0; i < this.headers.size(); i++) {
            if (values.size() != this.headers.size()) {
                values.add(headers.size() -1, "[No Value]"); // element is up for debate.Just a Placeholder
            }
            if (values.get(i).isEmpty()) {
                values.set(i, "[No Value]"); // element is up for debate.Just a Placeholder
            }
        }
        System.out.println(values);
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
                   // data[i][j] = ""; // add this here or when parsing the csv file?
                }
            }
        }

        return data;
    }

    private ArrayList<RowModel> getSampleRowObjects() {
        ArrayList<RowModel> objects = new ArrayList<>();
        Arrays.stream(this.rowArrays)
                .iterator()
                .forEachRemaining(row -> objects.add(new RowModel(row[0], row[1], row[2], row[3], row[4], row[5], row[6], row[7])));
        /*for (String[] row : this.rowArrays) {
            objects.add(new SampleRowObject(row[0], row[1], row[2], row[3], row[4], row[5], row[6], row[7]));
        }*/

        return objects;
    }

    public void addRow() {
        // add a new object to the array list that the custom table model uses
        // SampleRowObject newRowObject = new SampleRowObject("", "", "", "", "", "", "", "");
        this.rowObjects.add(new RowModel("", "", "", "", "", "", "", ""));
    }

    public void saveCsvFile() {
        // gather the headers
        StringBuilder output = new StringBuilder();
        this.headers.forEach(header -> output.append(header).append(","));
        // remove the last comma on the header row, and add a line break
        output.deleteCharAt(output.length() - 1).append("\n");
       // output.append("\n");

        // add a row for every object (row) in the table model
        for (RowModel row : this.rowObjects) {
            output.append(row.orderDate).append(",");
            output.append(row.region).append(",");
            output.append(row.rep1).append(",");
            output.append(row.rep2).append(",");
            output.append(row.item).append(",");
            output.append(row.units).append(",");
            output.append(row.unitCost).append(",");
            output.append(row.total).append("\n");
        }
        // remove the last line break
        output.deleteCharAt(output.length() - 1);

        try {
            File saveFile = new File("modified_sample.csv");
            FileWriter writer = new FileWriter(saveFile);
            writer.write(output.toString());
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Save failed");
        }
    }

    public String getObjectFieldName(int column) {
        // get the name of the column
        String columnName = this.headers.get(column);
        // replace the uppercase letter at the beginning with the corresponding
        // lowercase letter, to get the corresponding field name in the row object
        return columnName.substring(0, 1).toLowerCase() + columnName.substring(1);
    }

    public void sortObjects(int column) {
        String fieldName = this.getObjectFieldName(column);

        // i'm using the sort method of the class Collections to implement sorting of
        // the table model objects. i use a custom Comparator to be able to sort the
        // objects on the content of a specific field. to make this work for any
        // field/column, i use java reflection
        this.rowObjects.sort((o1, o2) -> {
            try {
                // get a field object that corresponds to the column we want to sort on
                Field columnField = RowModel.class.getDeclaredField(fieldName);
                // use the field object to get the values of that field from the two objects to
                // compare
                String valueToCompareO1 = (String) columnField.get(o1);
                String valueToCompareO2 = (String) columnField.get(o2);
                // use the built in method for comparison on string objects to make a comparison
                return valueToCompareO1.compareToIgnoreCase(valueToCompareO2);
            } catch (Exception e) {
                e.printStackTrace();
                // if a comparison can't be made, return 0
                // (this means that the two objects are considered equal)
                // NOTE: maybe it is better to let the program crash if this happens
                return 0;
            }
        });
    }
}
