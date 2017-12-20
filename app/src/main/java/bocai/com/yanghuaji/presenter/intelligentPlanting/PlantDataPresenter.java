package bocai.com.yanghuaji.presenter.intelligentPlanting;

import bocai.com.yanghuaji.base.presenter.BasePresenter;

/**
 * Created by shc on 2017/12/20.
 */

public class PlantDataPresenter extends BasePresenter<PlantingDataContract.View>
        implements PlantingDataContract.Presenter {
    public PlantDataPresenter(PlantingDataContract.View view) {
        super(view);
    }

    @Override
    public void setData(String token, String mac, String temperature, String waterLevel, String isLightOn, String Ec) {

    }

    @Override
    public void setUpdateStatus(String mac, String status) {

    }
}
