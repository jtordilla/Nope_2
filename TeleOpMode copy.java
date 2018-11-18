package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class TeleOpMode extends Base {

    @Override
    public void init(){


        super.init();

        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public void start(){

        super.start();
        reset_encoders();
    }

    @Override
    public void loop(){
        super.loop();
        climber.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        climber.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        left = this.gamepad1.left_stick_y;
        right = -this.gamepad1.right_stick_y;

        leftBack.setPower(left);
        rightBack.setPower(right);
        leftFront.setPower(left);
        rightFront.setPower(right);

        if (gamepad1.left_stick_y > 0 && gamepad1.left_stick_y < 0.5) {
            leftBack.setPower(slowPower);
        } else if (gamepad1.left_stick_y > 0.5 && gamepad1.left_stick_y < 0.9) {
            leftBack.setPower(normalPower);
        } else if (gamepad1.left_stick_y > 0.9) {
            leftBack.setPower(fastPower);
        } else if (gamepad1.right_stick_y > 0 && gamepad1.right_stick_y < 0.5) {
            rightBack.setPower(-slowPower);
        } else if (gamepad1.right_stick_y > 0.5 && gamepad1.right_stick_y < 0.9) {
            rightBack.setPower(-normalPower);
        } else if (gamepad1.right_stick_y > 0.9) {
            rightBack.setPower(-fastPower);
        }

        if (gamepad1.right_trigger > 0.7) {
            intakeMove.setPower(inPower);
        } else if (gamepad1.left_trigger > 0.7) {
            intakeMove.setPower(-inPower);
        } else {
            intakeMoveStop();
        }

        if(gamepad1.x){
            climber.setPower(1);
        }else if(gamepad1.y){
            climber.setPower(-1);
        }else{
            climber.setPower(0);
        }


        if (gamepad1.dpad_up && !gamepad1.dpad_down && top == false && bottom == true && max == false) {
            moveClimber(-4900);
            top = true;
            bottom = true;
            max = false;
        } else if (gamepad1.dpad_down && !gamepad1.dpad_up && top == true && bottom == true && max == false) {
            moveClimber(4900);
            top = false;
            bottom = true;
            max = false;
        } else if (gamepad1.dpad_right && top == true && max == false) {
            moveClimber(-1600);
            top = false;
            max = true;
        } else if (gamepad1.dpad_left && top == false && max == true) {
            moveClimber(1600);
            top = true;
            max = false;
        } else {
            climbStop();
        }


        if (gamepad1.right_bumper) {
            servoTest.setPosition(1.0);
        } else if (gamepad1.left_bumper) {
            servoTest.setPosition(0.3);
        }
    }
}
