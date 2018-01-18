import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;


public class FileParser {
    Path inputFile;

    public FileParser(String string) {
        this.inputFile = Paths.get(string);
    }

    public FileParser(Path path) {
        this.inputFile = path;
    }


    public void writeListLogDateInFile(List<LogDate> list, int fileNumber) {
        String outputFile = "avg_file" + fileNumber + ".csv";
        try (BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile, true)))) {
            for (LogDate nowLogDate : list) {
                output.write(nowLogDate.getDate().toString() + "\n");
                ArrayList<User> users = nowLogDate.getUsers();
                for (User nowUser : users) {
                    ArrayList<String> strings = nowUser.getArrayOfStrings();
                    for (String nowString : strings) {
                        output.write(nowString);
                    }
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public List<LogDate> readFileInList() {
        List<LogDate> logDates = new ArrayList<>();
        List<LogBox> nextDayUnits = new ArrayList<>();
        try (BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile.toFile())))) {
            while (input.ready()) {
                String line = input.readLine();
                String[] lines = line.split(",");
                LocalDateTime time = LocalDateTime.ofEpochSecond(Long.parseLong(lines[0]), 0, ZoneOffset.UTC);
                String username = lines[1];
                String url = lines[2];
                int seconds = Integer.parseInt(lines[3]);
                if (isBeforeMidnight(time, seconds) >= 0) {
                    nextDayUnits.add(new LogBox(time.plusDays(1), username, url, isBeforeMidnight(time, seconds)));
                    seconds = seconds - isBeforeMidnight(time, seconds);
                }
                LogDate temp = new LogDate(time.toLocalDate(), username, url, seconds);

                if (!logDates.contains(temp)) {
                    logDates.add(temp);
                } else {
                    logDates.get(logDates.indexOf(temp)).update(username, url, seconds);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (LogBox now : nextDayUnits) {
            LogDate temp = new LogDate(now.getTime().toLocalDate(), now.getUsername(), now.getUrl(), now.getSeconds());
            if (!logDates.contains(temp)) {
                logDates.add(temp);
            } else {
                logDates.get(logDates.indexOf(temp)).update(now.getUsername(), now.getUrl(), now.getSeconds());
            }
        }


        return logDates;

    }

    //return int of seconds after midninght
    private int isBeforeMidnight(LocalDateTime time, Integer seconds) {
        long temp = time.toEpochSecond(ZoneOffset.UTC) + seconds;
        if (time.getDayOfMonth() == LocalDateTime.ofEpochSecond(temp, 0, ZoneOffset.UTC).getDayOfMonth()) {
            return -1;
        } else {
            int firstDaySecondsToMidnight = (23 - time.getHour()) * 3600 + (59 - time.getMinute()) * 60 + (60 - time.getSecond());
            return seconds - firstDaySecondsToMidnight;
        }
    }

    class LogBox {
        private LocalDateTime time;
        private String username;
        private String url;
        private int seconds;

        public LogBox(LocalDateTime time, String username, String url, int seconds) {
            this.time = time;
            this.username = username;
            this.url = url;
            this.seconds = seconds;
        }

        public LocalDateTime getTime() {
            return time;
        }

        public void setTime(LocalDateTime time) {
            this.time = time;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getSeconds() {
            return seconds;
        }

        public void setSeconds(int seconds) {
            this.seconds = seconds;
        }
    }
}



