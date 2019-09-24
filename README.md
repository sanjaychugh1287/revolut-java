# How to run

You can download package here:
https://github.com/dragonfly799/revolut-test/releases/download/v1.0/RevolutTest.jar

To run it, execute in command line:
`java -jar RevolutTest.jar`

Server will be run on localhost:8080.

# API documentation
All responses are in JSON format.

## Create new account

`POST http://localhost:8080/accounts/`

Response example:
```
201 CREATED
{
    "id": 1,
    "amount": 0
}
```

## Get account info

`GET http://localhost:8080/accounts/<accountId>`

Response example:
```
200 OK
{
    "id": 1,
    "amount": 0
}
```

## Add money to the account

`POST http://localhost:8080/accounts/<accountId>/add?amount=<amountOfMoney>`

Response example:
```
202 ACCEPTED
```
There is no response body, because this operation can be queued.

## Transfer money to another account

`POST http://localhost:8080/accounts/<accountId>/transfer?to=<anotherAccountId>&amount=<amountOfMoney>`

Response example:
```
202 ACCEPTED
```
There is no response body, because this operation can be queued.
