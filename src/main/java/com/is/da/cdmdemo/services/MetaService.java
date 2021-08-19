package com.is.da.cdmdemo.services;

import com.rosetta.model.metafields.MetaFields;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MetaService {
    public MetaFields generateMeta(String globalKey) {
        if (globalKey == null) globalKey = UUID.randomUUID().toString();
        return MetaFields.builder().setGlobalKey(globalKey).build();
    }
}
