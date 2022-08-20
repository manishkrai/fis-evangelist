# fis-evangelist
#Manish Rai

Case Study for FIS Evangelist program

This repository contains following projects -

	1. Book Microservice

	2. Subscription Microservice
	
	3. Service Registry (using Eureka)
	
	4. API Gateway
	
Below approaches are being used for inter communication between the Microservices
    
	1. Rest template
	
	2. Open Feign
	
Service names are being resolved using Service Registry for Feign calls.


Added JUNIT Test cases for service and controller layers for following projects -

	1. Book Microservice

	2. Subscription Microservice

Added Rest Assured Test cases for following service -

	1. Book Microservice

Added Postman call for following services -

	1. Book Microservice

	2. Subscription Microservice
	
Added circuit breaker using Resilience4j for following service -

	1. Subscription Microservice

Added Postman API Testing sample that can be directly uploaded into Postman and can be tested after running all 4 endpoints in below order -
	
	1. Service Registry
	
	2. API Gateway
	
	3. Book Microservice
	
	4. Subscription Microservice
