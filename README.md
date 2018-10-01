# Java.Third.GeekBrains.HomeTask.FifthLesson
Java multithread part 2

1. Приведённый код перенести в новый проект.

public class MainClass {
    public static final int CARS_COUNT = 4;
    public static void main(String[] args) {
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        Race race = new Race(new Road(60), new Tunnel(), new Road(40));
        Car[] cars = new Car[CARS_COUNT];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10));
        }
        for (int i = 0; i < cars.length; i++) {
            new Thread(cars[i]).start();
        }
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
    }
}
public class Car implements Runnable {
    private static int CARS_COUNT;
    static {
        CARS_COUNT = 0;
    }
    private Race race;
    private int speed;
    private String name;
    public String getName() {
        return name;
    }
    public int getSpeed() {
        return speed;
    }
    public Car(Race race, int speed) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
    }
    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int)(Math.random() * 800));
            System.out.println(this.name + " готов");
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
        }
    }
}
public abstract class Stage {
    protected int length;
    protected String description;
    public String getDescription() {
        return description;
    }
    public abstract void go(Car c);
}
public class Road extends Stage {
    public Road(int length) {
        this.length = length;
        this.description = "Дорога " + length + " метров";
    }
    @Override
    public void go(Car c) {
        try {
            System.out.println(c.getName() + " начал этап: " + description);
            Thread.sleep(length / c.getSpeed() * 1000);
            System.out.println(c.getName() + " закончил этап: " + description);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
public class Tunnel extends Stage {
    public Tunnel() {
        this.length = 80;
        this.description = "Тоннель " + length + " метров";
    }
    @Override
    public void go(Car c) {
        try {
            try {
                System.out.println(c.getName() + " готовится к этапу(ждет): " + description);
                System.out.println(c.getName() + " начал этап: " + description);
                Thread.sleep(length / c.getSpeed() * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println(c.getName() + " закончил этап: " + description);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
public class Race {
    private ArrayList<Stage> stages;
    public ArrayList<Stage> getStages() { return stages; }
    public Race(Stage... stages) {
        this.stages = new ArrayList<>(Arrays.asList(stages));
    }
}

Организуем гонки:
Все участники должны стартовать одновременно, несмотря на то что на подготовку у каждого их них уходит разное время
В туннель не может заехать одновременно больше половины участников (условность)
Попробуйте всё это синхронизировать.
Только после того как все завершат гонку нужно выдать объявление об окончании
Можете корректировать классы (в т.ч. конструктор машин) и добавлять объекты классов из пакета util.concurrent
Пример выполнения кода до корректировки:

ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!
Участник #2 готовится
ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!
ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!
Участник #1 готовится
Участник #4 готовится
Участник #3 готовится
Участник #3 готов
Участник #3 начал этап: Дорога 60 метров
Участник #2 готов
Участник #2 начал этап: Дорога 60 метров
Участник #1 готов
Участник #1 начал этап: Дорога 60 метров
Участник #4 готов
Участник #4 начал этап: Дорога 60 метров
Участник #3 закончил этап: Дорога 60 метров
Участник #3 готовится к этапу(ждет): Тоннель 80 метров
Участник #3 начал этап: Тоннель 80 метров
Участник #2 закончил этап: Дорога 60 метров
Участник #2 готовится к этапу(ждет): Тоннель 80 метров
Участник #2 начал этап: Тоннель 80 метров
Участник #1 закончил этап: Дорога 60 метров
Участник #1 готовится к этапу(ждет): Тоннель 80 метров
Участник #1 начал этап: Тоннель 80 метров
Участник #4 закончил этап: Дорога 60 метров
Участник #4 готовится к этапу(ждет): Тоннель 80 метров
Участник #4 начал этап: Тоннель 80 метров
Участник #2 закончил этап: Тоннель 80 метров
Участник #2 начал этап: Дорога 40 метров
Участник #3 закончил этап: Тоннель 80 метров
Участник #3 начал этап: Дорога 40 метров
Участник #2 закончил этап: Дорога 40 метров
Участник #1 закончил этап: Тоннель 80 метров
Участник #1 начал этап: Дорога 40 метров
Участник #4 закончил этап: Тоннель 80 метров
Участник #4 начал этап: Дорога 40 метров
Участник #3 закончил этап: Дорога 40 метров
Участник #1 закончил этап: Дорога 40 метров
Участник #4 закончил этап: Дорога 40 метров

Что примерно должно получиться:

ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!
Участник #2 готовится
Участник #1 готовится
Участник #4 готовится
Участник #3 готовится
Участник #2 готов
Участник #4 готов
Участник #1 готов
Участник #3 готов
ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!
Участник #2 начал этап: Дорога 60 метров
Участник #4 начал этап: Дорога 60 метров
Участник #3 начал этап: Дорога 60 метров
Участник #1 начал этап: Дорога 60 метров
Участник #1 закончил этап: Дорога 60 метров
Участник #3 закончил этап: Дорога 60 метров
Участник #3 готовится к этапу(ждет): Тоннель 80 метров
Участник #1 готовится к этапу(ждет): Тоннель 80 метров
Участник #1 начал этап: Тоннель 80 метров
Участник #3 начал этап: Тоннель 80 метров
Участник #4 закончил этап: Дорога 60 метров
Участник #4 готовится к этапу(ждет): Тоннель 80 метров
Участник #2 закончил этап: Дорога 60 метров
Участник #2 готовится к этапу(ждет): Тоннель 80 метров
Участник #3 закончил этап: Тоннель 80 метров
Участник #1 закончил этап: Тоннель 80 метров
Участник #2 начал этап: Тоннель 80 метров
Участник #4 начал этап: Тоннель 80 метров
Участник #3 начал этап: Дорога 40 метров
Участник #1 начал этап: Дорога 40 метров
Участник #3 закончил этап: Дорога 40 метров
Участник #3 - WIN
Участник #1 закончил этап: Дорога 40 метров
Участник #4 закончил этап: Тоннель 80 метров
Участник #4 начал этап: Дорога 40 метров
Участник #2 закончил этап: Тоннель 80 метров
Участник #2 начал этап: Дорога 40 метров
Участник #2 закончил этап: Дорога 40 метров
Участник #4 закончил этап: Дорога 40 метров
ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!