package com.fooddelivery.dto.request;

import lombok.Data;

@Data
public class AddressRequest {
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String landmark;
}
