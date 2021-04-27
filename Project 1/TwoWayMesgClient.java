/*
 * Implementation of a two way client/server in java
 * By Timothy Gedney for CSCE 416
 */

// Package for I/O related stuff
import java.io.*;

// Package for socket related stuff
import java.net.*;

/*
 * This class does all the client's job
 * It connects to the server at the given address
 * and alternately sends messages typed by the user
 * and displays messages received from the server
 */
public class TwoWayMesgClient {
	public static void main(String args[])
	{
		// Needs server's contact information and user name
		if (args.length != 2) {
			System.out.println("usage: java TwoWayMesgClient <host> <port>");
			System.exit(1);
		}

		// Be prepared to catch socket related exceptions
		try {
			// Connect to the server at the given host and port
			Socket sock = new Socket(args[0], Integer.parseInt(args[1]));
			System.out.println(
					"Connected to server at " + args[0] + ":" + args[1]);

			// Prepare to read from server
			BufferedReader fromServerReader = new BufferedReader(
					new InputStreamReader(sock.getInputStream()));

			// Prepare to write to server with auto flush on
			PrintWriter toServerWriter =
					new PrintWriter(sock.getOutputStream(), true);

			// Prepare to read from keyboard
			BufferedReader fromUserReader = new BufferedReader(
					new InputStreamReader(System.in));
			
			// Keep doing till we get EOF from server or user
			while (true) {
				// Read a line from the keyboard
				String line = fromUserReader.readLine();

				// If we get null, it means user is done
				if (line == null) {
					System.out.println("Closing connection");
					break;
				}

				// Send the line to the server
				toServerWriter.println(line);

				
				//read line from server
				String Sline = fromServerReader.readLine();
				//print line from server
				System.out.println("Server: " + Sline);

			}

			// close the socket and exit
			toServerWriter.close();
			sock.close();
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
}
