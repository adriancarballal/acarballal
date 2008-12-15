package es.udc.acarballal.elmas.model.videocomment;

import java.util.List;

import es.udc.pojo.modelutil.dao.GenericDao;

public interface VideoCommentDao extends GenericDao<VideoComment, Long>{

	public List<VideoComment> findVideoCommentsByVideoId(Long videoId, 
			int startIndex, int count);
}
