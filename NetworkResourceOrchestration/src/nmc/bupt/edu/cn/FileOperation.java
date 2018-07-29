package nmc.bupt.edu.cn;

import java.io.*;
import java.util.ArrayList;

public class FileOperation {
	//д�ļ����������������
	public static void writeFile(ArrayList<ArrayList<Integer> > deployResAll, double[] delay,double averageDelay,int instancesNum, double resourceUtilization){
		try {  
	        File file = new File("F:\\temp.txt");    //��������޸Ĵ洢λ�ã�
	        if (!file.exists()) {  
	            file.createNewFile();  
	        }  
	        FileWriter fw = new FileWriter(file);  
	        BufferedWriter bw = new BufferedWriter(fw);  
	        
	        bw.write("{\r\n");
	        bw.write("  \"Orchestration\": {\r\n");
	        bw.write("    \"SFC requests\": [\r\n");
	        for(int i=0;i<deployResAll.size();i++){  //SFC������
	        	bw.write("      {\r\n");
	        	bw.write("        \"id\": " +"\""+ i+"\""+","+"\r\n");
	        	for(int j=0;j<deployResAll.get(i).size();j++){  //VNF���
	        		bw.write("        \"VNF"+Integer.toString(j)+"\""+": " +"\""+deployResAll.get(i).get(j)+"\""+","+"\r\n");
	        	}
	        	bw.write("        \"delay\": " +"\""+ delay[i]+"\""+"\r\n");
	        	if(i==deployResAll.size())  		bw.write("      } \r\n");
	        	else           									bw.write("      },\r\n");
	        }
	        bw.write("    ],\r\n");
	        bw.write("    \"averageDelay\":" +"\""+averageDelay+"\""+","+"\r\n");
	        bw.write("    \"instancesNum\":" +"\""+instancesNum+"\""+","+"\r\n");
	        bw.write("    \"resourceUtilization\":" +"\""+resourceUtilization+"\""+"\r\n");
	        bw.write("  }\r\n");
	        bw.write("}\r\n");
	        
	        bw.flush();  
	        bw.close();  
	    } 
		catch (IOException e) {  
	        e.printStackTrace(); 
	    }  
	}	
}
