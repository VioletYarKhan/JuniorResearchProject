/*
 * This source file was generated by the Gradle 'init' task
 */
package junior_research_project;

public class Main {
    public static void main(String[] args) {
        long timer = System.currentTimeMillis();
        int numIterations = 1461;

        Simulation simulation = new Simulation();
        simulation.runSimulation(numIterations);
        System.out.println((int) ((System.currentTimeMillis() - timer)/1000) + " Seconds");
    }
}
