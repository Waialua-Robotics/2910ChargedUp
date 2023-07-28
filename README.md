# 2910ChargedUp
<h2> Controller Binding </h2>

 **Controller** | **Operation** | **Control** |
----------|:----------:|---------|
Driver | YTranslation | LStickY |
Driver | XTranslation | LStickX |
Driver | Rotation | RStickX |
Driver | Zero Gyro | Start |
Driver | Rest Mods | Back |
Driver | Robot Centric | LB |
Driver | Snap 0 | Y |
Driver | Snap 180 | A|
Operator | 10* | NA |
Operator | Low | A |
Operator | Mid | B |
Operator | High | Y |
Operator | Feeder | X |
Operator | StandingPickup | rBumper |
Operator | LowPickup | lBumper |
Operator | ManualIntake | rTrigger |
Operator | ManualOutake | lTrigger |
Operator | 0* |
Operator | 90* |
Oporator | 180* |
Operator | AbortScore | 270* |
Operator | Stow | Start |
Operator | CubeMode | lJoystick |
Operator | ConeMode | rJoystick |
Overide | SequenceMode | Left |
Overide | FullManual | Right |
Overide |  | Middle |
Robot | BrakeMode | Button |
Robot | Zero | Button |
Robot | Coast | Button |

<h2> Id's/Location </h2>

**Name** | **Number** | **PDH** |
----------|:----------:|---------|
Front Left Drive | 0* |  |
Front Left Pivot | 10* | NA |
Front Left CanCoder | 20* | NA |
Front Left Drive 2 | 30* |  |
Front Right Drive | 1* | NA |
Front Right Pivot | 11* |NA |
Front Right CanCoder | 21* |NA |
Front Right Drive 2 | 31* |  |
Back Left Drive |2*| NA |
Back Left Pivot |12*| NA |
Back Left CanCoder |22*| NA |
Back Left Drive 2 | 32* |  |
Back Right Drive |3*| NA |
Back Right Pivot |13*| NA |
Back Right CanCoder |23*| NA |
Back Right Drive 2 | 33* |  |
Wrist | 1 |  |
Intake | 2 |  |
Front Left Pivot | 3 |  |
Back Left Pivot | 4 |  |
Front Arm | 5 |  |
Back Arm | 6 |  |
Front Right Pivot | 7 |  |
Back Right Pivot | 8 |  |

Scoring sequence (In sequence mode) :
while scoring button pressed 
  the rebot goes to a position
    when the button in released and the left d-pad isnt held, the robot scores & stows
    if it is held, skip over the outake command and continue to stow

Scoring sequence (Sequence False mode) :
when scoring button pressed 
  the rebot goes to a position
    then manually outake and stow

Scoring sequence (manual mode) :
disable the soft limits
allow the joysticks and triggerst t control the different axis in percent mode
