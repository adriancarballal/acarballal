package es.udc.acarballal.elmas.model.userprofile;

import java.util.List;

import org.springframework.stereotype.Repository;

import es.udc.acarballal.elmas.model.userprofile.UserProfile.Privileges_TYPES;
import es.udc.pojo.modelutil.dao.GenericDaoHibernate;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@Repository
public class UserProfileDaoHibernate extends
		GenericDaoHibernate<UserProfile, Long> implements UserProfileDao {

	public UserProfile findByLoginName(String loginName) throws InstanceNotFoundException {
		UserProfile userProfile = (UserProfile) getSession().createQuery(
				"SELECT u FROM UserProfile u WHERE u.loginName = :loginName")
				.setParameter("loginName", loginName)
				.uniqueResult();
		if (userProfile == null) {
			throw new InstanceNotFoundException(loginName, UserProfile.class.getName());
		} else {
			return userProfile;
		}
	}
	
	public List<UserProfile> findAllAdmin(int startIndex, int count){
		return getSession().createQuery("SELECT u FROM UserProfile u " +
				"WHERE u.privileges=:privileges").
				setParameter("privileges", Privileges_TYPES.ADMIN).		
				setFirstResult(startIndex).setMaxResults(count).list();
	}
	
	public List<UserProfile> findNonAdmin(int startIndex, int count){
		return getSession().createQuery("SELECT u FROM UserProfile u " +
				"WHERE u.privileges=:privileges").
				setParameter("privileges", Privileges_TYPES.VOTER).		
				setFirstResult(startIndex).setMaxResults(count).list();
	}

}