package caiyibin.thread;

public class MyRunnable implements Runnable {
	private String name;
	public MyRunnable(String name){
   	 this.name=name;
    }
	 public void run(){
	for(int i=0;i<10;i++){
		 System.out.println("dangqian:"+Thread.currentThread());}
		
	}

}
