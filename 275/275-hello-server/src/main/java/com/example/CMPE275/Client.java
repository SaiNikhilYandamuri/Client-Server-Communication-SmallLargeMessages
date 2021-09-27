package com.example.CMPE275;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import io.grpc.*;
import com.google.common.util.concurrent.ListenableFuture;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.Timer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import com.google.common.util.concurrent.MoreExecutors;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

public class Client {

    private static AtomicLong rpcCount = new AtomicLong();

    public static void createClient(LargeMessageServiceGrpc.LargeMessageServiceBlockingStub stub,AtomicReference<Throwable> errors) {


//        long startTime = System.currentTimeMillis();
//        //System.out.println(startTime);
        //LargeMessageServiceGrpc.LargeMessageServiceBlockingStub stub = LargeMessageServiceGrpc.newBlockingStub(channel);
        GreetingServiceOuterClass.HelloRequest request = GreetingServiceOuterClass.HelloRequest.newBuilder().setCity("C7959").build();
        GreetingServiceOuterClass.Responses responses = stub.largeMessage(request);
        System.out.println(responses.getResponseList());
//        //channel.shutdownNow();
//        //}
//        long endTime = System.currentTimeMillis();
//        //System.out.println(endTime-startTime);
//        channel.shutdownNow();

        // --------------------------------------------------

        //LargeMessageServiceGrpc.LargeMessageServiceFutureStub stub = LargeMessageServiceGrpc.newFutureStub(channel);
//        GreetingServiceOuterClass.HelloRequest request = GreetingServiceOuterClass.HelloRequest.newBuilder().setCity("C7959").build();
//        ListenableFuture<GreetingServiceOuterClass.Responses> responses = stub.largeMessage(request);
//        responses.addListener(()-> rpcCount.incrementAndGet(), MoreExecutors.directExecutor());
////        responses.addListener(()-> new Runnable() {
////            public void run(){
////                try {
////                    System.out.println(responses.get());
////                } catch (InterruptedException e) {
////                    e.printStackTrace();
////                } catch (ExecutionException e) {
////                    e.printStackTrace();
////                }
////            }
////        }, MoreExecutors.directExecutor());
//        Futures.addCallback(responses, new FutureCallback<GreetingServiceOuterClass.Responses>() {
//            @Override
//            public void onSuccess(@NullableDecl GreetingServiceOuterClass.Responses result) {
//                System.out.println("SUccess");
//                if(!result.equals(GreetingServiceOuterClass.Responses.getDefaultInstance())){
//                    errors.compareAndSet(null, new RuntimeException("Invalid Response"));
//                }
//                synchronized (result){
//                    System.out.println(result.getResponseList());
//                }
//
//            }
//
//
//            @Override
//            public void onFailure(Throwable throwable) {
//                System.out.println("Inside Failure");
//            }
//        }, MoreExecutors.directExecutor());
//        try {
//            synchronized (responses){
//                System.out.println(responses.get());
//            }
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
    }

    public static void main( String[] args ) throws Exception
    {
        // Channel is the abstraction to connect to a service endpoint
        // Let's use plaintext communication because we don't have certs
//        final ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:8080")
//                .usePlaintext(true)
//                .build();

        // It is up to the client to determine whether to block the call
        // Here we create a blocking stub, but an async stub,
        // or an async stub with Future are always possible.
//        GreetingServiceGrpc.GreetingServiceBlockingStub stub = GreetingServiceGrpc.newBlockingStub(channel);
 //GreetingServiceOuterClass.HelloRequest request =
//                GreetingServiceOuterClass.HelloRequest.newBuilder()
//                        .setCity("San Jose")
//                        .build();
//
//
//
//        // Finally, make the call using the stub
//        GreetingServiceOuterClass.HelloResponse response =
//                stub.greeting(request);
//
//        System.out.println(response);
//
//        // A Channel should be shutdown before stopping the process.
//        channel.shutdownNow();
        //for(int i=0;i<10;i++){
        //Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//        long startTime = System.currentTimeMillis();
//        System.out.println(startTime);
//            LargeMessageServiceGrpc.LargeMessageServiceBlockingStub stub = LargeMessageServiceGrpc.newBlockingStub(channel);
//            GreetingServiceOuterClass.HelloRequest request = GreetingServiceOuterClass.HelloRequest.newBuilder().setCity("C7959").build();
//            GreetingServiceOuterClass.Responses responses = stub.largeMessage(request);
//            System.out.println(responses.getResponseCount());
//            //channel.shutdownNow();
//        //}
//        long endTime = System.currentTimeMillis();
//        System.out.println(endTime-startTime);
//        channel.shutdownNow();

        final ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:8080")
                .usePlaintext(true)
                .build();
        AtomicReference<Throwable> errors = new AtomicReference<>();
        Instant start = Instant.now();
        long startTime = System.currentTimeMillis();
        //LargeMessageServiceGrpc.LargeMessageServiceFutureStub stub = LargeMessageServiceGrpc.newFutureStub(channel);
        LargeMessageServiceGrpc.LargeMessageServiceBlockingStub stub = LargeMessageServiceGrpc.newBlockingStub(channel);
        for(int i=0;i<10;i++){
            createClient(stub,errors);
        }
        Instant finish = Instant.now();
        long endTime = System.currentTimeMillis();
        System.out.println(endTime-startTime);
        System.out.println("****************************************************"+ Duration.between(start,finish).toMillis());
    }
}
