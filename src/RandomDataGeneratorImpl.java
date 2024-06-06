import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class RandomDataGeneratorImpl extends UnicastRemoteObject implements RandomDataGenerator {
    private List<int[]> dataList;

    protected RandomDataGeneratorImpl() throws RemoteException {
        dataList = new ArrayList<>();
    }

    @Override
    public void addArray(int[] data) throws RemoteException {
        dataList.add(data);
    }

    @Override
    public int[] combineArrays() throws RemoteException {
        // Combine arrays, but make sure to handle the logic to avoid unintended combining
        if (dataList.isEmpty()) return new int[0];
        return dataList.get(dataList.size() - 1); // Return only the last added array for simplicity
    }

    @Override
    public void clearData() throws RemoteException {
        dataList.clear();
    }
}
