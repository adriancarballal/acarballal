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

import es.udc.acarballal.elmas.web.pages.Index;

public class InsertVideo {

	@Property
	 private String title;

	 @Property
	 private String comment;
	
	 @Property
    private UploadedFile file;
	 
	 @Component
	 private Form videoForm;
	 
	 @Component(id = "title")
	 private TextArea titleField;
	 
	 @Component(id = "comment")
	 private TextArea commentField;

	 void onValidateForm() {

			if (!videoForm.isValid()) {
				return;
			}
	 }
	 
    Object onSuccess(){
   	 
        File copied = new File("D:/temp/" + file.getFileName());
        file.write(copied);         
        return Index.class;
    }
    
    @Persist(PersistenceConstants.FLASH)
    @Property
    private String message;


    Object onUploadException(FileUploadException ex)
    {
        message = "Upload exception: " + ex.getMessage();

        return this;
    }

}
