package shujuku;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class QueryUtil {
	public void findUserInfo(String[] usernames, int strategy) throws Exception {  
		//ȷ�����ݿ�����
		Class.forName("com.mysql.jdbc.Driver");  
		//���ӵ����ݿ⼰�˿ڣ��û���������
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root",  
                "123456");  
        //��������
        Statement stat = conn.createStatement();  
        //�������
        StringBuilder sql = new StringBuilder("select * from user_info where ");  
        //����ģʽ�������һ�µ��ǲ�ͬ���㷨��װ
        
        /*��findUserInfo�����ϼ���һ��������strategyֵΪ1��ʾʹ�õ�һ���㷨��strategyֵΪ2��ʾʹ�õڶ����㷨��
         * �ڶ����㷨ʹ����һ�����������������Ƿ���Ҫ�Ӹ�or����ؼ��֣�
         * ��һ��ִ��forѭ����ʱ����Ϊ�ò���ֵΪfalse��
         * ���Բ������or����ѭ������󽫲���ֵ��ֵΪtrue�������Ժ�ѭ��ÿ�ζ�����ͷ������һ��or�ؼ��֣�
         * ����ʹ����ͷ�����or�ķ��������Բ����ٵ���SQL����β������һ��or����
         */
        //Ϊ�˴���ɶ�����չ����Щ���Կ��Է�װΪ����
        if (strategy == 1) {  
            for (String user : usernames) {  
                sql.append("username = '");  
                sql.append(user);  
                sql.append("' or ");  
            }  
            sql.delete(sql.length() - " or ".length(), sql.length());  
        } else if (strategy == 2) {  
            boolean needOr = false;  
            for (String user : usernames) {  
                if (needOr) {  
                    sql.append(" or ");  
                }  
                sql.append("username = '");  
                sql.append(user);  
                sql.append("'");  
                needOr = true;  
            }  
        }  
        System.out.println(sql);  
        ResultSet resultSet = stat.executeQuery(sql.toString());  
        while (resultSet.next()) {  
            // ��������ݿ������������  
        }  
        // ����Ӧ��������������װ�ɶ��󷵻أ�������ȥ��    
    }  
}
