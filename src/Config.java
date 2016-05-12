class Config
{
	/* Network Settings */
	static final String   host             = "pets.ewi.utwente.nl";
	static final int      port             = 60914;
	static final int      envPort          = 62639;
	/* Encryption Settings */
	static final String   keyFileFormat    = "keys/public_key_";
	static final String   keyFileExtension = ".pem";
	static final String   AESMode          = "AES/CBC/PKCS7Padding";
	static final String   RSAMode          = "RSA/None/OAEPWithSHA1AndMGF1Padding";
	static final String   AESIV            = "GODVERDOMMEAJAX1";
	/* Mixnet Settings */
	static final String[] nodes            = new String[]{"A", "B", "C", "Cache"};
	/* Log Settings */
	static final String   cacheLog         = "log/cache";
	/* CLI Settings */
	static final String   commands         = "Commands:\n\tsend <recipient> <message>:\tSend a message\n\tassignment " +
		"<number>:\t\tCall assignment code\n\tstop:\t\t\t\t\t\tStop the application\n\treload:\t\t\t\t\t\tReload the " +
		"application";
}
