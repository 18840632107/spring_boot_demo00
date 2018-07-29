package caiyibin.hello.main;

public class Result {
	public static int testNum = 30;
	public static double averageDelay = 0.0;

	public static void acceptanceRatio() {
		System.out.println("�˴�SFC��GW�ϲ���ĳɹ���Ϊ�� " + (double) (testNum - SCNclass.failGW) / testNum);
		System.out.println("�˴�SFC��LEO�ϲ���ĳɹ���Ϊ�� " + (double) (testNum - SCNclass.failLEO) / testNum);
		System.out.println("�˴�SFC��MEO�ϲ���ĳɹ���Ϊ�� " + (double) (testNum - SCNclass.failMEO) / testNum);
	}

	public static void resourceUtilizationUsed() {
		int countCPU = 0;
		int countMemory = 0;
		int countStorage = 0;

		double GWCPU = 0.0;
		double GWMemory = 0.0;
		double GWStorage = 0.0;
		double GWbandWidthHL = 0.0;
		int countbandWidthHL = 0;
		for (int i = 0; i < SCNclass.numofGW; i++) {
			if (SCNclass.HG[i].CPU < 100.0) {
				countCPU++;
				GWCPU += SCNclass.HG[i].CPU;
			}
			if (SCNclass.HG[i].Memory < 100.0) {
				countMemory++;
				GWMemory += SCNclass.HG[i].Memory;
			}
			if (SCNclass.HG[i].Storage < 100.0) {
				countStorage++;
				GWStorage += SCNclass.HG[i].Storage;
			}
			if (SCNclass.HG[i].bandWidthHL < 100.0) {
				countbandWidthHL++;
				GWbandWidthHL += SCNclass.HG[i].bandWidthHL;
			}
		}
		System.out.println("GW��CPU����Դ������Ϊ��" + (double) (100.0 - GWCPU / countCPU));
		System.out.println("GW��Memory����Դ������Ϊ��" + (double) (100.0 - GWMemory / countMemory));
		System.out.println("GW��Storage����Դ������Ϊ��" + (double) (100.0 - GWStorage / countStorage));
		System.out.println("GW�ϴ������Դ������Ϊ��" + (double) (100.0 - GWbandWidthHL / countbandWidthHL));

		double LEOCPU = 0.0;
		double LEORadio = 0.0;
		double LEObandWidthLL = 0.0;
		countCPU = 0;
		int countRadio = 0;
		int countbandWidthLL = 0;
		for (int i = 0; i < SCNclass.numofLEOSatellites - 1; i++) {
			if (SCNclass.satL[i].CPU < 100.0) {
				countCPU++;
				LEOCPU += SCNclass.satL[i].CPU;
			}
			if (SCNclass.satL[i].Radio < 100.0) {
				countRadio++;
				LEORadio += SCNclass.satL[i].Radio;
			}
			if (SCNclass.satL[i].bandWidthLL < 100.0) {
				countbandWidthLL++;
				LEObandWidthLL += SCNclass.satL[i].bandWidthLL;
			}
		}
		System.out.println("LEO��CPU����Դ������Ϊ��" + (double) (100.0 - LEOCPU / countCPU));
		System.out.println("LEO��Radio����Դ������Ϊ��" + (double) (100.0 - LEORadio / countRadio));
		System.out.println("LEO��LL�������Դ������Ϊ��" + (double) (100.0 - LEObandWidthLL / countbandWidthLL));
		System.out.println(
				"LEO��LM�������Դ������Ϊ��" + (double) (100.0 - SCNclass.satL[SCNclass.numofLEOSatellites - 1].bandWidthLM));

		double MEOCPU = 0.0;
		double MEOMemory = 0.0;
		double MEOStorage = 0.0;
		double MEObandWidthMM = 0.0;
		countCPU = 0;
		countMemory = 0;
		countStorage = 0;
		int countbandWidthMM = 0;
		for (int i = 0; i < SCNclass.numofMEOSatellites; i++) {
			if (SCNclass.satM[i].CPU < 100.0) {
				countCPU++;
				MEOCPU += SCNclass.satM[i].CPU;
			}
			if (SCNclass.satM[i].Memory < 100.0) {
				countMemory++;
				MEOMemory += SCNclass.satM[i].Memory;
			}
			if (SCNclass.satM[i].Storage < 100.0) {
				countStorage++;
				MEOStorage += SCNclass.satM[i].Storage;
			}
		}
		for (int i = 0; i < SCNclass.numofMEOSatellites; i++) {
			for (int j = i + 1; j < SCNclass.numofMEOSatellites; j++) {
				if (SCNclass.MEOpathBW[i][j] >= 0 && SCNclass.MEOpathBW[i][j] < 100.0) {
					countbandWidthMM++;
					MEObandWidthMM += SCNclass.MEOpathBW[i][j];
				}
			}
		}
		System.out.println("MEO��CPU����Դ������Ϊ��" + (double) (100.0 - MEOCPU / countCPU));
		System.out.println("MEO��Memory����Դ������Ϊ��" + (double) (100.0 - MEOMemory / countMemory));
		System.out.println("MEO��Storage����Դ������Ϊ��" + (double) (100.0 - MEOStorage / countStorage));
		System.out.println("MEO�ϴ������Դ������Ϊ��" + (double) (100.0 - MEObandWidthMM / countbandWidthMM));
	}

	public static void resourceUtilization() {
		double GWCPU = 0.0;
		double GWMemory = 0.0;
		double GWStorage = 0.0;
		double GWbandWidthHL = 0.0;
		for (int i = 0; i < SCNclass.numofGW; i++) {
			GWCPU += SCNclass.HG[i].CPU;
			GWMemory += SCNclass.HG[i].Memory;
			GWStorage += SCNclass.HG[i].Storage;
			GWbandWidthHL += SCNclass.HG[i].bandWidthHL;
		}
		System.out.println("GW��CPU����Դ������Ϊ��" + (double) (100.0 - GWCPU / SCNclass.numofGW));
		System.out.println("GW��Memory����Դ������Ϊ��" + (double) (100.0 - GWMemory / SCNclass.numofGW));
		System.out.println("GW��Storage����Դ������Ϊ��" + (double) (100.0 - GWStorage / SCNclass.numofGW));
		System.out.println("GW�ϴ������Դ������Ϊ��" + (double) (100.0 - GWbandWidthHL / SCNclass.numofGW));

		double LEOCPU = 0.0;
		double LEORadio = 0.0;
		double LEObandWidthLL = 0.0;
		for (int i = 0; i < SCNclass.numofLEOSatellites - 1; i++) {
			LEOCPU += SCNclass.satL[i].CPU;
			LEORadio += SCNclass.satL[i].Radio;
			LEObandWidthLL += SCNclass.satL[i].bandWidthLL;
		}
		System.out.println("LEO��CPU����Դ������Ϊ��" + (double) (100.0 - LEOCPU / (SCNclass.numofLEOSatellites - 1)));
		System.out.println("LEO��Radio����Դ������Ϊ��" + (double) (100.0 - LEORadio / (SCNclass.numofLEOSatellites - 1)));
		System.out.println("LEO��LL�������Դ������Ϊ��" + (double) (100.0 - LEObandWidthLL / (SCNclass.numofLEOSatellites - 1)));
		System.out.println("LEO��LM�������Դ������Ϊ��" + (double) (100.0 - SCNclass.satL[4].bandWidthLM));

		double MEOCPU = 0.0;
		double MEOMemory = 0.0;
		double MEOStorage = 0.0;
		double MEObandWidthMM = 0.0;
		for (int i = 0; i < SCNclass.numofMEOSatellites; i++) {
			MEOCPU += SCNclass.satM[i].CPU;
			MEOMemory += SCNclass.satM[i].Memory;
			MEOStorage += SCNclass.satM[i].Storage;
		}
		for (int i = 0; i < SCNclass.numofMEOSatellites; i++) {
			for (int j = i + 1; j < SCNclass.numofMEOSatellites; j++) {
				if (SCNclass.MEOpathBW[i][j] >= 0 && SCNclass.MEOpathBW[i][j] < 100.0) {
					MEObandWidthMM += SCNclass.MEOpathBW[i][j];
				}
			}
		}
		System.out.println("MEO��CPU����Դ������Ϊ��" + (double) (100.0 - MEOCPU / SCNclass.numofMEOSatellites));
		System.out.println("MEO��Memory����Դ������Ϊ��" + (double) (100.0 - MEOMemory / SCNclass.numofMEOSatellites));
		System.out.println("MEO��Storage����Դ������Ϊ��" + (double) (100.0 - MEOStorage / SCNclass.numofMEOSatellites));
		System.out.println("MEO�ϴ������Դ������Ϊ��" + (double) (100.0 - MEObandWidthMM / SCNclass.numofMEOSatellites));
	}

	public static void printDelay() {
		System.out.println("�˴����в���SFC�Ĳ������ʱ��Ϊ�� " + averageDelay + " ms");
		System.out.println("�˴����в���SFC�Ĳ����ƽ��ʱ��Ϊ�� " + averageDelay / testNum + " ms");
	}
}
