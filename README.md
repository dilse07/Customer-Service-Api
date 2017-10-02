# Customer Service Api
## **Introduction:**

This is a guideline on how a Customer Service API is designed and implemented for outlined use cases. The design principles and guidelines are explained further in the below sections to better understand its functionality and usage.

* [Scope](#scope)
* [Use Cases](#use-cases)
* [Assumptions](#assumptions)
* [Design Guidelines-Customer Service API](#design-guidelines-customer-service-api)
* [Key Design Considerations](#key-design-considerations)
* [Solution for Use Cases](#solution-for-use-cases)
* [Code Setup and Execution](#code-setup-and-execution)
* [Road Map Ahead](#road-map-ahead)


## **Scope**

This integration allows verified consumers to seamlessly retrieve and modify customer details based on different customer service request.

## **Use Cases**

1. A consumer may periodically (every 5 minutes) consume the API to enable it (the consumer) to maintain a copy of the provider API's customers (the API represents the system of record)
 
2. A mobile application used by customer service representatives that uses the API to retrieve and update the customers details
 
3. Simple extension of the API to support future resources such as orders and products

## **Assumptions**

- High-availability and scalability of API is not considered as the volumetrics is unknown.
- Assuming the api is exposed internally and the consumers are validated before entering into the network layer.
- The Customer Details are stored internally and the updated customer data will be available until Mule Server run-time.
- Thread Profiles not implemented in the API.
- Customer Service API is constructed as a light-weight component with simple complexity. So, PUT method is used for customer updates.
- Reliability of request is not part of this scope, hence not considered.

## **Design Guidelines-Customer Service API**

### **RAML Specification**

The APIs RAML specification is referenced under the name "customerService-api.raml" in project customerServiceApi.
With the use case clearly in place, the Restful API is defined with the constructed RAML which comprises of resourceTypes,Traits and schemas.

### **API endpoint**

An endpoint is provided to the API based on a single resource "customers".Any future resources will come under the main resource to extend its functionality.

http://localhost:8081/api/customers

### **HTTP methods (verbs)**

The important HTTP methods available in the API are as follows:

1. **GET** method requests data from the resource and should not produce any side effect.

	E.g: /customers returns list of all customers.
    
    /customers/1 returns the customer details of customerID '1'.
    
    Note: The updated list will be available after 10 seconds because of caching.    
2. **POST** method requests the server to create a customer resource.

	E.g: /customers creates a new customer. 
    
3. **PUT** method requests the server to update resource or create the resource, if it doesn’t exist.

	E.g. /customers/1 will request the server to update, or create if doesn’t exist.

4. **DELETE** method requests that the resources, or its instance, should be removed.

	E.g /customers/1 will request to delete resource from customer collection.
    
### **HTTP Response status codes**
The following are the important categorization of HTTP codes for Customer Service API:

_Successful Status Codes:_
- **200 Ok** The standard HTTP response representing success for GET, PUT or POST.
- **201 Created** This status code should be returned whenever the new instance is created. E.g on creating a new instance, using POST method, should always return 201 status code.

_Unsuccessful Status Codes:_
- **400 Bad Request** indicates that the request by the client was not processed, as the server could not understand what the client is asking for.
- **401 Unauthorized** indicates that the client is not allowed to access resources, and should re-request with the required credentials.
- **404 Not Found** indicates that the requested resource is not available now.
- **405 Method Not Allowed** indicates that the request method is known by the server but has been disabled and cannot be used.
- **406 Not Acceptable** indicates that a response matching the list of acceptable values defined in Accept-Charset and Accept-Language cannot be served.
- **415 Unsupported Media Type** indicates that the server refuses to accept the request because the payload format is in an unsupported format

_Server Error:_
- **500 Internal Server Error** indicates that the request is valid, but the server is totally confused and the server is asked to serve some unexpected condition.

### **Exception Strategy:**

_Messaging Exceptions:_

The message exceptions for Customer Service API are thrown for customerID which does not exist during a search or no customer data found in the system while trying to list customers. A message exception will automatically be caught in global exception strategy mappings and a revised message payload is sent as below example with 404 Not Found.

Eg:
{
    "message": "Resource not found"
}

_System Exceptions:_

During remote service failures or system failures, an system exception of 500 Internal Server Error is thrown to the client.

Eg:
{ "message": "Internal server error" }

## **Key Design Considerations**
During design phase, the below important decisions were taken.

_Re-usability:_

Since, there is always an opportunity to re-use the assets in an API, the resorce types and traits were pre-defined. Thereby, reducing redunt declarations.

_Extensibility:_

In order to future-proof and reduce the cost of re-work, the RAML API is defined in version 1.0 which addresses the issue of extending or adding new resources, hence, helping to broaden an API whenever a situation arises.

_Synchronization:_

To provide consumers to periodically retrieve customer details , getCustomers(GET /customers) operation implements caching. This reduces unwanted overhead on performance issues to the Mule servers.

_Important Traits:_

As consumers differ and with digital media transformations on the rise, a need to add traits with sorting and pagination helps minimize content data tranfer and quickly return the specified data. This is also one of the important use case to address mobile app consumption.

_Exception Handling:_

Exception trown during failure scenarios are captured in APIKIt's exception handler and a corresponding error message is returnde to the consumer to indicate the reason for the error. 

## **Solution for Use Cases**
1. A periodic sync of customers using the API.
	- To reduce API mesaage query execution through the entire mule flow, caching is adopted in the current design. This reduces unnecessary mule flow execution to remote system. 

	The concept of caching greatly increases business benefits as number of calls are reduced significantly and lower costs based on number of calls to a remote sytem like Mainframes.

2. A mobile application used by customer service representatives to retrieve and update customers details.
	- The API needs quick access to customer details which is achieved by implementing caching.

3. Simple extension of the API to support future resources such as orders and products.
	- Usage of RAML 1.0 empowers with re-use options, extensibility and flexibility. Some of the principle featured in the designs are resourceTypes, traits, security schemas, and reusable assets.
    
## **Code Setup and Execution**
_Prerequisites:_

- Anypoint Studio- Mulesoft ESB (version 3.8 or over)
- SOAP posting client like SOAPUI or Boomerang SOAP & Rest Client (Google Web Store)
- JDK1.7 or over

_Setup:_

These are the important steps to be followed to run and test Customer Service API in Anypoint Studio.

1. Import customerServiceApi project in Anypoint Studio by Importing it as an Anypoint Studio Project.
2. In the Package Explorer pane in Aunypoint Studio, right-click the project name, then select Run As --> Mule Application.
3. The application will be deployed and a API console will automatically open for testing.

_Test Cases:_
1. RequestType - get:/customers
   
   Request URL - http://localhost:8081/api/customers

   Request - N/A
   
   Response - 
[
  {
    "id": "1",
    "firstName": "Ethan",
    "lastName": "Joehanz",
    "address": "7 Lane Way, Sydney, NSW 2145, Australia"
  },
  {
    "id": "2",
    "firstName": "Darla",
    "lastName": "Pheona",
    "address": "23 Flower Street, Sydney, NSW 2140, Australia"
  },
  {
    "id": "3",
    "firstName": "Ben",
    "lastName": "Gallio",
    "address": "3 Two Street, Canberra, NSW 2020, Australia"
  }

   Status - 200

2. RequestType - post:/customers

   Request URL - http://localhost:8081/api/customers?accessToken=ACCESS_TOKEN
   
   Request -
   {
  "firstName": "George",
  "lastName": "Hunter",
  "address": "8 Techie Street, North Sydney, NSW 2060, Australia"
}

   Response - 
   {
  "message": "The customer has been succesfully created."
}

   Status - 201
 
3. RequestType - get:/customers/{customerID}

   Request URL - http://localhost:8081/api/customers/1
   
   Request - N/A
   
   Response -
   
   {
  "id": "1",
  "firstName": "Ethan",
  "lastName": "Joehanz",
  "address": "7 Lane Way, Sydney, NSW 2145, Australia"
}

   Status - 200
 
4. RequestType - put:/customers/{customerID}

   Request URL - http://localhost:8081/api/customers/3?accessToken=ACCESS_TOKEN
   
   Request - 
   {
  "firstName": "Juliet",
  "lastName": "Rachel",
  "address": "5 Turnip Street, Homebush West, NSW 2140, Australia"
}
   
   Response -  
   {
  "message": "The customer has been succesfully updated."
}

   Status - 201

5.  RequestType - 

	Request URL - http://localhost:8081/api/customers/3?accessToken=ACCESS_TOKEN
    
    Request - N/A
    
    Response - 
    {
  "message": "The customer has been succesfully deleted."
}

   Status - 201
   
## **Road Map Ahead**
The API is designed with the given use case and assumptions whereas it can be improved in upcoming versions.
- using Authentication Methods like basic authentication or OATH2.0 to increase access management of the API.
- usage of reliable storage components like Amazon S3 or databases.
- using reliable messaging within Mule ESB during system exceptions.
- Better exposure of API service using HTTPS protocol for wide-use. 
- API response needs to be more stricter and accurate for mobile apps hence sorting and paging traits are added.

