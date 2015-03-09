package btransfer.android.com.bluetoothtransfer;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private static final int DISCOVER_DURATION = 300;
    private static final int REQUEST_BLU = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendViaBluetooth(View v) {

        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

        if(btAdapter == null){
            Toast.makeText(this, "Bluetooth is not supported on this device ", Toast.LENGTH_LONG).show();
        } else {
            enableBluetooth();
        }

    }

    public void enableBluetooth(){
        Intent discoveryIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);

        discoveryIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,DISCOVER_DURATION);

        startActivityForResult(discoveryIntent, REQUEST_BLU);

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == DISCOVER_DURATION && requestCode ==REQUEST_BLU){

            File sourceFile = new File("//storage/emulated/0/DCIM/md5sum.txt");
            Intent intent=new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");
            //File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "md5sum.txt");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(sourceFile));
            startActivity(intent);

            PackageManager pm = getPackageManager();
            List<ResolveInfo> appsList = pm.queryIntentActivities(intent,0);

            if (appsList.size() > 0){
                String packageName = null;
                String className = null;
                boolean found =false;

                for (ResolveInfo info : appsList) {
                    packageName = info.activityInfo.packageName;
                    if(packageName.equals("com.android.bluetooth")){
                        className = info.activityInfo.name;
                        found = true;
                        break;
                    }
                }

                if(!found){
                    Toast.makeText(this, "Bluetooth is not found", Toast.LENGTH_LONG).show();

                }else {
                    intent.setClassName(packageName, className);
                    startActivity(intent);}


            }


        }else {
            Toast.makeText(this, "Bluetooth is cancelled ", Toast.LENGTH_LONG).show();
        }

    }
}
