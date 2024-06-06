import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RandomDataGenerator extends Remote {
    void addArray(int[] data) throws RemoteException;

    int[] combineArrays() throws RemoteException;

    void clearData() throws RemoteException;

    void clearCombinedArrays() throws RemoteException;
}
