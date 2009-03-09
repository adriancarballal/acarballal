package es.udc.acarballal.elmas.web.pages.user;

import java.io.File;

import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextArea;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.upload.services.UploadedFile;

import es.udc.acarballal.elmas.ffmpeg.process.util.DirectoryGenerator;
import es.udc.acarballal.elmas.model.userservice.UserService;
import es.udc.acarballal.elmas.model.videoservice.VideoService;
import es.udc.acarballal.elmas.web.pages.Index;
import es.udc.acarballal.elmas.web.services.AuthenticationPolicy;
import es.udc.acarballal.elmas.web.services.AuthenticationPolicyType;
import es.udc.acarballal.elmas.web.util.UserSession;
import es.udc.acarballal.elmas.web.util.VideoMimeDetection;
import es.udc.acarballal.elmas.web.util.VideoPostProcessing;

@AuthenticationPolicy(AuthenticationPolicyType.PARTICIPANTS)
public class InsertVideo {

	 @SuppressWarnings("unused")
	 @Property
	 private String comment;

	 @SuppressWarnings("unused")
	 @Component(id = "comment")
	 private TextArea commentField;
	
	 @Property
	 private UploadedFile file;
	 
	 @SuppressWarnings("unused")
	 @Property
	 private String title;
	 
	 @SuppressWarnings("unused")
	 @Component(id = "title")
	 private TextArea titleField;

	 @Component
	 private Form videoForm;
	 
	 @Inject
	 private Messages messages;
	 
	 @ApplicationState
	 private UserSession userSession;
	 
	 @Inject
	 private VideoService videoService;
	 
	 @Inject
	 private UserService userService;
	 
	 Object onSuccess(){
		 String originalDir = DirectoryGenerator.create().getAbsolutePath();
		 
        File copied = new File(originalDir + "\\" + file.getFileName());
        file.write(copied); 

        VideoPostProcessing proc = new VideoPostProcessing(copied.getAbsolutePath(), 
        		userSession.getUserProfileId(), title, comment, videoService, userService);
        proc.start();
        
        return Index.class;
    }
    
	 //TODO
    void onValidateForm() {
			if (!videoForm.isValid()) {
				return;
			}
			if (!VideoMimeDetection.isValidVideoFile(file.getFilePath())){
				videoForm.recordError(messages.get("noValidVideo"));
			}
	 }

}
