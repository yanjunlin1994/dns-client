import java.rmi.Naming;

public class client {
    public static void main(String args[]) throws InterruptedException{
        String askport = args[0];
        int asknodeID = Integer.valueOf(args[1]);
        String lookupName = "//localhost:" + askport + "/Listener" + asknodeID;
        try {
            ListenerIntf NodeListener = (ListenerIntf) Naming.lookup(lookupName);
            String reply = NodeListener.clientRequest("moximoxi");
            System.out.println("[Recieve reply] "+ reply);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }  

}
