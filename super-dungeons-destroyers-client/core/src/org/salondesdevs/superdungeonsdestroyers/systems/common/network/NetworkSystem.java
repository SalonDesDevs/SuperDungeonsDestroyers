package org.salondesdevs.superdungeonsdestroyers.systems.common.network;

import SDD.Request.Action;
import SDD.Request.Ping;
import SDD.Request.Request;
import SDD.Request.Task;
import SDD.Response.Response;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.google.flatbuffers.FlatBufferBuilder;
import net.wytrem.ecs.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Singleton
public class NetworkSystem extends BaseSystem {

    private Socket clientSocket;

    private ListenThread listenThread;
    private OutputStream outputStream;

    @Inject
    NetworkHandler networkHandler;

    @Override
    public void initialize() {
        Gdx.net = new NetImpl();
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


    float remaining = 1;

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
            request().addPingAction((byte) 21).addPingAction((byte) 123).writeAndFlush();
        }

//        if (remaining < -3 && this.clientSocket.isConnected()) {
//            this.clientSocket.dispose();
//        }
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
    RequestBuilder requestBuilder = new RequestBuilder();

    public RequestBuilder request() {
        requestBuilder.clear();
        return requestBuilder;
    }

    private void writeAndFlush(ByteBuffer byteBuffer) {
        sizeBuffer.rewind();
        sizeBuffer.putInt(byteBuffer.remaining());
        sizeBuffer.flip();

        try {
            writeChannel.write(sizeBuffer);
            writeChannel.write(byteBuffer);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class RequestBuilder {

        private List<Pair> pairs = new ArrayList<>();

        public void clear() {
            this.pairs.clear();
            builder.clear();
        }

        public RequestBuilder addPingAction(final byte number) {
            return this.addAction(Action.Ping, fbb -> Ping.createPing(fbb, number));
        }

        public RequestBuilder addAction(byte actionType, Function<FlatBufferBuilder, Integer> creator) {
            this.addPair(actionType, creator.apply(builder));
            return this;
        }

        private void addPair(byte action, int location) {
            this.pairs.add(new Pair(action, location));
        }

        public void writeAndFlush() {
            int[] tasksContent = new int[this.pairs.size()];

            for (int i = 0; i < tasksContent.length; i++) {
                Task.startTask(builder);
                Pair pair = this.pairs.get(i);
                Task.addActionType(builder, pair.first);
                Task.addAction(builder, pair.second);
                tasksContent[i] = Task.endTask(builder);
            }

            int tasks = Request.createTasksVector(builder, tasksContent);

            Request.startRequest(builder);
            Request.addTasks(builder, tasks);
            int request = Request.endRequest(builder);
            builder.finish(request);
            NetworkSystem.this.writeAndFlush(builder.dataBuffer());
        }

        private class Pair {
            public byte first;
                public int second;

            public Pair(byte first, int second) {
                this.first = first;
                this.second = second;
            }
        }
    }

    private class ListenThread extends Thread {

        public boolean isRunning;

        public ListenThread() {
            super("Network listen thread");
        }


        ByteBuffer sizeBuffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);

        @Override
        public void run() {
            this.isRunning = true;

            DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());

            ReadableByteChannel readableByteChannel = Channels.newChannel(inputStream);

            int size;

            try {

                while (isRunning) {

                    sizeBuffer.rewind();
                    readableByteChannel.read(sizeBuffer);

                    if ((size = readSize(inputStream)) != -1) {
                        byte[] buffer = new byte[size];

                        inputStream.read(buffer);

                        synchronized (networkHandler.responsesToHandle) {
                            networkHandler.responsesToHandle.add(Response.getRootAsResponse(ByteBuffer.wrap(buffer)));
                        }
                    }
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

        /**
         * Reads 32 byte int little-endianly.
         */
        private int readSize(DataInputStream in) throws IOException {
            int ch1 = in.read();
            int ch2 = in.read();
            int ch3 = in.read();
            int ch4 = in.read();
            if ((ch1 | ch2 | ch3 | ch4) < 0)
                throw new EOFException();
            return (int) (((ch4 << 24) + (ch3 << 16) + (ch2 << 8) + ch1) & 0x00000000ffffffffL);
        }
    }
}
