package es.udc.acarballal.elmas.model.videoservice;

import java.util.List;

import es.udc.acarballal.elmas.model.videocomment.VideoComment;

public class VideoCommentBlock {
	private List<VideoComment> videoComments;
	private boolean existMoreVideoComments;
	
	public VideoCommentBlock(List<VideoComment> videoComments, boolean existMoreVideoComments){
		this.videoComments = videoComments;
		this.existMoreVideoComments = existMoreVideoComments;
	}

	public List<VideoComment> getUserComments() {
		return videoComments;
	}

	public boolean getExistMoreUserComments() {
		return existMoreVideoComments;
	}
}
