package com.fooddelivery.service.impl;
import org.springframework.transaction.annotation.Transactional;
import com.fooddelivery.dto.request.AddressRequest;
import com.fooddelivery.dto.response.AddressResponse;
import com.fooddelivery.entity.Address;
import com.fooddelivery.entity.User;
import com.fooddelivery.repository.AddressRepository;
import com.fooddelivery.repository.UserRepository;
import com.fooddelivery.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private static final Logger log = LoggerFactory.getLogger(AddressServiceImpl.class);

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public AddressResponse addAddress(Long userId, AddressRequest request) {
        log.info("Adding address for user ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        log.info("Fetched user: {}", user);  // should print user with id + name

        Address address = Address.builder()
                .street(request.getStreet())
                .city(request.getCity())
                .state(request.getState())
                .zipCode(request.getZipCode())
                .landmark(request.getLandmark())
                .user(user)
                .build();
        log.info("Prepared Address: {}", address);
        log.info("Saving this address: {}", address.toString());
        Address saved = addressRepository.save(address);
        log.info("Saved Address: {}", saved);

        log.info("Address added with ID: {}", saved.getId());

        return AddressResponse.builder()
                .id(saved.getId())
                .street(saved.getStreet())
                .city(saved.getCity())
                .state(saved.getState())
                .zipCode(saved.getZipCode())
                .landmark(saved.getLandmark())
                .build();
    }
}
