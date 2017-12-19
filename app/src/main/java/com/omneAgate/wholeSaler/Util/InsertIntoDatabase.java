package com.omneAgate.wholeSaler.Util;

import android.content.Context;

import com.omneAgate.wholeSaler.DTO.MessageDto;

/**
 * Created by user1 on 7/3/15.
 */
public class InsertIntoDatabase {

    Context context;

    public InsertIntoDatabase(Context context) {
        this.context = context;
    }


    public void insertIntoDatabase() {


        insertDbErrors(1000, "Access Denied", " அணுகல் மறுக்கப்பட்டது  ");

        insertDbErrors(1001, "Password mismatch", " கடவுச்சொல் பொருத்தமில்லை   ");

        insertDbErrors(1002, "Incorrect UserName or Password", "  தவறான பயனர் பெயர் அல்லது கடவுச்சொல்");

        insertDbErrors(2000, "Internal error. Please try again", "உள்ளீட்டு பிழை. மீண்டும் முயற்சிக்கவும்");

        insertDbErrors(5004, "Device inactive", "சாதனம் செயலற்ற நிலையில் உள்ளது");

        insertDbErrors(5017, "Invalid Device", "தவறான சாதனம்");

        insertDbErrors(6006,"Not a Whole Sale User","மொத்த விற்பணையாளர் இல்லை");

        insertDbErrors(6007,"Duplicate Reference No","அதே குறிப்பு எண் உள்ளது");

        insertDbErrors(5055,"Device is active","சாதனம் செயல்படுத்தப்ட்டுவிட்டது");

        insertDbErrors(5056, "Device is Registered already", "சாதனம் ஏற்கனவே பதிவு செய்யப்பட்டு விட்டது");

        insertDbErrors(5057, "Device is unassociated", "சாதனம் இணைக்கப்படவில்லை");

        insertDbErrors(8013,"Invalid Wholesaler","தவறான மொத்த விற்பனையாளர்");







      /*  insertDbErrors(2001, " Input error", "உள்ளீடு பிழை ");

        insertDbErrors(3001, " Invalid parameter ", "   தவறான அளவுரு     ");

        insertDbErrors(3002, "Invalid parameter format", "  தவறான அளவுரு வடிவம்  ");

        insertDbErrors(3003, "Not Null State.name cannot be null", "மாநிலத்தின் பெயர் இல்லை");

        insertDbErrors(3004, "State name should not be alphanumeric", "மாநில பெயரில் எண்கள் உள்ளது");

        insertDbErrors(3005, "State name should not be numeric", "மாநில பெயரில் எண்கள் உள்ளது");

        insertDbErrors(3006, " State Id cannot be null", "மாநிலத்தின் ஐடி இல்லை");

        insertDbErrors(3007, "StateCode cannot be null", "மாநிலத்தின் குறியீடு இல்லை");

        insertDbErrors(3008, "Godown Id cannot be null", "கிடங்கின் ஐடி இல்லை");

        insertDbErrors(3009, "Godown Code cannot be null", "கிடங்கின் குறியீடு இல்லை");

        insertDbErrors(3010, "Godown Name cannot be null", "கிடங்கின் பெயர் இல்லை");

        insertDbErrors(3011, "Product Name cannot be null", "பொருளின் பெயர் இல்லை");

        insertDbErrors(3012, "Product Id cannot be null", "பொருளின் ஐடி இல்லை");

        insertDbErrors(3013, "Product Code cannot null", "பொருளின் குறியீடு இல்லை");

        insertDbErrors(3014, "Given District ID is null", "மாவட்ட ஐடி இல்லை");

        insertDbErrors(3015, "Given District ID is already exist", "மாவட்ட ஐடி ஏற்கனவே உள்ளது");

        insertDbErrors(3016, "Given Language ID or Code is null", "மொழியின் ஐடி இல்லை");

        insertDbErrors(3017, "Data not found for GodownStockOutward", "கிடங்கு பொருள் வெளியீடு விவரங்கள் இல்லை");

        insertDbErrors(3018, "Given Request is Null", "உள்ளீடு பிழை ");

        insertDbErrors(3019, "FPS Stock is null", "கடையில் பொருள்கள் இல்லை");

        insertDbErrors(3020, "Duplicate Entry of Version", "பதிப்பு விவரம் ஏற்கனவே உள்ளிடப்பட்டுவிட்டது");

        insertDbErrors(3021, "Data not found for version Upgrade", "பதிப்பு மேம்படுத்தும்விவரம் இல்லை");

        insertDbErrors(3022, "Data not found for FPS Stock", "கடையில் பொருள் அளவு விவரங்கள் இல்லை");

        insertDbErrors(3023, "Data not found for GodownStockOutward", "கிடங்கு பொருள் வெளியீடு விவரங்கள் இல்லை");

        insertDbErrors(3024, "Data not found in GodownStock", "கிடங்கு அளவு விவரங்கள் இல்லை");

        insertDbErrors(3025, "Data not found in StockAlert", "அளவு சமிக்ஞை விவரங்கள் இல்லை");

        insertDbErrors(3026, "Duplicate entry for StockAlert", "அளவு சமிக்ஞை ஏற்கனவே உள்ளது");

        insertDbErrors(3027, "FPS Indent Data is null", "கடை உள்ளீட்டூ விவரங்கள் இல்லை");

        insertDbErrors(3028, "Godown Id or FPS ID is null", "கடை/ கிடங்கு ஐடி இல்லை");

        insertDbErrors(3029, "Fps Id cannot be null", "கடை ஐடி இல்லை");

        insertDbErrors(3030, "Given Product & village ID is already exist", "பொருள் ஐடி ஏற்கனவே உள்ளது");

        insertDbErrors(3031, "Data is not available in ProductPrice Entity", "பொருள் விலை விவரங்கள் இல்லை");

        insertDbErrors(3032, "Duplicate Entry in CardType Entity", "அட்டை வகை ஏற்கனவே உள்ளது");

        insertDbErrors(3033, "village code must be in String", "கிராம குறியீடு எழுத்தில் மட்டும் உள்ளிடவும்");

        insertDbErrors(3034, "District name between 2 and 45 characters, start with an alphabet, and may contain only alphabet, space", "மாவட்ட பெயர் 2 முதல் 45 எழுத்தில் மட்டும் இருக்க வேண்டும்");

        insertDbErrors(3035, "FPS Store Can't be Duplicated", "நியாய விலை கடையை நகல் எடுக்க முடியாது");

        insertDbErrors(3036, "FPS store can't be null", "நியாய விலை கடை விவரங்கள் இல்லை");

        insertDbErrors(3037, "please provide a numeric value for code", "குறியீட்டில் எண்ணை உள்ளிடவும்");

        insertDbErrors(3038, "There is a system error please try after some time", "சாதன பிழை. சிறிது நேரம் கழித்து முயற்சிக்கவும்");

        insertDbErrors(3039, "Godown Name  or Code should not contain any numeric value", "கிடங்கு பெயரில் எண் இருக்க கூடாது");

        insertDbErrors(3040, "godown code cannot be duplicate", "கிடங்கு குறியீடு ஏற்கனவே உள்ளது");

        insertDbErrors(3041, "User name already present please choose another username", "பயணர் பெயர் ஏற்கனவே தேர்வு செயப்பட்டுவிட்டது");

        insertDbErrors(3042, "username should be an Email Id", "பயணர் பெயர் மின்னஞ்சல் முகவரியாக இருக்க வேண்டும்");

        insertDbErrors(3043, "The state code is already present please choose a another state Code", "மாநில குறியீடு ஏற்கனவே தேர்வு செயப்பட்டுவிட்டது");

        insertDbErrors(3044, "The District code already present please ", "மாவட்ட  குறியீடு ஏற்கனவே தேர்வு செயப்பட்டுவிட்டது");

        insertDbErrors(3045, "Username Or UserId cannot be blank", "பயணர் விவரம் காலியாக இருக்க கூடாது");

        insertDbErrors(4000, " Generic database error", " பொதுவான தரவுத்தளம் தகவல் பிழை");

        insertDbErrors(9001, "MISSING_PARAMETER", "உள்ளீடு பிழை ");

        insertDbErrors(5001, " Card Not present", " அட்டை இல்லை");

        insertDbErrors(5002, " Beneficiary exceeded entitlement", "பயனாளியின் ஒதுக்கீடு அளவு மீறப்பட்டுவிட்டது");

        insertDbErrors(5003, "Card Blocked", "அட்டை முடக்கப்பட்டுள்ளது");

        insertDbErrors(5004, "Device inactive", "சாதனம் செயலற்ற நிலையில் உள்ளது");

        insertDbErrors(5005, "FPS Store invalid", "தவறான நியாய விலை கடை");

        insertDbErrors(5006, "Device ID null", "சாதன எண் இல்லை");

        insertDbErrors(5007, "Id cannot be null", "ஐடி இல்லை");

        insertDbErrors(5008, "UFC code is null in Beneficiary", "பயனாளிக்கான தனி குறியீட்டு எண் இல்லை");

        insertDbErrors(5009, "Data not found for Bill", "பில் விவரங்கள் இல்லை");

        insertDbErrors(5010, "Data not found for Product", "பொருட்கள் விவரங்கள் தவறு");

        insertDbErrors(5011, "Data not found for Beneficiary", "பயனாளி விவரங்கள் இல்லை");

        insertDbErrors(5012, "Data not found for CardType", "அட்டை வகை  தவறு");

        insertDbErrors(5013, "Data not found for UserDetail", "பயனாளி விவரங்கள் இல்லை");

        insertDbErrors(5014, "Data not found for FPS Store", "நியாய விலை கடை  விவரங்கள் இல்லை");

        insertDbErrors(5015, "Data not found for Transaction", "பரிவர்த்தனை விவரங்கள் இல்லை");

        insertDbErrors(5016, "Invalid request. Please login with Admin credentials", "தவறான கோரிக்கை. நிர்வாகச் சான்றுகளை கொண்டு உள்நுழையவும்");

        insertDbErrors(5017, "Invalid Device", "தவறான சாதனம்");

        insertDbErrors(5018, "FPS Device id should not be empty", "சாதன எண் இல்லை");

        insertDbErrors(5019, "Unable to connect to database", "தரவு தளத்துடன் தொடர்பு கொள்ள இயலவில்லை");

        insertDbErrors(5020, "QR Code not present", "க்யூஆர் குறியீடு இல்லை");

        insertDbErrors(5021, "Entitlement mismatch", "ஒதுக்கீடு தவறாக உள்ளது");

        insertDbErrors(5022, "Entitlement mismatch", "ஒதுக்கீடு தவறாக உள்ளது");

        insertDbErrors(5023, "QR Card Blocked", "க்யூஆர் குறியீடு முடக்கப்பட்டுள்ளது");

        insertDbErrors(5024, "Device is Blocked/Inactive .Please contact helpdesk ", "சாதனம் உபயோகத்தில் இல்லை");

        insertDbErrors(5025, "FPS Store Blocked/Inactive", "நியாய விலை கடை  உபயோகத்தில் இல்லை ");

        insertDbErrors(5026, "Stock Not available ", "பொருட்களின் கையிருப்பு  கிடைக்கவில்லை ");

        insertDbErrors(5027, "Entitlement not available", "பயனாளியின்  விவரங்கள் இல்லை ");

        insertDbErrors(5028, "OTP expired", "ஓடிபி நேரம் முடிவடைந்தது");

        insertDbErrors(5029, "No valid OTP available for beneficiary", "ஓடிபி பயனாளிக்கு உருவாக்க முடியவில்லை");

        insertDbErrors(5030, "RMN Not registered in FPS.Please contact helpdesk to register RMN", "அலைபேசி எண் பதிவு செய்யப்படவில்லை. உதவி மையத்தை அணுகவும்");

        insertDbErrors(5031, "Stock Not available", " கையிருப்பு  கிடைக்கவில்லை ");

        insertDbErrors(5032, "Beneficiary family details not found.", "பயனாளர் குடும்ப விவரங்கள் இல்லை");

        insertDbErrors(5033, "Invalid update stock request.", "தவறான மேம்படுத்துதல் கோரிக்கை");

        insertDbErrors(5034, "Invalid FPSStock for update", "கடைக்கான தவறான மேம்படுத்துதல் கோரிக்கை");

        insertDbErrors(5035, "Mobile Number is mandatory for beneficiary registration", "பயனாளியை பதிவு செய்ய அலைபேசி எண் அவசியம்");

        insertDbErrors(5036, "Beneficiary is active", "பயனாளி அட்டை  செயல்படுத்தபட்டுவிட்டது");

        insertDbErrors(5037, "Mobile number is already registered to another beneficiary", "அலைபேசி எண் மற்றோர் பயனாளிக்கு பதிவு செய்யப்பட்டு உள்ளது");

        insertDbErrors(5038, "Old card details already available", "பழைய அட்டை விவரங்கள் ஏற்கனவே உள்ளது");

        insertDbErrors(5039, "Input request is invalid", "உள்ளீட்டு விவரங்கள் தவறு");

        insertDbErrors(5040, "Bill details is empty", "பில் விவரங்கள் இல்லை");

        insertDbErrors(5041, "Bill item  details is invalid", "பில் உருப்படி தவறு");

        insertDbErrors(5042, "UFC Code in input is invalid", "தனிப்பட்ட குறியீட்டு எண் தவறு");

        insertDbErrors(5043, "Device id input is invalid", "சாதன எண் தவறு");

        insertDbErrors(5044, "Mobile Number input is invalid", "அலைபேசி எண் தவறு");

        insertDbErrors(5045, "Old card number input is invalid", "பழைய அட்டை விவரங்கள் தவறு");

        insertDbErrors(5046, "Card type  input is invalid", "அட்டை வகை உள்ளீடு தவறு");

        insertDbErrors(5047, "Cylinder number input is invalid", "சிலிண்டர் எண் தவறு");

        insertDbErrors(5048, "SMS service unavailable", "குருஞ்செய்தி அனுப்ப இயலாது");

        insertDbErrors(5049, "Requested service unavailable. Please try again", "இணைப்பில் கோளாறு உள்ளது. மீண்டும் முயற்சிக்கவும்");

        insertDbErrors(5050, "Allotment details unavailable for card type", "இந்த அட்டை வகைக்கு ஒதுக்கீட்டு விவரங்கள் இல்லை");

        insertDbErrors(5051, "Age group details unavailable", "வயது சம்பந்தமான குழுக்கள் இல்லை்");

        insertDbErrors(5052, "Product details not available", "பொருட்களுக்கான விவரங்கள் இல்லை");

        insertDbErrors(5053, "Transaction Request can not be null", "பரிவர்த்தனை விவரங்கள் இல்லை");

        insertDbErrors(5054, "Unsupported transaction type", "தவறான பரிவர்த்தனை வகை");

        insertDbErrors(5055, "Device is inactive", "சாதனம் செயலற்ற நிலையில் உள்ளது");

        insertDbErrors(5056, "Device is Registered already", "சாதனம் ஏற்கனவே பதிவு செய்யப்பட்டு விட்டது");

        insertDbErrors(5057, "Device is unassociated", "சாதனம் இணைக்கப்படவில்லை");

        insertDbErrors(5058, "Invalid update stock request", "தவறான மேம்படுத்துதல் கோரிக்கை");

        insertDbErrors(5059, "Public key is not registered", "பொது திறவுகோல் இல்லை");

        insertDbErrors(5060, "Master key is not registered", "முக்கிய திறவுகோல் இல்லை");

        insertDbErrors(5061, "Error in Encryption", "குறியாக்கத்தில் பிழை");

        insertDbErrors(5062, "Device not associated with user", "சாதனம் இணைக்கப்படவில்லை");

        insertDbErrors(5063, "Device not registered", "சாதனம் பதிவு செய்யப்படவில்லை");

        insertDbErrors(5064, "Master Key already exists in the Database", "முக்கிய திறவுகோல் ஏற்கனவே உள்ளது");

        insertDbErrors(5065, "Master Key String is not valid", "முக்கிய திறவுகோல் தவறு");

        insertDbErrors(5066, "Master Key not exist", "முக்கிய திறவுகோல் இல்லை");

        insertDbErrors(5067, "Error otp not generated", "ஒரு முறை கடவுசொல் உருவாக்கப்படவில்லை");

        insertDbErrors(5068, "Incorrect otp", "தவறான ஒரு முறை கடவுசொல் ");

        insertDbErrors(5069, "Registration Request not available for ration card number", "அட்டை எண்ணுக்கான பதிவு இல்லை");

        insertDbErrors(5070, "Card type not available for ration card number", "அட்டை விவரம் இல்லை");

        insertDbErrors(5071, "Request for card number available", "ரேசன் எண் ஏற்கனவே உள்ளது");

        insertDbErrors(5072, "Request with mobile number already available", "அழைப்பேசி எண் ஏற்கனவே பதிவு செய்யப்பட்டுள்ளது");

        insertDbErrors(5073, "Invalid card number format", "தவறான ரேசன் எண் முறை");

        insertDbErrors(5074, "Ufc is already associated", "இந்த குறியீடு ஏற்கனவே  பதிவு செய்யப்பட்டுள்ளது");

        insertDbErrors(5075, "Ufc does not belong to fps id", "இந்த குறியீடு இந்த கடையை சார்ந்தது இல்லை");

        insertDbErrors(5076, "Ufc is blocked", "இந்த குறியீடு முடக்கப்பட்டுள்ளது");

        insertDbErrors(5077, "Ufc not available in free pool", "இந்த குறியீடு நினைவத்தில் இல்லை");

        insertDbErrors(5078, "SMS input is in invalid format", "தவறான குருஞ்செய்தி வடிவம்");

        insertDbErrors(5079, "Provider details for sms txn for android not available", "குருஞ்செய்தி விநியோகஸ்தர் விவரங்கள் இல்லை");

        insertDbErrors(5080, "Ration card number input should not be null", "ரேசன் எண் உள்ளீடு காலியாக உள்ளது");

        insertDbErrors(5081, "Ration card Image should not be empty", "ரேசன் புகைப்படம் காலியாக உள்ளது");

        insertDbErrors(5082, "Registration Request not available for ration card number", "அட்டை எண்ணுக்கான பதிவு இல்லை");

        insertDbErrors(5083, "Beneficiary is already associated to another fps store", "பயனாளி வேறு கடைக்கு இணைக்கப்பட்டு விட்டார்");

        insertDbErrors(5084, "UFC is assigned to all Beneficiary", "இந்த குறியீடு வேறு பயனாளிக்கு ஒதுக்கப்பட்டுவிட்டது");

        insertDbErrors(5094, "Otp  Invalid (Or)Timeout (or) Used", "ஒரு முறை கடவுசொல் உருவாக்கப்படவில்லை");

        insertDbErrors(5095, "User is not associated to the given device", "நீங்கள் இந்த சாதனத்தின் பயனர் அல்ல");
*/
    }


    private void insertDbErrors(int errorCode, String description, String localDescription) {
        MessageDto messages = new MessageDto();
        messages.setLanguageCode(errorCode);
        messages.setDescription(description);
        messages.setLocalDescription(localDescription);
        WholesaleDBHelper.getInstance(context).insertErrorMessages(messages);
    }

}
