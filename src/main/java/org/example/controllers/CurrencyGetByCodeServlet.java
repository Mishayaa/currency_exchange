package org.example.controllers;

import org.example.daos.CurrencyDao;
import org.example.entities.Currency;
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
import java.util.Optional;

@WebServlet("/currencies/*")
public class CurrencyGetByCodeServlet extends HttpServlet {

    private final CurrencyDao currencyDao;

    public CurrencyGetByCodeServlet() {
        currencyDao = new CurrencyDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String code = req.getPathInfo().substring(1);
        if(!Validator.isCodeValid(code)){
            ExceptionHandler.handleException(400, "You haven't put currency code in path", resp);
            return;
        }
        Currency curr = currencyDao.getCurrency(code);
        if (curr == null) {
            ExceptionHandler.handleException(404, "Currency not found", resp);
        } else {
            String json = JsonMapper.toJson(curr);
            PrintWriter pw = resp.getWriter();
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.setStatus(200);
            pw.print(json);
            pw.close();
        }
    }


}
