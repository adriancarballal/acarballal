package es.udc.acarballal.elmas.model.userservice;

import java.util.List;

import es.udc.acarballal.elmas.model.usercomment.UserComment;

public class UserCommentBlock {

	private boolean existMoreUserComments;
	private List<UserComment> userComments;
	
	public UserCommentBlock(List<UserComment> userComments, boolean existMoreUserComments){
		this.userComments = userComments;
		this.existMoreUserComments = existMoreUserComments;
	}

	public boolean getExistMoreUserComments() {
		return existMoreUserComments;
	}

	public List<UserComment> getUserComments() {
		return userComments;
	}
	
}
