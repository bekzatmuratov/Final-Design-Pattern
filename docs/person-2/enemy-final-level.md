# Person 2 - Enemy Logic And Final Level

Person 2 is responsible for Erasyl, enemy movement, collision fixes, and level 10.

Files to check:
- `src/main/java/game/TrafficGame2D.java`

Important methods:
- `updateRival`
- `updateIntroRival`
- `updateBossRival`
- `rivalBlocked`
- `rivalLaneFree`
- `bestRivalLane`
- `updateBullets`
- `startBossExplosion`

Checklist:
- Erasyl does not appear on non-story chase levels
- Erasyl avoids traffic cars
- Erasyl does not pass through cars
- level 10 has no fuel cans
- level 10 has fewer traffic cars
- boss health bar is visible
- victory explosion starts after boss HP reaches zero
