package es.udc.acarballal.elmas.web.pages.user;

import java.io.File;
import java.util.Calendar;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextArea;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.upload.services.UploadedFile;

import es.udc.acarballal.elmas.ffmpeg.process.util.DirectoryGenerator;
import es.udc.acarballal.elmas.model.adminservice.AdminService;
import es.udc.acarballal.elmas.model.adminservice.util.ConfirmationMessage;
import es.udc.acarballal.elmas.model.exceptions.IncorrectPasswordException;
import es.udc.acarballal.elmas.model.exceptions.InsufficientPrivilegesException;
import es.udc.acarballal.elmas.model.userservice.UserService;
import es.udc.acarballal.elmas.model.videoservice.VideoService;
import es.udc.acarballal.elmas.web.pages.Index;
import es.udc.acarballal.elmas.web.pages.errors.InstanceNotFound;
import es.udc.acarballal.elmas.web.pages.errors.InsufficientPrivileges;
import es.udc.acarballal.elmas.web.services.AuthenticationPolicy;
import es.udc.acarballal.elmas.web.services.AuthenticationPolicyType;
import es.udc.acarballal.elmas.web.util.UserSession;
import es.udc.acarballal.elmas.web.util.VideoMimeDetection;
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
	 
	 @Inject
	 private Messages messages;
	 
	 @ApplicationState
	 private UserSession userSession;
	 
	 @Inject 
	 private AdminService adminService;
	 
	 @Inject 
	 private VideoService videoService;
	 
	 @Inject
	 private UserService userService;
	 
	 public boolean getAddedVideo(){
		 try {
			return videoService.addedVideo(userSession.getUserProfileId(), Calendar.getInstance());
		} catch (InstanceNotFoundException e) {
			return true;
		}
	 }
	 
	 Object onSuccess(){
		 String originalDir = DirectoryGenerator.create().getAbsolutePath();
		 
        File copied = new File(originalDir + "\\" + file.getFileName());
        file.write(copied); 

        try {
        	adminService.encodeVideo(copied.getAbsolutePath(), 
					userSession.getUserProfileId(), title, comment, userService, videoService);
		} catch (InstanceNotFoundException e) {
			return InstanceNotFound.class;
		} catch (IncorrectPasswordException e) {
			// NOT REACHED
		} catch (InsufficientPrivilegesException e) {
			return InsufficientPrivileges.class;
		}
        
        return Index.class;
    }
    
	@OnEvent(value="validate", component="videoForm")
    void onValidateForm(){
			if (!videoForm.isValid()) {
				return;
			}
			if (!VideoMimeDetection.isValidVideoFile(file.getFilePath())){
				videoForm.recordError(messages.get("noValidVideo"));
			}
	 }
    
    @SuppressWarnings("deprecation")
	void onUploadException(FileUploadException ex){
    		new ConfirmationMessage(userService, userSession.getUserProfileId(), 
    				messages.get("uploadError")).execute();
    }

}
