package com.is.da.cdmdemo.services;

import cdm.base.math.Quantity;
import cdm.base.staticdata.party.PayerReceiver;
import cdm.event.common.*;
import cdm.event.common.metafields.ReferenceWithMetaTradeState;
import cdm.event.position.PositionStatusEnum;
import com.rosetta.model.lib.records.DateImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class TransferService {

    final PartyService partyService;

    public TransferService(PartyService partyService) {
        this.partyService = partyService;
    }

    public BusinessEvent transfer(ReferenceWithMetaTradeState input, TransferInstruction instruction) {
        // follow instruction, transfer money, and save output state

        //TODO when follow instruction, need to store effect event here also

        TransferPrimitive primitive = TransferPrimitive.builder()
                .setBefore(input)
                .setAfter(createState(PositionStatusEnum.SETTLED))
                .build();

        return BusinessEvent.builder()
                .setEventDate(new DateImpl(2021,8,20))
                .addPrimitives(PrimitiveEvent.builder().setTransfer(primitive).build())
                .build();
    }

    public TransferInstruction creteInstruction() {
        return TransferInstruction.builder()
                .setPayerReceiver(PayerReceiver
                        .builder()
                        .setPayerPartyReferenceValue(partyService.DBS())
                        .setReceiverPartyReferenceValue(partyService.CITI())
                        .build())
                .setQuantity(Quantity.builder().setAmount(BigDecimal.TEN).build())
                .build();

    }

    public ReferenceWithMetaTradeState getInputState() {
        return createTradeState(UUID.randomUUID().toString(), PositionStatusEnum.FORMED);
    }


    private ReferenceWithMetaTradeState createTradeState(String globalRef, PositionStatusEnum position ) {
        TradeState state = createState(position);
        return ReferenceWithMetaTradeState.builder()
                .setGlobalReference(globalRef)
                .setValue(state)
                .build();
    }

    private TradeState createState(PositionStatusEnum position) {
        return TradeState.builder()
                .setState(State.builder().setPositionState(position).build())
                .build();
    }

}
