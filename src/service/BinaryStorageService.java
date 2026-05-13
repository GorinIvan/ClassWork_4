package service;

import model.User;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class BinaryStorageService {
    private final String filePath;
    private final ScheduledExecutorService scheduler;

    public BinaryStorageService(String filePath) {
        this.filePath = filePath;
        this.scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "auto-save");
            t.setDaemon(true);
            return t;
        });
    }

    public void startAutoSave(Supplier<Map<String, User>> dataSupplier, long intervalSeconds) {
        scheduler.scheduleAtFixedRate(() -> {
            save(dataSupplier.get());
            System.out.println("\n[АвтоСохранение] Данные сохранены в файл.");
        }, intervalSeconds, intervalSeconds, TimeUnit.SECONDS);
    }

    public void stopAutoSave() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public synchronized void save(Map<String, User> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(new HashMap<>(users));
        } catch (IOException e) {
            System.err.println("Ошибка сохранения данных: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, User> load() {
        File file = new File(filePath);
        if (!file.exists()) {
            return new HashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (Map<String, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Ошибка загрузки данных: " + e.getMessage());
            return new HashMap<>();
        }
    }
}
