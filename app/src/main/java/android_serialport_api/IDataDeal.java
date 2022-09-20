package android_serialport_api;

public interface IDataDeal {
	//接收串口数据
	public void onDataReceived(byte[] buffer, int size);
	

}
