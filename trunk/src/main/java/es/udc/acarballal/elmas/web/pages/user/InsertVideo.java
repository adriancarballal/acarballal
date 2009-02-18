package es.udc.acarballal.elmas.web.pages.user;

import java.io.File;
import java.util.Calendar;

import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextArea;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.upload.services.UploadedFile;

import es.udc.acarballal.elmas.ffmpeg.process.util.DirectoryGenerator;
import es.udc.acarballal.elmas.model.exceptions.InsufficientPrivilegesException;
import es.udc.acarballal.elmas.model.videoservice.VideoService;
import es.udc.acarballal.elmas.web.pages.Index;
import es.udc.acarballal.elmas.web.pages.errors.InstanceNotFound;
import es.udc.acarballal.elmas.web.pages.errors.InsufficientPrivileges;
import es.udc.acarballal.elmas.web.services.AuthenticationPolicy;
import es.udc.acarballal.elmas.web.services.AuthenticationPolicyType;
import es.udc.acarballal.elmas.web.util.UserSession;
import es.udc.acarballal.elmas.web.util.VideoPostProcessing;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

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
	 
	 @ApplicationState
	 private UserSession userSession;
	 
	 @Inject
	 private VideoService videoService;
	 
	 Object onSuccess(){
		 String originalDir = DirectoryGenerator.create().getAbsolutePath();
		 
		 //TODO borrar
		System.out.println("ORIGINAL: " + originalDir + "\\" + file.getFileName());
        File copied = new File(originalDir + "\\" + file.getFileName());
        file.write(copied); 

        VideoPostProcessing proc = new VideoPostProcessing(copied.getAbsolutePath(), 
        		userSession.getUserProfileId(), title, comment, videoService);
        proc.start();
        
        return Index.class;
    }
    
	 //TODO
    void onValidateForm() {
			if (!videoForm.isValid()) {
				return;
			}
	 }

}
