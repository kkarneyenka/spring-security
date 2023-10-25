package pl.training.shop.payments.adapters.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import pl.training.shop.commons.data.validation.ValidationExceptionMapper;
import pl.training.shop.commons.web.RestExceptionResponseBuilder;
import pl.training.shop.payments.adapters.rest.PaymentRestController;
import pl.training.shop.payments.adapters.rest.PaymentRestMapper;
import pl.training.shop.payments.domain.Payment;
import pl.training.shop.payments.ports.PaymentService;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.training.shop.payments.PaymentFixtures.*;

@WithMockUser(roles = "ADMIN")
@WebMvcTest(value = PaymentRestController.class)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class PaymentRestAdapterTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PaymentService paymentService;
    @MockBean
    private PaymentRestMapper mapper;

    @TestConfiguration
    static class TestConfig {

        @Bean
        public RestExceptionResponseBuilder responseBuilder(MessageSource messageSource) {
            return new RestExceptionResponseBuilder(messageSource);
        }

        @Bean
        public ValidationExceptionMapper validationExceptionMapper() {
            return Mappers.getMapper(ValidationExceptionMapper.class);
        }

       /* @Bean
        public WebSecurityCustomizer webSecurityCustomizer() {
            return web -> web.ignoring().anyRequest();
        }*/

    }

    @BeforeEach
    void beforeEach() {
        when(paymentService.getById(TEST_ID)).thenReturn(TEST_PAYMENT);
        when(mapper.toDto(any(Payment.class))).then(toDto);
    }

    @Test
    void given_payment_exists_when_get_by_id_then_returns_payment() throws Exception {
        mockMvc.perform(get("/api/payments/" + TEST_ID).accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(TEST_ID)))
                .andExpect(jsonPath("$.value", is(TEST_PAYMENT.getValue().toString())));
    }

}
