package com.example.sistema.repositories;

import com.example.sistema.model.Pedido;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PedidoRepositorio {

    private final String url = "jdbc:mysql://localhost:3306/sistema";
    private final String user = "sistema";
    private final String password = "123456";

    public List<Pedido> findAll() {
        List<Pedido> pedidos = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM pedido")) {

            while (resultSet.next()) {
                Pedido pedido = new Pedido();
                pedido.setId(resultSet.getLong("id"));
                pedido.setData(resultSet.getDate("data"));
                pedido.setTotal(resultSet.getDouble("total"));
                // Set cliente and produtos as needed
                pedidos.add(pedido);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pedidos;
    }

    public Optional<Pedido> findById(Long id) {
        Pedido pedido = null;
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM pedido WHERE id = ?")) {

            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    pedido = new Pedido();
                    pedido.setId(resultSet.getLong("id"));
                    pedido.setData(resultSet.getDate("data"));
                    pedido.setTotal(resultSet.getDouble("total"));
                    // Set cliente and produtos as needed
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(pedido);
    }

    public void save(Pedido pedido) {
        String sql = pedido.getId() == null ?
            "INSERT INTO pedido (data, total) VALUES (?, ?)" :
            "UPDATE pedido SET data = ?, total = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setDate(1, new java.sql.Date(pedido.getData().getTime()));
            statement.setDouble(2, pedido.getTotal());
            if (pedido.getId() != null) {
                statement.setLong(3, pedido.getId());
            }
            statement.executeUpdate();
            if (pedido.getId() == null) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        pedido.setId(generatedKeys.getLong(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(Long id) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("DELETE FROM pedido WHERE id = ?")) {

            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}