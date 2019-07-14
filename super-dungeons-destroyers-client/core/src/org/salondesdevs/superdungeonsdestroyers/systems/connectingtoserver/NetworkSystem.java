package org.salondesdevs.superdungeonsdestroyers.systems.connectingtoserver;

import SDD.Request.Action;
import SDD.Request.Ping;
import SDD.Request.Request;
import SDD.Request.Task;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.google.flatbuffers.FlatBufferBuilder;
import net.wytrem.ecs.*;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.List;

public class NetworkSystem extends BaseSystem {

    private Socket clientSocket;

    private List<Request> requests = new ArrayList<>();

    private ListenThread listenThread;
    private OutputStream outputStream;

    @Override
    public void initialize() {
        this.clientSocket = Gdx.net.newClientSocket(Net.Protocol.TCP, "localhost", 9000, new SocketHints());

        this.listenThread = new ListenThread();
        this.listenThread.start();

        sizeBuffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
        outputStream = this.clientSocket.getOutputStream();
        writeChannel = Channels.newChannel(outputStream);

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
            this.sendPingRequest(38);
        }
    }

    @Override
    public void end() {
        batch.end();
    }

    @Override
    public void dispose() {
        this.clientSocket.dispose();
    }

    ByteBuffer sizeBuffer;
    WritableByteChannel writeChannel;
    FlatBufferBuilder builder = new FlatBufferBuilder();

    private void writeAndFlush(ByteBuffer byteBuffer) {
        sizeBuffer.rewind();
        sizeBuffer.putInt(byteBuffer.remaining());
        sizeBuffer.flip();

        try {
            writeChannel.write(sizeBuffer);
            System.err.println("Wrote " + writeChannel.write(byteBuffer) + " bytes");
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendPingRequest(int number) {
        builder.clear();
        int ping = Ping.createPing(builder, (byte) number);

        Task.startTask(builder);
        Task.addActionType(builder, Action.Ping);
        Task.addAction(builder, ping);
        int task = Task.endTask(builder);

        int tasks = Request.createTasksVector(builder, new int[]{task});

        Request.startRequest(builder);
        Request.addTasks(builder, tasks);
        int request = Request.endRequest(builder);
        builder.finish(request);
        writeAndFlush(builder.dataBuffer());
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
