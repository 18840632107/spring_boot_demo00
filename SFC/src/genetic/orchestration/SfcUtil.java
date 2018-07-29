package genetic.orchestration;

import java.util.*;

//SFC请求的工具类
public class SfcUtil {
	public static int smallInstanceNodeComsuption = 6;
	public static int bigInstanceNodeComsuption = 15;

	public static int processDelay() {
		Random r = new Random();
		int delay = r.nextInt(25) % (11) + 15;
		return delay;
	}

	public static int nodeComsuption(int instanceNodeComsuption) {
		Random r = new Random();
		int delay = 0;
		if (instanceNodeComsuption == 6) { // 小实例时计算资源消耗
			delay = r.nextInt(4) % (3) + 2;
		} else {// 大实例时计算资源消耗
			delay = r.nextInt(5) % (5) + 1;
		}
		return delay;
	}

	public static int linkComsuption() {
		Random r = new Random();
		int delay = r.nextInt(5) % (5) + 1;
		return delay;
	}

   public static void printSmallNetworkResource(){
	   System.out.println("物理节点的计算资源：");
	   int nodeRes = 0;
	   int nodeUsed = 0;
	   for(int i=0;i<SmallNetwork.nodeNum;i++){
		   if(main.smallNetwork.nodesCompute[i]!=100){
			   nodeUsed++;
			   nodeRes+=main.smallNetwork.nodesCompute[i];
		   }
		   System.out.print(main.smallNetwork.nodesCompute[i]+" ");
	   }
	   System.out.println("物理节点的计算资源使用率为：" + (SmallNetwork.nodeCompute*nodeUsed-nodeRes)/nodeUsed);
	   
	   System.out.println("物理链路的带宽资源：");
	   int linkRes = 0;
	   int linkUsed = 0;
	   for (int i = 0; i < SmallNetwork.nodeNum; i++) {
			for (int j = 0; j < SmallNetwork.nodeNum; j++) {
				if(main.smallNetwork.linksCapacity[i][j]!=100 &&main.smallNetwork.linksCapacity[i][j]>0){
					linkUsed++;
					linkRes+=main.smallNetwork.linksCapacity[i][j];
				}
				System.out.print(main.smallNetwork.linksCapacity[i][j]+" ");
			}
			System.out.println();
		}
	   System.out.println("物理链路的带宽资源使用率为：" + (SmallNetwork.linkCapacity*linkUsed-linkRes)/linkUsed);
   }
   
   public static void printMediumNetworkResource(){
	   System.out.println("物理节点的计算资源：");
	   int nodeRes = 0;
	   int nodeUsed = 0;
	   for(int i=0;i<MediumNetwork.nodeNum;i++){
		   if(main.mediumNetwork.nodesCompute[i]!=100){
			   nodeUsed++;
			   nodeRes+=main.mediumNetwork.nodesCompute[i];
		   }
		   System.out.print(main.mediumNetwork.nodesCompute[i]+" ");
	   }
	   System.out.println("物理节点的计算资源使用率为：" + (MediumNetwork.nodeCompute*nodeUsed-nodeRes)/nodeUsed);
	   
	   System.out.println("物理链路的带宽资源：");
	   int linkRes = 0;
	   int linkUsed = 0;
	   for (int i = 0; i < MediumNetwork.nodeNum; i++) {
			for (int j = 0; j < MediumNetwork.nodeNum; j++) {
				if(main.mediumNetwork.linksCapacity[i][j]!=100 &&main.mediumNetwork.linksCapacity[i][j]>0){
					linkUsed++;
					linkRes+=main.mediumNetwork.linksCapacity[i][j];
				}
				System.out.print(main.mediumNetwork.linksCapacity[i][j]+" ");
			}
			System.out.println();
		}
	   System.out.println("物理链路的带宽资源使用率为：" + (MediumNetwork.linkCapacity*linkUsed-linkRes)/linkUsed);
   }
   
   public static void printLargeNetworkResource(){
	   System.out.println("物理节点的计算资源：");
	   int nodeRes = 0;
	   int nodeUsed = 0;
	   for(int i=0;i<LargeNetwork.nodeNum;i++){
		   if(main.largeNetwork.nodesCompute[i]!=100){
			   nodeUsed++;
			   nodeRes+=main.largeNetwork.nodesCompute[i];
		   }
		   System.out.print(main.largeNetwork.nodesCompute[i]+" ");
	   }
	   System.out.println("物理节点的计算资源使用率为：" + (LargeNetwork.nodeCompute*nodeUsed-nodeRes)/nodeUsed);
	   
	   System.out.println("物理链路的带宽资源：");
	   int linkRes = 0;
	   int linkUsed = 0;
	   for (int i = 0; i < LargeNetwork.nodeNum; i++) {
			for (int j = 0; j < LargeNetwork.nodeNum; j++) {
				if(main.largeNetwork.linksCapacity[i][j]!=100 &&main.largeNetwork.linksCapacity[i][j]>0 ){
					linkUsed++;
					linkRes+=main.largeNetwork.linksCapacity[i][j];
				}
				System.out.print(main.largeNetwork.linksCapacity[i][j]+" ");
			}
			System.out.println();
		}
	   System.out.println("物理链路的带宽资源使用率为：" + (LargeNetwork.linkCapacity*linkUsed-linkRes)/linkUsed);
   }
}