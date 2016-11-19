package plume.spartan.challongeandroid.store;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by charpe_r on 06/11/16.
 */

public class Participant {

    private long id = -1;
    private String name = null;
    private int seed = -1;

    public Participant() {
    }

    public Participant(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.getLong("id");
        this.name = jsonObject.getString("name");
        this.seed = jsonObject.getInt("seed");
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return (this.id);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return (this.name);
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    public int getSeed() {
        return (this.seed);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Participant) {
            Participant obj_tmp = (Participant) obj;
            if (this.name.equals(obj_tmp.getName()) && this.seed == obj_tmp.getSeed())
                return (true);
        }
        return (false);
    }
}
