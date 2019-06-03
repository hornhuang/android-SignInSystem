package com.example.sht.homework.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener

import com.example.sht.homework.R
import com.example.sht.homework.baseclasses.Artical
import com.example.sht.homework.forums.activities.ForumEditActivity
import com.example.sht.homework.forums.adapters.ArticalAdapter
import com.example.sht.homework.utils.MyToast
import com.example.sht.homework.utils.bmobmanager.pictures.SuperImagesLoader
import java.util.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ForumFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    private var toolbar: Toolbar? = null
    private var mWriteButton: FloatingActionButton? = null

    private var articalList: MutableList<Artical>? = null
    private var adapter: ArticalAdapter? = null
    private var recyclerView: RecyclerView? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_forum, container, false)

        iniViews(view)
        iniRecycler()
        iniSwipeReflesh()
        inifloatButton()
        return view
    }

    private fun iniViews(view: View) {
        recyclerView = view.findViewById(R.id.artical_recycler)
        swipeRefreshLayout = view.findViewById(R.id.artical_swipe_layout)
        mWriteButton = view.findViewById(R.id.write)
        toolbar = view.findViewById(R.id.toolbar)

    }

    private fun iniRecycler() {
        val manager = LinearLayoutManager(activity)
        recyclerView!!.layoutManager = manager
        articalList = ArrayList()
        adapter = ArticalAdapter(getData())
        recyclerView!!.adapter = adapter
    }

    private fun iniSwipeReflesh() {
        swipeRefreshLayout!!.setProgressViewOffset(false, 200, 400)
        swipeRefreshLayout!!.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            swipeRefreshLayout!!.isRefreshing = true
            getData()
        })
    }

    private fun inifloatButton() {
        mWriteButton!!.setOnClickListener(View.OnClickListener { ForumEditActivity.actionStart(activity as AppCompatActivity?) })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
//                finish()
                true
            }
            else -> false
        }
    }

    // 从 Bmob 获得所有用户信息
    fun getData(): MutableList<Artical>? {
        val query = BmobQuery<Artical>()
        query.setLimit(8).setSkip(0).order("-createdAt")
                .findObjects(object : FindListener<Artical>() {
                    override fun done(`object`: List<Artical>, e: BmobException?) {
                        if (e == null) {
                            articalList!!.clear()
                            articalList!!.addAll(`object`)
                            SuperImagesLoader(adapter, articalList, swipeRefreshLayout).articalLoad()
                        } else {
                            MyToast.makeToast(activity as AppCompatActivity?, "失败，请检查网络" + e.message)
                        }
                    }
                })
        return articalList
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }
}
