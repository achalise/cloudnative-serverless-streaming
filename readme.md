# Serverless and Data Streaming 

A sample project exploring event driven architecture and stream processing on serverless platforms.

This a hypothetical claims processing application implemented to illustrate architectural concepts. Customers can submit
claim requests which will then be processed according to the business rules resulting in customer getting paid or the
request being rejected.

The application consists of three main components.

- ClaimsService
- ClaimsProcessor
- StreamProcessor

Overall architecture diagram:

## Claim Service

The application exposes a http endpoint for users to submit their claim request. The service is implemented using 
Spring Cloud Function following hexagonal architecture principles.

At the core, the service is a function that accepts `ClaimRequest` to produce a `ClaimResponse` output. For simplicity,
`ClaimRequest` only consists of

and `ClaimResponse` consists of

Service publishes `event` on a Kafka topic upon receipt of customer claim request.

### Running Locally for Development

- Run kafka locally
  - using Docker Compose
    `cd docker && docker-compose up` (ensure that docker daemon is running) It starts kafka with image 
    `bitnami/kafka:latest` which is kafka without zookeeper.

The application is a spring cloud function and can be started locally using the following command:
`./gradlew bootRun` 
To test, issuing following POST request,
```http request
POST http://localhost:8080/submitClaimRequest
Content-Type: application/json

{
  "firstName": "Joe",
  "lastName": "Blogs",
  "amount": 20,
  "claimType": "CLAIM_A",
  "email": "test@user.com"
}
```
will produce following response:
```json lines
{
  "status" : {
    "code": "UNDER_REVIEW",
    "message": "Application received"
  },
  "correlationId": "7d979021-5cc8-478d-8990-9d3d7c23b20a"
}
```

### Running on local K8s Cluster
#### Pre-requisites
- Local Kubernetes

`k3s` is lightweight distribution of Kubernetes perfect for resource constrained environments and a good choice for local
development environment. And [k3d](https://k3d.io/v5.4.9/#installation) can be used to run [k3s](https://k3s.io/) locally on Docker.
Also ensure [https://kubernetes.io/docs/reference/kubectl/] (kubectl) is installed to connect to the cluster.
Create a local cluster with name `dev`
`k3d cluster create dev`
`kubectl get nodes` to list the nodes in the cluster.

- Kafka

We can start with hosted `kafka` solution, or deploy our own Kafka broker in the cluster. For a hosted solution
[https://www.confluent.io/get-started/](Confluent) is one option to get up and running quickly. 

#### Deployment

- build image using buildpack `./gradlew bootBuildImage`, which will build a local docker image `claimservice:0.0.1-SNAPSHOT`.
- import the image into the cluster so that it is accessible to the deployments
  `k3d image import claimservice:0.0.1-SNAPSHOT -c {cluster-name}`
- `cd k8s`
- `kubectl create -f secrets.yaml` (for kafka credentials to connect to Confluent hosted broker)
- `kubectl create -f deployment.yaml`
- `kubectl create -f service-node-port.yaml` (to create a node port service for local testing)

The service can now be accessed at `localhost:30000`

```http request
POST http://localhost:30000/submitClaimRequest
Content-Type: application/json
{
  "firstName": "Joe",
  "lastName": "Blogs",
  "amount": 20,
  "claimType": "",
  "email": "test@user.com"
}
```



### Running on AWS EKS
#### Prerequisites

Create an AWS EKS cluster by following [instructions](https://docs.aws.amazon.com/eks/latest/userguide/getting-started.html)

### Running on AWS Lambda

SpringCloudFunction provides adapters for various serverless platforms. For AWS Lambda, we need
the spring cloud function adapter for aws and the thin jar plugin to produce the shaded jar for deployment.

```
implementation("org.springframework.cloud:spring-cloud-function-adapter-aws")
implementation("org.springframework.boot.experimental:spring-boot-thin-gradle-plugin:1.0.29.RELEASE")
```
and the following declarations in the `build.gradle.kts`

```
tasks.assemble{
	dependsOn("shadowJar")
}

tasks.withType<ShadowJar> {
    archiveClassifier.set("aws")
	dependencies {
		exclude("org.springframework.cloud:spring-cloud-function-web")
	}
	// Required for Spring
	mergeServiceFiles()
	append("META-INF/spring.handlers")
	append("META-INF/spring.schemas")
	append("META-INF/spring.tooling")
	transform(PropertiesFileTransformer::class.java) {
		paths.add("META-INF/spring.factories")
		mergeStrategy = "append"
	}
}

tasks.withType<Jar> {
	manifest {
		attributes["Start-Class"] = "com.arun.claimservice.ClaimserviceApplication"
	}
}

```
We also need a request handler to process incoming requests and Spring provides a generic request handler for this:
`org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest`

Following environment variables need to be defined in the function configuration - `spring_profiles_active, KAFKA_USR, KAFKA_PASSWORD`

### Running on TAS
### Running on KNative
