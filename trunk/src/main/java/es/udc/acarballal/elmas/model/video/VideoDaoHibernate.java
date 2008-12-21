package es.udc.acarballal.elmas.model.video;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

@Repository
public class VideoDaoHibernate extends
		GenericDaoHibernate<Video, Long> implements VideoDao{
	
	@SuppressWarnings("unchecked")
	public List<Video> findByTitle(String key, int startIndex, int count){
		
		String[] keys = key.split(" ");
		Criteria q = getSession().createCriteria(Video.class);
		
		for(int i=0;i<keys.length;i++){
			q.add(Restrictions.ilike("title","%"+keys[i]+"%"));
		}
		
		return q.setFirstResult(startIndex).setMaxResults(count).list();
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Video> findByUser(Long userId, int startIndex, int count){
		
		return getSession().createQuery("SELECT v FROM Video v " +
		"WHERE v.userProfile.userProfileId=:userId").
		setParameter("userId", userId).		
		setFirstResult(startIndex).setMaxResults(count).list();
		
	}
	
	@SuppressWarnings("unchecked")
	public int getNumberOfVideosByUser(Long userId){
		
		long numberOfVideos = (Long) getSession().createQuery(
                "SELECT COUNT(v) FROM Video v " +
				"WHERE v.userProfile.userProfileId=:userId").
        		setParameter("userId", userId).		
                uniqueResult();
		
		return (int) numberOfVideos;
	}
}
