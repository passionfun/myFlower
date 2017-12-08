package bocai.com.yanghuaji.ui.plantingDiary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Activity;
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

    private PoiSearch.Query query;
    private PoiSearch poiSearch;

    //显示的入口
    public static void show(Context context) {
        context.startActivity(new Intent(context, LocationActivity.class));
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
    protected void initWidget() {
        super.initWidget();

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

                    query = new PoiSearch.Query(mKeyWork, "", "");
                    //keyWord表示搜索字符串，
                    //第二个参数表示POI搜索类型，二者选填其一，选用POI搜索类型时建议填写类型代码，码表可以参考下方（而非文字）
                    // cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
                    query.setPageSize(10);// 设置每页最多返回多少条poiitem
                    query.setPageNum(1);//设置查询页码
                    poiSearch = new PoiSearch(LocationActivity.this, query);
                    poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
                        @Override
                        public void onPoiSearched(PoiResult poiResult, int i) {
                            Log.i("gao", poiResult.toString());
                        }

                        @Override
                        public void onPoiItemSearched(PoiItem poiItem, int i) {
                            Log.i("gao", poiItem.toString());
                        }
                    });
                    poiSearch.searchPOIAsyn();
                }
                return false;
            }
        });
    }
}
