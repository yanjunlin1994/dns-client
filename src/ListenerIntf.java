import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ListenerIntf extends Remote {
    public String clientRequest(DNSEntry dnsentrys) throws RemoteException;
}
