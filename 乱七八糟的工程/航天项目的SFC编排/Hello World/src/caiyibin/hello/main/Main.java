package caiyibin.hello.main;

public class Main {

	public static void main(String[] args) {
		SCNclass.initial(); // ��ʼ��SCN���磬����LEO��MEO��GW
		Dijkstra.getAllShortestPath(); // ������нڵ�֮�����·��
		for (int i = 0; i < Result.testNum; i++) {
			Deployment.deploySFC(); // ����SFC
		}
		Result.acceptanceRatio(); // SFC����Ľ�����
		//Result.resourceUtilizationUsed();// ��ʹ�õ�ʹ����Դʹ�����
		Result.resourceUtilization(); //�ܵ�ʹ����Դʹ�����
		Result.printDelay();// ʱ�����
	}
}