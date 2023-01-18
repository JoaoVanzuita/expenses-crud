package com.joaovanzuita.model;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Expense {
  private Long id;
  private String description;
  private LocalDate date;
  private double value;
  private Category category;

  public Expense(String description, LocalDate date, double value, Category category){
    this.description = description;
    this.date = date;
    this.value = value;
    this.category = category;
  }
}