import java.util.*;

public class Event {
    private String name;
    private String date; // format: MM/DD/YY
    private String time; // format: hh:mmam/pm

    private Map<String, List<String[]>> activities = new HashMap<>();
    private Map<String, List<String[]>> budgets = new HashMap<>();

    // Constructor
    public Event(String name, String date, String time) {
        this.name = name;
        this.date = date;
        this.time = time;
    }

    // ---------- Basic Info ----------
    public String getName() { return name; }
    public String getDate() { return date; }
    public String getTime() { return time; }

    public void setName(String name) { this.name = name; }
    public void setDate(String date) { this.date = date; }
    public void setTime(String time) { this.time = time; }

    // ---------- Activities ----------
    public void addActivity(String activityName, List<String[]> participants) {
        activities.put(activityName, participants);
    }

    public void updateActivity(String activityName, List<String[]> participants) {
        if (activities.containsKey(activityName)) {
            activities.put(activityName, participants);
        } else {
            addActivity(activityName, participants);
        }
    }

    public Set<String> getActivityNames() {
        return activities.keySet();
    }

    public List<String[]> getParticipantsForActivity(String activityName) {
        return activities.getOrDefault(activityName, new ArrayList<>());
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

    // ---------- Display ----------
    @Override
    public String toString() {
        return name + " (" + date + " " + time + ")";
    }
}
