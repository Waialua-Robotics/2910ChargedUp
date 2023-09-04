package org.WaialuaRobotics359.robot.commands.autonomous.AutoScoring;

import org.WaialuaRobotics359.robot.commands.AutoCommands.HighPositionAuto;
import org.WaialuaRobotics359.robot.commands.AutoCommands.MidPositionAuto;
import org.WaialuaRobotics359.robot.commands.SetPoints.Scoring.*;
import org.WaialuaRobotics359.robot.commands.autonomous.*;
import org.WaialuaRobotics359.robot.subsystems.*;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class AutoMidPosition extends SequentialCommandGroup {

    public AutoMidPosition (Intake s_Intake, Arm s_Arm, Wrist s_Wrist, Pivot s_Pivot, Leds s_Leds) {


        addCommands(new SequentialCommandGroup(
                new MidPositionAuto(s_Intake, s_Arm, s_Wrist, s_Pivot, s_Leds), // HalfUpHighStart
                new AutoWait(0.1),
                new Score(s_Arm, s_Intake, s_Pivot, s_Wrist, s_Leds)
        // new AutoWait(.1)
        ));
    }
}