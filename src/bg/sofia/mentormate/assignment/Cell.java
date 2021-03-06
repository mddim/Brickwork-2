package bg.sofia.mentormate.assignment;

public class Cell {

    private int row;
    private int column;
    private int value;

    public Cell(int row, int column, int value) {
        this.row = row;
        this.column = column;
        this.value = value;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) {
            return true;
        }

        if (!(o instanceof Cell)) {
            return false;
        }

        Cell c = (Cell) o;

        return row == ((Cell) o).row && column == ((Cell) o).column &&
                value == ((Cell) o).value;
    }
}
