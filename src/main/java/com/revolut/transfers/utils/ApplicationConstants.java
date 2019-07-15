package com.revolut.transfers.utils;

public class ApplicationConstants {
    public static final String US_CURRENCY_CODE = "USD";
    public static final String UK_CURRENCY_CODE = "GBP";
    public static final String APPLICATION_JSON = "application/json";

    public static final String BASE_PATH = "/transactions/accounts/";

    public static final String ACCOUNT_ID_PATH = ":accountId";

    //----------END POINTS------------------//

    public static final String GET_ACCOUNT_SERVICE_END_POINT=BASE_PATH + ACCOUNT_ID_PATH;
    public static final String ACCOUNT_WITHDRAWAL_SERVICE_END_POINT=GET_ACCOUNT_SERVICE_END_POINT+"/withdrawals";
    public static final String ACCOUNT_DEPOSIT_SERVICE_END_POINT=GET_ACCOUNT_SERVICE_END_POINT+"/deposits";
    public static final String ACCOUNT_TRANSFER_SERVICE_END_POINT=BASE_PATH+":sourceAccountId/:targetAccountId/transfers";
    public static final String GET_ACCOUNT_HISTORY_SERVICE_END_POINT=GET_ACCOUNT_SERVICE_END_POINT + "/history" ;

}
