public class SampleRowObject {
    String orderDate;
    String region;
    String rep1;
    String rep2;
    String item;
    String units;
    String unitCost;
    String total;

    public SampleRowObject(String orderDate, String region, String rep1, String rep2, String item, String units,
            String unitCost, String total) {
        this.orderDate = orderDate;
        this.region = region;
        this.rep1 = rep1;
        this.rep2 = rep2;
        this.item = item;
        this.units = units;
        this.unitCost = unitCost;
        this.total = total;
    }
}
