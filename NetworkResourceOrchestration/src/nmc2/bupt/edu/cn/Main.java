package nmc2.bupt.edu.cn;

import java.util.ArrayList;
import java.util.Random;

public class Main {
	public static void main(String[] args) {
		/*
		// 主函数使用RECM类，提供了两个demo：
		// 1. 输入只有SFC的数量，对所有的SFC设定相同的所需时延
		new RECM(RECM.numOfSfc).Scaling(new Random().nextInt(350) * 1.0);
		// 2. 输入为SFC的编号与数量，对单个SFC设定所需时延
		new RECM(RECM.numOfSfc).Scaling(1, new Random().nextInt(350) * 1.0);
		// 这里测试的时候SFC的所需时延是随机生成的，可以自己根据实际情况设定。当时延设置到很大（比如大于等于1100之类），算法会认为服务被撤销
		*/
		//对于单个SFC完整的（随机）编排与扩缩容操作过程
		DataCenterNetwork dataCenterNetwork = new DataCenterNetwork(4);
	    SFCrequest sfCrequest = new SFCrequest(0, 3);
	    SFCUtil.orchestrationInit(dataCenterNetwork, sfCrequest);
	    SFCUtil.Scaling(dataCenterNetwork, sfCrequest, 50);    //可不断为该SFC传递时延信号，使之进行扩缩容操作
	    ArrayList<SFCrequest>sfcList = new ArrayList<>();      //写文件需要传入SFC链表，所以建议就算只有一个SFC也建一个链表
	    sfcList.add(sfCrequest);
	    FileOperation.writeFile(dataCenterNetwork, sfcList);
	}
	//这里留一些有用的查询语句
	public void someUsefulInfo() {
		/* 随机部署
		DataCenterNetwork dataCenterNetwork = new DataCenterNetwork(4);
		for (int i =0;i<SFCNUM;i++) {
			SFCrequest sfCrequest = new SFCrequest(i, 4);
			SFCUtil.orchestrationInit(dataCenterNetwork, sfCrequest);
			SFCUtil.getOrchestrationResult(dataCenterNetwork, sfCrequest);
		}
		//随机初始化部署
		SFCrequest sfCrequest = new SFCrequest(1, 2);
		DynamicOptimization dynamicOptimization = new DynamicOptimization();
		DataCenterNetwork dataCenterNetwork = new DataCenterNetwork(2);
		dynamicOptimization.orchestrationInit(dataCenterNetwork, sfCrequest);
		//获取SFC信息
		SFCrequest sfCrequest = new SFCrequest(1, 2);
		for (int i = 0; i < sfCrequest.getNumOfVNF(); i++) {
			System.out.println(sfCrequest.getVnfFG().get(i).getTypeId());
		}
		//获取服务器信息
		DataCenterNetwork dataCenterNetwork = new DataCenterNetwork(2);
		//获取服务器信息
		for (int j = 0; j < 16; j++) {
			for (int i = 0; i < 4; i++) {
				System.out.println(dataCenterNetwork.getServerList().get(i).getCpuCoreRes(i));
				System.out.println(dataCenterNetwork.getServerList().get(i).getVnfTypeInCore(i));
			}
		}
		//获取链路信息
		for (int i = 0; i < dataCenterNetwork.getAllNodes(); i++) {
			for (int j = 0; j < dataCenterNetwork.getAllNodes(); j++) {
				System.out.print(dataCenterNetwork.getLinksBandWidth()[i][j]+"              ");
			}
			System.out.println();
		}
		//查看部署情况
		for (int i = 0; i < sfCrequest.getNumOfVNF(); i++) {
			System.out.println("SFC中"+"vnf "+i +"的类型为："+sfCrequest.getVnfFG().get(i).getTypeId());
			System.out.println("部署在服务器"+sfCrequest.getVnfFG().get(i).getServerId() + "上");
		}
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 4; j++) {
				System.out.println("服务器"+i+"上核心"+j+"部署的SFC编号为："+dataCenterNetwork.getServerList().get(i).getSfcIdInCore(j));
				System.out.println("服务器"+i+"上核心"+j+"部署的VNF类型为："+dataCenterNetwork.getServerList().get(i).getVnfTypeInCore(j));
				System.out.println("服务器"+i+"上核心"+j+"资源为："+dataCenterNetwork.getServerList().get(i).getCpuCoreRes(j));
			}
		}
		 */
	}
}