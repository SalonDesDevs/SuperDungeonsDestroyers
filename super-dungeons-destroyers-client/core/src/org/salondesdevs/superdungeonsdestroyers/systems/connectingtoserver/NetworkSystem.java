package org.salondesdevs.superdungeonsdestroyers.systems.connectingtoserver;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import net.wytrem.ecs.*;

import java.io.IOException;
import java.io.InputStream;

public class NetworkSystem extends BaseSystem {

    private Socket clientSocket;

    @Override
    public void initialize() {
        this.clientSocket = Gdx.net.newClientSocket(Net.Protocol.TCP, "localhost", 9000, new SocketHints());

        new Thread("Socket listener") {
            @Override
            public void run() {

                InputStream inputStream = clientSocket.getInputStream();

                long size;
                byte[] sizeBuffer = new byte[4];
                byte[] buffer = new byte[1024];

                try {

                    while (true) {
                        if (inputStream.read(sizeBuffer) != 1) {
                            size = 0;
                            for (int i = 0; i < 4; i++) {
                                size <<= 8;
                                size |= (sizeBuffer[i] & 0xFF);
                            }

//                            System.out.println("size = ");

                            if (buffer.length < size) {
                                buffer = new byte[(int) size];
                            }

                            inputStream.read(buffer, 0, (int) size);

                        }

                    }
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }

            }
        }.start();
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
