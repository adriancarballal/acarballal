package es.udc.acarballal.elmas.model.videocommentcomplaint;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import es.udc.acarballal.elmas.model.userprofile.UserProfile;
import es.udc.acarballal.elmas.model.videocomment.VideoComment;

@Entity
@org.hibernate.annotations.Entity(mutable=false)
public class VideoCommentComplaint {
	
	private Long complaintId;
	private VideoComment reference;
	private UserProfile complainer;
	private Calendar date;
	
	public VideoCommentComplaint(){ }
	
	public VideoCommentComplaint(VideoComment ref, UserProfile user, Calendar date){
		this.reference = ref;
		this.complainer = user;
		this.date = date;
	}

	@Column(name = "id")
	@SequenceGenerator( // It only takes effect for
	name = "VideoCommentComplaintIdGenerator", // databases providing identifier generators.
	sequenceName = "VideoCommentComplaintSeq")	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "VideoCommentComplaintIdGenerator")
	public Long getComplaintId() {
		return complaintId;
	}

	public void setComplaintId(Long complaintId) {
		this.complaintId = complaintId;
	}
	
	@ManyToOne(optional=false, fetch=FetchType.EAGER)
	public VideoComment getReference() {
		return reference;
	}
	
	public void setReference(VideoComment reference) {
		this.reference = reference;
	}

	@ManyToOne(optional=false, fetch=FetchType.EAGER)
	public UserProfile getComplainer() {
		return complainer;
	}

	public void setComplainer(UserProfile complainer) {
		this.complainer = complainer;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}
}
