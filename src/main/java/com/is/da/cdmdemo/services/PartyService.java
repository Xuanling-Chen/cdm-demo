package com.is.da.cdmdemo.services;

import cdm.base.staticdata.party.Account;
import cdm.base.staticdata.party.Party;
import com.rosetta.model.metafields.FieldWithMetaString;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PartyService {
    final MetaService metaService;
    final List<Party> list = new ArrayList<>();

    public PartyService(MetaService metaService) {
        this.metaService = metaService;
    }

    public Party create(String id, String name) {
        return Party.builder()
                .addPartyId(FieldWithMetaString.builder().setValue(id).build())
                .setName(FieldWithMetaString.builder().setValue(name).build())
                .setAccount(Account.builder().setAccountNumberValue("1111-2222-3333-4444").build())
                .setMeta(metaService.generateMeta(id + "-" + name))
                .build();
    }

    public Party DBS() {
        return create("P1", "DBS");
    }

    public Party CITI() {
        return create("P2", "OCBC");
    }
}
