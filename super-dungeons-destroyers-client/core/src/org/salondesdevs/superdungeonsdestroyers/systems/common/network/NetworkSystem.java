package org.salondesdevs.superdungeonsdestroyers.systems.common.network;

import SDD.Client.Event;
import SDD.Client.EventUnion;
import SDD.Client.Events;
import SDD.Client.Move;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.google.flatbuffers.FlatBufferBuilder;
import net.wytrem.ecs.*;
import org.salondesdevs.superdungeonsdestroyers.states.IngameState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Singleton
public class NetworkSystem extends BaseSystem {

    private static final Logger logger = LoggerFactory.getLogger( NetworkSystem.class );

    private Socket clientSocket;

    private ListenThread listenThread;
    private OutputStream outputStream;

    @Inject
    NetworkHandlerSystem networkHandler;

    @Override
    public void initialize() {
        logger.info("Connecting to server");

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


    boolean test = true;

    @Override
    public void begin() {
        batch.begin();
    }

    float remaining = 4;

    @Override
    public void process() {
        // Send enqueued packets, ...

//        remaining -= world.getDelta();
//
//        if (remaining > 0) {
//            font.draw(batch, "Ingame in " + remaining, 100, 100);
//        }
//
//        if (remaining < 0 && test) {
//            test = false;
////            request().addPingContent((byte) 21).addPingContent((byte) 123).writeAndFlush();
//            this.world.push(IngameState.class);
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

        public RequestBuilder addMoveContent(final byte direction) {
            return this.addContent(EventUnion.Move, fbb -> Move.createMove(fbb, direction));
        }

//        public RequestBuilder addPingContent(final byte number) {
//            return this.addContent(EventUnion.Ping, fbb -> Ping.createPing(fbb, number));
//        }

        public RequestBuilder addContent(byte contentType, Function<FlatBufferBuilder, Integer> creator) {
            this.addPair(contentType, creator.apply(builder));
            return this;
        }

        private void addPair(byte action, int location) {
            this.pairs.add(new Pair(action, location));
        }

        public void writeAndFlush() {
            int[] messagesContent = new int[this.pairs.size()];

            for (int i = 0; i < messagesContent.length; i++) {
                Event.startEvent(builder);
                Pair pair = this.pairs.get(i);
                Event.addEventType(builder, pair.first);
                Event.addEvent(builder, pair.second);
                messagesContent[i] = Event.endEvent(builder);
            }

            int messagesVector = Events.createEventsVector(builder, messagesContent);

            Events.startEvents(builder);
            Events.addEvents(builder, messagesVector);
            int messages = Events.endEvents(builder);
            builder.finish(messages);
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


        @Override
        public void run() {
            this.isRunning = true;

            DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());

            int size;

            try {

                while (isRunning) {
                    if ((size = readSize(inputStream)) != -1) {
                        byte[] buffer = new byte[size];

                        inputStream.read(buffer);

                        synchronized (networkHandler.messagesToHandle) {
                            networkHandler.messagesToHandle.add(SDD.Server.Events.getRootAsEvents(ByteBuffer.wrap(buffer)));
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
