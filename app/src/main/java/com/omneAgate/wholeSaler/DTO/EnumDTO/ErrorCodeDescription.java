package com.omneAgate.wholeSaler.DTO.EnumDTO;


/**
 * The Enum ErrorCodeDescription.
 */
public enum ErrorCodeDescription {
    //Unauthorized access
    /**
     * The unauthorized user.
     */
    UNAUTHORIZED_USER(1000, "Access Denied"),

    /**
     * The password mismatch.
     */
    PASSWORD_MISMATCH(1001, "Password mismatch"),

    /**
     * The login validate.
     */
    LOGIN_VALIDATE(1002, "Incorrect UserName or Password"),
    //Cusine Exception Error Codes -11XX
    /** The cusine is empty. */

    // invalid parameter error codes - 3XXX
    /**
     * The invalid parameter.
     */
    INVALID_PARAMETER(3001, "invalid parameter"),

    /**
     * The invalid parameter format.
     */
    INVALID_PARAMETER_FORMAT(3002, "invalid parameter format"),

    // db exception error codes - 4XXX
    /**
     * The db error.
     */
    DB_ERROR(4000, "Generic database error"),
    // misc error codes - 9XXX
    /**
     * The missing parameter.
     */
    MISSING_PARAMETER(9001, "MISSING_PARAMETER"),

    MISSING_PARAMETER_ERROR(2001, "Input error"),


    /**
     * Beneficiary is inactive
     */
    ERROR_CARD_NOTPRESENT(101, "Card Not present"),

    /**
     * Beneficiary exceeded allotment error
     */
    ERROR_BENEFICIARY_EXCEEDEDALLOTMENT(103, "Beneficiary exceeded entitlement"),

    /**
     * Beneficiary not of the fps store
     */
    ERROR_FPSBENEFICIARY_MISMATCH(102, "QR code valid, Beneficiary does not belong to the particular FPS"),

    /**
     * Beneficiary is inactive
     */
    ERROR_BENEFICIARY_INVALID(104, "Card Blocked"),


    /**
     * FPSStore is inactive
     */
    ERROR_FPSSTORE_INVALID(106, "FPS Store inactive"),

    /**
     * Not enough stock in FPS store
     */
    ERROR_STOCK_NOTAVAILABLE(107, "Commodities stock unavailable"),

    /**
     * Not able to fetch beneficiary family details
     */
    ERROR_BENEFICIARYMEMBERS_NOTFOUND(108, "Beneficiary family details not found."),

    ERROR_INVALID_UPDATSTOCKREQUEST(109, "Invalid update stock request."),

    ERROR_FPSSTOCK_NOTAVAILABLE(110, "No valid FPSStock for update"),

    ERROR_NONADMIN_USER(201, "Invalid request. Please login with Admin credentials"),

    FPS_DEVICE_ID(202, "Device ID null"),

    ERROR_NOT_Null(301, "NOT Null. Id cannot be null"),

    ERROR_NOT_Null_StateUser(302, "Not Null State.name cannot be null"),

    ERROR_NOT_Null_StateId(303, "Not Null State Id.Id cannot be null"),

    ERROR_NOT_NULL_StateCode(304, "Not Null State Code.StateCode cannot be null"),

    ERROR_NOT_NUll_GodownId(401, "Not Null Godown Id.Godown Id cannot be null"),

    ERROR_NOT_NUll_GodownCode(402, "Not Null Godown Code.Godown Code cannot be null"),

    ERROR_NOT_Null_GodownName(403, "Not Null Godown Name.Godown Name cannot be null"),

    ERROR_NOT_Null_ProductName(404, "Not Null Product Name. Product Name cannot be null"),

    ERROR_NOT_Null_ProductId(405, "Not Null Product Id. Product Id cannot be null"),

    ERROR_NOT_Null_ProductCode(406, "Not Null Product Code.Product Code cannot null"),

    ERROR_NOT_WHOLESALE_USER(6006,"Not a Whole Sale User"),
    ERROR_DEVICE_INACTIVE(5004,"Device inactive") ,

    ERROR_DEVICE_INVALID(5017,"Invalid device"),

    ERROR_GENERIC(2000,"Internal Error. Please try again"),

    ERROR_DUPLICATE_REFERENCENO(6007,"Duplicate Reference No"),

    ERROR_DEVICE_ISACTIVE(5055,"Device is active"),

    /**	Device is active -checking in device registration service */
    ERROR_DEVICE_REGISTERED(5056,"Device is Registered already"),

    ERROR_DEVICE_UNASSOCIATED(5057,"Device is unassociated"),

    ERROR_Aadhaar_PRESENT(449, "Aadhar card number is already available"),

    ERROR_INVALID_WHOLESALER(8013,"Invalid Wholesaler");



    /**
     * The error code.
     */
    final private int errorCode;

    /**
     * The error description.
     */
    final private String errorDescription;

    /**
     * Instantiates a new error code description.
     *
     * @param code        the code
     * @param description the description
     */
    ErrorCodeDescription(int code, String description) {

        this.errorCode = code;
        this.errorDescription = description;
    }

    /**
     * Gets the error code.
     *
     * @return the error code
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * Gets the error description.
     *
     * @return the error description
     */
    public String getErrorDescription() {
        return errorDescription;
    }


}
