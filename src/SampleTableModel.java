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
        String columnName = this.getColumnName(column);
        columnName = columnName.substring(0, 1).toLowerCase() + columnName.substring(1);
        try {
            Field columnField = SampleRowObject.class.getDeclaredField(columnName);
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
        String columnName = this.getColumnName(columnIndex);
        columnName = columnName.substring(0, 1).toLowerCase() + columnName.substring(1);
        try {
            Field columnField = SampleRowObject.class.getDeclaredField(columnName);
            columnField.set(rowObject, aValue);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
