package es.udc.acarballal.elmas.model.vote;

import java.util.Calendar;

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
public class Vote {

	public enum VOTE_TYPES {VERY_BAD, BAD, NORMAL, GOOD, VERY_GOOD}
	
	private Long voteId;
	private Video video;
	private UserProfile voter;
	private VOTE_TYPES vote;
	private Calendar date;
	
	public Vote(){ }
	
	public Vote(Video video, UserProfile voter, VOTE_TYPES vote, Calendar date){
		this.video = video;
		this.voter = voter;
		this.vote = vote;
		this.date = date;
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

	public void setVoteId(Long voteId) {
		this.voteId = voteId;
	}

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="vidId")
	public Video getVideo() {
		return video;
	}

	public void setVideo(Video video) {
		this.video = video;
	}

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="voter")
	public UserProfile getVoter() {
		return voter;
	}

	public void setVoter(UserProfile voter) {
		this.voter = voter;
	}

	public VOTE_TYPES getVote() {
		return vote;
	}

	public void setVote(VOTE_TYPES vote) {
		this.vote = vote;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}
}
