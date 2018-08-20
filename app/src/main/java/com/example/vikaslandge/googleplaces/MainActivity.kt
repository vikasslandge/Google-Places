package com.example.vikaslandge.googleplaces

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.Toast
  import com.example.vikaslandge.googleplaces.beans.PlacesAPIBeans
import com.example.vikaslandge.googleplaces.beans.Result
import com.google.android.gms.location.places.ui.PlacePicker
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.indiview.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


var list_new:List<Result>? = null

class MainActivity : AppCompatActivity() {

    var lati: Double? = null
    var longi: Double? = null

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var lmanager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        lmanager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                1000,
                100.toFloat(),
                object : LocationListener {
                    override fun onLocationChanged(p0: Location?) {
                        lati = p0!!.latitude
                        longi = p0!!.longitude
                        tv_latitude.text = p0!!.latitude.toString()
                        tv_longitude.text = p0!!.longitude.toString()
                        lmanager.removeUpdates(this)
                    }

                    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
                    }

                    override fun onProviderEnabled(p0: String?) {
                    }

                    override fun onProviderDisabled(p0: String?) {
                    }

                }
        )

        picker.setOnClickListener {
            val builder = PlacePicker.IntentBuilder()
            startActivityForResult(builder.build(this@MainActivity), 1)

        }

        getplaces.setOnClickListener {
            var r = Retrofit.Builder().baseUrl("https://maps.googleapis.com/")
                    .addConverterFactory(GsonConverterFactory.create()).build()
            var api = r.create(PlacesAPI::class.java)
            var call = api.getPlacesAPIBeans("$lati,$longi", sp1.selectedItem.toString())
            call.enqueue(object : Callback<PlacesAPIBeans> {
                override fun onFailure(call: Call<PlacesAPIBeans>?, t: Throwable?) {
                    Toast.makeText(this@MainActivity, "..........", Toast.LENGTH_LONG).show()

                }

                override fun onResponse(call: Call<PlacesAPIBeans>?, response: Response<PlacesAPIBeans>?) {
                    var bean = response!!.body()
                    var list = bean!!.results

                    showall.setOnClickListener {
                        var i = Intent(this@MainActivity,
                                MapsActivity::class.java)
                        i.putExtra("from_showall", true)
                        list_new = list
                        startActivity(i)
                    }
                    /*var temp_list = mutableListOf<String>()
                    for(item in list!!){
                        temp_list.add(item.name+"\n"+
                                item.vicinity)
                    }
                    var adapter = ArrayAdapter<String>(this@MainActivity,
                            android.R.layout.select_dialog_item,
                            temp_list)*/
                    lview.adapter = object : BaseAdapter() {
                        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
                            var inflater = LayoutInflater.from(this@MainActivity)
                            var view = inflater.inflate(R.layout.indiview, null)
                            view.name.text = list.get(p0).name
                            view.address.text = list.get(p0).vicinity


                            view.b1.setOnClickListener {

                                var i = Intent(this@MainActivity,
                                        MapsActivity::class.java)
                                i.putExtra("latitude", list.get(p0).geometry.location.lat)
                                i.putExtra("longitude", list.get(p0).geometry.location.lng)
                                i.putExtra("name", list.get(p0).name)
                                i.putExtra("from_showall", false)
                                startActivity(i)

                            }

                            return view

                        }

                        override fun getItem(p0: Int): Any {
                            return 0

                        }

                        override fun getItemId(p0: Int): Long {
                            return 0
                        }

                        override fun getCount(): Int {
                            return list.size
                        }

                    }
                }


            })
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val place = PlacePicker.getPlace(data!!, this)
        lati = place.latLng.latitude
        longi = place.latLng.longitude
        tv_latitude.text = lati.toString()
        tv_longitude.text = longi.toString()
    }



}

