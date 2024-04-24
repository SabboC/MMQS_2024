package fi.vm.sade.kayttooikeus.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.*;

public class FreePortUtil {
    private static final Logger logger = LoggerFactory.getLogger(FreePortUtil.class);
    
    private static final int RESERVE_PORTS = 3;
    private static Stack<Integer> freePorts;
    private static final Map<String,Integer> assignedPortsByProperty = new HashMap<>();
    
    private FreePortUtil() {
    }
    
    public static Integer portNumberBySystemPropertyOrFree(String bySystemProperty) {
        Integer port = assignedPortsByProperty.get(bySystemProperty);
        if (port != null) {
            return port;
        }
        synchronized (assignedPortsByProperty) {
            Object systemPropertyPort = assignedPortsByProperty.get(bySystemProperty);
            if (systemPropertyPort != null) {
                port = Integer.parseInt(systemPropertyPort.toString());
                logger.info("Port set by system property {} -> {}", bySystemProperty, port);
                assignedPortsByProperty.put(bySystemProperty, port);
                return port;
            }
            if (freePorts == null) {
                try {
                    freePorts = getFreePorts(RESERVE_PORTS);
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }
            if (freePorts.isEmpty()) {
                throw new IllegalStateException("No more reserved random prosts. Please increase "
                        + FreePortUtil.class.getCanonicalName() +".RESERVE_PORTS="+RESERVE_PORTS);
            }
            port = freePorts.pop();
            logger.info("Reserved free port by undefined system property {} -> {}", bySystemProperty, port);
            assignedPortsByProperty.put(bySystemProperty, port);
            return port;
        }
    }
    
    private static Stack<Integer> getFreePorts(int numberOfPorts) throws IOException {
        Stack<Integer> portNumbers = new Stack<>();
        List<ServerSocket> servers = new ArrayList<>(numberOfPorts);
        ServerSocket tempServer;
        for (int i = 0; i< numberOfPorts; i++) {
            try {
                tempServer = new ServerSocket(0);
                servers.add(tempServer);
                portNumbers.add(tempServer.getLocalPort());
            } finally {
                for (ServerSocket server : servers) {
                    try {
                        server.close();
                    } catch (IOException e) {
                        // Continue closing servers.
                    }
                }
            }
        }
        return portNumbers;
    }
    
}
