public class Main {
    public int fileNumber = 0;

    public static void main(String[] args) {
        FolderListener fl = new FolderListener();
        fl.start();
    }
}
