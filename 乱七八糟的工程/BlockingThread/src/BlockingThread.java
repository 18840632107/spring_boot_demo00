import java.util.concurrent.ArrayBlockingQueue;
import java.util.Random;
                                               //�����������У�������Ŀ�



public class BlockingThread {
    private int queueSize = 1000, alivenum = 0;                                                  //�������е����洢��Ϊ1000����alivenum��¼��ǰ�״̬���߳���������ֵΪ0
    private ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(queueSize);
     
    public static void main(String[] args)  {
        BlockingThread mainthread = new BlockingThread();
		
        Producer producer1 = mainthread.new Producer();
		Producer producer2 = mainthread.new Producer();
		
		Consumer consumer1 = mainthread.new Consumer();
		Consumer consumer2 = mainthread.new Consumer();
		Consumer consumer3 = mainthread.new Consumer();
		Consumer consumer4 = mainthread.new Consumer();
        Consumer consumer5 = mainthread.new Consumer();
		
		
         
        producer1.start();
        
        try {
			Thread.sleep(2000);
        } catch (InterruptedException e) {
			e.printStackTrace(); 
        }                                                      //ʹ�����������̲߳���ͬʱ��ʼ���Ӷ���֤���߸���ʱ���õ���������Ӳ�ͬ
        
		producer2.start();
		
        consumer1.start();
		consumer2.start();
		consumer3.start();
		consumer4.start();
		consumer5.start();
		
		while(true)
		{
			if(consumer1.isAlive() == true)
				mainthread.alivenum++;
			if(consumer2.isAlive() == true)
				mainthread.alivenum++;
			if(consumer3.isAlive() == true)
				mainthread.alivenum++;
			if(consumer4.isAlive() == true)
				mainthread.alivenum++;
			if(consumer5.isAlive() == true)
				mainthread.alivenum++;                          //ͳ�ƻ״̬���������߳�����
			
			if(mainthread.alivenum == 0)
				System.exit(0);
			else
				System.out.println("�������̻߳״̬������Ϊ"+mainthread.alivenum+"��");        //���״̬���������߳�����Ϊ0����ֹ���̣߳���Ϊ0�����������
			
			mainthread.alivenum = 0;                                                    //��alivenum����Ϊ0
			
			try {
				Thread.sleep(3000);                                                     //���߳�˯��3s
            } catch (InterruptedException e) {
				e.printStackTrace(); 
            }
			
		}
		
    }
     
    class Consumer extends Thread{
       
    	
       
        public void run() {
            consume();
        }
        
        
        private void consume() {
			
        	int j = 1;           //��j�洢����������ͷȡ����Ԫ��
				
            while(j%100 != 0){   //��ȡ����j�ɱ�100����ʱ������ѭ������Ӧ��consumer�߳̽����״̬
                try {

                    j = queue.take();
                    System.out.println("�Ӷ���ȡ��һ��Ԫ�أ�Ϊ��"+j);
                	
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
     
    class Producer extends Thread{
         
        public void run() {
            produce();
        }
         
        private void produce() {
        	long time = System.currentTimeMillis();         //��õ�ǰϵͳ��ʱ��ֵ
			Random random1 = new Random(time);
			Random random2 = new Random(time+1);            //�����������������ʱ��ֵ��ʱ��ֵ+1��Ϊ���������
			
			
            while(true){
                try {
					int s = random1.nextInt(4) + 1;         //s��1��5֮��
					int t = random2.nextInt(999) + 1;       //t��1��1000֮��
					sleep(s * 1000);   
					
                    queue.put(t);
                    System.out.println("������в���һ��Ԫ��:"+t);
					
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}