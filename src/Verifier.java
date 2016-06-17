import java.math.BigInteger;
import java.util.Vector;

class Verifier
{
	private Paillier paillier;

	Verifier(Paillier paillier)
	{
		this.paillier = paillier;
	}
	Vector<BigInteger> getD1D2(BigInteger dEnc, int l) throws Exception
	{
		Vector<BigInteger> d1d2 = new Vector<>(2);
		BigInteger         d    = paillier.decrypt(dEnc);
		BigInteger         d1   = d.mod(BigInteger.valueOf(2).pow(l)); //d1 <= d mod 2^l
		BigInteger         d2   = d.divide(BigInteger.valueOf(2).pow(l)); //d2 <= floor(d / 2^l)

		d1d2.add(d1);
		d1d2.add(d2);

		return d1d2;
	}
}