package blind.al.geoindplugin;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.IPluginCalendarInterposer;
import android.os.IPluginCameraInterposer;
import android.os.IPluginContactsInterposer;
import android.os.IPluginLocationInterposer;
import android.os.PluginService;
import android.os.RemoteException;

public class PluginMain extends Service {

    public static String TAG = "Dalf";
    public static boolean DEBUG = false;

    public PluginLocationInterposer mLocationInterposer;

    @Override
    public void onCreate() {
        super.onCreate();

        mLocationInterposer = new PluginLocationInterposer();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private final PluginService mBinder = new PluginService() {
        @Override
        public IPluginLocationInterposer getLocationInterposer() throws RemoteException {
            return mLocationInterposer;
        }

        @Override
        public IPluginContactsInterposer getContactsInterposer() throws RemoteException {
            return null;
        }

        @Override
        public IPluginCalendarInterposer getCalendarInterposer() throws RemoteException {
            return null;
        }

        @Override
        public IPluginCameraInterposer getCameraInterposer() throws RemoteException {
            return null;
        }
    };
}
