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
import android.util.*;
import android.app.*;

public class GPSSender extends BroadcastReceiver implements LocationListener {

	static final String logTag = "SmsReceiver";
	static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
	private Location myLocation = null;
	double latPoint = 0.0;
	double lngPoint = 0.0;
	double attPoint = 0.0;
	float spd = 0;
	boolean send = false;
	boolean track = false;
	String sendTo = "";
	String sender = "";
	String myMessage = null;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		Log.e("PYGPS","=====GPSSENDER ACTIVATED-RECEIVED=====");
		
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
			sender = "";
			SmsMessage[] smsMessages = new SmsMessage[pdusObj.length];
			for (int i = 0; i < pdusObj.length; i++) {
				smsMessages[i] = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
				str = smsMessages[i].getMessageBody(); // 임의의 String 변수에 값 넣음
				sender = smsMessages[i].getOriginatingAddress();
			}
			SmsManager smsManager = SmsManager.getDefault();

			if (str.contains("#")) {
				Log.e("PYGPS","=====IT CONTAINS #=====");
			//	String sendTo = "01063239607";
			    sendTo = sender;
				myMessage = "";
			if (str.contains("#GPS_TRACK_ON")) {
			    track=true;
				myMessage = "REPLY:GPS 추적 ON\n위치가 업데이트되면 자동으로 문자가 전송됩니다";
			}
			if (str.contains("#GPS_TRACK_OFF")) {
				track=false;
				myMessage = "REPLY:GPS 추적 OFF";
			}
			if (str.contains("#GPS_SEND")) {
				Log.e("PYGPS","=====GPS_SEND MESSAGE DETECTED=====");
                // gps 전송
			//	Toast.makeText(context,"sms arrived : 상태",2000);
				String contextLS = Context.LOCATION_SERVICE; //안드로이드 시스템으로 부터 LocationManager 서비스를 요청하여 할당
				LocationManager locManager = (LocationManager) context.getSystemService(contextLS);
				Criteria criteria = new Criteria();
				criteria.setAccuracy(Criteria.ACCURACY_FINE);      	// 정확도
				criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);   // 전원 소비량
				criteria.setAltitudeRequired(true);                	// 고도, 높이 값을 얻어 올지를 결정
				criteria.setBearingRequired(true);                 // provider 기본 정보
				criteria.setSpeedRequired(true);                   //속도
				criteria.setCostAllowed(true);                      	//위치 정보를 얻어 오는데 들어가는 금전적 비용
				String provider = locManager.getBestProvider(criteria, true); //로케이션 메니저의 provider 
				myLocation = locManager.getLastKnownLocation(provider);
				locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 5, this);
				
				// 기지국으로 부터 위치정보를 업데이트 요청
				//locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
				// 주소를 확인하기 위한 Geocoder KOREA 와 KOREAN 둘다 가능
				Geocoder geoCoder = new Geocoder(context, Locale.KOREAN); 
				
			//	StringBuffer juso = new StringBuffer();
			//	float latPoint = myLocation.getLatitude();
			//	float lngPoint = myLocation.getLongitude();
			//	float speed = (float)(myLocation.getSpeed() * 3.6);
			
				StringBuffer juso = new StringBuffer();
				Log.e("PYGPS","=====GPS SETTING FINISHED=====");
				if (myLocation != null) {
					Log.e("PYGPS","=====MYLOCATION IS NOT NULL=====");
				    latPoint = myLocation.getLatitude();
					lngPoint = myLocation.getLongitude();
					attPoint = myLocation.getAltitude();
					spd = (float)(myLocation.getSpeed() * 3.6);
				try {
					// 위도,경도를 이용하여 현재 위치의 주소를 가져온다. 
					List<Address> addresses;
					//Geocoder geoCoder = new Geocoder(context, Locale.KOREAN); 
					addresses = geoCoder.getFromLocation(latPoint, lngPoint, 1);
					for(Address addr: addresses){
						int index = addr.getMaxAddressLineIndex();
						for(int i=0;i<=index;i++){
							juso.append(addr.getAddressLine(i));
							juso.append(" ");
						}
						juso.append("\n");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				} else  {
					Log.e("PYGPS","=====MYLOCATION IS NULL=====");
					myLocation = locManager.getLastKnownLocation( LocationManager.NETWORK_PROVIDER );
				}
				if (latPoint==0.0){
					Log.e("PYGPS","=====LATPOINT IS ZERO=====");
					double latPoint = 0.0;
					double lngPoint = 0.0;
					double attPoint = 0.0;
					float spd = 0;
					Log.e("PYGPS","=====LOC DATA CLEARED=====");
					String contextLS2 = Context.LOCATION_SERVICE; //안드로이드 시스템으로 부터 LocationManager 서비스를 요청하여 할당
					LocationManager locManager2 = (LocationManager) context.getSystemService(contextLS2);
					provider = locManager.getBestProvider(criteria, true);
					Location myLocation2 = locManager2.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					latPoint = myLocation2.getLatitude();
				    lngPoint = myLocation2.getLongitude();
					attPoint = myLocation2.getAltitude();
					spd = (float)(myLocation2.getSpeed() * 3.6);
					try {
						// 위도,경도를 이용하여 현재 위치의 주소를 가져온다. 
						List<Address> addresses;
						//Geocoder geoCoder = new Geocoder(context, Locale.KOREAN); 
						addresses = geoCoder.getFromLocation(latPoint, lngPoint, 1);
						for(Address addr: addresses){
							int index = addr.getMaxAddressLineIndex();
							for(int i=0;i<=index;i++){
								juso.append(addr.getAddressLine(i));
								juso.append(" ");
							}
							juso.append("\n");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (lngPoint==0.0){
						Log.e("PYGPS","=====LNGPOINT IS ZERO=====");
						myMessage = "REPLY:GPS 검색 중입니다";
						send = true;
					} else {
						Log.e("PYGPS","=====TWICE TRY SUCCEED=====");
						myMessage = "REPLY:GPS" + "\n" + "주소:" + juso + "\n" + "위도:" + latPoint + "\n" + "경도:" + lngPoint + "\n" + "고도:" + attPoint + "\n" + "속도:" +spd;
					 //   smsManager.sendTextMessage(sendTo, null, myMessage, null, null);  
					}
				}else {
					Log.e("PYGPS","=====FIRST TRY SUCCEED=====");
				myMessage = "REPLY:GPS" + "\n" + "주소:" + juso + "\n" + "위도:" + latPoint + "\n" + "경도:" + lngPoint + "\n" + "고도:" + attPoint + "\n" + "속도:" +spd;
				}
				if (myMessage != null && myMessage != ""){
				Log.e("PYGPS","=====MSG SENDING FIRST=====");
					smsManager.sendTextMessage(sendTo, null, myMessage, null, null);  
				Log.e("PYGPS","=====MSG SENT=====");
				}
			}

				if (myMessage != null && myMessage != ""){
				Log.e("PYGPS","=====MSG SENDING SECOND=====");
				//	smsManager.sendTextMessage(sendTo, null, myMessage, null, null);  
					PendingIntent sentPI;
					String SENT = "SMS_SENT";

				//	sentPI = PendingIntent.getBroadcast(context, 0,new Intent(SENT), 0);
					smsManager.sendTextMessage(sendTo, null, myMessage, null, null);  
				Log.e("PYGPS","=====MSG SENT1=====");
				}
		//		PendingIntent sentPI;
		//		String SENT = "SMS_SENT";
//
		//		sentPI = PendingIntent.getBroadcast(context, 0,new Intent(SENT), 0);
		//		smsManager.sendTextMessage(sendTo, null, myMessage, sentPI, null);  
		//		Log.e("PYGPS","=====MSG SENT2=====");
		}
		//	PendingIntent sentPI;
		//	String SENT = "SMS_SENT";
//
		//	sentPI = PendingIntent.getBroadcast(context, 0,new Intent(SENT), 0);
		//	smsManager.sendTextMessage(sendTo, null, myMessage, sentPI, null);  
		//	Log.e("PYGPS","=====MSG SENT3=====");
	}}
//	private final LocationListener locListener = new LocationListener() {

//		Location myLocation;
//		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
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
			if (send=true) {
				String myMessage;
				double latPoint = myLocation.getLatitude();
				double lngPoint = myLocation.getLongitude();
				double attPoint = myLocation.getAltitude();
				float spd = (float)(myLocation.getSpeed() * 3.6);
			//	Geocoder geoCoder = new Geocoder(this.getClass(), Locale.KOREAN); 
				myMessage = "REPLY:GPS" + "\n" + "위도:" + latPoint + "\n" + "경도:" + lngPoint + "\n" + "고도:" + attPoint + "\n" + "속도:" +spd;
				SmsManager smsManager = SmsManager.getDefault();
				if (sendTo != null){
				smsManager.sendTextMessage(sendTo, null, myMessage, null, null);  
				}
				send=false;
				}
				
			if (track=true) {
				String myMessage;
				double latPoint = myLocation.getLatitude();
				double lngPoint = myLocation.getLongitude();
				double attPoint = myLocation.getAltitude();
				float spd = (float)(myLocation.getSpeed() * 3.6);
				//	Geocoder geoCoder = new Geocoder(this.getClass(), Locale.KOREAN); 
				myMessage = "REPLY:GPS" + "\n" + "위도:" + latPoint + "\n" + "경도:" + lngPoint + "\n" + "고도:" + attPoint + "\n" + "속도:" +spd;
				SmsManager smsManager = SmsManager.getDefault();
				if (sendTo != null){
					PendingIntent sentPI;
					String SENT = "SMS_SENT";

				//	sentPI = PendingIntent.getBroadcast(context, 0,new Intent(SENT), 0);
				//	smsManager.sendTextMessage(sendTo, null, myMessage, sentPI, null);  
					smsManager.sendTextMessage(sender, null, myMessage, null, null);
				}
			}
			}
};
