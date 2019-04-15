package blind.al.geoindplugin;


import android.location.Location;
import android.util.Log;

/**
 *
 * Perturbs location data using Differential Privacy.
 * Paper: Geo-Indistinguishability: Differential Privacy for Location-Based Systems
 * URL: https://arxiv.org/abs/1212.1984
 *
 * The implementation is based on the Location Guard browser extension:  https://github.com/chatziko/location-guard
 *
 */
public class GeoInd {

    private final double EARTH_RADIUS = 6371e3; //in meters

    // privacy parameter to control the perturbation
    private double mEpsilon;

    public GeoInd(double epsilon){
        mEpsilon = epsilon;
        if(PluginMain.DEBUG){
            Log.d(PluginMain.TAG,"Initializing GeoInd with eps: "+ mEpsilon);
        }
    }

    /**
     * Add Laplace noise to perturb location data
     * @param location: original location data
     * @return noisy location data
     */
    public void perturbLocation(Location location){

        // generate random angle in [0, 2*PI)
        double angle = Math.random() * Math.PI * 2;

        // sample radius from inverse cumulative polar Laplace distribution
        double x = (Math.random()-1) / Math.E;
        double radius = -(LambertW(x)+1)/mEpsilon;

        // add sampled noisy distance to the original location
        double angularDistance = radius/EARTH_RADIUS;
        double lat = degreeToRadian(location.getLatitude());
        double lon = degreeToRadian(location.getLongitude());

        double noisyLat = Math.asin(Math.sin(lat)*Math.cos(angularDistance)
                +Math.cos(lat)*Math.sin(angularDistance)*Math.cos(angle));
        double noisyLon = lon + Math.atan2(Math.sin(angle)*Math.sin(angularDistance)*Math.cos(lat),
                Math.cos(angularDistance)-Math.sin(lat)*Math.sin(noisyLat));

        // normalize longitute to -180 to 180
        noisyLon = (noisyLon + 3 * Math.PI) % (2 * Math.PI) - Math.PI;

        location.setLatitude(radianToDegree(noisyLat));
        location.setLongitude(radianToDegree(noisyLon));

    }

    private double LambertW(double x){
        double minDiff = 1e-10;
        if(x == 0){
            return 0;
        }else if(x == -1/Math.E){
            return -1;
        }else{
            double q = Math.log(-x);
            double p = 1;
            while(Math.abs(p-q)>minDiff){
                p=(q*q+x/Math.exp(q))/(q+1);
                q=(p*p+x/Math.exp(p))/(p+1);
            }
            return (Math.round(1000000*q)/1000000.0);
        }
    }

    private double degreeToRadian(double degree){
        return degree * Math.PI / 180;
    }

    private double radianToDegree(double radian){
        return radian * 180 / Math.PI;
    }


}
