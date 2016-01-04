package org.aisen.weibo.sina.ui.fragment.timeline;

import android.text.TextUtils;

import org.aisen.android.network.http.Params;
import org.aisen.android.network.task.TaskException;
import org.aisen.android.support.adapter.ABaseAdapter;
import org.aisen.android.ui.fragment.AListSwipeRefreshFragment;
import org.aisen.android.ui.fragment.APagingFragment;
import org.aisen.weibo.sina.base.AppSettings;
import org.aisen.weibo.sina.sinasdk.bean.StatusContent;
import org.aisen.weibo.sina.sinasdk.bean.StatusContents;

import java.util.List;

/**
 * 微博列表基类
 *
 * Created by wangdan on 16/1/2.
 */
public abstract class ATimelineFragment extends AListSwipeRefreshFragment<StatusContent, StatusContents> {

    @Override
    protected ABaseAdapter.AItemView<StatusContent> newItemView() {
        return new TimelineItemView();
    }

    abstract class ATimelineTask extends PagingTask<Void, Void, StatusContents> {

        public ATimelineTask(RefreshMode mode) {
            super(mode);
        }

        @Override
        protected List<StatusContent> parseResult(StatusContents statusContents) {
            return statusContents.getStatuses();
        }

        @Override
        protected StatusContents workInBackground(RefreshMode mode, String previousPage, String nextPage, Void... p) throws TaskException {
            Params params = new Params();

            if (mode == APagingFragment.RefreshMode.refresh && !TextUtils.isEmpty(previousPage))
                params.addParameter("since_id", previousPage);

            if (mode == APagingFragment.RefreshMode.update && !TextUtils.isEmpty(nextPage))
                params.addParameter("max_id", nextPage);

            params.addParameter("count", String.valueOf(AppSettings.getTimelineCount()));

            return getStatusContents(params);
        }

        abstract StatusContents getStatusContents(Params params) throws TaskException;

    }

}
