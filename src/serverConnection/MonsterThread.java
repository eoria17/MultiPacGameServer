package serverConnection;

import game.Monster;
import game.Position;
import packets.MonsterPositionPacket;

import static java.lang.Thread.sleep;

public class MonsterThread implements Runnable {
    Monster monster;

    public MonsterThread(Monster monster) {
        this.monster = monster;
    }

    @Override
    public void run() {
        while (true) {
            Position position = monster.move();
            MonsterPositionPacket positionPacket = new MonsterPositionPacket(position);
            for (int i = 0; i < ConnectionHandler.connections.size(); i++) {
                Connection c = ConnectionHandler.connections.get(i);
                c.sendObject(positionPacket);
            }
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
