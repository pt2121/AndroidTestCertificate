package com.test.certificate;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import android.content.Context;
import android.os.Environment;
import android.test.AndroidTestCase;

public class ApCertTester extends AndroidTestCase {
	public void test(Context context) {
		String path = Environment.getExternalStorageDirectory().getPath().concat(context.getString(R.string.test_cert_path));
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
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		}
    	
	}
}
