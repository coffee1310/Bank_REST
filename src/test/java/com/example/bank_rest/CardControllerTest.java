package com.example.bank_rest;

import com.example.bank_rest.config.SecurityConfig;
import com.example.bank_rest.controller.CardController;
import com.example.bank_rest.dto.CardDTO;
import com.example.bank_rest.entity.Card;
import com.example.bank_rest.entity.User;
import com.example.bank_rest.repository.CardRepository;
import com.example.bank_rest.security.JwtAuthenticationFilter;
import com.example.bank_rest.security.JwtService;
import com.example.bank_rest.service.CardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CardController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CardControllerTest {

    @MockitoBean
    private CardService cardService;

    @MockitoBean
    private CardRepository cardRepository;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private Authentication authentication;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/card/cards -> 200, когда карты найдены и все успешно")
    @WithMockUser(username = "admin1", roles = {"ADMIN"})
    public void getCards_thenReturnCardsAndStatus200() throws Exception {
        CardDTO cardDTO = createCardDTO();

        when(cardService.getCards("admin1")).thenReturn(List.of(
                cardDTO
        ));

        mockMvc.perform(get("/api/card"))
                .andExpect(jsonPath("$[0].id").isNumber())
                .andExpect(jsonPath("$[0].status").value("ACTIVE"))
                .andExpect(jsonPath("$[0].card_number").value("1234 5678 9012 3456"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin1", roles = {"ADMIN"})
    public void getCard_thenReturnCardAndStatus200() throws Exception {
        Long userId = 1L;
        CardDTO cardDTO = createCardDTO();

        when(cardService.getCard(userId)).thenReturn(cardDTO);
        mockMvc.perform(get("/api/card/{id}", userId))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.card_number").value("1234 5678 9012 3456"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin1", roles = {"ADMIN"})
    public void createCard_thenReturnCardAndStatus201() throws Exception {
        CardDTO cardDTO = createCardDTO();

        when(cardService.createCard(any(CardDTO.class))).thenReturn(cardDTO);

        mockMvc.perform(post("/api/card")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cardDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.card_number").value("1234 5678 9012 3456"));
    }

    private CardDTO createCardDTO() {
        CardDTO cardDTO = new CardDTO();
        cardDTO.setId(1L);
        cardDTO.setStatus("ACTIVE");
        cardDTO.setCard_number("1234 5678 9012 3456");
        cardDTO.setMaskedNumber("**** **** **** 3456");
        cardDTO.setExpiry_date(Date.valueOf("2025-12-12"));
        cardDTO.setBalance(BigDecimal.ZERO);
        cardDTO.setUser_id(1L);

        return cardDTO;
    }

    private Card createCard() {
        Card card = new Card();
        card.setId(1L);
        card.setStatus("ACTIVE");
        card.setCardNumber("1234 5678 9012 3456");
        card.setMaskedNumber("**** **** **** 3456");
        card.setExpiryDate(Date.valueOf("2025-12-12"));
        card.setBalance(BigDecimal.ZERO);
        card.setUser(new User());

        return card;
    }
}
