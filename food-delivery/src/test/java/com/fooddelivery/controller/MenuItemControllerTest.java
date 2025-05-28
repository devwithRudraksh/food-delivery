package com.fooddelivery.controller;

import com.fooddelivery.dto.request.MenuItemRequest;
import com.fooddelivery.dto.response.MenuItemResponse;
import com.fooddelivery.enums.MenuCategory;
import com.fooddelivery.service.MenuItemService;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fooddelivery.dto.request.RestaurantRequest;
import com.fooddelivery.dto.response.RestaurantResponse;
import com.fooddelivery.service.RestaurantService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(MenuItemController.class)
@AutoConfigureMockMvc(addFilters = false)
class MenuItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuItemService menuItemService;

    @Test
    void testAddMenuItem() throws Exception {
        Long restaurantId = 1L;

        MenuItemRequest request = new MenuItemRequest();
        request.setName("Mojito");
        request.setPrice(99.0);
        request.setCategory(MenuCategory.BEVERAGE);

        MenuItemResponse response = MenuItemResponse.builder()
                .id(1L)
                .name("Mojito")
                .price(99.0)
                .category(MenuCategory.BEVERAGE)
                .build();

        when(menuItemService.addMenuItem(eq(restaurantId), any(MenuItemRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/restaurants/{restaurantId}/menu-items", restaurantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Mojito"))
                .andExpect(jsonPath("$.category").value("BEVERAGE"));
    }
}

