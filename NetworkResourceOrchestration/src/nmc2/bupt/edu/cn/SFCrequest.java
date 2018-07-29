package nmc2.bupt.edu.cn;

import java.util.*;

class VNF {
	private int typeId = -1;
	private int serverId = -1;
	private double reqResource = 20.0;
	private double realResource = 20.0;
	private VNF copyVNF = null; // ˮƽ������ʱ������¼��VNF����Ʒ����Ϣ

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
	private int numOfVNF; // �������������
	private double tolerableDelay = numOfVNF * 40.0; // ��ͬ���ͣ����ȣ���SFC�˵���ʱ�Ӳ�ͬ��֮����Ϊ�����ݵı�׼��ʹ�䶯̬�仯
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
			int type = new Random().nextInt(6); // ����6�����͵�VNF�����޸�
			this.vnfFG.add(new VNF(type));
		}
		for (int i = 0; i < this.numOfVNF - 1; i++) {
			this.reqBandWidth[i] = 1.0; // ��·�����������·ǰһ��VNF�±�һ�£�����Ҳ�����͵�SFC
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
