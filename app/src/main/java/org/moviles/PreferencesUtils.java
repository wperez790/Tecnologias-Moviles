package org.moviles;

import android.content.SharedPreferences;
import android.content.Context;

public class PreferencesUtils {

    private Context context;
    private SharedPreferences sharedPreferences;


    public PreferencesUtils(Context context) {
        this.context = context;
        sharedPreferences = this.context.getSharedPreferences(Constants.PREFS_NAME , Context.MODE_PRIVATE);
    }

    public void setUnity(String unity){
        sharedPreferences.edit().putString(Constants.USER_UNITY, unity).apply();
    }
    public void setNotification(Boolean notification){
        sharedPreferences.edit().putBoolean(Constants.USER_NOTIFICATION,notification).apply();
    }
    public void setTime(String time){
        sharedPreferences.edit().putString(Constants.USER_TIME, time).apply();
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }
}
