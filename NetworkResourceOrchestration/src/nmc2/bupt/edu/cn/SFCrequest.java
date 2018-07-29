package nmc2.bupt.edu.cn;

import java.util.*;

class VNF {
	private int typeId = -1;
	private int serverId = -1;
	private double reqResource = 20.0;
	private double realResource = 20.0;
	private VNF copyVNF = null; // 水平扩缩容时用来记录该VNF复制品的信息

	public VNF(int typeId) {
		this.typeId = typeId;
	}

	public VNF(int typeId, double reqResource) {
		this.typeId = typeId;
		this.reqResource = reqResource;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public double getReqResource() {
		return reqResource;
	}

	public void setReqResource(double reqResource) {
		this.reqResource = reqResource;
	}

	public double getRealResource() {
		return realResource;
	}

	public void setRealResource(double realResource) {
		this.realResource = realResource;
	}

	public VNF getCopyVNF() {
		return copyVNF;
	}

	public void setCopyVNF(VNF copyVNF) {
		this.copyVNF = copyVNF;
	}

}

public class SFCrequest {
	private int sfcId;
	private int numOfVNF; // 建议两到五个吧
	private double tolerableDelay = numOfVNF * 40.0; // 不同类型（长度）的SFC端到端时延不同，之后将作为扩缩容的标准，使其动态变化
	private ArrayList<VNF> vnfFG;
	private double[] reqBandWidth;

	public SFCrequest(int sfcId, int numOfVNF) {
		initSFC(sfcId, numOfVNF);
	}

	private void initSFC(int sfcId, int numOfVNF) {
		this.sfcId = sfcId;
		this.numOfVNF = numOfVNF;
		this.vnfFG = new ArrayList<VNF>(numOfVNF);
		this.reqBandWidth = new double[this.numOfVNF - 1];
		for (int i = 0; i < this.numOfVNF; i++) {
			int type = new Random().nextInt(6); // 共有6种类型的VNF，可修改
			this.vnfFG.add(new VNF(type));
		}
		for (int i = 0; i < this.numOfVNF - 1; i++) {
			this.reqBandWidth[i] = 1.0; // 链路编号与虚拟链路前一个VNF下标一致，反正也是线型的SFC
		}
	}

	public int getSfcId() {
		return sfcId;
	}

	public void setSfcId(int sfcId) {
		this.sfcId = sfcId;
	}

	public int getNumOfVNF() {
		return numOfVNF;
	}

	public void setNumOfVNF(int numOfVNF) {
		this.numOfVNF = numOfVNF;
	}

	public double getTolerableDelay() {
		return tolerableDelay;
	}

	public void setTolerableDelay(double tolerableDelay) {
		this.tolerableDelay = tolerableDelay;
	}

	public ArrayList<VNF> getVnfFG() {
		return vnfFG;
	}

	public void setVnfFG(ArrayList<VNF> vnfFG) {
		this.vnfFG = vnfFG;
	}

	public double[] getReqBandWidth() {
		return reqBandWidth;
	}

	public void setReqBandWidth(double[] reqBandWidth) {
		this.reqBandWidth = reqBandWidth;
	}

}
