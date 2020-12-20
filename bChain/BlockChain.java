package blockChain.bChain;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;

public class BlockChain {

    public ArrayList<Block> chain;
    static final int difficulty = 2;
    static BigDecimal miningReward = BigDecimal.valueOf(1);
    ArrayList<Transaction> pendingTransaction;


    public BlockChain() {
        this.chain = new ArrayList<>();
        chain.add(createGenesisBlock());
        pendingTransaction = new ArrayList<>();
    }


    Block createGenesisBlock() {
        Block genesisBlock = new Block(new Timestamp(System.currentTimeMillis()), null);
        genesisBlock.mineBlock(difficulty);
        return genesisBlock;
    }


    Block getLatestBlock() {
        return chain.get(chain.size()-1);
    }


    public void minePendingTransactions(String minerAddress) {
        ArrayList<Transaction> list = new ArrayList<>(pendingTransaction);
        Block block = new Block(new Timestamp(System.currentTimeMillis()), list, this.getLatestBlock().hash);
        block.mineBlock(difficulty);
        System.out.println("Block successfully mined...");
        this.chain.add(block);
        this.pendingTransaction.clear();
        this.pendingTransaction.add(new Transaction(null, minerAddress, miningReward));
    }


    public boolean isChainValid() {
        for (int i=1; i<this.chain.size(); i++) {
            Block curBlock = this.chain.get(i);
            Block prevBlock = this.chain.get(i-1);

            if (!curBlock.hasValidTransactions()) return false;
            if (!curBlock.hash.equals(curBlock.calculateHash())) return false;
            if (!curBlock.previousHash.equals(prevBlock.hash)) return false;
        }
        return true;
    }


    public void addTransaction(Transaction transaction) {
//        if(transaction.senderAddress!=null &&
//                this.getBalance(transaction.senderAddress).compareTo(transaction.amount)<0)
//                    throw new RuntimeException("No enough balance in the wallet");
        if(!transaction.isValid()) throw new RuntimeException("Transaction is not valid");
        this.pendingTransaction.add(transaction);
    }


    public BigDecimal getBalance(String address) {
        BigDecimal balance = BigDecimal.valueOf(0);
        for (Block block : this.chain) {
            if(block.isGenesisBlock()) continue;
            for (Transaction curTransaction : block.transactions) {
                if (address.equals(curTransaction.senderAddress)) balance = balance.subtract(curTransaction.amount);
                if (address.equals(curTransaction.receiverAddress)) balance = balance.add(curTransaction.amount);
            }
        }
        return balance;
    }


    public void printBlockChain() {
        for (Block block : this.chain) block.printBlock();
        System.out.println();
    }
}
