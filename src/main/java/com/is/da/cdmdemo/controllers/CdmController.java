package com.is.da.cdmdemo.controllers;

import cdm.base.math.Quantity;
import cdm.base.math.UnitType;
import cdm.base.math.metafields.FieldWithMetaQuantity;
import cdm.base.staticdata.asset.common.Commodity;
import cdm.base.staticdata.party.Party;
import cdm.event.common.*;
import cdm.observable.asset.*;
import cdm.observable.asset.metafields.FieldWithMetaPrice;
import cdm.product.common.TradeLot;
import cdm.product.template.Product;
import cdm.product.template.TradableProduct;
import com.is.da.cdmdemo.services.MetaService;
import com.is.da.cdmdemo.services.PartyService;
import com.is.da.cdmdemo.services.TransferService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api")
public class CdmController {
    final PartyService partyService;
    final MetaService metaService;
    final TransferService transferService;

    public CdmController(PartyService partyService, MetaService metaService, TransferService transferService) {
        this.partyService = partyService;
        this.metaService = metaService;
        this.transferService = transferService;
    }

    @GetMapping("/parties")
    public Party create() {
        return partyService.create("P1", "DBS");
    }


    @GetMapping("/tradable-product")
    public TradableProduct createExecutionService() {
        return TradableProduct.builder()
                .setProduct(Product.builder().setCommodityValue(Commodity.builder().build()).build())
                .addTradeLot(TradeLot.builder().addPriceQuantity(createPriceQuantity("SGD", 10000, "USD", 100000, 1.0)).build())
                .build();

    }

    public PriceQuantity buildPriceQuantity() {
        return PriceQuantity.builder()
                .addPriceValue(Price.builder().setAmount(BigDecimal.TEN).setPriceType(PriceTypeEnum.NET_PRICE).build())
                .addQuantityValue(Quantity.builder().setAmount(BigDecimal.valueOf(1000)).build())
                .build();

    }

    private PriceQuantity createPriceQuantity(String currency1Str, long quantity1, String currency2Str, long quantity2, double rate) {
        return PriceQuantity.builder()
                .addPrice(FieldWithMetaPrice.builder()
                        .setMeta(metaService.generateMeta(null))
                        .setValue(Price.builder()
                                .setAmount(BigDecimal.valueOf(rate))
                                .setUnitOfAmount(UnitType.builder()
                                        .setCurrencyValue(currency1Str))
                                .setPerUnitOfAmount(UnitType.builder()
                                        .setCurrencyValue(currency2Str))
                                .setPriceType(PriceTypeEnum.EXCHANGE_RATE))
                )
                .addQuantity(FieldWithMetaQuantity.builder()
                        .setMeta(metaService.generateMeta(null))
                        .setValue(Quantity.builder()
                                .setAmount(BigDecimal.valueOf(quantity1))
                                .setUnitOfAmount(UnitType.builder()
                                        .setCurrencyValue(currency1Str))))
                .addQuantity(FieldWithMetaQuantity.builder()
                        .setMeta(metaService.generateMeta(null))
                        .setValue(Quantity.builder()
                                .setAmount(BigDecimal.valueOf(quantity2))
                                .setUnitOfAmount(UnitType.builder()
                                        .setCurrencyValue(currency2Str))))
                .setObservable(Observable.builder()
                        .setCurrencyPairValue(QuotedCurrencyPair.builder()
                                .setCurrency1Value(currency1Str)
                                .setCurrency2Value(currency2Str)
                                .setQuoteBasis(QuoteBasisEnum.CURRENCY_2_PER_CURRENCY_1)))
                .build();
    }

    @GetMapping("/transfer")
    private BusinessEvent executePrimitive() {
        return transferService.transfer(transferService.getInputState(), transferService.creteInstruction());
    }

    private SplitPrimitive splitPrimitive() {
        return SplitPrimitive.builder()

                .build();
    }

    private TransferInstruction createInstruction() {
        return TransferInstruction.builder().build();
    }

    private TransferPrimitive transferPrimitive() {
        return TransferPrimitive.builder().build();
    }



}
