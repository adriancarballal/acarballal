package es.udc.acarballal.elmas.model.message;

import java.util.List;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

public class MessageDaoHibernate extends GenericDaoHibernate<Message, Long> implements MessageDao{

	public List<Message> getInBox(Long userProfileId, int startIndex, int count) {
		return getSession().createQuery("SELECT v FROM Message v " +
		"WHERE v.receiver.userProfileId=:userId").
		setParameter("userId", userProfileId).		
		setFirstResult(startIndex).setMaxResults(count).list();	
	}	
	
	public int inBoxTotal(Long userProfileId){
		long count = 
			(Long) getSession().createQuery("SELECT count(c) FROM Message c " +
					"WHERE v.receiver.userProfileId=:userId").
			uniqueResult(); 
		return (int)count;
	}
}
