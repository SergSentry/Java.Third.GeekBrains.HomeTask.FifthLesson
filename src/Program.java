import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class Program {
    public static final int CARS_COUNT = 4;
    private static final int CARS_HALF_COUNT = CARS_COUNT / 2;

    private static Semaphore bottleneck = new Semaphore(CARS_HALF_COUNT);
    private static CountDownLatch beginRace = new CountDownLatch(CARS_COUNT);
    private static CountDownLatch endRace = new CountDownLatch(CARS_COUNT);
    private static CyclicBarrier pistol = new CyclicBarrier(CARS_COUNT);

    private static AtomicBoolean finishLine = new AtomicBoolean(false);

    public static void main(String[] args) throws InterruptedException {
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");

        Race race = new Race(new Road(60), new Tunnel(bottleneck), new Road(40));

        Car[] cars = new Car[CARS_COUNT];

        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10), pistol, beginRace, endRace, finishLine);
        }

        for (int i = 0; i < cars.length; i++) {
            new Thread(cars[i]).start();
        }

        beginRace.await();
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");
        endRace.await();
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
    }
}
