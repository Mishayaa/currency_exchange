package org.example.controllers;


import org.example.daos.CurrencyDao;
import org.example.daos.ExchangeRatesDao;
import org.example.mappers.JsonMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/exchangeRates")
public class ExchangeRateServlet extends HttpServlet {

    private final ExchangeRatesDao exchangeRatesDao;

    public ExchangeRateServlet() {
        this.exchangeRatesDao = new ExchangeRatesDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String json = JsonMapper.toJson(exchangeRatesDao.getAllExchangeRates());
        resp.setStatus(200);
        PrintWriter out = resp.getWriter();
        out.print(json);
        out.flush();

    }
}
