package junior_research_project;

public class Agent {
    private final int id;
    private double state; // Represents some state of the agent

    public Agent(int id) {
        this.id = id;
        this.state = Math.random(); // Initialize with a random state
    }

    public void updateState() {
        // Update the state based on some logic or interaction
        this.state += Math.random() - 0.5; // Example: Random walk
    }

    public double getState() {
        return state;
    }

    public int getId() {
        return id;
    }

}
