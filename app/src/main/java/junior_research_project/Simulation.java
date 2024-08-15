package junior_research_project;

import java.util.ArrayList;
import java.util.List;

import junior_research_project.Agent.State;


public class Simulation {
    private final List<Agent> agents;
    private int timeStep;
    private boolean socialDistancing;
    private boolean vaccinationStarted;

    public Simulation() {
        agents = new ArrayList<>();
        for (int i = 0; i < Constants.TOTAL_POPULATION; i++) {
            agents.add(new Agent());
        }
        this.timeStep = 0;
        this.socialDistancing = false;
        this.vaccinationStarted = false;

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
            calculateEconomicImpact();
        }
    }

    private void applyPolicies() {
        // Social distancing starts at time step 30
        if (timeStep == 30) {
            socialDistancing = true;
        }

        // Vaccination starts at time step 60
        if (timeStep == 60) {
            vaccinationStarted = true;
        }

    }

    private void updateAgents() {
        for (Agent agent : agents) {
            agent.updateState(socialDistancing, vaccinationStarted);
        }
    }

    private void calculateEconomicImpact() {
        double totalProductivity = agents.stream()
            .mapToDouble(Agent::getEconomicProductivity)
            .sum();
        long infected = agents.stream().filter(Agent -> Agent.getState() == State.INFECTED).count();
        long dead = agents.stream().filter(Agent -> Agent.getState() == State.DEAD).count();
        System.out.println("Time Step: " + timeStep + " Total Economic Productivity: " + totalProductivity + " Infected: " + infected + " Dead: " + dead);
        // System.out.println("Economic Productivity per capita: " + totalProductivity/Constants.TOTAL_POPULATION + " Infected percent: " + Math.round(((double) infected/(double) Constants.TOTAL_POPULATION)*100));
    }
}
