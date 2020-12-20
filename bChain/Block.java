package blockChain.bChain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;

public class Block {

    Timestamp timestamp;
    String previousHash;
    String hash;
    public ArrayList<Transaction> transactions;
    int nonce;


    public Block(Timestamp timestamp, ArrayList<Transaction> transactions, String previousHash) {
        this.timestamp = timestamp;
        this.transactions = transactions;
        this.previousHash = previousHash;
        this.nonce = 1;
        this.hash = calculateHash();
    }


    public Block(Timestamp timestamp, ArrayList<Transaction> transactions) {
        this(timestamp, transactions, "#");
    }


    public String calculateHash() {
        StringBuilder sbr = new StringBuilder();
        sbr.append(this.timestamp.toString());
        if (!isGenesisBlock())
            for (Transaction t : this.transactions)
                sbr.append(t.printTransaction());
        sbr.append(this.previousHash).append(this.nonce-1);
        return Hash.sha_256(sbr.toString());
    }


    public void mineBlock(int difficulty) {
        String curHash = this.hash;
        char[] ch = new char[difficulty];    Arrays.fill(ch,'0');   String prev = new String(ch);
        while (!this.hash.substring(0,difficulty).equals(prev)) {
            this.hash = this.calculateHash();
            this.nonce++;
        }
        if(!curHash.equals(this.hash)) this.nonce--;
        System.out.println("Block mined successfully. Hash = " + this.hash + " Nonce = " + nonce);
    }


    public boolean hasValidTransactions() {
        for (Transaction t : this.transactions) {
            if (!t.isValid()) return false;
        }
        return true;
    }


    public boolean isGenesisBlock() {
        return this.previousHash.equals("#");
    }


    public void printBlock() {
        String out =
                "\nTimestamp = " + this.timestamp.toString() + ", ";
        if(!this.isGenesisBlock()) out += "\nTransactions :\n" + printTransactions();
        out +=
                "Hash = " + this.hash +
                ", PreviousHash = " + this.previousHash;
        System.out.println(out);
    }

    public String printTransactions() {
        StringBuilder sbr = new StringBuilder();
        for (Transaction t : transactions) sbr.append(t.printTransaction()).append('\n');
        return sbr.toString();
    }

}
