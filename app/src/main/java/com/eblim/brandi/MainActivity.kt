package com.eblim.brandi

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import coil.api.load
import com.eblim.brandi.common.Config
import com.eblim.brandi.common.RequestListener
import com.eblim.brandi.model.ImageInfo
import com.eblim.brandi.network.KakaoSearch
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : BaseActivity() {

    private lateinit var mEditText: EditText
    private lateinit var mGridView: GridView
    private lateinit var mTxtNoData: TextView

    private lateinit var mFabTop: FloatingActionButton

    private var mGridItemList: ArrayList<ImageInfo> = ArrayList()
    private lateinit var mAdapter: GridAdapter

    private var mPage: Int = 1
    private var mUserScrolled: Boolean = false
    private var backKeyPressedTime: Long = 0

    private lateinit var mRequestListener: RequestListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mEditText = findViewById(R.id.edit_img)
        mGridView = findViewById(R.id.grid_view)
        mTxtNoData = findViewById(R.id.no_data)
        mFabTop = findViewById(R.id.fab)

        mTxtNoData.text = resources.getText(R.string.img_search)
        mTxtNoData.visibility = View.VISIBLE
        mGridView.visibility = View.GONE

        mEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
                val str: String = mEditText.text.toString()
                Handler().postDelayed(Runnable {
                    if (mEditText.editableText.toString() != "" && str == mEditText.editableText.toString()) {
                        search(str, true)
                        mUserScrolled = false
                    }
                }, 1000)
            }
        })

        mFabTop.setOnClickListener {
            mGridView.setSelection(0)
        }

        mGridView.onItemClickListener = OnItemClickListener { parent, v, position, id ->
            var intent = Intent(this, ImageActivity::class.java)
            intent.putExtra(Config.IMAGE_URL, mGridItemList[position].image_url)
            intent.putExtra(Config.SITE_NAME, mGridItemList[position].display_sitename)
            intent.putExtra(Config.DATE_TIME, mGridItemList[position].dateTime)
            intent.putExtra(Config.DOC_URL, mGridItemList[position].doc_url)

            startActivity(intent)
        }

        mGridView.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScroll(view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                if (totalItemCount != 0) {
                    if (mUserScrolled && (firstVisibleItem + visibleItemCount >= totalItemCount)) {
                        if (mGridItemList != null) {
                            mUserScrolled = false
                            search(mEditText.text.toString(), false)
                        }
                    }
                }
            }

            override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    mUserScrolled = true
                }
            }
        })

        mRequestListener = object : RequestListener {
            override fun onRequestSuccess(requestCode: Int, result: Any?, isEnd: Boolean) {
                if (!isEnd) {
                    mPage += 1
                } else {
                    Toast.makeText(applicationContext, resources.getText(R.string.no_data), Toast.LENGTH_LONG).show()
                    return
                }

                mGridItemList.addAll(result as ArrayList<ImageInfo>)

                if (mGridItemList.size == 0) {
                    mGridView.visibility = View.GONE
                    mTxtNoData.visibility = View.VISIBLE
                    mTxtNoData.text = resources.getText(R.string.no_data)
                } else {
                    mGridView.visibility = View.VISIBLE
                    mTxtNoData.visibility = View.GONE
                }

                hideSoftKeyboard(mEditText)
                mAdapter.notifyDataSetChanged()
            }

            override fun onRequestFailure(t: Throwable?) {
                mGridView.visibility = View.GONE
                mTxtNoData.visibility = View.VISIBLE
                mTxtNoData.text = resources.getText(R.string.no_data)
            }
        }

        mAdapter = GridAdapter(applicationContext, R.layout.item_img_list, mGridItemList)
        mGridView.adapter = mAdapter
        mAdapter.notifyDataSetChanged()
    }

    fun search(searchWord : String, isFirstYn : Boolean) {

        if (isFirstYn) {
            mGridItemList.clear()
            mGridView.setSelection(0)
            mPage = 1
        }

        KakaoSearch().getSearchList(searchWord, mPage, 30, mRequestListener)
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis()
            Toast.makeText(this, resources.getText(R.string.app_exit), Toast.LENGTH_SHORT).show()
            return
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish()
        }
    }

    private fun hideSoftKeyboard(view: View) {
        val mgr = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        mgr.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private class GridAdapter(context: Context?, textViewResourceId: Int, items: ArrayList<ImageInfo>) :
        ArrayAdapter<ImageInfo?>(context!!, textViewResourceId, items as List<ImageInfo?>) {
        private val items: ArrayList<ImageInfo> = items

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var convertView = convertView
            val holder: ViewHolder

            if (convertView == null) {
                holder = ViewHolder()
                val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                convertView = inflater.inflate(R.layout.item_img_list, null)
                holder.searchImg = convertView!!.findViewById<View>(R.id.img) as ImageView
                convertView.tag = holder
            } else {
                holder = convertView.tag as ViewHolder
            }

            val imageInfo = items[position]
            holder.searchImg!!.load(imageInfo.image_url)

            return convertView!!
        }

        private inner class ViewHolder {
            var searchImg: ImageView? = null
        }
    }
}