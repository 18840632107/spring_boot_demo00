package wine.orchestration;

import java.util.Random;
import java.util.Vector;

import random.orchestration.Dijkstra;
import random.orchestration.SfcUtil;

public class SfcRequest implements Cloneable {

	public SmallNetwork smallNetwork; // ȷ����������
	public MediumNetwork mediumNetwork;
	public LargeNetwork largeNetwork;
	public int[] endPoints; // ȷ����ʼ��Ŀ�Ľڵ�
	public int networkType; // �������������
	public int geneNum; // ��������ڵ���
	public int sfcType; // SFC���������
	public int chromLen; // SFC����Ľڵ���
	public int[] geneSequence; // Ⱦɫ���ڵĻ������У�����ڵ����У�
	public double fitness; // ��Ӧ��

	public SfcRequest(int networkType, int sfcType) {
		this.smallNetwork = main.smallNetwork;
		this.mediumNetwork = main.mediumNetwork;
		this.largeNetwork = main.largeNetwork;

		this.networkType = networkType;
		switch (networkType) {
		case 1:
			this.geneNum = SmallNetwork.nodeNum;
			this.endPoints = endPointsGeneration(SmallNetwork.endPoints);
			break;
		case 2:
			this.geneNum = MediumNetwork.nodeNum;
			this.endPoints = endPointsGeneration(MediumNetwork.endPoints);
			break;
		case 3:
			this.geneNum = LargeNetwork.nodeNum;
			this.endPoints = endPointsGeneration(LargeNetwork.endPoints);
			break;
		default:
			break;
		}

		this.sfcType = sfcType;
		switch (sfcType) {
		case 1:
			this.chromLen = 3;
			break;
		case 2:
			this.chromLen = 4;
			break;
		case 3:
			this.chromLen = 8;
			break;
		case 4:
			this.chromLen = 7;
			break;
		case 5:
			this.chromLen = 6;
			break;
		case 6:
			this.chromLen = 7;
			break;
		default:
			break;
		}
		this.geneSequence = new int[this.chromLen];
		this.fitness = 0.0;
	}

	public int[] getGeneSequence() {
		return geneSequence;
	}

	public void setGeneSequence(int[] geneSequence) {
		this.geneSequence = geneSequence;
	}

	public double getFitness() {
		this.fitness = calculateFitness();
		return fitness;
	}

	// ����������ն˽ڵ�����(��ͬ������û��ն˽ڵ㲻ͬ)
	public int[] endPointsGeneration(int[] clientNode) {
		Vector<Integer> endPoints = new Vector<Integer>();
		for (int i = 1; i < clientNode.length; i++) {
			endPoints.add(clientNode[i]);
		}
		int[] res = new int[3];
		for (int i = 0; i < 3; i++) {
			Random r = new Random(System.currentTimeMillis() + new Random().nextInt(1000));
			int index = r.nextInt(endPoints.size());
			res[i] = endPoints.get(index).intValue();
			endPoints.remove(index);
		}
		return res;
	}

	// ����WiNE�⣬��������·���⼸��һ�������ܿ��ܻ��һ�㣩�����Բ�д�ˣ�
	/* ����˼�룺
	 * 1. Ϊÿһ��VNFɸѡ���ɲ��������ڵ㼯�ϣ���ΪVNF1��VNF2...
	 * 2. ���ո��Լ���Ԫ�ظ�������VNF��������
	 * 3. �����кõ�VNF����ӳ�䣬�ҳ�ӳ������VNF����С��·����������ڵ�ԣ�һһƥ����ң�
	 */
	public void wineGeneration() {
		geneSequence[0] = endPoints[0]; // ���
		geneSequence[chromLen - 1] = endPoints[1]; // �յ㣨֮һ��
		if (sfcType == 3 || sfcType == 4) {
			geneSequence[chromLen - 2] = endPoints[2];
		}
		
		// ȷ������VNF�Ľڵ�,geneSequence[]��Ԫ��
		if (networkType == 1) {
			int[] tmpPathArray1 = null;
			int[] tmpPathArray2 = null;
			int countNodeNum = 0;

			switch (sfcType) {
			case 1:
				Vector<Integer> VNF1 = new Vector<Integer>();				
				for(int i=0;i<smallNetwork.nodeNum;i++){ 
					int tmp = (geneSequence[0]+i)%smallNetwork.nodeNum;
					if (SfcUtil.nodeComsuption(SfcUtil.smallInstanceNodeComsuption) < this.smallNetwork.nodesCompute[tmp]) {
						VNF1.add(Integer.valueOf(tmp));
					}
				}
				tmpPathArray1 = Dijkstra.dijkstraPathArray(this.smallNetwork.linksDelay, geneSequence[0],
						geneSequence[chromLen - 1]);
				
				break;
			case 2:
				
				tmpPathArray1 = Dijkstra.dijkstraPathArray(this.smallNetwork.linksDelay, geneSequence[0],
						geneSequence[chromLen - 1]);
				
				break;
			// ������Ӧ������Լ����ͬһSFC�е�VNF���ܷ�����ͬһ�ڵ��ϣ����ﲻ���ˣ��鷳
			case 3:
				
				tmpPathArray1 = Dijkstra.dijkstraPathArray(this.smallNetwork.linksDelay, geneSequence[0],
						geneSequence[chromLen - 1]);
				
				// �ڶ���·�������SFC�зֲ��VNF
				
				tmpPathArray2 = Dijkstra.dijkstraPathArray(this.smallNetwork.linksDelay, geneSequence[1],
						geneSequence[chromLen - 2]);
				
				break;
			case 4:
				
				tmpPathArray1 = Dijkstra.dijkstraPathArray(this.smallNetwork.linksDelay, geneSequence[0],
						geneSequence[chromLen - 1]);
				
				// �ڶ���·�������SFC�зֲ��VNF
				
				tmpPathArray2 = Dijkstra.dijkstraPathArray(this.smallNetwork.linksDelay, geneSequence[2],
						geneSequence[chromLen - 2]);
				
				break;
			case 5:
				
				tmpPathArray1 = Dijkstra.dijkstraPathArray(this.smallNetwork.linksDelay, geneSequence[0],
						geneSequence[chromLen - 1]);
				
				// �ڶ���·�������SFC�зֲ��VNF
				
				tmpPathArray2 = Dijkstra.dijkstraPathArray(this.smallNetwork.linksDelay, geneSequence[2],
						geneSequence[chromLen - 1]);
				
				break;
			case 6:
				
				tmpPathArray1 = Dijkstra.dijkstraPathArray(this.smallNetwork.linksDelay, geneSequence[0],
						geneSequence[chromLen - 1]);
				
				// �ڶ���·�������SFC�зֲ��VNF
				
				tmpPathArray2 = Dijkstra.dijkstraPathArray(this.smallNetwork.linksDelay, geneSequence[1],
						geneSequence[chromLen - 1]);
				
				break;
			default:
				break;
			}
		}

	}

	// ������Ӧ�Ⱥ���
	public double calculateFitness() {
		double processDelayWeight = 1.0; // ����ʱ��Ȩ�ز���
		double processDelay = 0.0; // ����ʱ��
		double transDelayWeight = 1.0; // ����ʱ��Ȩ�ز���
		double transDelay = 0.0; // ����ʱ��
		double totalDelayWeight = 1.0; // ��ʱ��Ȩ��
		double totalDelay = 0.0; // ��ʱ��

		double nodeWeight = 1.0; // �ڵ㿪��Ȩ�ز���
		double nodeCost = 0.0; // �ڵ㿪��
		double linkWeight = 1.0; // ��·����Ȩ�ز���
		double linkCost = 0.0; // ��·����
		double totalCostWeight = 1.0; // �ܿ���Ȩ�ز���
		double totalCost = 0.0; // �ܿ���

		double fitness = 0.0;
		int[][] calculateFitnessDelayMatrix = null;

		switch (networkType) {
		case 1:
			calculateFitnessDelayMatrix = this.smallNetwork.linksDelay;
			break;
		case 2:
			calculateFitnessDelayMatrix = this.mediumNetwork.linksDelay;
			break;
		case 3:
			calculateFitnessDelayMatrix = this.largeNetwork.linksDelay;
			break;
		default:
			break;
		}

		int[] tmpPathArray = null;
		// Ϊ����SFC׼����
		double processDelay1 = 0.0;
		double processDelay2 = 0.0;
		double transDelay1 = 0.0;
		double transDelay2 = 0.0;
		double totalDelay1 = 0.0;
		double totalDelay2 = 0.0;

		switch (sfcType) {
		case 1:
			for (int i = 0; i < geneSequence.length - 1; i++) {
				tmpPathArray = Dijkstra.dijkstraPathArray(calculateFitnessDelayMatrix, geneSequence[i],
						geneSequence[i + 1]);
				for (int j = 0; j < tmpPathArray.length - 1; j++) {
					linkCost = SfcUtil.linkComsuption();
					this.smallNetwork.linksCapacity[tmpPathArray[j]][tmpPathArray[j + 1]] -= linkCost;
					this.smallNetwork.linksCapacity[tmpPathArray[j + 1]][tmpPathArray[j]] -= linkCost;
				}
				transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[i],
						geneSequence[i + 1]);
			}

			System.out.println("���Բ���VNFa�Ľڵ��У�" + geneSequence[1]);
			this.smallNetwork.nodesCompute[geneSequence[1]] -= SfcUtil
					.nodeComsuption(SfcUtil.smallInstanceNodeComsuption);
			processDelay += SfcUtil.processDelay();

			totalDelay = (processDelayWeight * processDelay + transDelayWeight * transDelay);
			break;
		case 2:
			for (int i = 0; i < geneSequence.length - 1; i++) {
				tmpPathArray = Dijkstra.dijkstraPathArray(calculateFitnessDelayMatrix, geneSequence[i],
						geneSequence[i + 1]);
				for (int j = 0; j < tmpPathArray.length - 1; j++) {
					linkCost = SfcUtil.linkComsuption();
					this.smallNetwork.linksCapacity[tmpPathArray[j]][tmpPathArray[j + 1]] -= linkCost;
					this.smallNetwork.linksCapacity[tmpPathArray[j + 1]][tmpPathArray[j]] -= linkCost;
				}
				transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[i],
						geneSequence[i + 1]);
			}

			System.out.print("����VNFa��VNFb�Ľڵ��ǣ�" + geneSequence[1] + " " + geneSequence[2]);
			this.smallNetwork.nodesCompute[geneSequence[1]] -= SfcUtil
					.nodeComsuption(SfcUtil.smallInstanceNodeComsuption);
			processDelay += SfcUtil.processDelay();
			this.smallNetwork.nodesCompute[geneSequence[2]] -= SfcUtil
					.nodeComsuption(SfcUtil.smallInstanceNodeComsuption);
			processDelay += SfcUtil.processDelay();

			totalDelay = (processDelayWeight * processDelay + transDelayWeight * transDelay);
			break;
		case 3:
			// ��һ��·
			for (int i = 0; i < (3); i++) {
				tmpPathArray = Dijkstra.dijkstraPathArray(calculateFitnessDelayMatrix, geneSequence[i],
						geneSequence[i + 1]);
				for (int j = 0; j < tmpPathArray.length - 1; j++) {
					linkCost = SfcUtil.linkComsuption();
					this.smallNetwork.linksCapacity[tmpPathArray[j]][tmpPathArray[j + 1]] -= linkCost;
					this.smallNetwork.linksCapacity[tmpPathArray[j + 1]][tmpPathArray[j]] -= linkCost;
				}
				transDelay1 += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[i],
						geneSequence[i + 1]);
			}
			tmpPathArray = Dijkstra.dijkstraPathArray(calculateFitnessDelayMatrix, geneSequence[3], geneSequence[7]);
			for (int j = 0; j < tmpPathArray.length - 1; j++) {
				linkCost = SfcUtil.linkComsuption();
				this.smallNetwork.linksCapacity[tmpPathArray[j]][tmpPathArray[j + 1]] -= linkCost;
				this.smallNetwork.linksCapacity[tmpPathArray[j + 1]][tmpPathArray[j]] -= linkCost;
			}
			transDelay1 += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[3], geneSequence[7]);

			System.out
					.println("����VNFa��VNFb��VNFc�Ľڵ��ǣ�" + geneSequence[1] + " " + geneSequence[2] + " " + geneSequence[3]);
			for (int i = 0; i < 3; i++) {
				processDelay1 += SfcUtil.processDelay();
				this.smallNetwork.nodesCompute[geneSequence[i + 1]] -= SfcUtil
						.nodeComsuption(SfcUtil.smallInstanceNodeComsuption);
			}

			totalDelay1 = (processDelayWeight * processDelay1 + transDelayWeight * transDelay1);
			// �ڶ���·
			tmpPathArray = Dijkstra.dijkstraPathArray(calculateFitnessDelayMatrix, geneSequence[0], geneSequence[1]);
			transDelay2 += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[0], geneSequence[1]);
			for (int j = 0; j < tmpPathArray.length - 1; j++) {
				linkCost = SfcUtil.linkComsuption();
				this.smallNetwork.linksCapacity[tmpPathArray[j]][tmpPathArray[j + 1]] -= linkCost;
				this.smallNetwork.linksCapacity[tmpPathArray[j + 1]][tmpPathArray[j]] -= linkCost;
			}
			tmpPathArray = Dijkstra.dijkstraPathArray(calculateFitnessDelayMatrix, geneSequence[1], geneSequence[4]);
			transDelay2 += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[1], geneSequence[4]);
			for (int j = 0; j < tmpPathArray.length - 1; j++) {
				linkCost = SfcUtil.linkComsuption();
				this.smallNetwork.linksCapacity[tmpPathArray[j]][tmpPathArray[j + 1]] -= linkCost;
				this.smallNetwork.linksCapacity[tmpPathArray[j + 1]][tmpPathArray[j]] -= linkCost;
			}
			for (int i = 4; i < (6); i++) {
				tmpPathArray = Dijkstra.dijkstraPathArray(calculateFitnessDelayMatrix, geneSequence[i],
						geneSequence[i + 1]);
				for (int j = 0; j < tmpPathArray.length - 1; j++) {
					linkCost = SfcUtil.linkComsuption();
					this.smallNetwork.linksCapacity[tmpPathArray[j]][tmpPathArray[j + 1]] -= linkCost;
					this.smallNetwork.linksCapacity[tmpPathArray[j + 1]][tmpPathArray[j]] -= linkCost;
				}
				transDelay2 += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[i],
						geneSequence[i + 1]);
			}

			for (int i = 0; i < 3; i++) {
				processDelay2 += SfcUtil.processDelay();
			}
			System.out.println("����VNFc��VNFd�Ľڵ��ǣ�" + geneSequence[4] + " " + geneSequence[5]);
			this.smallNetwork.nodesCompute[geneSequence[4]] -= SfcUtil
					.nodeComsuption(SfcUtil.smallInstanceNodeComsuption);
			this.smallNetwork.nodesCompute[geneSequence[5]] -= SfcUtil
					.nodeComsuption(SfcUtil.smallInstanceNodeComsuption);

			totalDelay2 = (processDelayWeight * processDelay2 + transDelayWeight * transDelay2);

			totalDelay = Math.max(totalDelay1, totalDelay2);
			break;
		case 4:
			// ��һ��·
			for (int i = 0; i < (3); i++) {
				tmpPathArray = Dijkstra.dijkstraPathArray(calculateFitnessDelayMatrix, geneSequence[i],
						geneSequence[i + 1]);
				for (int j = 0; j < tmpPathArray.length - 1; j++) {
					linkCost = SfcUtil.linkComsuption();
					this.smallNetwork.linksCapacity[tmpPathArray[j]][tmpPathArray[j + 1]] -= linkCost;
					this.smallNetwork.linksCapacity[tmpPathArray[j + 1]][tmpPathArray[j]] -= linkCost;
				}
				transDelay1 += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[i],
						geneSequence[i + 1]);
			}
			tmpPathArray = Dijkstra.dijkstraPathArray(calculateFitnessDelayMatrix, geneSequence[3], geneSequence[6]);
			for (int j = 0; j < tmpPathArray.length - 1; j++) {
				linkCost = SfcUtil.linkComsuption();
				this.smallNetwork.linksCapacity[tmpPathArray[j]][tmpPathArray[j + 1]] -= linkCost;
				this.smallNetwork.linksCapacity[tmpPathArray[j + 1]][tmpPathArray[j]] -= linkCost;
			}
			transDelay1 += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[3], geneSequence[6]);

			for (int i = 0; i < 3; i++) {
				processDelay1 += SfcUtil.processDelay();
				this.smallNetwork.nodesCompute[geneSequence[i + 1]] -= SfcUtil
						.nodeComsuption(SfcUtil.smallInstanceNodeComsuption);
			}
			System.out
					.println("����VNFa��VNFb��VNFd�Ľڵ��ǣ�" + geneSequence[1] + " " + geneSequence[2] + " " + geneSequence[3]);

			totalDelay1 = (processDelayWeight * processDelay1 + transDelayWeight * transDelay1);
			// �ڶ���·
			for (int i = 0; i < (2); i++) {
				tmpPathArray = Dijkstra.dijkstraPathArray(calculateFitnessDelayMatrix, geneSequence[i],
						geneSequence[i + 1]);
				for (int j = 0; j < tmpPathArray.length - 1; j++) {
					linkCost = SfcUtil.linkComsuption();
					this.smallNetwork.linksCapacity[tmpPathArray[j]][tmpPathArray[j + 1]] -= linkCost;
					this.smallNetwork.linksCapacity[tmpPathArray[j + 1]][tmpPathArray[j]] -= linkCost;
				}
				transDelay2 += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[i],
						geneSequence[i + 1]);
			}
			tmpPathArray = Dijkstra.dijkstraPathArray(calculateFitnessDelayMatrix, geneSequence[2], geneSequence[4]);
			transDelay2 += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[2], geneSequence[4]);
			for (int j = 0; j < tmpPathArray.length - 1; j++) {
				linkCost = SfcUtil.linkComsuption();
				this.smallNetwork.linksCapacity[tmpPathArray[j]][tmpPathArray[j + 1]] -= linkCost;
				this.smallNetwork.linksCapacity[tmpPathArray[j + 1]][tmpPathArray[j]] -= linkCost;
			}
			tmpPathArray = Dijkstra.dijkstraPathArray(calculateFitnessDelayMatrix, geneSequence[4], geneSequence[5]);
			transDelay2 += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[4], geneSequence[5]);
			for (int j = 0; j < tmpPathArray.length - 1; j++) {
				linkCost = SfcUtil.linkComsuption();
				this.smallNetwork.linksCapacity[tmpPathArray[j]][tmpPathArray[j + 1]] -= linkCost;
				this.smallNetwork.linksCapacity[tmpPathArray[j + 1]][tmpPathArray[j]] -= linkCost;
			}

			for (int i = 0; i < 3; i++) {
				processDelay2 += SfcUtil.processDelay();
			}
			System.out.println("���Բ���VNFc�Ľڵ��У�" + geneSequence[4]);
			this.smallNetwork.nodesCompute[geneSequence[4]] -= SfcUtil
					.nodeComsuption(SfcUtil.smallInstanceNodeComsuption);
			totalDelay2 = (processDelayWeight * processDelay2 + transDelayWeight * transDelay2);

			totalDelay = Math.max(totalDelay1, totalDelay2);
			break;
		case 5:
			// ��һ��·
			for (int i = 0; i < (3); i++) {
				tmpPathArray = Dijkstra.dijkstraPathArray(calculateFitnessDelayMatrix, geneSequence[i],
						geneSequence[i + 1]);
				for (int j = 0; j < tmpPathArray.length - 1; j++) {
					linkCost = SfcUtil.linkComsuption();
					this.smallNetwork.linksCapacity[tmpPathArray[j]][tmpPathArray[j + 1]] -= linkCost;
					this.smallNetwork.linksCapacity[tmpPathArray[j + 1]][tmpPathArray[j]] -= linkCost;
				}
				transDelay1 += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[i],
						geneSequence[i + 1]);
			}
			tmpPathArray = Dijkstra.dijkstraPathArray(calculateFitnessDelayMatrix, geneSequence[3], geneSequence[5]);
			transDelay1 += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[3], geneSequence[5]);
			for (int j = 0; j < tmpPathArray.length - 1; j++) {
				linkCost = SfcUtil.linkComsuption();
				this.smallNetwork.linksCapacity[tmpPathArray[j]][tmpPathArray[j + 1]] -= linkCost;
				this.smallNetwork.linksCapacity[tmpPathArray[j + 1]][tmpPathArray[j]] -= linkCost;
			}

			System.out
					.println("����VNFa��VNFb��VNFc�Ľڵ��ǣ�" + geneSequence[1] + " " + geneSequence[2] + " " + geneSequence[3]);
			for (int i = 0; i < 3; i++) {
				processDelay1 += SfcUtil.processDelay();
				this.smallNetwork.nodesCompute[geneSequence[i + 1]] -= SfcUtil
						.nodeComsuption(SfcUtil.smallInstanceNodeComsuption);
			}

			totalDelay1 = (processDelayWeight * processDelay1 + transDelayWeight * transDelay1);
			// �ڶ���·
			for (int i = 0; i < (2); i++) {
				tmpPathArray = Dijkstra.dijkstraPathArray(calculateFitnessDelayMatrix, geneSequence[i],
						geneSequence[i + 1]);
				for (int j = 0; j < tmpPathArray.length - 1; j++) {
					linkCost = SfcUtil.linkComsuption();
					this.smallNetwork.linksCapacity[tmpPathArray[j]][tmpPathArray[j + 1]] -= linkCost;
					this.smallNetwork.linksCapacity[tmpPathArray[j + 1]][tmpPathArray[j]] -= linkCost;
				}
				transDelay2 += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[i],
						geneSequence[i + 1]);
			}
			tmpPathArray = Dijkstra.dijkstraPathArray(calculateFitnessDelayMatrix, geneSequence[2], geneSequence[4]);
			transDelay2 += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[2], geneSequence[4]);
			for (int j = 0; j < tmpPathArray.length - 1; j++) {
				linkCost = SfcUtil.linkComsuption();
				this.smallNetwork.linksCapacity[tmpPathArray[j]][tmpPathArray[j + 1]] -= linkCost;
				this.smallNetwork.linksCapacity[tmpPathArray[j + 1]][tmpPathArray[j]] -= linkCost;
			}
			tmpPathArray = Dijkstra.dijkstraPathArray(calculateFitnessDelayMatrix, geneSequence[4], geneSequence[5]);
			transDelay2 += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[4], geneSequence[5]);
			for (int j = 0; j < tmpPathArray.length - 1; j++) {
				linkCost = SfcUtil.linkComsuption();
				this.smallNetwork.linksCapacity[tmpPathArray[j]][tmpPathArray[j + 1]] -= linkCost;
				this.smallNetwork.linksCapacity[tmpPathArray[j + 1]][tmpPathArray[j]] -= linkCost;
			}

			for (int i = 0; i < 3; i++) {
				processDelay2 += SfcUtil.processDelay();
			}
			System.out.println("���Բ���VNFd�Ľڵ��У�" + geneSequence[4]);
			this.smallNetwork.nodesCompute[geneSequence[4]] -= SfcUtil
					.nodeComsuption(SfcUtil.smallInstanceNodeComsuption);
			totalDelay2 = (processDelayWeight * processDelay2 + transDelayWeight * transDelay2);

			totalDelay = Math.max(totalDelay1, totalDelay2);
			break;
		case 6:
			// ��һ��·
			for (int i = 0; i < (3); i++) {
				tmpPathArray = Dijkstra.dijkstraPathArray(calculateFitnessDelayMatrix, geneSequence[i],
						geneSequence[i + 1]);
				for (int j = 0; j < tmpPathArray.length - 1; j++) {
					linkCost = SfcUtil.linkComsuption();
					this.smallNetwork.linksCapacity[tmpPathArray[j]][tmpPathArray[j + 1]] -= linkCost;
					this.smallNetwork.linksCapacity[tmpPathArray[j + 1]][tmpPathArray[j]] -= linkCost;
				}
				transDelay1 += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[i],
						geneSequence[i + 1]);
			}
			tmpPathArray = Dijkstra.dijkstraPathArray(calculateFitnessDelayMatrix, geneSequence[3], geneSequence[6]);
			transDelay1 += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[3], geneSequence[6]);
			for (int j = 0; j < tmpPathArray.length - 1; j++) {
				linkCost = SfcUtil.linkComsuption();
				this.smallNetwork.linksCapacity[tmpPathArray[j]][tmpPathArray[j + 1]] -= linkCost;
				this.smallNetwork.linksCapacity[tmpPathArray[j + 1]][tmpPathArray[j]] -= linkCost;
			}

			System.out
					.println("����VNFa��VNFb��VNFd�Ľڵ��ǣ�" + geneSequence[1] + " " + geneSequence[2] + " " + geneSequence[3]);
			for (int i = 0; i < 3; i++) {
				processDelay1 += SfcUtil.processDelay();
				this.smallNetwork.nodesCompute[geneSequence[i + 1]] -= SfcUtil
						.nodeComsuption(SfcUtil.smallInstanceNodeComsuption);
			}

			totalDelay1 = (processDelayWeight * processDelay1 + transDelayWeight * transDelay1);
			// �ڶ���·
			tmpPathArray = Dijkstra.dijkstraPathArray(calculateFitnessDelayMatrix, geneSequence[0], geneSequence[1]);
			transDelay2 += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[0], geneSequence[1]);
			for (int j = 0; j < tmpPathArray.length - 1; j++) {
				linkCost = SfcUtil.linkComsuption();
				this.smallNetwork.linksCapacity[tmpPathArray[j]][tmpPathArray[j + 1]] -= linkCost;
				this.smallNetwork.linksCapacity[tmpPathArray[j + 1]][tmpPathArray[j]] -= linkCost;
			}
			tmpPathArray = Dijkstra.dijkstraPathArray(calculateFitnessDelayMatrix, geneSequence[1], geneSequence[4]);
			transDelay2 += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[1], geneSequence[4]);
			for (int j = 0; j < tmpPathArray.length - 1; j++) {
				linkCost = SfcUtil.linkComsuption();
				this.smallNetwork.linksCapacity[tmpPathArray[j]][tmpPathArray[j + 1]] -= linkCost;
				this.smallNetwork.linksCapacity[tmpPathArray[j + 1]][tmpPathArray[j]] -= linkCost;
			}
			for (int i = 4; i < (6); i++) {
				tmpPathArray = Dijkstra.dijkstraPathArray(calculateFitnessDelayMatrix, geneSequence[i],
						geneSequence[i + 1]);
				for (int j = 0; j < tmpPathArray.length - 1; j++) {
					linkCost = SfcUtil.linkComsuption();
					this.smallNetwork.linksCapacity[tmpPathArray[j]][tmpPathArray[j + 1]] -= linkCost;
					this.smallNetwork.linksCapacity[tmpPathArray[j + 1]][tmpPathArray[j]] -= linkCost;
				}
				transDelay2 += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[i],
						geneSequence[i + 1]);
			}

			for (int i = 0; i < 3; i++) {
				processDelay2 += SfcUtil.processDelay();
			}
			System.out.println("����VNFc��VNFd�Ľڵ��ǣ�" + geneSequence[4] + " " + geneSequence[5]);
			this.smallNetwork.nodesCompute[geneSequence[4]] -= SfcUtil
					.nodeComsuption(SfcUtil.smallInstanceNodeComsuption);
			this.smallNetwork.nodesCompute[geneSequence[5]] -= SfcUtil
					.nodeComsuption(SfcUtil.smallInstanceNodeComsuption);
			totalDelay2 = (processDelayWeight * processDelay2 + transDelayWeight * transDelay2);

			totalDelay = Math.max(totalDelay1, totalDelay2);
			break;
		default:
			break;
		}
		System.out.print("��ʱ�ӣ�" + totalDelay + " " + "�ڵ���Դ���ģ�" + "" + "��·��Դ���ģ�" + " ");
		fitness = totalDelay * totalDelayWeight + totalCost * totalCostWeight;
		return fitness;
	}

	// �õ�WiNE��ı��Ž��
	public void getWiNEOrchestrationResult() {
		wineGeneration();

		System.out.print("SFC����ڵ�ӳ�������ڵ�����Ϊ��");
		for (int i = 0; i < chromLen; i++) {
			System.out.print(geneSequence[i] + " ");
		}
		System.out.println();
		System.out.println("��Ӧ��Ϊ�� " + getFitness());
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		SfcRequest chromosome = (SfcRequest) super.clone();
		chromosome.chromLen = this.chromLen;
		chromosome.geneSequence = this.geneSequence.clone();
		chromosome.fitness = this.fitness;
		return chromosome;
	}
}
