package es.udc.acarballal.elmas.model.usercomment;

import org.springframework.stereotype.Repository;
import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

@Repository
public class UserCommentDaoHibernate extends
	GenericDaoHibernate<UserComment, Long> implements UserCommentDao{

}
