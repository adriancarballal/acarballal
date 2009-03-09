package es.udc.acarballal.elmas.model.favourite;

import java.util.List;

import es.udc.acarballal.elmas.model.video.Video;
import es.udc.pojo.modelutil.dao.GenericDao;

public interface FavouriteDao extends GenericDao<Favourite, Long>{
	
	public List<Video> findFavourites(Long userProfileId, int startIndex, int count);
	public boolean isFavourite(Long userProfileId, Long videoId);
	public void removeFromFavourites(Long userProfileId, Long videoId);
	
}
