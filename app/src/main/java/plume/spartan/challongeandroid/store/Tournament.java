package plume.spartan.challongeandroid.store;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by charpe_r on 01/11/16.
 */

public class Tournament {

    private String name = null;
    private String url = null;
    private String tournament_type = null;
    private String game_name = null;
    private String completed_at = null;
    private int participants_count = -1;
    private String live_image_url = null;
    private boolean participants_locked = false;

    public Tournament() {
    }

    public Tournament(JSONObject jsonObject) throws JSONException {
        this.name = jsonObject.getString("name");
        this.url = jsonObject.getString("url");
        this.tournament_type = jsonObject.getString("tournament_type");
        this.game_name = jsonObject.getString("game_name");
        this.completed_at = jsonObject.getString("completed_at");
        this.participants_count = jsonObject.getInt("participants_count");
        this.live_image_url = jsonObject.getString("live_image_url");
        this.participants_locked = jsonObject.getBoolean("participants_locked");
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return (this.name);
    }

    public void setTournamentType(String tournamentType) {
        this.tournament_type = tournamentType;
    }

    public String getUrl() {
        return (this.url);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTournamentType() {
        return (this.tournament_type);
    }

    public void setGameName(String gameName) {
        this.game_name = gameName;
    }

    public String getGameName() {
        return (this.game_name);
    }

    public void setCompletedAt(String completedAt) {
        this.completed_at = completedAt;
    }

    public String getCompletedAt() {
        return (this.completed_at);
    }

    public boolean isCompleted() {
        if (this.completed_at == null) {
            return false;
        } else {
            return true;
        }
    }

    public void setParticipantsCount(int participantsCount) {
        this.participants_count = participantsCount;
    }

    public int getParticipantsCount() {
        return (this.participants_count);
    }

    public void setLiveImageUrl(String liveImageUrl) {
        this.live_image_url = liveImageUrl;
    }

    public String getLiveImageUrl() {
        return (this.live_image_url);
    }

    public void setParticipantsLocked(boolean participantsLocked) {
        this.participants_locked = participantsLocked;
    }

    public boolean isParticipantsLocked() {
        return (this.participants_locked);
    }
}
