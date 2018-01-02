package bocai.com.yanghuaji.ui.plantingDiary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import org.greenrobot.eventbus.EventBus;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Activity;
import bocai.com.yanghuaji.base.RecyclerAdapter;
import bocai.com.yanghuaji.model.MessageEvent;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者 yuanfei on 2017/11/13.
 * 邮箱 yuanfei221@126.com
 */

public class LocationActivity extends Activity {
    @BindView(R.id.img_back)
    ImageView mImgBack;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.tv_not_show_addr)
    TextView tvNotShowAddr;

    private PoiSearch.Query query;
    private PoiSearch poiSearch;
    private String mCityName;
    private static final String CITY_NAME = "cityName";
    private RecyclerAdapter<PoiItem> mAdapter;
    public static final String LOCATION = "LOCATION";

    //显示的入口
    public static void show(Context context, String cityName) {
        Intent intent = new Intent(context, LocationActivity.class);
        intent.putExtra(CITY_NAME, cityName);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_location;
    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        mCityName = getIntent().getStringExtra(CITY_NAME);
        return super.initArgs(bundle);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(mAdapter = new RecyclerAdapter<PoiItem>() {
            @Override
            protected int getItemViewType(int position, PoiItem poiItem) {
                return R.layout.item_addr_result;
            }

            @Override
            protected ViewHolder<PoiItem> onCreateViewHolder(View root, int viewType) {
                return new LocationActivity.ViewHolder(root);
            }
        });
        tvNotShowAddr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectPosition = -1;
                mAdapter.notifyDataSetChanged();
                tvNotShowAddr.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.mipmap.img_check, 0);
            }
        });
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                String poiName = "";
                if (mSelectPosition != -1) {
                    poiName = mAdapter.getItems().get(mSelectPosition).getTitle();
                }
                EventBus.getDefault().post(new MessageEvent(poiName,LOCATION));
            }
        });

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //如果actionId是搜索的id，则进行下一步的操作
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                    }
                    String mKeyWork = etSearch.getText().toString();

                    query = new PoiSearch.Query(mKeyWork, "", mCityName);
                    //keyWord表示搜索字符串，
                    //第二个参数表示POI搜索类型，二者选填其一，选用POI搜索类型时建议填写类型代码，码表可以参考下方（而非文字）
                    // cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
                    query.setPageSize(10);// 设置每页最多返回多少条poiitem
                    query.setPageNum(1);//设置查询页码
                    poiSearch = new PoiSearch(LocationActivity.this, query);
                    poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
                        @Override
                        public void onPoiSearched(PoiResult poiResult, int i) {
                            mAdapter.clear();
                            if (poiResult != null && poiResult.getPois().size() != 0) {
                                mAdapter.add(poiResult.getPois());
                            } else {
                                tvNotShowAddr.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.mipmap.img_check, 0);
                                mSelectPosition = -1;
                            }
                        }

                        @Override
                        public void onPoiItemSearched(PoiItem poiItem, int i) {
                        }
                    });
                    poiSearch.searchPOIAsyn();
                }
                return false;
            }
        });
    }

    private int mSelectPosition = -1;

    class ViewHolder extends RecyclerAdapter.ViewHolder<PoiItem> {
        @BindView(R.id.item_tv_poi)
        TextView itemTvPoi;
        @BindView(R.id.item_tv_addr)
        TextView itemTvAddr;
        @BindView(R.id.item_iv_check)
        ImageView itemIvCheck;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(PoiItem poiItem) {
            itemTvPoi.setText(poiItem.getTitle());
            itemTvAddr.setText(poiItem.getProvinceName() + poiItem.getCityName() +
                    poiItem.getAdName() + poiItem.getSnippet());
            if (mSelectPosition == getAdapterPosition()) {
                itemIvCheck.setVisibility(View.VISIBLE);
            } else {
                itemIvCheck.setVisibility(View.GONE);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tvNotShowAddr.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
                    mSelectPosition = getAdapterPosition();
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
    }
}