// Timothy Gedney CSCE 416 Assignment 3

import java.io.*; //I/O
import java.net.*; //Socket
import java.util.*; //Data structure

/*
 * Server
 * Parent thread (main method) creates threads based on new connections
 * Child thread reads from the keyboard and writes to socket
 * Since a thread is being created with this class object,
 * this class declaration includes "implements Runnable"
 */
public class ConferenceServer implements Runnable {
	private Socket clientSocket; //new socket
	private static List<PrintWriter> clientList; //active clients

	//constructor sets child
	public ConferenceServer(Socket socket) {
		clientSocket = socket;
	}
	
	//add client
	public static synchronized boolean addClient(PrintWriter toClientWriter) {
		return clientList.add(toClientWriter);
	}
	
	//remove client
	public static synchronized boolean removeClient(PrintWriter toClientWriter) {
		return clientList.remove(toClientWriter);
	}
	
	//relay to clients
	public static synchronized void relayClient(PrintWriter fromClientWriter, String s) {
		//go through client list
		for (int i = 0; i < clientList.size(); i++) {
			//relay message to all clients, except the sender
			clientList.get(i).println(s);
		}
	}

	// The child thread starts here
	public void run() {
		//read from client and relay to other clients
		try {
			//prepare to read from socket
			BufferedReader fromSockReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
			//get client name
			String clientName = fromSockReader.readLine();
			
			//prepare to write to socket
			PrintWriter toSockWriter = new PrintWriter(clientSocket.getOutputStream(), true);
			
			//add client to active client list
			clientList.add(toSockWriter);
			
			// Keep doing till user types EOF (Ctrl-D)
			while (true) {
				// Read a line from the user
				String line = fromSockReader.readLine();

				// If we get null, it means EOF, so quit
				if (line == null) {
					break;
				} else { //relay line to all clients
					relayClient(toSockWriter, line);
				}
			}
		}
		catch (Exception e) {
			System.out.println(e);
			System.exit(1);
		}
		
		// End the other thread too
		System.exit(0);
	}

	/*
	 * Conference server program
	 * Accepts new clients
	 */
	public static void main(String args[]) {
		// Server needs a port to listen on
		if (args.length != 1) {
			System.out.println("usage: java ConferenceServer <server port>");
			System.exit(1);
		}

		//catch socket errors
		try {
			//creates server socket with given port
			ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));
			
			//initialize list
			clientList = new ArrayList<PrintWriter>();
			
			//continue accepting clients
			while (true) {
				//wait to accept a client
				System.out.println("Waiting for a client...");
				serverSocket.accept();
				System.out.println("Connected to a client.");
				
				//spawn thread and relay messages
				Thread child = new Thread(new ConferenceServer(new Socket()));
				child.start();
			}
		} catch (Exception e) {
			System.out.println(e);
			System.exit(1);
		}
		
		//end threads
		System.exit(0);
	}
}