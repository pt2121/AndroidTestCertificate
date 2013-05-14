package com.test.certificate;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

public class ApCertificateUtil {

	/**
	 * parse a certificate file into ArrayList of certificates
	 */
	public static ArrayList<Certificate> readCertificate(File f)
			throws CertificateException {
		ArrayList<Certificate> certs = new ArrayList<Certificate>();
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		BufferedInputStream in;
		try {
			in = new BufferedInputStream(new FileInputStream(f));
			while (in.available() > 0) {
				Certificate cert = cf.generateCertificate(in);
				certs.add(cert);
			}
			in.close();
			return certs;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * populate X509Certificate from a certificate file at the certFilePath
	 */
	public static X509Certificate getX509(String certFilePath) throws CertificateException, FileNotFoundException {
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		FileInputStream in = new FileInputStream(certFilePath);
		return (X509Certificate) cf.generateCertificate(in);
	}
	
	/**
	 * Checks whether given X.509 certificate is self-signed.
	 */
	public static boolean isSelfSigned(X509Certificate cert)
			throws CertificateException, NoSuchAlgorithmException,
			NoSuchProviderException {
		try {
			// Try to verify certificate signature with its own public key
			PublicKey key = cert.getPublicKey();
			cert.verify(key);
			return true;
		} catch (SignatureException sigEx) {
			// Invalid signature --> not self-signed
			return false;
		} catch (InvalidKeyException keyEx) {
			// Invalid key --> not self-signed
			return false;
		}
	}
}
