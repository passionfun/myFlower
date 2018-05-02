package bocai.com.yanghuajien.model;

import java.util.List;

/**
 * 作者 yuanfei on 2017/12/5.
 * 邮箱 yuanfei221@126.com
 */

public class AutoParaModel {


    /**
     * CMD : Auto
     * Pid : 101
     * UUID : 1504608600
     * Para : [{"TempL":15,"LedOn":36000,"FlushDUR":1800,"ECL":300,"Begin":1503553439,"ECH":2000,"FlushINVL":3600,"End":1503563439,"TempH":40},
     * {"TempL":15,"LedOn":36000,"FlushDUR":1800,"ECL":300,"Begin":1503553439,"ECH":2000,"FlushINVL":3600,"End":1503563439,"TempH":40},
     * {"TempL":15,"LedOn":36000,"FlushDUR":1800,"ECL":300,"Begin":1503553439,"ECH":2000,"FlushINVL":3600,"End":1503563439,"TempH":40},
     * {"TempL":15,"LedOn":36000,"FlushDUR":1800,"ECL":300,"Begin":1503553439,"ECH":2000,"FlushINVL":3600,"End":1503563439,"TempH":40}]
     */

    private String CMD;
    private int Pid;
    private int UUID;
    private List<AutoModel.ParaBean> Para ;

    public AutoParaModel() {
    }

    public AutoParaModel(String CMD, int pid, int UUID, List<AutoModel.ParaBean> data) {
        this.CMD = CMD;
        Pid = pid;
        this.UUID = UUID;
        Para = data;
    }

    public List<AutoModel.ParaBean> getPara() {
        return Para;
    }

    public void setPara(List<AutoModel.ParaBean> para) {
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

    public void setPid(int pid) {
        Pid = pid;
    }

    public int getUUID() {
        return UUID;
    }

    public void setUUID(int UUID) {
        this.UUID = UUID;
    }

    public static class ParaBean {
        /**
         * TempL : 15
         * LedOn : 36000
         * FlushDUR : 1800
         * ECL : 300
         * Begin : 1503553439
         * ECH : 2000
         * FlushINVL : 3600
         * End : 1503563439
         * TempH : 40
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
