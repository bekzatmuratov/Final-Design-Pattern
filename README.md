# Алматы Движ

2D игра на Java/libGDX в стиле Traffic Racer. Главный герой Бекзат проходит 10 уровней, догоняет Ерасыла, избегает трафика и сражается с ним в финальном уровне.

## Запуск

Собрать проект:

```bash
mvn clean package
```

Запустить игру:

```bash
java -XstartOnFirstThread -jar target/traffic-car-2d.jar
```

## Управление

- `A / Left` - влево
- `D / Right` - вправо
- `W / Up` - газ
- `S / Down` - тормоз
- `Space / Enter` - старт, диалоги, пауза, выбор
- `Space` на 10 уровне - стрелять
- `G` - гараж
- `Tab` - сменить раздел гаража
- `F11` - полный экран
- `Esc` - меню

## Структура проекта

```text
Final-Design-Pattern/
├── README.md
├── pom.xml
└── src/
    └── main/
        ├── java/game/
        └── resources/
```

## Где что находится

- `pom.xml` - настройки Maven и libGDX
- `src/main/java/game` - весь Java-код игры
- `src/main/resources/cars` - текстуры машин
- `src/main/resources/forest_bg.jpg` - фон дороги

## Основные файлы кода

- `Main.java` - запуск окна игры
- `TrafficGame2D.java` - основная логика игры, уровни, UI, враг, диалоги
- `Factory.java` - создание машин и локаций
- `Builder.java` - сборка локаций
- `Singleton.java` - сохранение прогресса
- `State.java` - состояния игры
- `Command.java` - управление клавишами
- `Strategy.java` - подсчёт очков
- `Observer.java` - игровые события
- `Decorator.java` - бонусы машин

## Разделение работы

person-1:
- структура проекта
- player logic
- уровни
- главное меню
- выбор уровней

person-2:
- enemy logic
- диалоги
- collision fixes
- level 10
- финальная документация

## Сюжет

- уровни 1, 5, 9 и 10 содержат сюжетные сцены
- уровни 2-4 и 6-8 являются обычной погоней
- Ерасыл появляется только на нужных сюжетных уровнях
- 10 уровень - финальная битва с HP Ерасыла и стрельбой
