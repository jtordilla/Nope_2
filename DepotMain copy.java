package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.DogeCV;
import com.disnodeteam.dogecv.detectors.roverrukus.GoldAlignDetector;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public class DepotMain extends Base {

    private int stage = 0;
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

            case 0:
                if (timer.seconds() < 1.5) {
                    climber.setPower(-fastSpeed);
                } else {
                    climber.setPower(0);
                    timer.reset();
                    stage++;
                }
                break;

            case 1:

                if (detector.getAligned()) {
                    stage += 3;
                } else {
                    stage++;
                }
                break;

            case 2:
                /*if (detector.getAligned()) {
                    stage += 2;
                }*/
                if (detector.getXPosition() < 215 && detector.getXPosition() > 0) {
                    turnLeft(0.2);
                } else {
                    driveForward(0);
                    stage++;
                }
                break;

            case 3:
                /*if (detector.getAligned()) {
                    stage++;
                }*/
                if (detector.getXPosition() > 315) {
                    turnRight(0.2);
                } else {
                    driveForward(0);
                    timer.reset();
                    stage++;
                }
                break;

            case 4:
                if (timer.seconds() < 3) {
                    driveForward(slowSpeed);
                }
                else if(timer.seconds() > 3){
                    driveForward(0);

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
}