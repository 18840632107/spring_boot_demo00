package nmc2.bupt.edu.cn;

import java.util.ArrayList;
import java.util.Random;

public class SFCUtil {

	// ��ΪҪרע�����ݲ�����ֻʵ�ּ򵥵ı��ų�ʼ�������ѡ�����������VNF����������ʼĿ�Ľڵ㣬ͬһSFC��VNF������һ��������
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
				int serverId = r.nextInt(serverCandidate.size()); // �����õ���index
				// �����������ѡ���������δ����SFC��VNF���������
				for (int j = 0; j < DCN.getServerList().get(serverId).getCoreNum(); j++) {
					if (DCN.getServerList().get(serverId).getSfcIdInCore(j) == -1
							&& DCN.getServerList().get(serverId).getVnfTypeInCore(j) == -1
							&& DCN.getServerList().get(serverId).getSfcIdInCore(j) != sfcId) {// ����VNF�ɹ�
						DCN.getServerList().get(serverId).setSfcIdInCore(j, sfcId);
						DCN.getServerList().get(serverId).setVnfTypeInCore(j, SFC.getVnfFG().get(i).getTypeId());
						double initResInCore = DCN.getServerList().get(serverId).getCpuCoreRes(j);
						double initResOfVNF = SFC.getVnfFG().get(i).getReqResource();
						// ��һ����Դռ��
						DCN.getServerList().get(serverId).setCpuCoreRes(j, initResInCore - initResOfVNF);
						SFC.getVnfFG().get(i).setServerId(DCN.getServerList().get(serverId).getServerId());
						vnfDeploySuccess = true;
						break;
					}
				}
				serverCandidate.remove(serverId); // ���ﲢ�����Ƴ��Ǹ�ֵ����������±����
				if (vnfDeploySuccess == true) {
					break;
				}
				if (serverCandidate.size() == 0 && vnfDeploySuccess == false) {
					// System.out.println("Faliure");����ʧ��
					throw new NullPointerException();
				}
			}
		}
		// return ����ɹ�
		System.out.println("Success");
	}

	// ȷ��VNF������Դ�������봦��ʱ��֮��Ĺ�ϵ������VNF�Ĵ���ʱ�ӣ�����������
	public static double vnfProcessDelay(double realRes) {
		double minRes = 20.0;
		double maxDelay = 30.0;
		double maxRes = 80.0;
		double minDelay = 10.0;
		double k = (maxDelay - minDelay) / (minRes - maxRes); // б��
		double b = ((minDelay * minRes) - (maxDelay * maxRes)) / (minRes - maxRes); // �ؾ�

		if (realRes > 80.0 && realRes <= 100)
			return minDelay;
		else if (realRes < 20.0 && realRes > 0)
			return maxDelay; // ���ǲ�����ȡֵ����20.0
		else if (realRes > 100 || realRes <= 0)
			throw new IllegalArgumentException();
		else
			return k * realRes + b;
	}

	// ����SFC����Ķ˵���ʱ��
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

	// ���������������Դ��״
	public static void getResOfDCN(DataCenterNetwork DCN) {
		System.out.println("�������ĵķ�������Դ��״Ϊ��");
		for (int i = 0; i < DCN.getServerList().size(); i++) {
			for (int j = 0; j < DCN.getServerList().get(i).getCoreNum(); j++) {
				System.out
						.println("������ " + i + "��" + j + "�����������Դʣ����Ϊ�� " + DCN.getServerList().get(i).getCpuCoreRes(j));
			}
		}
	}

	// ��ӡSFC��ʼ���Ž��
	public static void getOrchestrationResult(DataCenterNetwork DCN, SFCrequest SFC) {
		System.out.println("SFC " + SFC.getSfcId() + " �ĳ�ʼ���Ž��Ϊ��");
		for (int i = 0; i < SFC.getNumOfVNF(); i++) {
			System.out.println("VNF " + i + "�����ڷ����� " + SFC.getVnfFG().get(i).getServerId() + " ��������ϡ�");
		}
	}

	// SFC�����ݲ���ǰ��״̬
	public static void getOptimizationResultBefore(DataCenterNetwork DCN, SFCrequest SFC) {
		System.out.println("SFC " + SFC.getSfcId() + " �������ݲ���ǰ��״̬Ϊ��");
		for (int i = 0; i < SFC.getNumOfVNF(); i++) {
			System.out.print("VNF " + i + "�����ڷ����� " + SFC.getVnfFG().get(i).getServerId() + " ��������ϡ�");
			System.out.print("ռ�õļ�����ԴΪ�� " + SFC.getVnfFG().get(i).getRealResource() + "  ");
		}
		System.out.println("�˵���ʱ��Ϊ�� " + SFCUtil.sfcEndToEndDelay(SFC));
	}

	// SFC�����ݲ������״̬
	public static void getOptimizationResultAfter(DataCenterNetwork DCN, SFCrequest SFC) {
		System.out.println("SFC " + SFC.getSfcId() + " �������ݲ������״̬Ϊ��");
		for (int i = 0; i < SFC.getNumOfVNF(); i++) {
			System.out.print("VNF " + i + "�����ڷ����� " + SFC.getVnfFG().get(i).getServerId() + " ��������ϡ�" + "ռ�õļ�����ԴΪ�� "
					+ SFC.getVnfFG().get(i).getRealResource() + "  ");
			if (SFC.getVnfFG().get(i).getCopyVNF() != null) {
				System.out.print("  �½���VNFʵ�������ڷ�����  " + SFC.getVnfFG().get(i).getCopyVNF().getServerId() + " ��������ϡ�"
						+ "ռ�õļ�����ԴΪ�� " + SFC.getVnfFG().get(i).getCopyVNF().getRealResource() + "  ");
			}
		}
		System.out.println("�˵���ʱ��Ϊ�� " + SFCUtil.sfcEndToEndDelay(SFC));
	}

	// ִ�е���SFC�������ݲ���
	public static void Scaling(DataCenterNetwork dataCenterNetwork, SFCrequest SFC, double dyDelay) {
		SFCUtil.getOptimizationResultBefore(dataCenterNetwork, SFC);
		int loop = 1; // ��ѯ������Ĭ��Ϊһ�Σ�����Ϊ��ÿ�ν��ܵ��������źŲ�ִ�����㣬���Զ����ʵ�������ݲ���
		DynamicOptimization dynamicOptimization = new DynamicOptimization();
		while (loop > 0) {
			SFC.setTolerableDelay(dyDelay);
			// �Զ�ѡ����к��ֲ���
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
