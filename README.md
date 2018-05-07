# gc-wien
GiftCard demo application as live coded at DDD/Microservices Vienna on 3 May 2018

## Running this application

This application is designed to run in several modes:

### 1. Running as a stand-alone monolith

Just run the application; it's configuration 
as committed to the repository is like that. It
will run with a SimpleCommandBus, SimpleQueryBus
and an EmbeddedEventStore (backed by JPA+H2) as
EventBus.

### 2. Running locally with AxonHub and AxonDB

Edit pom.xml, comment in the dependency on 
io.axoniq:axonhub-spring-boot-autoconfigure. 
Download the free developer editions of AxonDB and AxonHub from
https://axoniq.io, and run them (which you may do through
Docker as explained in docker-platform, or straight on
your own machine).

Now, you still have one instance of your application
but it will use an AxonHubCommandBus, AxonHubQueryBus
and AxonHubEventBus, and all communication will go through
AxonHub.

### 3. Running as a microservices system

You can create many instances of the application and run them in
parallel. There are a few properties in application.properties that
allow you to enable or disable the GUI, Command-Side and Query-Side
separately. You could do this through Docker and Kubernetes as well.
For the demo in Vienna, I set up a Kubernetes cluster through Google
Cloud Platform, which is a really easy way of getting it done.

The free Developer editions of AxonDB and AxonHub run a single nodes
only. If you want to experiment with more advanced configs like AxonDB
and AxonHub cluster, please reach out.

## Questions?

Feel free to reach out at frans dot vanbuul at axoniq dot io
 








