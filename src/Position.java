/**
 * The Position class represents one position on the board. It has two fields row and column, which represent the index
 * of row and column on the board separately. It provides two constructors. One is with no input parameter. Another is
 * with specified row and column. It provides two getters to get the row and column of the Position object.
 */
public class Position {
    private int row;
    private int column;

    /**
     * Constructor of the Position class with no parameter.
     */
    public Position(){

    }

    /**
     * Constructor of the Position class with row and column as the inputs.
     *
     * @param row The input row.
     * @param column The input column.
     */
    public Position(int row, int column){
        this.row = row;
        this.column = column;
    }

    /**
     * Get the row.
     *
     * @return The row of the Position object.
     */
    public int getRow(){
        return row;
    }

    /**
     * Get the column.
     *
     * @return The column of the Position object.
     */
    public int getColumn() {
        return column;
    }
}
