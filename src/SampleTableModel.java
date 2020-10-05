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
        return td.rows.size();
    }

    public Object getValueAt(int row, int column) {
        try {
            return this.td.rows.get(row).get(column);
        } catch (Exception e) {
            return "";
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
        try {
            td.rows.get(rowIndex).remove(columnIndex);
            td.rows.get(rowIndex).add(columnIndex, (String) aValue);
        } catch (Exception e) {
            System.out.println(e.getCause());
        }
    }
}
