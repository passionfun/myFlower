package bocai.com.yanghuaji.ui.intelligentPlanting;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Fragment;
import bocai.com.yanghuaji.ui.intelligentPlanting.recyclerHelper.CardScaleHelper;
import bocai.com.yanghuaji.ui.intelligentPlanting.recyclerHelper.HorizntalAdapter;
import butterknife.BindView;

/**
 * 作者 yuanfei on 2017/11/15.
 * 邮箱 yuanfei221@126.com
 */

public class HorizontalRecyclerFragment extends Fragment {
    private HorizntalAdapter mAdapter;

    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;

    @BindView(R.id.tv_current)
    TextView mCurrentNum;

    @BindView(R.id.tv_total)
    TextView mTotalNum;

    private List<Integer> mList = new ArrayList<>();
    private CardScaleHelper mCardScaleHelper = null;
    private int mLastPos = -1;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_horizontal_recycler;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        for (int i=0;i<10;i++){
            mList.add(i);
        }
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new HorizntalAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);
        mCardScaleHelper = new CardScaleHelper();
        mCardScaleHelper.setCurrentItemPos(2);
        mCardScaleHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    protected void initData() {
        super.initData();
        mTotalNum.setText(String.valueOf(mAdapter.getItemCount()));
        mAdapter.setOnCurrentPositionListener(new HorizntalAdapter.CurrentPositionListener() {
            @Override
            public void currentPosition() {
                mCurrentNum.setText(String.valueOf(mAdapter.getPosition()+1));
            }
        });
    }
}
