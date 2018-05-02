package bocai.com.yanghuajien.ui.intelligentPlanting;


import xpod.longtooth.LongToothAttachment;

public class SampleAttachment implements LongToothAttachment {

    private boolean ckHex = false;
	@Override
	public Object handleAttachment(Object... arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	
	public void setCkHex(boolean ckHex){
		this.ckHex = ckHex;
	}
	
	public boolean getCkHex(){
		return this.ckHex;
	}

}
