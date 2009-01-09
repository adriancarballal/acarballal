package es.udc.acarballal.elmas.model.videocomplaint;

import java.util.List;

import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

@Repository
public class VideoComplaintDaoHibernate extends
		GenericDaoHibernate<VideoComplaint, Long> implements VideoComplaintDao {
	
	public int countVideoComplaints(){
		int count = 
			(Integer) getSession().createQuery("SELECT count(*) FROM VideoComplaint c ").
			uniqueResult(); 
		return count;
	}
	
	public List<VideoComplaint> findVideoComplaints(int startIndex, int count){
		return getSession().createQuery("SELECT c FROM VideoComplaint c ").
		setFirstResult(startIndex).setMaxResults(count).list();
	}

}
