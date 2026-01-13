package com.example.util;

import com.example.dto.DataDTO;

public class EmvcoBuilder {

    public static String build(DataDTO data) {

        StringBuilder emv = new StringBuilder();

        emv.append(tag("00", "01"));

        emv.append(tag("01",
                data.getPointOfInitiationMethod().equalsIgnoreCase("ESTATICO") ? "11" : "12"
        ));

        String merchantAccount =
                tag("00", "CO.BANCO.QR") +
                        tag("01", mapAliasType(
                                data.getMerchantAccountInformation().getAliasType()
                        )) +
                        tag("02", data.getMerchantAccountInformation().getAliasValue());

        emv.append(tag("26", merchantAccount));

        emv.append(tag("52", data.getMerchantAccountInformation().getMerchantCategoryCode()));
        emv.append(tag("53", "170"));
        emv.append(tag("54", data.getAmount().toPlainString()));
        emv.append(tag("58", "CO"));
        emv.append(tag("59", data.getMerchantAccountInformation().getMerchantName()));
        emv.append(tag("60", data.getMerchantAccountInformation().getMerchantCity()));

        String payloadWithoutCrc = emv + "6304";
        String crc = Crc16Util.calculate(payloadWithoutCrc);

        return payloadWithoutCrc + crc;
    }

    private static String tag(String tag, String value) {
        return tag + String.format("%02d", value.length()) + value;
    }

    private static String mapAliasType(String aliasType) {
        return switch (aliasType.toUpperCase()) {
            case "CELULAR" -> "01";
            case "CEDULA"  -> "02";
            case "CUENTA"  -> "03";
            case "EMAIL"   -> "04";
            default -> throw new IllegalArgumentException("AliasType no soportado");
        };
    }

}
