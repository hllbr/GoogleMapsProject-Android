package com.hllbr.googlemapsproject;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapLongClickListener(this);//critics
        //kullanıcı konumuna erişmek için

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        //LocationListener = konum dinleyicisi konum yöneticisinden haber alıp konum değiştiğinde haver veren yapımız


        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                //dinleyici(interface olduğu) için bir obje değil bir dinleyici metod oluşturuyor

                //bu yapının işi konum değişti olarak ifade edilebilir.

                //location sınıfı ltlng a göre daha geniş başka özellikleride tutabiliyor

                //veri değişikliğini kontrol etmek için önce yazdırarak test etmek istiyorum

                //location.tostring veriyi string olarak yazdırmama imkan sağlayan bir yapı

                //burada önce konum için gerekli izinleri almalı bu izinleri sorgulamalı ve konumu neye göre alacağımızı belirlemeliyiz.
                //mMap.clear();

               // System.out.println("Location "+location.toString());
                /*
                LatLng userLocation = new LatLng(location.getAltitude(),location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,15));

                 */
                //Enlem ve boylamı adrese çevirmem gerekiyor ki anlaşılabilir bir hal alsın
            }

        };
        //coin ve artbook projeleri içerisinde alınan izinleri kontrol etmekten farklı değil

        //location permission is dangerous

        //tehlikeli izin seviyesinde olduğu için açık bir şekilde bu izni kullanıcıdan istemek zorundayız .

        //API 23 öncesinde bu yapıyı kontrol etmeye gerek dahi yoktu.


        // Add a marker in Sydney and move the camera
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            //not allowed operation area =

            //Permission operations =

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

            //It is a necessary structure to control the permit


        }else{
            //allowed operation area =
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            Location lastLocation =  locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastLocation != null){
                LatLng userlastlocation = new LatLng(lastLocation.getAltitude(),lastLocation.getLongitude());
                mMap.addMarker(new MarkerOptions().position(userlastlocation).title("YOUR LAST LOCATİON"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userlastlocation,15));
            }



            //son bilinene konumu getir
        }
        //Latitude enlem,longtidue boylam
        //39.74824347025661, 37.01547672561973
        /*
        LatLng medrese = new LatLng(39.74824347025661, 37.01547672561973);
        mMap.addMarker(new MarkerOptions().position(medrese).title("Marker in GÖK MEDRESE"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(medrese,15));//camera hareketleri

         */
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //izinlerimizin sonucunda meydana gelen işleri ele aldığım alan
        //izin verildiği andan itibaren neler olacak
        if(grantResults.length>0){
            if (requestCode == 1){
                if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        mMap.clear();
        //haritaya uzun tıklandığında olacak işlemleri yazıyorum

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        String address = "";

        //içerisinde adres sınıfının bulunduğu bir liste oluşturuyorum
        try {
            List<Address> addressList = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
            if(addressList != null && addressList.size() > 0){
                if(addressList.get(0).getThoroughfare()!= null){
                    address += addressList.get(0).getThoroughfare();
                    if(addressList.get(0).getSubThoroughfare() != null){
                        address += addressList.get(0).getSubThoroughfare();
                        System.out.println("Location : "+address);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMap.addMarker(new MarkerOptions().position(latLng).title(address));



    }
}