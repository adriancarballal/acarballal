package es.udc.acarballal.elmas.model.message;

import java.util.List;

import es.udc.pojo.modelutil.dao.GenericDao;

public interface MessageDao extends GenericDao<Message, Long>{
	
	public List<Message> getInBox(Long userProfileId, int startIndex, int count);
	
	public int inBoxTotal(Long userProfileId);

}
