// automatically generated by the FlatBuffers compiler, do not modify

package SDD.Response;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class Pong extends Table {
  public static void ValidateVersion() { Constants.FLATBUFFERS_1_11_1(); }
  public static Pong getRootAsPong(ByteBuffer _bb) { return getRootAsPong(_bb, new Pong()); }
  public static Pong getRootAsPong(ByteBuffer _bb, Pong obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { __reset(_i, _bb); }
  public Pong __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public byte value() { int o = __offset(4); return o != 0 ? bb.get(o + bb_pos) : 0; }
  public boolean mutateValue(byte value) { int o = __offset(4); if (o != 0) { bb.put(o + bb_pos, value); return true; } else { return false; } }

  public static int createPong(FlatBufferBuilder builder,
      byte value) {
    builder.startTable(1);
    Pong.addValue(builder, value);
    return Pong.endPong(builder);
  }

  public static void startPong(FlatBufferBuilder builder) { builder.startTable(1); }
  public static void addValue(FlatBufferBuilder builder, byte value) { builder.addByte(0, value, 0); }
  public static int endPong(FlatBufferBuilder builder) {
    int o = builder.endTable();
    return o;
  }
}

