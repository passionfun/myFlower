package bocai.com.yanghuajien.util;

/**
 * Created by zhanghuanhuan on 2018/4/4.
 */

public class ConstUtil {
    public static final String HOST_NAME = "sunparl.longtooth.io";// wifi  118.178.233.149  sunparl.longtooth.io  sunparl.longtooth.io  cmcc.longtooth.io 114.215.170.184
    public static final int PORT = 53180;//wifi信息中默认的53199  53180
    public static final int APPID = 2;
    public static final int DEVID = 2001110001;//2001110001  me:2000110293
    //30820126300D06092A864886F70D010101050003820113003082010E028201023030413333384530414633383545344245434146394336453233453742393244463543353245323830384643383238443244433035453937433946454533444146373042373533363646434543434637384644344545373336323443374541423844304331324143314434303045443339413432364446463030364237394230423845464330374132353646334638323730444433374544363634393132314235323530373645384335303835464138413931364144314435363443423034303837313535333135413031323141303534363941364431323239353842313336313131303635373435433845424532303339383644313037413833374244464533460206303130303031
    public static final String APPKEY = "30820126300D06092A864886F70D010101050003820113003082010E028201023030393946373936384438313342423744304134343338414644373242354544323346324644323239333045334438304238323031314135364645323346463234313146424631463945333733463643374230373546313045354334413834324338424539353846463637323945373738413832354534443941313531453145363441443434463343383234373144333037343446343535384642394630443838424531364532353531453438453532343535324535463033354631353143423645313745463936323637354333374437333845353042363744304243464546413334303732454338354546413243393046443639453943423532324646383138370206303130303031";
    public static final String NSS_SERVICENAME = "n22s";
    public static final String LONGTOOTH_SERVICENAME = "longtooth";

    public static final String CMD_DEVICE_BIND = "BR";
    public static final String CMD_DEVICE_RESETFACTORY = "FactoryReset";
    public static final String CMD_DEVICE_BRIGHTNESS = "LedSet";
    public static final String CMD_GET_WORKMODE = "getOpMode";
    public static final String CMD_DEVICE_INTELLIGENT_CONTROL = "Auto";
    public static final String CMD_DEVICE_NODISTURB = "noDisturb";
    public static final String CMD_LIGHT_ON = "lightOn";
    public static final String CMD_PUMP_SET = "PumpSet";
    public static final String CMD_DEVICE_ISUPDATE = "isUpdate";
    public static final String MDNS_SERVICE_NAME = "_easylink._tcp.local.";
    public static final String CMD_UPDATE_FIMEWARE = "update";
    public static final String CMD_UPDATE_STATE = "checkUpdateStat";
    public static final String CMD_GET_OPMODE = "getOpMode";
    public static final String CMD_FIRMWARE_VERSION = "softVer";


    public static final String ACTION_SEND_FRAME_SUCCESS = "action_send_longtooth_cmd_success";
    public static final String OP_START = "start_longtooth";
    public static final String OP_BIND = "bind_device";
    public static final String OP_BRIGHTNESS = "bright_device";
    public static final String OP_QUERRY = "querry_device";
    public static final String OP_RESET = "reset_device";
    public static final String OP_INTELLIGENT_CONTROL = "intelligent_control";
    public static final String OP_AUTO = "auto_control";
    public static final String OP_NODISTURB = "no_disturb";
    public static final String OP_LIGHTON = "light_on";
    public static final String OP_PUMP_SET = "pump_set";
    public static final String OP_ISUPDATE = "check_update";
    public static final String OP_UPDATE_FIRMWARM = "update_firmware";
    public static final String OP_UPDATE_STATE = "update_state";
    public static final String OP_DEVICE_FIRMWARE_VERSION = "soft_ver";
    public static final String OP_GET_DEVICE_WORK_MODE = "get_op_mode";

    public static final int TIME_OUT = 20000;


    public static final int START_LONGTOOTH_SUCCESS = 0;
    public static final int START_LONGTOOTH_FAIL = 1;
    public static final int ADDSERVICE_SUCCESS = 2;
    public static final int ADDSERVICE_FAIL = 3;

    public static final int BIND_DEVICE_SUCCESS = 4;
    public static final int BIND_DEVICE_FAIL = 5;
    public static final int CONTROL_DEVICE_BRIGHTNESS_SUCCESS = 6;
    public static final int CONTROL_DEVICE_BRIGHTNESS_FAIL = 7;
    public static final int QUERRY_DEVICE_SUCCESS = 8;
    public static final int QUERRY_DEVICE_FAIL = 9;

    public static final int RESET_DEVICE_SUCCESS = 10;
    public static final int RESET_DEVICE_FAIL = 11;

    public static final int INTELLIGENT_CONTROL_SUCCESS = 12;
    public static final int INTELLIGENT_CONTROL_FAIL = 13;

    public static final int SMART_CONTROL_AUTO_SUCCESS = 40;
    public static final int SMART_CONTROL_AUTO_FAIL = 41;

    public static final int NO_DISTURB_SUCCESS = 42;
    public static final int NO_DISTURB_FAIL = 43;

    public static final int LIGHT_ON_SUCCESS = 44;
    public static final int LIGHT_ON_FAIL = 45;



    public static final int PUMP_SET_SUCCESS = 46;
    public static final int PUMP_SET_FAIL = 47;

    public static final int CHECK_UPDATE_SUCCESS = 60;

    public static final int DEVICE_UPDATING = 70;
    public static final int DEVICE_UPDATE_STATE = 80;

    public static final int GET_DEVICE_OP_MODE_SUCCESS = 90;

    public static final int GET_DEVICE_FIRMWARE_VERSION_VERSION = 98;

    public static final int MDNS_HAVE_DEVICES = 100;



    public static final int FIND_DEVICE_SUCCESS = 20;
    public static final int FIND_DEVICE_FAIL = 30;

    public static final String SSID_KEY = "ssid";
    public static final String PASSWORD_KEY = "password";

}
