package es.udc.acarballal.elmas.model.exceptions;

public class VideoAlreadyVotedException extends Exception{
	
	private String loginName;
	private String videoTitle;
    
    public VideoAlreadyVotedException(String loginName, String videoTitle) {
        super("User " + loginName + " already voted video " + videoTitle);
        this.loginName = loginName;
        this.videoTitle = videoTitle;
    }
    
    public String getLoginName() {
        return loginName;
    }
    
    public String getVideoTitle() {
    	return videoTitle;
    }

}
