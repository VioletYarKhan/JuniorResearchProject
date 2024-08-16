package junior_research_project;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Simulation {
    private List<Agent> agents;
    private int timeStep;
    private boolean socialDistancing;
    private boolean vaccinationStarted;
    private int activeInfections;
    private int totalDeaths;

    public Simulation() {
        agents = new ArrayList<>();
        for (int i = 0; i < Constants.TOTAL_POPULATION; i++) {
            agents.add(new Agent());
        }
        timeStep = 0;
        socialDistancing = false;
        vaccinationStarted = false;
        activeInfections = 0;
        totalDeaths = 0;

        // Infect initial agents
        for (int i = 0; i < Constants.INITIAL_INFECTED; i++) {
            agents.get(i).updateState(false, false);
        }
    }

    public void runSimulation(int totalSteps) {
        for (int i = 0; i < totalSteps; i++) {
            timeStep++;
            updateAgents();
            applyPolicies();
            handleBirths();
            calculateEconomicImpact();
        }
    }

    private void applyPolicies() {
        activeInfections = (int) agents.stream()
                .filter(agent -> agent.getState() == Agent.State.INFECTED)
                .count();

        // Dynamic social distancing policy
        if (activeInfections > Constants.TOTAL_POPULATION * 0.1) {
            socialDistancing = true;
        } else if (activeInfections < Constants.TOTAL_POPULATION * 0.05) {
            socialDistancing = false;
        }

        // Dynamic vaccination policy
        if (activeInfections > Constants.TOTAL_POPULATION * 0.2) {
            vaccinationStarted = true;
        }
    }

    private void updateAgents() {
        for (Agent agent : agents) {
            agent.updateState(socialDistancing, vaccinationStarted);
            if (agent.getState() == Agent.State.DEAD) {
                totalDeaths++;
            }
        }
    }

    private void handleBirths() {
        int birthchances = (int) (agents.size());
        int births = 0;
        List<Agent> newAgents = new ArrayList<>();
        for (int i = 0; i < birthchances; i++) {
            if (Math.random() < Constants.BIRTH_RATE){
                Agent parent1 = agents.get(new Random().nextInt(agents.size()));
                Agent parent2 = agents.get(new Random().nextInt(agents.size()));
                births += 1;
                newAgents.add(new Agent(parent1, parent2)); // Newborns inherit occupation from parents
            }
        }
        agents.addAll(newAgents);
        System.out.println("Births: " + births);
    }

    private void calculateEconomicImpact() {
        double totalProductivity = agents.stream()
            .filter(agent -> agent.getState() != Agent.State.DEAD)
            .mapToDouble(Agent::getEconomicProductivity)
            .sum();
        System.out.println("Time Step: " + timeStep +
                           " Total Economic Productivity: " + totalProductivity +
                           " Active Infections: " + activeInfections +
                           " Total Deaths: " + totalDeaths +
                           " Population Size: " + agents.size());
    }
}
