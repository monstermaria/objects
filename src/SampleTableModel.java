import java.lang.reflect.Field;

import javax.swing.table.AbstractTableModel;

class SampleTableModel extends AbstractTableModel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    TableData td;

    SampleTableModel(TableData td) {
        this.td = td;
    }

    public int getColumnCount() {
        return td.headers.size();
    }

    public int getRowCount() {
        return td.rowObjects.size();
    }

    public Object getValueAt(int row, int column) {
        SampleRowObject rowObject = this.td.rowObjects.get(row);
        String fieldName = this.td.getObjectFieldName(column);
        try {
            // using java reflection to get the value of a field of the object
            Field columnField = SampleRowObject.class.getDeclaredField(fieldName);
            return columnField.get(rowObject);
        } catch (Exception e) {
            System.out.println(e);
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
        SampleRowObject rowObject = this.td.rowObjects.get(rowIndex);
        String fieldName = this.td.getObjectFieldName(columnIndex);
        try {
            // using java reflection to set the value of a field of the object
            Field columnField = SampleRowObject.class.getDeclaredField(fieldName);
            columnField.set(rowObject, aValue);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
