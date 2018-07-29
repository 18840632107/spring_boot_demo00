package nmc2.bupt.edu.cn;

import java.util.ArrayList;
import java.util.Random;

//封装了完整的算法流程
public class RECM {
	public static int numOfSfc = 10; // SFC的数量
	private int kOfDC = 4; // 胖树的规模大小
	private DataCenterNetwork dataCenterNetwork = null;
	private ArrayList<SFCrequest> sfcList = null;

	public RECM(int numOfSfc) {
		RECM.numOfSfc = numOfSfc;
		this.dataCenterNetwork = new DataCenterNetwork(this.kOfDC);
		this.sfcList = new ArrayList<SFCrequest>(RECM.numOfSfc);
		for (int i = 0; i < RECM.numOfSfc; i++) {
			SFCrequest SFC = new SFCrequest(i, (new Random().nextInt(4) + 2));
			sfcList.add(SFC);
			SFCUtil.orchestrationInit(dataCenterNetwork, SFC);
			SFCUtil.getOptimizationResultBefore(dataCenterNetwork, SFC);
		}
		// 存储数据
		FileOperation.writeFile(this.dataCenterNetwork, this.sfcList);
	}

	// 设置轮询次数（自动扩缩容次数），在每个时间片内，随机设置每个SFC所需的端到端时延，然后进行扩缩容操作
	public void Scaling(double dyDelay) {
		int loop = 1; // 轮询次数，默认为一次，可认为是每次接受到扩缩容信号才执行运算，不自动多次实现扩缩容操作
		while (loop > 0) {
			for (int i = 0; i < RECM.numOfSfc; i++) {
				SFCUtil.getOptimizationResultBefore(dataCenterNetwork, sfcList.get(i));
				sfcList.get(i).setTolerableDelay(dyDelay);
				DynamicOptimization dynamicOptimization = new DynamicOptimization();
				// 自动选择进行何种操作
				int flag = dynamicOptimization.scalingFlag(sfcList.get(i));
				switch (flag) {
				case DynamicOptimization.VERTICALSCALINGUP:
					dynamicOptimization.verticalScalingUpAlg(dataCenterNetwork, sfcList.get(i));
					dynamicOptimization.horizontalScalingUpAlg(dataCenterNetwork, sfcList.get(i));
					break;
				case DynamicOptimization.HORIZONTALSCALINGUP:
					dynamicOptimization.horizontalScalingUpAlg(dataCenterNetwork, sfcList.get(i));
					break;
				case DynamicOptimization.HORIZONTALSCALINGDOWN:
					dynamicOptimization.horizontalScalingDownAlg(dataCenterNetwork, sfcList.get(i));
					dynamicOptimization.verticalScalingDownAlg(dataCenterNetwork, sfcList.get(i));
					break;
				case DynamicOptimization.VERTICALSCALINGDOWN:
					dynamicOptimization.verticalScalingDownAlg(dataCenterNetwork, sfcList.get(i));
					break;
				default:
					break;
				}
				SFCUtil.getOptimizationResultAfter(dataCenterNetwork, sfcList.get(i));
			}
			loop--;
		}
		// 存储数据
		FileOperation.writeFile(this.dataCenterNetwork, this.sfcList);
	}

	// 设置轮询次数，在每个时间片内，随机设置单个SFC所需的端到端时延，然后进行扩缩容操作
	public void Scaling(int SFCId, double dyDelay) {
		if (SFCId < 0 || SFCId >= sfcList.size()) {
			System.out.println("SFC编号输入不合法");
			return;
		}
		int loop = 1; // 轮询次数，默认为一次，可认为是每次接受到扩缩容信号才执行运算，不自动多次实现扩缩容操作
		while (loop > 0) {
			SFCUtil.getOptimizationResultBefore(dataCenterNetwork, sfcList.get(SFCId));
			sfcList.get(SFCId).setTolerableDelay(dyDelay);
			DynamicOptimization dynamicOptimization = new DynamicOptimization();
			// 自动选择进行何种操作
			int flag = dynamicOptimization.scalingFlag(sfcList.get(SFCId));
			switch (flag) {
			case DynamicOptimization.VERTICALSCALINGUP:
				dynamicOptimization.verticalScalingUpAlg(dataCenterNetwork, sfcList.get(SFCId));
				dynamicOptimization.horizontalScalingUpAlg(dataCenterNetwork, sfcList.get(SFCId));
				break;
			case DynamicOptimization.HORIZONTALSCALINGUP:
				dynamicOptimization.horizontalScalingUpAlg(dataCenterNetwork, sfcList.get(SFCId));
				break;
			case DynamicOptimization.HORIZONTALSCALINGDOWN:
				dynamicOptimization.horizontalScalingDownAlg(dataCenterNetwork, sfcList.get(SFCId));
				dynamicOptimization.verticalScalingDownAlg(dataCenterNetwork, sfcList.get(SFCId));
				break;
			case DynamicOptimization.VERTICALSCALINGDOWN:
				dynamicOptimization.verticalScalingDownAlg(dataCenterNetwork, sfcList.get(SFCId));
				break;
			default:
				break;
			}
			SFCUtil.getOptimizationResultAfter(dataCenterNetwork, sfcList.get(SFCId));
			loop--;
		}
		// 存储数据
		FileOperation.writeFile(this.dataCenterNetwork, this.sfcList);
	}
}
