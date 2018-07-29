package nmc2.bupt.edu.cn;

import java.util.ArrayList;
import java.util.Random;

//��װ���������㷨����
public class RECM {
	public static int numOfSfc = 10; // SFC������
	private int kOfDC = 4; // �����Ĺ�ģ��С
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
		// �洢����
		FileOperation.writeFile(this.dataCenterNetwork, this.sfcList);
	}

	// ������ѯ�������Զ������ݴ���������ÿ��ʱ��Ƭ�ڣ��������ÿ��SFC����Ķ˵���ʱ�ӣ�Ȼ����������ݲ���
	public void Scaling(double dyDelay) {
		int loop = 1; // ��ѯ������Ĭ��Ϊһ�Σ�����Ϊ��ÿ�ν��ܵ��������źŲ�ִ�����㣬���Զ����ʵ�������ݲ���
		while (loop > 0) {
			for (int i = 0; i < RECM.numOfSfc; i++) {
				SFCUtil.getOptimizationResultBefore(dataCenterNetwork, sfcList.get(i));
				sfcList.get(i).setTolerableDelay(dyDelay);
				DynamicOptimization dynamicOptimization = new DynamicOptimization();
				// �Զ�ѡ����к��ֲ���
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
		// �洢����
		FileOperation.writeFile(this.dataCenterNetwork, this.sfcList);
	}

	// ������ѯ��������ÿ��ʱ��Ƭ�ڣ�������õ���SFC����Ķ˵���ʱ�ӣ�Ȼ����������ݲ���
	public void Scaling(int SFCId, double dyDelay) {
		if (SFCId < 0 || SFCId >= sfcList.size()) {
			System.out.println("SFC������벻�Ϸ�");
			return;
		}
		int loop = 1; // ��ѯ������Ĭ��Ϊһ�Σ�����Ϊ��ÿ�ν��ܵ��������źŲ�ִ�����㣬���Զ����ʵ�������ݲ���
		while (loop > 0) {
			SFCUtil.getOptimizationResultBefore(dataCenterNetwork, sfcList.get(SFCId));
			sfcList.get(SFCId).setTolerableDelay(dyDelay);
			DynamicOptimization dynamicOptimization = new DynamicOptimization();
			// �Զ�ѡ����к��ֲ���
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
		// �洢����
		FileOperation.writeFile(this.dataCenterNetwork, this.sfcList);
	}
}
