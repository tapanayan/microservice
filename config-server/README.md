# Config-server

This is a sample Java / Gradle Buildship 2x / Spring Boot (version 1.5.6) application demonstrating config client

## How to Run 
* Clone this repository 
* Make sure you are using JDK 1.8 and Gradle 3.x
* Once successfully built, you can run the service as mentioned below:
```
       gradle bootRun
```
* Check the stdout to make sure no exceptions are thrown

Once the application runs you should see something like this

```
Started *Application in 12.87 seconds (JVM running for 13.83)
```

## About the Service

For any change in the properties file (shoppingcart.properties) to reflect we need to do a POST on http://localhost:8981/refresh 
The service is as below,
* GET http://localhost:8981/rest/message 



# About Spring Boot

Spring Boot is an application bootstrapping framework that makes it easy to create new RESTful services (among other types of applications). It provides many of the usual Spring facilities that can be configured easily usually without any XML. In addition to easy set up of Spring Controllers, Spring Data, etc. This Spring Boot application comes with the 

# Questions and Comments: shamsnezami@gmail.com






