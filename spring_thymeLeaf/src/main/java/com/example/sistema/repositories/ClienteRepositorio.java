package com.example.sistema.repositories;

import com.example.sistema.model.Cliente;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ClienteRepositorio {

    private final String url = "jdbc:mysql://localhost:3306/sistema";
    private final String user = "sistema";
    private final String password = "123456";

    public List<Cliente> findAll() {
        List<Cliente> clientes = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM cliente")) {

            while (resultSet.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(resultSet.getLong("id"));
                cliente.setNome(resultSet.getString("nome"));
                cliente.setEndereco(resultSet.getString("endereco"));
                cliente.setTelefone(resultSet.getString("telefone"));
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientes;
    }

    public Optional<Cliente> findById(Long id) {
        Cliente cliente = null;
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM cliente WHERE id = ?")) {

            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    cliente = new Cliente();
                    cliente.setId(resultSet.getLong("id"));
                    cliente.setNome(resultSet.getString("nome"));
                    cliente.setEndereco(resultSet.getString("endereco"));
                    cliente.setTelefone(resultSet.getString("telefone"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(cliente);
    }

    public void save(Cliente cliente) {
        String sql = cliente.getId() == null ?
            "INSERT INTO cliente (nome, endereco, telefone) VALUES (?, ?, ?)" :
            "UPDATE cliente SET nome = ?, endereco = ?, telefone = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, cliente.getNome());
            statement.setString(2, cliente.getEndereco());
            statement.setString(3, cliente.getTelefone());
            if (cliente.getId() != null) {
                statement.setLong(4, cliente.getId());
            }
            statement.executeUpdate();
            if (cliente.getId() == null) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        cliente.setId(generatedKeys.getLong(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(Long id) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("DELETE FROM cliente WHERE id = ?")) {

            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}