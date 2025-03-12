# MDD (Monde de Dév) - A Social Network for Developers

MDD is a social networking application designed for developers.
Users can register, subscribe to programming topics, publish articles, and engage with the community through comments.  

## Features

- **User Authentication**: Secure login and registration with JWT authentication and refresh tokens.  
- **Personalized Feed**: Users see articles based on their subscribed topics.  
- **Topic Management**: Subscribe/unsubscribe to programming-related topics.  
- **Content Creation**: Write articles and post comments.  
- **User Profile**: Manage username, email, and subscriptions.  

## Tech Stack

- Angular 18
- Spring Boot 3.4.2
- Java 17
- MySQL 8

## Installing and Running the application

1. Clone the repository

```
git clone https://github.com/arnaud-romil/mdd-mvp.git
```

2. Install the database

* Ensure MySQL is installed on your machine.

* Create a databse : ``` CREATE DATABASE mdd_db; ```

* Create a user with the necessary privileges:
```
CREATE USER 'mdd_user'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON mdd_db.* TO 'mdd_user'@'localhost';
FLUSH PRIVILEGES;
```

* Verify that the connection details in ```mdd-mvp/back/mdd-api/src/main/resources/application.properties``` are correct:
```
spring.datasource.url=jdbc:mysql://localhost:3306/mdd_db
spring.datasource.username=mdd_user
spring.datasource.password=password
```

* Run the following sql scripts: ```mdd-mvp/back/mdd-api/src/main/resources/schema.sql``` (the database schema) and ```mdd-mvp/back/mdd-api/src/test/resources/data.sql``` (to populate the database).

3. Generate JWT keys using openssl and place them inside the ```mdd-mvp/back/mdd-api/src/main/resources``` folder:
```
# Generate private key
openssl genpkey -algorithm RSA -out app.key -pkeyopt rsa_keygen_bits:2048

# Extract public key
openssl rsa -pubout -in app.key -out app.pub
```

4. Install Maven dependencies for the back-end
```
cd mdd-mvp/back/mdd-api
mvn clean install
```

5. Install npm dependencies
```
cd mdd-mvp/front/mdd-ng
npm install
```

6. Start the Backend: Ensure MySQL is running and start the Spring Boot Server
```
cd mdd-mvp/back/mdd-api
mvn spring-boot:run
```
The API will be available at ```http://localhost:3000/api```

7. Start the Frontend
```
cd mdd-mvp/front/mdd-ng
ng serve
```
The application will be accessible at ```http://localhost:4200```

## Best Practices

**Secure JWT Keys**: Ensure private keys are never exposed publicly.

## Licence

This project is the property of **ORION**.
