package com.sxtkl.immersiveminer.network;

import com.sxtkl.immersiveminer.Immersiveminer;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record KeyData(
    boolean activate
) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<KeyData> TYPE = new CustomPacketPayload.Type<>(
        Immersiveminer.namespace("key_data"));

    public static final StreamCodec<ByteBuf, KeyData> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.BOOL,
        KeyData::activate,
        KeyData::new
    );

    @Override
    @NotNull
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
