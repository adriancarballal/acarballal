package es.udc.acarballal.elmas.model.videocommentcomplaint;

import java.util.List;

import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

@Repository
public class VideoCommentComplaintDaoHibernate extends
		GenericDaoHibernate<VideoCommentComplaint, Long> implements VideoCommentComplaintDao {
	
	public int countVideoCommentComplaints(){
		long count = 
			(Long) getSession().createQuery("SELECT count(c) FROM VideoCommentComplaint c ").
			uniqueResult(); 
		return (int)count;
	}
	
	public List<VideoCommentComplaint> findVideoCommentComplaints(int startIndex, int count){
		return getSession().createQuery("SELECT c FROM VideoCommentComplaint c ").
		setFirstResult(startIndex).setMaxResults(count).list();
	}

}