package es.udc.acarballal.elmas.model.videocomment;

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
import es.udc.acarballal.elmas.model.video.Video;

@Entity
@org.hibernate.annotations.Entity(mutable=false)
public class VideoComment {

	private Long commentId;
	private Video video;
	private UserProfile commentator;
	private String comment;
	private Calendar date;
	
	public VideoComment(){ }
	
	public VideoComment(Video video, UserProfile commentator, 
			String comment, Calendar date){
		this.video = video;
		this.commentator = commentator;
		this.comment = comment;
		this.date = date;
	}

	@Column(name = "cmmtId")
	@SequenceGenerator( // It only takes effect for
	name = "VideoCommentIdGenerator", // databases providing identifier
	sequenceName = "VidCommentSeq")
	// generators.
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "VideoCommentIdGenerator")
	
	public Long getCommentId() {
		return commentId;
	}

	public void setCommentId(Long commentId) {
		this.commentId = commentId;
	}

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="vidId")
	public Video getVideo() {
		return video;
	}

	public void setVideo(Video video) {
		this.video = video;
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
		if ((obj == null) || !(obj instanceof VideoComment)) {
			return false;
		}

		VideoComment theOther = (VideoComment) obj;

		return commentId.equals(theOther.commentId)
			&& (video != null) && video.equals(theOther.getVideo())
			&& (commentator != null) && commentator.equals(theOther.getCommentator())
			&& (comment != null) && comment.equals(theOther.comment);
	}
}
