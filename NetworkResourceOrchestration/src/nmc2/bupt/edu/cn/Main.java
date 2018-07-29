package nmc2.bupt.edu.cn;

import java.util.ArrayList;
import java.util.Random;

public class Main {
	public static void main(String[] args) {
		/*
		// ������ʹ��RECM�࣬�ṩ������demo��
		// 1. ����ֻ��SFC�������������е�SFC�趨��ͬ������ʱ��
		new RECM(RECM.numOfSfc).Scaling(new Random().nextInt(350) * 1.0);
		// 2. ����ΪSFC�ı�����������Ե���SFC�趨����ʱ��
		new RECM(RECM.numOfSfc).Scaling(1, new Random().nextInt(350) * 1.0);
		// ������Ե�ʱ��SFC������ʱ����������ɵģ������Լ�����ʵ������趨����ʱ�����õ��ܴ󣨱�����ڵ���1100֮�ࣩ���㷨����Ϊ���񱻳���
		*/
		//���ڵ���SFC�����ģ�����������������ݲ�������
		DataCenterNetwork dataCenterNetwork = new DataCenterNetwork(4);
	    SFCrequest sfCrequest = new SFCrequest(0, 3);
	    SFCUtil.orchestrationInit(dataCenterNetwork, sfCrequest);
	    SFCUtil.Scaling(dataCenterNetwork, sfCrequest, 50);    //�ɲ���Ϊ��SFC����ʱ���źţ�ʹ֮���������ݲ���
	    ArrayList<SFCrequest>sfcList = new ArrayList<>();      //д�ļ���Ҫ����SFC�������Խ������ֻ��һ��SFCҲ��һ������
	    sfcList.add(sfCrequest);
	    FileOperation.writeFile(dataCenterNetwork, sfcList);
	}
	//������һЩ���õĲ�ѯ���
	public void someUsefulInfo() {
		/* �������
		DataCenterNetwork dataCenterNetwork = new DataCenterNetwork(4);
		for (int i =0;i<SFCNUM;i++) {
			SFCrequest sfCrequest = new SFCrequest(i, 4);
			SFCUtil.orchestrationInit(dataCenterNetwork, sfCrequest);
			SFCUtil.getOrchestrationResult(dataCenterNetwork, sfCrequest);
		}
		//�����ʼ������
		SFCrequest sfCrequest = new SFCrequest(1, 2);
		DynamicOptimization dynamicOptimization = new DynamicOptimization();
		DataCenterNetwork dataCenterNetwork = new DataCenterNetwork(2);
		dynamicOptimization.orchestrationInit(dataCenterNetwork, sfCrequest);
		//��ȡSFC��Ϣ
		SFCrequest sfCrequest = new SFCrequest(1, 2);
		for (int i = 0; i < sfCrequest.getNumOfVNF(); i++) {
			System.out.println(sfCrequest.getVnfFG().get(i).getTypeId());
		}
		//��ȡ��������Ϣ
		DataCenterNetwork dataCenterNetwork = new DataCenterNetwork(2);
		//��ȡ��������Ϣ
		for (int j = 0; j < 16; j++) {
			for (int i = 0; i < 4; i++) {
				System.out.println(dataCenterNetwork.getServerList().get(i).getCpuCoreRes(i));
				System.out.println(dataCenterNetwork.getServerList().get(i).getVnfTypeInCore(i));
			}
		}
		//��ȡ��·��Ϣ
		for (int i = 0; i < dataCenterNetwork.getAllNodes(); i++) {
			for (int j = 0; j < dataCenterNetwork.getAllNodes(); j++) {
				System.out.print(dataCenterNetwork.getLinksBandWidth()[i][j]+"              ");
			}
			System.out.println();
		}
		//�鿴�������
		for (int i = 0; i < sfCrequest.getNumOfVNF(); i++) {
			System.out.println("SFC��"+"vnf "+i +"������Ϊ��"+sfCrequest.getVnfFG().get(i).getTypeId());
			System.out.println("�����ڷ�����"+sfCrequest.getVnfFG().get(i).getServerId() + "��");
		}
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 4; j++) {
				System.out.println("������"+i+"�Ϻ���"+j+"�����SFC���Ϊ��"+dataCenterNetwork.getServerList().get(i).getSfcIdInCore(j));
				System.out.println("������"+i+"�Ϻ���"+j+"�����VNF����Ϊ��"+dataCenterNetwork.getServerList().get(i).getVnfTypeInCore(j));
				System.out.println("������"+i+"�Ϻ���"+j+"��ԴΪ��"+dataCenterNetwork.getServerList().get(i).getCpuCoreRes(j));
			}
		}
		 */
	}
}