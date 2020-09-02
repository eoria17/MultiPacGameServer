package tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import client.Client;
import clientPackets.AddConnectionPacket;
import server.PlayerLimitException;
import server.Server;

public class PlayerLimitTest {

	Client player1;
	Client player2;
	Client player3;
	Client player4;
	Client player5;
	Server server;
	
	@Before
	public void setUp() throws Exception {
		server = new Server(8080);
		server.start();
	}

	@After
	public void tearDown() throws Exception {
		player1.close();
		player2.close();
		player3.close();
		player4.close();
		player5.close();
		server.shutdown();
	}

	@Test (expected = PlayerLimitException.class)
	public void test() {
		player1 = new Client("localhost", 8080);
		player1.connect();
		
		AddConnectionPacket packet = new AddConnectionPacket();
		player1.sendObject(packet);
		
		player2 = new Client("localhost", 8080);
		player2.connect();
		
		player3 = new Client("localhost", 8080);
		player3.connect();
		
		player4 = new Client("localhost", 8080);
		player4.connect();
		
		player5 = new Client("localhost", 8080);
		player5.connect();
		
	}

}
