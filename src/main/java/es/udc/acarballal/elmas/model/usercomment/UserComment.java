package es.udc.acarballal.elmas.model.usercomment;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

	private Long commentId;
	private UserProfile commented;
	private UserProfile commentator;
	private String comment;
	private Calendar date;
	
	public UserComment(){	}
	
	public UserComment(UserProfile commented, UserProfile commentator, 
			String comment, Calendar date){
		
		this.commented = commented;
		this.commentator = commentator;
		this.comment = comment;
		this.date = date;
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

	public void setCommentId(Long commentId) {
		this.commentId = commentId;
	}

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="commented")
	public UserProfile getCommented() {
		return commented;
	}

	public void setCommented(UserProfile commented) {
		this.commented = commented;
	}

	@ManyToOne(optional=false, fetch=FetchType.EAGER)
    @JoinColumn(name="commentator")
	public UserProfile getCommentator() {
		return commentator;
	}

	public void setCommentator(UserProfile commentator) {
		this.commentator = commentator;
	}

	@Column(name = "cmmnt")
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
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
	
}
