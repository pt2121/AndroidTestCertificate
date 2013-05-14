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

public class CertVerifier {
	
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

	/**
	 * Checks whether given X.509 certificate is self-signed.
	 */
	public static boolean isSelfSigned(String certFilePath)
			throws CertificateException, NoSuchAlgorithmException,
			NoSuchProviderException {
		File certFile = new File(certFilePath);
		ArrayList<Certificate> certs = readCertificate(certFile);
		boolean selfSigned = false;
		for (Certificate cert : certs) {
			System.out.println(" -- ");
			// Try to verify certificate signature with its own public key
			selfSigned = selfSigned
					|| CertVerifier.isSelfSigned((X509Certificate) cert);
		}
		return selfSigned;
	}

	private static ArrayList<Certificate> readCertificate(File f)
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
	 * Verify certFilePath against caFilePath
	 */
	public static boolean verify(String certFilePath, String caFilePath)
			throws CertificateException, FileNotFoundException {
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		FileInputStream in = new FileInputStream(caFilePath);
		X509Certificate caCert = (X509Certificate) cf.generateCertificate(in);
		File file = new File(certFilePath);
		// CertVerifier verifier = new CertVerifier();
		try {
			ArrayList<Certificate> certs = readCertificate(file);
			for (Certificate cert : certs) {
				cert.verify(caCert.getPublicKey());
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Verify certFilePath against downloadedCertPath
	 * return true if the certificate matches any certificate in the downloadedCertPath chain
	 */
	public static boolean verifyInChain(String certFilePath,
			String downloadedCertPath) {
		File certFile = new File(certFilePath);
		File downloadedFile = new File(downloadedCertPath);
		try {
			ArrayList<Certificate> certs = readCertificate(certFile);
			ArrayList<Certificate> downloadedChain = readCertificate(downloadedFile);

			System.out.println(certs.size());
			System.out.println(downloadedChain.size());

			for (Certificate cert : certs) {
				for (Certificate c : downloadedChain) {
					try {
						cert.verify(c.getPublicKey());
						return true;
					} catch (Exception e) {
						System.out.println(e.toString());
					}
				}
			}

		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return false;
	}

}
