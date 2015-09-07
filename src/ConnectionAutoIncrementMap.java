import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by User on 05.09.2015.
 */
public class ConnectionAutoIncrementMap extends HashMap<Integer, TcpServerSocketProcessor> {
    public int connectionCounter;

    public ConnectionAutoIncrementMap() {
        this.connectionCounter = 0;
    }

    public int getNextId() {
        return connectionCounter++;
    }

    public void pushConnection(int connectionId, TcpServerSocketProcessor tcpServerSocketProcessor) {
        this.put(connectionId, tcpServerSocketProcessor);
    }

    public Iterable<TcpServerSocketProcessor> getAllExceptOne(int exceptConnectionId) {
        int resultSize = this.size() - 1;
        if (resultSize < 1)
            return new ArrayList<TcpServerSocketProcessor>(0);

        ArrayList<TcpServerSocketProcessor> result = new ArrayList<TcpServerSocketProcessor>(resultSize);

        Set<Entry<Integer, TcpServerSocketProcessor>> set = this.entrySet();
        for ( Map.Entry<Integer, TcpServerSocketProcessor> s : set ) {
            Integer key = s.getKey();

            if(key != exceptConnectionId){
                result.add(s.getValue());
            }
        }

        return result;
    }
}
