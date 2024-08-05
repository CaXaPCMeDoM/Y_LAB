# Y_LAB
*** Проект написан на `Java 17` 
Используется микросервисная архитектура ***

## Сборка проекта
# Склонируйте репозиторий:

```
git clone https://github.com/CaXaPCMeDoM/Y_LAB
cd Y_LAB
```
# Скомпилируйте проект:

```
javac -d out src/com/y_lab/y_lab/*.java src/com/y_lab/y_lab/entity/*.java src/com/y_lab/y_lab/manager/*.java src/com/y_lab/y_lab/service/*.java src/com/y_lab/y_lab/exception/*.java src/com/y_lab/y_lab/in/*.java src/com/y_lab/y_lab/out/*.java
```
## Запуск проекта
# Запустите приложение:

```
java -cp out com.y_lab.y_lab.YLabApplication
```
* Следуйте инструкциям на экране для использования функционала приложения.

## Запуск тестов
# Запустите тесты:
```
javac -cp "path/to/junit-platform-console-standalone.jar:out" -d test_out test/com/y_lab/y_lab/*.java
java -jar path/to/junit-platform-console-standalone.jar --class-path test_out --scan-class-path
```

## Структура проекта
src/com/y_lab/y_lab/ - основной исходный код приложения
src/com/y_lab/y_lab/entity/ - классы сущностей (Car, User, Order, AuditEntity, UserInfo(для сохранения в context сессии текущего пользователя))
src/com/y_lab/y_lab/starter/manager/ - классы-прослойки между меню и сервисами (CarManagement, UserManagement и т.д.)
src/com/y_lab/y_lab/service/ - сервисные классы (UserService, CarService, OrderService и т.д.)
src/com/y_lab/y_lab/exception/ - классы исключений
src/com/y_lab/y_lab/in/ - классы для обработки ввода
src/com/y_lab/y_lab/out/ - классы для обработки вывода
test/com/y_lab/y_lab/ - тесты