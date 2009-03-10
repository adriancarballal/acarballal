package es.udc.acarballal.elmas.model.message;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import es.udc.acarballal.elmas.model.userprofile.UserProfile;

@Entity
@org.hibernate.annotations.Entity(mutable=false)
public class Message {

	private Long id;
	private UserProfile receiver;
	private UserProfile sender;
	private String text;
	
	public Message(){}
	
	public Message(UserProfile from, UserProfile to, String text){
		this.sender = from;
		this.receiver = to;
		this.text = text;
	}
	
	@Column(name = "id")
	@SequenceGenerator( // It only takes effect for
	name = "messageIdGenerator", // databases providing identifier
	sequenceName = "messageSeq")
	// generators.
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "messageIdGenerator")
	public Long getId(){
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(optional=false, fetch=FetchType.EAGER)
	@JoinColumn(name="receiver")
	public UserProfile getReceiver() {
		return receiver;
	}
	public void setReceiver(UserProfile receiver) {
		this.receiver = receiver;
	}
	
	@ManyToOne(optional=false, fetch=FetchType.EAGER)
	@JoinColumn(name="sender")
	public UserProfile getSender() {
		return sender;
	}
	public void setSender(UserProfile sender) {
		this.sender = sender;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}
