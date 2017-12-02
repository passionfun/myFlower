package bocai.com.yanghuaji.model;

import java.util.List;

/**
 * 作者 yuanfei on 2017/12/2.
 * 邮箱 yuanfei221@126.com
 */

public class IntelligentControlModel {


    /**
     * CMD : Auto
     * Pid : 101
     * UUID : 1504608600
     * Para : [{"TempL":15,"LedOn":36000,"FlushDUR":1800,"ECL":300,"Begin":1503553439,"ECH":2000,"FlushINVL":3600,"End":1503563439,"TempH":40},{"TempL":15,"LedOn":36000,"FlushDUR":1800,"ECL":300,"Begin":1503553439,"ECH":2000,"FlushINVL":3600,"End":1503563439,"TempH":40},{"TempL":15,"LedOn":36000,"FlushDUR":1800,"ECL":300,"Begin":1503553439,"ECH":2000,"FlushINVL":3600,"End":1503563439,"TempH":40},{"TempL":15,"LedOn":36000,"FlushDUR":1800,"ECL":300,"Begin":1503553439,"ECH":2000,"FlushINVL":3600,"End":1503563439,"TempH":40}]
     */

    /**
     * 根据选择的种植品种，向设备发送智能控制参数
     * 发送数据：Auto
     */
    private String CMD;//Auto 智能控制
    private int Pid;//种植品种配置 id
    private int UUID;//唯一标识符(时间戳)
    private List<ParaBean> Para;//植物不同生长周期参数数组

    public IntelligentControlModel() {
    }

    public IntelligentControlModel(String CMD, int pid, int UUID, List<ParaBean> para) {
        this.CMD = CMD;
        Pid = pid;
        this.UUID = UUID;
        Para = para;
    }

    public String getCMD() {
        return CMD;
    }

    public void setCMD(String CMD) {
        this.CMD = CMD;
    }

    public int getPid() {
        return Pid;
    }

    public void setPid(int Pid) {
        this.Pid = Pid;
    }

    public int getUUID() {
        return UUID;
    }

    public void setUUID(int UUID) {
        this.UUID = UUID;
    }

    public List<ParaBean> getPara() {
        return Para;
    }

    public void setPara(List<ParaBean> Para) {
        this.Para = Para;
    }

    public static class ParaBean {
        /**
         * TempL : 15 //温度的最低限度
         * LedOn : 36000 //开灯时长（单位秒）（为一个时间段。比如持续工作 2小时，则传值为：2*60*60 = 7200）
         * FlushDUR : 1800 //水泵每次开启持续工作时间（为一个时间段。比如持续工作 2 小时，则传值为：2*60*60 = 7200）
         * ECL : 300 //EC 值的最低限度
         * Begin : 1503553439 //本周期开始时间（时间戳）
         * ECH : 2000 //EC 值的最高限度
         * FlushINVL : 3600 //水泵开启周期（为一个时间段。比如持续工作 2 小时，则传值为：2*60*60 = 7200）
         * End : 1503563439 //本周期结束时间（时间戳）
         * TempH : 40 //温度的最高限度
         */

        private int TempL;
        private int LedOn;
        private int FlushDUR;
        private int ECL;
        private int Begin;
        private int ECH;
        private int FlushINVL;
        private int End;
        private int TempH;

        public ParaBean() {
        }

        public ParaBean(int tempL, int ledOn, int flushDUR, int ECL, int begin, int ECH, int flushINVL, int end, int tempH) {
            TempL = tempL;
            LedOn = ledOn;
            FlushDUR = flushDUR;
            this.ECL = ECL;
            Begin = begin;
            this.ECH = ECH;
            FlushINVL = flushINVL;
            End = end;
            TempH = tempH;
        }

        public int getTempL() {
            return TempL;
        }

        public void setTempL(int TempL) {
            this.TempL = TempL;
        }

        public int getLedOn() {
            return LedOn;
        }

        public void setLedOn(int LedOn) {
            this.LedOn = LedOn;
        }

        public int getFlushDUR() {
            return FlushDUR;
        }

        public void setFlushDUR(int FlushDUR) {
            this.FlushDUR = FlushDUR;
        }

        public int getECL() {
            return ECL;
        }

        public void setECL(int ECL) {
            this.ECL = ECL;
        }

        public int getBegin() {
            return Begin;
        }

        public void setBegin(int Begin) {
            this.Begin = Begin;
        }

        public int getECH() {
            return ECH;
        }

        public void setECH(int ECH) {
            this.ECH = ECH;
        }

        public int getFlushINVL() {
            return FlushINVL;
        }

        public void setFlushINVL(int FlushINVL) {
            this.FlushINVL = FlushINVL;
        }

        public int getEnd() {
            return End;
        }

        public void setEnd(int End) {
            this.End = End;
        }

        public int getTempH() {
            return TempH;
        }

        public void setTempH(int TempH) {
            this.TempH = TempH;
        }
    }
}
