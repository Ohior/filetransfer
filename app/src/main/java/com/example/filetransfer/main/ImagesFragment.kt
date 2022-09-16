package com.example.filetransfer.main

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.filetransfer.R
import com.example.filetransfer.adapter.ImageAdapter
import com.example.filetransfer.adapter.ImageDataClass
import com.example.filetransfer.tools.Constant
import com.example.filetransfer.tools.Tools
import java.io.File


class ImagesFragment : Fragment() {
    private lateinit var fragmentView: View
    private lateinit var imageAdapter: ImageAdapter
    private lateinit var idRecyclerView: RecyclerView
    private lateinit var imagePathList: ArrayList<String>
    private lateinit var idFabOptions: ImageButton


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        // this method is called after permissions has been granted.
        // we are checking the permission code.
        if (requestCode == Constant.IMAGE_PERMISSION_REQUEST_CODE) {
            // in this case we are checking if the permissions are accepted or not.
            if (grantResults.isNotEmpty()) {
                val storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                if (storageAccepted) {
                    // if the permissions are accepted we are displaying a toast message
                    // and calling a method to get image path.
                    getAllImagePath()
                }
            } else {
                Tools.showToast(requireContext(), "You Need To Grant Permission")
                Tools.requestForPermissions(requireContext(), requireActivity())
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.title = "Select Images"
        imagePathList = ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_images, container, false)
        idRecyclerView = fragmentView.findViewById(R.id.id_rv_image_adapter)
        idFabOptions = fragmentView.findViewById(R.id.id_fab_options)
        imageAdapter = ImageAdapter(requireContext(), idRecyclerView)
        Tools.requestForPermissions(requireContext(), requireActivity())

        getAllImagePath()

        fabButtonClickedListener()
        manageImageAdapter()

        return fragmentView
    }

    private fun fabButtonClickedListener() {
        idFabOptions.setOnClickListener {
            Tools.popUpWindow(requireContext(), fragmentView) {
                val share = it.findViewById<TextView>(R.id.id_iv_share)
                share.setOnClickListener { shareToOtherApps() }
            }
        }
    }

    private fun shareToOtherApps() {
        if (imagePathList.isNotEmpty()) {
            val intent = Intent(Intent.ACTION_SEND_MULTIPLE)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            val uriList: ArrayList<Uri> = ArrayList()
            imagePathList.forEach { path ->
                val file = File(path)
                val uri = FileProvider.getUriForFile(
                    requireContext(),
                    Constant.APPLICATION_AUTHORITY,
                    file
                )
                uriList.add(uri)
                intent.setDataAndType(uri, "image/*")
            }
            intent.putExtra(Intent.EXTRA_STREAM, uriList)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//            startActivity(intent)
            startActivity(Intent.createChooser(intent, "Share Image"))
        } else {
            Tools.showToast(requireContext(), "You do not have any Images To share")
        }

    }


    private fun manageImageAdapter() {
        imageAdapter.setOnItemClickListener(object : ImageAdapter.OnClickListener {
            override fun onItemClick(position: Int, view: View) {
                val llitem = view.findViewById<LinearLayout>(R.id.id_ll_image_item)
                val title = view.findViewById<TextView>(R.id.id_tv_item_title)
                val image = view.findViewById<ImageView>(R.id.id_iv_item_image)
                val filelocation = view.findViewById<TextView>(R.id.id_tv_item_location)
                llitem.setBackgroundColor(Color.GRAY)
                if (!imagePathList.contains(filelocation.text)) {
                    imagePathList.add(filelocation.text.toString())
                }
            }

            override fun onLongItemClick(position: Int, view: View) {
            }
        })
    }

    private fun getAllImagePath() {
        // check id device has a storage card
        val isSDpresent = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
        if (isSDpresent) {
            // if the sd card is present we are creating a new list in
            // which we are getting our images data with their ids.
            val sdcard = arrayOf(MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID)
            val orderbyID = MediaStore.Images.Media._ID

            // this method will stores all the images
            // from the gallery in Cursor
            val cursor = activity?.contentResolver?.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                sdcard,
                null,
                null,
                orderbyID
            )

            // below line is to get total number of images
            val count = cursor?.count ?: 0

            // on below line we are running a loop to add
            // the image file path in our array list.
            for (i in 0 until count) {

                // on below line we are moving our cursor position
                cursor?.moveToPosition(i)

                // on below line we are getting image file path
                val dataColumnIndex = cursor?.getColumnIndex(MediaStore.Images.Media.DATA)

                // after that we are getting the image file path
                // and adding that path in our array list.
                val file = File(dataColumnIndex?.let { cursor.getString(it) }!!)
                val imageDataClass = ImageDataClass(
                    image_url = cursor.getString(dataColumnIndex),
                    title = file.name,
                    file_location = file.path
                )
                imageAdapter.addToAdapter(imageDataClass)
                imageAdapter.notifyItemChanged(i)
            }
            // after adding the data to our
            // array list we are closing our cursor.
            cursor?.close()
        }
    }
}