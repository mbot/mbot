public class teamchat {

	public static void main( String[] args ) {
		if(args.length < 2) {
			System.out.println("Usuage: java teamchat nick channel");
			System.exit(0);
		}

		System.out.println("Starting teamchat");
	
		ircbot b = new ircbot("localhost", 6667, args[0], "#"+args[1]);

		System.out.println("Bot has been initialized");

		System.out.println("Connecting");

		String channel = "#" + args[1];

		if( b.connect() ) {
			if( b.join(channel) ) {
				// Lets listen
				String line; 
				Message message;
				while ( ( message = b.listen() ) != null ) {
					System.out.println(message.getRawMessage());

					line = message.getRawMessage();
					if ( line.matches(".*JOIN.*") ) {
						String [] parts = line.split("!");
						parts = parts[0].split(":");
						String nick = parts[1];
						b.write("MODE " + channel + " +o " + nick + "\n");
					}

					if ( line.matches("PING.*") ) {
						b.write("PONG " + channel + "\n");
					}


					if ( line.matches(".*PRIVMSG.*:!mbot.*") ) {
						String [] requestArray = line.split("!mbot ");
						if( requestArray.length == 1) {
							b.write("PRIVMSG " + channel + " :Help Needed \n");
						} else {
					       		String request = requestArray[1];	
							b.write("PRIVMSG " + channel + " :Request - " + request + "\n");	
						
							if ( request.matches( "CUT_BRANCH.*" ) ) {
								requestArray = request.split(" ");
								if (requestArray.length == 4) {
									b.write("PRIVMSG " + channel + " :cvs rtag -b -r " + requestArray[1] + " " + requestArray[2] + " " + requestArray[3] + "\n");	
								} else {
									b.write("PRIVMSG " + channel + " :USAGE - !mbot CUT_BRANCH FROM_BRANCH TO_BRANCH MODULE\n");	
								} 
							}
						}
					}
				}
			} 
		}

	}

}
