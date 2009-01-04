package es.udc.acarballal.elmas.web.components;

import java.io.File;

import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextArea;
import org.apache.tapestry5.corelib.components.TextField;

import es.udc.acarballal.elmas.web.pages.FindVideos;
import es.udc.acarballal.elmas.web.pages.Index;
import es.udc.acarballal.elmas.web.util.UserSession;

public class Layout {
	
	@SuppressWarnings("unused")
	@Property
	@Parameter(required = false, defaultPrefix = "literal")
	private String menuExplanation;
	
	@SuppressWarnings("unused")
	@Property
	@Parameter(required = true, defaultPrefix = "literal")
	private String pageTitle;

	@SuppressWarnings("unused")
	@Property
	@ApplicationState
	private UserSession userSession;
	
	@SuppressWarnings("unused")
	@Property
	private boolean userSessionExists;
	
	@SuppressWarnings("unused")
	@Component
	private Form findVideosForm;
	
	@SuppressWarnings("unused")
	@Property
	private String keys;
	
	@SuppressWarnings("unused")
	@Component(id = "keys")
	private TextField keysField;
	
	@InjectPage
	private FindVideos findVideos;
	
	Object onSuccess(){
                 
		findVideos.setKeys(keys);
        return findVideos;
    }
	
}