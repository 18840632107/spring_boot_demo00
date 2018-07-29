package caiyibin.hello.main;

import java.util.Random;

public class Deployment {

	public static String getDst() {
		Random r = new Random();
		int Idst = r.nextInt(SCNclass.numofMEOSatellites);
		String dst = Integer.toString(Idst);
		if (Idst < 10) {
			dst = "0" + dst;
		}
		return dst;
	}

	public static void deploySFC() {
		// ���Դ��Ŀ�ĵ�·������¼
		String src = "27";
		String dst = getDst();
		int[] retpath = new int[40];
		// ����ĳһ���͵�SFC���в���ʵ����Branch ��Loop �ڳ�����һ������������
		Random r = new Random();
		int sfcType = r.nextInt(3);
		double oneDelay = 0.0;
		switch (sfcType) {
		case 0:
			System.out.println("Linear Form of SFC");
			oneDelay += SCNclass.deployOnHUB(SCNclass.HG);
			oneDelay += SCNclass.deployOnLEO(SCNclass.satL);

			dst = getDst();
			retpath = new int[40];
			oneDelay += Dijkstra.printPathInfo(src, dst, retpath);
			oneDelay += SCNclass.deployOnMEO(SCNclass.satM, retpath);
			break;
		case 1:
			System.out.println("Branch Form of SFC");
			oneDelay += SCNclass.deployOnHUB(SCNclass.HG);
			oneDelay += SCNclass.deployOnLEO(SCNclass.satL);

			double meoDst1 = 0.0;
			double meoDst2 = 0.0;
			dst = getDst();
			retpath = new int[40];
			meoDst1 += Dijkstra.printPathInfo(src, dst, retpath);
			meoDst1 += SCNclass.deployOnMEO(SCNclass.satM, retpath);

			dst = getDst();
			retpath = new int[40];
			meoDst2 += Dijkstra.printPathInfo(src, dst, retpath);
			meoDst2 += SCNclass.deployOnMEO(SCNclass.satM, retpath);

			System.out.println("��MEO�ϲ���VNF����ʱ��Ϊ��" + Math.max(meoDst1, meoDst2) + "ms");
			oneDelay += Math.max(meoDst1, meoDst2);
			break;
		case 2:
			System.out.println("Loop Form of SFC");
			oneDelay += SCNclass.deployOnHUB(SCNclass.HG);
			oneDelay += SCNclass.deployOnLEO(SCNclass.satL);

			double meoDst11 = 0.0;
			double meoDst21 = 0.0;
			dst = getDst();
			retpath = new int[40];
			meoDst11 += Dijkstra.printPathInfo(src, dst, retpath);
			meoDst11 += SCNclass.deployOnMEO(SCNclass.satM, retpath);

			dst = getDst();
			retpath = new int[40];
			meoDst21 += Dijkstra.printPathInfo(src, dst, retpath);
			meoDst21 += SCNclass.deployOnMEO(SCNclass.satM, retpath);

			System.out.println("��MEO�ϲ���VNF����ʱ��Ϊ��" + Math.max(meoDst11, meoDst21) + "ms");
			oneDelay += Math.max(meoDst11, meoDst21);
			break;
		default:
			break;
		}
		System.out.println("�˴�SFC�Ĳ������ʱ��Ϊ�� " + oneDelay + " ms");
		Result.averageDelay += oneDelay;
	}

}
