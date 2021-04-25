# SearchLight-Health
# Java 11
#Run mvn clean install
Start the application using Starter.java class.
The application is using inmemory DB.
The application will initialize DB with two stores information(for reference check DataInitializer).
Use below curl to add a new store. 

curl --location --request POST 'http://localhost:8080/store/add' \
--header 'Content-Type: application/json' \
--header 'Accept: application/json' \
--data-raw '{  
   "lat": 19.98675,  
   "longi": 9.38675,  
   "name": "Test",  
   "products": [  
     {  
       "name": "Test",  
       "noOfItems": 1,  
       "price": 1  
     }  
   ],  
   "shipmentTime": "03:00"
 }'
 
And to fetch the corresponding store based on input, use below curl 

curl --location --request GET 'http://localhost:8080/store/getStores' \
--header 'Content-Type: application/json' \
--header 'Accept: application/json' \
--data-raw '{  
   "lat": 19.426940,  
   "longi": 9.38990,  
   "productNames": [  
     "Test"  
   ]  
 }' 
 
A shedular imlementation is also there which will fetch the no of items sold in offline mode.
This schedular isn't effective till we have a proper other service to fetch the data 
