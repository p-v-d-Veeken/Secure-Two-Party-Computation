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
		BigInteger         z    = calculateZ(a, b, l);     //[z] = [2^l] * [a] * [b]^-1
		BigInteger         r    = calculateR(l);           //[r] = [random int of 80 + l + 1 bits]
		BigInteger         d    = calculateD(z, r);        //[d] = [z].[r]
		Vector<BigInteger> d1d2 = verifier.getD1D2(d, l);  //[d^1] = d mod 2^l; [d^2] = floor(d / 2^l)
		Vector<BigInteger> t    = verifier.getT(l);        //[t_i] = d^1_i + sum^{l-1}_{j=i+1} 2^j * d^1_j

		System.out.println(t);

		return 0;
	}
	private BigInteger calculateZ(BigInteger a, BigInteger b, int l) throws Exception
	{
		return paillier.encrypt(BigInteger.valueOf(2).pow(l)) //[z] <= [2^l] * [a] * [b]^-1
			.multiply(a)
			.mod(paillier.getNsquare())
			.multiply(b.modPow(BigInteger.ONE.negate(), paillier.getNsquare()))
			.mod(paillier.getNsquare());
	}
	private BigInteger calculateR(int l) throws Exception
	{
		//return paillier.encrypt(new BigInteger(80 + l + 1, Generator.random));
		return paillier.encrypt(BigInteger.valueOf(27));
	}
	private BigInteger calculateD(BigInteger z, BigInteger r)
	{
		return z.multiply(r).mod(paillier.getNsquare());
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