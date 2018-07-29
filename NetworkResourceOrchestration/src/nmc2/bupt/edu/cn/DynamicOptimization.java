package nmc2.bupt.edu.cn;

import java.util.*;

public class DynamicOptimization {
	// 四种扩缩容操作标志位，或维持原样
	public static final int REMAINING = 0;
	public static final int VERTICALSCALINGUP = 1;
	public static final int HORIZONTALSCALINGUP = 3;
	public static final int HORIZONTALSCALINGDOWN = 4;
	public static final int VERTICALSCALINGDOWN = 2;

	public int scalingFlag(SFCrequest SFC) {
		int sfcLen = SFC.getNumOfVNF();
		// 扩容
		if (SFCUtil.sfcEndToEndDelay(SFC) > SFC.getTolerableDelay()) {
			int numOfVnfScalable = 0; // sfcLen/2
			for (int i = 0; i < sfcLen; i++) {
				if (SFC.getVnfFG().get(i).getRealResource() < 75.0) {
					numOfVnfScalable++;
				}
			}
			if (numOfVnfScalable >= (sfcLen / 2)) {// 垂直扩容
				return VERTICALSCALINGUP;
			} else {// 水平扩容
				return HORIZONTALSCALINGUP;
			}
		}
		// 缩容
		if (SFCUtil.sfcEndToEndDelay(SFC) < SFC.getTolerableDelay() * 0.7) {
			for (int i = 0; i < sfcLen; i++) {
				if (SFC.getVnfFG().get(i).getCopyVNF() != null) {
					return HORIZONTALSCALINGDOWN; // 水平缩容
				}
			}
			return VERTICALSCALINGDOWN;// 垂直缩容
		}
		// 维持现状，跳出动态优化操作的标志
		return REMAINING;
	}

	// 垂直扩容
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
			if (minRes > 75.0) {// 表示全都已经无法垂直扩容了
				// System.out.println("无法垂直扩容，该水平扩容了");
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

	// 水平扩容
	public void horizontalScalingUpAlg(DataCenterNetwork DCN, SFCrequest SFC) {
		if (SFCUtil.sfcEndToEndDelay(SFC) > SFC.getTolerableDelay()) {
			// 选出当前CPU使用率最高的VNF进行水平扩容
			double maxRes = 0.0;
			int maxResVnfId = 0;
			for (int i = 0; i < SFC.getNumOfVNF(); i++) {
				if (SFC.getVnfFG().get(i).getRealResource() >= maxRes) {
					maxRes = SFC.getVnfFG().get(i).getRealResource();
					maxResVnfId = i;
				}
			}
			// 随机选择服务器放置VNF复制品
			boolean placementSuccess = false;
			int sfcId = SFC.getSfcId();
			while (!placementSuccess) {
				Random r = new Random(System.currentTimeMillis() + new Random().nextInt(1000));
				int serverIdOfVnfCopy = r.nextInt(DCN.getAllNodes());
				// 遍历虚拟机，选择服务器上未部署SFC（VNF）的虚拟机
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
			// System.out.println("无法水平扩容，扩容操作失败");
		}
	}

	// 水平缩容
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
					break;// 一个一个移除
				}
			}
		}
	}

	// 垂直缩容
	public void verticalScalingDownAlg(DataCenterNetwork DCN, SFCrequest SFC) {
		if (SFCUtil.sfcEndToEndDelay(SFC) < SFC.getTolerableDelay() * 0.7) {
			if (SFC.getTolerableDelay() >= 1000.0) { // 表示服务被撤销，将规定时延设得很大
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
				if (maxRes < 25.0) {// 表示已经无法垂直缩容了
					// System.out.println("不应继续垂直扩容，保留活跃度");
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
			// System.out.println("无法垂直缩容，缩容操作失败");
		}
	}
}
