package cn.com.woong.readhub.ui.topic

import android.content.Context
import android.text.TextUtils
import cn.com.woong.readhub.bean.TopicMo
import com.ajguan.library.EasyRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import cn.com.woong.readhub.R
import cn.com.woong.readhub.base.BaseFragment
import com.blankj.utilcode.util.LogUtils
import kotlinx.android.synthetic.main.fragment_topic.*


class TopicFragment : BaseFragment<TopicPresenter>(), TopicContract.View {
    private var mTopicAdapter: TopicAdapter? = null
    private lateinit var mLastOrder: String

    override fun getLayoutId(): Int {
        return R.layout.fragment_topic
    }

    override fun initView(view: View?) {

    }

    override fun initData() {
        mPresenter = TopicPresenter(this)

        mTopicAdapter = TopicAdapter(activity as Context)
        topic_recycler_view.layoutManager = LinearLayoutManager(activity)
        topic_recycler_view.adapter = mTopicAdapter

        mLastOrder = ""
        LogUtils.i("TopicFragment initData mPresenter === $mPresenter")

        mPresenter?.getTopicNews(mLastOrder)

        topic_refresh_layout.addEasyEvent(object : EasyRefreshLayout.EasyEvent {
            override fun onLoadMore() {
                if (mPresenter != null) {
                    mPresenter.getTopicNews(mLastOrder)
                }
            }

            override fun onRefreshing() {
                if (mPresenter != null) {
                    mLastOrder = ""
                    mPresenter.getTopicNews(mLastOrder)
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        topic_refresh_layout.loadMoreComplete()
    }

    fun refreshTopicData() {
        mLastOrder = ""
        topic_recycler_view.scrollToPosition(0)
        mPresenter?.getTopicNews(mLastOrder)
    }

    override fun updateTopicData(order: String, topicMos: ArrayList<TopicMo>) {
        topic_refresh_layout.refreshComplete()
        topic_refresh_layout.loadMoreComplete()

        mTopicAdapter?.updateTopics(TextUtils.isEmpty(order), topicMos)
        mLastOrder = "${topicMos[topicMos?.size - 1].order}"
    }
}