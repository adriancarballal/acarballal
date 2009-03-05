package es.udc.acarballal.elmas.web.util;

public class PageSession {
	
	private Class page;
	
	public PageSession(Class page){
		this.page=page;
	}
	
	public Class getPage(){
		return page;
	}
	
	@Override
	public String toString(){
		return page.toString();
	}

}
