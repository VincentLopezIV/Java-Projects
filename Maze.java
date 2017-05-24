/**
 * This is the maze class. We have all the necessary constructors, getters,
 * and is methods. 
 *
 * @author Vincent Lopez
 * @version FINAL
 */
public class Maze {
    public char[][] squares; // 2d array [row][col] is used to make the maze
    private boolean[][] explored; // 2d array of booleans is used to mark explored "spaces"
    private boolean animated;
    private int lastIsRow, lastIsCol;   // last location on which an 'is' method
    // was called; used for toString '?'

    public Maze(String text) {
        if (text == null || (text = text.trim()).length() == 0) { //trim() takes the whitespace out of the string
            throw new IllegalArgumentException("empty lines data");
        }

        String[] lines = text.split("\r?\n"); // allows the "lines" to separated into 
        //                                       indexs i.e 1st line=[0],2nd line[1]
        //                                       ? works like an if else
        
        //setting the height and width of the 2d arrays
        squares = new char[lines.length][lines[0].length()]; // [num of rows][num of col]
        explored = new boolean[lines.length][lines[0].length()]; // array of booleans
        
        //System.out.println("lines.length= " +lines.length + " lines[0].length()= " + lines[0].length());
        for (int row = 0; row < getHeight(); row++) { // a loop for the whole maze
            if (lines[row].length() != getWidth()) { // checking the maze against the mother string lines
                                                     // they should be the same throughout
                throw new IllegalArgumentException("line " + (row + 1) + " wrong length (was "
                    + lines[row].length() + " but should be " + getWidth() + ")");
            }
            
            // if we pass the previous test will fill the squares array with the layout of the maze ie lines array
            for (int col = 0; col < getWidth(); col++) {
                squares[row][col] = lines[row].charAt(col); //starts at row 0 and fills the col left to right
            }
        }
        // setting base cases for the maze
        lastIsRow = -1; 
        lastIsCol = -1;
    }

    public int getHeight() {
        return squares.length; // vertical height of the maze
    }

    public int getWidth() {
        return squares[0].length; // width of maze
    }
    
    public char getSquare(int row, int col) { 
        checkIndexes(row, col); // making sure we are within the maze
        return squares[row][col]; // giving current location
    }
    
     public void setExplored(int row, int col, boolean value) {
        checkIndexes(row, col);// making sure we are within the maze
        explored[row][col] = value;// sets the explored array index to true 
    }
    
    public boolean isExplored(int row, int col) {
        checkIndexes(row, col);// making sure we are within the maze
        lastIsRow = row; // resetting lastIsRow & lastIsCol to row and col
        lastIsCol = col;
        return explored[row][col]; // returning true if visted previously and false if it hasn't 
    }

    public void mark(int row, int col) {
        checkIndexes(row, col);// making sure we are within the maze
        sleep(100);// if the maze has been animated wait 1/10 sec
        squares[row][col] = 'x'; // marking the way out
    }
   
    public boolean isMarked(int row, int col) {
        checkIndexes(row, col);// making sure we are within the maze
        lastIsRow = row;// resetting lastIsRow & lastIsCol to row and col
        lastIsCol = col;
        return squares[row][col] == '.'; // returns true if the space has a '.' meaning its been marked explored
    }

    public boolean isWall(int row, int col) {
        checkIndexes(row, col);// making sure we are within the maze
        lastIsRow = row;// resetting lastIsRow & lastIsCol to row and col
        lastIsCol = col;
        return squares[row][col] == '#';// returns true if the space is a wall '#' making it unusable in the escape
    }
    
    public boolean isExit(int row, int col) {
        checkIndexes(row, col);// making sure we are within the maze
        lastIsRow = row;// resetting lastIsRow & lastIsCol to row and col
        lastIsCol = col;
        return squares[row][col] == '@';// returns true if the space is the exit/treasure
    }

    public String toString() {
        StringBuilder result = new StringBuilder(getWidth() * (getHeight()+1));   // using a StringBuilder because they can be appened
                                                                                  // unlike a regular string
                                                                                  // the capacity of the StringBuilder if the area of the maze 
                                                                                  // we add 1 to capacity to make sure we don't append more the the capacity
        for (int row = 0; row < getHeight(); row++) {
            for (int col = 0; col < getWidth(); col++) {
                if (row == lastIsRow && col == lastIsCol) { // keeps track of where the cursor is
                    result.append('$');
                } else if (squares[row][col] == '@') { // prints the exit/treasure
                    result.append('@');
                }else if (squares[row][col] == '#') { // prints the walls
                    result.append('#');
                } else if (squares[row][col] == 'x') {// prints path out
                    result.append('x');
                } else if (explored[row][col]) {// prints the squares visted
                    result.append('.');
                } else {// prints the spaces not visted yet
                    result.append(' ');
                }
            }
            result.append('\n'); // new line to create space between maze iterations
        }
        sleep(100); // will wait before next animation to make it easier to see the process 
        return result.toString();
    }

    public void checkIndexes(int row, int col) {
        if (row < 0 || row >= getHeight() || col < 0 || col >= getWidth()) { // these cases should never occur if they do throw exception
            throw new IllegalArgumentException("illegal indexes: (" + 
                row + ", " + col + ")");
        }
    }

    private void sleep(int ms) { // standard sleep method. Used to slow the maze animation
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ie) {}
    }
}