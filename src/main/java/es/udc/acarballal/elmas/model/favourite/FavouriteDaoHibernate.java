package es.udc.acarballal.elmas.model.favourite;

import java.util.List;

import org.springframework.stereotype.Repository;

import es.udc.acarballal.elmas.model.video.Video;
import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

@Repository
public class FavouriteDaoHibernate extends
		GenericDaoHibernate<Favourite, Long> implements FavouriteDao{

	public List<Video> findFavourites(Long userProfileId, int startIndex, int count) {
			return getSession().createQuery("SELECT v.favourite FROM Favourite v " +
			"WHERE v.user.userProfileId=:userId order by v.id desc").
			setParameter("userId", userProfileId).		
			setFirstResult(startIndex).setMaxResults(count).list();
	}

	public boolean isFavourite(Long userProfileId, Long videoId){

		long count = 
			(Long) getSession().createQuery("SELECT count(c) FROM Favourite c " +
					"WHERE c.favourite.videoId=:videoId AND c.user.userProfileId=:userProfileId").
					setParameter("videoId", videoId).
					setParameter("userProfileId", userProfileId).
			uniqueResult();
		return count>0;

	}
	
	public void removeFromFavourites(Long userProfileId, Long videoId){
		
		getSession().createQuery("DELETE FROM Favourite c " +
		"WHERE c.favourite.videoId=:videoId AND c.user.userProfileId=:userProfileId").
		setParameter("videoId", videoId).
		setParameter("userProfileId", userProfileId).executeUpdate();
	}

}
