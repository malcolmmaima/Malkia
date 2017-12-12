package malkia.malkiaunesco.example.com.malkia.Model;

/**
 * Created by User on 6/29/2017.
 */

public class UserAccountSettings {

    private String description;
    private String display_name;
    private long followers;
    private long following;
    private long points;
    private long posts;
    private String profile_photo;
    private String background_photo;
    private String username;
    private String website;

    public UserAccountSettings(String description, String display_name, long followers, long following,long points,
                               long posts, String profile_photo, String background_photo, String username, String website) {
        this.description = description;
        this.display_name = display_name;
        this.followers = followers;
        this.following = following;
        this.points = points;
        this.posts = posts;
        this.profile_photo = profile_photo;
        this.background_photo=background_photo;
        this.username = username;
        this.website = website;
    }
    public UserAccountSettings() {

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public long getFollowers() {
        return followers;
    }

    public void setFollowers(long followers) {
        this.followers = followers;
    }

    public long getFollowing() {
        return following;
    }

    public void setFollowing(long following) {
        this.following = following;
    }

    public long getPoints() {
        return points;
    }

    public void setPoints(long points) {
        this.points = points;
    }

    public long getPosts() {
        return posts;
    }

    public void setPosts(long posts) {
        this.posts = posts;
    }

    public String getProfile_photo() {
        return profile_photo;
    }

    public void setProfile_photo(String profile_photo) {
        this.profile_photo = profile_photo;
    }

    public String getBackground_photo() {
        return background_photo;
    }

    public void setBackground_photo(String profile_photo) {
        this.background_photo = profile_photo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }


    @Override
    public String toString() {
        return "UserAccountSettings{" +
                "description='" + description + '\'' +
                ", display_name='" + display_name + '\'' +
                ", followers=" + followers +
                ", following=" + following +
                ", points=" + points +
                ", posts=" + posts +
                ", profile_photo='" + profile_photo + '\'' +
                ", background_photo='" + background_photo + '\'' +
                ", username='" + username + '\'' +
                ", website='" + website + '\'' +
                '}';
    }
}
