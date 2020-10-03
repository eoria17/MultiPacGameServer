package packets;

import game.Grid;
import game.Position;

import java.io.Serializable;

public class MonsterPositionPacket implements Serializable {
    private static final long serialVersionUID = 1L;

    public Position[] position;

    public MonsterPositionPacket(Position[] position) {
        this.position = position;
    }
}
