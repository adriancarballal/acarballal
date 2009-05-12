package es.udc.acarballal.elmas.model.usercommentcomplaint;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import es.udc.acarballal.elmas.model.usercomment.UserComment;
import es.udc.acarballal.elmas.model.userprofile.UserProfile;

@Entity
@org.hibernate.annotations.Entity(mutable=false)
public class UserCommentComplaint {
	
	private UserProfile complainer;
	private Long complaintId;
	private Calendar date;
	private UserComment reference;
	
	public UserCommentComplaint(){ }
	
	public UserCommentComplaint(UserComment ref, UserProfile user, Calendar date){
		this.reference = ref;
		this.complainer = user;
		this.date = date;
	}

	@ManyToOne(optional=false)
	@JoinColumn(name="complainer")
	public UserProfile getComplainer() {
		return complainer;
	}

	@Column(name = "id")
	@SequenceGenerator( // It only takes effect for
	name = "UserCommentComplaintIdGenerator", // databases providing identifier generators.
	sequenceName = "UserCommentComplaintSeq")	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "UserCommentComplaintIdGenerator")
	public Long getComplaintId() {
		return complaintId;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	public Calendar getDate() {
		return date;
	}
	
	@ManyToOne(optional=false)
	@JoinColumn(name="reference")
	public UserComment getReference() {
		return reference;
	}

	public void setComplainer(UserProfile complainer) {
		this.complainer = complainer;
	}

	public void setComplaintId(Long complaintId) {
		this.complaintId = complaintId;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	public void setReference(UserComment reference) {
		this.reference = reference;
	}
}
