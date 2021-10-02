package com.example.grpc;

import com.example.CMPE275.GreetingServiceOuterClass;
import com.example.CMPE275.LargeMessageServiceGrpc;
import com.example.CMPE275.LoadBalanceServiceGrpc;
import com.example.CMPE275.LoadBalancerService;
import com.example.loadBalancing.MultiAddressNameResolverFactory;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.NameResolver;
import io.grpc.stub.StreamObserver;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

public class LoadBalancerI extends LoadBalanceServiceGrpc.LoadBalanceServiceImplBase {
    public void laodBalancer(LoadBalancerService.RequestData request, StreamObserver<LoadBalancerService.ResponsesData> responseObserver){

        AtomicReference<Throwable> errors = new AtomicReference<>();
        //Load Balancing Client
        NameResolver.Factory nameResolverFactory = new MultiAddressNameResolverFactory(
                new InetSocketAddress("localhost", 8080),
                new InetSocketAddress("localhost", 8081),
                new InetSocketAddress("localhost", 8082)
        );

        ManagedChannel channel = ManagedChannelBuilder.forTarget("service")
                .nameResolverFactory(nameResolverFactory)
                .defaultLoadBalancingPolicy("round_robin")
                .usePlaintext()
                .build();


        LargeMessageServiceGrpc.LargeMessageServiceFutureStub stub = LargeMessageServiceGrpc.newFutureStub(channel);
        // LargeMessageServiceGrpc.LargeMessageServiceBlockingStub stub = LargeMessageServiceGrpc.newBlockingStub(channel);
        Instant start = Instant.now();
        long startTime = System.currentTimeMillis();
        //for(int i=0;i<10;i++){//System.out.println(i%2);
            try {
                createClient(stub,errors);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        //}
        Instant finish = Instant.now();
        long endTime = System.currentTimeMillis();
        System.out.println(endTime-startTime);
        System.out.println("****************************************************"+ Duration.between(start,finish).toMillis());
    }

    public static void createClient(LargeMessageServiceGrpc.LargeMessageServiceFutureStub stub, AtomicReference<Throwable> errors) throws InterruptedException {



        GreetingServiceOuterClass.HelloRequest request = GreetingServiceOuterClass.HelloRequest.newBuilder().setCity("C7959").build();
        ListenableFuture<GreetingServiceOuterClass.Responses> responses = stub.largeMessage(request);
        responses.addListener(()-> new Runnable() {
            public void run() {
                try {
                    System.out.println(responses.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }}, MoreExecutors.directExecutor());

        Futures.addCallback(responses, new FutureCallback<GreetingServiceOuterClass.Responses>() {
            @Override
            public void onSuccess(@NullableDecl GreetingServiceOuterClass.Responses result) {
                System.out.println("SUccess");
                if(!result.equals(GreetingServiceOuterClass.Responses.getDefaultInstance())){
                    errors.compareAndSet(null, new RuntimeException("Invalid Response"));
                }
                synchronized (result){
                    result.getResponseList();
                }

                while(!responses.isDone()){
                    System.out.println(".....wait");
                }

            }


            @Override
            public void onFailure(Throwable throwable) {
                System.out.println("Inside Failure");
            }
        }, MoreExecutors.directExecutor());

        while(!responses.isDone()){
            //System.out.println("waiting...");
//            Thread.sleep(2000);

        }
    }
}
