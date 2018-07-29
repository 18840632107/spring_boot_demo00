import java.util.concurrent.ArrayBlockingQueue;
import java.util.Random;
                                               //引入阻塞队列，随机数的库



public class BlockingThread {
    private int queueSize = 1000, alivenum = 0;                                                  //阻塞队列的最大存储量为1000，用alivenum记录当前活动状态的线程数量，初值为0
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
        }                                                      //使两个生产者线程不会同时开始，从而保证两者根据时间获得的随机数种子不同
        
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
				mainthread.alivenum++;                          //统计活动状态的消费者线程数量
			
			if(mainthread.alivenum == 0)
				System.exit(0);
			else
				System.out.println("消费者线程活动状态的数量为"+mainthread.alivenum+"个");        //若活动状态的消费者线程数量为0则终止主线程，不为0则输出其数量
			
			mainthread.alivenum = 0;                                                    //将alivenum重置为0
			
			try {
				Thread.sleep(3000);                                                     //主线程睡眠3s
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
			
        	int j = 1;           //用j存储从阻塞队列头取出的元素
				
            while(j%100 != 0){   //当取出的j可被100整除时，跳出循环，相应的consumer线程结束活动状态
                try {

                    j = queue.take();
                    System.out.println("从队列取走一个元素，为："+j);
                	
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
        	long time = System.currentTimeMillis();         //获得当前系统的时间值
			Random random1 = new Random(time);
			Random random2 = new Random(time+1);            //设置两个随机数，以时间值和时间值+1作为随机数种子
			
			
            while(true){
                try {
					int s = random1.nextInt(4) + 1;         //s在1到5之间
					int t = random2.nextInt(999) + 1;       //t在1到1000之间
					sleep(s * 1000);   
					
                    queue.put(t);
                    System.out.println("向队列中插入一个元素:"+t);
					
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}