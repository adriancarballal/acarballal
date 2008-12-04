package es.udc.acarballal.elmas.model.vote;

import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

@Repository
public class VoteDaoHibernate extends
		GenericDaoHibernate<Vote, Long> implements VoteDao{


}
