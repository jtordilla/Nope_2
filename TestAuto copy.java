package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.DogeCV;
import com.disnodeteam.dogecv.detectors.roverrukus.GoldAlignDetector;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Base;
import org.firstinspires.ftc.teamcode.ConstantVariables;

/*
 * Created by chun on 8/8/18 for robotics boot camp 2018.
 */

@Autonomous

public class TestAuto extends Base { //CHANGE TO BaseRobot
    private int stage = 0;
    private GoldAlignDetector detector;

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
        telemetry.addData("IsAligned" , detector.getAligned()); // Is the bot aligned with the gold mineral
        telemetry.addData("X Pos" , detector.getXPosition()); // Gold X pos.
        double turnMult = 0;

        switch (stage) {
            case 0:
                detachClimber(400, 100);
                break;
            /*case 1:
                //move away from lander
                if (auto_drive(0.8, 6)) {
                    reset_encoders();
                    stage++;
                }
                break;
            case 2:
                break;
            case 3:
                if (detector.getXPosition()>280) {
                    if (auto_turn(0.4,50)) { //turns left
                        reset_encoders();
                        turnMult=-1;
                        stage++;
                    }
                } else if (detector.getXPosition()<280) {
                    if (auto_turn(-0.4, 50)) { //turns right
                        reset_encoders();
                        turnMult=1;
                        stage++;
                    }
                } else {
                    turnMult=0;
                    stage++;
                }
                break;
            case 4:
                if (auto_drive(0.8, 20)) {
                    reset_encoders();
                    stage++;
                }
                break;
            case 5:
                if (turnMult == -1 || turnMult == 1) {
                    if (auto_turn(-0.4*(turnMult), 40)) {
                        reset_encoders();
                        stage++;
                    }
                } else {
                    stage++;
                }
                break;
            case 6:
                if (auto_drive(0.8, 20)) {
                    reset_encoders();
                    stage++;
                }
                break;
            case 7:
                set_marker_servo(ConstantVariables.K_MARKER_SERVO_DOWN);
                stage++;
                break;
            case 8:
                //back out of corner
                if (auto_drive(-0.5, 8)) {
                    reset_encoders();
                    stage++;
                }
                break;
                */
            default:
                break;
        }
    }

    @Override
    public void stop() {
        detector.disable();
    }
}

