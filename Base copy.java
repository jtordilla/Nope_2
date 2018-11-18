package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

public class Base extends OpMode {
    
    public DcMotor leftBack, rightBack, leftFront, rightFront, intakeMove, climber;
    public Servo servoTest;
    public ElapsedTime timer = new ElapsedTime();
    public double Pin = 1.0;
    public double climbPower = 1.0;

    static public boolean extended = false;
    static public boolean inside = true;
    static public boolean top = false;
    static public boolean bottom = true;

    static public boolean max = false;

    double left = 0;
    double right = 0;

    double intakeTrigger = 1.0;

    double inPower = 0.3;

    double slowPower = 0.4;
    double normalPower = 0.7;
    double fastPower = 1.0;

    double intakePower = 0.7;

    double stopPower = 0;

    public double slowSpeed = 0.4;
    public double normalSpeed = 0.8;
    public double fastSpeed = 1.0;

    public double dropPosition = 1.0;

    @Override

    public void init() {
        timer.reset();
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");

        intakeMove = hardwareMap.get(DcMotor.class, "intakeMove");

        climber = hardwareMap.get(DcMotor.class, "climber");

        servoTest = hardwareMap.get(Servo.class, "servoTest");

        /*leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);*/

        intakeMove = hardwareMap.get(DcMotor.class, "intakeMove");

        climber = hardwareMap.get(DcMotor.class, "climber");
    }

    @Override
    public void start(){
        timer.reset();
       //reset_encoders();
    }

    @Override
    public void loop() {

    }

    public void set_marker_servo(double pos) {
        servoTest.setPosition(pos);
    }


    public void climb(double power) {
        double speed = Range.clip(power, -1, 1);

    /*if (get_climb_motor_enc() >= ConstantVariables.K_CLIMB_MAX) {
        speed = Range.clip(speed, -1, 0);
    } else if (get_climb_motor_enc() <= ConstantVariables.K_CLIMB_MIN) {
        speed = Range.clip(speed, 0, 1);
    }*/
        climber.setPower(-speed);
    }

    public boolean auto_drive(double power, double inches) {
        double TARGET_ENC = ConstantVariables.K_PPIN_DRIVE * inches;
        telemetry.addData("Target_enc: ", TARGET_ENC);
        double left_speed = power;
        double right_speed = -power;
    /*double error = -get_left_drive_motor_enc() - get_right_drive_motor_enc();

    error /= ConstantVariables.K_DRIVE_ERROR_P;
    left_speed -= error;
    right_speed += error;*/

        left_speed = Range.clip(left_speed, -1, 1);
        right_speed = Range.clip(right_speed, -1, 1);
        leftBack.setPower(left_speed);
        leftFront.setPower(left_speed);
        rightBack.setPower(right_speed);
        rightFront.setPower(right_speed);


        if (Math.abs(get_left_drive_motor_enc()) >= TARGET_ENC && Math.abs(get_right_drive_motor_enc()) >= TARGET_ENC) {
            leftBack.setPower(0);
            leftFront.setPower(0);
            rightBack.setPower(0);
            rightFront.setPower(0);
            return true;
        }
        return false;
    }

    public boolean auto_turn(double power, double degrees) {
        double TARGET_ENC = Math.abs(ConstantVariables.K_PPDEG_DRIVE * degrees);
        telemetry.addData("D99 TURNING TO ENC: ", TARGET_ENC);

        if (Math.abs(get_left_drive_motor_enc()) >= TARGET_ENC && Math.abs(get_right_drive_motor_enc()) >= TARGET_ENC) {
            leftBack.setPower(0);
            leftFront.setPower(0);
            rightBack.setPower(0);
            rightFront.setPower(0);
            return true;
        } else {
            leftBack.setPower(-power);
            leftFront.setPower(-power);
            rightBack.setPower(-power);
            rightFront.setPower(-power);
        }
        return false;
    }

    public void reset_climb_encoders() {
        climber.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        climber.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public int get_left_drive_motor_enc() {
        if (leftBack.getMode() != DcMotor.RunMode.RUN_USING_ENCODER) {
            leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        return leftBack.getCurrentPosition();
    }

    public int get_right_drive_motor_enc() {
        if (rightBack.getMode() != DcMotor.RunMode.RUN_USING_ENCODER) {
            rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        return rightBack.getCurrentPosition();
    }

    public int get_climb_motor_enc() {
        if (climber.getMode() != DcMotor.RunMode.RUN_USING_ENCODER) {
            climber.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        return climber.getCurrentPosition();
    }



    /*public void moveClimber(int distance){

        //reset encoders

        climber.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //set target position

        climber.setTargetPosition(distance);

        //set run to position

        climber.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //set power

        climber.setPower(climbPower);


        while(climber.isBusy()){
            //waits
        }

        //stops and reverts to normal

        climbStop();

        climber.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }*/

    public void intakeMoveStop(){
        intakeMove.setPower(0);
    }

    public void climbStop(){
        climber.setPower(0);
    }

    public void drive(double power){
        leftBack.setPower(power);
        rightBack.setPower(power);
        leftFront.setPower(power);
        rightFront.setPower(power);
    }

    public void stopDrive(){
        driveForward(0, 0);
    }

    /*public void turnRight(double power) {
        leftBack.setPower(power);
        rightBack.setPower(-power);
        leftFront.setPower(power);
        rightFront.setPower(-power);
    }

    public void turnLeft(double power) {
        leftBack.setPower(power);
        rightBack.setPower(power);
        leftFront.setPower(power);
        rightFront.setPower(power);
    }*/

    /*public void detachClimber(int distance){

        climber.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        climber.setTargetPosition(distance);
        climber.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        moveClimber(normalSpeed);

        while (climber.isBusy()) {
            //waits
        }

        stopRobot();

        climber.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }*/

    /*

    public void forwardDistance(double power, int distance) {

        //reset encoders

        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //set target position

        leftBack.setTargetPosition(-distance);
        rightBack.setTargetPosition(distance);
        leftFront.setTargetPosition(-distance);
        rightFront.setTargetPosition(distance);

        //set run to position

        leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //set power

        driveForward(power);

        while (leftBack.isBusy() && rightBack.isBusy() && leftFront.isBusy() && rightFront.isBusy()) {
            //waits
        }

        //stops and reverts to normal

        stopRobot();

        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }*/

    /*
    public void backDistance(double power, int distance) {

        //reset encoders

        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //set target position

        leftBack.setTargetPosition(distance);
        rightBack.setTargetPosition(-distance);
        leftFront.setTargetPosition(distance);
        rightFront.setTargetPosition(-distance);

        //set run to position

        leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //set power

        driveBack(power);

        while (leftBack.isBusy() && rightBack.isBusy() && leftFront.isBusy() && rightFront.isBusy()) {
            //waits
        }

        //stops and reverts to normal

        stopRobot();

        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }*/

    /*

    public void leftDistance(double power, int distance) {

        //reset encoders

        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //set target position

        leftBack.setTargetPosition(distance);
        rightBack.setTargetPosition(distance);
        leftFront.setTargetPosition(distance);
        rightFront.setTargetPosition(distance);

        //set run to position

        leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //set power

        driveForward(power);

        while (leftBack.isBusy() && rightBack.isBusy() && leftFront.isBusy() && rightFront.isBusy()) {
            //waits
        }

        //stops and reverts to normal

        stopRobot();

        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }
    */

    /*

    public void rightDistance(double power, int distance) {

        //reset encoders

        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //set target position

        leftBack.setTargetPosition(-distance);
        rightBack.setTargetPosition(-distance);
        leftFront.setTargetPosition(-distance);
        rightFront.setTargetPosition(-distance);

        //set run to position

        leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //set power

        driveForward(power);

        while (leftBack.isBusy() && rightBack.isBusy() && leftFront.isBusy() && rightFront.isBusy()) {
            //waits
        }

        //stops and reverts to normal

        stopRobot();

        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    */

    public void driveBack(double power) {
        leftBack.setPower(-normalSpeed);
        rightBack.setPower(normalSpeed);
        leftFront.setPower(-normalSpeed);
        rightFront.setPower(normalSpeed);
    }

    public void moveClimber(double power) {
        climber.setPower(-power);
    }

    public void stopRobot() {
        leftBack.setPower(0);
        rightBack.setPower(0);
        leftFront.setPower(0);
        rightFront.setPower(0);
    }

    public void dropMarker() {
        servoTest.setPosition(dropPosition);
    }

    public int ticksPerInch(int inchesDecimal) {
        return inchesDecimal * 89;
    }

    public int seconds(int seconds) {
        return seconds * 1000;
    }

    /*
    public void detachAndTurn(){
        detachClimber(-4900);
        detachClimber(900);
        leftDistance(normalSpeed, ticksPerInch(13));
        detachClimber(5800);
        climbStop();
    }
    */

    public void moveClimber(int distance){

        climber.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        climber.setTargetPosition(distance);
        climber.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        moveClimber(normalSpeed);

        while (climber.isBusy()) {
            //waits
        }

        stopRobot();

        climber.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    public void detachClimber(int up, int max){
        moveClimber(up);
        moveClimber(max);
        moveClimber(-(up+max));
    }

    public void reset_encoders() {
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        intakeMove.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        climber.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intakeMove.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        climber.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void driveForward(double leftPower, double rightPower) {
        leftBack.setPower(-leftPower);
        rightBack.setPower(rightPower);
        leftFront.setPower(-leftPower);
        rightFront.setPower(rightPower);

        Range.clip(leftPower, -1, 1);
        Range.clip(rightPower, -1, 1);
    }

    public void turnRight() {
        leftBack.setPower(-0.2); //0.3
        rightBack.setPower(-0.2); //0.3
        leftFront.setPower(-0.2);
        rightFront.setPower(-0.2);
    }

    public void turnLeft() {
        leftBack.setPower(0.2);
        rightBack.setPower(0.2);
        leftFront.setPower(0.2);
        rightFront.setPower(0.2);
    }

    public void goStraight() {
        leftBack.setPower(-1);
        rightBack.setPower(1);
        leftFront.setPower(-1);
        rightFront.setPower(1);
    }

    public void goBack(){
        leftBack.setPower(1);
        rightBack.setPower(-1);
        leftFront.setPower(1);
        rightFront.setPower(-1);
    }

    public void turn_degrees(){
        leftBack.setPower(0.5);
        rightBack.setPower(0.5);
        leftFront.setPower(0.5);
        rightFront.setPower(0.5);
    }
}
