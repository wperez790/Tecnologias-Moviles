package org.moviles.activity.Fragments;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngQuad;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.RasterLayer;
import com.mapbox.mapboxsdk.style.sources.ImageSource;

import org.moviles.Contexto;
import org.moviles.activity.MenuActivity;
import org.moviles.activity.R;
import org.moviles.business.ClimaBusiness;
import org.moviles.model.Clima;

import java.io.InputStream;
import java.util.Map;

public class FragmentMap extends Fragment  {

    private MapView mapView;
    private MapboxMap mapboxMap;
    private Handler handler;
    private Runnable runnable;
    private ImageButton buttonSearch;
    private EditText editText;
    private ClimaBusiness climaBO;
    public static Clima clima;
    private static final String ID_IMAGE_SOURCE = "animated_image_source";
    private static final String ID_IMAGE_LAYER = "animated_image_layer";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map,container,false);
        clima = Contexto.getClima();
        buttonSearch =  view.findViewById(R.id.buttonSearch);
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        editText = view.findViewById(R.id.cityText);
        editText.setText(clima.getCiudad()+ ", "+ clima.getPais());
        climaBO = Contexto.getClimaBusiness(getActivity().getApplication());


        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                mapboxMap.setCameraPosition(getCameraPosition());
                mapboxMap.setStyle(Style.SATELLITE_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {

// Map is set up and the style has loaded. Now you can add data or make other map adjustments.
                     /*   style.addSource(new ImageSource(ID_IMAGE_SOURCE,
                                getCoords(), getWeatherImage()));*/

// Add layer
                        //style.addLayer(new RasterLayer(ID_IMAGE_LAYER, ID_IMAGE_SOURCE));

// Loop the GeoJSON refreshing

                        buttonSearch.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String city  =  editText.getText().toString();
                                clima = getClimaByCity(city);
                               CameraPosition position = getCameraPosition();

                                mapboxMap.animateCamera(CameraUpdateFactory
                                        .newCameraPosition(position), 4000, new MapboxMap.CancelableCallback() {
                                    @Override
                                    public void onCancel() {
                                        refresh();
                                    }

                                    @Override
                                    public void onFinish() {
                                        refresh();
                                    }
                                });

                            }
                        });
                        InputStream gifInputStream = getImageByCondicion(clima.getCondicion());

                        handler = new Handler();
                        if(gifInputStream!=null) {
                            runnable = new RefreshImageRunnable(style, Movie.decodeStream(gifInputStream), handler);
                            handler.postDelayed(runnable, 100);

                        }


                    }
                });
            }
        });

        return view;

    }
    private void refresh(){
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();

    }

    private CameraPosition getCameraPosition(){
        Double lat = Double.parseDouble(clima.getCoordLat());
        Double lon = Double.parseDouble(clima.getCoordLon());

        CameraPosition position = new CameraPosition.Builder()
                .target(new LatLng(lat, lon)) // Sets the new camera position
                .zoom(4) // Sets the zoom
                .bearing(360) // Rotate the camera
                .tilt(30) // Set the camera tilt
                .build(); // Creates a CameraPosition from the builder

        return position;
    }

    private InputStream getImageByCondicion(String condicion) {
        InputStream inputStream= null;
        switch (condicion) {
            case "Thunderstorm":
                inputStream = getResources().openRawResource(R.raw.thunder);
                ;
                break;
            case "Clear":
                inputStream = getResources().openRawResource(R.raw.sun);
                ;
                break;
            case "Clouds":
                inputStream = getResources().openRawResource(R.raw.cloudy);
                ;
                break;
            case "Rain":
                inputStream = getResources().openRawResource(R.raw.rainy);
                ;
                break;
            case "Drizzle":
                inputStream = getResources().openRawResource(R.raw.verycloudy);
                break;
            case "Snow":
                inputStream = getResources().openRawResource(R.raw.snowy);
                break;

        }
        return inputStream;
    }

    private Clima getClimaByCity(String city) {
        ((MenuActivity)getActivity()).getFromApi(city);
        Clima clima = Contexto.getClima();
        ClimaBusiness cBO =  Contexto.getClimaBusiness(getActivity().getApplication());
        cBO.insert(clima);
        return clima;
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }


    private static class RefreshImageRunnable implements Runnable {
        private ImageSource imageSource;
        private Style style;
        private Movie movie;
        private Handler handler;
        private long movieStart;
        private Bitmap bitmap;
        private Canvas canvas;

        RefreshImageRunnable(Style style, Movie movie, Handler handler) {
            this.style = style;
            this.movie = movie;
            this.handler = handler;
            bitmap = Bitmap.createBitmap(movie.width(), movie.height(), Bitmap.Config.ARGB_8888);
            canvas = new Canvas(bitmap);
        }

        @Override
        public void run() {
            double centerLat = Double.parseDouble(clima.getCoordLat());//-31.4135;
            double centerLong= Double.parseDouble(clima.getCoordLon());//-64.18105;
            double size =0.6;

            double topLeftLat= centerLat + size;
            double topLeftLong = centerLong - size;

            double topRightLat= centerLat + size;
            double topRightLong = centerLong + size;

            double bottomRightLat = centerLat - size;
            double bottomRightLong = centerLong + size;

            double bottomLeftLat = centerLat - size;
            double bottomLeftLong = centerLong - size;



            long now = android.os.SystemClock.uptimeMillis();
            if (movieStart == 0) {
                movieStart = now;
            }

            int dur = movie.duration();
            if (dur == 0) {
                dur = 1000;
            }

            movie.setTime((int) ((now - movieStart) % dur));
            movie.draw(canvas, 0, 0);

            if (imageSource == null) {
// Set the bounds/size of the gif. Then create an image source object with a unique id,
// the bounds, and drawable image
                imageSource = new ImageSource(ID_IMAGE_SOURCE,
                        new LatLngQuad(
                                new LatLng(topLeftLat, topLeftLong),
                                new LatLng(topRightLat, topRightLong),
                                new LatLng(bottomRightLat, bottomRightLong),
                                new LatLng(bottomLeftLat, bottomLeftLong))
                                /*new LatLng(-31.4135, -64.181),
                                new LatLng(-31.4135, -55.181),
                                new LatLng(-40.4135, -55.181),
                                new LatLng(-40.4135, -64.181))*/, bitmap);

// Add the source to the map
                style.addSource(imageSource);

// Create an raster layer with a unique id and the image source created above. Then add the layer to the map.
                style.addLayer(new RasterLayer(ID_IMAGE_LAYER, ID_IMAGE_SOURCE));
            }


            imageSource.setImage(bitmap);
            handler.postDelayed(this, 50);

        }
    }




}