/* Assignment 2 CSCE 416
 * Written By: Timothy Gedney
 */

import java.io.*; // I/O
import java.net.*; // socket
import java.util.*; //objects

public class HttpClient {
	public static void main(String args[]) throws IOException { //catch errors with attaining url or download
		String keyboard = args[0]; //user inputs url
		URL url = new URL(keyboard); //convert to url
		HttpURLConnection connect = (HttpURLConnection) url.openConnection(); //creat connection
		connect.setRequestMethod("GET"); //request
		InputStream input = connect.getInputStream(); //input stream
		System.out.println("- Downloading: " + keyboard); //print url being downloaded
		Map<String, List<String>> header = connect.getHeaderFields(); //header Map to get header and contents
		for (Map.Entry<String, List<String>> item : header.entrySet()) { //loops through map and gets contents from header's
			System.out.println(item.getKey() + ": " + item.getValue());
		}

		//check for redirects
		if (connect.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP || connect.getResponseCode() == HttpURLConnection.HTTP_SEE_OTHER ||
			connect.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM) {
			url = new URL(connect.getHeaderField("Location")); //use location for connection redirect
			connect = (HttpURLConnection) url.openConnection(); //connect again, repeat same as above
			input = connect.getInputStream();
			System.out.println("- Downloading: " + keyboard);
			header = connect.getHeaderFields();
			for (Map.Entry<String, List<String>> item : header.entrySet()) {
				System.out.println(item.getKey() + ": " + item.getValue());
			}
		}
		
		//read in downloaded info and print to console
		BufferedReader readIn = new BufferedReader(new InputStreamReader(input));
		String output;
		while ((output = readIn.readLine()) != null) {
			System.out.println(output); //print to console
		}
		readIn.close();
	}
}
