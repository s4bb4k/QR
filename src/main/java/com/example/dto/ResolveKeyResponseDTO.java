package com.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResolveKeyResponseDTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("handle")
    private String handle;

    @JsonProperty("keeper")
    private List<KeeperDTO> keeper;

    @JsonProperty("labels")
    private LabelsDTO labels;

    @JsonProperty("error")
    private ErrorDTO error;

    @Data
    public static class  KeeperDTO {
        @JsonProperty("public")
        private String publicKey;
        private String scheme;
    }

    @Data
    public class ErrorDTO {
        private int code;
        private String message;
    }

    public static class LabelsDTO {

        @JsonProperty("name")
        private String name;

        @JsonProperty("type")
        private String type;

        @JsonProperty("bankId")
        private String bankId;

        @JsonProperty("status")
        private String status;

        @JsonProperty("created")
        private String created;

        @JsonProperty("bankName")
        private String bankName;

        @JsonProperty("diceSync")
        private Boolean diceSync;

        @JsonProperty("received")
        private String received;

        @JsonProperty("aliasType")
        private String aliasType;

        @JsonProperty("bankBicfi")
        private String bankBicfi;

        @JsonProperty("consented")
        private String consented;

        @JsonProperty("createdBy")
        private String createdBy;

        @JsonProperty("aliasValue")
        private String aliasValue;

        @JsonProperty("dispatched")
        private String dispatched;

        @JsonProperty("proprietary")
        private String proprietary;

        @JsonProperty("identification")
        private String identification;

        @JsonProperty("bankAccountType")
        private String bankAccountType;

        @JsonProperty("routerReference")
        private String routerReference;

        @JsonProperty("targetSpbviCode")
        private String targetSpbviCode;

        @JsonProperty("bankAccountNumber")
        private String bankAccountNumber;
    }
}
