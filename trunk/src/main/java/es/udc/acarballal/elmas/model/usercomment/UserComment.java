package es.udc.acarballal.elmas.model.usercomment;

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

import es.udc.acarballal.elmas.model.userprofile.UserProfile;

@Entity
@org.hibernate.annotations.Entity(mutable=false)
public class UserComment {

	private String comment;
	private UserProfile commentator;
	private UserProfile commented;
	private Long commentId;
	private Calendar date;
	
	public UserComment(){	}
	
	public UserComment(UserProfile commented, UserProfile commentator, 
			String comment, Calendar date){
		
		this.commented = commented;
		this.commentator = commentator;
		this.comment = comment;
		this.date = date;
	}

	@Override
	public boolean equals(Object obj) {
		if ((obj == null) || !(obj instanceof UserComment)) {
			return false;
		}

		UserComment theOther = (UserComment) obj;

		return commentId.equals(theOther.commentId)
			&& (commented != null) && commented.equals(theOther.getCommented())
			&& (commentator != null) && commentator.equals(theOther.getCommentator())
			&& (comment != null) && comment.equals(theOther.comment);
	}

	@Column(name = "cmmnt")
	public String getComment() {
		return comment;
	}

	@ManyToOne(optional=false)
    @JoinColumn(name="commentator")
	public UserProfile getCommentator() {
		return commentator;
	}

	@ManyToOne(optional=false)
    @JoinColumn(name="commented")
	public UserProfile getCommented() {
		return commented;
	}

	@Column(name = "cmmtId")
	@SequenceGenerator( // It only takes effect for
	name = "CommentIdGenerator", // databases providing identifier
	sequenceName = "UserCommentSeq")
	// generators.
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "CommentIdGenerator")
	public Long getCommentId() {
		return commentId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Calendar getDate() {
		return date;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setCommentator(UserProfile commentator) {
		this.commentator = commentator;
	}

	public void setCommented(UserProfile commented) {
		this.commented = commented;
	}

	public void setCommentId(Long commentId) {
		this.commentId = commentId;
	}
	
	public void setDate(Calendar date) {
		this.date = date;
	}
	
}
