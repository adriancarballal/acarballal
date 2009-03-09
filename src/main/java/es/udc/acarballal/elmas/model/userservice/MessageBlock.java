package es.udc.acarballal.elmas.model.userservice;

import java.util.List;

import es.udc.acarballal.elmas.model.message.Message;

public class MessageBlock {
	
	private boolean existMoreMessages;
    private List<Message> messages;
    
    public MessageBlock(List<Message> messages, boolean existMoreMessages){
    	this.messages = messages;
    	this.existMoreMessages = existMoreMessages;
    }

	public boolean getExistMoreMessages() {
		return existMoreMessages;
	}

	public List<Message> getmessages() {
		return messages;
	}
}
