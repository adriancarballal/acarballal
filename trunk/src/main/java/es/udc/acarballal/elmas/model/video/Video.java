package es.udc.acarballal.elmas.model.video;

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
public class Video {

	public Long videoId;
	public UserProfile userProfile;
	public String title;
	public String comment;
	public String snapshot;
	public String original;
	public String flvVideo;
	public String mp4Video;
	public Calendar date;
	
	public Video(){
		
	}
	
	public Video(UserProfile userProfile, String title, String comment,
			String snapshot, String original, String flvVideo, String mp4Video, 
			Calendar date){
		
		this.userProfile = userProfile;
		this.title = title;
		this.comment = comment;
		this.snapshot = snapshot;
		this.original = original;
		this.flvVideo = flvVideo;
		this.mp4Video = mp4Video;
		this.date = date;
	}
	
	@Column(name = "vidId")
	@SequenceGenerator( // It only takes effect for
	name = "VideoIdGenerator", // databases providing identifier
	sequenceName = "VideoSeq")
	// generators.
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "VideoIdGenerator")
	public Long getVideoId() {
		return videoId;
	}
	public void setVideoId(Long videoId) {
		this.videoId = videoId;
	}	
	
	@ManyToOne(optional=false, fetch=FetchType.EAGER)
    @JoinColumn(name="usrId")
	public UserProfile getUserProfile() {
		return userProfile;
	}
	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Column(name = "cmmnt")
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	@Column(name = "urlshot")
	public String getSnapshot() {
		return snapshot;
	}
	public void setSnapshot(String snapshot) {
		this.snapshot = snapshot;
	}
	
	public String getOriginal() {
		return original;
	}

	public void setOriginal(String original) {
		this.original = original;
	}

	@Column(name = "urlflv")
	public String getFlvVideo() {
		return flvVideo;
	}

	public void setFlvVideo(String flvVideo) {
		this.flvVideo = flvVideo;
	}

	@Column(name = "urlmp4")
	public String getMp4Video() {
		return mp4Video;
	}

	public void setMp4Video(String mp4Video) {
		this.mp4Video = mp4Video;
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
		if ((obj == null) || !(obj instanceof Video)) {
			return false;
		}

		Video theOther = (Video) obj;

		return (title != null) && title.equals(theOther.title)
			&& videoId.equals(theOther.videoId)	
			&& userProfile.getUserProfileId().equals(theOther.getUserProfile()
					.getUserProfileId())
			&& (comment != null) && comment.equals(theOther.comment)
			&& (snapshot != null) && snapshot.equals(theOther.snapshot)
			&& (original != null) && original.equals(theOther.original)
			&& (flvVideo != null) && flvVideo.equals(theOther.flvVideo)
			&& (mp4Video != null) && mp4Video.equals(theOther.mp4Video);
	}

		
}
