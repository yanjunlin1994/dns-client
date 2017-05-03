import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Random;

public class Client {
    public static void main(String args[]) throws InterruptedException{
        String reply;
        int nodeNum = 0, sample = 0, count = 0;
        HashMap<Integer, String> lookUpName = new HashMap<Integer, String>();
        HashMap<Integer, ListenerIntf> registrationMap = new HashMap<Integer, ListenerIntf>();
        String[] list = null;
        try {
        	/* read node id and registration from 'configuration' file */
			File file = new File("configuration");
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
			/* Number of nodes in the system */
			nodeNum = Integer.valueOf(bufferedReader.readLine());
			System.out.println(nodeNum);
			count = 0;
			while (count < nodeNum) {
				line = bufferedReader.readLine();
				list = line.split(",");
				System.out.println(list[0] + "," + list[1]);
				lookUpName.put(Integer.valueOf(list[0]), name(list[1], list[0]));
				count = count + 1;
			}
			sample = Integer.valueOf(bufferedReader.readLine());
			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        /* add node's registration information in registrationMap */
        lookUpName.forEach((node, uri)-> {
        	try {
				registrationMap.put(node, (ListenerIntf) Naming.lookup(uri));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (NotBoundException e) {
				e.printStackTrace();
			}
        });
        count = 0;
        try {
			File file = new File("us.csv");
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
			int requestNode;
			Random random = new Random();
			while ((line = bufferedReader.readLine()) != null && count < sample) {
				list = line.split(",");
				if (list.length < 2 || list[1].length() == 0 || list[1].length() > DNSEntry.DNS_MAXLENGTH || list[0].length() > DNSEntry.IP_MAXLENGTH) {
					continue;
				} else {
					count = count + 1;
				}
				System.out.println(list[0] + "," + list[1]);
				requestNode = (int)(random.nextFloat() * nodeNum);
				try {
					reply = registrationMap.get(requestNode).clientRequest(new DNSEntry(list[0],list[1]));
					System.out.println(reply);
				} catch(RemoteException e) {
					System.out.println("server " + requestNode + " loses connection");
				}
			}
			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }  
    private static String name(String port, String node) {
    	String lookupName = "//localhost:" + port + "/Listener" + node; 
    	return lookupName;
    }
}
