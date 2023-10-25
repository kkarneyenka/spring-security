package pl.training.shop.payments;

import org.javamoney.moneta.Money;
import org.mockito.stubbing.Answer;
import pl.training.shop.payments.adapters.persistence.jpa.PaymentEntity;
import pl.training.shop.payments.adapters.rest.PaymentDto;
import pl.training.shop.payments.domain.Payment;
import pl.training.shop.payments.domain.PaymentFeeCalculator;
import pl.training.shop.payments.domain.PaymentIdGenerator;
import pl.training.shop.payments.domain.PaymentStatus;
import pl.training.shop.payments.ports.TimeProvider;

import java.time.Instant;

import static pl.training.shop.payments.domain.PaymentStatus.STARTED;

public class PaymentFixtures {

    public static final String TEST_ID = "4828124d-e43e-4f59-a5f6-3cbfecc9898f";
    public static final String TEST_CURRENCY_CODE = "PLN";
    public static final Money TEST_MONEY_VALUE = Money.of(1_000, TEST_CURRENCY_CODE);
    public static final PaymentStatus TEST_STATUS = STARTED;
    public static final Instant TEST_TIMESTAMP = Instant.now();
    public static final Money TEST_FEE = Money.of(10, TEST_CURRENCY_CODE);
    public static Money TEST_MONEY_VALUE_WITH_FEE = TEST_MONEY_VALUE.add(TEST_FEE);
    public static final PaymentIdGenerator TEST_GENERATOR = () -> TEST_ID;
    public static final PaymentFeeCalculator TEST_FEE_CALCULATOR = (paymentValue) -> TEST_FEE;
    public static final TimeProvider TEST_TIME_PROVIDER = () -> TEST_TIMESTAMP;
    public static final Payment TEST_PAYMENT = Payment.builder()
            .id(TEST_ID)
            .value(TEST_MONEY_VALUE_WITH_FEE)
            .status(TEST_STATUS)
            .timestamp(TEST_TIMESTAMP)
            .build();

    public static PaymentEntity createEntity(String status) {
        var entity = new PaymentEntity();
        entity.setId(TEST_ID);
        entity.setValue(TEST_MONEY_VALUE_WITH_FEE.getNumber().doubleValueExact());
        entity.setCurrencyCode(TEST_CURRENCY_CODE);
        entity.setTimestamp(TEST_TIMESTAMP);
        entity.setStatus(status);
        return entity;
    }

    public static Money moneyOf(double value) {
        return Money.of(value, TEST_CURRENCY_CODE);
    }

    public static final Answer<PaymentDto> toDto = (invocation) -> {
        var payment = invocation.getArgument(0, Payment.class);
        var paymentDto = new PaymentDto();
        paymentDto.setId(payment.getId());
        paymentDto.setValue(payment.getValue().toString());
        return paymentDto;
    };

}
