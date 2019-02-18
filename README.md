"# kube-seeder" 


This project is based on 
* https://spring.io/guides/gs/spring-boot-docker/ 
* https://cloud.google.com/kubernetes-engine/docs/tutorials/hello-app

Create project and activate cloud shell in the https://console.cloud.google.com/appengine/

Open shell 

* Step 1: Build the container image
   
      git clone https://github.com/mathewjustin/gkube-seeder.git
   
* Step 2: Set up environment variables, which are going to be reference in the future steps

       export PROJECT_ID="$(gcloud config get-value project -q)"
   
   PROJECT_ID Will be useful while publishing to the container registry 

* Step 3: Build the container image
   
   On Step 1 we have cloned the repo to the project, next step is to build an image of this app
   and push to container registry. 
   
       docker build -t gcr.io/${PROJECT_ID}/kube-seeder:v1 .
       or
       ./mvnw install dockerfile:build : for this particular project
    
    if this step throwing any error, switch to root user, connect to 
    
   After this step you will be able to see the newly built image , by listing the local images "docker images" 
   
* Step 4: Upload the container image
 
   Inorder for GKE to download and run the image, we need to push the image to container registry
  
   first authenticate with container registry.
   
      gcloud auth configure-docker

   Now run this command.
   
      docker push gcr.io/${PROJECT_ID}/kube-seeder:v1
   
* Step 5: Test it by running locally.

      docker run --rm -p 8080:8080 gcr.io/${PROJECT_ID}/kube-seeder:v1
      curl http://localhost:8080

* Step 6: Create a container cluster
  
  Create a new cluster for your app and configure nodes using UI, Or use this command
  
      gcloud container clusters create kube-cluster --num-nodes=3
    
      gcloud compute instances list

  If you have already created the cluster then run the following to retrieve the 
  cluster credentials.
      gcloud container clusters get-credentials kube-cluster

* Step 7: Deploy the application
  --------------------------------------------------------------------------
  The kubectl run command causes Kubernetes to create a Deployment named kube-web on your cluster.
  The Deployment manages multiple copies of your application, called replicas, 
  and schedules them to run on the individual nodes in your cluster.
  In this case, the Deployment will be running only one Pod of your application.  
  
      kubectl run kube-web --image=gcr.io/${PROJECT_ID}/kube-seeder:v1 --port 

     To see the Pod created by the Deployment, run the following command:

       kubectl get pods

* Step 8: Expose your application to the Internet
  ---------------------------------------------

       kubectl expose deployment kube-web --type=LoadBalancer --port 80 --target-port 8080
    
    use the following command to see the details of service
    
        kubectl get service
    
    
* Step 9: Scale up your application - Manual
    
        kubectl scale deployment kube-web --replicas=3
        kubectl get deployment kube-web =>To see instance details
        
* Step 10: Updating versions.

 Build  new image with updated version, push to registry and run the following
 to do rolling update
 
     kubectl set image deployment/kube-web kube-web=gcr.io/${PROJECT_ID}/kube-seeder:v2



   



