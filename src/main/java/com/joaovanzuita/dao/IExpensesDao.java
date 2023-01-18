package com.joaovanzuita.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.joaovanzuita.model.Expense;

public interface IExpensesDao {
  Expense save(Expense expense) throws SQLException;
  Expense update(Expense expense) throws SQLException;
  void delete(Long id) throws SQLException;
  Optional<Expense> findById(Long id) throws SQLException;
  List<Expense> findAll() throws SQLException;
  List<Expense> findByCategory(String categoryName) throws SQLException;
}
