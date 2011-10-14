public class Message {
	/*
	RFC States:
	Each IRC message may consist of up to three main parts: the prefix (optional), the command, and the command parameters (of which there may be up to 15). The prefix, command, and all parameters are separated by one (or more) ASCII space character(s) (0x20).

	The presence of a prefix is indicated with a single leading ASCII colon character (':', 0x3b), which must be the first character of the message itself. There must be no gap (whitespace) between the colon and the prefix. The prefix is used by servers to indicate the true origin of the message. If the prefix is missing from the message, it is assumed to have originated from the connection from which it was received. Clients should not use prefix when sending a message from themselves; if they use a prefix, the only valid prefix is the registered nickname associated with the client. If the source identified by the prefix cannot be found from the server's internal database, or if the source is registered from a different link than from which the message arrived, the server must ignore the message silently.
	*/

	String prefix;
	String command;
	String[] commandParams;
	String rawMessage;
	
	public Message(String message) {
		this.rawMessage = message;
		getPrefix();
		getCommand();
		getCommandParams();
	}

	private void getPrefix() {
		// Prefix is indicated with a signle leading ACSII colon character (':', 0x3b)
		// Lets check to see if the : is a the beginning of the message
		if ( this.rawMessage.matches(":.*") ) {
			// Prefix is from ":TO_FIRST_WHITESPACE"
			String[] messageArray = this.rawMessage.split("\\s+"); 
			String rawPrefix = messageArray[0];

			// We need to remove the ":"
			this.prefix = rawPrefix.replace(":", "");
		} 
	}

	private void getCommand() {
		// Command is the second item when splitting by whitespace \s+
		String[] messageArray = this.rawMessage.split("\\s+");
		String rawCommand = messageArray[1];
		this.command = rawCommand;
	}

	private void getCommandParams() {
		// CommandParmas in the 3rd up to 15 items when splitting by whitespace \s+

		// We have to take into consideration that one of the params may have a ":"
		// If it does it should be the last parameter
		// It is allowed to have spaces
		String[] messageArray = this.rawMessage.split(":");

		String lastParam = "";
		String otherParams;
		
		if(messageArray.length > 2) {

			if(messageArray.length > 3) {
			   this.commandParams = new String[1];
	
			    // Since this has more than 2 ":" we need to do some stuff
			    for (int i = 2; i < messageArray.length; i++) {
				if(i > 2) {
				lastParam = lastParam.concat( ":" + messageArray[i]);
				} else {
				lastParam = lastParam.concat( messageArray[i] );
				}
			    }
			} else {
			// This works if there are only 2 ":" in the full string
			lastParam = messageArray[messageArray.length - 1];
			otherParams = messageArray[messageArray.length - 2];

			messageArray = otherParams.split("\\s+");
			
			this.commandParams = new String[messageArray.length - 2 + 1];
			for (int i = 2; i < messageArray.length; i++) {
			    this.commandParams[i -2] = messageArray[i];
			}
			this.commandParams[commandParams.length-1] = lastParam;
			}

		} else {
			messageArray = this.rawMessage.split("\\s+"); 

	    		// TODO
	    		// If prefix isn't found then the index changes 
		
	    		if(messageArray.length > 2) {		
				this.commandParams = new String[messageArray.length -2];
				for(int i = 2; i < messageArray.length; i++) {
		  		this.commandParams[i - 2] = messageArray[i]; 
				}
	    		}
		} 

		for(int i = 0; i < this.commandParams.length; i++) {
		}
	}

	public String getRawMessage() {
		return this.rawMessage;
	}

}
