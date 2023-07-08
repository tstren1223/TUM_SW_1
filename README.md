Deliver and host your application
In this exercise, we are looking at a Spring Boot application that provides functionality to persist Persons and create parent-child relationships between different persons.

We will run this app locally with ngrok to make it accessible over the internet. To persist entities we will use a Postgres database. Before you can start working on this exercise, you will have to fulfill the following prerequisites:

install your system-specific Docker version.
Please Note:

If you don't use docker desktop, you have to install Docker Compose seperately.

The pipeline process
![image](https://github.com/tstren1223/TUM_SW_1/assets/64294878/a9dfb132-c2e4-4342-8f55-bcbaa663b14b)

The following model describes the pipeline on how your Java source code files will be processed to deploy your application to Ngrok. The interesting thing about this deployment is, that it's completely local. Deployment Overview.png

Java compiles the source code files (*.java) to binary files (*.class) to be executable on your local machine. As a side note, Java compiles your source code files every time you execute Java code locally.
Gradle executes the implemented unit and integration test cases.
Gradle builds an executable .jar-file that contains the application itself and all required external dependencies (e.g. the Spring Boot dependency).
Docker creates an image from this executable .jar file.
The Docker image runs locally and is now through ngrok accessible via the internet.
The system
You run three Docker containers via docker compose on your local machine. The first container contains our hosted Spring Boot application. The second container runs the postgres database for the persistence. A third container runs ngrok to make the sprint boot app accessible via the internet. A client (e.g. any machine out in the world connected to the internet) can invoke HTTP requests to access functionality that your Spring Boot application provides (Person REST API). The Spring Boot container talks to the postgres database, which is in the second container. The Spring Boot application consists of three different layers:

Web Layer: This layer contains PersonResource which provides HTTP(-REST) endpoints to the client.
Application Layer: This layer contains PersonService which provides business logic to PersonResource.
Persistence Layer: This layer contains PersonRepository which provides functionality to perform database actions regarding entities of type Person. This layer uses functionality of the connected Postgres database.
Subsystem Decomposition
![image](https://github.com/tstren1223/TUM_SW_1/assets/64294878/214a9986-9d09-41be-a1b0-6219c28ae4b2)

Part 1: Get your personal Ngrok auth token
Perform the following steps in order to give the ngrok container access to the authentication token:

Sign up on ngrok, if you did not create an account yet.
Copy your auth token. Alternatively you can navigate on the left to Getting Started and Your Auth Token and copy it.
Duplicate in the root directory the file ngrok.template.yml and rename the duplicate to ngrok.yml and insert the auth token in the file.
Part 2: Prepare the deployment
 Create Dockerfile 1 of 1 tests passing
The Dockerfile in your local repository defines how Docker creates an image from your application. This reference provides information on how to create a sample Dockerfile and outlines the general structure. Here you can find all the commands necessary to adjust the Dockerfile in your repository as described in the following steps:

Set openjdk:17-bullseye as the base image.
Set the working directory to /app. The command WORKDIR may be useful. The working directory is the directory where the commands in the Dockerfile are executed.
When you execute ./gradlew clean build, Gradle generates a .jar file from your application and stores this file in the standard Gradle build output directory (most likely in ./build/libs). This is shown in step 3 in the pipeline process at the very start of the problem statment. Create a Docker command that copies this generated jar file to the current working directory with a new name app.jar. (Note that the file name may be different before!).
Create a command that copies the file start.sh into the working directory. Note that we set the working directory in step 2 and therefore, can simply copy the file to the current directory.
Might be necessary: Make start.sh executable by executing chmod 770 start.sh from the command line. Otherwise, you may receive an error about missing permissions.
Add the line CMD ./start.sh to your Dockerfile. This command defines that the script defined in start.sh will be executed as part of building the Docker image.
 Create build script 1 of 1 tests passing
Note 1: In this task, you will have to create a script that you will have to execute on your local computer. Since the supported file formats differ between existing operating systems, you will only have to adjust one script file and should ignore the other file. If you are working on a Windows setup, you will have to adjust the build.bat. If you are working on a Linux/Mac setup, you will have to adjust the build.sh. In both cases, only adjust your system-specific file.

Note 2: If you are working on a Windows setup, prepend call before every command described in the following steps (e.g., call ./gradlew clean build).

This build script should automatically build your Spring Boot server, create a Docker image and start the containers. To do so, you will have to add the following commands to the script file:

Build the Spring Boot application with Gradle: ./gradlew clean build
Build the docker image docker build -t eist-ngrok .
Run the docker containers docker compose up -d
Tip: The official documentation is your best friend, when it comes to debugging.

Part 3: Get the public url from ngrok
 Deploy with Ngrok 6 of 6 tests passing
You can run your script by executing ./build.bat or ./build.sh (depending on your OS) from the command line. Do not execute start.sh.

Note: The submission of your deployed app is only tested (and rated) on Artemis on new push events to your repository. Therefore, make sure to perform a push for every new change that should be tested and scored on Artemis.

Create a file called ngrok-endpoint.txt in the root directory of the project
Navigate to http://localhost:4040/status and copy the URL from the dashboard
Paste the URL in the file ngrok-endpoint.txt. Make sure this piece of text is the only thing in the entire file. Don't forget to check for extra white spaces.
Part 4: Implement new functionality
Now that you have deployed the first version of your application, you decide to improve the application and deploy an updated version. In this part, you should implement functionality to create and persist parent-child relationships between persons.

Adding a feature
 Implement PersonService 8 of 8 tests passing
You have to complete the implementation of some methods in PersonService. In case the following respective condition is not fulfilled, each method should throw an exception of type ResponseStatusException with status code 400 (Bad Request):

addParent(Person person, Person parent): After adding parent to person, person has to have 2 parents at most. parent should be added to the person's list of parents. Afterward, person should be saved to the database by calling save(..) from personRepository. addParent should return the result of this method call.
addChild(Person person, Person child): child is not allowed to have more than 2 parents. Add child to the parent's list of children. Save and return the changed and persisted person.
removeParent(Person person, Person parent): person has to have a number of parents greater than 1 before removing the parent. Remove parent from the person's list of parents. Save and return the changed and persisted person.
removeChild(Person person, Person child): child has to have a number of parents greater than 1 before removing the child. Remove child from the person's list of children. Save and return the changed and persisted person.
Important: You only have to save the parent-child in one direction. E.g., addParent(person: Person, parent: Person) only has to add parent to the list of parents of child, and not child to the list of children of parent. Saving the updated person to the database (with PersonRepository), Spring automatically sets this reference.

Writing test cases
Writing test cases should be part of implementating a new feature to test and ensure that the implemented functionality works correctly. We will look at two types of test cases: Unit Tests and Integration Tests.

 Implement PersonServiceTest 2 of 2 tests passing
You should implement two unit tests in PersonServiceTest using JUnit5. PersonServiceTest already contains two examples that may help you implementing the following test cases:

testAddParent: Create two instances of Person, one acts as a child and one as a parent, and save these objects to the database. Invoke the method addParent in PersonService. Verify that all instances of Person have been correctly saved to the database and that the parent-child relationship has been created between the two added Persons (in the database).
testAddThreeParents: Create four instances of Person, one acts as a child and three as parents, and save these objects to the database. Invoke the method addParent twice so that the parent-child relationships between the two instances of Person are created. Assert that the method invocations return the correct persons, the parent and child have been correctly saved to the database, and that the parent-child relationship is created (in the database and the returned objects of addParent). Invoke addParent again with the third parent and assert that the method throws a ResponseStatusException with status code 400 (Bad Request).
 Optional: Implement PersonIntegrationTest 2 of 2 tests passing
Note 1: This task is optional and grants up to one bonus point. You are still able to reach the full score without solving this task.

Note 2: You will have to use an instance of ObjectMapper to serialize the instances of Person (example provided). You have to use the attribute named objectMapper in PersonIntegrationTest. Don't create and use a new instance of ObjectMapper, nor delete the existing one!

To make sure that the whole system works correctly, you should write two integration tests in PersonIntegrationTest. These tests basically test the same functionality, but at the full scope of the system (instead of only the service and repository). Therefore, instead of invoking the methods of PersonService directly, we will invoke the REST endpoints of PersonResource. You should use Spring MockMVC to invoke the endpoints. PersonIntegrationTests already contains two test cases that you can use as a reference, and in which you should implement the following test cases:

testAddParent: Perform the same test setup and assertions as in PersonServiceTest, but instead of invoking PersonService::addParent, you should invoke the endpoint PUT /api/persons/{personId}/parents and pass the respective parent as the content.
testAddThreeParents: Instead of invoking PersonService::addParent, you should invoke the endpoint PUT /api/persons/{personId}/parents and pass the respective parent as a the content.
Part 5: Deploy the updated version
 Deploy your app to the internet locally 2 of 2 tests passing
Now it is time to deploy the updated version of your application. Perform the same actions as in Step 3 to automatically build the project, build the Docker image, and run the containers.

IMPORTANT: You must keep the deployment running locally for all the tests to pass. If you want to re-trigger Artemis tests, you must make another commit, and if there are major changes in your code, don't forget to rebuild the entire project.

Part 6: Clean Up
Optional Deploying a piece of software has a lot of work going on in the background. If you're interested in seeing all the containers that are running at the moment in your machine locally, you can use the command docker ps. It is always a good practice to stop using the resources once the work is done. Going ahead with the same sentiment, in this task you should implement another script clean.sh (Unix environments) or clean.bat (Windows environments).

Steps:

Similar to Part 2, start with a docker compose down command to turn all the containers down at once.
Next you need to clean up the images. In the course of this exercise we created 3 images:
eist-ngrok
postgres
ngrok/ngrok
Tip: have a look at docker-compose.yml to know more about these images.

Lastly you should remove the /build and /webapp directories.
For your ease, here are some resources on:

Common Linux shell commands
Common Windows shell commands# TUM_SW_1
