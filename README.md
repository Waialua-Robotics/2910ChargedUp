# 2910ChargedUp
- Updated to 2023
- Hold button to extend arm
- When you release the buttton it score

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


<h2> Id's/Location </h2>

**Name** | **Number** | **PDH** |
----------|:----------:|---------|
