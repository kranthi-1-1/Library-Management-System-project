package com.library;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;

public class LibraryGUI extends JFrame {

    private final Color navy = new Color(35, 55, 95);
    private final Color background = new Color(245, 247, 251);

    private JPanel contentPanel;
    private CardLayout cardLayout;

    public LibraryGUI() {
        setTitle("Library Management System");
        setSize(1200, 750);
        setMinimumSize(new Dimension(950, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        createHeader();
        createSidebar();

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(background);

        add(contentPanel, BorderLayout.CENTER);

        showDashboard();
    }

    // ================= HEADER =================

    private void createHeader() {
        JLabel header = new JLabel(
                "LIBRARY MANAGEMENT SYSTEM",
                SwingConstants.CENTER
        );

        header.setFont(new Font("Arial", Font.BOLD, 25));
        header.setForeground(Color.WHITE);
        header.setBackground(navy);
        header.setOpaque(true);
        header.setBorder(
                BorderFactory.createEmptyBorder(20, 10, 20, 10)
        );

        add(header, BorderLayout.NORTH);
    }

    // ================= SIDEBAR =================

    private void createSidebar() {
        JPanel sidebar = new JPanel(new GridLayout(10, 1, 8, 8));
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(new Color(232, 237, 246));
        sidebar.setBorder(
                BorderFactory.createEmptyBorder(15, 10, 15, 10)
        );

        String[] options = {
                "Dashboard",
                "Books",
                "Members",
                "Issue Books",
                "Return Books",
                "Fine Management",
                "Current Issues",
                "Overdue Books",
                "Reports",
                "Exit"
        };

        for (String option : options) {
            JButton button = new JButton(option);
            button.setFont(new Font("Arial", Font.PLAIN, 15));
            button.setFocusPainted(false);

            button.addActionListener(e -> navigate(option));

            sidebar.add(button);
        }

        add(sidebar, BorderLayout.WEST);
    }

    private void navigate(String option) {
        switch (option) {
            case "Dashboard":
                showDashboard();
                break;
            case "Books":
                showBooks();
                break;
            case "Members":
                showMembers();
                break;
            case "Issue Books":
                showIssueBooks();
                break;
            case "Return Books":
                showReturnBooks();
                break;
            case "Fine Management":
                showFines();
                break;
            case "Current Issues":
                showCurrentIssues();
                break;
            case "Overdue Books":
                showOverdueBooks();
                break;
            case "Reports":
                showReports();
                break;
            case "Exit":
                if (JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to exit?",
                        "Exit",
                        JOptionPane.YES_NO_OPTION
                ) == JOptionPane.YES_OPTION) {
                    dispose();
                }
                break;
        }
    }

    // ================= COMMON HELPERS =================

    private JPanel createPage(String title) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        JLabel heading = new JLabel(title);
        heading.setFont(new Font("Arial", Font.BOLD, 23));
        heading.setForeground(navy);

        panel.add(heading, BorderLayout.NORTH);
        contentPanel.add(panel, title);

        return panel;
    }

    private JButton createButton(String text, Runnable action) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.addActionListener(e -> action.run());
        return button;
    }

    private DefaultTableModel createModel(String... columns) {
        return new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private JTable createTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );
        table.setAutoCreateRowSorter(true);
        return table;
    }

    private void displayQuery(
            DefaultTableModel model,
            String sql,
            Object... parameters
    ) {
        model.setRowCount(0);

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            for (int i = 0; i < parameters.length; i++) {
                ps.setObject(i + 1, parameters[i]);
            }

            try (ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData meta = rs.getMetaData();
                int count = meta.getColumnCount();

                while (rs.next()) {
                    Object[] row = new Object[count];

                    for (int i = 0; i < count; i++) {
                        row[i] = rs.getObject(i + 1);
                    }

                    model.addRow(row);
                }
            }

        } catch (SQLException ex) {
            showError(ex);
        }
    }

    private int executeUpdate(String sql, Object... parameters)
            throws SQLException {

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            for (int i = 0; i < parameters.length; i++) {
                ps.setObject(i + 1, parameters[i]);
            }

            return ps.executeUpdate();
        }
    }

    private Object queryValue(String sql, Object... parameters)
            throws SQLException {

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            for (int i = 0; i < parameters.length; i++) {
                ps.setObject(i + 1, parameters[i]);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getObject(1);
                }
                return null;
            }
        }
    }

    private void showError(Exception ex) {
        JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private String ask(String message) {
        return JOptionPane.showInputDialog(this, message);
    }

    private Integer askInt(String message) {
        String value = ask(message);

        if (value == null) {
            return null;
        }

        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid whole number."
            );
            return null;
        }
    }

    private boolean confirm(String message) {
        return JOptionPane.showConfirmDialog(
                this,
                message,
                "Confirm",
                JOptionPane.YES_NO_OPTION
        ) == JOptionPane.YES_OPTION;
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    // ================= DASHBOARD =================

    private void showDashboard() {
        JPanel panel = createPage("Dashboard");

        JPanel cards = new JPanel(new GridLayout(2, 2, 20, 20));
        cards.setBackground(Color.WHITE);

        addStatCard(cards, "Total Books",
                "SELECT COUNT(*) FROM book");

        addStatCard(cards, "Total Members",
                "SELECT COUNT(*) FROM member");

        addStatCard(cards, "Currently Issued",
                "SELECT COUNT(*) FROM issue WHERE return_date IS NULL");

        addStatCard(cards, "Unpaid Fines",
                "SELECT COUNT(*) FROM fine WHERE paid_status = FALSE");

        panel.add(cards, BorderLayout.CENTER);

        JButton refresh = createButton(
                "Refresh Dashboard",
                this::showDashboard
        );

        panel.add(refresh, BorderLayout.SOUTH);

        cardLayout.show(contentPanel, "Dashboard");
    }

    private void addStatCard(
            JPanel parent,
            String title,
            String sql
    ) {
        JPanel card = new JPanel(new BorderLayout(5, 15));
        card.setBackground(new Color(238, 243, 251));
        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(210, 220, 235)
                        ),
                        BorderFactory.createEmptyBorder(20, 20, 20, 20)
                )
        );

        JLabel name = new JLabel(title);
        name.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel value = new JLabel("Loading...");
        value.setFont(new Font("Arial", Font.BOLD, 30));
        value.setForeground(navy);

        try {
            Object result = queryValue(sql);
            value.setText(result == null ? "0" : result.toString());
        } catch (SQLException ex) {
            value.setText("Unavailable");
        }

        card.add(name, BorderLayout.NORTH);
        card.add(value, BorderLayout.CENTER);

        parent.add(card);
    }

    // ================= BOOK MANAGEMENT =================

    private void showBooks() {
        JPanel panel = createPage("Book Management");

        DefaultTableModel model = createModel(
                "Book ID", "Title", "ISBN", "Author",
                "Publisher", "Category", "Quantity"
        );

        JTable table = createTable(model);

        String sql = """
                SELECT b.book_id, b.title, b.isbn,
                       a.author_name, p.publisher_name,
                       c.category_name, b.quantity
                FROM book b
                LEFT JOIN author a ON b.author_id = a.author_id
                LEFT JOIN publisher p ON b.publisher_id = p.publisher_id
                LEFT JOIN category c ON b.category_id = c.category_id
                ORDER BY b.book_id
                """;

        Runnable refresh = () -> displayQuery(model, sql);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));

        buttons.add(createButton("Refresh", refresh));

        buttons.add(createButton("Search", () -> {
            String keyword = ask("Enter book title or ISBN:");

            if (keyword != null) {
                displayQuery(
                        model,
                        sql.replace(
                                "ORDER BY b.book_id",
                                """
                                WHERE b.title LIKE ?
                                   OR b.isbn LIKE ?
                                ORDER BY b.book_id
                                """
                        ),
                        "%" + keyword.trim() + "%",
                        "%" + keyword.trim() + "%"
                );
            }
        }));

        buttons.add(createButton("Add Book", this::addBook));

        buttons.add(createButton("Update Book", () -> {
            int id = selectedId(table, 0);

            if (id != -1) {
                updateBook(id);
                refresh.run();
            }
        }));

        buttons.add(createButton("Delete Book", () -> {
            int id = selectedId(table, 0);

            if (id != -1 && confirm(
                    "Delete book ID " + id + "?"
            )) {
                try {
                    executeUpdate(
                            "DELETE FROM book WHERE book_id = ?",
                            id
                    );

                    showMessage("Book deleted successfully.");
                    refresh.run();

                } catch (SQLException ex) {
                    showError(ex);
                }
            }
        }));

        panel.add(buttons, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        refresh.run();
        cardLayout.show(contentPanel, "Book Management");
    }

    private void addBook() {
        JTextField title = new JTextField();
        JTextField isbn = new JTextField();
        JTextField author = new JTextField();
        JTextField publisher = new JTextField();
        JTextField category = new JTextField();
        JTextField quantity = new JTextField("1");

        Object[] fields = {
                "Book title:", title,
                "ISBN:", isbn,
                "Author ID:", author,
                "Publisher ID:", publisher,
                "Category ID:", category,
                "Quantity:", quantity
        };

        if (JOptionPane.showConfirmDialog(
                this,
                fields,
                "Add New Book",
                JOptionPane.OK_CANCEL_OPTION
        ) != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            if (title.getText().isBlank()) {
                showMessage("Book title is required.");
                return;
            }

            int quantityValue = Integer.parseInt(
                    quantity.getText().trim()
            );

            if (quantityValue < 0) {
                showMessage("Quantity cannot be negative.");
                return;
            }

            Integer authorId = parseOptionalId(author.getText());
            Integer publisherId = parseOptionalId(
                    publisher.getText()
            );
            Integer categoryId = parseOptionalId(
                    category.getText()
            );

            executeUpdate(
                    """
                    INSERT INTO book
                    (title, isbn, author_id, publisher_id,
                     category_id, quantity)
                    VALUES (?, ?, ?, ?, ?, ?)
                    """,
                    title.getText().trim(),
                    emptyToNull(isbn.getText()),
                    authorId,
                    publisherId,
                    categoryId,
                    quantityValue
            );

            showMessage("Book added successfully.");

        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void updateBook(int id) {

    try {
        String sql = """
                SELECT title, isbn, author_id,
                       publisher_id, category_id, quantity
                FROM book
                WHERE book_id = ?
                """;

        String titleValue;
        String isbnValue;
        String authorValue;
        String publisherValue;
        String categoryValue;
        String quantityValue;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    showMessage("Book not found.");
                    return;
                }

                titleValue = rs.getString("title");
                isbnValue = rs.getString("isbn");

                int authorId = rs.getInt("author_id");
                authorValue = rs.wasNull()
                        ? ""
                        : String.valueOf(authorId);

                int publisherId = rs.getInt("publisher_id");
                publisherValue = rs.wasNull()
                        ? ""
                        : String.valueOf(publisherId);

                int categoryId = rs.getInt("category_id");
                categoryValue = rs.wasNull()
                        ? ""
                        : String.valueOf(categoryId);

                quantityValue = String.valueOf(
                        rs.getInt("quantity")
                );
            }
        }

        JTextField title = new JTextField(titleValue);
        JTextField isbn = new JTextField(
                isbnValue == null ? "" : isbnValue
        );
        JTextField author = new JTextField(authorValue);
        JTextField publisher = new JTextField(publisherValue);
        JTextField category = new JTextField(categoryValue);
        JTextField quantity = new JTextField(quantityValue);

        Object[] fields = {
                "Book ID:", new JLabel(String.valueOf(id)),
                "Title:", title,
                "ISBN:", isbn,
                "Author ID:", author,
                "Publisher ID:", publisher,
                "Category ID:", category,
                "Quantity:", quantity
        };

        int choice = JOptionPane.showConfirmDialog(
                this,
                fields,
                "Update Book Details",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (choice != JOptionPane.OK_OPTION) {
            return;
        }

        if (title.getText().isBlank()) {
            showMessage("Book title cannot be empty.");
            return;
        }

        int quantityNumber = Integer.parseInt(
                quantity.getText().trim()
        );

        if (quantityNumber < 0) {
            showMessage("Quantity cannot be negative.");
            return;
        }

        Integer authorId = parseOptionalId(author.getText());
        Integer publisherId = parseOptionalId(publisher.getText());
        Integer categoryId = parseOptionalId(category.getText());

        String updateSql = """
                UPDATE book
                SET title = ?,
                    isbn = ?,
                    author_id = ?,
                    publisher_id = ?,
                    category_id = ?,
                    quantity = ?
                WHERE book_id = ?
                """;

        int rows;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(updateSql)) {

            ps.setString(1, title.getText().trim());
            ps.setString(2, emptyToNull(isbn.getText()));
            ps.setObject(3, authorId);
            ps.setObject(4, publisherId);
            ps.setObject(5, categoryId);
            ps.setInt(6, quantityNumber);
            ps.setInt(7, id);

            rows = ps.executeUpdate();
        }

        if (rows > 0) {
            showMessage("Book details updated successfully.");
        } else {
            showMessage("No book was updated.");
        }

    } catch (NumberFormatException ex) {
        showMessage(
                "Author ID, Publisher ID, Category ID, "
                        + "and Quantity must contain valid numbers."
        );
    } catch (SQLException ex) {
        showError(ex);
    }
}

    // ================= MEMBER MANAGEMENT =================

    private void showMembers() {
        JPanel panel = createPage("Member Management");

        DefaultTableModel model = createModel(
                "Member ID", "Name", "Email", "Phone", "Department"
        );

        JTable table = createTable(model);

        String sql = """
                SELECT member_id, name, email, phone, department
                FROM member
                ORDER BY member_id
                """;

        Runnable refresh = () -> displayQuery(model, sql);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));

        buttons.add(createButton("Refresh", refresh));
        buttons.add(createButton("Register Member", this::addMember));

        buttons.add(createButton("Search Member", () -> {
            String keyword = ask("Enter member name or email:");

            if (keyword != null) {
                displayQuery(
                        model,
                        """
                        SELECT member_id, name, email, phone, department
                        FROM member
                        WHERE name LIKE ? OR email LIKE ?
                        ORDER BY member_id
                        """,
                        "%" + keyword.trim() + "%",
                        "%" + keyword.trim() + "%"
                );
            }
        }));

        buttons.add(createButton("Delete Member", () -> {
            int id = selectedId(table, 0);

            if (id != -1 && confirm(
                    "Delete member ID " + id + "?"
            )) {
                try {
                    executeUpdate(
                            "DELETE FROM member WHERE member_id = ?",
                            id
                    );

                    showMessage("Member deleted successfully.");
                    refresh.run();

                } catch (SQLException ex) {
                    showError(ex);
                }
            }
        }));

        buttons.add(createButton("Borrowing History", () -> {
            int id = selectedId(table, 0);

            if (id != -1) {
                showMemberHistory(id);
            }
        }));

        panel.add(buttons, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        refresh.run();
        cardLayout.show(contentPanel, "Member Management");
    }

    private void addMember() {
        JTextField name = new JTextField();
        JTextField email = new JTextField();
        JTextField phone = new JTextField();
        JTextField department = new JTextField();

        Object[] fields = {
                "Name:", name,
                "Email:", email,
                "Phone:", phone,
                "Department:", department
        };

        if (JOptionPane.showConfirmDialog(
                this,
                fields,
                "Register Member",
                JOptionPane.OK_CANCEL_OPTION
        ) != JOptionPane.OK_OPTION) {
            return;
        }

        if (name.getText().isBlank()) {
            showMessage("Member name is required.");
            return;
        }

        try {
            executeUpdate(
                    """
                    INSERT INTO member (name, email, phone, department)
                    VALUES (?, ?, ?, ?)
                    """,
                    name.getText().trim(),
                    emptyToNull(email.getText()),
                    emptyToNull(phone.getText()),
                    emptyToNull(department.getText())
            );

            showMessage("Member registered successfully.");

        } catch (SQLException ex) {
            showError(ex);
        }
    }

    private void showMemberHistory(int memberId) {
        JPanel panel = createPage(
                "Member History - " + memberId
        );

        DefaultTableModel model = createModel(
                "Issue ID", "Book Title", "Issue Date",
                "Due Date", "Return Date"
        );

        JTable table = createTable(model);

        displayQuery(
                model,
                """
                SELECT i.issue_id, b.title,
                       i.issue_date, i.due_date, i.return_date
                FROM issue i
                JOIN book b ON i.book_id = b.book_id
                WHERE i.member_id = ?
                ORDER BY i.issue_date DESC
                """,
                memberId
        );

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        cardLayout.show(
                contentPanel,
                "Member History - " + memberId
        );
    }

    // ================= ISSUE BOOKS =================

    private void showIssueBooks() {
        JPanel panel = createPage("Issue a Book");

        JPanel form = new JPanel(new GridLayout(4, 2, 10, 15));
        form.setBackground(Color.WHITE);
        form.setBorder(
                BorderFactory.createEmptyBorder(30, 50, 30, 50)
        );

        JTextField memberId = new JTextField();
        JTextField bookId = new JTextField();
        JTextField dueDate = new JTextField(
                LocalDate.now().plusDays(14).toString()
        );

        form.add(new JLabel("Member ID:"));
        form.add(memberId);

        form.add(new JLabel("Book ID:"));
        form.add(bookId);

        form.add(new JLabel("Due Date (YYYY-MM-DD):"));
        form.add(dueDate);

        JButton issueButton = createButton(
                "Issue Book",
                () -> issueBook(
                        memberId.getText(),
                        bookId.getText(),
                        dueDate.getText()
                )
        );

        form.add(new JLabel());
        form.add(issueButton);

        JLabel note = new JLabel(
                "<html>Enter an existing Member ID and Book ID. "
                        + "Default due date is 14 days from today.</html>"
        );

        panel.add(note, BorderLayout.NORTH);
        panel.add(form, BorderLayout.CENTER);

        cardLayout.show(contentPanel, "Issue a Book");
    }

    private void issueBook(
            String memberText,
            String bookText,
            String dueText
    ) {
        try {
            int memberId = Integer.parseInt(memberText.trim());
            int bookId = Integer.parseInt(bookText.trim());
            Date dueDate = Date.valueOf(dueText.trim());

            if (!dueDate.toLocalDate().isAfter(LocalDate.now())) {
                showMessage("Due date must be in the future.");
                return;
            }

            executeUpdate(
                    """
                    INSERT INTO issue
                    (member_id, book_id, issue_date, due_date, return_date)
                    VALUES (?, ?, CURDATE(), ?, NULL)
                    """,
                    memberId, bookId, dueDate
            );

            showMessage(
                    "Book issued successfully!\n"
                            + "Member ID: " + memberId
                            + "\nBook ID: " + bookId
            );

        } catch (Exception ex) {
            showError(ex);
        }
    }

    // ================= RETURN BOOKS =================

    private void showReturnBooks() {
        JPanel panel = createPage("Return a Book");

        DefaultTableModel model = createModel(
                "Issue ID", "Member ID", "Member Name",
                "Book ID", "Book Title", "Issue Date", "Due Date"
        );

        JTable table = createTable(model);

        String sql = """
                SELECT i.issue_id, m.member_id, m.name,
                       b.book_id, b.title,
                       i.issue_date, i.due_date
                FROM issue i
                JOIN member m ON i.member_id = m.member_id
                JOIN book b ON i.book_id = b.book_id
                WHERE i.return_date IS NULL
                ORDER BY i.due_date
                """;

        Runnable refresh = () -> displayQuery(model, sql);

        JButton returnButton = createButton(
                "Return Selected Book",
                () -> {
                    int issueId = selectedId(table, 0);

                    if (issueId != -1) {
                        returnBook(issueId);
                        refresh.run();
                    }
                }
        );

        JButton refreshButton = createButton("Refresh", refresh);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.add(returnButton);
        buttons.add(refreshButton);

        panel.add(buttons, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        refresh.run();
        cardLayout.show(contentPanel, "Return a Book");
    }

    private void returnBook(int issueId) {
        try {
            Object result = queryValue(
                    """
                    SELECT DATEDIFF(CURDATE(), due_date)
                    FROM issue
                    WHERE issue_id = ? AND return_date IS NULL
                    """,
                    issueId
            );

            if (result == null) {
                showMessage("This issue does not exist or is already returned.");
                return;
            }

            int overdueDays = Math.max(
                    0,
                    ((Number) result).intValue()
            );

            if (!confirm(
                    "Return issue ID " + issueId + "?\n"
                            + "Overdue days: " + overdueDays
                            + "\nEstimated fine: ₹"
                            + (overdueDays * 10)
            )) {
                return;
            }

            executeUpdate(
                    """
                    UPDATE issue
                    SET return_date = CURDATE()
                    WHERE issue_id = ? AND return_date IS NULL
                    """,
                    issueId
            );

            if (overdueDays > 0) {
                Object existingFine = queryValue(
                        "SELECT fine_id FROM fine WHERE issue_id = ?",
                        issueId
                );

                if (existingFine == null) {
                    executeUpdate(
                            """
                            INSERT INTO fine (issue_id, amount, paid_status)
                            VALUES (?, ?, FALSE)
                            """,
                            issueId, overdueDays * 10
                    );
                } else {
                    executeUpdate(
                            """
                            UPDATE fine
                            SET amount = ?, paid_status = FALSE
                            WHERE issue_id = ?
                            """,
                            overdueDays * 10, issueId
                    );
                }
            }

            showMessage("Book returned successfully.");

        } catch (Exception ex) {
            showError(ex);
        }
    }

    // ================= FINE MANAGEMENT =================

    private void showFines() {
        JPanel panel = createPage("Fine Management");

        DefaultTableModel model = createModel(
                "Fine ID", "Issue ID", "Member",
                "Book", "Amount", "Paid Status"
        );

        JTable table = createTable(model);

        String sql = """
                SELECT f.fine_id, f.issue_id, m.name,
                       b.title, f.amount, f.paid_status
                FROM fine f
                JOIN issue i ON f.issue_id = i.issue_id
                JOIN member m ON i.member_id = m.member_id
                JOIN book b ON i.book_id = b.book_id
                ORDER BY f.fine_id DESC
                """;

        Runnable refresh = () -> displayQuery(model, sql);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));

        buttons.add(createButton("Refresh", refresh));

        buttons.add(createButton("Calculate / Record Fine", () -> {
            Integer issueId = askInt("Enter Issue ID:");

            if (issueId == null) {
                return;
            }

            try {
                Object overdue = queryValue(
                        """
                        SELECT DATEDIFF(
                            COALESCE(return_date, CURDATE()),
                            due_date
                        )
                        FROM issue
                        WHERE issue_id = ?
                        """,
                        issueId
                );

                if (overdue == null) {
                    showMessage("Issue ID not found.");
                    return;
                }

                int days = Math.max(
                        0,
                        ((Number) overdue).intValue()
                );

                double amount = days * 10.0;

                Object fineId = queryValue(
                        "SELECT fine_id FROM fine WHERE issue_id = ?",
                        issueId
                );

                if (fineId == null) {
                    executeUpdate(
                            """
                            INSERT INTO fine (issue_id, amount, paid_status)
                            VALUES (?, ?, FALSE)
                            """,
                            issueId, amount
                    );
                } else {
                    executeUpdate(
                            """
                            UPDATE fine SET amount = ?
                            WHERE issue_id = ? AND paid_status = FALSE
                            """,
                            amount, issueId
                    );
                }

                showMessage(
                        "Overdue days: " + days
                                + "\nFine amount: ₹" + amount
                );

                refresh.run();

            } catch (Exception ex) {
                showError(ex);
            }
        }));

        buttons.add(createButton("Mark Fine as Paid", () -> {
            int fineId = selectedId(table, 0);

            if (fineId != -1 && confirm(
                    "Mark fine ID " + fineId + " as paid?"
            )) {
                try {
                    executeUpdate(
                            """
                            UPDATE fine
                            SET paid_status = TRUE
                            WHERE fine_id = ?
                            """,
                            fineId
                    );

                    showMessage("Fine marked as paid.");
                    refresh.run();

                } catch (SQLException ex) {
                    showError(ex);
                }
            }
        }));

        panel.add(buttons, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        refresh.run();
        cardLayout.show(contentPanel, "Fine Management");
    }

    // ================= CURRENT ISSUES =================

    private void showCurrentIssues() {
        JPanel panel = createPage("Currently Issued Books");

        DefaultTableModel model = createModel(
                "Issue ID", "Member", "Book",
                "Issue Date", "Due Date"
        );

        JTable table = createTable(model);

        Runnable refresh = () -> displayQuery(
                model,
                """
                SELECT i.issue_id, m.name, b.title,
                       i.issue_date, i.due_date
                FROM issue i
                JOIN member m ON i.member_id = m.member_id
                JOIN book b ON i.book_id = b.book_id
                WHERE i.return_date IS NULL
                ORDER BY i.due_date
                """
        );

        panel.add(
                createButton("Refresh", refresh),
                BorderLayout.NORTH
        );

        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        refresh.run();
        cardLayout.show(contentPanel, "Currently Issued Books");
    }

    // ================= OVERDUE BOOKS =================

    private void showOverdueBooks() {
        JPanel panel = createPage("Overdue Books");

        DefaultTableModel model = createModel(
                "Issue ID", "Member", "Phone", "Book",
                "Due Date", "Overdue Days", "Estimated Fine"
        );

        JTable table = createTable(model);

        Runnable refresh = () -> displayQuery(
                model,
                """
                SELECT i.issue_id, m.name, m.phone, b.title,
                       i.due_date,
                       DATEDIFF(CURDATE(), i.due_date) AS overdue_days,
                       DATEDIFF(CURDATE(), i.due_date) * 10 AS estimated_fine
                FROM issue i
                JOIN member m ON i.member_id = m.member_id
                JOIN book b ON i.book_id = b.book_id
                WHERE i.return_date IS NULL
                  AND i.due_date < CURDATE()
                ORDER BY i.due_date
                """
        );

        panel.add(
                createButton("Refresh", refresh),
                BorderLayout.NORTH
        );

        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        refresh.run();
        cardLayout.show(contentPanel, "Overdue Books");
    }

    // ================= REPORTS =================

    private void showReports() {
        JPanel panel = createPage("Library Reports");

        JPanel buttons = new JPanel(
                new GridLayout(0, 1, 10, 10)
        );

        buttons.setBackground(Color.WHITE);
        buttons.setBorder(
                BorderFactory.createEmptyBorder(20, 40, 20, 40)
        );

        buttons.add(createButton(
                "All Books",
                this::showBooks
        ));

        buttons.add(createButton(
                "All Members",
                this::showMembers
        ));

        buttons.add(createButton(
                "Currently Issued Books",
                this::showCurrentIssues
        ));

        buttons.add(createButton(
                "Overdue Books",
                this::showOverdueBooks
        ));

        buttons.add(createButton(
                "Books by Category",
                this::showCategoryReport
        ));

        buttons.add(createButton(
                "Overall Statistics",
                this::showStatistics
        ));

        panel.add(buttons, BorderLayout.CENTER);

        cardLayout.show(contentPanel, "Library Reports");
    }

    private void showCategoryReport() {
        JPanel panel = createPage("Books by Category");

        DefaultTableModel model = createModel(
                "Category", "Number of Titles", "Total Copies"
        );

        JTable table = createTable(model);

        displayQuery(
                model,
                """
                SELECT c.category_name,
                       COUNT(b.book_id) AS number_of_titles,
                       COALESCE(SUM(b.quantity), 0) AS total_copies
                FROM category c
                LEFT JOIN book b ON c.category_id = b.category_id
                GROUP BY c.category_id, c.category_name
                ORDER BY c.category_name
                """
        );

        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        cardLayout.show(contentPanel, "Books by Category");
    }

    private void showStatistics() {
        JPanel panel = createPage("Library Statistics");

        DefaultTableModel model = createModel(
                "Metric", "Value"
        );

        JTable table = createTable(model);

        String[][] queries = {
                {"Total Titles", "SELECT COUNT(*) FROM book"},
                {"Total Book Copies", "SELECT COALESCE(SUM(quantity),0) FROM book"},
                {"Total Members", "SELECT COUNT(*) FROM member"},
                {"Currently Issued", "SELECT COUNT(*) FROM issue WHERE return_date IS NULL"},
                {"Returned Books", "SELECT COUNT(*) FROM issue WHERE return_date IS NOT NULL"},
                {"Overdue Books", "SELECT COUNT(*) FROM issue WHERE return_date IS NULL AND due_date < CURDATE()"},
                {"Unpaid Fines", "SELECT COUNT(*) FROM fine WHERE paid_status = FALSE"},
                {"Outstanding Fine Amount", "SELECT COALESCE(SUM(amount),0) FROM fine WHERE paid_status = FALSE"}
        };

        try {
            for (String[] query : queries) {
                Object value = queryValue(query[1]);
                model.addRow(new Object[]{
                        query[0],
                        value == null ? 0 : value
                });
            }
        } catch (SQLException ex) {
            showError(ex);
        }

        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        cardLayout.show(contentPanel, "Library Statistics");
    }

    // ================= UTILITIES =================

    private int selectedId(JTable table, int column) {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            showMessage("Please select a row first.");
            return -1;
        }

        int modelRow = table.convertRowIndexToModel(selectedRow);

        Object value = table.getModel().getValueAt(
                modelRow,
                column
        );

        return Integer.parseInt(value.toString());
    }

    private Integer parseOptionalId(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return Integer.parseInt(value.trim());
    }

    private String emptyToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }

    // ================= MAIN =================

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName()
                );
            } catch (Exception ignored) {
                // Use the default Swing appearance.
            }

            new LibraryGUI().setVisible(true);
        });
    }
}