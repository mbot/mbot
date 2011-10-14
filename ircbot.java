import java.io.*;
import java.net.*;

public class ircbot {
	private String server;
	private int port;
	private String channel;
	private String nick;
	private Socket socket;
	private BufferedWriter bufferedWriter;
	private BufferedReader bufferedReader;

	public ircbot(String server, int port, String nick, String channel) {
		this.server = server;
		this.port = port;
		this.channel = channel;
		this.nick = nick;
	}

	// 
	public ircbot() {

	}

	public boolean connect() {
	   try {
		this.socket = new Socket(this.server, this.port);
		
		this.bufferedWriter = new BufferedWriter( new OutputStreamWriter( socket.getOutputStream() ) );

		this.bufferedReader = new BufferedReader( new InputStreamReader( socket.getInputStream() ) ) ;

		// If were connected
		// Send NICK
		this.bufferedWriter.write( "NICK " + this.nick + "\n" );

		// TODO
		// Check to see if the NICK command Errored Out
	
		// Send USER
		System.out.println( "USER " + this.nick + " 0 * :ClubbsJavaBot\n" );
		this.bufferedWriter.write( "USER " + this.nick + " 0 * :ClubbsJavaBot\n" );

		// TODO 
		// Check to see if the USER command Errored Out

		// Send the bufferedWriter
		this.bufferedWriter.flush();

			return true;
	   } catch (Exception e) {
		System.out.println("Failed in the Socket Layer");
		return false;
	   }

	}

	public boolean join ( String channel ) {
	    try {
		System.out.println("JOIN " + channel);
		this.bufferedWriter.write("JOIN " + channel + "\n");
		this.bufferedWriter.flush();
	    } catch (IOException e) {
		System.out.println("JOIN: Failed");
		return false;
	    }

		return true;
	}

	public Message listen () {
		try {
			String rawLine = this.bufferedReader.readLine();
			Message message = new Message(rawLine);
			return message;
		} catch (IOException e) {
			return null;
		}
	}

	public boolean write ( String line ) {
	    try {
		System.out.println( line );
		this.bufferedWriter.write(line);
		this.bufferedWriter.flush();
		return true;
	    } catch (IOException e) {
		return false;
	    }
	}

}
