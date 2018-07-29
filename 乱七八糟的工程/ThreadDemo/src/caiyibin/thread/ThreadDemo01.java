package caiyibin.thread;

public class ThreadDemo01 {

	public static void main(String[] args) {
//		MyThread t1 =new MyThread("A");
//		MyThread t2 =new MyThread("B");
//		t1.run();
//		t2.run();
		//线程启功是通过start
//		t1.start();
//		t2.start();
		MyRunnable R1 = new MyRunnable("A");
//		MyRunnable R2 = new MyRunnable("B");
		Thread t1=new Thread(R1);
//		Thread t2=new Thread(R2);
		t1.start();
//    	t2.start();
		
	}
}
