package org.example.services;

import org.example.daos.ExchangeRatesDao;
import org.example.dtos.Exchange;
import org.example.entities.ExchangeRate;

import javax.servlet.http.HttpServlet;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;

public class ExchangeService {
    public static final ExchangeService INSTANCE = new ExchangeService();
    private final ExchangeRatesDao currencyRateDAO;

    private ExchangeService() {
        currencyRateDAO = new ExchangeRatesDao();
    }

    public Exchange exchangeCurrency(String from, String to, BigDecimal amount) throws SQLException {
        ExchangeRate cr1 = currencyRateDAO.getExchangeRatePairByCode(from + to);
        if (cr1 != null) {
            return new Exchange(
                    cr1.getBaseCurrency(),
                    cr1.getTargetCurrency(),
                    cr1.getRate(),
                    amount,
                    amount.multiply(cr1.getRate())
            );
        }

        ExchangeRate cr2 = currencyRateDAO.getExchangeRatePairByCode(to + from);
        if (cr2 != null) {
            return new Exchange(
                    cr2.getTargetCurrency(),
                    cr2.getBaseCurrency(),
                    amount.divide(amount.multiply(cr2.getRate())),
                    amount,
                    amount.divide(cr2.getRate())
            );
        }

        cr1 = currencyRateDAO.getExchangeRatePairByCode("USD" + from);
        cr2 = currencyRateDAO.getExchangeRatePairByCode("USD" + to);
        if (cr1 == null || cr2 == null) {
            return null;
        }

        return new Exchange(
                cr1.getTargetCurrency(),
                cr2.getTargetCurrency(),
                cr2.getRate().divide(cr1.getRate(), 2, RoundingMode.DOWN),
                amount,
                amount.multiply(cr2.getRate().divide(cr1.getRate(), 2, RoundingMode.DOWN))
        );
    }
}
