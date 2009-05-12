package es.udc.acarballal.elmas.model.vote;

import java.util.Calendar;

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
import es.udc.acarballal.elmas.model.video.Video;

@Entity
@org.hibernate.annotations.Entity(mutable=false)
public class Vote {

	public enum VOTE_TYPES {BAD, GOOD, NORMAL, VERY_BAD, VERY_GOOD}
	
	private Calendar date;
	private Video video;
	private VOTE_TYPES vote;
	private Long voteId;
	private UserProfile voter;
	
	public Vote(){ }
	
	public Vote(Video video, UserProfile voter, VOTE_TYPES vote, Calendar date){
		this.video = video;
		this.voter = voter;
		this.vote = vote;
		this.date = date;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Calendar getDate() {
		return date;
	}

	@ManyToOne(optional=false)
    @JoinColumn(name="vidId")
	public Video getVideo() {
		return video;
	}

	public VOTE_TYPES getVote() {
		return vote;
	}

	@SequenceGenerator( // It only takes effect for
	name = "VoteIdGenerator", // databases providing identifier
	sequenceName = "VoteSeq")
	// generators.
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "VoteIdGenerator")
	public Long getVoteId() {
		return voteId;
	}

	@ManyToOne(optional=false)
    @JoinColumn(name="voter")
	public UserProfile getVoter() {
		return voter;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	public void setVideo(Video video) {
		this.video = video;
	}

	public void setVote(VOTE_TYPES vote) {
		this.vote = vote;
	}

	public void setVoteId(Long voteId) {
		this.voteId = voteId;
	}

	public void setVoter(UserProfile voter) {
		this.voter = voter;
	}
}
