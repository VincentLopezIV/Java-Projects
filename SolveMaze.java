/**
 * The SolveMaze class opens the given input file and makes 
 * a maze out of the given text. Once the maze is built the
 * user is asked to input a start and exit/treasure point.
 * Once the necessary inputs are given the maze is solved via
 * solve and explore methods. The process of solving the maze
 * is printed out step by step until a path to the exit/treasure is
 * found. 
 *
 * @author Vincent Lopez
 * @version FINAL
 */
import java.io.*;
import java.util.*;
public class SolveMaze {
    public static void main(String[] args) throws FileNotFoundException {
        // "file read" algorithm taken and modified from lab9
        String fileName;                    // Name of maze file
        Scanner keyboard;                   // a scanner connected to keyboard
        Scanner inputFile = null;           // a scanner connected to the file containing the maze
        File mazeFile = null;             // The maze input file
        boolean succeeded = false;          // Succeeded in opening file

        boolean startPointValid = false; // booleans for the start and exit/treasure indexs
        boolean endPointValid = false;
        //-----------------------------------------------------------------
        // Initialize Scanner - To read filename from keyboard
        keyboard = new Scanner(System.in);

        // Open the specified maze file.
        while ( !succeeded )
        {
            System.out.print("\nEnter the name of the message file (use maze1.txt) : ");
            fileName = keyboard.nextLine( );
            try
            {
                mazeFile = new File(fileName);     // create a File associated with filename
                inputFile = new Scanner(mazeFile); // connect a Scanner to the File               
                succeeded = true;           // Assume file is found
            }
            catch (FileNotFoundException e)
            {
                System.out.print("File Not Found: " + fileName);
                succeeded = false;          // File was not found
            }
        }

        // Reading the lines of the file one line at a time into a string
        String fileText = "";
        while (inputFile.hasNextLine()) {
            fileText += inputFile.nextLine() + "\n";
        }
        Maze maze = new Maze(fileText);  // making the maze
        System.out.println(maze); // using the toString

        // Getting the user input for the start point. Using a modified verion of above algorithm.
        int startRow=0; // initialzing the vars outside the loop for scope
        int startCol=0;
        while ( !startPointValid )
        {
            System.out.print("\nEnter a valid starting point x,y (use 1 6): ");
            startRow = keyboard.nextInt();
            startCol = keyboard.nextInt();
            try
            {
                maze.checkIndexes(startRow,startCol);
                if(!maze.isWall(startRow,startCol)) // validating point i.e. not a wall or out of maze
                {
                    startPointValid = true; // the start point is vaild
                }
                else
                {
                    System.out.print("Can't use index as it's a wall");
                    startPointValid = false; // is a vaild index but is a wall so return false
                }
            }
            catch (IllegalArgumentException e) // illegal index
            {
                System.out.print(("illegal indexes: (" + startRow + ", " + startCol + ")"));
                startPointValid = false;          
            }
        }
        System.out.println("Start from (" + startRow + "," + startCol + ")");
        
        // Getting the user input for the exit/treasure. Using a modified verion of above algorithm.
        int endRow=0;
        int endCol=0;
        while ( !endPointValid )
        {
            System.out.print("\nEnter a valid end/treasure point x,y (use "+(maze.getHeight()-3)+" "+(maze.getWidth()-1)+"): ");
            // doing the math inside the print ensures that the user is prompted to set the exit at the right point on the maze
            endRow = keyboard.nextInt();
            endCol = keyboard.nextInt();
            try
            {
                maze.checkIndexes(endRow,endCol);
                if(!maze.isWall(endRow,endCol)) // validating point i.e. not a wall or out of maze
                {
                    endPointValid = true; // the end/treasure point is vaild
                }
                else
                {
                    System.out.print("Can't use index as it's a wall");
                    endPointValid = false; // is a vaild index but is a wall so return false
                }
            }
            catch (IllegalArgumentException e) // illegal index
            {
                System.out.print(("illegal indexes: (" + endRow + ", " + endCol + ")"));
                endPointValid = false;          
            }
        }
        maze.squares[endRow][endCol] = '@'; // is the symbol for the exit/treasure
        if(endRow!=9 && endCol!=9) // if we are not using the provided exit(9,9) we need to fill the exit hole or we will get
                                   // and illegal indexes error while searching for the inputed exit/treasure
        {
            maze.squares[9][9] = '#';
        }
        
        solve(maze, startRow, startCol);// starts the process
    }

    /*
     * Finds an exit route out the given maze from the inputed start location.
     * the maze should not be empty and startRow/Col need to be within the maze
     */
    public static void solve(Maze maze, int startRow, int startCol) {
        if (explore(maze, startRow, startCol)) {
            System.out.println(maze);
            System.out.println();
            System.out.println("EXIT FOUND!"); // end of method
        } else {
            System.out.println("NO SOLUTION"); // if the maze is closed in or empty
        }
    }

    /*
     * Using recurision(recursive backtracking) the method first finds the exit making its path 
     * as it travels through the maze. Once the exit is found (base case is filled) the 'true' 
     * path is recursively outlined with 'x's 
     */
    private static boolean explore(Maze maze, int row, int col) {
        System.out.println(maze);   // show the maze

        if (maze.isWall(row, col) || maze.isExplored(row, col)) { // An unproductive path: 
                                                                                       // outOfBounds, wall or
                                                                                       // previously explored
            return false;
        } else if (maze.isExit(row,col)) { // Base case filled the exit has been found
            return true; // starts the recursive calls back to the start point
        } else {
            maze.setExplored(row, col, true); // setting the current space to true so we know its been visted
            /*
             * The recurision takes place here. We search until we get a failed case in which case we go in a diiferent
             * direction(the mazes used will always have an exit) or we find the exit. 
             * I implemented an South->West->North->East approach a 'box' method is prefered.
             * There are 24 box configs. I assume that each would have around the same performance overall.
             */
            if (explore(maze, row + 1, col) || // south
            explore(maze, row, col - 1)|| // west
            explore(maze, row - 1, col) || // north
            explore(maze, row, col + 1)) // east
            {
                maze.mark(row, col); // marks the path with an X
                System.out.println(maze); // show the maze
                return true; // 'pops' an explore call off as true inturn 'backtracking' the path out. 
            }
        }
        return false;   // returns false if it reaches a dead end 
    }
}
