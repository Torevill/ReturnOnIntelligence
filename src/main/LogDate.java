import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

public class LogDate {
    private LocalDate date;
    private ArrayList<User> users = new ArrayList<>();

    public LogDate(LocalDate date, String username, String url, int seconds) {
        this.date = date;
        users.add(new User(username, url, seconds));
    }

    public void update(String username, String url, int seconds) {
        User temp = new User(username, url, seconds);
        if (!users.contains(temp)) {
            users.add(temp);
        } else {
            users.get(users.indexOf(temp)).plusSecondsToUrlOrCreateNewUrl(url, seconds);
        }
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public ArrayList<User> getUsers() {
        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o1.getUsername().compareTo(o2.getUsername());
            }
        });
        return users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogDate logDate = (LogDate) o;
        return Objects.equals(date, logDate.date);
    }

    @Override
    public int hashCode() {

        return Objects.hash(date);
    }
}
