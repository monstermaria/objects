package se.gritacademy.model;

import se.gritacademy.controller.TableData;

import javax.swing.table.AbstractTableModel;

public class TablesModel extends AbstractTableModel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    TableData td;

    public TablesModel(TableData td) {
        this.td = td;
    }

    public int getColumnCount() {
        return td.headers.size();
    }

    public int getRowCount() {
        return td.rowObjects.size();
    }

    public Object getValueAt(int row, int column) {
        RowModel rowObject = this.td.rowObjects.get(row);
        String fieldName = this.td.getObjectFieldName(column);
        try {
            // using java reflection to get the value of a field of the object
            return RowModel.class.getDeclaredField(fieldName).get(rowObject);
           // return columnField.get(rowObject);
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed";
        }
    }

    @Override
    public String getColumnName(int columnIndex) {
        return td.headers.get(columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        RowModel rowObject = this.td.rowObjects.get(rowIndex);
        String fieldName = this.td.getObjectFieldName(columnIndex);
        try {
            // using java reflection to set the value of a field of the object
            RowModel.class.getDeclaredField(fieldName).set(rowObject, aValue);
           // columnField.set(rowObject, aValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
