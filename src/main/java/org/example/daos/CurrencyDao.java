package org.example.daos;

import org.example.db.DbConnection;
import org.example.entities.Currencies;

import java.sql.*;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

public class CurrencyDao extends DbConnection {


    public List<Currencies> getAllCurrencies() {
        List<Currencies> currencies = new ArrayList<>();

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


    public Optional<Currencies> getCurrency(String code) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM currencies WHERE code = ?")) {

            statement.setString(1, code);
            ResultSet resultSet = statement.executeQuery();
            Currencies currency = null;
            if (resultSet.next()) {
                currency = createCurrency(resultSet);
            }
            resultSet.close();
            return Optional.ofNullable(currency);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public Currencies addCurrency(Currencies currency) {
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

        return currency;
    }

    private Currencies createCurrency(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt(1);
        String code = resultSet.getString(2);
        String full_name = resultSet.getString(3);
        String sign = resultSet.getString(4);
        return new Currencies(id, code, full_name, sign);
    }

}
