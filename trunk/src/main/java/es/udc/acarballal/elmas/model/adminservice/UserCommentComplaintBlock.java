package es.udc.acarballal.elmas.model.adminservice;

import java.util.List;

import es.udc.acarballal.elmas.model.usercommentcomplaint.UserCommentComplaint;

public class UserCommentComplaintBlock {
	
	private boolean existMoreUserCommentComplaints;
	private List<UserCommentComplaint> userCommentComplaints;
	
	public UserCommentComplaintBlock(List<UserCommentComplaint> userCommentsComplaints, 
			boolean existMoreUserCommentComplaints){
		this.userCommentComplaints = userCommentsComplaints;
		this.existMoreUserCommentComplaints = existMoreUserCommentComplaints;
	}

	public boolean getExistMoreUserCommentComplaints() {
		return existMoreUserCommentComplaints;
	}

	public List<UserCommentComplaint> getUserCommentComplaints() {
		return userCommentComplaints;
	}

	
}
