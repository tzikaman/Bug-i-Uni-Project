//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import javax.vecmath.Vector3d;
import simbad.sim.Agent;
import simbad.sim.LightSensor;
import simbad.sim.RangeSensorBelt;
import simbad.sim.RobotFactory;

public class MyRobot extends Agent {
    public LightSensor ll = RobotFactory.addLightSensorLeft(this);
    public LightSensor lr = RobotFactory.addLightSensorRight(this);
    public LightSensor lc = RobotFactory.addLightSensor(this);
    public RangeSensorBelt sonars;
    public boolean performStop;
    public boolean performFollow;
    public boolean performOrientation;
    public boolean performForward;
    private boolean CLOCKWISE = false;
    private int effectiveCounter = 0;
    private boolean oriented = false;
    public double lightThreshold;
    private double maxLux = 0.077;
    private double minLux = 0.0012;
    private int num_of_sonars = 360;
    private double[] previous_values = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
    private int stepCounter = 0;

    public MyRobot(Vector3d position, String name) {
        super(position, name);
        this.sonars = RobotFactory.addSonarBeltSensor(this, this.num_of_sonars);
        System.out.println(this.foundLocalMax());
    }

    public void initBehavior() {
        this.performStop = false;
        this.performFollow = false;
        this.performOrientation = true;
        this.performForward = false;
        System.out.println(this.lc.getLux());
    }

    public void performBehavior() {
        double l = this.ll.getLux();
        double c = this.lc.getLux();
        double r = this.lr.getLux();
        if (c >= this.maxLux) {
            this.performStop = true;
        }

        int min = 0;

        for(int i = 1; i < this.sonars.getNumSensors(); ++i) {
            if (this.sonars.getMeasurement(i) < this.sonars.getMeasurement(min)) {
                min = i;
            }
        }

        double maxThreshold = 1.0E-4;
        double minThreshold = 1.0E-6;
        this.lightThreshold = minThreshold + (c - this.minLux) / this.maxLux * (maxThreshold - minThreshold);
        this.lightThreshold = c / 150.0;
        if (!this.performFollow && !this.performOrientation) {
            if (this.sonars.getMeasurement(min) < 1.0) {
                this.performFollow = true;
            }

            if (min > 45 && min < 315) {
                this.performFollow = false;
            }
        }

        if (this.performFollow) {
            this.oriented = false;
            ++this.effectiveCounter;
            if (this.effectiveCounter > 30) {
                if (this.stepCounter < this.previous_values.length) {
                    this.previous_values[this.stepCounter] = c;
                    ++this.stepCounter;
                } else {
                    this.shift(c);
                    if (this.foundLocalMax()) {
                        this.performFollow = false;
                        this.performOrientation = true;
                        this.effectiveCounter = 0;
                    }
                }
            }
        }

        if (this.performOrientation && Math.abs(l - r) < this.lightThreshold && c < l && c < r) {
            this.performOrientation = false;
            this.previous_values[0] = 0.0;
            this.previous_values[1] = 0.0;
            this.stepCounter = 0;
            this.oriented = true;
            this.performForward = true;
        }

        if (this.performStop) {
            SimpleBehaviors.stop(this);
        } else if (this.performOrientation) {
            SimpleBehaviors.orientation(this);
        } else if (this.performFollow) {
            SimpleBehaviors.follow(this, this.CLOCKWISE);
        } else if (this.performForward) {
            System.out.println(c);
            SimpleBehaviors.forward(this);
        }

    }

    private void shift(double c) {
        int i;
        for(i = 0; i < this.previous_values.length - 1; ++i) {
            this.previous_values[i] = this.previous_values[i + 1];
        }

        this.previous_values[i] = c;
    }

    private boolean foundLocalMax() {
        int middleIndex = (this.previous_values.length - 1) / 2;
        boolean isLocalMax = true;

        int i;
        for(i = 0; isLocalMax && i < middleIndex; ++i) {
            isLocalMax = this.previous_values[middleIndex] > this.previous_values[i];
        }

        ++i;

        while(isLocalMax && i < this.previous_values.length) {
            isLocalMax = this.previous_values[middleIndex] > this.previous_values[i];
            ++i;
        }

        return isLocalMax;
    }
}
