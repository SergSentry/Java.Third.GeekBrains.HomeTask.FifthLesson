import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

public class Car implements Runnable {
    private static int CARS_COUNT;
    static {
        CARS_COUNT = 0;
    }

    private Race race;
    private int speed;
    private CyclicBarrier pistol;
    private CountDownLatch beginRace;
    private CountDownLatch endRace;
    private AtomicBoolean finishLine;
    private String name;

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    public Car(Race race, int speed, CyclicBarrier pistol, CountDownLatch beginRace, CountDownLatch endRace, AtomicBoolean finishLine) {
        this.race = race;
        this.speed = speed;
        this.pistol = pistol;
        this.beginRace = beginRace;
        this.endRace = endRace;
        this.finishLine = finishLine;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
    }

    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int)(Math.random() * 800));
            System.out.println(this.name + " готов");

            beginRace.countDown();
            pistol.await();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
        }

        if (!finishLine.getAndSet(true))
        {
            System.out.printf("Участник #%d WIN\n",CARS_COUNT);
        }

        endRace.countDown();
    }
}
