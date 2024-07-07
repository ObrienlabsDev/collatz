package dev.obrienlabs.collatz.service;

import java.math.BigInteger;

public class CollatzUnitOfWork {
    public static final BigInteger COLLATZ88 = BigInteger.valueOf(1980976057694848447L);     
    public static final BigInteger COLLATZ_2651 = new  BigInteger("2367363789863971985761"); // path of 2651
    protected static final BigInteger BIG_INTEGER_TWO = BigInteger.valueOf(2);
    
	protected long start;
	protected long len;
	protected long uowSplit = 1024;
	protected BigInteger maximumPath = BigInteger.ONE;
	protected BigInteger maximumValue = BigInteger.ONE;
	
	
	public CollatzUnitOfWork(long aStart, long aLen) {
		this.start = aStart;
		this.len = aLen;
	}
	
	protected void computeNoFork() {
		BigInteger current = BigInteger.valueOf(start);
		BigInteger stop = BigInteger.valueOf(start + len);
		BigInteger maxValue = stop;
		BigInteger maxPath = BigInteger.ONE;
		long path = 0;
		boolean newMax = false;
		while(current.compareTo(stop) != 0) {
			BigInteger prev = current;
			while (prev.compareTo(BigInteger.ONE) > 0) {
				if(prev.testBit(0)) {
					prev = prev.shiftLeft(1).add(prev).add(BigInteger.ONE);
				} else {
					prev = prev.shiftRight(1);
				}
				path++;
				if(prev.compareTo(maxValue) > 0) {
					maxValue = prev;
				}
			}
			if(path > maxPath.longValue()) {
				maxPath = BigInteger.valueOf(path);
			}			
			current = current.add(BIG_INTEGER_TWO);			
		}	
		if(maxValue.compareTo(maximumValue) > 0) {			
			maximumValue = maxValue;
			newMax = true;
		}
		
		if(maxPath.compareTo(maximumPath) > 0) {			
			maximumPath = maxPath;		
			newMax = true;
		}
		if(newMax) {
			System.out.println("S: " + start + " N: " + current + " M: " + maxValue + " P: " + maxPath + " thread: " + Thread.currentThread().getId());
		}
	}
	public static void main(String[] args) {
		CollatzUnitOfWork uow = new CollatzUnitOfWork(27, 2);
		uow.computeNoFork();

	}

}
