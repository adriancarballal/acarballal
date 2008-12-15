package es.udc.acarballal.elmas.model.usercomment;

import java.util.List;

import es.udc.pojo.modelutil.dao.GenericDao;

public interface UserCommentDao extends GenericDao<UserComment, Long>{

	public List<UserComment> findCommentByCommentator(Long userProfileId,
			int startIndex, int count);
	
	public List<UserComment> findCommentByCommented(Long userProfileId,
			int startIndex, int count);
}
