package org.example.controllers;

import org.example.daos.ExchangeRatesDao;
import org.example.dtos.Exchange;
import org.example.entities.ExchangeRate;
import org.example.exception_handler.ExceptionHandler;
import org.example.mappers.JsonMapper;
import org.example.validators.Validator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.SQLException;

@WebServlet("/exchangeRate/*")
public class ExchangeRatePairServlet extends HttpServlet {

    ExchangeRatesDao exchangeRatesDao;

    public ExchangeRatePairServlet() {
        this.exchangeRatesDao = new ExchangeRatesDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pair = req.getPathInfo().substring(1);
        if (pair.length() != 6) {
            ExceptionHandler.handleException(400, "You entered incorrect currency pair", resp);
            return;
        }

        try {
            ExchangeRate exchangeRate = exchangeRatesDao.getExchangeRatePairByCode(pair);
            if (exchangeRate == null) {
                ExceptionHandler.handleException(404, "Currency rate with such codes doesn't exists", resp);
                return;
            }

            String json = JsonMapper.toJson(exchangeRate);
            PrintWriter out = resp.getWriter();
            resp.setStatus(200);
            out.print(json);
            out.flush();
        } catch (SQLException e) {
            ExceptionHandler.handleException(500, "Unable to connect to database", resp);
        }

    }

}
