package es.udc.acarballal.elmas.model.userprofile;

import java.util.List;

import es.udc.pojo.modelutil.dao.GenericDao;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public interface UserProfileDao extends GenericDao<UserProfile, Long>{

    public List<UserProfile> findAllAdmin(int startIndex, int count);
    
    /**
     * Returns an UserProfile by login name (not user identifier)
     *
     * @param loginName the user identifier
     * @return the UserProfile
     */
    public UserProfile findByLoginName(String loginName) throws InstanceNotFoundException;
    
    public List<UserProfile> findNonAdmin(int startIndex, int count);
}
