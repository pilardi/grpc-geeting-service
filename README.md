# GRPC greeting service using:
* java
* maven
* spring boot (https://docs.spring.io/spring-boot)
* grpc spring boot (https://yidongnan.github.io/grpc-spring-boot-starter)
* grpcurl (https://github.com/fullstorydev/grpcurl)

## Can be run with:
* docker (https://www.docker.com/) 
* kubernates (https://kubernetes.io/)
* helm (https://helm.sh/)

### Docker run:
1. mvn package -Ddocker=run
2. mvn package -Ddocker=debug

#### Health check:
1. docker exec $(docker ps | grep org.geetingservice/grpc-geeting-service-server:LOCAL-SNAPSHOT | cut -d" " -f1) /bin/grpc_health_probe -addr=:8080
2. docker exec $(docker ps | grep org.geetingservice/grpc-geeting-service-server:LOCAL-SNAPSHOT | cut -d" " -f1) /bin/grpc_health_probe -addr=:8080 -service org.geetingservice.GreetingService

## Kubernates run:
1. mvn package -Ddocker
2. cd grpc-geeting-service-server && kubectl apply -f kubernetes.yaml && kubectl port-forward service/grpc-geeting-service 8080:8080 &
3. kubectl delete -f kubernetes.yaml # after running

### Health check:
1. kubectl exec $(kubectl get pods | grep grpc-geeting-service | cut -f1 -d" ") -- /bin/grpc_health_probe -addr=:8080
2. kubectl exec $(kubectl get pods | grep grpc-geeting-service | cut -f1 -d" ") -- /bin/grpc_health_probe -addr=:8080 -service org.geetingservice.GreetingService

## Helm run
1. cd grpc-geeting-service/helm && helm install grpc-geeting-service ./grpc-geeting-service
2. export POD_NAME=$(kubectl get pods --namespace default -l "app.kubernetes.io/name=grpc-geeting-service,app.kubernetes.io/instance=grpc-geeting-service" -o jsonpath="{.items[0].metadata.name}")
3. export CONTAINER_PORT=$(kubectl get pod --namespace default $POD_NAME -o jsonpath="{.spec.containers[0].ports[0].containerPort}")
4. kubectl --namespace default port-forward $POD_NAME 8080:$CONTAINER_PORT &
5. cd grpc-geeting-service/helm && helm delete grpc-geeting-service # after running

### Health check:
1. kubectl exec $(kubectl get pods | grep grpc-geeting-service | cut -f1 -d" ") -- /bin/grpc_health_probe -addr=:8080
2. kubectl exec $(kubectl get pods | grep grpc-geeting-service | cut -f1 -d" ") -- /bin/grpc_health_probe -addr=:8080 -service org.geetingservice.GreetingService

## Test (docker/kubernates/helm)
1. grpcurl --plaintext localhost:8080 list
2. grpcurl --plaintext localhost:8080 describe
3. grpcurl -d '{ "name": "there" }' --plaintext localhost:8080 org.geetingservice.GreetingService.greeting
