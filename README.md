# Virtualization and Introduction to Docker

Refers to breaking applications into smaller, independent modules while using virtualization to run them in isolated environments. Docker simplifies this by using containers, which package applications and their dependencies, ensuring consistency across different environments.
This project use reflexion for use annotations and there use virtualization for deploy but in
this exercise use a image of Docker.

## Getting Started

1. For clone this repository you use this command:
 ```
 git clone https://github.com/JffMv/server_anotations_rest_full.git
 ```
2. Compile this project:
 ```
 mvn clena install
 ```
3. Run this project:

 ```
 java -cp "target/classes" co.edu.escuelaing.WebApplication controllers.GreetingController

 ```
### Prerequisites

Have installed:
maven 3.9.6 - [Install Maven](https://maven.apache.org/download.cgi#Installation)

git 2.44 - [Install Git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git)


java 21 - [Install Java](https://www.oracle.com/co/java/technologies/downloads/)

## Deployment

Image run this project at instance of AWS per port 42000.
![img_6.png](pictures_of_readme/img_6.png)

This project run with image Docker from repository:

```
jeffer07/server_anotations_rest_full_lab_4
```
![img_7.png](pictures_of_readme/img_7.png)


### How deploy this application?
#### Local
1. create Dockerfile
2. use for create container ```docker build --tag name_of_container .```
3. use for create image ```docker run -d -p 34000:6000 --name name_of_image name_of_container```
#### Push
5. Tag of container and create other image to load to repository ```docker tag name_container name/repository```
6. Login to docker ```docker login```
7. Do push ```docker push name/repository:latest```

#### Deploy at instance of AWS
8. Create one instance at AWS EC2 and connect
9. Install docker: 
```
sudo yum update -y
sudo yum install docker

```
10. Start service of docker:
```
sudo service docker start
```
11. Configure user at group:
```
sudo usermod -a -G docker ec2-user

```
12. Exit and into at the instance
13. Create instance of Dockerhub
```
docker run -d -p 42000:6000 --name name_image_at_aws name/repository
```
14. Open of port at instance of AWS
![img_9.png](pictures_of_readme/img_9.png)
15. Verify whit link in http per port 42000

## Architecture
![img_5.png](pictures_of_readme/img_5.png)
### HttpServer:
Is one server that host several services API´s

### Form HTML
Host a simple form and functions javascript. Those functions call API´s

## Test
if you want run of test use this command:
```
mvn test
```
Test of server and API´s work:

![img_4.png](pictures_of_readme/img.png)


## How it see
![img_8.png](pictures_of_readme/img_8.png)

## Built With

* [Java](https://www.java.com/es/) - The language used
* [Maven](https://maven.apache.org/) - Dependency Management
* [React](https://reactjs.org/) - Frontend framework
* [Git](http://git-scm.com/) - Version Control System


## Authors

* **Yeferson Mesa**

## License

This project is licensed under the MIT License - see the [LICENCE.md](LICENCE) file for details


