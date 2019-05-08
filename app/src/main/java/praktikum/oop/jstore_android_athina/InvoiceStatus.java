package praktikum.oop.jstore_android_athina;

public enum InvoiceStatus
{
    PAID("Paid"),
    UNPAID("Unpaid"),
    INSTALLMENT("Installment");

    private String invoiceStatus;

    InvoiceStatus (String invoiceStatus)
    {
        this.invoiceStatus = invoiceStatus;
    }

    public String toString()
    {
        return invoiceStatus;
    }
}
