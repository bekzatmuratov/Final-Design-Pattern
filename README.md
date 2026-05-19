# Алматы Движ

2D игра на Java/libGDX в стиле Traffic Racer. Главный герой Бекзат проходит 10 уровней, догоняет Ерасыла, избегает трафика и сражается с ним в финальном уровне.

## Запуск

```bash
./scripts/build.sh
./scripts/run.sh
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

## Простая структура проекта

```text
Final-Design-Pattern/
├── README.md
├── pom.xml
├── scripts/
│   ├── build.sh
│   └── run.sh
└── src/
    └── main/
        ├── java/game/
        └── resources/
```

## Где что находится

- `src/main/java/game` - весь код игры в одной папке
- `src/main/resources/cars` - текстуры машин
- `src/main/resources/forest_bg.jpg` - фон дороги
- `scripts/build.sh` - сборка проекта
- `scripts/run.sh` - запуск игры

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
- финальная проверка README

## Сюжет

- уровни 1, 5, 9 и 10 содержат сюжетные сцены
- уровни 2-4 и 6-8 являются обычной погоней
- Ерасыл появляется только на нужных сюжетных уровнях
- 10 уровень - финальная битва с HP Ерасыла и стрельбой

## Проверка перед сдачей

```bash
./scripts/build.sh
./scripts/run.sh
```
