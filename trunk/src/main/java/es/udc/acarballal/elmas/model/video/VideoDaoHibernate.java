package es.udc.acarballal.elmas.model.video;

import org.springframework.stereotype.Repository;
import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

@Repository
public class VideoDaoHibernate extends
		GenericDaoHibernate<Video, Long> implements VideoDao{
	
}
