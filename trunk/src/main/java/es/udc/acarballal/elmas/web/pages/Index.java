package es.udc.acarballal.elmas.web.pages;

import org.apache.tapestry5.annotations.ApplicationState;

import es.udc.acarballal.elmas.web.util.PageSession;

public class Index
{
	@SuppressWarnings("unused")
	@ApplicationState
	private PageSession nextPage;
	
	void onActivate() {
		nextPage = null;
	}
}