package org.WaialuaRobotics359.robot.commands.autonomous.AutoScoring;

import org.WaialuaRobotics359.robot.commands.AutoCommands.PickupPositionAuto;
import org.WaialuaRobotics359.robot.commands.SetPoints.Pickup.Upright;
import org.WaialuaRobotics359.robot.commands.SetPoints.Scoring.*;
import org.WaialuaRobotics359.robot.commands.autonomous.*;
import org.WaialuaRobotics359.robot.subsystems.*;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class LowPickupStow extends SequentialCommandGroup {

    public LowPickupStow (Intake s_Intake, Arm s_Arm, Wrist s_Wrist, Pivot s_Pivot, Leds s_Leds, Flight s_Flight) {


        addCommands(new SequentialCommandGroup(
            new PickupPositionAuto(s_Arm, s_Intake, s_Flight , s_Wrist, s_Leds, s_Pivot),
            new Upright(s_Intake, s_Arm, s_Leds, s_Flight, s_Wrist, s_Pivot)
        ));
    }
}
