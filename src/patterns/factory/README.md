# Factory

Ответственный: person-1  
Рабочая ветка: `feature/person-1`

## Где используется в игре

Основной код: `src/main/java/game/Factory.java`

Factory создаёт машины и локации по id. Благодаря этому в игровом коде не нужно вручную писать `new CarSkin(...)` в разных местах.

## Файлы

- `PatternCode.java` - простой код паттерна Factory
- `RunExample.java` - пример запуска

## Запуск примера

```bash
cd src/patterns/factory
javac PatternCode.java RunExample.java
java RunExample
```
