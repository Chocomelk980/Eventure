import java.sql.*;
import java.util.ArrayList;
import java.util.List;
@SuppressWarnings("all")
public final class EventureDatabase {
    private static final String JDBC_URL = "jdbc:sqlite:eventure.db";
    private static boolean initialized = false;

    private EventureDatabase() {
    }

    public static synchronized void initialize() throws SQLException {
        if (initialized) {
            return;
        }

        try (Connection connection = DriverManager.getConnection(JDBC_URL)) {
            configureConnection(connection);
            createSchema(connection);
        }

        initialized = true;
    }

    private static Connection connect() throws SQLException {
        Connection connection = DriverManager.getConnection(JDBC_URL);
        configureConnection(connection);
        return connection;
    }

    private static void configureConnection(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");
            statement.execute("PRAGMA busy_timeout = 5000");
        }
    }

    private static void createSchema(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS events (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "name TEXT NOT NULL," +
                            "date TEXT NOT NULL," +
                            "time TEXT NOT NULL" +
                            ")"
            );

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS activities (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "event_id INTEGER NOT NULL," +
                            "name TEXT NOT NULL," +
                            "UNIQUE(event_id, name)," +
                            "FOREIGN KEY(event_id) REFERENCES events(id) ON DELETE CASCADE" +
                            ")"
            );

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS activity_participants (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "activity_id INTEGER NOT NULL," +
                            "position INTEGER NOT NULL," +
                            "name TEXT," +
                            "student_number TEXT," +
                            "contact TEXT," +
                            "FOREIGN KEY(activity_id) REFERENCES activities(id) ON DELETE CASCADE" +
                            ")"
            );

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS budgets (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "event_id INTEGER NOT NULL," +
                            "name TEXT NOT NULL," +
                            "UNIQUE(event_id, name)," +
                            "FOREIGN KEY(event_id) REFERENCES events(id) ON DELETE CASCADE" +
                            ")"
            );

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS budget_items (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "budget_id INTEGER NOT NULL," +
                            "position INTEGER NOT NULL," +
                            "item TEXT," +
                            "amount TEXT," +
                            "price_per_item TEXT," +
                            "total_spent TEXT," +
                            "FOREIGN KEY(budget_id) REFERENCES budgets(id) ON DELETE CASCADE" +
                            ")"
            );
        }
    }

    public static List<Event> loadAllEvents() throws SQLException {
        initialize();

        List<Event> events = new ArrayList<>();
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT id, name, date, time FROM events ORDER BY id DESC"
             );
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String date = resultSet.getString("date");
                String time = resultSet.getString("time");
                events.add(new Event(id, name, date, time));
            }
        }

        return events;
    }

    public static Event createEvent(String name, String date, String time) throws SQLException {
        initialize();

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO events(name, date, time) VALUES (?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS
             )) {

            statement.setString(1, name);
            statement.setString(2, date);
            statement.setString(3, time);
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (!keys.next()) {
                    throw new SQLException("Failed to create event (no generated id).");
                }
                return new Event(keys.getInt(1), name, date, time);
            }
        }
    }

    public static Event loadEventWithDetails(int eventId) throws SQLException {
        initialize();

        try (Connection connection = connect()) {
            Event event = loadEvent(connection, eventId);
            if (event == null) {
                return null;
            }

            loadActivities(connection, event);
            loadBudgets(connection, event);
            return event;
        }
    }

    private static Event loadEvent(Connection connection, int eventId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT id, name, date, time FROM events WHERE id = ?"
        )) {
            statement.setInt(1, eventId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return null;
                }
                return new Event(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("date"),
                        resultSet.getString("time")
                );
            }
        }
    }

    private static void loadActivities(Connection connection, Event event) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT id, name FROM activities WHERE event_id = ? ORDER BY id ASC"
        )) {
            statement.setInt(1, event.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int activityId = resultSet.getInt("id");
                    String activityName = resultSet.getString("name");
                    event.addActivity(activityName, loadParticipants(connection, activityId));
                }
            }
        }
    }

    private static List<String[]> loadParticipants(Connection connection, int activityId) throws SQLException {
        List<String[]> participants = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT name, student_number, contact FROM activity_participants WHERE activity_id = ? ORDER BY position ASC"
        )) {
            statement.setInt(1, activityId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    participants.add(new String[]{
                            nullToEmpty(resultSet.getString("name")),
                            nullToEmpty(resultSet.getString("student_number")),
                            nullToEmpty(resultSet.getString("contact"))
                    });
                }
            }
        }
        return participants;
    }

    private static void loadBudgets(Connection connection, Event event) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT id, name FROM budgets WHERE event_id = ? ORDER BY id ASC"
        )) {
            statement.setInt(1, event.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int budgetId = resultSet.getInt("id");
                    String budgetName = resultSet.getString("name");
                    event.addBudget(budgetName, loadBudgetItems(connection, budgetId));
                }
            }
        }
    }

    private static List<String[]> loadBudgetItems(Connection connection, int budgetId) throws SQLException {
        List<String[]> items = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT item, amount, price_per_item, total_spent FROM budget_items WHERE budget_id = ? ORDER BY position ASC"
        )) {
            statement.setInt(1, budgetId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    items.add(new String[]{
                            nullToEmpty(resultSet.getString("item")),
                            nullToEmpty(resultSet.getString("amount")),
                            nullToEmpty(resultSet.getString("price_per_item")),
                            nullToEmpty(resultSet.getString("total_spent"))
                    });
                }
            }
        }
        return items;
    }

    public static void saveActivity(int eventId, String activityName, List<String[]> participants) throws SQLException {
        initialize();

        try (Connection connection = connect()) {
            connection.setAutoCommit(false);
            try {
                int activityId = getOrCreateActivityId(connection, eventId, activityName);
                deleteActivityParticipants(connection, activityId);
                insertActivityParticipants(connection, activityId, participants);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        }
    }

    private static int getOrCreateActivityId(Connection connection, int eventId, String activityName) throws SQLException {
        try (PreparedStatement insert = connection.prepareStatement(
                "INSERT OR IGNORE INTO activities(event_id, name) VALUES (?, ?)"
        )) {
            insert.setInt(1, eventId);
            insert.setString(2, activityName);
            insert.executeUpdate();
        }

        try (PreparedStatement select = connection.prepareStatement(
                "SELECT id FROM activities WHERE event_id = ? AND name = ?"
        )) {
            select.setInt(1, eventId);
            select.setString(2, activityName);
            try (ResultSet resultSet = select.executeQuery()) {
                if (!resultSet.next()) {
                    throw new SQLException("Failed to find activity after insert: " + activityName);
                }
                return resultSet.getInt("id");
            }
        }
    }

    private static void deleteActivityParticipants(Connection connection, int activityId) throws SQLException {
        try (PreparedStatement delete = connection.prepareStatement(
                "DELETE FROM activity_participants WHERE activity_id = ?"
        )) {
            delete.setInt(1, activityId);
            delete.executeUpdate();
        }
    }

    private static void insertActivityParticipants(Connection connection, int activityId, List<String[]> participants) throws SQLException {
        try (PreparedStatement insert = connection.prepareStatement(
                "INSERT INTO activity_participants(activity_id, position, name, student_number, contact) VALUES (?, ?, ?, ?, ?)"
        )) {
            for (int i = 0; i < participants.size(); i++) {
                String[] participant = participants.get(i);
                insert.setInt(1, activityId);
                insert.setInt(2, i);
                insert.setString(3, safeArrayValue(participant, 0));
                insert.setString(4, safeArrayValue(participant, 1));
                insert.setString(5, safeArrayValue(participant, 2));
                insert.addBatch();
            }
            insert.executeBatch();
        }
    }

    public static void saveBudget(int eventId, String budgetName, List<String[]> items) throws SQLException {
        initialize();

        try (Connection connection = connect()) {
            connection.setAutoCommit(false);
            try {
                int budgetId = getOrCreateBudgetId(connection, eventId, budgetName);
                deleteBudgetItems(connection, budgetId);
                insertBudgetItems(connection, budgetId, items);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        }
    }

    private static int getOrCreateBudgetId(Connection connection, int eventId, String budgetName) throws SQLException {
        try (PreparedStatement insert = connection.prepareStatement(
                "INSERT OR IGNORE INTO budgets(event_id, name) VALUES (?, ?)"
        )) {
            insert.setInt(1, eventId);
            insert.setString(2, budgetName);
            insert.executeUpdate();
        }

        try (PreparedStatement select = connection.prepareStatement(
                "SELECT id FROM budgets WHERE event_id = ? AND name = ?"
        )) {
            select.setInt(1, eventId);
            select.setString(2, budgetName);
            try (ResultSet resultSet = select.executeQuery()) {
                if (!resultSet.next()) {
                    throw new SQLException("Failed to find budget after insert: " + budgetName);
                }
                return resultSet.getInt("id");
            }
        }
    }

    private static void deleteBudgetItems(Connection connection, int budgetId) throws SQLException {
        try (PreparedStatement delete = connection.prepareStatement(
                "DELETE FROM budget_items WHERE budget_id = ?"
        )) {
            delete.setInt(1, budgetId);
            delete.executeUpdate();
        }
    }

    private static void insertBudgetItems(Connection connection, int budgetId, List<String[]> items) throws SQLException {
        try (PreparedStatement insert = connection.prepareStatement(
                "INSERT INTO budget_items(budget_id, position, item, amount, price_per_item, total_spent) VALUES (?, ?, ?, ?, ?, ?)"
        )) {
            for (int i = 0; i < items.size(); i++) {
                String[] item = items.get(i);
                insert.setInt(1, budgetId);
                insert.setInt(2, i);
                insert.setString(3, safeArrayValue(item, 0));
                insert.setString(4, safeArrayValue(item, 1));
                insert.setString(5, safeArrayValue(item, 2));
                insert.setString(6, safeArrayValue(item, 3));
                insert.addBatch();
            }
            insert.executeBatch();
        }
    }

    private static String safeArrayValue(String[] array, int index) {
        if (array == null || index < 0 || index >= array.length || array[index] == null) {
            return "";
        }
        return array[index];
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}

