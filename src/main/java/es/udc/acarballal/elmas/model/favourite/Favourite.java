package es.udc.acarballal.elmas.model.favourite;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import es.udc.acarballal.elmas.model.userprofile.UserProfile;
import es.udc.acarballal.elmas.model.video.Video;

@Entity
@org.hibernate.annotations.Entity(mutable=false)
public class Favourite {

	private Long id;
	private UserProfile user;
	private Video favourite;
	
	public Favourite(){	}
	
	public Favourite(UserProfile user, Video favourite){
		this.user = user;
		this.favourite = favourite;
	}
	
	@Column(name = "favouriteId")
	@SequenceGenerator( // It only takes effect for
	name = "favouriteIdGenerator", // databases providing identifier
	sequenceName = "favouriteSeq")
	// generators.
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "favouriteIdGenerator")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(optional=false)
	@JoinColumn(name="user")
	public UserProfile getUser() {
		return user;
	}

	public void setUser(UserProfile user) {
		this.user = user;
	}

	@ManyToOne(optional=false)
	@JoinColumn(name="favourite")
	public Video getFavourite() {
		return favourite;
	}

	public void setFavourite(Video favourite) {
		this.favourite = favourite;
	}

	@Override
	public boolean equals(Object obj) {
		if ((obj == null) || !(obj instanceof Video)) return false;

		Favourite theOther = (Favourite) obj;

		return this.getId().equals(theOther.getId()) &&
				this.getUser().equals(theOther.getUser()) &&
				this.getFavourite().equals(theOther.getFavourite());
	}
}
