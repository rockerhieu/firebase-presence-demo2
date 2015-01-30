package testy.firebase.squar.com.userpresence;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Logger;
import com.firebase.client.ServerValue;
import com.firebase.client.ValueEventListener;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends Activity {

  private Firebase mRef;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Firebase.getDefaultConfig().setLogLevel(Logger.Level.DEBUG);
    Firebase.setAndroidContext(this);
    mRef = new Firebase("http://mychat-beta.firebaseio.com/").child("mychat").child("presences").child("testy");
    mRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        if ((Boolean) dataSnapshot.getValue()) {
          comeOnline();
        } else {
        }
      }

      @Override
      public void onCancelled(FirebaseError firebaseError) {

      }
    });
    setContentView(R.layout.activity_main);
  }

  private void comeOnline() {
    mRef.goOnline();
    mRef.onDisconnect().setValue(getOfflineValue(), new Firebase.CompletionListener() {
      @Override
      public void onComplete(FirebaseError firebaseError, Firebase firebase) {
        if (firebaseError == null) {
          mRef.setValue(getOnlineValue());
        }
      }
    });
  }

  private void comeOffline() {
    mRef.goOffline();
  }

  private Map<String, Object> getOfflineValue() {
    Map<String, Object> map = new HashMap<>();
    map.put("status", "offline");
    map.put("lastSeen", ServerValue.TIMESTAMP);
    return map;
  }

  private Map<String, Object> getOnlineValue() {
    Map<String, Object> map = new HashMap<>();
    map.put("status", "online");
    map.put("lastSeen", ServerValue.TIMESTAMP);
    return map;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
