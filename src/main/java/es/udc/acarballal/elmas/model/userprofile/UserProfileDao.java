package es.udc.acarballal.elmas.model.userprofile;

import es.udc.pojo.modelutil.dao.GenericDao;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public interface UserProfileDao extends GenericDao<UserProfile, Long>{

    /**
     * 
     * @param loginName
     * @return UserProfile
     * @throws InstanceNotFoundException
     */
    public UserProfile findByLoginName(String loginName) throws InstanceNotFoundException;
    
}
