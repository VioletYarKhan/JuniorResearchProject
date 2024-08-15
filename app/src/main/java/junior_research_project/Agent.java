package junior_research_project;

public class Agent {
    public enum State { SUSCEPTIBLE, INFECTED, RECOVERED, VACCINATED, DEAD }

    private State state;
    private double economicProductivity;
    private int daysInfected;

    public Agent() {
        this.state = State.SUSCEPTIBLE;
        this.economicProductivity = Math.random(); // Initial productivity
        this.daysInfected = 0;
    }

    public void updateState(boolean socialDistancing, boolean vaccinated) {
        if (this.state == State.SUSCEPTIBLE && !vaccinated) {
            if (Math.random() < Constants.TRANSMISSION_RATE * (socialDistancing ? Constants.SOCIAL_DISTANCING_EFFECT : 1)) {
                this.state = State.INFECTED;
            }
        }

        if (this.state == State.INFECTED) {
            daysInfected++;
            // Decrease productivity based on infection
            this.economicProductivity *= Math.random();
            if (Math.random() < Constants.MORTALITY_RATE) {
                this.state = State.DEAD;
                this.economicProductivity = 0; // No productivity if dead
            } else if (daysInfected >= Constants.RECOVERY_TIME && (Math.random() > 0.25)) {
                this.state = State.RECOVERED;
            }
        } else {
            if ((this.economicProductivity) < 1){
                this.economicProductivity *= (1 + (Math.random()/2));
            }
            if (this.economicProductivity > 1){
                this.economicProductivity = 1;
            }
        }
    }


    public State getState() {
        return state;
    }

    public double getEconomicProductivity() {
        return economicProductivity;
    }
}
