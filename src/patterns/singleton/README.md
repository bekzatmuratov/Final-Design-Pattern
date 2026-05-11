# Singleton

Ответственный: person-1  
Рабочая ветка: `feature/person-1`

## Где используется в игре

Основной код: `src/main/java/game/Singleton.java`

Singleton хранит один общий объект сохранений игры, чтобы разные части проекта брали данные из одного места.

## Файлы

- `PatternCode.java` - простой код паттерна Singleton
- `RunExample.java` - пример запуска

## Запуск примера

```bash
cd src/patterns/singleton
javac PatternCode.java RunExample.java
java RunExample
```
