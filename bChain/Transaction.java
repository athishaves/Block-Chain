package blockChain.bChain;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.*;

public class Transaction {

    String senderAddress;
    String receiverAddress;
    public BigDecimal amount;
    byte[] signature = null;


    public Transaction(String senderAddress, String receiverAddress, BigDecimal amount) {
        this.senderAddress = senderAddress;
        this.receiverAddress = receiverAddress;
        this.amount = amount;
    }


    public String printTransaction() {
        return senderAddress + "," + receiverAddress + "," + amount;
    }


    public byte[] getTransaction() throws UnsupportedEncodingException {
        return Hash.sha_256(printTransaction()).getBytes(Hash.CHARSET);
    }


    public void signTransaction(KeyPair signingKey) {
        if (!Hash.keyToHex(signingKey.getPublic().getEncoded()).equals(this.senderAddress))
            throw new RuntimeException("You cannot sign transactions of other wallets");

        try {
            byte[] data = getTransaction();

            Signature signature = Signature.getInstance(Hash.SIG_ALGO);
            signature.initSign(signingKey.getPrivate());
            signature.update(data);

            this.signature = signature.sign();
            System.out.println("Signature : " + Hash.keyToHex(this.signature));

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    boolean isValid() {
        if(senderAddress == null) return true;
        if(signature == null || signature.length==0) throw new RuntimeException("No signature for the transaction");

        try {
            Signature signature = Signature.getInstance(Hash.SIG_ALGO);
            signature.initVerify(Hash.hexToPublicKey(this.senderAddress));
            signature.update(getTransaction());
            return signature.verify(this.signature);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
