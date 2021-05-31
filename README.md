# Cab microservices 
    
| **side note**: *There were changes to the  original Hire assignment, in my opinion it fits better the way it's presented in this documentation some endpoints.*

This application has two parts: 
    * user services listenting on port 8081
    * cab services listening on port 8080

Cab drivers can send their location via a restful endpoint (it will send the request to kafka) or send it via kafka producer by using "cab_location" topic.

You can explore both services api documentation in the following url:

```
0.0.0.0:8080/api-docs

0.0.0.0:8081/api-docs 
```

Another way to explore this apis is by importing postman.json into your postman application, there are a lot of examples to follow along this cab application. Btw there are registries already imported into DB.

## building
First execute make to build spring boot microservices, once maven build finish then invoke docker-compose.
```
make &&
docker-compose -f docker-compose up -d
```

## usage

Please refer to api-docs for advanced documentation and explanations, this is only a quick guide.

### Create accounts 
In order to use this application, you must to create driver and customer

Creae a user:

```
Post: 0.0.0.0:9081/api/v1/users

Body: 
{
     "email":"customer@gmail.com"
}
```

Create a cab:
```
Post: 0.0.0.0:9080/api/v1/cabs

Body: 
 {
     "driverId":"cab9@gmail.com"
 }
```
### Update geo location 

Update geo for customer:
```
PUT 0.0.0.0:9081/api/v1/users/locations

Body:
 {
     "email": "customer@gmail.com",
      "geoLocation": 
          {
            "latitude": 20.091388298968315,
            "longitude": -98.80124459419224
           }
}
```

#### Update geo for cab
Note thar you can modify "available" node to exclude this cab from further operations. Body request is the same if you prefer to send it via kafka rather than consuming this endpoint.

```
PUT 0.0.0.0:9080/api/v1/cabs/locations

Body:
 {
     "driverId": "cab9@gmail.com",
     "available":true,
      "geoLocation": 
          {
            "longitude": 20.09511479751501,
            "latitude":  -98.80315844989903
           }
}
```
### Listing information

#### Retrieve geo location of user

In order to retrieve user's location please provide  user's email:
```
GET http://localhost:9081/api/v1/users/{email}/locations

Example:

GET http://localhost:9081/api/v1/users/customer@gmail.com/locations
```

#### Retrieve geo location of cab driver

In order to retrieve cab's location please provide  cab driver's email:
```
GET http://localhost:9081/api/v1/cabs/{email}/locations

Example:

GET http://localhost:9081/api/v1/cabs/driver2@gmail.com/locations
```


#### Retrieve all cabs

```
GET http://localhost:9080/api/v1/cabs
```

#### Retrieve near cabs
You can list all available cabs using customer's geo location by invoking following endpoint:

```
GET http://localhost:9080/api/v1/cabs/locations/{CUSTOMER_GEO_ID}?cabs={LIMIT_NEAR_CABS}

Example:
http://localhost:9080/api/v1/cabs/locations/106?cabs=5
```

Beware every time you update user's geo location you will get a new geo Id from response, this is the one used as reference.
