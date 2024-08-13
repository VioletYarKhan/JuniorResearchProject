package junior_research_project;

import java.util.ArrayList;
import java.util.List;

public class Simulation {
    private final List<Agent> agents;

    public Simulation(int numAgents) {
        agents = new ArrayList<>();
        for (int i = 0; i < numAgents; i++) {
            agents.add(new Agent(i));
        }
    }

    public void runSimulation(int numIterations) {
        for (int i = 0; i < numIterations; i++) {
            for (Agent agent : agents) {
                agent.updateState(); // Update each agent's state
            }
            // Optionally, collect data or perform analysis here
        }
    }

    public void printResults() {
        for (Agent agent : agents) {
            System.out.println("Agent " + agent.getId() + " state: " + agent.getState());
        }
    }
}
