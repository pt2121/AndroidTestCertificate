package com.test.certificate.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import android.content.Context;
import android.os.Environment;
import android.test.AndroidTestCase;
import android.util.Log;

import com.test.certificate.ApCertFile;
import com.test.certificate.ApCertificateUtil;
import com.test.certificate.R;

public class ApCertTester extends AndroidTestCase {
	private static final String TAG = "ApCertTester";
	public void test(Context context) {
		String path = Environment.getExternalStorageDirectory().getPath().concat(context.getString(R.string.test_cert_path));
		Log.v(TAG, path);
		try {
			X509Certificate rootCACert = ApCertificateUtil.getX509(path+"AetherPal-Root-CA.cer");
			ApCertFile cert7 = new ApCertFile(path+"cert7.cer");
	    	ApCertFile cert6 = new ApCertFile(path+"cert6.cer");
	    	ApCertFile devselfsigned = new ApCertFile(path+"devselfsigned.cer");
	    	
	    	assertTrue(cert7.verify(rootCACert));
	    	assertFalse(cert6.verify(rootCACert));
	    	assertFalse(devselfsigned.verify(rootCACert));
	    	assertFalse(ApCertificateUtil.isSelfSigned(ApCertificateUtil.getX509(path+"cert7.cer")));
	    	assertTrue(ApCertificateUtil.isSelfSigned(ApCertificateUtil.getX509(path+"devselfsigned.cer")));
		} catch (CertificateException e) {
			Log.e(TAG, e.toString());
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			Log.e(TAG, e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(TAG, e.toString());
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			Log.e(TAG, e.toString());
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			Log.e(TAG, e.toString());
			e.printStackTrace();
		}
    	
	}
}
