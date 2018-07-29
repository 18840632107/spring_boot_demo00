package nmc.bupt.edu.cn;
import java.util.*;

public class NROT {
	private int numOfSFC;
	public static ArrayList<ArrayList<Integer> > deployResAll = new ArrayList<ArrayList<Integer> >();
	private int numOfInstance=0;
	private double totalDelay=0.0;
	private double nodeRes=0.0;

	public NROT(int numOfSFC) {
		this.numOfSFC = numOfSFC;
		if (this.numOfSFC <= 0) {
			System.out.println("您的输入不正确");
		} else {
			double[] delay = new double[numOfSFC];
			for (int i = 0; i < this.numOfSFC; i++) {
				System.out.println("SFC"+i+"的编排结果为：")	;
				GeneticAlgorithm ga = new GeneticAlgorithm(100, 100, 0.9, 0.05, 1, (i%6)+1);
				//GeneticAlgorithm(args...)构造方法参数说明：种群数目，迭代次数，交叉概率，变异概率，网络类型（小1，中2，大3），SFC类型（线型1、2，分枝3、4，环型5、6）
				this.numOfInstance+=ga.getResult();
				delay[i] = ga.bestFitness;
				System.out.println("端到端时延为："+delay[i]);	
				//ga.getResult();
				//this.numOfInstance+=ga.getResult();
				this.nodeRes+=ga.nodeRes();
				this.totalDelay+=ga.bestFitness;
			}
			System.out.println("平均时延为："+	this.totalDelay/this.numOfSFC);
			System.out.println("总实例数目为："+	this.numOfInstance+"个");
			System.out.println("资源利用率为："+this.nodeRes/main.smallNetwork.nodeNum);//依然以小网络类型为例
			//写文件
			FileOperation.writeFile(NROT.deployResAll,delay,this.totalDelay/this.numOfSFC, this.numOfInstance, this.nodeRes/main.smallNetwork.nodeNum);
		}
	}
	public int getNumOfSFC() {
		return numOfSFC;
	}
	public void setNumOfSFC(int numOfSFC) {
		this.numOfSFC = numOfSFC;
	}
	public int getNumOfInstance() {
		return numOfInstance;
	}
	public void setNumOfInstance(int numOfInstance) {
		this.numOfInstance = numOfInstance;
	}
}
