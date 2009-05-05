package es.udc.acarballal.elmas.model.adminservice.thread;

public class CurrentThread extends Thread{
	
	int id;
	private ThreadList parentList;
	
	public CurrentThread(int i, ThreadList parentList){
		this.id = i;
		this.parentList = parentList;
	}
	
	public CurrentThread(int i){
		this.id = i;
	}
	
	public void setParentList(ThreadList tl){
		this.parentList = tl;
	}

	public void run(){
		System.out.println("iniciando thread id: " + id);
		try {	Thread.sleep(3000);		} 
		catch (InterruptedException e) {	e.printStackTrace();	}
		System.out.println("finalizado thread id: " + id);
		parentList.setOffUse();
		if(!parentList.isEmpty()){
			parentList.run();
		}
	}
	
	public int getIdNumber(){
		return id;
	}
}
