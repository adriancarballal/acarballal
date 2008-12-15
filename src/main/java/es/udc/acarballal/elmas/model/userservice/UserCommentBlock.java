package es.udc.acarballal.elmas.model.userservice;

import java.util.List;

import es.udc.acarballal.elmas.model.usercomment.UserComment;

public class UserCommentBlock {

	private List<UserComment> userComments;
	private boolean existMoreUserComments;
	
	public UserCommentBlock(List<UserComment> userComments, boolean existMoreUserComments){
		this.userComments = userComments;
		this.existMoreUserComments = existMoreUserComments;
	}

	public List<UserComment> getUserComments() {
		return userComments;
	}

	public boolean getExistMoreUserComments() {
		return existMoreUserComments;
	}
	
}
