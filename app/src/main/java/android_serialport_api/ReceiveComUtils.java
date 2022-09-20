package android_serialport_api;

import java.io.IOException;
import java.io.InputStream;


import android.util.Log;

import com.example.xlb.utils.LogUtils;


/**
 * 接收串口数据
 * @author linyong
 *
 */
public class ReceiveComUtils {
	public ReadThread mReadThread = null;//串口接收线程


	public ReceiveComUtils(SerialPort serialPort,
			IDataDeal dataDeal) {
		LogUtils.Logs_e("------ReceiveCom-----");
		if(mReadThread==null){
			LogUtils.Logs_e("------mReadThread-----");
			InputStream mInStream = serialPort.getInputStream();
			mReadThread = new ReadThread(mInStream, dataDeal);
			mReadThread.start();
		}else{
			LogUtils.Logs_e("-------mReadThread!=null-----");
		}
	}
	public class ReadThread extends Thread {
		private InputStream mInputStream;
		private IDataDeal dataDeal;
		public ReadThread(InputStream mInputStream, IDataDeal dataDeal) {
			this.mInputStream = mInputStream;
			this.dataDeal = dataDeal;
		}
		

		@Override
		public void run() {
			super.run();
			while (!isInterrupted()) {
				int size;
				try {
					byte[] buffer = new byte[51200];
					if (mInputStream == null)
						return;
					size = mInputStream.read(buffer);
					if (size > 0) {
						Log.e("zq", "size=="+size);
						dataDeal.onDataReceived(buffer, size);
					}
				} catch (IOException e) {
					e.printStackTrace();
					mReadThread = null;
				} catch (Exception e) {
					e.printStackTrace();
					mReadThread = null;
				}
			}
		}
	}
}
