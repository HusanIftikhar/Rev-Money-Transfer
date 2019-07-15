# Rev-Money-Transfer
How to start and Test

1) To package : mvn clean package

2) To Run tests : mvn test

3)To run application

 Go inside target folder in project director
 java -jar money-transfer-1.0-SNAPSHOT-fat.jar
 
 
 Sample Request and Responses:
 
 Get Account by Id
http://localhost:8089/transactions/accounts/70001 (GET)
Response :
{
    "message": "Account Found",
    "status": 200,
    "result": {
        "accountId": 70001,
        "accountTitle": "Iftikhar Hussain",
        "availableBalance": "USD390.21"
    }
}

-------------------------------------------------------------------------------------------

GET Transaction History
http://localhost:8089/transactions/accounts/70001/history (GET)

Response:
{
    "message": "Total Transactions: 3",
    "status": 200,
    "result": [
        {
            "dateTime": "2019-07-15T04:00:44.666",
            "transactionType": "WITHDRAWAL",
            "transactionAmount": "USD20.00"
        },
        {
            "dateTime": "2019-07-15T04:00:44.774",
            "transactionType": "DEPOSIT",
            "transactionAmount": "USD20.00"
        },
        {
            "dateTime": "2019-07-15T04:00:46.557",
            "transactionType": "WITHDRAWAL",
            "transactionAmount": "USD20.00"
        }
    ]
}

-----------------------------------------------------------------------------------------------

Transfer Amount
http://localhost:8089/transactions/accounts/70001/70002/transfers (PUT)
Request :
{
"amount":30.00,
"currencyCode":"USD"

}

Response :
{
    "message": "Balance Transferred Successfully",
    "status": 200,
    "result": "SUCCESS"
}

------------------------------------------------------------------------------------------------

Withdrawal Amount
http://localhost:8089/transactions/accounts/70001/withdrawals (PUT)
Request :
{
    "amount": 139.789,
    "currencyCode": "USD"
}

Response :

{
    "message": "Withdrawal Successful",
    "status": 200,
    "result": "SUCCESS"
}


-------------------------------------------------------------------------------------------------
Deposit Amount
http://localhost:8089/transactions/accounts/70001/deposits  (PUT)
Request
{
    "amount": 139.789,
    "currencyCode": "USD"
}

Response:

{
    "message": "Deposit successful",
    "status": 200,
    "result": "SUCCESS"
}

-----------------------------------------------------------------------------------------------------

Please refer to performance-test-report.txt for load testing reports using apache bench mark tool
avaiable inside resources directory in project folder.










