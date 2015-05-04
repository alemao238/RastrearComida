package br.com.sebrae.rastrearcomida.activity;

import android.app.Activity;
import android.graphics.Color;
import android.net.http.AndroidHttpClient;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import br.com.sebrae.rastrearcomida.R;
import br.com.sebrae.rastrearcomida.adapter.ListaEmpresaAdapter;
import br.com.sebrae.rastrearcomida.modelo.Empresa;
import br.com.sebrae.rastrearcomida.modelo.Endereco;
import br.com.sebrae.rastrearcomida.util.Localizador;


public class MainActivity extends Activity{
    private TextView distanciaView;
    private ListView lista;
    private ListaEmpresaAdapter empresaAdapter;

    private SupportMapFragment mapFrag;
    private GoogleMap map;
    private Marker marker;
    private Polyline polyline;
    private List<LatLng> list;
    private long distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Dados ficticios
        List<Empresa> listaEmpresas = new ArrayList<Empresa>();
        Empresa empresa = new Empresa();
        empresa.setId(1L);
        empresa.setNome("El-Shaday");
        empresa.setDescricao("Lanchonete - sanduiches, açaí cremoso");
        empresa.setEndereco(new Endereco());
        empresa.getEndereco().setLogradouro("Rua Virgilio Lima 126, Seis de Agosto, Rio Branco");
        listaEmpresas.add(empresa);
        empresa = new Empresa();
        empresa.setId(2L);
        empresa.setNome("Pinguin");
        empresa.setDescricao("Sorveteria");
        empresa.setEndereco(new Endereco());
        empresa.getEndereco().setLogradouro("Rua Londrina 169, Nova Estação, Rio Branco");
        listaEmpresas.add(empresa);

        lista = (ListView) findViewById(R.id.listaFood);

        //GoogleMapOptions options = new GoogleMapOptions();
        //options.zOrderOnTop(true);

        //mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa);

        //map = mapFrag.getMap();

        Localizador localizador = new Localizador(this);
        LatLng latLng = localizador.getCoordenada("Rua Virgilio Lima 126, Seis de Agosto, Rio Branco");
        LatLng latLng2 = localizador.getCoordenada("Rua Londrina 167, Nova Estação, Rio Branco");
        distanciaView = (TextView) findViewById(R.id.distancia);
        distanciaView.setText(String.valueOf(distance(latLng, latLng2))+" metros");
        getRoute(latLng, latLng2);
        Log.i("Distancia: ", String.valueOf(distance(latLng, latLng2)));
        if(latLng != null){
            Log.i("Latitudee: ",String.valueOf(latLng.latitude));
            Log.i("Longitudee: ",String.valueOf(latLng.longitude));
        }

        empresaAdapter = new ListaEmpresaAdapter(this, listaEmpresas);
        lista.setAdapter(empresaAdapter);

        EditText campoPesquisa = (EditText) findViewById(R.id.campoPesquisa);
        campoPesquisa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                empresaAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public static double distance(LatLng StartP, LatLng EndP) {
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2-lat1);
        double dLon = Math.toRadians(lon2-lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return 6366000 * c;
    }

    public void getRouteByGMAV2(LatLng origem, LatLng destino) throws UnsupportedEncodingException {
        getRoute(origem, destino);
    }

    public void getRoute(final LatLng origin, final LatLng destination){
        new Thread(){
            public void run(){
						/*String url= "http://maps.googleapis.com/maps/api/directions/json?origin="
								+ origin+"&destination="
								+ destination+"&sensor=false";*/
                String url= "http://maps.googleapis.com/maps/api/directions/json?origin="
                        + origin.latitude+","+origin.longitude+"&destination="
                        + destination.latitude+","+destination.longitude+"&sensor=false";


                HttpResponse response;
                HttpGet request;
                AndroidHttpClient client = AndroidHttpClient.newInstance("route");

                request = new HttpGet(url);
                try {
                    response = client.execute(request);
                    final String answer = EntityUtils.toString(response.getEntity());

                    runOnUiThread(new Runnable(){
                        public void run(){
                            try {
                                //Log.i("Script", answer);
                                list = buildJSONRoute(answer);
                                drawRoute();
                            }
                            catch(JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }
                catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    // PARSER JSON
    public List<LatLng> buildJSONRoute(String json) throws JSONException{
        JSONObject result = new JSONObject(json);
        JSONArray routes = result.getJSONArray("routes");

        distance = routes.getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONObject("distance").getInt("value");

        JSONArray steps = routes.getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONArray("steps");
        List<LatLng> lines = new ArrayList<LatLng>();

        for(int i=0; i < steps.length(); i++) {
            Log.i("Script", "STEP: LAT: "+steps.getJSONObject(i).getJSONObject("start_location").getDouble("lat")+" | LNG: "+steps.getJSONObject(i).getJSONObject("start_location").getDouble("lng"));


            String polyline = steps.getJSONObject(i).getJSONObject("polyline").getString("points");

            for(LatLng p : decodePolyline(polyline)) {
                lines.add(p);
            }

            Log.i("Script", "STEP: LAT: "+steps.getJSONObject(i).getJSONObject("end_location").getDouble("lat")+" | LNG: "+steps.getJSONObject(i).getJSONObject("end_location").getDouble("lng"));
        }

        return(lines);
    }

    // DECODE POLYLINE
    private List<LatLng> decodePolyline(String encoded) {

        List<LatLng> listPoints = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)), (((double) lng / 1E5)));
            Log.i("Script", "POL: LAT: "+p.latitude+" | LNG: "+p.longitude);
            listPoints.add(p);
        }
        return listPoints;
    }

    public void drawRoute(){
        PolylineOptions po;

        if(polyline == null){
            po = new PolylineOptions();

            for(int i = 0, tam = list.size(); i < tam; i++){
                po.add(list.get(i));
            }

            po.color(Color.BLACK).width(4);
            polyline = map.addPolyline(po);
        }
        else{
            polyline.setPoints(list);
        }
    }
}
