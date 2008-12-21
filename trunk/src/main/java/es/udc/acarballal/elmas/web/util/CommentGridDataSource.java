package es.udc.acarballal.elmas.web.util;

import java.util.List;

import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;

import es.udc.acarballal.elmas.model.usercomment.UserComment;
import es.udc.acarballal.elmas.model.userservice.UserService;

public class CommentGridDataSource implements GridDataSource{

	private UserService userService;
	private Long userId;
	private List<UserComment> userComments;
	private int totalNumberOfComments;
	private int startIndex;
	private boolean commentsNotFound;
	
	public CommentGridDataSource(UserService userService, Long userId) {
		
		this.userService = userService;
		this.userId = userId;
	
		totalNumberOfComments = 
			userService.getNumberOfUserCommentsByCommented(userId);
		
		if (totalNumberOfComments == 0) commentsNotFound = true;
		
	}
	
    public int getAvailableRows() {
        return totalNumberOfComments;
    }

    public Class<UserComment> getRowType() {
        return UserComment.class;
    }

    public Object getRowValue(int index) {
        return userComments.get(index-this.startIndex);
    }

    public void prepare(int startIndex, int endIndex,
    	List<SortConstraint> sortConstraints) {
    	
       	userComments = userService.findUserCommentsByCommented(userId, startIndex, 
       				endIndex-startIndex+1).getUserComments();
        this.startIndex = startIndex;
		
    }
    
    public boolean getCommentsNotFound() {
    	return commentsNotFound;
    }
}
