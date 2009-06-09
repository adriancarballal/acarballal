package es.udc.acarballal.elmas.model.video;

import java.util.Calendar;
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
		"WHERE v.userProfile.userProfileId=:userId ORDER BY v.date DESC").
		setParameter("userId", userId).		
		setFirstResult(startIndex).setMaxResults(count).list();
		
	}
	
	public Video findRandomVotableVideo(Long userProfileId, int preSelected) 
		throws InstanceNotFoundException{
		List<Video> result = 
			(List<Video>)getSession().createQuery(
					"select v from Video v where v " +
					"not in (select distinct(e.video) from Vote e where " +
					"e.voter.userProfileId = :userId) " +
					"and v.userProfile.userProfileId <> :userId " +
					"ORDER BY v.date DESC"). 
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

	public boolean addedVideo(Long userProfileId, Calendar today) {
		Calendar startDate = (Calendar)today.clone();
		startDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		startDate.set(Calendar.HOUR, 0);
		startDate.set(Calendar.MINUTE, 0);
		startDate.set(Calendar.SECOND, 0);
		startDate.set(Calendar.AM_PM, Calendar.AM);
		startDate.set(Calendar.MILLISECOND, 0);
		Calendar endDate = (Calendar)today.clone();
		endDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		endDate.set(Calendar.HOUR, 23);
		endDate.set(Calendar.MINUTE, 59);
		endDate.set(Calendar.SECOND, 59);
		endDate.set(Calendar.AM_PM, Calendar.PM);
		endDate.set(Calendar.MILLISECOND, 999);
		long count = 
			(Long) getSession().createQuery("SELECT count(c) FROM Video c " +
					"WHERE c.userProfile.userProfileId=:userProfileId AND " +
                	"c.date >= :startDate AND c.date <= :endDate").
					setParameter("userProfileId", userProfileId).
					setCalendar("startDate", startDate).
	                setCalendar("endDate", endDate).
			uniqueResult();
		return count!=0;
	}
		
}
