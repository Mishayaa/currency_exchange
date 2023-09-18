package org.example.controllers;


import org.example.daos.CurrencyDao;
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
import java.sql.SQLException;

@WebServlet("/currencies")
public class CurrencyServlet extends HttpServlet {
    private final CurrencyDao currencyDao;

    public CurrencyServlet() {
        currencyDao = new CurrencyDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String json = JsonMapper.toJson(currencyDao.getAllCurrencies());
        resp.setStatus(200);
        PrintWriter out = resp.getWriter();
        out.print(json);
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String code = req.getParameter("code");
        String sign = req.getParameter("sign");
        if (!Validator.isNotNull(name, code, sign)) {
            ExceptionHandler.handleException(400, "You missed one or more arguments", resp);
            return;
        }
        if (currencyDao.getCurrency(code) != null) {
            ExceptionHandler.handleException(409, "Currency with such code is already exists", resp);
            return;
        }
        currencyDao.addCurrency(currencyDao.getCurrency(code));
        resp.sendRedirect("/currency/" + code);
    }

}
