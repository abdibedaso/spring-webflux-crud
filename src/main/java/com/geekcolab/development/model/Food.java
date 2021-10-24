package com.geekcolab.development.model;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "foods")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Food {
  @Id
  private String id = UUID.randomUUID().toString();
  private String restaurantID;
  private String restaurantName;
  private String title;
  private String image;
  private String description;

  @Override
  public String toString() {
    return "Food [id=" + id + ", title=" + title + ", desc=" + description + "]";
  }
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Food)) return false;

    Food employee = (Food) o;

    if (!getId().equals(employee.getId())) return false;
    if (getTitle() != null ? !getTitle().equals(employee.getTitle()) : employee.getTitle() != null)
      return false;
    if (getRestaurantName() != null ? !getRestaurantName().equals(employee.getRestaurantName()) : employee.getRestaurantName() != null)
      return false;
    return getRestaurantID().equals(employee.getRestaurantID());
  }

  @Override
  public int hashCode() {
    int result = getId().hashCode();
    result = 31 * result + (getTitle() != null ? getTitle().hashCode() : 0);
    result = 31 * result + (getRestaurantName() != null ? getRestaurantName().hashCode() : 0);
    result = 31 * result + getRestaurantID().hashCode();
    return result;
  }
  
}
