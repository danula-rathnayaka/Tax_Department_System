package edu.iit.gtds.tax_department_system.model;

public class Transaction {
    private String billId;
    private String itemCode;
    private Double internalPrice;
    private Double discount;
    private Double salePrice;
    private Integer quantity;
    private Double lineTotal;
    private Integer checksum;

    // All args constructor
    public Transaction(String billId, String itemCode, Double internalPrice, Double discount, Double salePrice, Integer quantity, Double lineTotal, Integer checksum) {
        this.billId = billId;
        this.itemCode = itemCode;
        this.internalPrice = internalPrice;
        this.discount = discount;
        this.salePrice = salePrice;
        this.quantity = quantity;
        this.lineTotal = lineTotal;
        this.checksum = checksum;
    }

    // Setters and Getters
    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public Double getInternalPrice() {
        return internalPrice;
    }

    public void setInternalPrice(Double internalPrice) {
        this.internalPrice = internalPrice;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(Double lineTotal) {
        this.lineTotal = lineTotal;
    }

    public Integer getChecksum() {
        return checksum;
    }

    public void setChecksum(Integer checksum) {
        this.checksum = checksum;
    }

    // To string method to print the transaction details properly
    @Override
    public String toString() {
        return "Transaction{" +
                "billId='" + billId + '\'' +
                ", itemCode='" + itemCode + '\'' +
                ", internalPrice=" + internalPrice +
                ", discount=" + discount +
                ", salePrice=" + salePrice +
                ", quantity=" + quantity +
                ", lineTotal=" + lineTotal +
                ", checksum=" + checksum +
                '}';
    }
}
