package caiyibin.hello.main;

public class Main {

	public static void main(String[] args) {
		SCNclass.initial(); // 初始化SCN网络，包括LEO、MEO与GW
		Dijkstra.getAllShortestPath(); // 获得所有节点之间最短路径
		for (int i = 0; i < Result.testNum; i++) {
			Deployment.deploySFC(); // 部署SFC
		}
		Result.acceptanceRatio(); // SFC请求的接受率
		//Result.resourceUtilizationUsed();// 已使用的使用资源使用情况
		Result.resourceUtilization(); //总的使用资源使用情况
		Result.printDelay();// 时延情况
	}
}