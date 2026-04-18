import java.math.BigDecimal;
import java.util.*;

public class Event {
    private static final String DEFAULT_ACTIVITY_STATUS = "Open";

    private final int id;
    private String name;
    private String date; // format: MM/DD/YY
    private String time; // format: hh:mmam/pm
    private String totalBudget;

    private Map<String, List<String[]>> activities = new LinkedHashMap<>();
    private Map<String, String> activityStatuses = new HashMap<>();
    private Map<String, List<String[]>> budgets = new LinkedHashMap<>();

    // Constructor
    public Event(int id, String name, String date, String time) {
        this(id, name, date, time, "0");
    }

    public Event(int id, String name, String date, String time, String totalBudget) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.totalBudget = normalizeBudgetValue(totalBudget);
    }

    // ---------- Basic Info ----------
    public int getId() { return id; }
    public String getName() { return name; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getTotalBudget() { return totalBudget; }

    public void setName(String name) { this.name = name; }
    public void setDate(String date) { this.date = date; }
    public void setTime(String time) { this.time = time; }
    public void setTotalBudget(String totalBudget) { this.totalBudget = normalizeBudgetValue(totalBudget); }

    // ---------- Activities ----------
    public void addActivity(String activityName, List<String[]> participants) {
        addActivity(activityName, participants, DEFAULT_ACTIVITY_STATUS);
    }

    public void addActivity(String activityName, List<String[]> participants, String status) {
        activities.put(activityName, participants);
        activityStatuses.put(activityName, normalizeActivityStatus(status));
    }

    public void updateActivity(String activityName, List<String[]> participants) {
        updateActivity(activityName, participants, getActivityStatus(activityName));
    }

    public void updateActivity(String activityName, List<String[]> participants, String status) {
        if (activities.containsKey(activityName)) {
            activities.put(activityName, participants);
            activityStatuses.put(activityName, normalizeActivityStatus(status));
        } else {
            addActivity(activityName, participants, status);
        }
    }

    public Set<String> getActivityNames() {
        return activities.keySet();
    }

    public List<String[]> getParticipantsForActivity(String activityName) {
        return activities.getOrDefault(activityName, new ArrayList<>());
    }

    public String getActivityStatus(String activityName) {
        return normalizeActivityStatus(activityStatuses.get(activityName));
    }

    public void setActivityStatus(String activityName, String status) {
        if (activities.containsKey(activityName)) {
            activityStatuses.put(activityName, normalizeActivityStatus(status));
        }
    }

    // ---------- Budgets ----------
    public void addBudget(String budgetName, List<String[]> items) {
        budgets.put(budgetName, items);
    }

    public void updateBudget(String budgetName, List<String[]> items) {
        if (budgets.containsKey(budgetName)) {
            budgets.put(budgetName, items);
        } else {
            addBudget(budgetName, items);
        }
    }

    public Set<String> getBudgetNames() {
        return budgets.keySet();
    }

    public List<String[]> getItemsForBudget(String budgetName) {
        return budgets.getOrDefault(budgetName, new ArrayList<>());
    }

    public BigDecimal getTotalBudgetAmount() {
        return parseAmount(totalBudget);
    }

    public BigDecimal getTotalSpentAmount() {
        BigDecimal totalSpent = BigDecimal.ZERO;
        for (List<String[]> items : budgets.values()) {
            for (String[] item : items) {
                if (item != null && item.length > 3) {
                    totalSpent = totalSpent.add(parseAmount(item[3]));
                }
            }
        }
        return totalSpent;
    }

    public BigDecimal getRemainingBudgetAmount() {
        return getTotalBudgetAmount().subtract(getTotalSpentAmount());
    }

    // ---------- Display ----------
    @Override
    public String toString() {
        return name + " (" + date + " " + time + ")";
    }

    private String normalizeActivityStatus(String status) {
        return "Closed".equalsIgnoreCase(status) ? "Closed" : DEFAULT_ACTIVITY_STATUS;
    }

    private String normalizeBudgetValue(String value) {
        return value == null || value.trim().isEmpty() ? "0" : value.trim();
    }

    private BigDecimal parseAmount(String value) {
        try {
            return new BigDecimal(normalizeBudgetValue(value).replace(",", ""));
        } catch (NumberFormatException ex) {
            return BigDecimal.ZERO;
        }
    }
}
