package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.DogeCV;
import com.disnodeteam.dogecv.detectors.roverrukus.GoldAlignDetector;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public class CraterAuto extends Base {

    private int stage = 0;
    private GoldAlignDetector detector;
    static boolean climbComplete;
    static boolean turned = false;
    static double turn;

    //0 = center
    //1 = left
    //2 = right

    @Override
    public void init() {
        super.init();

        servoTest.setPosition(0.3);

        telemetry.addData("Status", "DogeCV 2018.0 - Gold Align Example");

        //set_marker_servo(ConstantVariables.K_MARKER_SERVO_UP);

        detector = new GoldAlignDetector();
        detector.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
        detector.useDefaults();

        // Optional Tuning
        detector.alignSize = 50; // How wide (in pixels) is the range in which the gold object will be aligned. (Represented by green bars in the preview)
        detector.alignPosOffset = 0; // How far from center frame to offset this alignment zone.
        detector.downscale = 0.4; // How much to downscale the input frames

        detector.areaScoringMethod = DogeCV.AreaScoringMethod.MAX_AREA; // Can also be PERFECT_AREA
        //detector.perfectAreaScorer.perfectArea = 10000; // if using PERFECT_AREA scoring
        detector.maxAreaScorer.weight = 0.005;

        detector.ratioScorer.weight = 5;
        detector.ratioScorer.perfectRatio = 1.0;

        detector.enable();
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void loop() {

        super.loop();
        telemetry.addData("IsAligned", detector.getAligned()); // Is the bot aligned with the gold mineral?
        telemetry.addData("X Pos", detector.getXPosition()); // Gold X position.
        telemetry.addData("Time: ", timer.seconds());

        switch (stage) {

            case 0://detach climber

                if (timer.seconds() < 2.38) {
                    climber.setPower(-fastSpeed);
                }

                else {
                    climber.setPower(0);
                    timer.reset();
                    stage++;
                }

                break;

            case 1://turn to face objects

                if(timer.seconds() < 1.1){ //more turn?
                    turn_degrees();
                }

                else{
                    stop_all();
                    timer.reset();
                    stage++;
                }

                break;

            case 2://go back

                if(timer.seconds() < 0.09){
                    goBack();
                }

                else{
                    stop_all();
                    timer.reset();
                    stage++;
                }

                break;

            case 3: //if aligned initially, go to case 6

                if (detector.getAligned()) {
                    stage += 3;

                }

                else {
                    timer.reset();
                    stage++;
                }

                break;

            case 4: //if yellow cube is left of aligned area

                if (detector.getXPosition() < 250 && detector.getXPosition() != 0) { //more distance?
                    if(detector.getXPosition() < 250){
                        turnLeft();
                    }
                }

                else{
                    stop_all();
                    timer.reset();
                    stage++;
                }

                break;

            case 5: //if yellow cube is right of aligned area

                if (detector.getXPosition() > 300 && detector.getXPosition() != 0) { //more distance?
                    if(timer.seconds() < 4){
                        turnRight();
                    }
                }

                else{
                    stop_all();
                    timer.reset();
                    stage++;
                }

                break;

            case 6: //move intake
                if(timer.seconds() < 0.65){
                    intakeMove.setPower(0.26); // 0.36
                }

                else{

                    stop_all();
                    timer.reset();
                    stage++;

                }

                break;

            case 7: //go straight for 1 second

                if (timer.seconds() < 0.7) {
                    goStraight();
                }

                else{

                    stop_all();
                    timer.reset();
                    stage++;

                }

                break;

            case 8: //intake back

                if(timer.seconds() < 0.65){
                    intakeMove.setPower(-0.26); //change -0.36
                }
                else{
                    stop_all();
                    timer.reset();
                    stage++;
                }

            case 9: //go straight for 1 second

                if (timer.seconds() < 0.3) {
                    goStraight();
                }

                else{

                    stop_all();
                    timer.reset();
                    stage++;

                }

                break;

            case 10: //climber

                if(timer.seconds() < 1){ //change 1.8
                    climber.setPower(fastSpeed);
                }
                else{
                    stop_all();
                    stage++;
                }

                break;


            default:

                break;
        }
    }

    @Override
    public void stop() {
        detector.disable();
    }

    public void stop_all(){
        leftBack.setPower(0);
        rightBack.setPower(0);
        leftFront.setPower(0);
        rightFront.setPower(0);
        climber.setPower(0);
        intakeMove.setPower(0);
    }
}