# 📚 Library Management System

A Java-based Library Management System developed using **Java, JDBC, MySQL, Maven, and Java Swing**. The application helps manage books, library members, book issues and returns, and fine calculations through a graphical user interface and supporting database operations.

## 🎯 Project Objective

The objective of this project is to simplify library operations by organizing book records, managing member information, tracking issued books, and calculating overdue fines using a MySQL database.

## ✨ Features

* **Book Management:** Add, view, search, update, and delete books.
* **Member Management:** Register members and manage their information.
* **Book Issue:** Record book borrowing transactions.
* **Book Return:** Track returned books and update availability.
* **Fine Calculation:** Calculate overdue fines.
* **Fine Management:** Record and manage fine payments.
* **Issue Tracking:** View current and overdue issues.
* **Library Reports:** Display library statistics and category-based reports.
* **Database Integration:** Store and retrieve information using MySQL.
* **Graphical User Interface:** Provide a desktop interface using Java Swing.

*Note: The available features depend on the current implementation of each application screen.*

## 🛠️ Technologies Used

| Technology         | Purpose                                 |
| ------------------ | --------------------------------------- |
| Java               | Application programming                 |
| Java Swing         | Desktop graphical interface             |
| JDBC               | Java-to-database connectivity           |
| MySQL              | Relational database                     |
| Maven              | Dependency management and project build |
| Git                | Version control                         |
| GitHub             | Source code hosting                     |
| Visual Studio Code | Development environment                 |

## 📂 Project Structure

```text
LibraryManagementSystem/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── library/
│   │               ├── App.java
│   │               ├── LibraryGUI.java
│   │               ├── DBConnection.java
│   │               ├── BookDAO.java
│   │               ├── MemberDAO.java
│   │               ├── IssueDAO.java
│   │               ├── ReturnDAO.java
│   │               ├── FineDAO.java
│   │               └── ...
│   └── test/
│       └── java/
├── pom.xml
├── .gitignore
└── README.md
```

## 🗄️ Database

The application uses MySQL to store library information.

The database includes tables for:

* Members
* Authors
* Publishers
* Categories
* Books
* Book issue and return records
* Fines

Database relationships and constraints help maintain consistency between related records.

### Database Setup

1. Install MySQL Server and MySQL Workbench.

2. Open MySQL Workbench or another MySQL client.

3. Create the database:

   ```sql
   CREATE DATABASE library_management;
   ```

4. Create the required tables, relationships, views, procedures, functions, and triggers using your project's SQL scripts.

5. Insert sample data if required.

6. Configure the database connection in `DBConnection.java` or your local configuration.

**Important:** This repository must include the SQL setup script before other developers can recreate the complete database. Update this section with the actual SQL filename once you add it.

## ⚙️ Prerequisites

Install the following software:

* JDK 21 or another JDK version compatible with the project's Maven compiler configuration
* Apache Maven
* MySQL Server
* Visual Studio Code or another Java IDE

## ▶️ How to Run

### 1. Clone the repository

```bash
git clone https://github.com/YOUR-USERNAME/Library-Management-System.git
```

Replace `YOUR-USERNAME` and the repository name with your actual GitHub repository details.

### 2. Open the project

Open the cloned `LibraryManagementSystem` folder in VS Code.

### 3. Configure MySQL

Update the local database configuration with your MySQL connection details.

Do not commit your actual database password to GitHub.

### 4. Compile the project

Open the terminal in the directory containing `pom.xml` and run:

```bash
mvn compile
```

### 5. Launch the graphical interface

Run:

```bash
mvn exec:java "-Dexec.mainClass=com.library.LibraryGUI"
```

If Maven reports that it cannot find the `exec` goal, check that the Maven Exec Plugin is configured in `pom.xml`.

To run the console application instead, use:

```bash
mvn exec:java "-Dexec.mainClass=com.library.App"
```

## 🖥️ Application Screenshots

Add screenshots of your application below to demonstrate its interface and functionality.

### Dashboard

<!-- Add your dashboard screenshot here -->

### Book Management

<!-- Add your book management screenshot here -->

### Member Management

<!-- Add your member management screenshot here -->

### Issue and Return Management

<!-- Add your issue and return screenshots here -->

## 📚 Concepts Demonstrated

* Object-oriented programming in Java
* Database connectivity using JDBC
* CRUD operations
* SQL queries and relational database design
* Primary keys and foreign keys
* Database constraints
* Exception handling
* Java Swing components
* Maven-based project management
* Git version control

## 🔮 Future Enhancements

* Complete the remaining GUI screens and connect them to the database.
* Add search, filtering, and sorting to data tables.
* Improve input validation and error messages.
* Add authentication and role-based access.
* Export reports to PDF or CSV.
* Improve the interface design and usability.

## 👨‍💻 Author

**Student Project — Library Management System**

* GitHub: Add your GitHub profile link here.
* College: Add your college name here.
* Department: Computer Science and Engineering.

## 📄 License

No license has been specified yet. Add an appropriate license if you intend to allow others to reuse or distribute this project.
