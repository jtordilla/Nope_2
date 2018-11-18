package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.DogeCV;
import com.disnodeteam.dogecv.detectors.roverrukus.GoldAlignDetector;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public class TestTurn extends Base {

    private int stage = 3;
    private GoldAlignDetector detector;
    static boolean climbComplete;
    static boolean turned = false;

    @Override
    public void init() {
        super.init();

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
                    //if(timer.seconds() < 4){
                    if(detector.getXPosition() < 250){
                        turnLeft();
                    }
                    else {
                        stop_all();
                        timer.reset();
                        stage += 5;
                    }

                    break;
                }

                else{
                    stop_all();
                    timer.reset();
                    stage++;
                }

                /*else {
                    stop_all();
                    timer.reset();
                    stage++;
                }*/

                break;

            case 5: //if yellow cube is right of aligned area

                if (detector.getXPosition() > 300 && detector.getXPosition() != 0) { //more distance?
                    if(timer.seconds() < 20){
                        turnRight();
                    }
                    else {
                        stop_all();
                        timer.reset();
                        stage += 8;
                    }

                    break;
                }

                else{
                    stop_all();
                    timer.reset();
                    stage++;
                }

                /*else {
                    stop_all();
                    timer.reset();
                    stage++;
                }*/

                break;

            case 6: //move intake
                if(timer.seconds() < 0.8){
                    intakeMove.setPower(0.36);
                }

                else{

                    stop_all();
                    timer.reset();
                    stage++;

                }

                break;

            case 7: //go straight for 2 seconds

                if (timer.seconds() < 2) {
                    climber.setPower(fastSpeed);
                    goStraight();
                }

                else{

                    stop_all();
                    timer.reset();
                    stage++;

                }

                break;

            case 8: //drop marker

                servoTest.setPosition(1.0);
                stage += 9;
                break;







            //yellow on left

            case 9:
                if(timer.seconds() < 0.8){
                    intakeMove.setPower(0.36);
                }

                else{

                    stop_all();
                    timer.reset();
                    stage++;

                }

                break;

            case 10:
                if(timer.seconds() < 10){
                    goStraight();
                }
                else{
                    stop_all();
                    timer.reset();
                    stage++;
                }
                break;

            case 11:

                if(timer.seconds() < 10){ //change
                    turnRight();
                }
                else{
                    stop_all();
                    timer.reset();
                    stage++;
                }

                break;

            case 12:

                servoTest.setPosition(1.0);
                stage += 5;
                break;







            //yellow on right



            case 13:
                if(timer.seconds() < 0.8){
                    intakeMove.setPower(0.36);
                }

                else{

                    stop_all();
                    timer.reset();
                    stage++;

                }

                break;

            case 14:
                if(timer.seconds() < 10){
                    goStraight();
                }
                else{
                    stop_all();
                    timer.reset();
                    stage++;
                }

                break;

            case 15:
                if(timer.seconds() < 10){ //change
                    turnRight();
                }
                else{
                    stop_all();
                    timer.reset();
                    stage++;
                }

                break;

            case 16:

                servoTest.setPosition(1.0);
                stage++;
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