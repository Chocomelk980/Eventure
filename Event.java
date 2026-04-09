import java.util.*;

public class Event {
    private String name;
    private Map<String, List<String[]>> activities = new HashMap<>();
    private Map<String, List<String[]>> budgets = new HashMap<>();

    public Event(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    // Activities
    public void addActivity(String activityName, List<String[]> participants) {
        activities.put(activityName, participants);
    }

    public Set<String> getActivityNames() {
        return activities.keySet();
    }

    public List<String[]> getParticipantsForActivity(String activityName) {
        return activities.getOrDefault(activityName, new ArrayList<>());
    }

    // Budgets (similar)
    public void addBudget(String budgetName, List<String[]> items) {
        budgets.put(budgetName, items);
    }

    public Set<String> getBudgetNames() {
        return budgets.keySet();
    }

    public List<String[]> getItemsForBudget(String budgetName) {
        return budgets.getOrDefault(budgetName, new ArrayList<>());
    }
}
