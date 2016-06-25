package bacakomik.daeng.app.comic;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Isfahani on 02-Mei-16.
 */
public class ObjectTrailer {
    @SerializedName("results")
    List<DataTrailer> results;

    public class DataTrailer {
        @SerializedName("name")
        public String name;

        @SerializedName("key")
        public String key;

        @SerializedName("type")
        public String type;
    }
}
