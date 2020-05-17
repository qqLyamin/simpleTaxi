package utils

class rideInfo {

    var id : Int = 0

    var startAddressCity : String = ""
    var startAddressAddress : String = ""

    var endAddressCity : String = ""
    var endAddressAddress : String = ""

    var priceRideAmount : Int = 0
    var priceRideCurrency : String = ""

    var orderTime : String = "1970-01-01 00:00:00 GMT"

    var regNumber: String = ""
    var modelName: String = ""
    var photo: String = ""
    var driverName: String = ""

//////////////////////////////////////////////////////////////
    fun setRideId(id : Int) {
        this.id = id
    }

    fun setStartAddress(city : String, address : String) {
        this.startAddressCity = city
        this.startAddressAddress = address
    }

    fun setEndAddress(city : String, address : String) {
        this.endAddressCity = city
        this.endAddressAddress = address
    }

    fun setPriceAmountAndCurrency(amount : Int, currency: String) {
        this.priceRideAmount = amount
        this.priceRideCurrency = currency
    }

    fun setRideOrderTime(orderTime : String){
        this.orderTime = orderTime
    }

    fun setRideVehicleInfo(regNumber : String, modelName : String, photo : String, driverName : String) {
        this.regNumber = regNumber
        this.modelName = modelName
        this.photo = photo
        this.driverName = driverName
    }
}