package junior_research_project;

import java.util.Random;

public class Agent {
    public enum State { SUSCEPTIBLE, INFECTED, RECOVERED, VACCINATED, DEAD }
    public enum Occupation { CHILD, STUDENT, WORKER, ESSENTIAL_WORKER, RETIRED }

    private State state;
    private Occupation occupation;
    private double economicProductivity;
    private int daysInfected;
    private int age;

    public Agent() {
        this.state = State.SUSCEPTIBLE;
        this.age = new Random().nextInt(Constants.MAX_AGE);
        this.occupation = assignOccupation(null, null);
        this.economicProductivity = Math.random();
        this.daysInfected = 0;
    }

    public Agent(Agent parent1, Agent parent2) {
        this.state = State.SUSCEPTIBLE;
        this.age = 0; // Newborns start at age 0
        this.occupation = assignOccupation(parent1, parent2);
        this.economicProductivity = Math.random();
        this.daysInfected = 0;
    }

    private Occupation assignOccupation(Agent parent1, Agent parent2) {
        if (age < Constants.WORKING_AGE_MIN) {
            return Occupation.CHILD;
        } else if (age > Constants.WORKING_AGE_MAX) {
            return Occupation.RETIRED;
        } else {
            // Inherit occupation with a certain probability
            if (parent1 != null && parent2 != null && Math.random() < Constants.OCCUPATION_INHERITANCE_PROBABILITY) {
                return Math.random() < 0.5 ? parent1.occupation : parent2.occupation;
            }
            // Randomly assign occupation otherwise
            return Math.random() < 0.2 ? Occupation.ESSENTIAL_WORKER : Occupation.WORKER;
        }
    }

    public void updateState(boolean socialDistancing, boolean vaccinated) {
        if (state == State.SUSCEPTIBLE && !vaccinated) {
            double transmissionRate = Constants.TRANSMISSION_RATE;
            if (occupation == Occupation.ESSENTIAL_WORKER) {
                transmissionRate *= Constants.ESSENTIAL_WORKER_RISK;
            }
            if (age < Constants.WORKING_AGE_MIN) {
                transmissionRate *= Constants.INTERACTION_RATE_YOUTH;
            } else if (age > Constants.WORKING_AGE_MAX) {
                transmissionRate *= Constants.INTERACTION_RATE_ELDERLY;
            }
            if (Math.random() < transmissionRate * (socialDistancing ? Constants.SOCIAL_DISTANCING_EFFECT : 1)) {
                state = State.INFECTED;
            }
        }

        if (state == State.INFECTED) {
            daysInfected++;
            economicProductivity *= (Math.random());
            double mortalityRate = Constants.MORTALITY_RATE;
            if (age > Constants.WORKING_AGE_MAX) {
                mortalityRate = Constants.ELDERLY_MORTALITY_RATE;
            }
            if (Math.random() < mortalityRate) {
                state = State.DEAD;
                economicProductivity = 0;
            } else if (daysInfected >= Constants.RECOVERY_TIME && Math.random() < 0.25) {
                state = State.RECOVERED;
            }
        } else {
            this.economicProductivity *= (1 + Math.random()/4);
            if (this.economicProductivity > 1){
                economicProductivity = 1;
            }
        }

        if (vaccinated && state == State.SUSCEPTIBLE) {
            state = State.VACCINATED;
        }

        age++;
    }

    public State getState() {
        return state;
    }

    public double getEconomicProductivity() {
        return economicProductivity;
    }

    public Occupation getOccupation() {
        return occupation;
    }

    public int getAge() {
        return age;
    }
}