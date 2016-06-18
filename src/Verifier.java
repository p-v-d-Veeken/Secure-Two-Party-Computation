import java.math.BigInteger;
import java.util.Vector;

class Verifier
{
	private Paillier   paillier;
	private BigInteger d1;

	Verifier(Paillier paillier)
	{
		this.paillier = paillier;
	}
	Vector<BigInteger> getD1D2(BigInteger dEnc, int l) throws Exception
	{
		BigInteger         d    = paillier.decrypt(dEnc);
		BigInteger         d1   = d.mod(BigInteger.valueOf(2).pow(l)); //d1 <= d mod 2^l
		BigInteger         d2   = d.divide(BigInteger.valueOf(2).pow(l)); //d2 <= floor(d / 2^l)
		Vector<BigInteger> d1d2 = new Vector<>(2);

		d1d2.add(d1);
		d1d2.add(d2);

		this.d1 = d1;

		return d1d2;
	}
	Vector<BigInteger> getT(int l) throws Exception
	{
		Vector<BigInteger> t      = new Vector<>(l - 1);
		byte               d1Bits = d1.byteValue();

		for(int i = 0; i < l; i++)
		{
			BigInteger ti = BigInteger.ZERO.add(BigInteger.valueOf((d1Bits >> i) & 1)); //t_i = d^1_i

			for(int j = i + 1; j < l; j++)
			{
				ti = ti.add(BigInteger.valueOf(2).pow(j) //t_i += sum^{l-1}_{j=i+1} 2^j * d^1_j
					.multiply(BigInteger.valueOf((d1Bits >> j) & 1))
				);
			}
			t.add(paillier.encrypt(ti));
		}
		return t;
	}
}