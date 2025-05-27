package com.fooddelivery.service.impl;

import com.fooddelivery.dto.AddressRequest;
import com.fooddelivery.dto.AddressResponse;
import com.fooddelivery.entity.Address;
import com.fooddelivery.entity.User;
import com.fooddelivery.repository.AddressRepository;
import com.fooddelivery.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AddressServiceImplTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AddressServiceImpl addressService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddAddress_Success() {
        // Arrange
        Long userId = 1L;

        AddressRequest request = new AddressRequest();
        request.setStreet("MG Road");
        request.setCity("Bangalore");
        request.setState("Karnataka");
        request.setZipCode("560001");
        request.setLandmark("Metro");

        User user = User.builder()
                .id(userId)
                .name("Test User")
                .email("test@example.com")
                .password("testpass")
                .build();

        // Mock user repository
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Mock save to simulate DB-generated ID
        when(addressRepository.save(any(Address.class))).thenAnswer(inv -> {
            Address a = inv.getArgument(0);
            a.setId(100L);
            return a;
        });

        // Act
        AddressResponse response = addressService.addAddress(userId, request);

        // Assert
        assertNotNull(response);
        assertEquals("MG Road", response.getStreet());
        assertEquals("Bangalore", response.getCity());
        assertEquals("Karnataka", response.getState());
        assertEquals("560001", response.getZipCode());
        assertEquals("Metro", response.getLandmark());
        assertEquals(100L, response.getId());

        // Verify interactions
        verify(userRepository, times(1)).findById(userId);
        verify(addressRepository, times(1)).save(any(Address.class));
    }
}
