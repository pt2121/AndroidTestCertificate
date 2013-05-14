package com.test.certificate.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.test.certificate.ApCertTester;
//import com.test.certificate.CertVerifier;
import com.test.certificate.R;
// Testing how to use x509certificate in Android: verifying certificate, getting certificate info. Basically, everything is the same as in Java
public class MainActivity extends Activity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ApCertTester tester = new ApCertTester();
        tester.test(this);
		
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
