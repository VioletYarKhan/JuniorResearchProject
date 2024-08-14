package junior_research_project;

public class Agent {
    public enum State { SUSCEPTIBLE, INFECTED, RECOVERED, VACCINATED }

    private State state;
    private double economicProductivity;
    private int timeInfected;

    public Agent() {
        this.state = State.SUSCEPTIBLE;
        this.economicProductivity = Math.random(); // Initial productivity
    }

    public void updateState(boolean socialDistancing, boolean vaccinated) {
        if (this.state == State.SUSCEPTIBLE && !vaccinated) {
            if (Math.random() < Constants.TRANSMISSION_RATE * (socialDistancing ? Constants.SOCIAL_DISTANCING_EFFECT : 1)) {
                this.state = State.INFECTED;
                this.timeInfected = 0;
            }
        }
        if (this.state == State.INFECTED) {
            // Decrease productivity based on infection
            this.economicProductivity *= Math.random();
            this.timeInfected += 1;
        } else {
            if ((this.economicProductivity) < 1){
                this.economicProductivity *= (1 + (Math.random()/2));
            }
            if (this.economicProductivity > 1){
                this.economicProductivity = 1;
            }
        }
        // Implement vaccination logic
        if ((this.timeInfected > Constants.RECOVERY_TIME) && (Math.random() > 0.25)){
            this.state = State.SUSCEPTIBLE;
        }
    }

    public State getState() {
        return state;
    }

    public double getEconomicProductivity() {
        return economicProductivity;
    }
}
