# Concentric: Java Test Scenario

Author: Antonio Passarelli  
Level: Senior Java Developer  
Technologies: CDI, JSF, JPA, EJB, JAX-RS, BV  
Summary: The Java Test Scenario is based on the RedHat Jboss `kitchensink` quickstart, which demonstrates a Java EE 7 web-enabled database application using JSF, CDI, EJB, JPA, and Bean Validation.    
Target Product: JBoss EAP  
Source: <https://github.com/antoniopassarelli/concentricjavatest/>    
Credit Source: <https://github.com/jbossas/eap-quickstarts/>  

## What is it?

The deliverable is a deployable Maven 3 project developed with Java EE 7 on Red Hat JBoss Enterprise Application Platform.

_Note: This quickstart uses the H2 database included with Red Hat JBoss Enterprise Application Platform 7.1. It is a lightweight, relational example datasource that is used for examples only. It is not robust or scalable, is not supported, and should NOT be used in a production environment!_


## System Requirements

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 7.1 or later.

All you need to build this project is Java 8.0 (Java SDK 1.8) or later and Maven 3.3.1 or later.


## Build and Deploy the Quickstart

1. Open a command prompt and navigate to the root directory of this project.
3. Type this command to start wildfly, build the project and deploy the archive:

        mvn clean install wildfly:run

4. This will deploy `target/ConcentricJavaTest.war` to the running instance of the server.


## Access the Application

The application will be running at the following URL: <http://localhost:8080/ConcentricJavaTest/>.

By default the application comes with three users:  
*admin*  
*user*  
*user2*  

and two credit cards for the regular users.


## Stop the server

1. When finished testing, type this command to undeploy the archive:

        mvn wildfly:shutdown


## Eclipse Oxygen IDE import

The project can be imported (as a Maven project) into the Eclipse Oxygen IDE.
It can be necessary to install the m2e connector, if the IDE requires.
In order to run the maven commands from the IDE, it is necessary to temporary define the JDK as the default Installed JRE. 


## Debug the Application

If you want to debug the application, uncomment line 224 in the pom.xml file:

    <!-- javaOpt>-agentlib:jdwp=transport=dt_socket,address=8787,server=y,suspend=y</javaOpt -->

 then run the above starting commmand and debug as a remote Java application as usual.