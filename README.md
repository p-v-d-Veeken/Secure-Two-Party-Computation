# WHADDUP
Dit is wat ik tot nu toe heb gedaan:
- Willekeurige boodschap kan volgens het schema geëncrypt worden.
- Boodschap kan via  een socket naar de mixnets gestuurd worden.

## Issues:
- Ik heb geen idee of ik de goede cipher modes gebruik (zie: Config.AESMode & Config.RSAMode).
- Het mixnet [control panel](http://pets.ewi.utwente.nl:62639/) ligt eruit, kan dus niks testen of debuggen.
- Ik gebruik steeds dezelfde IV voor AES encryptie (zie: Config.AESIV), geen idee of dat een probleem is.
- Weet niet zeker of mijn implementatie van het RSA encrypten van de AES key en IV correct is (lines 57-61 in Encryptor)