package com.example.petemergency;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.maps.*;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.petemergency.databinding.ActivityFindVetMapsBinding;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.model.Geometry;
import com.google.maps.model.PlaceDetails;
import com.google.maps.model.PlacesSearchResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FindVetMapsActivity extends FragmentActivity implements OnMapReadyCallback, OnTaskCompleted {

    private GoogleMap mMap;
    private ActivityFindVetMapsBinding binding;
    private FusedLocationProviderClient fusedLocationClient;
    private Location loc;
    private String curPhone = "";
    private MarkerOptions mo = null;
    private boolean done = false;
    private Map<String, String> phones = new HashMap<>();

    @Override
    public void onTaskCompleted(String values) {
        curPhone = values;
        mo.snippet(curPhone);
        mMap.addMarker(mo);
    }


    private class osl implements OnSuccessListener<Location> {

        @Override
        public void onSuccess(Location location) {
            loc = location;
        }
    }

    @Override
    @SuppressLint("MissingPermission")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFindVetMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(FindVetMapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    if (loc != null) {
                        loc = (Location) o;
                    }
                }
            });
        } else {
            ActivityCompat.requestPermissions(FindVetMapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

                // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng cur = new LatLng(47.6607, -122.1490);
        //LatLng cur = new LatLng(loc.getLatitude(), loc.getLongitude());
        System.out.println(cur);
        mMap.addMarker(new MarkerOptions().position(cur).title("You"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cur, 12));

        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                "location=" + cur.latitude + "," + cur.longitude +
                "&radius=45000" +
                "&keyword=" + PetActivity.petType + "+Emergency+" + "Veterinarians" +
                "&key=AIzaSyAbWbTTuNxoNqUE8urNq4IkppCj-8-QL1M";

        GeoApiContext context = new GeoApiContext.Builder().apiKey("AIzaSyAbWbTTuNxoNqUE8urNq4IkppCj-8-QL1M").build();
        NearbySearchRequest searchReq = PlacesApi.nearbySearchQuery(context, new com.google.maps.model.LatLng(47.6607, -122.1490));
        searchReq = searchReq.radius(15000).keyword(PetActivity.petType + "+Emergency+" + "Veterinarians");
        PlacesSearchResult[] results = searchReq.awaitIgnoreError().results;
        for (PlacesSearchResult res : results) {
            String name = res.name;
            com.google.maps.model.LatLng latLng = res.geometry.location;
            String placeid = res.placeId;
            PlaceDetailsRequest detReq = PlacesApi.placeDetails(context, placeid).fields(PlaceDetailsRequest.FieldMask.FORMATTED_PHONE_NUMBER);
            PlaceDetails details = detReq.awaitIgnoreError();
            String phone = details.formattedPhoneNumber;
            MarkerOptions options = new MarkerOptions();
            options.position(new LatLng(latLng.lat, latLng.lng));
            options.title(name);
            options.snippet(phone);
            mMap.addMarker(options);
        }

        //new PlaceTask().execute(url);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        if (loc != null) {
                            loc = (Location) o;
                        }
                    }
                });
            }
        }
    }

    private class PlaceTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            try {
                data = downloadUrl(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //System.out.println("data: " + data);
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            //System.out.println("placetask: " + s);
            new ParserTask().execute(s);
        }
    }

    private String downloadUrl(String string) throws IOException {
        URL url = new URL(string);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.connect();
        InputStream stream = conn.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder builder = new StringBuilder();
        String line = "";
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        String data = builder.toString();
        reader.close();
        conn.disconnect();
        //System.out.println("download: " + data);
        return data;
    }

    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> { ;

        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {
            JsonParser jsonParser = new JsonParser();
            List<HashMap<String, String>> mapList = null;
            JSONObject object = null;
            try {
                System.out.println(strings[0]);
                object = new JSONObject(strings[0]);
                mapList = jsonParser.parseResult(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mapList;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> hashMaps) {
            mMap.clear();

            for (int i=0; i < hashMaps.size(); i++) {
                HashMap<String, String> hashMapList = hashMaps.get(i);
                double lat = Double.parseDouble(hashMapList.get("lat"));
                double lng = Double.parseDouble(hashMapList.get("lng"));
                String name = hashMapList.get("name");
                String place_id = hashMapList.get("place_id");

                String url = "https://maps.googleapis.com/maps/api/place/details/json?" +
                        "place_id=" + place_id +
                        "&fields=name%2Crating%2Cformatted_phone_number" +
                        "&key=AIzaSyAbWbTTuNxoNqUE8urNq4IkppCj-8-QL1M";
                System.out.println(url);

                new PlaceDetTask().execute(url);

                /*int j = 0;
                while (!done) {
                    try {
                        System.out.println(j + " waiting");
                        Thread.sleep(100);
                        j++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }*/

                System.out.println(curPhone);
                LatLng latLng = new LatLng(lat, lng);
                MarkerOptions options = new MarkerOptions();
                options.position(latLng);
                options.title(name);
                //options.snippet("425 503 6118");
                //mMap.addMarker(options);
                mo = options;
            }
        }
    }

    ///////////////////////////////////////////////
    /////////////////////////////////////////////
    ///////////////////////////////////////////////
    ///////////////////////////////////////////////

    @SuppressLint("StaticFieldLeak")
    private class PlaceDetTask extends AsyncTask<String, Integer, String> {


        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            try {
                data = downloadDetUrl(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("data: " + data);
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            //super.onPostExecute(s);
            System.out.println("placetask: " + s);
           // new ParserDetTask(this).execute(s);
        }
    }

    private String downloadDetUrl(String string) throws IOException {
        URL url = new URL(string);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.connect();
        InputStream stream = conn.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder builder = new StringBuilder();
        String line = "";
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        String data = builder.toString();
        reader.close();
        conn.disconnect();
        System.out.println("download: " + data);
        return data;
    }

    private class ParserDetTask extends AsyncTask<String, Integer, HashMap<String, String>> {

        private OnTaskCompleted ls;

        private ParserDetTask(OnTaskCompleted liste) {
            ls = liste;
        }

        @Override
        protected HashMap<String, String> doInBackground(String... strings) {
            System.out.println("entering parser doinback");
            JsonDetParser jsonParser = new JsonDetParser();
            HashMap<String, String> map = null;
            JSONObject object = null;
            try {
                System.out.println(" strngs[0]" + strings[0]);
                object = new JSONObject(strings[0]);
                map = jsonParser.parseResult(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return map;
        }

        @Override
        protected void onPostExecute(HashMap<String, String> hashMap) {

            String phone = hashMap.get("phone");
            //System.out.println("phone here: " + phone);
            ls.onTaskCompleted(phone);
        }
    }
}