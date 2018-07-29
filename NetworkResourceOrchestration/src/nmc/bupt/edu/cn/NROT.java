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
			System.out.println("�������벻��ȷ");
		} else {
			double[] delay = new double[numOfSFC];
			for (int i = 0; i < this.numOfSFC; i++) {
				System.out.println("SFC"+i+"�ı��Ž��Ϊ��")	;
				GeneticAlgorithm ga = new GeneticAlgorithm(100, 100, 0.9, 0.05, 1, (i%6)+1);
				//GeneticAlgorithm(args...)���췽������˵������Ⱥ��Ŀ������������������ʣ�������ʣ��������ͣ�С1����2����3����SFC���ͣ�����1��2����֦3��4������5��6��
				this.numOfInstance+=ga.getResult();
				delay[i] = ga.bestFitness;
				System.out.println("�˵���ʱ��Ϊ��"+delay[i]);	
				//ga.getResult();
				//this.numOfInstance+=ga.getResult();
				this.nodeRes+=ga.nodeRes();
				this.totalDelay+=ga.bestFitness;
			}
			System.out.println("ƽ��ʱ��Ϊ��"+	this.totalDelay/this.numOfSFC);
			System.out.println("��ʵ����ĿΪ��"+	this.numOfInstance+"��");
			System.out.println("��Դ������Ϊ��"+this.nodeRes/main.smallNetwork.nodeNum);//��Ȼ��С��������Ϊ��
			//д�ļ�
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
