package es.udc.acarballal.elmas.model.adminservice;

import java.util.List;

import es.udc.acarballal.elmas.model.videocommentcomplaint.VideoCommentComplaint;

public class VideoCommentComplaintBlock {

	private List<VideoCommentComplaint> videoCommentComplaints;
	private boolean existMoreVideoCommentComplaints;
	
	public VideoCommentComplaintBlock(List<VideoCommentComplaint> videoCommentsComplaints, 
			boolean existMoreVideoCommentComplaints){
		this.videoCommentComplaints = videoCommentsComplaints;
		this.existMoreVideoCommentComplaints = existMoreVideoCommentComplaints;
	}

	public List<VideoCommentComplaint> getVideoCommentComplaints() {
		return videoCommentComplaints;
	}

	public boolean getExistMoreVideoCommentComplaints() {
		return existMoreVideoCommentComplaints;
	}

}
