package nmc2.bupt.edu.cn;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileOperation {
	// 负责JSON的输入输出
	public static void writeFile(DataCenterNetwork DCN, ArrayList<SFCrequest> sfcList) {
		try {
			File file = new File("F:\\temp.txt"); // 这里可以修改存储位置！
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);

			bw.write("{\r\n");
			bw.write("    \"Optimization\": [\r\n");
			for (int sfcId = 0; sfcId < sfcList.size(); sfcId++) {
				bw.write("      {\r\n");
				bw.write("        \"sfcId\":" + sfcId + ",\r\n");
				bw.write("        \"VNF\":" + "[\r\n");
				for (int vnfId = 0; vnfId < sfcList.get(sfcId).getNumOfVNF(); vnfId++) {
					bw.write("      {\r\n");
					bw.write("          \"vnfId\":" + vnfId + ",\r\n");
					bw.write("          \"serverId\":" + sfcList.get(sfcId).getVnfFG().get(vnfId).getServerId()
							+ ",\r\n");
					if (sfcList.get(sfcId).getVnfFG().get(vnfId).getCopyVNF() != null) {
						bw.write("          \"serverIdOfVnfCpoy\":"
								+ sfcList.get(sfcId).getVnfFG().get(vnfId).getCopyVNF().getServerId() + ",\r\n");
					} else {
						bw.write("          \"serverIdOfVnfCpoy\":" + "null" + "\r\n");
					}
					if (vnfId != sfcList.get(sfcId).getNumOfVNF() - 1) {
						bw.write("      },\r\n");
					} else {
						bw.write("      }\r\n");
					}
				}
				bw.write("        ]\r\n");
				if (sfcId != sfcList.size() - 1) {
					bw.write("      },\r\n");
				} else {
					bw.write("      }\r\n");
				}
			}
			bw.write("    ]\r\n");
			bw.write("}\r\n");

			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
