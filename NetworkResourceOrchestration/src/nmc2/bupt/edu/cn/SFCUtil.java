package nmc2.bupt.edu.cn;

import java.util.ArrayList;
import java.util.Random;

public class SFCUtil {

	// 因为要专注扩缩容操作，只实现简单的编排初始化：随机选择服务器放置VNF，不考虑起始目的节点，同一SFC的VNF不放在一个服务器
	public static void orchestrationInit(DataCenterNetwork DCN, SFCrequest SFC) {
		int serverNum = DCN.getServerList().size();
		ArrayList<Integer> serverCandidate = new ArrayList<Integer>();
		for (int i = 0; i < serverNum; i++) {
			serverCandidate.add(i);
		}
		int sfcId = SFC.getSfcId();
		int vnfNum = SFC.getNumOfVNF();
		for (int i = 0; i < vnfNum; i++) {
			boolean vnfDeploySuccess = false;
			while (serverCandidate.size() > 0) {
				Random r = new Random(System.currentTimeMillis() + new Random().nextInt(1000));
				int serverId = r.nextInt(serverCandidate.size()); // 这里获得的是index
				// 遍历虚拟机，选择服务器上未部署SFC（VNF）的虚拟机
				for (int j = 0; j < DCN.getServerList().get(serverId).getCoreNum(); j++) {
					if (DCN.getServerList().get(serverId).getSfcIdInCore(j) == -1
							&& DCN.getServerList().get(serverId).getVnfTypeInCore(j) == -1
							&& DCN.getServerList().get(serverId).getSfcIdInCore(j) != sfcId) {// 部署VNF成功
						DCN.getServerList().get(serverId).setSfcIdInCore(j, sfcId);
						DCN.getServerList().get(serverId).setVnfTypeInCore(j, SFC.getVnfFG().get(i).getTypeId());
						double initResInCore = DCN.getServerList().get(serverId).getCpuCoreRes(j);
						double initResOfVNF = SFC.getVnfFG().get(i).getReqResource();
						// 改一下资源占用
						DCN.getServerList().get(serverId).setCpuCoreRes(j, initResInCore - initResOfVNF);
						SFC.getVnfFG().get(i).setServerId(DCN.getServerList().get(serverId).getServerId());
						vnfDeploySuccess = true;
						break;
					}
				}
				serverCandidate.remove(serverId); // 这里并不是移除那个值，而是这个下标的数
				if (vnfDeploySuccess == true) {
					break;
				}
				if (serverCandidate.size() == 0 && vnfDeploySuccess == false) {
					// System.out.println("Faliure");部署失败
					throw new NullPointerException();
				}
			}
		}
		// return 部署成功
		System.out.println("Success");
	}

	// 确定VNF计算资源分配量与处理时延之间的关系，计算VNF的处理时延（反正比例）
	public static double vnfProcessDelay(double realRes) {
		double minRes = 20.0;
		double maxDelay = 30.0;
		double maxRes = 80.0;
		double minDelay = 10.0;
		double k = (maxDelay - minDelay) / (minRes - maxRes); // 斜率
		double b = ((minDelay * minRes) - (maxDelay * maxRes)) / (minRes - maxRes); // 截距

		if (realRes > 80.0 && realRes <= 100)
			return minDelay;
		else if (realRes < 20.0 && realRes > 0)
			return maxDelay; // 但是不建议取值低于20.0
		else if (realRes > 100 || realRes <= 0)
			throw new IllegalArgumentException();
		else
			return k * realRes + b;
	}

	// 计算SFC请求的端到端时延
	public static double sfcEndToEndDelay(SFCrequest SFC) {
		int sfcLen = SFC.getNumOfVNF();
		double realEndToEndDelay = 0.0;
		for (int i = 0; i < sfcLen; i++) {
			if (SFC.getVnfFG().get(i).getCopyVNF() == null) {
				realEndToEndDelay += vnfProcessDelay(SFC.getVnfFG().get(i).getRealResource());
			} else {
				realEndToEndDelay += vnfProcessDelay(SFC.getVnfFG().get(i).getRealResource()) / 2;
			}
		}
		return realEndToEndDelay;
	}

	// 数据中心网络的资源现状
	public static void getResOfDCN(DataCenterNetwork DCN) {
		System.out.println("数据中心的服务器资源现状为：");
		for (int i = 0; i < DCN.getServerList().size(); i++) {
			for (int j = 0; j < DCN.getServerList().get(i).getCoreNum(); j++) {
				System.out
						.println("服务器 " + i + "第" + j + "个虚拟机的资源剩余量为： " + DCN.getServerList().get(i).getCpuCoreRes(j));
			}
		}
	}

	// 打印SFC初始编排结果
	public static void getOrchestrationResult(DataCenterNetwork DCN, SFCrequest SFC) {
		System.out.println("SFC " + SFC.getSfcId() + " 的初始编排结果为：");
		for (int i = 0; i < SFC.getNumOfVNF(); i++) {
			System.out.println("VNF " + i + "部署在服务器 " + SFC.getVnfFG().get(i).getServerId() + " 的虚拟机上。");
		}
	}

	// SFC扩缩容操作前的状态
	public static void getOptimizationResultBefore(DataCenterNetwork DCN, SFCrequest SFC) {
		System.out.println("SFC " + SFC.getSfcId() + " 的扩缩容操作前的状态为：");
		for (int i = 0; i < SFC.getNumOfVNF(); i++) {
			System.out.print("VNF " + i + "部署在服务器 " + SFC.getVnfFG().get(i).getServerId() + " 的虚拟机上。");
			System.out.print("占用的计算资源为： " + SFC.getVnfFG().get(i).getRealResource() + "  ");
		}
		System.out.println("端到端时延为： " + SFCUtil.sfcEndToEndDelay(SFC));
	}

	// SFC扩缩容操作后的状态
	public static void getOptimizationResultAfter(DataCenterNetwork DCN, SFCrequest SFC) {
		System.out.println("SFC " + SFC.getSfcId() + " 的扩缩容操作后的状态为：");
		for (int i = 0; i < SFC.getNumOfVNF(); i++) {
			System.out.print("VNF " + i + "部署在服务器 " + SFC.getVnfFG().get(i).getServerId() + " 的虚拟机上。" + "占用的计算资源为： "
					+ SFC.getVnfFG().get(i).getRealResource() + "  ");
			if (SFC.getVnfFG().get(i).getCopyVNF() != null) {
				System.out.print("  新建的VNF实例部署在服务器  " + SFC.getVnfFG().get(i).getCopyVNF().getServerId() + " 的虚拟机上。"
						+ "占用的计算资源为： " + SFC.getVnfFG().get(i).getCopyVNF().getRealResource() + "  ");
			}
		}
		System.out.println("端到端时延为： " + SFCUtil.sfcEndToEndDelay(SFC));
	}

	// 执行单个SFC的扩缩容操作
	public static void Scaling(DataCenterNetwork dataCenterNetwork, SFCrequest SFC, double dyDelay) {
		SFCUtil.getOptimizationResultBefore(dataCenterNetwork, SFC);
		int loop = 1; // 轮询次数，默认为一次，可认为是每次接受到扩缩容信号才执行运算，不自动多次实现扩缩容操作
		DynamicOptimization dynamicOptimization = new DynamicOptimization();
		while (loop > 0) {
			SFC.setTolerableDelay(dyDelay);
			// 自动选择进行何种操作
			int flag = dynamicOptimization.scalingFlag(SFC);
			switch (flag) {
			case DynamicOptimization.VERTICALSCALINGUP:
				dynamicOptimization.verticalScalingUpAlg(dataCenterNetwork, SFC);
				dynamicOptimization.horizontalScalingUpAlg(dataCenterNetwork, SFC);
				break;
			case DynamicOptimization.HORIZONTALSCALINGUP:
				dynamicOptimization.horizontalScalingUpAlg(dataCenterNetwork, SFC);
				break;
			case DynamicOptimization.HORIZONTALSCALINGDOWN:
				dynamicOptimization.horizontalScalingDownAlg(dataCenterNetwork, SFC);
				dynamicOptimization.verticalScalingDownAlg(dataCenterNetwork, SFC);
				break;
			case DynamicOptimization.VERTICALSCALINGDOWN:
				dynamicOptimization.verticalScalingDownAlg(dataCenterNetwork, SFC);
				break;
			default:
				break;
			}
			SFCUtil.getOptimizationResultAfter(dataCenterNetwork, SFC);
			loop--;
		}
	}
}
