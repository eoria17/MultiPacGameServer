import game.OutofBoundaryException;
import game.Position;
import game.SamePositionException;
import game.SearchPath;
import org.junit.Assert;
import org.junit.Test;
import serverConnection.ConnectionHandler;

import java.util.ArrayList;

public class SearchPathTest {
    @Test
    public void aStarSearchTest() throws Exception {
        ConnectionHandler.gridObstacles = new Position[] {new Position(0, 1)};
        ArrayList<Position> result = SearchPath.aStarSearch(new Position(0, 0), new Position(0, 2));
        Assert.assertEquals(5, result.size());
        Assert.assertEquals(0, result.get(0).getRow());
        Assert.assertEquals(2, result.get(0).getCol());
        Assert.assertEquals(1, result.get(1).getRow());
        Assert.assertEquals(2, result.get(1).getCol());
        Assert.assertEquals(1, result.get(2).getRow());
        Assert.assertEquals(1, result.get(2).getCol());
        Assert.assertEquals(1, result.get(3).getRow());
        Assert.assertEquals(0, result.get(3).getCol());
        Assert.assertEquals(0, result.get(4).getRow());
        Assert.assertEquals(0, result.get(4).getCol());
    }

    @Test (expected= SamePositionException.class)
    public void aStarSearchTestSamePosition() throws Exception {
        ConnectionHandler.gridObstacles = new Position[] {new Position(0, 1)};
        SearchPath.aStarSearch(new Position(0, 2), new Position(0, 2));
    }

    @Test (expected = OutofBoundaryException.class)
    public void aStarSearchTestOutOfBoundary() throws Exception {
        ConnectionHandler.gridObstacles = new Position[] {new Position(0, 1)};
        SearchPath.aStarSearch(new Position(0, 1), new Position(0, 2));
        SearchPath.aStarSearch(new Position(0, 0), new Position(0, 1));
    }
}
