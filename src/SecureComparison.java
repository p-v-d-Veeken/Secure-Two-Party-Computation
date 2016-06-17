import java.math.BigInteger;
import java.util.Vector;

class SecureComparison
{
	private Paillier paillier;
	private Verifier verifier;

	SecureComparison(Paillier paillier)
	{
		this.paillier = paillier;
		this.verifier = new Verifier(paillier);
	}
	int compare(BigInteger a, BigInteger b, int l) throws Exception
	{
		BigInteger z = paillier.encrypt(BigInteger.valueOf(2).pow(l)) //[z] <= [2^l] * [a] * [b]^-1
			.multiply(a)
			.mod(paillier.getNsquare())
			.multiply(b.modPow(BigInteger.ONE.negate(), paillier.getNsquare()))
			.mod(paillier.getNsquare());

		//BigInteger       r    = new BigInteger(80 + l + 1, Generator.random)).mod(paillier.getN();
		BigInteger         r    = BigInteger.valueOf(27); //Hard coded random value in for verifying correctness
		BigInteger         d    = z.multiply(paillier.encrypt(r)).mod(paillier.getNsquare()); //[d] = [z].[r]
		Vector<BigInteger> d1d2 = verifier.getD1D2(d, l);

		System.out.println(d1d2);

		return 0;
	}
	public static void main(String[] args) throws Exception
	{
		Paillier         paillier = new Paillier(8);
		SecureComparison comp     = new SecureComparison(paillier);
		BigInteger       a        = BigInteger.valueOf(6);
		BigInteger       b        = BigInteger.valueOf(5);
		int              l        = Integer.max(a.bitLength(), b.bitLength());

		comp.compare(paillier.encrypt(a), paillier.encrypt(b), l);
	}
}