package com.fooddelivery.service;

import com.fooddelivery.dto.AddressRequest;
import com.fooddelivery.dto.AddressResponse;

public interface AddressService {
    AddressResponse addAddress(Long userId, AddressRequest request);
}
