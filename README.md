# Java Banking System

This Java project implements a comprehensive banking system using object-oriented programming principles and design patterns. It utilizes the JDBC library to interact with a database, ensuring data integrity with ACID properties and encapsulation.

## Key Features

- **Bank Class:** Represents a bank entity with features to manage clients, accounts, and transactions.
- **Client Class:** Represents a bank client with attributes such as name, age, email, etc.
- **Transaction Handling:** Supports various banking operations like transferring money between accounts.
- **Database Interaction:** Utilizes JDBC to interact with a database, ensuring data integrity and security.
- **Encapsulation:** Ensures that data is encapsulated within classes, providing a clean and secure interface for interacting with the banking system.
- **Abstract Classes and Design Patterns:** Utilizes abstract classes and design patterns such as Singleton to ensure maintainability and scalability of the codebase.

## Usage

To use this banking system, follow these steps:

1. Ensure you have Java installed on your system.
2. Set up a MySQL database and configure the JDBC connection details in the `Model` class.
3. Compile all the Java files in the project.
4. Run the `App` class to start the banking system.

## Database Setup

1. Create a MySQL database.
2. Create tables using the SQL scripts provided in the `sql` folder.
3. Update the database connection details in the `Model` class.

## Contributors

- [Robert Connolly and Rafael Augusto]

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
