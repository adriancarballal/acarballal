package es.udc.acarballal.elmas.model.adminservice;

import java.util.List;

import es.udc.acarballal.elmas.model.usercommentcomplaint.UserCommentComplaint;

public class UserCommentComplaintBlock {
	
	private List<UserCommentComplaint> userCommentComplaints;
	private boolean existMoreUserCommentComplaints;
	
	public UserCommentComplaintBlock(List<UserCommentComplaint> userCommentsComplaints, 
			boolean existMoreUserCommentComplaints){
		this.userCommentComplaints = userCommentsComplaints;
		this.existMoreUserCommentComplaints = existMoreUserCommentComplaints;
	}

	public List<UserCommentComplaint> getUserCommentComplaints() {
		return userCommentComplaints;
	}

	public boolean getExistMoreUserCommentComplaints() {
		return existMoreUserCommentComplaints;
	}

	
}
