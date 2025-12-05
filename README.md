# I/O Study Case 

## Description
This project is a simple console-based application for managing users, products, and orders. All data is stored using flat files, making it suitable for learning the basics of **Java Input/Output (I/O)**, especially file handling.

The application was created as part of a learning process to practice Java I/O operations, data parsing, and modular project structure.

## Features
- **User Management**  
  Create, read, update, and delete user data.

- **Product Management**  
  Create, read, update, and delete product data.

- **Order Management**  
  Create new orders, store order history, and display existing orders.

## Technologies Used
- **Java 21**
- **Maven 3.9**

### Dependencies
Several libraries are used to support file I/O functionality:
- **Jackson (FasterXML)** – for handling JSON data.
- **Apache Commons CSV** – for reading and writing CSV files.

## Project Structure
```
.
└── src
    └── main
        └── java
            └── hehe_com
                ├── App.java
                ├── console
                ├── order
                ├── product
                └── user
```



## How to Run
1. **Compile**
   ```bash
   mvn compile
   ```
2. **Run**:
   ```bash
   mvn exec:java
   ```

---
> 😊 Feel free to explore this project, but don’t rely on it blindly. <br/>
> If you run into any errors, try fixing them yourself—it’s a great way to learn! 😉

> ☕ Don’t forget your cup of coffee today!