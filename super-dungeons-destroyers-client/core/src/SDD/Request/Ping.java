// automatically generated by the FlatBuffers compiler, do not modify

package SDD.Request;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class Ping extends Table {
  public static void ValidateVersion() { Constants.FLATBUFFERS_1_11_1(); }
  public static Ping getRootAsPing(ByteBuffer _bb) { return getRootAsPing(_bb, new Ping()); }
  public static Ping getRootAsPing(ByteBuffer _bb, Ping obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { __reset(_i, _bb); }
  public Ping __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public byte value() { int o = __offset(4); return o != 0 ? bb.get(o + bb_pos) : 0; }
  public boolean mutateValue(byte value) { int o = __offset(4); if (o != 0) { bb.put(o + bb_pos, value); return true; } else { return false; } }

  public static int createPing(FlatBufferBuilder builder,
      byte value) {
    builder.startTable(1);
    Ping.addValue(builder, value);
    return Ping.endPing(builder);
  }

  public static void startPing(FlatBufferBuilder builder) { builder.startTable(1); }
  public static void addValue(FlatBufferBuilder builder, byte value) { builder.addByte(0, value, 0); }
  public static int endPing(FlatBufferBuilder builder) {
    int o = builder.endTable();
    return o;
  }
}

