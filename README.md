# Vertx, OpenApi and SpringBoot Example

## Compile and run

```bash
mvn clean package
java -jar ./target/MyVertxOpenAPIWebService-1.0.0.jar
```

Then post a transaction with `curl`:

#Create
```bash
curl --location --request POST 'http://localhost:8080/api/customer' \
--header 'Content-Type: application/json' \
--data-raw '{
    "id": "1",
    "firstName": "John",
    "lastName": "Doe"
}'
```

#Get all customers
```bash
curl --location --request GET 'http://localhost:8080/api/customer'
```

#Delete
```bash
curl --location --request DELETE 'http://localhost:8080/api/customer/1'
```

#Update
```bash
curl --location --request PUT 'http://localhost:8080/api/customer/1' \
--header 'Content-Type: application/json' \
--data-raw '{
    "id": "2",
    "firstName": "Jane",
    "lastName": "Doe"
}'
```