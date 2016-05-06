class Config
{
	/* Network Settings */
	static final String   host             = "pets.ewi.utwente.nl";
	static final int      port             = 62639;
	/* Encryption Settings */
	static final String   keyFileFormat    = "keys/public_key_";
	static final String   keyFileExtension = ".pem";
	static final String   AESMode          = "AES/CBC/PKCS5Padding";
	static final String   RSAMode          = "RSA/ECB/PKCS1Padding";
	static final String   AESIV            = "AFCAJAX1KAMPIOEN";
	/* Mixnet Settings */
	static final String[] nodes            = new String[]{"A", "B", "C", "Cache"};
}
