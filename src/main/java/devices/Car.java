package devices;

public class Car {

    private String carId;
    private String appointmentId;
    private String make;
    private String model;
    private String variant;
    private String fuelType;
    private String isScrap;
    private int odometerReading;
    private String ownerNumber;
    private String year;
    private int tp;
    private String bidAmount;
    private String city;
    private String ocbType;
    private int requestedC24Quote;
    private int requestedHb;
    private boolean isOcbAccept;
    private boolean isOcbReject;
    private boolean isUnnatiSanctioned;
    private int rcRsd;
    private int serviceFee;
    private int transportationCharge;
    private int unnatiLoanAmount;
    private int unnatiProcessingFees;
    private int discount;
    private int totalPrice;

    /*this constructor is for easy debugging. Sometimes you dont need to create a new car and you want to continue a flow for already
    created car, so use this.
     */

    public Car(String appointmentId, String carId) {
        this.appointmentId = appointmentId;
        this.carId = carId;
    }

    public Car(String appointmentId, String carId, String make, String model,
               String variant, String fuelType, String isScrap, int odometerReading, String ownerNumber, String year, int tp, String city) {
        this.appointmentId = appointmentId;
        this.carId = carId;
        this.make = make;
        this.model = model;
        this.variant = variant;
        this.fuelType = fuelType;
        this.isScrap = isScrap;
        this.odometerReading = odometerReading;
        this.ownerNumber = ownerNumber;
        this.year = year;
        this.tp = tp;
        this.city = city;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getIsScrap() {
        return isScrap;
    }

    public void setIsScrap(String isScrap) {
        this.isScrap = isScrap;
    }

    public int getOdometerReading() {
        return odometerReading;
    }

    public void setOdometerReading(int odometerReading) {
        this.odometerReading = odometerReading;
    }

    public String getOwnerNumber() {
        return ownerNumber;
    }

    public void setOwnerNumber(String ownerNumber) {
        this.ownerNumber = ownerNumber;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getTp() {
        return tp;
    }

    public void setTp(int tp) {
        this.tp = tp;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(String bidAmount) {
        this.bidAmount = bidAmount;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public String getOcbType() {
        return ocbType;
    }

    public void setOcbType(String ocbType) {
        this.ocbType = ocbType;
    }

    public int getRequestedC24Quote() {
        return requestedC24Quote;
    }

    public void setRequestedC24Quote(int requestedC24Quote) {
        this.requestedC24Quote = requestedC24Quote;
    }

    public int getRequestedHb() {
        return requestedHb;
    }

    public void setRequestedHb(int requestedHb) {
        this.requestedHb = requestedHb;
    }

    public void setIsUnnatiSanctioned(boolean isUnnatiApplied) {
        this.isUnnatiSanctioned = isUnnatiApplied;
    }

    public boolean getIsUnnatiSanctioned() {
        return isUnnatiSanctioned;
    }
    public int getRcRsd() {
        return rcRsd;
    }

    public void setRcRsd(int rcRsd) {
        this.rcRsd = rcRsd;
    }

    public int getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(int serviceFee) {
        this.serviceFee = serviceFee;
    }

    public int getTransportationCharge() {
        return transportationCharge;
    }

    public void setTransportationCharge(int transportationCharge) {
        this.transportationCharge = transportationCharge;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getUnnatiLoanAmount() {
        return unnatiLoanAmount;
    }

    public void setUnnatiLoanAmount(int unnatiLoanAmount) {
        this.unnatiLoanAmount = unnatiLoanAmount;
    }

    public int getUnnatiProcessingFees() {
        return unnatiProcessingFees;
    }

    public void setUnnatiProcessingFees(int unnatiProcessingFees) {
        this.unnatiProcessingFees = unnatiProcessingFees;
    }

    public boolean getIsOcbAccept() {
        return isOcbAccept;
    }

    public void setIsOcbAccept(boolean isOcbAccept) {
        this.isOcbAccept = isOcbAccept;
    }

    public boolean getIsOcbReject() {
        return isOcbReject;
    }

    public void setOcbReject(boolean ocbReject) {
        isOcbReject = ocbReject;
    }
}
