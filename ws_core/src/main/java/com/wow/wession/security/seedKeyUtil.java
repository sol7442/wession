package com.wow.wession.security;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

public class seedKeyUtil {
	private static HashMap<String, String> map = null;

	public seedKeyUtil() {
		map = seedKeySet.map;
	}

	public static String getKey(String tempCode, String hashCode) {
		// Hashmap �씫�뼱 temp肄붾뱶瑜� �뜑�븯�뿬 SHA256.work瑜� �븿
		Set key = map.keySet();
		String valueName = "";

		for (Iterator iterator = key.iterator(); iterator.hasNext();) {
			String keyName = (String) iterator.next();
			try {
				String keysetHashCode = encryptSHA256.work(tempCode + keyName);
				if (keysetHashCode.equals(hashCode)) {
					valueName = (String) map.get(keyName);
					System.out.println(keyName +" = " +valueName);
				}
			} catch ( NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch ( UnsupportedEncodingException e) {
				e.printStackTrace();
			}

		}
		return valueName;
	}

	public static void main(String [] args) {
		setting();
		String mapkey = "1024";
		String tempCode = "5579";
		String plainText = "I love You, but you disappointed me. so, I'm sad. This is safe method. Until keyset noose.";
		
		// �씠�젃寃� �븫�샇�솕瑜� �븯怨좎꽑
		String seedkey = map.get(mapkey);
		String enc = "";
		try {
			enc = encryptSEED.getSeedEncrypt(plainText, encryptSEED.getSeedRoundKey(seedkey));
			System.out.println("plain text : " + plainText);
			System.out.println("encrypt text = " + enc );
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		System.out.println("======================");
		

		String hashCode = "";
		String sendEnc = "";
		// �꽌踰꾧컙 �넻�떊�뿉�뒗 �씠�젃寃�
		try {
			Base64 encoder = new Base64();
			hashCode = encryptSHA256.work(tempCode + mapkey);
			System.out.println("tempCode = " + tempCode + " and hascode = " + hashCode );
			sendEnc = encoder.encode(enc.getBytes());
			System.out.println("send encrypt text = " + URLEncoder.encode(sendEnc, "UTF-8"));
			
		} catch (NoSuchAlgorithmException e) { 
			e.printStackTrace();
		} catch ( UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		System.out.println("======================");
		
		// 諛쏆븘�꽌�뒗 �씠�젃寃�
		String seedkey2 = getKey(tempCode, hashCode);
		String dec = "";
		String recvEnc = "";
		
		try {
			Base64 decoder = new Base64();
			recvEnc = decoder.decodeBuffer(sendEnc).toString();
			dec = encryptSEED.getSeedDecrypt(enc, encryptSEED.getSeedRoundKey(seedkey));
			System.out.println(dec);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		LinkedList list = new LinkedList();
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		list.add("5");
		list.remove("1");

		System.out.println(list.get(list.size()-1));

		
	}

	private static void setting() {
		//map.clear();
//		map = new HashMap<String, String>();
//		for (int i=0; i<100; i++) {
//			map.put( Integer.toString(1001+i), "seedkey" + (20130001+i) + "%@");
//		}
		map = seedKeySet.map;
	}
}
