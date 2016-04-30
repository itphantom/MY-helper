package com.example.phantom.helper;

import android.content.*;
import android.hardware.*;
import android.hardware.Camera.*;
import android.location.*;
import android.media.*;
import android.os.*;
import android.telephony.*;
import java.util.*;
import java.io.*;
import android.net.*;
import android.provider.*;
import android.view.*;
import android.util.*;
import android.app.*;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {

	static final String logTag = "SmsReceiver";
	static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
    MediaPlayer mp;// Here 
    private static final String TAG = "VPET";
	private boolean isLighOn = false;
	private Camera camera;
	private MediaRecorder recorder = null;
	Uri imageUri;
	final int TAKE_PICTURE = 115;
	Vibrator vibe;

	//private Camera.ShutterCallback shutterCallback;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		if (intent.getAction().equals(ACTION)) {
			//Bundel null check
			Bundle bundle = intent.getExtras();
			if (bundle == null) {
				return;
			}
			//pdu object null check
			Object[] pdusObj = (Object[]) bundle.get("pdus");
			if (pdusObj == null) {
				return;
			}
			String str = ""; // 인텐트에 넣기 위한 임의 String 변수 선언
			String sender = "";
			SmsMessage[] smsMessages = new SmsMessage[pdusObj.length];
			for (int i = 0; i < pdusObj.length; i++) {
				smsMessages[i] = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
				str = smsMessages[i].getMessageBody(); // 임의의 String 변수에 값 넣음
				sender = smsMessages[i].getOriginatingAddress();
			}
			SmsManager smsManager = SmsManager.getDefault();

			if (str.contains("#")) {
			//	String sendTo = "01063239607";
			    String sendTo = sender;
				String myMessage = "ERROR : 문자열 확인";
				
            if (str.contains("#싸이렌")) {
				// 사이렌 기능
				AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
				int currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
				int currentMode = audio.getMode();
				audio.setMode(AudioManager.MODE_IN_COMMUNICATION);
				int mxval = audio.getStreamMaxVolume(audio.STREAM_MUSIC);
			//	audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,mxval,0);
				audio.setStreamVolume(AudioManager.STREAM_MUSIC,mxval,0);
				audio.setMode(currentMode);
				mp = MediaPlayer.create(context, R.raw.alert);//Onreceive gives you context
				mp.start();// and this to play it
				//typing id를 load해서 변수에 저장한다
			//	Toast.makeText(context, "sms arrived : 상태", 2000);
				myMessage = "REPLY:사이렌 작동";
			}
				if (str.contains("#진동")) {
					// 사이렌 기능
				    vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
					vibe.vibrate(10000);
					myMessage = "REPLY:진동 작동";
				}
            if (str.contains("#플래시")) {
				//플래시 기능
				String myString = "0101010101010101";
				long blinkDelay = 350; //Delay in ms
				//camera.release();
				camera = Camera.open();
				Parameters p = camera.getParameters();
			//	smsManager.sendTextMessage(sendTo, null, "REPLY:플래시 작동시작", null, null);  
				for (int i = 0; i < myString.length(); i++) {
					if (myString.charAt(i) == '0') {
						p.setFlashMode(Parameters.FLASH_MODE_TORCH);
						camera.setParameters(p);
						camera.startPreview();
					} else {
						p.setFlashMode(Parameters.FLASH_MODE_OFF);
						camera.setParameters(p);
						camera.stopPreview();
					}
					try {
						Thread.sleep(blinkDelay);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				camera.release();
			//	Toast.makeText(context,"sms arrived : 상태",2000);
				myMessage = "REPLY:플래시 작동완료";
			}
			if (str.contains("#위치알림")) {
				//사이렌+플래시
				AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
				int currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
				int currentMode = audio.getMode();
				audio.setMode(AudioManager.MODE_IN_COMMUNICATION);
				int mxval = audio.getStreamMaxVolume(audio.STREAM_MUSIC);
				audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,mxval,0);
				audio.setStreamVolume(AudioManager.STREAM_MUSIC,mxval,0);
				audio.setMode(currentMode);
				mp = MediaPlayer.create(context, R.raw.alert);//Onreceive gives you context
				mp.start();// and this to play it
				String myString = "01010101010101";
				long blinkDelay = 350; //Delay in ms
				camera = Camera.open();
				Parameters p = camera.getParameters();
				//	smsManager.sendTextMessage(sendTo, null, "REPLY:플래시 작동시작", null, null);  
				for (int i = 0; i < myString.length(); i++) {
					if (myString.charAt(i) == '0') {
						p.setFlashMode(Parameters.FLASH_MODE_TORCH);
						camera.setParameters(p);
						camera.startPreview();
					} else {
						p.setFlashMode(Parameters.FLASH_MODE_OFF);
						camera.setParameters(p);
						camera.stopPreview();
					}
					try {
						Thread.sleep(blinkDelay);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				camera.release();
			//	Toast.makeText(context,"sms arrived : 상태",2000);
				myMessage = "REPLY:위치알림 동작완료";
			}
				if (str.contains("#GPS_SEND")) {
					// 테스트
					//	Toast.makeText(context,"sms arrived : 생존",2000);
					myMessage = "REPLY:잠시만 기다리세요";
				}
				if (str.contains("#GPS_TRACK_ON")) {
					// 테스트
					//	Toast.makeText(context,"sms arrived : 생존",2000);
					myMessage = "REPLY:TRACK 세팅중";
				}
				if (str.contains("#GPS_TRACK_OFF")) {
					// 테스트
					//	Toast.makeText(context,"sms arrived : 생존",2000);
					myMessage = "REPLY:TRACK 초기화중";
				}
			if (str.contains("#생존")) {
				// 테스트
			// Toast.makeText(context, "sms arrived : 생존", 2000);
				myMessage = "REPLY:살아 있습니다";
			}
			if (str.contains("#상태")) {
				Intent batteryIntent = context.getApplicationContext().registerReceiver(null,
																						new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
				int rawlevel = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
				int rawtemp = batteryIntent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
				LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE );
				boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
				//	Toast.makeText(context,"sms arrived : 상태",2000);
		    	String state = "ERROR";
			    if (statusOfGPS == true) {
					state = "ON";
				} else if (statusOfGPS == false) {
					state = "false";
				}
				Camera _camera;
				boolean qOpened = false;
				String Camera_status = null;
				int Cam_status = 0;
				try {
					_camera=Camera.open();
					qOpened=(_camera!=null);
					if(qOpened){
						Camera_status = "STATUS_OFF";
						Cam_status = 0;
					}else{
						System.out.println("==nothing to do====");
					}

				} catch (Exception e) {
					Camera_status = "STATUS_ON";
					Cam_status = 1;
					System.out.println("=====Camera running=====");
				}
				if (Cam_status == 0) {
					//카메라 미실행중
					Camera_status = "OFF";
				} else if (Cam_status == 1){
					//카메라 실행중
					Camera_status = "ON";
				}
				String level = rawlevel + "%";
				String temp = rawtemp/10 + "℃";
			//	Toast.makeText(context,"sms arrived : 상태",2000);
				myMessage = "REPLY:현재상태\n" +
				    "배터리 잔량: "+ level + "\n" +
					"배터리 온도: " + temp + "\n" +
					"GPS 상태: "+ state + "\n" +
					"카메라 상태: " + Camera_status;
			}
			if (str.contains("#녹화")) {
			//	Toast.makeText(context,"sms arrived : 상태",2000);
				myMessage = "REPLY:녹화 시작됨";
			}
			if (str.contains("#녹화중지")) {
			//	Toast.makeText(context,"sms arrived : 상태",2000);
				myMessage = "REPLY:녹화 중지됨";
				if ( recorder !=null){
					//Log.e("CAM TEST","CAMERA STOP!!!!!");
					recorder.stop();
					recorder.release();
					recorder = null;
					myMessage = "REPLY:RECORD STOPPED";
			     } else {
					myMessage = "REPLY:NOT RECORDING STATE";
				 }
			}
			if (str.contains("#GPS_STATE")) {
				LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE );
				boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			//	Toast.makeText(context,"sms arrived : 상태",2000);
		    	String state = "ERROR";
			    if (statusOfGPS == true) {
					state = "ON";
				} else if (statusOfGPS == false) {
					state = "false";
				}
				myMessage = "REPLY:GPS State - " + state;
			}
			if (str.contains("#사진")) {
			//	Toast.makeText(context,"sms arrived : 상태",2000);
				myMessage = "REPLY:사진";
				Camera _camera;
				boolean qOpened = false;
				String Camera_status = null;
				int Cam_status = 0;
				try {
					_camera=Camera.open();
					qOpened=(_camera!=null);
					if(qOpened){
						Camera_status = "STATUS_OFF";
						Cam_status = 0;
					}else{
						System.out.println("==nothing to do====");
					}
				} catch (Exception e) {
					Camera_status = "STATUS_ON";
					Cam_status = 1;
					System.out.println("=====Camera running=====");
				}
				if (Cam_status == 0) {
					//카메라 미실행중
					Intent intenttp = new Intent("android.media.action.IMAGE_CAPTURE");
					File photoFile = new File(Environment.getExternalStorageDirectory(),  "Photo.png");
					intenttp.putExtra(MediaStore.EXTRA_OUTPUT,
									Uri.fromFile(photoFile));
					imageUri = Uri.fromFile(photoFile);
					SurfaceView view = new SurfaceView(context);
					try
					{
						camera.setPreviewDisplay(view.getHolder());
					}
					catch (IOException e)
					{}
				//	camera.startPreview();
				//	camera.takePicture(shutterCallback, rawPictureCallback, jpegPictureCallback);
				//	startActivityForResult(intenttp, TAKE_PICTURE);
				} else if (Cam_status == 1){
					//카메라 실행중
				}
				}
				if (myMessage != null){
				PendingIntent sentPI;
				String SENT = "SMS_SENT";

				sentPI = PendingIntent.getBroadcast(context, 0,new Intent(SENT), 0);
				smsManager.sendTextMessage(sendTo, null, myMessage, sentPI, null);  
				Log.e("PYGPS","=====MSG SENT SMSRECEIVER=====");
				}
			//	smsManager.sendTe
			}
			
		//	intent.putExtra("test2", str);
		//	intent.setClass(context, RedirectActivity.class);
		//	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		//	context.startActivity(intent);
		}
	}
//	private final LocationListener locListener = new LocationListener() {

//		Location myLocation;
//		@Override
/*		public void onStatusChanged(String provider, int status, Bundle extras) {
// TODO Auto-generated method stub
// GPS 상태 변화에 따른 행동을 적어주면 된다.
		}

		@Override
		public void onProviderEnabled(String provider) {
// TODO Auto-generated method stub
// GPS 사용 가능으로 바꼈을 때 행동이다.
		}
		@Override
		public void onProviderDisabled(String provider) {
// TODO Auto-generated method stub
// GPS 사용 불가능으로 바꼈을 때 행동을 적어주면 된다.
		}

		@Override
		public void onLocationChanged(Location location) {
// TODO Auto-generated method stub
// 바뀐 위치를 수신하였을 경우 수행되는 부분이다.
			myLocation = location;
	};
	*/
}
