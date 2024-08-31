package junior_research_project;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Simulation {
    private final List<Agent> agents;
    private int timeStep;
    private boolean socialDistancing;
    private boolean vaccinationStarted;
    private int activeInfections;
    private int totalDeaths;
    private int living;
    private int deaths;
    private int births;
    private FileWriter fileWriter;

    public Simulation() {
        agents = new ArrayList<>();
        living = Constants.TOTAL_POPULATION;
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
            agents.get(i).setInfected();
        }

        // Initialize the file writer
        try {
            fileWriter = new FileWriter("simulation_output.csv");
            fileWriter.write("Timestep,Living Population,Active Infections,Economic Productivity\n");
        } catch (IOException e) {
            e.printStackTrace();
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

        // Close the file writer
        try {
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Total Population Change: " + (living - Constants.TOTAL_POPULATION));
    }

    private void applyPolicies() {
        activeInfections = (int) agents.stream()
                .filter(agent -> agent.getState() == Agent.State.INFECTED)
                .count();
    }

    private void updateAgents() {
        deaths = 0;
        for (Agent agent : agents) {
            if (agent.getState() != Agent.State.DEAD) {
                agent.updateState(socialDistancing, vaccinationStarted, agents);
                if (agent.getState() == Agent.State.DEAD) {
                    deaths++;
                    living--;
                    totalDeaths++;
                }
            }
        }
    }

    private void handleBirths() {
        int birthchances = (living);
        births = 0;
        List<Agent> newAgents = new ArrayList<>();
        for (int i = 0; i < birthchances; i++) {
            if (Math.random() < Constants.BIRTH_RATE) {
                Agent parent1 = agents.get(new Random().nextInt(agents.size()));
                Agent parent2 = agents.get(new Random().nextInt(agents.size()));
                if ((parent1.getAge() < 18 && parent2.getAge() < 18) && (parent1.getAge() < 65 && parent2.getAge() < 65) && (parent1.getState() != Agent.State.INFECTED) && (parent2.getState() != Agent.State.INFECTED)) {
                    births++;
                    living++;
                }
                newAgents.add(new Agent(parent1, parent2)); // Newborns inherit occupation from parents
            }
        }
        agents.addAll(newAgents);
        System.out.println("Population Change: " + (births - deaths));
    }

    private void calculateEconomicImpact() {
        double totalProductivity = agents.stream()
            .filter(agent -> agent.getState() != Agent.State.DEAD)
            .mapToDouble(Agent::getEconomicProductivity)
            .sum();

        // Write data to file
        try {
            fileWriter.write(timeStep + "," + living + "," + activeInfections + "," + totalProductivity + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Time Step: " + timeStep +
                           " Total Economic Productivity: " + (int) totalProductivity +
                           " Active Infections: " + activeInfections +
                           " Total Deaths: " + totalDeaths +
                           " Living: " + living);
    }
}
