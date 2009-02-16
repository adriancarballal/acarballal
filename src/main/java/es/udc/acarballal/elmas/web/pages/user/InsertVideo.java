package es.udc.acarballal.elmas.web.pages.user;

import java.io.File;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextArea;
import org.apache.tapestry5.upload.services.UploadedFile;

import es.udc.acarballal.elmas.ffmpeg.VideoPostProcessing;
import es.udc.acarballal.elmas.web.pages.Index;
import es.udc.acarballal.elmas.web.services.AuthenticationPolicy;
import es.udc.acarballal.elmas.web.services.AuthenticationPolicyType;

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
	 
	 @Persist(PersistenceConstants.FLASH)
	 @SuppressWarnings("unused")
	 @Property
	 private String message;
	 
	 @SuppressWarnings("unused")
	 @Property
	 private String title;
	 
	 @SuppressWarnings("unused")
	 @Component(id = "title")
	 private TextArea titleField;

	 @Component
	 private Form videoForm;
	 
	 Object onSuccess(){
   	 
        File copied = new File("D:/temp/" + file.getFileName());
        file.write(copied); 
        VideoPostProcessing proc = new VideoPostProcessing("D:/temp/" + file.getFileName());
        proc.start();
        
        return Index.class;
    }
    
    Object onUploadException(FileUploadException ex)
    {
        message = "Upload exception: " + ex.getMessage();
        return this;
    }


    void onValidateForm() {

			if (!videoForm.isValid()) {
				return;
			}
	 }

}
