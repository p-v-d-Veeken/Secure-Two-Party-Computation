import java.math.BigInteger;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Verifier
{
	private           Paillier   paillier;
	private transient BigInteger d1;
	private transient BigInteger A;

	Verifier(Paillier paillier)
	{
		this.paillier = paillier;
	}
	BigInteger getD2(BigInteger dEnc, int l) throws Exception
	{
		BigInteger d  = paillier.decrypt(dEnc);
		BigInteger d1 = d.mod(BigInteger.valueOf(2).pow(l)); //d1 <= d mod 2^l
		BigInteger d2 = d.divide(BigInteger.valueOf(2).pow(l)); //d2 <= floor(d / 2^l)

		this.d1 = d1;

		return paillier.encrypt(d2);
	}
	Vector<BigInteger> getT(int l) throws Exception
	{
		byte d1Bits = d1.byteValue();

		return IntStream.range(0, l)
			.parallel()
			.mapToObj(i -> {
				try
				{
					BigInteger ti = BigInteger.valueOf((d1Bits >> i) & 1); //t_i = d^1_i

					for(int j = i + 1; j < l; j++)
					{
						ti = ti.add(BigInteger.valueOf(2).pow(j) //t_i += sum^{l-1}_{j=i+1} 2^j * d^1_j
							.multiply(BigInteger.valueOf((d1Bits >> j) & 1))
						);
					}
					return paillier.encrypt(ti.mod(paillier.getN()));
				}
				catch(Exception e) { e.printStackTrace(); }

				return BigInteger.ZERO;
			})
			.collect(Collectors.toCollection(Vector<BigInteger>::new));
	}
	BigInteger getA(BigInteger ei) throws Exception
	{
		if(A != null && A.equals(BigInteger.ONE))
		{
			return A;
		}
		ei = paillier.decrypt(ei);

		if(ei.equals(BigInteger.ZERO))
		{
			return A = paillier.encrypt(BigInteger.ONE);
		}
		else if(A == null)
		{
			A = paillier.encrypt(BigInteger.ZERO);
		}
		return A;
	}
}