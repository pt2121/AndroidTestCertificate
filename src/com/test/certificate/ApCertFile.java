package com.test.certificate;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import android.util.Log;

public class ApCertFile {

	private ArrayList<X509Certificate> mCerts;
	private String mCertFilePath;
	private File mCertFile;

	private final String TAG = "ApCertFile";

	public ApCertFile(String mCertFilePath) throws FileNotFoundException,
			IOException, CertificateException {
		super();
		this.mCertFilePath = mCertFilePath;
		mCertFile = new File(mCertFilePath);
		if (mCertFile.isFile()) {
			mCerts = new ArrayList<X509Certificate>();
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			BufferedInputStream in = new BufferedInputStream(
					new FileInputStream(mCertFile));
			while (in.available() > 0) {
				X509Certificate cert = (X509Certificate) cf
						.generateCertificate(in);
				mCerts.add(cert);
			}
			in.close();
		}
	}

	public ApCertFile(File mCertFile) throws FileNotFoundException,
			CertificateException, IOException {
		super();
		this.mCertFile = mCertFile;
		if (mCertFile.isFile()) {
			mCerts = new ArrayList<X509Certificate>();
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			BufferedInputStream in = new BufferedInputStream(
					new FileInputStream(mCertFile));
			while (in.available() > 0) {
				X509Certificate cert = (X509Certificate) cf
						.generateCertificate(in);
				mCerts.add(cert);
			}
			in.close();
		}
	}

	/**
	 * Verify this certificate against a certificate at filePath
	 * 
	 * @return true if this certificate was signed with the public key of the
	 *         certificate at certFilePath.
	 */
	public boolean verify(String certFilePath) throws CertificateException,
			FileNotFoundException {
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		FileInputStream in = new FileInputStream(certFilePath);
		X509Certificate caCert = (X509Certificate) cf.generateCertificate(in);
		try {
			for (Certificate cert : mCerts) {
				cert.verify(caCert.getPublicKey());
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Verify this certificate against the caCert
	 * 
	 * @return true if this certificate was signed with the public key of
	 *         caCert.
	 */
	public boolean verify(X509Certificate caCert) throws CertificateException,
			FileNotFoundException {
		try {
			for (Certificate cert : mCerts) {
				cert.verify(caCert.getPublicKey());
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Verify this certificate against the caCert
	 * 
	 * @return true if this certificate was signed with the public key of any
	 *         certificate in apCertChain. false otherwise.
	 */
	public boolean verify(ApCertFile apCertChain) {
		boolean ret = false;
		ArrayList<X509Certificate> certs = apCertChain.getmCerts();
		if (certs == null) {
			Log.e(TAG, "no cert in apCertChain");
			return false;
		}
		for (Certificate cert : mCerts) {
			for (Certificate c : certs) {
				try {
					cert.verify(c.getPublicKey());
					ret = true;
				} catch (InvalidKeyException e) {
					Log.e(TAG, e.toString());
				} catch (CertificateException e) {
					Log.e(TAG, e.toString());
				} catch (NoSuchAlgorithmException e) {
					Log.e(TAG, e.toString());
				} catch (NoSuchProviderException e) {
					Log.e(TAG, e.toString());
				} catch (SignatureException e) {
					Log.e(TAG, e.toString());
				}
			}
		}
		return ret;
	}

	public String getmCertFilePath() {
		return mCertFilePath;
	}

	public File getmCertFile() {
		return mCertFile;
	}

	public ArrayList<X509Certificate> getmCerts() {
		return mCerts;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (X509Certificate cert : mCerts) {
			sb.append("Certificate SN# " + cert.getSerialNumber() + "\n");
			sb.append("Certificate issued by: " + cert.getIssuerDN() + "\n");
			sb.append("Certificate for: " + cert.getSubjectDN() + "\n");
		}
		return sb.toString();
	}

}
