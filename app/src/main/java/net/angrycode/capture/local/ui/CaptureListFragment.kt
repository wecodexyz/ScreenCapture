package net.angrycode.capture.local.ui

import android.Manifest
import android.os.Bundle
import net.angrycode.capture.MainActivity
import net.angrycode.capture.R
import net.angrycode.capture.base.BaseAdapter
import net.angrycode.capture.base.BaseViewHolder
import net.angrycode.capture.base.CommonListFragment
import net.angrycode.capture.listLocalVideos
import net.angrycode.capture.local.adapter.CaptureListAdapter
import net.angrycode.capture.local.entity.Video
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

/**
 * Capture video file list in local storage.
 * Created by pc on 2018/1/3.
 */
class CaptureListFragment : CommonListFragment<Video, BaseViewHolder>() {

    override var adapter: BaseAdapter<Video, BaseViewHolder> = CaptureListAdapter()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requestWithPermission()
    }

    @AfterPermissionGranted(REQUEST_CODE)
    private fun requestWithPermission() {
        val perms = Manifest.permission.READ_EXTERNAL_STORAGE
        if (context != null) {
            if (!EasyPermissions.hasPermissions(context!!, perms)) {
                EasyPermissions.requestPermissions(this, getString(R.string.rationale_ask), MainActivity.REQUEST_CODE_PERMISSIONS, perms)
            } else {
                doOnRefresh()
            }
        }
    }

    override fun doOnRefresh() {
        doAsync {
            val localList = context?.listLocalVideos()
            uiThread {
                adapter.setNewData(localList)
            }
        }
    }

    companion object {
        const val REQUEST_CODE = 200
    }

}