package es.udc.acarballal.elmas.model.videocomment;

import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

@Repository
public class VideoCommentDaoHibernate  extends
GenericDaoHibernate<VideoComment, Long> implements VideoCommentDao{

}
