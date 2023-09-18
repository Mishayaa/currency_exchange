package org.example.daos;

import org.example.db.DbConnection;
import org.example.entities.Currency;
import org.example.entities.ExchangeRate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRatesDao extends DbConnection {
    CurrencyDao currencyDao;

    public ExchangeRatesDao() {
        this.currencyDao = new CurrencyDao();
    }

    public List<ExchangeRate> getAllExchangeRates() {
        List<ExchangeRate> exchangeRates = new ArrayList<>();

        String select = "SELECT * FROM exchange_rates";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(select)) {

            while (resultSet.next()) {
                while (resultSet.next()) {
                    exchangeRates.add(
                            new ExchangeRate(
                                    resultSet.getInt("id"),
                                    currencyDao.findById(resultSet.getInt("base_currency_id")),
                                    currencyDao.findById(resultSet.getInt("target_currency_id")),
                                    resultSet.getBigDecimal("rate")));

                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return exchangeRates;
    }

    public ExchangeRate getExchangeRatePairByCode(String code) throws SQLException {

        String base = code.substring(0, 3);
        String target = code.substring(3);
        Currency baseCurr = currencyDao.getCurrency(base);
        Currency targetCurr = currencyDao.getCurrency(target);

        if (baseCurr == null || targetCurr == null) {
            return null;
        }
        try (Connection conn = getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM exchange_rates" +
                    " WHERE base_currency_id = ? AND target_currency_id = ?");
            ps.setInt(1, baseCurr.getId());
            ps.setInt(2, targetCurr.getId());
            ps.execute();
            ResultSet rs = ps.getResultSet();

            if (!rs.next()) {
                return null;
            }
            return new ExchangeRate(
                    rs.getInt("id"),
                    currencyDao.findById(rs.getInt("base_currency_id")),
                    currencyDao.findById(rs.getInt("target_currency_id")),
                    rs.getBigDecimal("rate")
            );
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }


//    public ExchangeRate save(ExchangeRate exchangeRates) {
////        String sql = "INSERT INTO exchange_rate (base_currency_id,target_currency_id,rate) VALUES(?,?,?)";
////        int rows;
////        try (Connection connection = getConnection();
////             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
////
////
////            preparedStatement.setLong(2, exchangeRates.getBaseCurrencyId());
////            preparedStatement.setLong(3, exchangeRates.getTargetCurrencyId());
////            preparedStatement.setBigDecimal(4, exchangeRates.getRate());
////
////            rows = preparedStatement.getUpdateCount();
////            if (rows > 0) {
////                ResultSet keys = preparedStatement.getGeneratedKeys();
////                if (keys.next()) {
////                    exchangeRates.setId(keys.getInt(1));
////                }
////            }
////
////        } catch (SQLException e) {
////            throw new RuntimeException(e);
////        }
////        return exchangeRates;
//    }

}
