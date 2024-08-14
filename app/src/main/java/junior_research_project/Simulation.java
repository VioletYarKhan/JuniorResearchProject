package junior_research_project;

import java.util.ArrayList;
import java.util.List;

import junior_research_project.Agent.State;


public class Simulation {
    private final List<Agent> agents;
    public int timeStep;

    public Simulation() {
        agents = new ArrayList<>();
        for (int i = 0; i < Constants.TOTAL_POPULATION; i++) {
            agents.add(new Agent());
        }
        this.timeStep = 0;
        // Infect initial agents
        for (int i = 0; i < Constants.INITIAL_INFECTED; i++) {
            agents.get(i).updateState(false, false);
        }
    }

    public void runSimulation(int totalSteps) {
        for (int i = 0; i < totalSteps; i++) {
            timeStep++;
            applyPolicies();
            updateAgents();
            calculateStats();
        }
    }

    private void applyPolicies() {
        // Implement policy changes based on timeStep
    }

    private void updateAgents() {
        for (Agent agent : agents) {
            boolean socialDistancing = (timeStep > 30 && timeStep < 60); // Example policy implementation
            boolean vaccinated = (timeStep > 60); // Example vaccination rollout time
            agent.updateState(socialDistancing, vaccinated);
        }
    }

    private void calculateStats() {
        double totalProductivity = agents.stream().mapToDouble(Agent::getEconomicProductivity).sum();
        long infected = agents.stream().filter(Agent -> Agent.getState() == State.INFECTED).count();
        System.out.println("Time Step: " + timeStep + " Total Economic Productivity: " + totalProductivity + " Infected: " + infected);
    }
}
