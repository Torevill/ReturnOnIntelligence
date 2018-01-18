import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

public class FolderListener {
    private WatchService watchService = null;
    private WatchKey key = null;
    private boolean flag = true;
    private Deque<Path> stack = new ArrayDeque<>();
    int fileNumber = 0;

    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter log directory");
        Path path = Paths.get(scanner.nextLine());
        System.out.println("Thank you. Starting listening. Your commands:\n" +
                "EXIT - force killing of programm\n" +
                "DELETE FileName - delete file\n" +
                "MOVE fileName TO directory - move file to dircetory");

        try {
            watchService = path.getFileSystem().newWatchService();
            path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        while (flag) {
            try {
                key = watchService.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (WatchEvent event : key.pollEvents()) {
                if (event.kind().name().equals("ENTRY_CREATE")) {
                    System.out.println("Found file " + event.context() + "\nStart parsing.");
                    FileParser fileParser = new FileParser(path + "/" + event.context().toString());
                    fileParser.writeListLogDateInFile(fileParser.readFileInList(), fileNumber++);
                }
            }
            key.reset();
        }
    }

}
