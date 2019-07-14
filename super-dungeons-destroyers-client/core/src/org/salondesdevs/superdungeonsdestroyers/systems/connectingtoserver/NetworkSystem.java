package org.salondesdevs.superdungeonsdestroyers.systems.connectingtoserver;

import SDD.Request.Action;
import SDD.Request.Ping;
import SDD.Request.Request;
import SDD.Request.Task;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.net.NetJavaSocketImpl;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.google.flatbuffers.FlatBufferBuilder;
import net.wytrem.ecs.*;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NetworkSystem extends BaseSystem {

    private Socket clientSocket;

    private List<Request> requests = new ArrayList<>();

    private ListenThread listenThread;

    @Override
    public void initialize() {
        this.clientSocket = Gdx.net.newClientSocket(Net.Protocol.TCP, "localhost", 9000, new SocketHints());

        this.listenThread = new ListenThread();
        this.listenThread.run();
        batch = new SpriteBatch();
        font = new BitmapFont();
    }

    SpriteBatch batch;

    BitmapFont font;


    float remaining = 4;

    boolean test = true;

    @Override
    public void begin() {
        batch.begin();
    }

    @Override
    public void process() {
        // Send enqueued packets, ...

        remaining -= world.getDelta();

        if (remaining > 0)
            font.draw(batch, "Sending in " + remaining, 100, 100);

        if (remaining < 0 && test) {
            System.err.println("SENT");
            test = false;
            FlatBufferBuilder builder = new FlatBufferBuilder();
            int request = createRequest(builder);

            builder.finish(request);
            ByteBuffer byteBuffer = builder.dataBuffer();

            DataOutputStream dataOutputStream = new DataOutputStream(this.clientSocket.getOutputStream());

            try {
                dataOutputStream.writeInt(byteBuffer.remaining());
                WritableByteChannel channel = Channels.newChannel(dataOutputStream);

                System.err.println("Wrote " + channel.write(byteBuffer) + " bytes");
                dataOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        if (remaining < -3 && this.clientSocket.isConnected()) {
            this.close();
        }

//        try {
//            this.clientSocket.getOutputStream().flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public void close() {
        System.err.println("CLOSED");
        this.listenThread.isRunning = false;
        this.clientSocket.dispose();
    }

    @Override
    public void end() {
        batch.end();
    }

    public int createRequest(FlatBufferBuilder builder) {
        int ping = Ping.createPing(builder, (byte) 28);

        Task.startTask(builder);
        Task.addActionType(builder, Action.Ping);
        Task.addAction(builder, ping);
        int task = Task.endTask(builder);

        int tasks = Request.createTasksVector(builder, new int[]{task});

        Request.startRequest(builder);
        Request.addTasks(builder, tasks);

        return Request.endRequest(builder);
    }

    public class ListenThread extends Thread {

        public boolean isRunning;

        public ListenThread() {
            super("Network listen thread");
        }

        @Override
        public void run() {
            this.isRunning = true;

            DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());

            long size;

            try {

                while (isRunning) {
                    if ((size = (inputStream.readInt() & 0x00000000ffffffffL)) != -1) {

                        byte[] buffer = new byte[(int) size];

                        inputStream.read(buffer);

                        Request request = Request.getRootAsRequest(ByteBuffer.wrap(buffer));

                        synchronized (requests) {
                            requests.add(request);
                        }
                    }
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

        }
    }
}
