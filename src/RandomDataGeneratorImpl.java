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
        if (dataList.isEmpty()) return new int[0];
        int totalLength = dataList.stream().mapToInt(arr -> arr.length).sum();
        int[] combinedArray = new int[totalLength];
        int index = 0;
        for (int[] array : dataList) {
            System.arraycopy(array, 0, combinedArray, index, array.length);
            index += array.length;
        }
        return combinedArray;
    }

    @Override
    public void clearData() throws RemoteException {
        dataList.clear();
    }
}
