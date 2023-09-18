package org.example.daos;

import org.example.db.DbConnection;
import org.example.entities.Currency;

import java.sql.*;
import java.util.ArrayList;

import java.util.List;

public class CurrencyDao extends DbConnection {


    public List<Currency> getAllCurrencies() {
        List<Currency> currencies = new ArrayList<>();

        String select = "SELECT * FROM currencies";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(select)) {

            while (resultSet.next()) {
                currencies.add(createCurrency(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return currencies;
    }


    public Currency getCurrency(String code) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM currencies WHERE code = ?")) {

            statement.setString(1, code);
            ResultSet resultSet = statement.executeQuery();
            Currency currency = null;
            if (resultSet.next()) {
                currency = createCurrency(resultSet);
            }
            resultSet.close();
            return currency;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void addCurrency(Currency currency) {
        String sql = "INSERT INTO currencies (code, full_name, sign) VALUES (?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, currency.getCode());
            preparedStatement.setString(2, currency.getFullName());
            preparedStatement.setString(3, currency.getSign());

            int rows = preparedStatement.executeUpdate();

            if (rows > 0) {
                try (ResultSet keys = preparedStatement.getGeneratedKeys()) {
                    if (keys.next()) {
                        currency.setId(keys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private Currency createCurrency(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt(1);
        String code = resultSet.getString(2);
        String full_name = resultSet.getString(3);
        String sign = resultSet.getString(4);
        return new Currency(id, code, full_name, sign);
    }

    public Currency findById(int id) throws SQLException {
        try (Connection conn = getConnection()) {
            PreparedStatement stat = conn.prepareStatement("SELECT * FROM Currencies WHERE id = ?");
            stat.setInt(1, id);
            stat.execute();
            ResultSet rs = stat.getResultSet();

            if (rs.next()) {
                return new Currency(
                        rs.getInt("id"),
                        rs.getString("code"),
                        rs.getString("full_name"),
                        rs.getString("sign")
                );
            } else return null;
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

}
