package nmc2.bupt.edu.cn;

import java.util.*;

public class DynamicOptimization {
	// ���������ݲ�����־λ����ά��ԭ��
	public static final int REMAINING = 0;
	public static final int VERTICALSCALINGUP = 1;
	public static final int HORIZONTALSCALINGUP = 3;
	public static final int HORIZONTALSCALINGDOWN = 4;
	public static final int VERTICALSCALINGDOWN = 2;

	public int scalingFlag(SFCrequest SFC) {
		int sfcLen = SFC.getNumOfVNF();
		// ����
		if (SFCUtil.sfcEndToEndDelay(SFC) > SFC.getTolerableDelay()) {
			int numOfVnfScalable = 0; // sfcLen/2
			for (int i = 0; i < sfcLen; i++) {
				if (SFC.getVnfFG().get(i).getRealResource() < 75.0) {
					numOfVnfScalable++;
				}
			}
			if (numOfVnfScalable >= (sfcLen / 2)) {// ��ֱ����
				return VERTICALSCALINGUP;
			} else {// ˮƽ����
				return HORIZONTALSCALINGUP;
			}
		}
		// ����
		if (SFCUtil.sfcEndToEndDelay(SFC) < SFC.getTolerableDelay() * 0.7) {
			for (int i = 0; i < sfcLen; i++) {
				if (SFC.getVnfFG().get(i).getCopyVNF() != null) {
					return HORIZONTALSCALINGDOWN; // ˮƽ����
				}
			}
			return VERTICALSCALINGDOWN;// ��ֱ����
		}
		// ά����״��������̬�Ż������ı�־
		return REMAINING;
	}

	// ��ֱ����
	public void verticalScalingUpAlg(DataCenterNetwork DCN, SFCrequest SFC) {
		while (SFCUtil.sfcEndToEndDelay(SFC) > SFC.getTolerableDelay()) {
			double minRes = 100.0;
			int minResVnfId = 0;
			for (int i = 0; i < SFC.getNumOfVNF(); i++) {
				if (SFC.getVnfFG().get(i).getRealResource() <= minRes) {
					minRes = SFC.getVnfFG().get(i).getRealResource();
					minResVnfId = i;
				}
			}
			if (minRes > 75.0) {// ��ʾȫ���Ѿ��޷���ֱ������
				// System.out.println("�޷���ֱ���ݣ���ˮƽ������");
				break;
			} else {
				if (SFC.getVnfFG().get(minResVnfId).getCopyVNF() == null) {
					SFC.getVnfFG().get(minResVnfId).setRealResource(Math.min(minRes * 1.2, 80.0));
				} else {
					SFC.getVnfFG().get(minResVnfId).setRealResource(Math.min(minRes * 1.2, 80.0));
					SFC.getVnfFG().get(minResVnfId).getCopyVNF().setRealResource(Math.min(minRes * 1.2, 80.0));
				}
			}
		}
	}

	// ˮƽ����
	public void horizontalScalingUpAlg(DataCenterNetwork DCN, SFCrequest SFC) {
		if (SFCUtil.sfcEndToEndDelay(SFC) > SFC.getTolerableDelay()) {
			// ѡ����ǰCPUʹ������ߵ�VNF����ˮƽ����
			double maxRes = 0.0;
			int maxResVnfId = 0;
			for (int i = 0; i < SFC.getNumOfVNF(); i++) {
				if (SFC.getVnfFG().get(i).getRealResource() >= maxRes) {
					maxRes = SFC.getVnfFG().get(i).getRealResource();
					maxResVnfId = i;
				}
			}
			// ���ѡ�����������VNF����Ʒ
			boolean placementSuccess = false;
			int sfcId = SFC.getSfcId();
			while (!placementSuccess) {
				Random r = new Random(System.currentTimeMillis() + new Random().nextInt(1000));
				int serverIdOfVnfCopy = r.nextInt(DCN.getAllNodes());
				// �����������ѡ���������δ����SFC��VNF���������
				for (int j = 0; j < DCN.getServerList().get(serverIdOfVnfCopy).getCoreNum(); j++) {
					if (DCN.getServerList().get(serverIdOfVnfCopy).getSfcIdInCore(j) == -1
							&& DCN.getServerList().get(serverIdOfVnfCopy).getVnfTypeInCore(j) == -1) {
						VNF tmp = SFC.getVnfFG().get(maxResVnfId);
						tmp.setServerId(serverIdOfVnfCopy);
						SFC.getVnfFG().get(maxResVnfId).setCopyVNF(tmp);
						DCN.getServerList().get(serverIdOfVnfCopy).setSfcIdInCore(j, sfcId);
						DCN.getServerList().get(serverIdOfVnfCopy).setVnfTypeInCore(j,
								SFC.getVnfFG().get(maxResVnfId).getTypeId());
						DCN.getServerList().get(serverIdOfVnfCopy).setCpuCoreRes(j, 100.0 - tmp.getRealResource());
						placementSuccess = true;
						break;
					}
				}
			}
		} else {
			// System.out.println("�޷�ˮƽ���ݣ����ݲ���ʧ��");
		}
	}

	// ˮƽ����
	public void horizontalScalingDownAlg(DataCenterNetwork DCN, SFCrequest SFC) {
		while (SFCUtil.sfcEndToEndDelay(SFC) < SFC.getTolerableDelay() * 0.7) {
			for (int i = 0; i < SFC.getNumOfVNF(); i++) {
				if (SFC.getVnfFG().get(i).getCopyVNF() != null) {
					int serverIdOfVnfCopy = SFC.getVnfFG().get(i).getCopyVNF().getServerId();
					for (int j = 0; j < DCN.getServerList().get(serverIdOfVnfCopy).getCoreNum(); j++) {
						if (SFC.getSfcId() == DCN.getServerList().get(serverIdOfVnfCopy).getSfcIdInCore(j)) {
							DCN.getServerList().get(serverIdOfVnfCopy).setCpuCoreRes(j, 100.0);
							DCN.getServerList().get(serverIdOfVnfCopy).setSfcIdInCore(j, -1);
							DCN.getServerList().get(serverIdOfVnfCopy).setVnfTypeInCore(j, -1);
							break;
						}
					}
					SFC.getVnfFG().get(i).setCopyVNF(null);
					break;// һ��һ���Ƴ�
				}
			}
		}
	}

	// ��ֱ����
	public void verticalScalingDownAlg(DataCenterNetwork DCN, SFCrequest SFC) {
		if (SFCUtil.sfcEndToEndDelay(SFC) < SFC.getTolerableDelay() * 0.7) {
			if (SFC.getTolerableDelay() >= 1000.0) { // ��ʾ���񱻳��������涨ʱ����úܴ�
				for (int i = 0; i < SFC.getNumOfVNF(); i++) {
					int serverId = SFC.getVnfFG().get(i).getServerId();
					for (int j = 0; j < DCN.getServerList().get(serverId).getCoreNum(); j++) {
						if (SFC.getSfcId() == DCN.getServerList().get(serverId).getSfcIdInCore(j)) {
							DCN.getServerList().get(serverId).setCpuCoreRes(j, 100.0);
							DCN.getServerList().get(serverId).setSfcIdInCore(j, -1);
							DCN.getServerList().get(serverId).setVnfTypeInCore(j, -1);
							break;
						}
					}
					if (SFC.getVnfFG().get(i).getCopyVNF() != null) {
						int serverIdOfVnfCopy = SFC.getVnfFG().get(i).getCopyVNF().getServerId();
						for (int j = 0; j < DCN.getServerList().get(serverIdOfVnfCopy).getCoreNum(); j++) {
							if (SFC.getSfcId() == DCN.getServerList().get(serverIdOfVnfCopy).getSfcIdInCore(j)) {
								DCN.getServerList().get(serverIdOfVnfCopy).setCpuCoreRes(j, 100.0);
								DCN.getServerList().get(serverIdOfVnfCopy).setSfcIdInCore(j, -1);
								DCN.getServerList().get(serverIdOfVnfCopy).setVnfTypeInCore(j, -1);
							}
						}
					}
				}
			}
			while (SFCUtil.sfcEndToEndDelay(SFC) < SFC.getTolerableDelay() * 0.7) {
				double maxRes = 0.0;
				int maxResVnfId = 0;
				for (int i = 0; i < SFC.getNumOfVNF(); i++) {
					if (SFC.getVnfFG().get(i).getRealResource() >= maxRes) {
						maxRes = SFC.getVnfFG().get(i).getRealResource();
						maxResVnfId = i;
					}
				}
				if (maxRes < 25.0) {// ��ʾ�Ѿ��޷���ֱ������
					// System.out.println("��Ӧ������ֱ���ݣ�������Ծ��");
					break;
				} else {
					if (SFC.getVnfFG().get(maxResVnfId).getCopyVNF() == null) {
						SFC.getVnfFG().get(maxResVnfId).setRealResource(Math.min(maxRes * 0.8, 20.0));
					} else {
						SFC.getVnfFG().get(maxResVnfId).setRealResource(Math.min(maxRes * 0.8, 20.0));
						SFC.getVnfFG().get(maxResVnfId).getCopyVNF().setRealResource(Math.min(maxRes * 0.8, 20.0));
					}
				}
			}
		} else {
			// System.out.println("�޷���ֱ���ݣ����ݲ���ʧ��");
		}
	}
}
