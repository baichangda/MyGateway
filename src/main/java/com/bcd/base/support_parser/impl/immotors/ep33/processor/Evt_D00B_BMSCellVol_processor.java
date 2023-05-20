package com.bcd.base.support_parser.impl.immotors.ep33.processor;

import com.bcd.base.support_parser.Parser;
import com.bcd.base.support_parser.exception.BaseRuntimeException;
import com.bcd.base.support_parser.impl.immotors.Evt;
import com.bcd.base.support_parser.impl.immotors.ep33.data.*;
import com.bcd.base.support_parser.processor.ProcessContext;
import com.bcd.base.support_parser.processor.Processor;
import com.bcd.base.support_parser.util.BitBuf_reader;
import com.google.common.base.Strings;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public class Evt_D00B_BMSCellVol_processor implements Processor<float[]> {

    static Logger logger = LoggerFactory.getLogger(Evt_D00B_BMSCellVol_processor.class);

    @Override
    public float[] process(ByteBuf data, ProcessContext parentContext) {
        final BitBuf_reader bitBuf = BitBuf_reader.newBitBuf(data);
        final Evt_D00B instance = (Evt_D00B) parentContext.instance;
        final short bmsCellVolSumNum = instance.BMSCellVolSumNum;
        final float[] arr1 = new float[bmsCellVolSumNum];
        final byte[] arr2 = new byte[bmsCellVolSumNum];
        for (int i = 0; i < bmsCellVolSumNum; i++) {
            arr1[i] = bitBuf.read(13, true) * 0.001F;
            arr2[i] = (byte) bitBuf.read(1, true);
            bitBuf.skip(2);
        }
        instance.BMSCellVolV = arr2;
        return arr1;
    }

    @Override
    public void deProcess(ByteBuf data, ProcessContext parentContext, float[] instance) {
        Parser.deParse(instance, data, parentContext);
    }

    public static void main(String[] args) {
        System.out.println("[" + new String(new byte[1]) + "]");
    }
}
