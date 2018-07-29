import java.util.*;
import java.io.*;

public class Main {
	public static void main(String[] args) {
		try {  
	        File file = new File("F:\\Musubi\\topo.txt");  
	        if (!file.exists()) {  
	            file.createNewFile();  
	        }  
	        FileWriter fw = new FileWriter(file);  
	        BufferedWriter bw = new BufferedWriter(fw);  
	        int k = 4;
	    	int cs = 0;
	    	int ce = (k / 2)*(k / 2) - 1;
	    	int as = ce + 1;
	    	int ae = as + (k * k / 2) - 1;
	    	int es = ae + 1;
	    	int ee = es + (k * k / 2) - 1;
	    	
	    	bw.write("{\\\"nodes\\\":[");
	        for(int i=0;i<=ee;i++){
	        	String str = null;
        		str=String.format("%s%d%s","\\\"s",i,"\\\",");
	        	bw.write(str);	
	        }
	        for(int i=0;i<(k*k*k/4);i++){
	        	String str = null;
	        	if(i == ((k*k*k/4)-1)){
	        		str=String.format("%s%d%s","\\\"h",i,"\\\"");
		        	bw.write(str);
	        	}
	        	else{
	        		str=String.format("%s%d%s","\\\"h",i,"\\\",");
		        	bw.write(str);
	        	}
	        }
	        bw.write("],");
	        bw.write("\\\"links\\\":[");
	    	//核心层与聚合层
	        for (int i = 0; i <= ce; i++){
	    		int tmp = i / (k / 2);
	    		for (int j = (as + tmp); j <= ae; j += (k / 2)){
	    			String str = null;
		        	str=String.format("%s%d%s%d%s","{\\\"left\\\":\\\"s", i, "\\\",\\\"right\\\":\\\"s",j,"\\\" },");
		        	bw.write(str);
	    		}
	    	}
	    	//聚合层与架顶层
	        for (int i = as; i <= ae; i++){
	    		int tmp = (i - as) / (k / 2);
	    		for (int j = es + tmp * (k / 2); j < es + tmp * (k / 2) + k / 2; j++){
	    			String str = null;
		        	str=String.format("%s%d%s%d%s","{\\\"left\\\":\\\"s", i, "\\\",\\\"right\\\":\\\"s",j,"\\\" },");
		        	bw.write(str);
	    		}
	    	}
	    	//架顶层与主机
	    	int hm = 0;
	    	for (int i = es; i <= ee;i++){
	    		for (int j = 0; j < k / 2;j++){
	    			String str = null;
	    			if(i==ee&&j==(k / 2 - 1)){
			        	str=String.format("%s%d%s%d%s","{\\\"left\\\":\\\"s", i, "\\\",\\\"right\\\":\\\"h",hm++,"\\\" }");
			        	bw.write(str);
	    			}
	    			else{
	    				str=String.format("%s%d%s%d%s","{\\\"left\\\":\\\"s", i, "\\\",\\\"right\\\":\\\"h",hm++,"\\\" },");
			        	bw.write(str);
	    			}
	    		}
	    	}
	    	bw.write("]}");
	        bw.flush();  
	        bw.close();  

	    } 
		catch (IOException e) {  
	        e.printStackTrace(); 
	    }  


		
    }
} 


