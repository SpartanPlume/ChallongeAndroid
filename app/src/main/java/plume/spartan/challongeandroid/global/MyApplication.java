package plume.spartan.challongeandroid.global;

import android.app.Application;

import plume.spartan.challongeandroid.store.Tournament;

/**
 * Created by charpe_r on 05/11/16.
 */

public class MyApplication extends Application {

    private String username = "";
    private String api_key = "";
    private String currentFragmentTag = null;
    private Tournament tournament = null;

    public MyApplication() {

    }

    public void setUser(String username, String apiKey) {
        this.username = username;
        this.api_key = apiKey;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return (this.username);
    }

    public void setApiKey(String apiKey) {
        this.api_key = apiKey;
    }

    public String getApiKey() {
        return (this.api_key);
    }

    public void setCurrentFragmentTag(String newFragmentTag) {
        this.currentFragmentTag = newFragmentTag;
    }

    public String getCurrentFragmentTag() {
        return (this.currentFragmentTag);
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public Tournament getTournament() {
        return (this.tournament);
    }
}
