package blind.al.geoindplugin;

import android.location.Location;
import android.os.IPluginLocationInterposer;
import android.os.RemoteException;

public class PluginLocationInterposer extends IPluginLocationInterposer.Stub {

    private GeoInd mGeoInd;

    // Privacy budget use by GeoInd perturbation
    public double mPrivacyBudget = 0.001;

    public PluginLocationInterposer(){
        mGeoInd = new GeoInd(mPrivacyBudget);
    }

    @Override
    public Location modifyLocation(String packageName, Location location) throws RemoteException {
        // Perturb location using DP
        mGeoInd.perturbLocation(location);
        return location;
    }
}
