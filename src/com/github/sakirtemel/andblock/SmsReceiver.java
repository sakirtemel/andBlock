package com.github.sakirtemel.andblock;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Base64;
import android.widget.Toast;


public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
    	
        Bundle bundle = intent.getExtras();
        if (bundle == null)
            return;

    	
    	Object[] pdus = (Object[])bundle.get("pdus");
        if (pdus.length == 0)
            return;
        SmsMessage[] msgs = new SmsMessage[pdus.length];
        msgs[0] = SmsMessage.createFromPdu((byte[])pdus[0]);

        String sender = msgs[0].getOriginatingAddress();
        
		DatabaseHelper db = DatabaseHelper.getInstance(context);
       
        if ( db.isBlocked(sender) ){
        
	        StringBuilder message = new StringBuilder(msgs[0].getMessageBody());
	
	        for (int i = 1; i < msgs.length; i++) {
	            msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
	            message.append(msgs[i].getMessageBody());
	        }
	        //Toast.makeText(context, sender +  "  -  " + message, Toast.LENGTH_LONG).show();
	        db.insertNewBlockedMessage(message.toString(), sender);
	        abortBroadcast();
        }
    }


    
    
}
