package com.fooddelivery.service;

import com.fooddelivery.dto.request.AddressRequest;
import com.fooddelivery.dto.response.AddressResponse;

public interface AddressService {
    AddressResponse addAddress(Long userId, AddressRequest request);
}
