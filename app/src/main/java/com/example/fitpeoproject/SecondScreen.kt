package com.example.fitpeoproject

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.DialogInterface.OnMultiChoiceClickListener
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.fitpeoproject.databinding.FragmentSecondScreenBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class SecondScreen : Fragment() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private lateinit var locationText: TextView

    companion object {
        private val REQUEST_PERMISSION_REQUEST_CODE = 2020
    }

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentSecondScreenBinding.inflate(inflater, container, false)
        val bottomNav =
            activity?.findViewById<BottomNavigationView>(com.example.fitpeoproject.R.id.bottomNavigationView)
        bottomNav?.visibility = View.GONE

        val daylist = arrayListOf<Int>()
        val dayArray = arrayOf(
            "Monday",
            "Tuesday",
            "Wednesday",
            "Thursday",
            "Friday",
            "Saturday",
            "Sunday"
        )
        val selectedDay = BooleanArray(dayArray.size)
        binding.dropDown.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Select Day")
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
            builder.setMultiChoiceItems(dayArray, selectedDay,
                OnMultiChoiceClickListener { dialog, which, isChecked ->
                    if (isChecked) {
                        daylist.add(which)
                        Collections.sort(daylist)
                    } else daylist.remove(which)
                    // check or uncheck other items? how?
                })
            builder.setPositiveButton("Ok",
                DialogInterface.OnClickListener { dialog, id ->

                    val sb = StringBuilder()
                    for (i in 0..daylist.size - 1) {
                        sb.append(dayArray[daylist.get(i)])

                        if (i != daylist.size - 1) {
                            sb.append(", ")
                        }
                    }

                    binding.dropDown.setText(sb.toString())
                })
            builder.setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialog, id ->
                    dialog.dismiss()
                })

            builder.setNeutralButton("Clear",
                DialogInterface.OnClickListener { dialog, id ->

                    for(i in 0..selectedDay.size-1){
                        selectedDay[i] = false
                        daylist.clear()
                        binding.dropDown.setText("")
                    }
                })

            builder.show()





        }






        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        locationText =
            view.findViewById(com.example.fitpeoproject.R.id.current_location) as TextView



        locationText.setOnClickListener {
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PERMISSION_GRANTED -> {
                    getLocation()
                }

                else -> {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                        REQUEST_PERMISSION_REQUEST_CODE
                    )
                }

            }
        }

        val mapText = view.findViewById<TextView>(com.example.fitpeoproject.R.id.map_text)

        mapText.setOnClickListener {
            val uri = Uri.parse("geo:" + latitude.toString() + "," + longitude.toString())
            val mapIntent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(mapIntent)
        }


    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener {
            latitude = it.latitude
            longitude = it.longitude


            val geoCoder = Geocoder(requireContext(), Locale.getDefault())
            val address = geoCoder.getFromLocation(latitude, longitude, 1)
            locationText.text = address[0].locality
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation()
                } else {
                    Log.e("Access denied", "Location permission not granted")
                }
            }
        }
    }


}