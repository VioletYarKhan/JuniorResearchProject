package junior_research_project;

import java.util.List;
import java.util.Random;



public class Agent {
    public enum State { SUSCEPTIBLE, INFECTED, RECOVERED, VACCINATED, DEAD }
    public enum Occupation { CHILD, STUDENT, WORKER, ESSENTIAL_WORKER, RETIRED }

    private State state;
    private Occupation assignedOccupation;
    private Occupation occupation;
    private double economicProductivity;
    private int daysInfected;
    private double age;
    private int daysRecovered;
    private Agent interactedAgent;
    private final int yearsHigherEducation;
    private int timesInfected;
    private int daysSusceptible;


    public Agent() {
        this.state = State.SUSCEPTIBLE;
        this.age = new Random().nextDouble(Constants.WORKING_AGE_MAX);
        this.assignedOccupation = assignOccupation(null, null);
        this.occupation = updateOccupation(this.assignedOccupation);
        this.economicProductivity = 0;
        if (this.occupation == Occupation.ESSENTIAL_WORKER || this.occupation == Occupation.WORKER){
            this.economicProductivity = Math.random();
        }
        this.daysInfected = 0;
        this.daysRecovered = 0;
        this.yearsHigherEducation = new Random().nextInt(Constants.MAX_STUDENT_AGE_UPPER_BOUND) - Constants.MAX_STUDENT_AGE_LOWER_BOUND;
        this.timesInfected = 0;
        this.daysSusceptible = 0;
    }

    public Agent(Agent parent1, Agent parent2) {
        this.state = State.SUSCEPTIBLE;
        this.age = 0; // Newborns start at age 0
        this.occupation = assignOccupation(parent1, parent2);
        this.economicProductivity = 0;
        this.daysInfected = 0;
        this.daysRecovered = 0;
        this.timesInfected = 0;
        this.daysSusceptible = 0;
        this.yearsHigherEducation = (parent1.getEducation() + parent2.getEducation())/2;
    }

    private Occupation updateOccupation(Occupation adultOccupation) {
        if (age < Constants.WORKING_AGE_MIN) {
            return Occupation.CHILD;
        } else if (age > Constants.WORKING_AGE_MAX) {
            return Occupation.RETIRED;
        } else if (age < yearsHigherEducation + Constants.MIN_STUDENT_AGE){
            return Occupation.STUDENT;
        } else {
            return adultOccupation;
        }
    }

    private Occupation assignOccupation(Agent parent1, Agent parent2){
        // Inherit occupation with a certain probability
        if (parent1 != null && parent2 != null && Math.random() < Constants.OCCUPATION_INHERITANCE_PROBABILITY) {
            return Math.random() < 0.5 ? parent1.occupation : parent2.occupation;
        }
        // Randomly assign occupation otherwise
        return Math.random() < 0.2 ? Occupation.ESSENTIAL_WORKER : Occupation.WORKER;
    }

    public void setInfected(){
        timesInfected ++;
        this.state = State.INFECTED;
    }
    
    public void updateState(boolean socialDistancing, boolean vaccinated, List<Agent> agents) {
        interactedAgent = agents.get(new Random().nextInt(agents.size()));
        if (state == State.SUSCEPTIBLE && !vaccinated){
            this.daysSusceptible ++;
            if(this.daysSusceptible > (365*(Math.random()+1)) && this.timesInfected > 0){
                this.timesInfected --;
            }
            if (interactedAgent.getState() == State.INFECTED) {
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
                    this.daysInfected = 0;
                    this.timesInfected ++;
                }
            }
        }

        if (state == State.INFECTED) {
            if ((interactedAgent.getState() == State.SUSCEPTIBLE) && (this.daysInfected > Constants.PRESYMPTOMS_TIME && this.daysInfected < Constants.RECOVERY_TIME)) {
                double transmissionRate = Constants.TRANSMISSION_RATE * Constants.INFECTED_INTERACTION_LOSS;
                if (interactedAgent.getOccupation() == Occupation.ESSENTIAL_WORKER) {
                    transmissionRate *= Constants.ESSENTIAL_WORKER_RISK;
                }
                if (interactedAgent.getAge() < Constants.WORKING_AGE_MIN) {
                    transmissionRate *= Constants.INTERACTION_RATE_YOUTH;
                } else if (interactedAgent.getAge() > Constants.WORKING_AGE_MAX) {
                    transmissionRate *= Constants.INTERACTION_RATE_ELDERLY;
                }
                if (Math.random() < transmissionRate * (socialDistancing ? Constants.SOCIAL_DISTANCING_EFFECT : 1)) {
                    interactedAgent.state = State.INFECTED;
                    interactedAgent.daysInfected = 0;
                    interactedAgent.timesInfected ++;
                }
            }
            daysInfected++;
            economicProductivity *= (Math.random() / 8);
            double mortalityRate = Constants.MORTALITY_RATE;
            if (age > Constants.WORKING_AGE_MAX) {
                mortalityRate = Constants.ELDERLY_MORTALITY_RATE;
            }
            if (Math.random() < mortalityRate) {
                state = State.DEAD;
                economicProductivity = 0;
            } else if (daysInfected >= Constants.RECOVERY_TIME && Math.random() < 0.25) {
                state = State.RECOVERED;
                daysRecovered = 0;
            }
        } else if (this.occupation == Occupation.ESSENTIAL_WORKER || this.occupation == Occupation.WORKER){
            this.economicProductivity *= (1 + Math.random()/8);
            if (this.economicProductivity > 1){
                economicProductivity = 1;
            }
        }

        if (vaccinated && state == State.SUSCEPTIBLE) {
            state = State.VACCINATED;
        }

        if (state == State.RECOVERED) {
            daysRecovered ++;
            if (daysRecovered >= Constants.RECOVERED_IMMUNITY_TIME && Math.random() < (0.5 / (timesInfected + 1))) {
                state = State.SUSCEPTIBLE;
                daysSusceptible = 0;
            }
        }
        this.occupation = updateOccupation(this.assignedOccupation);
        this.age += 1/365.25;
        if ((this.age >= Constants.WORKING_AGE_MAX) || (this.age <= Constants.MIN_STUDENT_AGE)){
            if (Math.random() < 0.0003 * (this.age/Constants.MAX_AGE)){
                this.state = State.DEAD;
            }
        }
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

    public double getAge() {
        return age;
    }

    public int getEducation(){
        return yearsHigherEducation;
    }
}