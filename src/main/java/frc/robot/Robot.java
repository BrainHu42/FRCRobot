// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
// import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.motorcontrol.Spark;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the
 * name of this class or
 * the package after creating this project, you must also update the manifest
 * file in the resource
 * directory.
 */
public class Robot extends TimedRobot {

	/////////////////
	// Drivetrain Stuff
	//////////////////
	private CANSparkMax leftFrontMotor = new CANSparkMax(Constants.LEFT_FRONT_MOTOR_ID, MotorType.kBrushed);
	private CANSparkMax leftBackMotor = new CANSparkMax(Constants.LEFT_BACK_MOTOR_ID, MotorType.kBrushed);
	private CANSparkMax rightFrontMotor = new CANSparkMax(Constants.RIGHT_FRONT_MOTOR_ID, MotorType.kBrushed);
	private CANSparkMax rightBackMotor = new CANSparkMax(Constants.RIGHT_BACK_MOTOR_ID, MotorType.kBrushed);

	private MotorControllerGroup leftMotorGroup = new MotorControllerGroup(leftFrontMotor, leftBackMotor);
	private MotorControllerGroup rightMotorGroup = new MotorControllerGroup(rightFrontMotor, rightBackMotor);

	private DifferentialDrive difDrive = new DifferentialDrive(rightMotorGroup, leftMotorGroup);

	// private final PWMSparkMax m_leftDrive = new PWMSparkMax(0);
	// private final PWMSparkMax m_rightDrive = new PWMSparkMax(1);
	// private final DifferentialDrive m_robotDrive = new
	// DifferentialDrive(m_leftDrive, m_rightDrive);

	private final XboxController controller = new XboxController(0);

	private final Timer m_timer = new Timer();

	// Intake
	// private boolean toggleIntake = false;
	private Spark intakeMotor = new Spark(Constants.INTAKE_MOTOR_ID);

	// Transition
	private CANSparkMax transitionMotor = new CANSparkMax(Constants.TRANSITION_MOTOR_ID, MotorType.kBrushed);
	private boolean toggleTransition = false;

	private Spark shooterMotor = new Spark(Constants.SHOOTER_MOTOR_ID);
	private boolean toggleShooter = false;

	/**
	 * This function is run when the robot is first started up and should be used
	 * for any
	 * initialization code.
	 */
	@Override
	public void robotInit() {
		// Invert one side of the drivetrain so that positive voltages result in both
		// sides moving forward.
		rightMotorGroup.setInverted(true);
	}

	/** This function is run once each time the robot enters autonomous mode. */
	@Override
	public void autonomousInit() {
		m_timer.reset();
		m_timer.start();
	}

	/** This function is called periodically during autonomous. */
	@Override
	public void autonomousPeriodic() {
		// Drive for 2 seconds
		if (m_timer.get() < 2.0) {
			difDrive.arcadeDrive(0.5, 0.0); // drive forwards half speed
		} else {
			difDrive.stopMotor(); // stop robot
		}
	}

	/**
	 * This function is called once each time the robot enters teleoperated mode.
	 */
	@Override
	public void teleopInit() {

		// Insert intake deployment here
	}

	/** This function is called periodically during teleoperated mode. */
	@Override
	public void teleopPeriodic() {
		// difDrive.tankDrive(rightJoystick.getY(), leftJoystick.getY());
		difDrive.arcadeDrive(controller.getLeftY(), controller.getLeftX());

		intakeMotor.set(controller.getLeftTriggerAxis());

		if (controller.getAButton()) {
			toggleShooter = !toggleShooter;
		}

		if (controller.getRightStickButton()) {
			toggleTransition = !toggleTransition;
		}

		if (toggleShooter) {
			shooterMotor.set(0.5);
		} else {
			shooterMotor.stopMotor();
		}

		// Intake --> Shooter
		if (toggleTransition) {
			transitionMotor.set(0.5);
		} else {
			transitionMotor.stopMotor();
		}

	}

	/** This function is called once each time the robot enters test mode. */
	@Override
	public void testInit() {
	}

	/** This function is called periodically during test mode. */
	@Override
	public void testPeriodic() {
	}
}
