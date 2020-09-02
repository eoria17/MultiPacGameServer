package tests;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import client.Client;
import server.Server;

public class ConnectionTest {
	Client client;
	Server server;

	@Before
	public void setUp() throws Exception {
		server = new Server(8080);
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
