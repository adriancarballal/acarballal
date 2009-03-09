package es.udc.acarballal.elmas.model.video;

import java.util.List;
import java.util.Random;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

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
	
	public Video findRandomVotableVideo(Long userProfileId, int preSelected) 
		throws InstanceNotFoundException{
		List<Video> result = 
			(List<Video>)getSession().createQuery(
					"select v from Video v where v " +
					"not in (select distinct(e.video) from Vote e where " +
					"e.voter.userProfileId = :userId) ORDER BY v.date DESC"). 
					setParameter("userId", userProfileId).		
					setFirstResult(0).setMaxResults(preSelected).list();
		if(result.size()==0){
			throw new InstanceNotFoundException("No video available for voting", userProfileId.toString());
		}
		int range = result.size();
		Random rand = new Random();
		int index = 0 + rand.nextInt(range);
		return result.get(index);
	}
		
}
