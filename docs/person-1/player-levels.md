# Person 1 - Player Logic And Levels

Person 1 works with player movement, level flow, and regular gameplay.

Files to check:
- `src/main/java/game/TrafficGame2D.java`
- `src/main/java/game/Factory.java`
- `src/main/java/game/Location.java`
- `src/main/java/game/CarSkin.java`

Important methods:
- `updateGame` - speed, movement, score, world update
- `spawnRow` - road objects and traffic rows
- `collide` - player hitbox checks
- `startRun` - level start setup
- `checkLevelGoal` - level completion

Testing:
- start levels 1-9
- check smooth player movement
- check that crashes happen only on visible car hitboxes
