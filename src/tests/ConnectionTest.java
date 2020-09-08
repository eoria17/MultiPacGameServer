package tests;

import java.io.IOException;

import client.Client;
import server.GameServer;

public class ConnectionTest {
	Client client;
	GameServer server;

	@Before
	public void setUp() throws Exception {
		server = new GameServer(8080);
		server.start();
		
	}

	@After
	public void tearDown() throws Exception {
		client.close();
		server.shutdown();
	}

	@Test
	public void test() throws IOException {
		client = new Client("localhost", 8080);
		assertTrue(client.connect());
	}

}
