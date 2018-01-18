import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;


public class User {
    private String username;
    private Map<String, SiteInfo> sites = new TreeMap<>();//key - url


    public User(String username, String url, int seconds) {
        this.username = username;
        sites.put(url, new SiteInfo(url, seconds));
    }

    public String getUsername() {
        return username;
    }

    public Map<String, SiteInfo> getSites() {
        return sites;
    }

    public void plusSecondsToUrlOrCreateNewUrl(String url, Integer seconds) {
        if (!sites.containsKey(url)) sites.put(url, new SiteInfo(url, seconds));
        else sites.get(url).plusSeconds(seconds);
    }

    public ArrayList<String> getArrayOfStrings() {
        ArrayList<String> list = new ArrayList<>();
        for (Map.Entry<String, SiteInfo> now : sites.entrySet()) {
            String temp = username + "," + now.getValue().getUrl() + "," + now.getValue().getAverageSeconds() + "\n";
            list.add(temp);
        }
        return list;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {

        return Objects.hash(username);
    }

    class SiteInfo {
        private String url;
        private Integer seconds;
        private int counter = 0;

        public SiteInfo(String url, Integer seconds) {
            this.url = url;
            this.seconds = seconds;
            counter++;
        }

        public void plusSeconds(Integer seconds) {
            this.seconds += seconds;
            counter++;
        }

        public int getAverageSeconds() {
            return seconds / counter;
        }

        public String getUrl() {
            return url;
        }

        public Integer getSeconds() {
            return seconds;
        }

        public int getCounter() {
            return counter;
        }


    }
}
