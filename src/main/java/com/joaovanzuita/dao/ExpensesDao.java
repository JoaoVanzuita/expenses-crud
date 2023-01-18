package com.joaovanzuita.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.joaovanzuita.infra.db.DataSource;
import com.joaovanzuita.model.Category;
import com.joaovanzuita.model.Expense;

public class ExpensesDao implements IExpensesDao {

  private static ExpensesDao instance;

  public static synchronized ExpensesDao getInstance() {

    if (instance == null) {
      instance = new ExpensesDao();
    }

    return instance;
  }

  private ExpensesDao() {
  }

  @Override
  public Expense save(Expense expense) throws SQLException {

    final String sql = "INSERT INTO expenses (description, value, date, category) VALUES (?, ?, ?, ?)";

    try (Connection connection = DataSource.getConnection()) {

      PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

      statement.setString(1, expense.getDescription());
      statement.setDouble(2, expense.getValue());
      statement.setDate(3, java.sql.Date.valueOf(expense.getDate()));
      statement.setString(4, expense.getCategory().toString());

      statement.execute();

      ResultSet result = statement.getGeneratedKeys();
      result.next();

      long generatedId = result.getLong("id");
      expense.setId(generatedId);
    }

    return expense;
  }

  @Override
  public Expense update(Expense expense) throws SQLException {

    final String sql = "UPDATE expenses SET description = ?, value = ?, date = ?, category = ? WHERE id = ?";

    try (Connection connection = DataSource.getConnection()) {

      PreparedStatement statement = connection.prepareStatement(sql);

      statement.setString(1, expense.getDescription());
      statement.setDouble(2, expense.getValue());
      statement.setDate(3, java.sql.Date.valueOf(expense.getDate()));
      statement.setString(4, expense.getCategory().toString());
      statement.setLong(5, expense.getId());

      statement.execute();
    }

    return expense;
  }

  @Override
  public void delete(Long id) throws SQLException {

    try (Connection connection = DataSource.getConnection()) {

      PreparedStatement statement = connection.prepareStatement("DELETE FROM expenses WHERE id = ?");
      statement.setLong(1, id);

      statement.execute();

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public Optional<Expense> findById(Long id) throws SQLException {

    Expense expense = null;

    final String sql = "SELECT * FROM expenses WHERE id = ?";

    try (Connection connection = DataSource.getConnection()) {

      PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      statement.setLong(1, id);

      ResultSet result = statement.executeQuery();

      if (result.next()) {
        String description = result.getString("description");
        LocalDate date = result.getDate("date").toLocalDate();
        double value = result.getDouble("value");
        Category category = Category.valueOf(result.getString("category"));

        expense = new Expense(id, description, date, value, category);
      }
    }

    return Optional.ofNullable(expense);
  }

  @Override
  public List<Expense> findAll() throws SQLException {

    List<Expense> expenses = new ArrayList<>();

    final String sql = "SELECT * FROM expenses";

    try (Connection connection = DataSource.getConnection()) {

      PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

      ResultSet result = statement.executeQuery();

      while (result.next()) {
        Long id = result.getLong("id");
        String description = result.getString("description");
        LocalDate date = result.getDate("date").toLocalDate();
        double value = result.getDouble("value");
        Category category = Category.valueOf(result.getString("category"));

        Expense expense = new Expense(description, date, value, category);
        expense.setId(id);

        expenses.add(expense);
      }
    }

    return expenses;
  }

  @Override
  public List<Expense> findByCategory(String categoryName) throws SQLException {

    List<Expense> expenses = new ArrayList<>();

    final String sql = "SELECT * FROM expenses WHERE category = ?";

    try (Connection connection = DataSource.getConnection()) {

      PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      statement.setString(1, categoryName);

      ResultSet result = statement.executeQuery();

      while (result.next()) {
        Long id = result.getLong("id");
        String description = result.getString("description");
        LocalDate date = result.getDate("date").toLocalDate();
        double value = result.getDouble("value");
        Category category = Category.valueOf(result.getString("category"));

        Expense expense = new Expense(id, description, date, value, category);

        expenses.add(expense);
      }
    }

    return expenses;
  }

}