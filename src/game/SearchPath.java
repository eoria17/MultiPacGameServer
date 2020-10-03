// references:
// https://www.geeksforgeeks.org/a-search-algorithm/
// https://en.wikipedia.org/wiki/A*_search_algorithm

package game;

import serverConnection.ConnectionHandler;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchPath {
    public static HashMap<Double, Position> pPair;
    public static final int ROW = 11;
    public static final int COL = 11;
    public static ArrayList<Position> otherMonsterPath;

    public static boolean isValid(int row, int col) {
        // Returns true if row number and column number
        // is in range
        return (row >= 0) && (row < ROW) &&
                (col >= 0) && (col < COL);
    }

    public static boolean isUnBlocked(int row, int col) {
        if (ConnectionHandler.gridObstacles == null) {
            return true;
        }
        for (int k = 0; k < ConnectionHandler.gridObstacles.length; k++) {
            Position pos = ConnectionHandler.gridObstacles[k];
            if (row == pos.row && col == pos.col) {
                return false;
            }
        }
        if (otherMonsterPath == null) {
            return true;
        }
        for (int i = 0; i < otherMonsterPath.size(); i++) {
            Position pos = otherMonsterPath.get(i);
            if (row == pos.row && col == pos.col) {
                return false;
            }
        }
        return true;
    }

    public static boolean isDestination(int row, int col, Position dest) {
        if (row == dest.row && col == dest.col)
            return true;
        else
            return false;
    }

    public static double calculateHValue(int row, int col, Position dest) {
        return Math.abs(row - dest.row) + Math.abs(col - dest.col);
    }

    public static ArrayList<Position> tracePath(CellDetail cellDetails[][], Position dest) {
        int row = dest.row;
        int col = dest.col;

        ArrayList<Position> path = new ArrayList<>();

        while (!(cellDetails[row][col].parentI == row
                && cellDetails[row][col].parentJ == col)) {
            path.add(new Position(row, col));
            int temp_row = cellDetails[row][col].parentI;
            int temp_col = cellDetails[row][col].parentJ;
            row = temp_row;
            col = temp_col;
        }

        path.add(new Position(row, col));

        return path;
    }

    public static ArrayList<Position> aStarSearch(Position src, Position dest) throws SamePositionException, OutofBoundaryException {
        if (!isValid(src.row, src.col) || !isValid(dest.row, dest.col)) {
            throw new OutofBoundaryException("The search position is out of boundary");
        }

        if (isDestination(src.row, src.col, dest)) {
            throw new SamePositionException("The source and destination point!");
        }

        // Create a closed list and initialise it to false which means
        // that no cell has been included yet
        // This closed list is implemented as a boolean 2D array
        boolean closedList[][] = new boolean[ROW][COL];

        // Declare a 2D array of structure to hold the details
        //of that cell
        CellDetail cellDetails[][] = new CellDetail[ROW][COL];

        int i, j;

        for (i = 0; i < ROW; i++) {
            for (j = 0; j < COL; j++) {
                cellDetails[i][j] = new CellDetail(-1, -1, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
            }
        }

        // Initialising the parameters of the starting node
        i = src.row;
        j = src.col;
        cellDetails[i][j].f = 0.0;
        cellDetails[i][j].g = 0.0;
        cellDetails[i][j].h = 0.0;
        cellDetails[i][j].parentI = i;
        cellDetails[i][j].parentJ = j;

    /*
     Create an open list having information as-
     <f, <i, j>>
     where f = g + h,
     and i, j are the row and column index of that cell
     Note that 0 <= i <= ROW-1 & 0 <= j <= COL-1
     This open list is implenented as a set of pair of pair.*/
        ArrayList<PositionWithP> openList = new ArrayList<>();

        // Put the starting cell on the open list and set its
        // 'f' as 0
        openList.add(new PositionWithP(0.0, new Position(i, j)));

        while (openList.size() != 0) {
            PositionWithP p = openList.get(0);

            // Remove this vertex from the open list
            openList.remove(0);

            // Add this vertex to the closed list
            i = p.position.row;
            j = p.position.col;
            closedList[i][j] = true;

           /*
            Generating all the 8 successor of this cell

                N.W   N   N.E
                  \   |   /
                   \  |  /
                W----Cell----E
                     / | \
                   /   |  \
                S.W    S   S.E

            Cell-->Popped Cell (i, j)
            N -->  North       (i-1, j)
            S -->  South       (i+1, j)
            E -->  East        (i, j+1)
            W -->  West           (i, j-1)
            N.E--> North-East  (i-1, j+1)
            N.W--> North-West  (i-1, j-1)
            S.E--> South-East  (i+1, j+1)
            S.W--> South-West  (i+1, j-1)*/

            // To store the 'g', 'h' and 'f' of the 8 successors
            double gNew, hNew, fNew;

            //----------- 1st Successor (North) ------------
            if (isValid(i - 1, j) == true) {
                // If the destination cell is the same as the
                // current successor
                if (isDestination(i - 1, j, dest) == true) {
                    // Set the Parent of the destination cell
                    cellDetails[i - 1][j].parentI = i;
                    cellDetails[i - 1][j].parentJ = j;
                    return tracePath(cellDetails, dest);
                }
                // If the successor is already on the closed
                // list or if it is blocked, then ignore it.
                // Else do the following
                else if (closedList[i - 1][j] == false &&
                        isUnBlocked(i - 1, j) == true) {
                    gNew = cellDetails[i][j].g + 1.0;
                    hNew = calculateHValue(i - 1, j, dest);
                    fNew = gNew + hNew;

                    // If it isn’t on the open list, add it to
                    // the open list. Make the current square
                    // the parent of this square. Record the
                    // f, g, and h costs of the square cell
                    //                OR
                    // If it is on the open list already, check
                    // to see if this path to that square is better,
                    // using 'f' cost as the measure.
                    if (cellDetails[i - 1][j].f == Float.MAX_VALUE ||
                            cellDetails[i - 1][j].f > fNew) {
                        openList.add(new PositionWithP(fNew,
                                new Position(i - 1, j)));

                        // Update the details of this cell
                        cellDetails[i - 1][j].f = fNew;
                        cellDetails[i - 1][j].g = gNew;
                        cellDetails[i - 1][j].h = hNew;
                        cellDetails[i - 1][j].parentI = i;
                        cellDetails[i - 1][j].parentJ = j;
                    }
                }
            }

            //----------- 2nd Successor (South) ------------
            if (isValid(i + 1, j) == true) {
                // Only process this cell if this is a valid one
                // If the destination cell is the same as the
                // current successor
                if (isDestination(i + 1, j, dest) == true) {
                    // Set the Parent of the destination cell
                    cellDetails[i + 1][j].parentI = i;
                    cellDetails[i + 1][j].parentJ = j;
                    return tracePath(cellDetails, dest);
                }
                // If the successor is already on the closed
                // list or if it is blocked, then ignore it.
                // Else do the following
                else if (closedList[i + 1][j] == false &&
                        isUnBlocked(i + 1, j) == true) {
                    gNew = cellDetails[i][j].g + 1.0;
                    hNew = calculateHValue(i + 1, j, dest);
                    fNew = gNew + hNew;

                    // If it isn’t on the open list, add it to
                    // the open list. Make the current square
                    // the parent of this square. Record the
                    // f, g, and h costs of the square cell
                    //                OR
                    // If it is on the open list already, check
                    // to see if this path to that square is better,
                    // using 'f' cost as the measure.
                    if (cellDetails[i + 1][j].f == Float.MAX_VALUE ||
                            cellDetails[i + 1][j].f > fNew) {
                        openList.add(new PositionWithP(fNew, new Position(i + 1, j)));
                        // Update the details of this cell
                        cellDetails[i + 1][j].f = fNew;
                        cellDetails[i + 1][j].g = gNew;
                        cellDetails[i + 1][j].h = hNew;
                        cellDetails[i + 1][j].parentI = i;
                        cellDetails[i + 1][j].parentJ = j;
                    }
                }
            }
            //----------- 3rd Successor (East) ------------
            if (isValid(i, j + 1) == true) {
                // Only process this cell if this is a valid one
                // If the destination cell is the same as the
                // current successor
                if (isDestination(i, j + 1, dest) == true) {
                    // Set the Parent of the destination cell
                    cellDetails[i][j + 1].parentI = i;
                    cellDetails[i][j + 1].parentJ = j;
                    return tracePath(cellDetails, dest);
                }

                // If the successor is already on the closed
                // list or if it is blocked, then ignore it.
                // Else do the following
                else if (closedList[i][j + 1] == false &&
                        isUnBlocked(i, j + 1) == true) {
                    gNew = cellDetails[i][j].g + 1.0;
                    hNew = calculateHValue(i, j + 1, dest);
                    fNew = gNew + hNew;

                    // If it isn’t on the open list, add it to
                    // the open list. Make the current square
                    // the parent of this square. Record the
                    // f, g, and h costs of the square cell
                    //                OR
                    // If it is on the open list already, check
                    // to see if this path to that square is better,
                    // using 'f' cost as the measure.
                    if (cellDetails[i][j + 1].f == Float.MAX_VALUE ||
                            cellDetails[i][j + 1].f > fNew) {
                        openList.add(new PositionWithP(fNew,
                                new Position(i, j + 1)));

                        // Update the details of this cell
                        cellDetails[i][j + 1].f = fNew;
                        cellDetails[i][j + 1].g = gNew;
                        cellDetails[i][j + 1].h = hNew;
                        cellDetails[i][j + 1].parentI = i;
                        cellDetails[i][j + 1].parentJ = j;
                    }
                }
            }
            //----------- 4th Successor (West) ------------
            if (isValid(i, j - 1) == true) {
                // Only process this cell if this is a valid one
                // If the destination cell is the same as the
                // current successor
                if (isDestination(i, j - 1, dest) == true) {
                    // Set the Parent of the destination cell
                    cellDetails[i][j - 1].parentI = i;
                    cellDetails[i][j - 1].parentJ = j;
                    return tracePath(cellDetails, dest);
                }

                // If the successor is already on the closed
                // list or if it is blocked, then ignore it.
                // Else do the following
                else if (closedList[i][j - 1] == false &&
                        isUnBlocked(i, j - 1) == true) {
                    gNew = cellDetails[i][j].g + 1.0;
                    hNew = calculateHValue(i, j - 1, dest);
                    fNew = gNew + hNew;

                    // If it isn’t on the open list, add it to
                    // the open list. Make the current square
                    // the parent of this square. Record the
                    // f, g, and h costs of the square cell
                    //                OR
                    // If it is on the open list already, check
                    // to see if this path to that square is better,
                    // using 'f' cost as the measure.
                    if (cellDetails[i][j - 1].f == Float.MAX_VALUE ||
                            cellDetails[i][j - 1].f > fNew) {
                        openList.add(new PositionWithP(fNew,
                                new Position(i, j - 1)));

                        // Update the details of this cell
                        cellDetails[i][j - 1].f = fNew;
                        cellDetails[i][j - 1].g = gNew;
                        cellDetails[i][j - 1].h = hNew;
                        cellDetails[i][j - 1].parentI = i;
                        cellDetails[i][j - 1].parentJ = j;
                    }
                }
            }
        }
        // When the destination cell is not found and the open
        // list is empty, then we conclude that we failed to
        // reach the destiantion cell. This may happen when the
        // there is no way to destination cell (due to blockages)
        return null;
    }

    public static class PositionWithP {
        public double p;
        public Position position;

        public PositionWithP(double p, Position position) {
            this.p = p;
            this.position = position;
        }
    }

    public static class CellDetail {
        public int parentI;
        public int parentJ;
        public double f;
        public double h;
        public double g;

        public CellDetail(int i, int j, double f, double g, double h) {
            this.parentI = i;
            this.parentJ = j;
            this.f = f;
            this.g = g;
            this.h = h;
        }
    }
}
