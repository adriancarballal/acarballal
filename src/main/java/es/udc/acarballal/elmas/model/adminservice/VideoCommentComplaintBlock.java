package es.udc.acarballal.elmas.model.adminservice;

import java.util.List;

import es.udc.acarballal.elmas.model.videocommentcomplaint.VideoCommentComplaint;

public class VideoCommentComplaintBlock {

	private boolean existMoreVideoCommentComplaints;
	private List<VideoCommentComplaint> videoCommentComplaints;
	
	public VideoCommentComplaintBlock(List<VideoCommentComplaint> videoCommentsComplaints, 
			boolean existMoreVideoCommentComplaints){
		this.videoCommentComplaints = videoCommentsComplaints;
		this.existMoreVideoCommentComplaints = existMoreVideoCommentComplaints;
	}

	public boolean getExistMoreVideoCommentComplaints() {
		return existMoreVideoCommentComplaints;
	}

	public List<VideoCommentComplaint> getVideoCommentComplaints() {
		return videoCommentComplaints;
	}

}
