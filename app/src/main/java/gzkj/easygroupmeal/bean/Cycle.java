package gzkj.easygroupmeal.bean;

public class Cycle {
    String cycleName;
    String cycleNum;

    public Cycle(String cycleName, String cycleNum) {
        this.cycleName = cycleName;
        this.cycleNum = cycleNum;
    }

    public String getCycleName() {
        return cycleName;
    }

    public void setCycleName(String cycleName) {
        this.cycleName = cycleName;
    }

    public String getCycleNum() {
        return cycleNum;
    }

    public void setCycleNum(String cycleNum) {
        this.cycleNum = cycleNum;
    }
}
