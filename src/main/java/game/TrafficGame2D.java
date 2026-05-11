package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class TrafficGame2D extends ApplicationAdapter implements InputProcessor, Command.Keys, Observer.Listener {
    static final int W = 960;
    static final int H = 640;
    static final int ROAD_W = 430;
    static final int ROAD_X = (W - ROAD_W) / 2;
    static final int LANES = 4;
    static final float CAR_W = 58;
    static final float CAR_H = 104;
    static final float PLAYER_Y = 72;

    OrthographicCamera camera;
    FitViewport viewport;
    SpriteBatch batch;
    ShapeRenderer shapes;
    BitmapFont font;
    BitmapFont bigFont;
    GlyphLayout layout = new GlyphLayout();

    SaveData save;
    CarSkin car;
    Location location;
    Decorator.Ride ride;
    Strategy.ScoreRule scoreRule = new Strategy.RunnerScore();
    Observer.Bus bus = new Observer.Bus();
    State.RunState state = new State.Ready();
    Map<Integer, Command.KeyCommand> keys = new HashMap<Integer, Command.KeyCommand>();

    Map<String, Texture> skinTextures = new HashMap<String, Texture>();
    ArrayList<Texture> trafficTextures = new ArrayList<Texture>();
    Texture coinTexture;
    Texture playerTexture;
    Texture rivalTexture;
    Texture bulletTexture;
    Texture enemyBulletTexture;
    Texture forestBgTexture;

    ArrayList<RoadThing> things = new ArrayList<RoadThing>();
    ArrayList<Bullet> bullets = new ArrayList<Bullet>();
    ArrayList<Bullet> enemyBullets = new ArrayList<Bullet>();
    Random random = new Random();
    String mode = "menu";
    String message = "";
    String dialogSpeaker = "";
    String dialogLine = "";
    boolean levelFinished = false;
    boolean rivalVisible = false;
    boolean rivalGone = false;
    boolean fullScreen = false;
    int lane = 1;
    int playLevel = 1;
    int dialogIndex = 0;
    int menuChoice = 0;
    int levelChoice = 0;
    int shopType = 0;
    int shopIndex = 0;
    int rivalLane = 2;
    int rivalTargetLane = 2;
    int score = 0;
    int coinScore = 0;
    int shield = 0;
    int nearMiss = 0;
    int lastScoreEvent = 0;
    int collectedThisRun = 0;
    int rivalHp = 100;
    int playerHp = 100;
    float currentSpeedKmh = 0;
    float highSpeedTime = 0;
    float playerX = laneX(1);
    float targetX = laneX(1);
    float rivalX = laneX(2);
    float rivalY = H + 120;
    float rivalMoveTimer = 0;
    float rivalFlashTimer = 0;
    float rivalIntroTimer = 0;
    float rivalSpeedY = 0;
    float explosionX = W / 2f;
    float explosionY = H - 190f;
    float fireCooldown = 0;
    float enemyFireCooldown = 1.5f;
    float sceneTimer = 0;
    float dialogAlpha = 0;
    float roadScroll = 0;
    float meters = 0;
    float spawnTimer = 0;
    float nitroTimer = 0;
    boolean accelerating = false;
    boolean braking = false;

    public void create() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(W, H, camera);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        batch = new SpriteBatch();
        shapes = new ShapeRenderer();
        makeFonts();
        save = Singleton.get().load();
        car = Factory.car(save.car);
        location = Factory.location(save.location);
        ride = Decorator.make(car);
        bindKeys();
        makeTextures();
        bus.add(this);
        Gdx.input.setInputProcessor(this);
    }

    void bindKeys() {
        keys.put(Input.Keys.LEFT, new Command.Left());
        keys.put(Input.Keys.A, new Command.Left());
        keys.put(Input.Keys.RIGHT, new Command.Right());
        keys.put(Input.Keys.D, new Command.Right());
        keys.put(Input.Keys.UP, new Command.Jump());
        keys.put(Input.Keys.W, new Command.Jump());
        keys.put(Input.Keys.DOWN, new Command.Slide());
        keys.put(Input.Keys.S, new Command.Slide());
        keys.put(Input.Keys.SPACE, new Command.Action());
        keys.put(Input.Keys.ENTER, new Command.Action());
        keys.put(Input.Keys.G, new Command.Garage());
        keys.put(Input.Keys.TAB, new Command.Tab());
        keys.put(Input.Keys.ESCAPE, new Command.Menu());
    }

    void makeFonts() {
        String[] paths = {
                "/System/Library/Fonts/Supplemental/Arial.ttf",
                "/System/Library/Fonts/Supplemental/Arial Unicode.ttf",
                "/System/Library/Fonts/Supplemental/Verdana.ttf",
                "/Library/Fonts/Arial.ttf",
                "C:/Windows/Fonts/arial.ttf",
                "/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf"
        };
        for (int i = 0; i < paths.length; i++) {
            if (Gdx.files.absolute(paths[i]).exists()) {
                FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.absolute(paths[i]));
                FreeTypeFontGenerator.FreeTypeFontParameter small = new FreeTypeFontGenerator.FreeTypeFontParameter();
                small.size = 22;
                small.characters = russianChars();
                small.borderWidth = 1;
                small.borderColor = new Color(0, 0, 0, 0.35f);
                font = generator.generateFont(small);
                FreeTypeFontGenerator.FreeTypeFontParameter big = new FreeTypeFontGenerator.FreeTypeFontParameter();
                big.size = 42;
                big.characters = russianChars();
                big.borderWidth = 2;
                big.borderColor = new Color(0, 0, 0, 0.45f);
                bigFont = generator.generateFont(big);
                generator.dispose();
                return;
            }
        }
        font = new BitmapFont();
        bigFont = new BitmapFont();
        font.getData().setScale(1.22f);
        bigFont.getData().setScale(2.15f);
    }

    String russianChars() {
        return FreeTypeFontGenerator.DEFAULT_CHARS
                + "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ"
                + "абвгдеёжзийклмнопрстуфхцчшщъыьэюя"
                + "ӘәІіҢңҒғҮүҰұҚқӨөҺһ"
                + "₸-«»№";
    }

    void makeTextures() {
        disposeTextures();
        skinTextures.clear();
        trafficTextures.clear();
        List<CarSkin> cars = Factory.cars();
        for (int i = 0; i < cars.size(); i++) {
            CarSkin skin = cars.get(i);
            skinTextures.put(skin.id, loadTexture("cars/" + skin.id + ".png"));
        }
        playerTexture = skinTextures.get(car.id);
        trafficTextures.add(loadTexture("cars/traffic_black.png"));
        trafficTextures.add(loadTexture("cars/traffic_blue.png"));
        trafficTextures.add(loadTexture("cars/traffic_green.png"));
        trafficTextures.add(loadTexture("cars/traffic_red.png"));
        trafficTextures.add(loadTexture("cars/traffic_yellow.png"));
        rivalTexture = loadTexture("cars/rival.png");
        forestBgTexture = loadTexture("forest_bg.jpg");
        coinTexture = coinTexture(location.coin);
        bulletTexture = bulletTexture(Color.valueOf("FACC15"));
        enemyBulletTexture = bulletTexture(Color.valueOf("EF4444"));
    }

    Texture loadTexture(String path) {
        Texture texture = new Texture(Gdx.files.internal(path));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        return texture;
    }

    Texture coinTexture(Color color) {
        Pixmap p = new Pixmap(48, 48, Pixmap.Format.RGBA8888);
        p.setColor(0, 0, 0, 0);
        p.fill();
        p.setColor(0, 0, 0, 0.18f);
        round(p, 10, 9, 30, 34, 5);
        p.setColor(Color.valueOf("EF4444"));
        round(p, 9, 7, 29, 35, 5);
        p.setColor(Color.valueOf("7F1D1D"));
        p.fillRectangle(29, 11, 11, 6);
        p.fillRectangle(34, 15, 5, 11);
        p.setColor(color);
        p.fillRectangle(15, 15, 12, 14);
        p.setColor(Color.WHITE);
        p.drawRectangle(14, 14, 14, 16);
        p.drawLine(18, 20, 24, 26);
        p.drawLine(24, 20, 18, 26);
        p.setColor(Color.valueOf("FDE68A"));
        p.fillTriangle(21, 33, 15, 41, 28, 41);
        Texture texture = new Texture(p);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        p.dispose();
        return texture;
    }

    Texture bulletTexture(Color color) {
        Pixmap p = new Pixmap(18, 34, Pixmap.Format.RGBA8888);
        p.setColor(0, 0, 0, 0);
        p.fill();
        p.setColor(color);
        p.fillCircle(9, 8, 7);
        p.fillRectangle(5, 8, 8, 18);
        p.setColor(Color.WHITE);
        p.fillRectangle(8, 4, 3, 21);
        Texture texture = new Texture(p);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        p.dispose();
        return texture;
    }

    void round(Pixmap p, int x, int y, int w, int h, int r) {
        p.fillRectangle(x + r, y, w - r * 2, h);
        p.fillRectangle(x, y + r, w, h - r * 2);
        p.fillCircle(x + r, y + r, r);
        p.fillCircle(x + w - r, y + r, r);
        p.fillCircle(x + r, y + h - r, r);
        p.fillCircle(x + w - r, y + h - r, r);
    }

    public void render() {
        float dt = Math.min(Gdx.graphics.getDeltaTime(), 0.05f);
        if (mode.equals("game") && state.updateWorld()) {
            updateGame(dt);
        } else if (mode.equals("crashScene")) {
            updateCrashScene(dt);
        } else if (mode.equals("bossExplosion")) {
            updateBossExplosion(dt);
        } else {
            roadScroll += dt * 180;
        }
        if (mode.equals("dialog")) {
            dialogAlpha = Math.min(1f, dialogAlpha + dt * 3.2f);
        }
        if (mode.equals("win")) {
            sceneTimer += dt;
        }
        draw();
    }

    void updateGame(float dt) {
        float difficulty = 1f + Math.min(0.70f, (playLevel - 1) * 0.055f);
        float baseSpeed = playLevel == 10 ? 255f + meters * 0.10f : 278f + meters * 0.18f;
        float speedLimit = playLevel == 10 ? 690f : 820f;
        if (playLevel == 10) {
            difficulty = 1.08f;
        }
        float speed = Math.min(speedLimit, baseSpeed * location.speed * difficulty);
        if (accelerating) {
            speed *= 1.15f;
        }
        if (braking) {
            speed *= 0.72f;
        }
        if (nitroTimer > 0) {
            speed *= 1.42f;
        }

        meters += speed * dt / 9.5f;
        roadScroll += speed * dt;
        currentSpeedKmh = MathUtils.lerp(currentSpeedKmh, speed * 0.31f, Math.min(1f, dt * 3.5f));
        if (currentSpeedKmh > 125f) {
            highSpeedTime += dt;
        }
        float turnSmooth = playLevel == 10 ? 5.8f + car.handling * 0.85f : 8f + car.handling;
        playerX = MathUtils.lerp(playerX, targetX, Math.min(1, dt * turnSmooth));
        spawnTimer += dt;
        nitroTimer = Math.max(0, nitroTimer - dt);
        fireCooldown = Math.max(0, fireCooldown - dt);
        enemyFireCooldown = Math.max(0, enemyFireCooldown - dt);

        float gap = Math.max(0.72f, 1.20f / (location.traffic + meters / 1800f));
        if (playLevel == 10) {
            gap = Math.max(1.05f, gap * 1.45f);
        }
        while (spawnTimer > gap) {
            spawnTimer -= gap;
            spawnRow();
        }

        for (int i = things.size() - 1; i >= 0; i--) {
            RoadThing t = things.get(i);
            t.y -= (speed * t.speed) * dt;
            if (t.y < -140) {
                things.remove(i);
                continue;
            }
            if (collide(t)) {
                if (t.good()) {
                    collect(t);
                    things.remove(i);
                } else if (nitroTimer > 0) {
                    coinScore += 25;
                    things.remove(i);
                } else if (shield > 0) {
                    shield--;
                    things.remove(i);
                    message = "Щит спас машину";
                } else {
                    crash();
                    return;
                }
            } else if (t.type == RoadThing.TRAFFIC && !t.near && t.y < PLAYER_Y + 90 && t.y > PLAYER_Y - 70) {
                if (Math.abs(t.x - playerX) < 92 && Math.abs(t.x - playerX) > 45) {
                    t.near = true;
                    nearMiss++;
                    coinScore += 18 + nearMiss * 2;
                    message = "Опасный обгон x" + nearMiss;
                }
            }
        }

        updateRival(dt);
        updateBullets(dt);

        score = coinScore + scoreRule.distance(meters, car, location, ride, nitroTimer > 0);
        checkLevelGoal();
        if (score - lastScoreEvent > 90) {
            lastScoreEvent = score;
            bus.send(new Observer.Event("score", "score", score));
        }
    }

    void spawnRow() {
        int safeLane = chooseSafeLane();
        boolean[] busy = new boolean[LANES];
        int chance = playLevel == 10 ? 38 : Math.min(86, 55 + playLevel * 3);
        int cars = playLevel == 10 ? 1 : random.nextInt(100) < chance ? 2 : 1;
        for (int i = 0; i < cars; i++) {
            int carLane = freeTrafficLane(safeLane, busy);
            if (carLane < 0) {
                break;
            }
            busy[carLane] = true;
            RoadThing traffic = new RoadThing(RoadThing.TRAFFIC, carLane, laneX(carLane), H + 135 + i * 96, CAR_W, CAR_H);
            traffic.texture = trafficTextures.get(random.nextInt(trafficTextures.size()));
            traffic.speed = 0.88f;
            things.add(traffic);
        }

        if (playLevel != 10 && random.nextInt(100) < 38) {
            int coinLane = safeLane;
            int fuelCount = playLevel <= 4 ? 3 : 2;
            for (int i = 0; i < fuelCount; i++) {
                RoadThing coin = new RoadThing(RoadThing.COIN, coinLane, laneX(coinLane), H + 125 + i * 58, 34, 34);
                coin.texture = coinTexture;
                things.add(coin);
            }
        }

    }

    int freeTrafficLane(int safeLane, boolean[] busy) {
        for (int tries = 0; tries < 10; tries++) {
            int lane = random.nextInt(LANES);
            if (lane != safeLane && !busy[lane] && !recentTrafficInLane(lane)) {
                return lane;
            }
        }
        for (int lane = 0; lane < LANES; lane++) {
            if (lane != safeLane && !busy[lane] && !recentTrafficInLane(lane)) {
                return lane;
            }
        }
        return -1;
    }

    int chooseSafeLane() {
        for (int tries = 0; tries < 12; tries++) {
            int lane = random.nextInt(LANES);
            if (!recentTrafficInLane(lane)) {
                return lane;
            }
        }
        return random.nextInt(LANES);
    }

    boolean recentTrafficInLane(int lane) {
        for (int i = 0; i < things.size(); i++) {
            RoadThing t = things.get(i);
            if (t.type == RoadThing.TRAFFIC && t.lane == lane && t.y > H - 210) {
                return true;
            }
        }
        return false;
    }

    boolean collide(RoadThing t) {
        float px = playerX - CAR_W / 2 + 13;
        float py = PLAYER_Y + 15;
        float pw = CAR_W - 26;
        float ph = CAR_H - 32;
        float tw = t.type == RoadThing.TRAFFIC ? t.w - 24 : t.w - 14;
        float th = t.type == RoadThing.TRAFFIC ? t.h - 30 : t.h - 14;
        float tx = t.x - tw / 2;
        float ty = t.y + (t.type == RoadThing.TRAFFIC ? 15 : 7);
        return px < tx + tw && px + pw > tx && py < ty + th && py + ph > ty;
    }

    void collect(RoadThing t) {
        if (t.type == RoadThing.COIN) {
            coinScore += scoreRule.coin(car, location, ride, nitroTimer > 0);
            collectedThisRun++;
        }
    }

    void crash() {
        mode = "over";
        state = new State.Dead();
        save.coins += score;
        int nextBest = Math.max(save.best, score);
        save.runs++;
        save.best = nextBest;
        Singleton.get().save(save);
        bus.send(new Observer.Event("over", "finished", score));
    }

    void startRun() {
        levelFinished = false;
        playLevel = MathUtils.clamp(save.level, 1, 10);
        car = Factory.car(save.car);
        location = storyLocation(playLevel);
        ride = Decorator.make(car);
        playerTexture = skinTextures.get(car.id);
        things.clear();
        bullets.clear();
        enemyBullets.clear();
        lane = 1;
        playerX = laneX(lane);
        targetX = playerX;
        rivalLane = playLevel == 1 ? 0 : 2;
        rivalTargetLane = rivalLane;
        rivalX = laneX(rivalLane);
        rivalY = H + 150;
        rivalVisible = false;
        rivalGone = false;
        rivalMoveTimer = 0;
        rivalFlashTimer = 0;
        rivalIntroTimer = 0;
        rivalSpeedY = 0;
        fireCooldown = 0;
        enemyFireCooldown = 1.2f;
        rivalHp = playLevel == 10 ? 300 : 100 + playLevel * 12;
        playerHp = 100;
        sceneTimer = 0;
        meters = 0;
        spawnTimer = 0.55f;
        score = 0;
        coinScore = 0;
        nearMiss = 0;
        collectedThisRun = 0;
        lastScoreEvent = 0;
        currentSpeedKmh = 0;
        highSpeedTime = 0;
        shield = ride.shield();
        nitroTimer = 0;
        accelerating = false;
        braking = false;
        message = "";
        prepareRivalForLevel();
        state = new State.Playing();
        mode = "game";
    }

    void prepareRivalForLevel() {
        if (introRivalLevel(playLevel)) {
            rivalVisible = true;
            rivalLane = playLevel == 1 ? 1 : 2;
            rivalTargetLane = rivalLane;
            rivalX = laneX(rivalLane);
            rivalY = H - 300;
            rivalSpeedY = 95f;
            seedIntroTraffic();
            return;
        }
        if (playLevel == 9) {
            rivalVisible = true;
            rivalLane = 2;
            rivalTargetLane = 2;
            rivalX = laneX(rivalLane);
            rivalY = H - 235;
            return;
        }
        if (playLevel == 10) {
            rivalVisible = true;
            rivalLane = 2;
            rivalTargetLane = 2;
            rivalX = laneX(rivalLane);
            rivalY = H - 185;
        }
    }

    void seedIntroTraffic() {
        int[] lanes = playLevel == 1 ? new int[] {2, 1, 3} : new int[] {1, 2, 3};
        for (int i = 0; i < lanes.length; i++) {
            RoadThing traffic = new RoadThing(RoadThing.TRAFFIC, lanes[i], laneX(lanes[i]), H - 230 + i * 126, CAR_W, CAR_H);
            traffic.texture = trafficTextures.get((i + playLevel) % trafficTextures.size());
            traffic.speed = 0.86f;
            things.add(traffic);
        }
    }

    void startStory() {
        if (save.storyWon) {
            sceneTimer = 2f;
            mode = "win";
            return;
        }
        save.level = MathUtils.clamp(save.level, 1, 10);
        playLevel = save.level;
        if (hasDialog(playLevel)) {
            startDialog();
        } else {
            startRun();
        }
    }

    boolean hasDialog(int level) {
        return level == 1 || level == 5 || level == 9 || level == 10;
    }

    boolean introRivalLevel(int level) {
        return level == 1 || level == 5;
    }

    void startDialog() {
        mode = "dialog";
        dialogIndex = 0;
        dialogAlpha = 0;
        setDialogLine();
    }

    void nextDialog() {
        dialogIndex++;
        if (dialogIndex >= dialogCount(playLevel)) {
            startRun();
        } else {
            dialogAlpha = 0;
            setDialogLine();
        }
    }

    void setDialogLine() {
        String[][] data = dialogData(playLevel);
        int index = Math.min(dialogIndex, data.length - 1);
        dialogSpeaker = data[index][0];
        dialogLine = data[index][1];
    }

    int dialogCount(int level) {
        return dialogData(level).length;
    }

    String[][] dialogData(int level) {
        if (level == 1) {
            return new String[][] {
                    {"Ерасыл", "Эй, Бекзат, ты вообще умеешь ездить? Такая скорость только для парковки подходит."},
                    {"Бекзат", "Я просто ехал по делам. Не мешай дороге."},
                    {"Ерасыл", "Алматы Движ не любит медленных. Смотри, как надо."},
                    {"Система", "Красная машина резко выходит из соседней полосы. Фары режут ночь, трафик сжимается."},
                    {"Бекзат", "Что ты сказал? Я тебя догоню."},
                    {"Ерасыл", "Ну попробуй. Только не потеряйся в зеркалах."},
                    {"Бекзат", "Хорошо. Тогда начинается настоящая погоня."},
                    {"Система", "Ерасыл стартует первым. Твоя задача - не потерять темп и выжить в потоке."}
            };
        }
        if (level == 5) {
            return new String[][] {
                    {"Ерасыл", "Ого, Бекзат, ты всё-таки не отстаёшь. Признаю, ты быстрый."},
                    {"Бекзат", "Я же сказал, что догоню тебя."},
                    {"Ерасыл", "Быстрый - да. Но не быстрее меня."},
                    {"Бекзат", "Ты слишком много говоришь. На дороге решает не голос, а реакция."},
                    {"Система", "Машины вокруг сигналят, дорога бликует, а дистанция между вами почти исчезла."},
                    {"Ерасыл", "Тогда попробуй удержаться на следующей трассе."},
                    {"Бекзат", "Я не удержусь. Я тебя обгоню."},
                    {"Ерасыл", "Запомни этот звук мотора. Следующий раз ты услышишь его только впереди."}
            };
        }
        if (level == 9) {
            return new String[][] {
                    {"Ерасыл", "Ты мне уже надоел. Я здесь самый быстрый гонщик Алматы Движ, и тебе меня не догнать."},
                    {"Бекзат", "Ты уже проиграл, когда связался со мной."},
                    {"Ерасыл", "Последний шанс свернуть. Дальше будет удар."},
                    {"Бекзат", "Я не сворачиваю. Я заканчиваю эту гонку."},
                    {"Система", "Напряжение растёт. Держи скорость и не врезайся в трафик."},
                    {"Ерасыл", "Ты думаешь, один смелый рывок сделает тебя легендой?"},
                    {"Бекзат", "Нет. Легендой делает момент, когда ты не сдаёшься."},
                    {"Система", "Обе машины идут на опасную дистанцию. Столкновение становится неизбежным."}
            };
        }
        if (level == 10) {
            return new String[][] {
                    {"Ерасыл", "Последняя гонка, Бекзат. Покажи, что умеешь."},
                    {"Бекзат", "Сегодня Алматы узнает, кто действительно быстрее."},
                    {"Ерасыл", "У меня есть оружие, скорость и вся дорога впереди."},
                    {"Бекзат", "А у меня есть цель. Я остановлю тебя."},
                    {"Система", "После аварии обе машины повреждены, но моторы всё ещё ревут."},
                    {"Ерасыл", "Если хочешь победить, стреляй точно. Второго шанса не будет."},
                    {"Бекзат", "Я не ищу второй шанс. Мне хватит одного попадания за другим."},
                    {"Система", "Финал: SPACE стрелять, A/D уворачиваться, W держать скорость."},
                    {"Система", "Попадай по Ерасылу, избегай атак и не теряй контроль."}
            };
        }
        return new String[][] {{"Система", "Готовься к заезду."}};
    }

    Location storyLocation(int level) {
        List<Location> locations = Factory.locations();
        int index = MathUtils.clamp(level - 1, 0, locations.size() - 1);
        return locations.get(index);
    }

    void updateRival(float dt) {
        if (!rivalVisible || playLevel < 1 || playLevel > 10) {
            return;
        }
        if (introRivalLevel(playLevel)) {
            updateIntroRival(dt);
        } else if (playLevel == 9) {
            updateLevelNineRival(dt);
        } else {
            updateBossRival(dt);
        }
    }

    void updateIntroRival(float dt) {
        rivalIntroTimer += dt;
        if (rivalBlocked(rivalTargetLane, rivalY, 250f)) {
            rivalTargetLane = bestRivalLane(rivalTargetLane, rivalY, 300f);
        }
        rivalX = MathUtils.lerp(rivalX, laneX(rivalTargetLane), Math.min(1f, dt * 3.2f));
        if (Math.abs(rivalX - laneX(rivalTargetLane)) < 4f) {
            rivalLane = rivalTargetLane;
        }
        if (rivalLaneFree(rivalTargetLane, rivalY, 165f)) {
            rivalSpeedY = Math.min(455f, rivalSpeedY + (95f + rivalIntroTimer * 70f) * dt);
        } else {
            rivalSpeedY = Math.max(80f, rivalSpeedY - 260f * dt);
        }
        rivalY += rivalSpeedY * dt;
        if (rivalY > H + 130) {
            rivalVisible = false;
            rivalGone = true;
            rivalY = H + 180;
        }
    }

    void updateLevelNineRival(float dt) {
        if (rivalBlocked(rivalTargetLane, rivalY, 230f)) {
            rivalTargetLane = bestRivalLane(rivalTargetLane, rivalY, 280f);
        }
        rivalX = MathUtils.lerp(rivalX, laneX(rivalTargetLane), Math.min(1f, dt * 2.5f));
        if (Math.abs(rivalX - laneX(rivalTargetLane)) < 4f) {
            rivalLane = rivalTargetLane;
        }
        rivalY = MathUtils.lerp(rivalY, H - 215f, Math.min(1f, dt * 2f));
    }

    void updateBossRival(float dt) {
        rivalMoveTimer -= dt;
        if (rivalMoveTimer <= 0 || rivalBlocked(rivalTargetLane, rivalY, 260f)) {
            rivalMoveTimer = 0.75f + random.nextFloat() * 0.75f;
            rivalTargetLane = bestRivalLane(rivalTargetLane, rivalY, 305f);
            if (random.nextInt(100) < 36 && rivalLaneFree(rivalTargetLane, rivalY, 305f)) {
                rivalTargetLane = MathUtils.clamp(rivalTargetLane + (random.nextBoolean() ? 1 : -1), 0, LANES - 1);
            }
        }
        if (!rivalLaneFree(rivalTargetLane, rivalY, 210f)) {
            rivalTargetLane = bestRivalLane(rivalTargetLane, rivalY, 320f);
        }
        rivalX = MathUtils.lerp(rivalX, laneX(rivalTargetLane), Math.min(1f, dt * 2.7f));
        if (Math.abs(rivalX - laneX(rivalTargetLane)) < 4f) {
            rivalLane = rivalTargetLane;
        }
        rivalY = H - 185 + MathUtils.sin(meters * 0.04f) * 16f;
        if (enemyFireCooldown <= 0) {
            enemyFireCooldown = 0.92f + random.nextFloat() * 0.48f;
            enemyBullets.add(new Bullet(rivalX, rivalY - 12, -340f));
        }
    }

    boolean rivalBlocked(int lane, float y, float lookAhead) {
        for (int i = 0; i < things.size(); i++) {
            RoadThing t = things.get(i);
            if (t.type == RoadThing.TRAFFIC && t.lane == lane && t.y > y - 28f && t.y < y + lookAhead) {
                return true;
            }
        }
        return false;
    }

    boolean rivalLaneFree(int lane, float y, float lookAhead) {
        for (int i = 0; i < things.size(); i++) {
            RoadThing t = things.get(i);
            if (t.type == RoadThing.TRAFFIC && t.lane == lane && t.y > y - 100f && t.y < y + lookAhead) {
                return false;
            }
        }
        return true;
    }

    int bestRivalLane(int current, float y, float lookAhead) {
        int best = -1;
        float bestScore = -9999f;
        for (int lane = 0; lane < LANES; lane++) {
            float score = -Math.abs(lane - current) * 1.2f;
            boolean blocked = false;
            for (int i = 0; i < things.size(); i++) {
                RoadThing t = things.get(i);
                if (t.type == RoadThing.TRAFFIC && t.lane == lane && t.y > y - 105f && t.y < y + lookAhead) {
                    score -= 1000f;
                    blocked = true;
                } else if (t.type == RoadThing.TRAFFIC && t.lane == lane && Math.abs(t.y - y) < lookAhead + 120f) {
                    score -= 3f;
                }
            }
            if (!blocked && score > bestScore) {
                bestScore = score;
                best = lane;
            }
        }
        if (best >= 0) {
            return best;
        }
        return current;
    }

    void updateBullets(float dt) {
        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);
            bullet.y += bullet.speed * dt;
            if (bullet.y > H + 40) {
                bullets.remove(i);
            } else if (playLevel == 10 && Math.abs(bullet.x - rivalX) < 42 && Math.abs(bullet.y - rivalY) < 62) {
                rivalHp -= 10;
                bullets.remove(i);
                message = "Попадание!";
                if (rivalHp <= 0) {
                    startBossExplosion();
                    return;
                }
            }
        }
        for (int i = enemyBullets.size() - 1; i >= 0; i--) {
            Bullet bullet = enemyBullets.get(i);
            bullet.y += bullet.speed * dt;
            if (bullet.y < -40) {
                enemyBullets.remove(i);
            } else if (Math.abs(bullet.x - playerX) < 29 && Math.abs(bullet.y - (PLAYER_Y + 52)) < 44) {
                playerHp -= 18;
                enemyBullets.remove(i);
                message = "Попадание по тебе!";
                if (playerHp <= 0) {
                    loseFinal();
                    return;
                }
            }
        }
    }

    void checkLevelGoal() {
        if (playLevel == 10 || levelFinished) {
            return;
        }
        float goal = levelGoal();
        if (meters < goal) {
            return;
        }
        if (playLevel == 9) {
            startCrashScene();
        } else {
            completeLevel();
        }
    }

    float levelGoal() {
        if (playLevel <= 4) {
            return 980 + playLevel * 240;
        }
        if (playLevel <= 8) {
            return 1550 + playLevel * 285;
        }
        return 2600;
    }

    void completeLevel() {
        levelFinished = true;
        mode = "levelComplete";
        state = new State.Ready();
        save.coins += score + 150 + playLevel * 45;
        save.runs++;
        save.best = Math.max(save.best, score);
        save.level = Math.min(10, playLevel + 1);
        Singleton.get().save(save);
    }

    void startCrashScene() {
        levelFinished = true;
        mode = "crashScene";
        sceneTimer = 0;
        state = new State.Ready();
        rivalVisible = true;
        rivalX = playerX;
        rivalY = PLAYER_Y + 135;
    }

    void updateCrashScene(float dt) {
        roadScroll += dt * 90;
        sceneTimer += dt;
        if (sceneTimer > 2.35f) {
            save.coins += score + 500;
            save.runs++;
            save.best = Math.max(save.best, score);
            save.level = 10;
            playLevel = 10;
            Singleton.get().save(save);
            startDialog();
        }
    }

    void startBossExplosion() {
        levelFinished = true;
        mode = "bossExplosion";
        state = new State.Ready();
        sceneTimer = 0;
        explosionX = rivalX;
        explosionY = rivalY + CAR_H / 2f;
        rivalVisible = false;
        bullets.clear();
        enemyBullets.clear();
        message = "Ерасыл потерял управление!";
    }

    void updateBossExplosion(float dt) {
        roadScroll += dt * 240;
        sceneTimer += dt;
        currentSpeedKmh = MathUtils.lerp(currentSpeedKmh, 80f, Math.min(1f, dt * 1.6f));
        if (sceneTimer > 3.8f) {
            winStory();
        }
    }

    void winStory() {
        mode = "win";
        state = new State.Ready();
        rivalVisible = false;
        sceneTimer = 0;
        save.coins += score + 2500;
        save.best = Math.max(save.best, score);
        save.storyWon = true;
        save.level = 10;
        Singleton.get().save(save);
    }

    void loseFinal() {
        mode = "over";
        state = new State.Dead();
        save.runs++;
        Singleton.get().save(save);
        message = "Ерасыл: Я же говорил. Я лучший гонщик Алматы Движ.";
    }

    void draw() {
        Gdx.gl.glClearColor(0.02f, 0.04f, 0.035f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        shapes.setProjectionMatrix(camera.combined);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        drawPhotoBackground();
        batch.end();
        drawRoad();
        drawUiBackground();
        batch.begin();
        drawCarsAndItems();
        drawUi();
        batch.end();
    }

    void drawPhotoBackground() {
        if (forestBgTexture == null) {
            return;
        }
        float offset = -(roadScroll * 0.12f % H);
        batch.setColor(Color.WHITE);
        batch.draw(forestBgTexture, 0, offset, W, H);
        batch.draw(forestBgTexture, 0, offset + H, W, H);
        if (offset > 0) {
            batch.draw(forestBgTexture, 0, offset - H, W, H);
        }
    }

    void drawUiBackground() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        if (mode.equals("dialog")) {
            shapes.setColor(0, 0, 0, 0.62f * dialogAlpha);
            shapes.rect(0, 0, W, H);
            panel(178, 92, 604, 240, Color.valueOf("0B1220"), 0.90f * dialogAlpha);
            panel(206, 206, 96, 96, dialogSpeaker.equals("Ерасыл") ? Color.valueOf("7F1D1D") : Color.valueOf("1E3A8A"), 0.95f * dialogAlpha);
            shapes.setColor(location.coin.r, location.coin.g, location.coin.b, 0.18f * dialogAlpha);
            shapes.rect(178, 329, 604, 3);
        } else if (mode.equals("menu")) {
            shapes.setColor(0, 0, 0, 0.46f);
            shapes.rect(0, 0, W, H);
            panel(86, 382, 788, 172, Color.valueOf("0B1220"), 0.84f);
            panel(300, 126, 360, 242, Color.valueOf("101827"), 0.86f);
            for (int i = 0; i < 4; i++) {
                Color color = i == menuChoice ? Color.valueOf("FACC15") : Color.valueOf("243047");
                panel(328, 284 - i * 48, 304, 38, color, i == menuChoice ? 0.92f : 0.70f);
            }
        } else if (mode.equals("levels")) {
            shapes.setColor(0, 0, 0, 0.50f);
            shapes.rect(0, 0, W, H);
            panel(84, 58, 792, 520, Color.valueOf("0B1220"), 0.92f);
            for (int i = 0; i < 10; i++) {
                float x = 142 + (i % 5) * 148;
                float y = i < 5 ? 350 : 230;
                Color color = i == levelChoice ? Color.valueOf("FACC15") : Color.valueOf("243047");
                panel(x, y, 112, 70, color, i == levelChoice ? 0.94f : 0.72f);
            }
        } else if (mode.equals("shop")) {
            shapes.setColor(0, 0, 0, 0.42f);
            shapes.rect(0, 0, W, H);
            panel(96, 54, 768, 532, Color.valueOf("0B1220"), 0.90f);
            panel(136, 302, 688, 210, Color.valueOf("172033"), 0.78f);
            if (shopType == 1) {
                Location item = Factory.locations().get(Math.min(shopIndex, Factory.locations().size() - 1));
                shapes.setColor(item.side);
                shapes.rect(300, 326, 360, 150);
                shapes.setColor(item.road);
                shapes.rect(400, 326, 160, 150);
                shapes.setColor(item.stripe);
                shapes.rect(478, 326, 4, 150);
                shapes.setColor(item.coin);
                shapes.circle(443, 408, 9);
                shapes.circle(516, 378, 9);
            }
        } else if (mode.equals("bossExplosion")) {
            shapes.setColor(0, 0, 0, 0.34f);
            shapes.rect(0, 0, W, H);
            drawExplosionShapes();
            panel(232, 226, 496, 156, Color.valueOf("0B1220"), 0.82f);
        } else if (mode.equals("over") || mode.equals("levelComplete") || mode.equals("win") || mode.equals("crashScene")) {
            shapes.setColor(0, 0, 0, 0.50f);
            shapes.rect(0, 0, W, H);
            panel(230, 212, 500, 280, Color.valueOf("0B1220"), 0.90f);
        } else {
            panel(22, 508, 210, 106, Color.valueOf("0B1220"), 0.66f);
            panel(728, 394, 206, 184, Color.valueOf("0B1220"), 0.58f);
            panel(290, 548, 380, 68, Color.valueOf("0B1220"), 0.48f);
            panel(22, 20, 270, 58, Color.valueOf("0B1220"), 0.50f);
            if (playLevel == 10) {
                panel(318, 502, 324, 38, Color.valueOf("0B1220"), 0.74f);
                shapes.setColor(Color.valueOf("4B5563"));
                shapes.rect(336, 516, 288, 10);
                shapes.setColor(Color.valueOf("EF4444"));
                shapes.rect(336, 516, 288 * MathUtils.clamp(rivalHp / 300f, 0f, 1f), 10);
                shapes.setColor(Color.valueOf("4B5563"));
                shapes.rect(336, 504, 288, 8);
                shapes.setColor(Color.valueOf("22C55E"));
                shapes.rect(336, 504, 288 * MathUtils.clamp(playerHp / 100f, 0f, 1f), 8);
            }
        }
        shapes.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    void drawExplosionShapes() {
        float p = MathUtils.clamp(sceneTimer / 3.2f, 0f, 1f);
        float blast = 42f + p * 150f;
        float alpha = Math.max(0f, 1f - p * 0.75f);
        shapes.setColor(1f, 0.18f, 0.02f, 0.88f * alpha);
        shapes.circle(explosionX, explosionY, blast);
        shapes.setColor(1f, 0.68f, 0.05f, 0.92f * alpha);
        shapes.circle(explosionX, explosionY + 6f, blast * 0.68f);
        shapes.setColor(1f, 0.95f, 0.45f, 0.95f * alpha);
        shapes.circle(explosionX, explosionY + 10f, blast * 0.36f);
        for (int i = 0; i < 14; i++) {
            float a = i * 25.7f + sceneTimer * 78f;
            float inner = 22f + p * 34f;
            float outer = 92f + p * 170f + (i % 3) * 10f;
            float x1 = explosionX + MathUtils.cosDeg(a - 7f) * inner;
            float y1 = explosionY + MathUtils.sinDeg(a - 7f) * inner;
            float x2 = explosionX + MathUtils.cosDeg(a + 7f) * inner;
            float y2 = explosionY + MathUtils.sinDeg(a + 7f) * inner;
            float x3 = explosionX + MathUtils.cosDeg(a) * outer;
            float y3 = explosionY + MathUtils.sinDeg(a) * outer;
            shapes.setColor(1f, 0.52f, 0.02f, 0.72f * alpha);
            shapes.triangle(x1, y1, x2, y2, x3, y3);
        }
        for (int i = 0; i < 8; i++) {
            float a = i * 45f + 18f;
            float smoke = 38f + p * 125f + i * 2f;
            float sx = explosionX + MathUtils.cosDeg(a) * smoke;
            float sy = explosionY + MathUtils.sinDeg(a) * smoke * 0.72f;
            shapes.setColor(0.12f, 0.13f, 0.15f, 0.45f * p);
            shapes.circle(sx, sy, 24f + p * 34f);
        }
        shapes.setColor(1f, 0.89f, 0.20f, 0.9f * alpha);
        for (int i = 0; i < 18; i++) {
            float a = i * 20f + sceneTimer * 110f;
            float d = 62f + p * 180f + (i % 4) * 9f;
            float sx = explosionX + MathUtils.cosDeg(a) * d;
            float sy = explosionY + MathUtils.sinDeg(a) * d;
            shapes.rect(sx - 3f, sy - 3f, 6f, 6f);
        }
    }

    void panel(float x, float y, float w, float h, Color color, float alpha) {
        shapes.setColor(color.r, color.g, color.b, alpha);
        shapes.rect(x, y, w, h);
        shapes.setColor(1, 1, 1, alpha * 0.16f);
        shapes.rect(x, y + h - 2, w, 2);
        shapes.rect(x, y, 2, h);
    }

    void drawRoad() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        shapes.setColor(0.02f, 0.05f, 0.03f, 0.28f);
        shapes.rect(0, 0, ROAD_X, H);
        shapes.rect(ROAD_X + ROAD_W, 0, W - ROAD_X - ROAD_W, H);
        shapes.setColor(location.road.r * 0.72f, location.road.g * 0.72f, location.road.b * 0.72f, 0.98f);
        shapes.rect(ROAD_X, 0, ROAD_W, H);
        shapes.setColor(1, 1, 1, 0.035f);
        shapes.rect(ROAD_X + ROAD_W * 0.18f, 0, 18, H);
        shapes.rect(ROAD_X + ROAD_W * 0.72f, 0, 14, H);
        shapes.setColor(0, 0, 0, 0.20f);
        shapes.rect(ROAD_X + 20, 0, 14, H);
        shapes.rect(ROAD_X + ROAD_W - 34, 0, 14, H);
        shapes.setColor(Color.valueOf("5B6470"));
        shapes.rect(ROAD_X - 11, 0, 7, H);
        shapes.rect(ROAD_X + ROAD_W + 4, 0, 7, H);
        shapes.setColor(Color.valueOf("B7BEC8"));
        shapes.rect(ROAD_X - 16, 0, 5, H);
        shapes.rect(ROAD_X + ROAD_W + 11, 0, 5, H);
        shapes.setColor(0, 0, 0, 0.16f);
        shapes.rect(ROAD_X, 0, 7, H);
        shapes.rect(ROAD_X + ROAD_W - 7, 0, 7, H);
        shapes.setColor(location.stripe);
        float laneOffset = roadScroll % 92f;
        float sideOffset = roadScroll % 150f;
        if (laneOffset < 0) {
            laneOffset += 92f;
        }
        if (sideOffset < 0) {
            sideOffset += 150f;
        }
        for (int i = 1; i < LANES; i++) {
            float x = ROAD_X + i * (ROAD_W / (float) LANES);
            for (float y = H + 50 - laneOffset; y > -100; y -= 92) {
                shapes.rect(x - 2, y, 4, 54);
            }
        }
        shapes.setColor(1, 1, 1, 0.23f);
        for (float y = H + 50 - sideOffset; y > -120; y -= 150) {
            shapes.rect(ROAD_X + 18, y, 34, 6);
            shapes.rect(ROAD_X + ROAD_W - 52, y + 42, 34, 6);
        }
        shapes.setColor(location.coin.r, location.coin.g, location.coin.b, 0.20f);
        for (float y = H + 20 - sideOffset; y > -120; y -= 150) {
            shapes.rect(ROAD_X - 34, y, 9, 58);
            shapes.rect(ROAD_X + ROAD_W + 25, y + 42, 9, 58);
            shapes.circle(ROAD_X - 30, y + 58, 10);
            shapes.circle(ROAD_X + ROAD_W + 30, y + 100, 10);
        }
        shapes.setColor(Color.valueOf("7A828D"));
        for (float y = H + 40 - sideOffset; y > -120; y -= 150) {
            shapes.rect(ROAD_X - 22, y, 17, 7);
            shapes.rect(ROAD_X + ROAD_W + 5, y + 58, 17, 7);
        }
        shapes.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    void drawCarsAndItems() {
        if (mode.equals("bossExplosion")) {
            batch.draw(playerTexture, playerX - CAR_W / 2, PLAYER_Y, CAR_W, CAR_H);
            return;
        }
        for (int i = 0; i < things.size(); i++) {
            RoadThing t = things.get(i);
            if (t.type == RoadThing.TRAFFIC) {
                batch.draw(t.texture, t.x - t.w / 2, t.y, t.w, t.h);
            }
        }
        for (int i = 0; i < things.size(); i++) {
            RoadThing t = things.get(i);
            if (t.type != RoadThing.TRAFFIC) {
                batch.draw(t.texture, t.x - t.w / 2, t.y, t.w, t.h);
            }
        }
        drawRivalAndBullets();
        batch.draw(playerTexture, playerX - CAR_W / 2, PLAYER_Y, CAR_W, CAR_H);
    }

    void drawRivalAndBullets() {
        if ((mode.equals("game") || mode.equals("crashScene")) && rivalVisible && rivalY < H + 100) {
            batch.draw(rivalTexture, rivalX - CAR_W / 2, rivalY, CAR_W, CAR_H);
        }
        for (int i = 0; i < bullets.size(); i++) {
            Bullet b = bullets.get(i);
            batch.draw(bulletTexture, b.x - 7, b.y, 14, 26);
        }
        for (int i = 0; i < enemyBullets.size(); i++) {
            Bullet b = enemyBullets.get(i);
            batch.draw(enemyBulletTexture, b.x - 7, b.y, 14, 26);
        }
    }

    void drawUi() {
        if (mode.equals("dialog")) {
            drawDialog();
            return;
        }
        if (mode.equals("menu")) {
            drawMenu();
            return;
        }
        if (mode.equals("levels")) {
            drawLevelSelect();
            return;
        }
        if (mode.equals("shop")) {
            drawShop();
            return;
        }
        if (mode.equals("over")) {
            drawGameOver();
            return;
        }
        if (mode.equals("levelComplete")) {
            drawLevelComplete();
            return;
        }
        if (mode.equals("win")) {
            drawVictory();
            return;
        }
        if (mode.equals("bossExplosion")) {
            drawBossExplosion();
            return;
        }
        if (mode.equals("crashScene")) {
            drawCrashScene();
            return;
        }

        font.setColor(Color.WHITE);
        font.draw(batch, "СЧЁТ", 38, 598);
        font.setColor(Color.valueOf("7CFF57"));
        font.draw(batch, "" + score, 38, 570);
        font.setColor(Color.WHITE);
        font.draw(batch, car.name, 318, 596);
        font.draw(batch, location.name, 318, 568);
        font.setColor(Color.YELLOW);
        font.draw(batch, "СКОРОСТЬ", 762, 558);
        font.draw(batch, (int) currentSpeedKmh + " КМ/Ч", 762, 530);
        font.draw(batch, "ДИСТАНЦИЯ", 762, 490);
        font.draw(batch, String.format("%.2f КМ", meters / 1000f), 762, 462);
        font.setColor(Color.valueOf("7CFF57"));
        font.draw(batch, "БЫСТРО " + String.format("%.1f", highSpeedTime), 762, 420);
        if (playLevel < 10) {
            font.setColor(Color.YELLOW);
            font.draw(batch, "ЦЕЛЬ " + (int) Math.min(levelGoal(), meters) + "/" + (int) levelGoal() + " м", 38, 96);
        } else {
            font.setColor(Color.valueOf("EF4444"));
            font.draw(batch, "HP ЕРАСЫЛА " + Math.max(0, rivalHp), 38, 96);
            font.setColor(Color.valueOf("7CFF57"));
            font.draw(batch, "ТВОЁ HP " + Math.max(0, playerHp), 752, 96);
        }
        if (message.length() > 0) {
            center(message, 496, font, Color.YELLOW);
        }
        if (!state.id().equals("playing")) {
            center(state.title(), 336, bigFont, Color.WHITE);
            center(state.hint(), 292, font, Color.WHITE);
        }
    }

    void drawDialog() {
        font.setColor(Color.WHITE);
        center("ГЛАВА " + playLevel + " - " + levelTitle(playLevel), 558, font, Color.YELLOW);
        font.draw(batch, dialogSpeaker, 326, 282);
        font.setColor(Color.LIGHT_GRAY);
        wrap(dialogLine, 326, 246, 420);
        font.setColor(Color.WHITE);
        center("SPACE / ENTER - Далее", 126, font, Color.YELLOW);
        bigFont.setColor(Color.WHITE);
        if (dialogSpeaker.equals("Ерасыл")) {
            bigFont.draw(batch, "Е", 238, 270);
        } else if (dialogSpeaker.equals("Бекзат")) {
            bigFont.draw(batch, "Б", 238, 270);
        } else {
            bigFont.draw(batch, "!", 242, 270);
        }
    }

    String levelTitle(int level) {
        if (level == 1) return "Первая встреча";
        if (level <= 4) return "Погоня";
        if (level == 5) return "Вторая встреча";
        if (level <= 8) return "Усиленная погоня";
        if (level == 9) return "Столкновение";
        return "Финальная битва";
    }

    void wrap(String text, float x, float y, float maxWidth) {
        String[] words = text.split(" ");
        String line = "";
        float yy = y;
        for (int i = 0; i < words.length; i++) {
            String next = line.length() == 0 ? words[i] : line + " " + words[i];
            layout.setText(font, next);
            if (layout.width > maxWidth && line.length() > 0) {
                font.draw(batch, line, x, yy);
                yy -= 28;
                line = words[i];
            } else {
                line = next;
            }
        }
        if (line.length() > 0) {
            font.draw(batch, line, x, yy);
        }
    }

    void drawMenu() {
        center("АЛМАТЫ ДВИЖ", 526, bigFont, Color.WHITE);
        center("премиальная 2D-погоня по ночному Алматы", 486, font, Color.LIGHT_GRAY);
        center("Рекорд: " + save.best + "   Глава: " + MathUtils.clamp(save.level, 1, 10), 446, font, Color.YELLOW);
        center("Машина: " + car.name + "   Трасса: " + storyLocation(MathUtils.clamp(save.level, 1, 10)).name, 416, font, Color.WHITE);
        center(save.storyWon ? "Кампания пройдена" : "Сюжет: " + levelTitle(MathUtils.clamp(save.level, 1, 10)), 392, font, Color.valueOf("7CFF57"));
        String[] items = {"СТАРТ", "ВЫБОР УРОВНЯ", "ГАРАЖ", "ВЫХОД"};
        for (int i = 0; i < items.length; i++) {
            center((i == menuChoice ? "> " : "  ") + items[i] + (i == menuChoice ? " <" : "  "), 310 - i * 48, font,
                    i == menuChoice ? Color.valueOf("111827") : Color.WHITE);
        }
        center("W/S выбрать   SPACE подтвердить   F11 полный экран", 86, font, Color.LIGHT_GRAY);
        center("A/D поворот   W газ   S тормоз во время игры", 56, font, Color.LIGHT_GRAY);
    }

    void drawLevelSelect() {
        center("ВЫБОР УРОВНЯ", 540, bigFont, Color.WHITE);
        center("Выбери любую главу кампании Алматы Движ", 502, font, Color.LIGHT_GRAY);
        for (int i = 0; i < 10; i++) {
            int level = i + 1;
            float x = 198 + (i % 5) * 148;
            float y = i < 5 ? 392 : 272;
            Color color = i == levelChoice ? Color.valueOf("111827") : Color.WHITE;
            centerAt("УР. " + level, x, y, font, color);
            centerAt(levelTitle(level), x, y - 25, font, i == levelChoice ? Color.valueOf("111827") : Color.LIGHT_GRAY);
        }
        center("A/D или W/S переключать   SPACE старт", 124, font, Color.YELLOW);
        center("ESC назад в главное меню", 90, font, Color.LIGHT_GRAY);
    }

    void drawGameOver() {
        if (playLevel == 10) {
            drawFinalLoseDialog();
            return;
        }
        center(playLevel == 10 ? "ПОРАЖЕНИЕ" : "АВАРИЯ", 452, bigFont, Color.WHITE);
        center("Счёт: " + score, 400, bigFont, Color.YELLOW);
        center("Дистанция: " + String.format("%.2f КМ", meters / 1000f), 354, font, Color.WHITE);
        center("Опасные обгоны: " + nearMiss, 324, font, Color.WHITE);
        center("Машина разбита, попробуй пройти уровень снова", 288, font, Color.LIGHT_GRAY);
        center("SPACE попробовать снова   ESC главное меню", 250, font, Color.WHITE);
    }

    void drawFinalLoseDialog() {
        center("ПОРАЖЕНИЕ", 462, bigFont, Color.valueOf("EF4444"));
        center("Ерасыл: Я же говорил. Я лучший гонщик Алматы Движ.", 404, font, Color.WHITE);
        center("Бекзат: Машина не слушается... но я ещё вернусь.", 372, font, Color.LIGHT_GRAY);
        center("Ерасыл: Запомни этот звук мотора. Это звук твоего поражения.", 340, font, Color.WHITE);
        center("Система: Сирены приближаются, трасса темнеет, гонка закончена.", 308, font, Color.YELLOW);
        center("SPACE попробовать снова   ESC главное меню", 252, font, Color.WHITE);
    }

    void drawLevelComplete() {
        center("УРОВЕНЬ ПРОЙДЕН", 452, bigFont, Color.WHITE);
        center("Счёт: " + score, 400, bigFont, Color.YELLOW);
        center("Открыта глава " + save.level + "/10", 354, font, Color.valueOf("7CFF57"));
        center("SPACE продолжить сюжет   G гараж   ESC меню", 306, font, Color.WHITE);
    }

    void drawCrashScene() {
        center("СТОЛКНОВЕНИЕ", 452, bigFont, Color.WHITE);
        center("Обе машины влетают друг в друга!", 400, font, Color.YELLOW);
        center("Финальная дуэль уже близко...", 354, font, Color.WHITE);
    }

    void drawBossExplosion() {
        center("ВЗРЫВ", 352, bigFont, Color.valueOf("FACC15"));
        center("Машина Ерасыла уничтожена!", 306, font, Color.WHITE);
        center("Финальная сцена начнётся через момент...", 272, font, Color.LIGHT_GRAY);
    }

    void drawVictory() {
        center("ПОБЕДА", 462, bigFont, Color.valueOf("7CFF57"));
        center("Ерасыл: Нет... Я не мог так просто проиграть...", 404, font, Color.WHITE);
        center("Бекзат: Ты был быстрым, но скорость без уважения - это опасность.", 372, font, Color.LIGHT_GRAY);
        center("Ерасыл: Алматы Движ... запомнит это имя...", 340, font, Color.WHITE);
        center("Система: Машина Ерасыла взрывается, трасса наконец затихает.", 308, font, Color.YELLOW);
        center("Бекзат: Гонка закончена. Теперь можно ехать домой.", 276, font, Color.valueOf("7CFF57"));
        center(sceneTimer < 1.2f ? "Финальная сцена..." : "SPACE главное меню   Выбор уровня - для новой игры", 230, font, Color.WHITE);
    }

    void drawShop() {
        List<CarSkin> cars = Factory.cars();
        List<Location> locations = Factory.locations();
        center("ГАРАЖ", 552, bigFont, Color.WHITE);
        center("TAB раздел   ENTER купить/выбрать", 508, font, Color.YELLOW);
        if (shopType == 0) {
            if (shopIndex >= cars.size()) {
                shopIndex = 0;
            }
            CarSkin item = cars.get(shopIndex);
            Texture texture = skinTextures.get(item.id);
            batch.draw(texture, W / 2f - 48, 314, 96, 172);
            String status = save.hasCar(item.id) ? (save.car.equals(item.id) ? "ВЫБРАНО" : "КУПЛЕНО") : "ЦЕНА " + item.price;
            center("МАШИНА " + (shopIndex + 1) + "/" + cars.size(), 276, font, Color.LIGHT_GRAY);
            center(item.name, 238, bigFont, Color.WHITE);
            center(item.info + "   " + status, 190, font, Color.WHITE);
            center("Управление " + item.handling + "   Бонус x" + String.format("%.2f", item.bonus), 160, font, Color.YELLOW);
        } else {
            if (shopIndex >= locations.size()) {
                shopIndex = 0;
            }
            Location item = locations.get(shopIndex);
            String status = save.hasLocation(item.id) ? (save.location.equals(item.id) ? "ВЫБРАНО" : "КУПЛЕНО") : "ЦЕНА " + item.price;
            center("ЛОКАЦИЯ " + (shopIndex + 1) + "/" + locations.size(), 286, font, Color.LIGHT_GRAY);
            center(item.name, 244, bigFont, Color.WHITE);
            center(item.info + "   " + status, 196, font, Color.WHITE);
            center("Скорость x" + String.format("%.2f", item.speed) + "   Трафик x" + String.format("%.2f", item.traffic)
                    + "   Очки x" + String.format("%.2f", item.bonus), 160, font, Color.YELLOW);
        }
        if (message.length() > 0) {
            center(message, 124, font, Color.ORANGE);
        }
        center("СТРЕЛКИ выбрать   ESC меню", 86, font, Color.LIGHT_GRAY);
    }

    void center(String text, float y, BitmapFont usedFont, Color color) {
        usedFont.setColor(color);
        layout.setText(usedFont, text);
        usedFont.draw(batch, text, (W - layout.width) / 2f, y);
    }

    void centerAt(String text, float x, float y, BitmapFont usedFont, Color color) {
        usedFont.setColor(color);
        layout.setText(usedFont, text);
        usedFont.draw(batch, text, x - layout.width / 2f, y);
    }

    static float laneX(int lane) {
        return ROAD_X + ROAD_W / (float) LANES * lane + ROAD_W / (float) LANES / 2f;
    }

    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.F11) {
            toggleFullscreen();
            return true;
        }
        Command.KeyCommand cmd = keys.get(keycode);
        if (cmd != null) {
            cmd.run(this);
            return true;
        }
        return false;
    }

    void toggleFullscreen() {
        if (fullScreen) {
            Gdx.graphics.setWindowedMode(W, H);
            fullScreen = false;
        } else {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            fullScreen = true;
        }
    }

    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.W || keycode == Input.Keys.UP) {
            accelerating = false;
        }
        if (keycode == Input.Keys.S || keycode == Input.Keys.DOWN) {
            braking = false;
        }
        return false;
    }

    public void left() {
        if (mode.equals("shop")) {
            changeShop(-1);
            return;
        }
        if (mode.equals("levels")) {
            changeLevel(-1);
            return;
        }
        if (mode.equals("game") && state.id().equals("playing")) {
            lane = Math.max(0, lane - 1);
            targetX = laneX(lane);
        }
    }

    public void right() {
        if (mode.equals("shop")) {
            changeShop(1);
            return;
        }
        if (mode.equals("levels")) {
            changeLevel(1);
            return;
        }
        if (mode.equals("game") && state.id().equals("playing")) {
            lane = Math.min(LANES - 1, lane + 1);
            targetX = laneX(lane);
        }
    }

    public void jump() {
        if (mode.equals("menu")) {
            menuChoice--;
            if (menuChoice < 0) {
                menuChoice = 3;
            }
            return;
        }
        if (mode.equals("levels")) {
            changeLevel(-1);
            return;
        }
        accelerating = true;
    }

    public void slide() {
        if (mode.equals("menu")) {
            menuChoice++;
            if (menuChoice > 3) {
                menuChoice = 0;
            }
            return;
        }
        if (mode.equals("levels")) {
            changeLevel(1);
            return;
        }
        braking = true;
    }

    public void action() {
        if (mode.equals("menu")) {
            if (menuChoice == 0) {
                startStory();
            } else if (menuChoice == 1) {
                levelSelect();
            } else if (menuChoice == 2) {
                garage();
            } else {
                Gdx.app.exit();
            }
            return;
        }
        if (mode.equals("dialog")) {
            nextDialog();
            return;
        }
        if (mode.equals("levelComplete")) {
            startStory();
            return;
        }
        if (mode.equals("win")) {
            if (sceneTimer < 1.2f) {
                return;
            }
            menu();
            return;
        }
        if (mode.equals("over")) {
            if (playLevel == 10) {
                startStory();
            } else {
                startRun();
            }
            return;
        }
        if (mode.equals("shop")) {
            buyOrSelect();
            return;
        }
        if (mode.equals("levels")) {
            save.level = levelChoice + 1;
            save.storyWon = false;
            Singleton.get().save(save);
            startStory();
            return;
        }
        if (playLevel == 10 && state.id().equals("playing")) {
            shoot();
            return;
        }
        if (state.id().equals("playing")) {
            state = new State.Paused();
        } else {
            state = new State.Playing();
        }
    }

    void shoot() {
        if (fireCooldown > 0) {
            return;
        }
        fireCooldown = 0.22f;
        bullets.add(new Bullet(playerX, PLAYER_Y + CAR_H - 10, 680f));
    }

    public void garage() {
        mode = "shop";
        state = new State.Ready();
        things.clear();
        rivalVisible = false;
        message = "";
    }

    void levelSelect() {
        mode = "levels";
        state = new State.Ready();
        things.clear();
        bullets.clear();
        enemyBullets.clear();
        rivalVisible = false;
        levelChoice = MathUtils.clamp(save.level, 1, 10) - 1;
        message = "";
    }

    public void tab() {
        if (mode.equals("shop")) {
            shopType = shopType == 0 ? 1 : 0;
            shopIndex = 0;
            message = "";
        }
    }

    public void menu() {
        if (mode.equals("menu")) {
            Gdx.app.exit();
        } else {
            mode = "menu";
            state = new State.Ready();
            things.clear();
            bullets.clear();
            enemyBullets.clear();
            rivalVisible = false;
            message = "";
        }
    }

    void changeShop(int diff) {
        int count = shopType == 0 ? Factory.cars().size() : Factory.locations().size();
        shopIndex += diff;
        if (shopIndex < 0) {
            shopIndex = count - 1;
        }
        if (shopIndex >= count) {
            shopIndex = 0;
        }
        message = "";
    }

    void changeLevel(int diff) {
        levelChoice += diff;
        if (levelChoice < 0) {
            levelChoice = 9;
        }
        if (levelChoice > 9) {
            levelChoice = 0;
        }
    }

    void buyOrSelect() {
        if (shopType == 0) {
            CarSkin item = Factory.cars().get(shopIndex);
            if (save.hasCar(item.id)) {
                save.car = item.id;
                message = "Выбрано: " + item.name;
            } else if (save.coins >= item.price) {
                save.coins -= item.price;
                save.cars.add(item.id);
                save.car = item.id;
                message = "Куплено: " + item.name;
            } else {
                message = "Не хватает бензина";
            }
        } else {
            Location item = Factory.locations().get(shopIndex);
            if (save.hasLocation(item.id)) {
                save.location = item.id;
                message = "Выбрано: " + item.name;
            } else if (save.coins >= item.price) {
                save.coins -= item.price;
                save.locations.add(item.id);
                save.location = item.id;
                message = "Куплено: " + item.name;
            } else {
                message = "Не хватает бензина";
            }
        }
        car = Factory.car(save.car);
        location = Factory.location(save.location);
        ride = Decorator.make(car);
        playerTexture = skinTextures.get(car.id);
        if (coinTexture != null) {
            coinTexture.dispose();
        }
        coinTexture = coinTexture(location.coin);
        Singleton.get().save(save);
        bus.send(new Observer.Event("shop", message, save.coins));
    }

    public void onEvent(Observer.Event event) {
    }

    void disposeTextures() {
        for (Texture texture : skinTextures.values()) {
            if (texture != null) {
                texture.dispose();
            }
        }
        for (int i = 0; i < trafficTextures.size(); i++) {
            trafficTextures.get(i).dispose();
        }
        if (coinTexture != null) coinTexture.dispose();
        if (rivalTexture != null) rivalTexture.dispose();
        if (bulletTexture != null) bulletTexture.dispose();
        if (enemyBulletTexture != null) enemyBulletTexture.dispose();
        if (forestBgTexture != null) forestBgTexture.dispose();
    }

    public void resize(int width, int height) {
        if (viewport != null) {
            viewport.update(width, height, true);
        }
    }

    public void dispose() {
        disposeTextures();
        batch.dispose();
        shapes.dispose();
        font.dispose();
        bigFont.dispose();
    }

    public boolean keyTyped(char character) { return false; }
    public boolean touchDown(int screenX, int screenY, int pointer, int button) { return false; }
    public boolean touchUp(int screenX, int screenY, int pointer, int button) { return false; }
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) { return false; }
    public boolean touchDragged(int screenX, int screenY, int pointer) { return false; }
    public boolean mouseMoved(int screenX, int screenY) { return false; }
    public boolean scrolled(float amountX, float amountY) { return false; }

    static class RoadThing {
        static final int TRAFFIC = 0;
        static final int COIN = 1;

        int type;
        int lane;
        float x;
        float y;
        float w;
        float h;
        float speed = 1f;
        boolean near = false;
        Texture texture;

        RoadThing(int type, int lane, float x, float y, float w, float h) {
            this.type = type;
            this.lane = lane;
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }

        boolean good() {
            return type == COIN;
        }
    }

    static class Bullet {
        float x;
        float y;
        float speed;

        Bullet(float x, float y, float speed) {
            this.x = x;
            this.y = y;
            this.speed = speed;
        }
    }
}
