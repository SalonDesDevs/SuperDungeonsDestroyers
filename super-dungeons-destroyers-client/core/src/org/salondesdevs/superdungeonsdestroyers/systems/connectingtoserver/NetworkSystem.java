package org.salondesdevs.superdungeonsdestroyers.systems.connectingtoserver;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import net.wytrem.ecs.*;

public class NetworkSystem extends BaseSystem {

    private Socket clientSocket;

    @Override
    public void initialize() {
        this.clientSocket = Gdx.net.newClientSocket(Net.Protocol.TCP, "localhost", 9000, new SocketHints());
    }

    @Override
    public void process() {
        // Send enqueued packets, ...

//        try {
//            this.clientSocket.getOutputStream().flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
