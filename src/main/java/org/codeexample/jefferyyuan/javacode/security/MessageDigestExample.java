package org.codeexample.jefferyyuan.javacode.security;

import java.io.FileInputStream;
import java.security.MessageDigest;

/**
 * Source: http://www.informit.com/articles/article.aspx?p=170967&seqNum=5
 * <p>
 * Message digests, also known as message fingerprints or secure hash, are
 * computed by applying a one-way hash function over the data bits comprising
 * the message. Any modification in the original message, either intentional or
 * unintentional, will most certainly result in a change of the digest value.
 * Also, it is computationally impossible to derive the original message from
 * the digest value. These properties make digests ideal for detecting changes
 * in a given message. Compute the digest before storing or transmitting the
 * message and then compute the digest after loading or receiving the message.
 * If the digest values match then one can be sure with good confidence that the
 * message has not changed
 * <p>
 * A common use of message digests is to securely store and validate passwords.
 * The basic idea is that you never store the password in clear-text. Compute
 * the message digest of the password and store the digest value. To verify the
 * password, compute its digest and match it with the stored value. If both
 * values are equal, the verification succeeds. This way no one, not even the
 * administrator, gets to know your password.
 * <p>
 * The verification or check for integrity of the message is done by computing
 * the digest value and comparing this with the original digest for size and
 * content equality. Class MessageDigest even includes static method
 * isEqual(byte[] digestA, byte[] digestB) to perform this task.
 * <p>
 * The providers bundled with J2SE v1.4 support two message digest algorithms:
 * SHA (Secure Hash Algorithm) and MD5. SHA, also known as SHA-1, produces a
 * message digest of 160 bits. It is a FIPS (Federal Information Processing
 * Standard) approved standard. In August 2002, NIST announced three more FIPS
 * approved standards for computing message digest: SHA-256, SHA-384 and
 * SHA-512. These algorithms use a digest value of 256, 384 and 512 bits
 * respectively, and hence provide much better protection against brute-force
 * attacks. MD5 produces only 128 bits as message digest, and is considerably
 * weaker.
 * <p>
 * 
 */
public class MessageDigestExample
{
    public static void main(
            String[] args) throws Exception
    {
        String datafile = "ComputeDigest.java";

        MessageDigest md = MessageDigest.getInstance("SHA1");
        FileInputStream fis = new FileInputStream(datafile);
        byte[] dataBytes = new byte[1024];
        int nread = fis.read(dataBytes);
        while (nread > 0)
        {
            md.update(dataBytes, 0, nread);
            nread = fis.read(dataBytes);
        }
        byte[] mdbytes = md.digest();
        // System.out.println("Digest(in hex):: " +
        // Util.byteArray2Hex(mdbytes));
    }
}
