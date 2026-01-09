package com.example.util;

import com.example.dto.DataDTO;

public class EmvcoBuilder {
    public static String build(DataDTO data) {

        StringBuilder emv = new StringBuilder();

        // Payload Format Indicator
        emv.append(tag("00", "01"));

        // Point of Initiation Method
        emv.append(tag("01",
                data.getPointOfInitiationMethod().equalsIgnoreCase("ESTATICO") ? "11" : "12"
        ));

        // Merchant Account Information (Ejemplo ID 26)
        String merchantAccount =
                tag("00", "CO.BANCO.QR") +
                        tag("01", data.getMerchantAccountInformation().getAliasValue());

        emv.append(tag("26", merchantAccount));

        // Merchant Category Code
        emv.append(tag("52", data.getMerchantAccountInformation().getMerchantCategoryCode()));

        // Currency (COP = 170)
        emv.append(tag("53", "170"));

        // Amount
        emv.append(tag("54", data.getAmount().toPlainString()));

        // Country Code
        emv.append(tag("58", "CO"));

        // Merchant Name
        emv.append(tag("59", data.getMerchantAccountInformation().getMerchantName()));

        // Merchant City
        emv.append(tag("60", data.getMerchantAccountInformation().getMerchantCity()));

        // CRC placeholder
        String payloadWithoutCrc = emv.toString() + "6304";
        String crc = Crc16Util.calculate(payloadWithoutCrc);

        return payloadWithoutCrc + crc;
    }

    private static String tag(String id, String value) {
        return id + String.format("%02d", value.length()) + value;
    }
}
