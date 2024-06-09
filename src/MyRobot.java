//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import javax.vecmath.Vector3d;
import simbad.sim.Agent;
import simbad.sim.LightSensor;
import simbad.sim.RangeSensorBelt;
import simbad.sim.RobotFactory;

import java.util.Arrays;

public class MyRobot extends Agent {
    // Αισθητήρες φωτός
    public LightSensor ll = RobotFactory.addLightSensorLeft(this);
    public LightSensor lr = RobotFactory.addLightSensorRight(this);
    public LightSensor lc = RobotFactory.addLightSensor(this);

    // Αισθητήρες απόστασης
    public RangeSensorBelt sonars;


    // Μεταβλητές flags εκτέλεσης συμπεριφορών
    public boolean performStop;
    public boolean performFollow;
    public boolean performOrientation;
    public boolean performForward;

    private boolean CLOCKWISE = false;

    private int effectiveCounter = 0;

    // Κατώφλι για τον έλεγχο της ευθυγράμμισης του ρομπότ με τον στόχο
    public double lightThreshold;

    // Ανώτατη τιμή που μπορεί να λάβει κάποιος αισθητήρας φωτός του ρομποτ (από παρατήρηση)
    private double maxLux = 0.077;

    // Πλήθος διαθέσιμων αισθητήρων απόστασης
    private int num_of_sonars = 24;

    // Πλήθος αποθηκευμένων προηγούμενων τιμών του κεντρικού αισθητήρα φωτός
    private int number_of_previous_values = 11;

    // Οι προηγούμενες τιμές του κεντρικού αισθητήρα φωτός
    private double[] previous_values = new double[this.number_of_previous_values];

    // Μετρητής ο οποίος ελέγχει το γέμισμα του πίνακα previous_values με
    // τις πιο πρόσφατες τιμές του κεντρικού αισθητήρα φωτός
    private int stepCounter = 0;

    public MyRobot(Vector3d position, String name) {
        super(position, name);
        this.sonars = RobotFactory.addSonarBeltSensor(this, this.num_of_sonars);
    }

    public void initBehavior() {
        this.performStop = false;
        this.performFollow = false;
        this.performOrientation = true;
        this.performForward = false;

        // Αρχικοποίηση πίνακα previous_values με μηδενικά
        this.reset_previous_values();
    }

    public void performBehavior() {
        double l = this.ll.getLux();
        double c = this.lc.getLux();
        double r = this.lr.getLux();

        // Έλεγχος για το αν το ρομπότ έφτασε στον στόχο
        if (c >= this.maxLux) {
            this.performStop = true;
        }

        // Εύρεση του αισθητήρα απόστασης με την μικρότερη ένδειξη από κάποιο εμπόδιο
        int min = this.getSonarWithMinimumMeasurement();

        if (!this.performFollow && !this.performOrientation) {
            // Έλεγχος για το αν το ρομπότ βρίσκεται κοντά σε εμπόδιο
            if (this.sonars.getMeasurement(min) < 1.0) {

                // Έλεγχος για το αν το ρομπότ έχει χώρο να κινηθεί προς τα μπρος, ακόμα και αν ανίχνευσε
                // κάποιο εμπόδιο κοντά του
                if (this.frontAreaIsClear() == false) {
                    this.performFollow = true;
                }
            }
        }

        if (this.performFollow) {
            ++this.effectiveCounter;
            if (this.effectiveCounter > 30) {
                if (this.stepCounter < this.previous_values.length) {
                    // Ο πίνακας previous_values δεν έχει γεμίσει ακόμα. Εισήγαγε την τελευταία
                    // τιμή του c
                    this.previous_values[this.stepCounter] = c;
                    ++this.stepCounter;
                } else {
                    // Απέρριψε την παλιότερη τιμή του c και εισήγαγε την πιο πρόσφατη
                    this.shift(c);

                    if (this.foundLocalMax()) {
                        // Αν βρήκες τοπικό μέγιστο κατά την εκτέλεση της Follow, τότε σταμάτα
                        // την Follow και ξεκίνα Orientation
                        this.performFollow = false;
                        this.performOrientation = true;
                        this.effectiveCounter = 0;
                    }
                }
            }
        }

        if (this.performOrientation && this.isOriented(c, r, l)) {
            // Αν κατά τη διάρκεια της Orientation διαπίστωσες ότι το ρομπότ έχει ευθυγραμμιστεί
            // με τον στόχο, τότε σταμάτα την Orientation και ξεκίνα την Forward
            this.performOrientation = false;
            this.reset_previous_values();
            this.performForward = true;
        }

        if (this.performStop) {
            SimpleBehaviors.stop(this);
        } else if (this.performOrientation) {
            SimpleBehaviors.orientation(this);
        } else if (this.performFollow) {
            SimpleBehaviors.follow(this, this.CLOCKWISE);
        } else if (this.performForward) {
            SimpleBehaviors.forward(this);
        }
    }




    /**
     * Μετακινεί όλες τις προηγούμενες ενδείξεις του αισθητήρα c, οι οποίες είναι
     * αποθηκευμένες στον πίνακα previous_values κατά μία θέση προς τα αριστέρα.
     * Στην ουσία 'πετάει' την πιο παλιά ένδειξη, και συμπεριλαμβάνει την τελευταία ένδειξη
     * του c, η οποία εισάγεται στο τέλος του πίνακα.
     * @param c Η τελευταία (πιο πρόσφατη) ένδειξη του αισθητήρα στο κέντρο του ρομπότ
     */
    private void shift(double c) {
        int i;
        for(i = 0; i < this.previous_values.length - 1; ++i) {
            this.previous_values[i] = this.previous_values[i + 1];
        }

        this.previous_values[i] = c;
    }

    /**
     * Ελέγχει εάν το ρομπότ έχει προσεγγίσει σημείο στο οποίο παρατηρείται
     * τοπικό μέγιστο στην τιμή του κεντρικού αισθητήρα (c). Ουσιαστικά ελέγχουμε
     * αν η τιμή στη μέση του πίνακα previous_values, δηλαδή η (k+1)-οστή τιμή
     * είναι η μεγαλύτερη τιμή από όλες τις άλλες k τιμές στα αριστερά (παλιότερες)
     * και τις k τιμές στα δεξιά (πρόσφατες).
     * @return true/false Ανάλογα με το αν προσεγγίστηκε τοπικό μέγιστο (πριν k frames)
     */
    private boolean foundLocalMax() {
        int middleIndex = (this.previous_values.length - 1) / 2;
        boolean isLocalMax = true;

        int i = 0;

        while (isLocalMax && i < middleIndex) {
            isLocalMax = this.previous_values[middleIndex] > this.previous_values[i];
            i += 1;
        }

        i += 1;

        while(isLocalMax && i < this.previous_values.length) {
            isLocalMax = this.previous_values[middleIndex] > this.previous_values[i];
            i += 1;
        }

        return isLocalMax;
    }


    /**
     * Ελέγχει αν το ρομπότ έχει ευθυγραμμιστεί με την λάμπα φωτός. Αυτό συμβαίνει
     * όταν η διαφορά των πλαϊνών αισθητήρων είναι αρκετά μικρή, και όταν το ρομπότ
     * κοιτάει πρός την κατεύθυνση της λάμπας, άρα η ένδειξη του κεντρικού αισθητήρα
     * είναι μικρότερη των άλλων δύο.
     * @param c Η ένδειξη του κεντρικού αισθητήρα
     * @param r Η ένδειξη του δεξιού αισθητήρα
     * @param l Η ένδειξη του αριστερού αισθητήρα
     * @return true/false ανάλογα με το αν το ρομπότ ευθυγραμμίστηκε
     */
    private boolean isOriented(double c, double r, double l) {
        this.lightThreshold = c / 150.0;

        return Math.abs(l - r) < this.lightThreshold && c < l && c < r;
    }

    /**
     * Βρίσκει και επιστρέφει τον αριθμό του αισθητήρα ο οποίος δείχνει
     * την μικρότερη μέτρηση απόστασης από εμπόδιο.
     */
    private int getSonarWithMinimumMeasurement() {
        int min = 0;

        for(int i = 1; i < this.sonars.getNumSensors(); ++i) {
            if (this.sonars.getMeasurement(i) < this.sonars.getMeasurement(min)) {
                min = i;
            }
        }

        return min;
    }

    /**
     * Ελέγχει την ορατότητα του ρομπότ στο μπροστινό του τμήμα. Αν το ρομπότ δεν εντοπίζει
     * εμπόδια σε απόσταση εντός των 0.75 μέτρων χρησιμοποιώντας τους μπροστινούς αισθητήρες
     * απόστασης (πρόκειται για το 25% το συνολικών αισθητήρων που διαθέτει), τότε θεωρούμε
     * πως η περιοχή μπροστά του ρομπότ είναι 'καθαρή'.
     */
    private boolean frontAreaIsClear() {
        double frontSonarsPercentage = 0.25;
        int totalFrontSonars = (int) (this.num_of_sonars * frontSonarsPercentage);

        for (int i = 0; i < totalFrontSonars; i++) {
            int sonarIndex = (this.num_of_sonars -(totalFrontSonars / 2) + i) % this.num_of_sonars;
            if (this.sonars.getMeasurement(sonarIndex) < 0.75) {
                return false;
            }
        }

        return true;
    }


    private void reset_previous_values() {
        Arrays.fill(this.previous_values, 0);
        this.stepCounter = 0;
    }
}
