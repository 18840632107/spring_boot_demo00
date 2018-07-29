package caiyibin.main;

import java.util.Vector;

public class ChatManager {
   private ChatManager(){}
   private static final ChatManager cm = new ChatManager();
   public static ChatManager getChatManager(){
	   return cm;
   }
   Vector<ChatManager> vector = new Vector<ChatManager>();
   public void add(ChatManager cs){
	   vector.add(cs);
   }
   public void publish(ChatSocket cs,String out){
	   for(int i = 0; i<vector.size();i++)
	   {
		   ChatSocket csChatSocket = vector.get(i);
		   if(cs.equals(csChatSocket)){
			   csChatSocket.out(out);
		   }
	   }
   }

}
