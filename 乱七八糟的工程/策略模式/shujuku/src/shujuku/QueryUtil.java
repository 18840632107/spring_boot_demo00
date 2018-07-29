package shujuku;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class QueryUtil {
	public void findUserInfo(String[] usernames, int strategy) throws Exception {  
		//确定数据库类型
		Class.forName("com.mysql.jdbc.Driver");  
		//连接的数据库及端口，用户名、密码
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root",  
                "123456");  
        //建立链接
        Statement stat = conn.createStatement();  
        //操作语句
        StringBuilder sql = new StringBuilder("select * from user_info where ");  
        //策略模式，将结果一致但是不同的算法封装
        
        /*在findUserInfo方法上加了一个参数，strategy值为1表示使用第一种算法，strategy值为2表示使用第二种算法。
         * 第二种算法使用了一个布尔变量来控制是否需要加个or这个关键字，
         * 第一次执行for循环的时候因为该布尔值为false，
         * 所以不会加上or，在循环的最后将布尔值赋值为true，这样以后循环每次都会在头部加上一个or关键字，
         * 由于使用了头部添加or的方法，所以不用再担心SQL语句的尾部会多出一个or来。
         */
        //为了代码可读与扩展，这些策略可以封装为对象
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
            // 处理从数据库读出来的数据  
        }  
        // 后面应将读到的数据组装成对象返回，这里略去。    
    }  
}
