package com.example.CMPE275;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import io.grpc.*;
import com.google.common.util.concurrent.ListenableFuture;

import java.net.InetSocketAddress;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Timer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import com.example.loadBalancing.MultiAddressNameResolverFactory;
import com.google.common.util.concurrent.MoreExecutors;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.Logger;

public class Client {

    private static AtomicLong rpcCount = new AtomicLong();
    static final Logger logger = Logger.getLogger(Client.class);

    public static void printResult(ListenableFuture<GreetingServiceOuterClass.Responses> res)  {
        try {
            System.out.println(res.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void createClient(LargeMessageServiceGrpc.LargeMessageServiceBlockingStub stub, AtomicReference<Throwable> errors, long st,int i){
        PropertyConfigurator.configure("log4j.properties");
        GreetingServiceOuterClass.HelloRequest request = GreetingServiceOuterClass.HelloRequest.newBuilder().setCity("C7959").build();
        GreetingServiceOuterClass.Responses responses = stub.largeMessage(request);
        responses.getResponseList();
        long endTime = System.currentTimeMillis();
//                System.out.println(endTime);
        logger.debug(endTime-st+"**************"+i);
    }
    public static void createClient(LargeMessageServiceGrpc.LargeMessageServiceFutureStub stub,AtomicReference<Throwable> errors,long st,int i) throws InterruptedException {
        logger.debug("Hello World!");
        //System.out.println(i);
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
                //System.out.println(i);
                //Configure logger
                PropertyConfigurator.configure("log4j.properties");
                logger.debug("Hello World!");
                if(!result.equals(GreetingServiceOuterClass.Responses.getDefaultInstance())){
                    errors.compareAndSet(null, new RuntimeException("Invalid Response"));
                }
                synchronized (result){
                    result.getResponseList();
                }
                long endTime = System.currentTimeMillis();
//                System.out.println(endTime);
                logger.debug(endTime-st+"********************"+i);
                //logger.debug(i);



            }


            @Override
            public void onFailure(Throwable throwable) {
                System.out.println("Inside Failure");
            }
        }, MoreExecutors.directExecutor());
       // System.out.println(i);
//        while(!responses.isDone() && i == 9){
//            //System.out.println("waiting...");
////            Thread.sleep(2000);
//
//        }
    }

    public static void main( String[] args ) throws Exception
    {

        //ArrayList<LargeMessageServiceGrpc.LargeMessageServiceFutureStub> stubs = new ArrayList<>(2);
        //ArrayList<LargeMessageServiceGrpc.LargeMessageServiceBlockingStub> stubs = new ArrayList<>(2);

        AtomicReference<Throwable> errors = new AtomicReference<>();
//        for(int i = 0; i < 2; i++){
//            int port = 8080 + i;
//            final ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:"+port)
//                    .usePlaintext(true)
//                    .build();
//
//
//            LargeMessageServiceGrpc.LargeMessageServiceFutureStub stub = LargeMessageServiceGrpc.newFutureStub(channel);
//            //LargeMessageServiceGrpc.LargeMessageServiceBlockingStub stub = LargeMessageServiceGrpc.newBlockingStub(channel);
//            stubs.add(i,stub);
//        }

        //Load Balancing Client
        NameResolver.Factory nameResolverFactory = new MultiAddressNameResolverFactory(
                new InetSocketAddress("localhost", 8080),
                new InetSocketAddress("localhost", 8081),
                new InetSocketAddress("localhost", 8082),
                new InetSocketAddress("localhost", 8083)
        );

        ManagedChannel channel = ManagedChannelBuilder.forTarget("service")
                .nameResolverFactory(nameResolverFactory)
                .defaultLoadBalancingPolicy("round_robin")
                .usePlaintext()
                .build();


//        for(int j =0 ; j< stubs.size(); j++){
//            System.out.println(stubs.get(j));
//        }
        LargeMessageServiceGrpc.LargeMessageServiceFutureStub stub = LargeMessageServiceGrpc.newFutureStub(channel);
        //LargeMessageServiceGrpc.LargeMessageServiceBlockingStub stub = LargeMessageServiceGrpc.newBlockingStub(channel);
        Instant start = Instant.now();
        long startTime = 0;
        //long endTime = System.currentTimeMillis();

        //logger.debug(startTime);

        int count = 0;
        for(int i=0;i<10;i++){//System.out.println(i%2);
            //System.out.println(stubs.get(i%2));
            //LargeMessageServiceGrpc.LargeMessageServiceFutureStub stub = stubs.get(i%2);
            //LargeMessageServiceGrpc.LargeMessageServiceBlockingStub stub = stubs.get(0);
            startTime = System.currentTimeMillis();

            createClient(stub,errors, startTime,i);
            count += 1;
        }
        Instant finish = Instant.now();
        long endTime = System.currentTimeMillis();
        System.out.println(endTime-startTime);


        System.out.println("****************************************************"+ Duration.between(start,finish).toMillis());
        while(true){
            System.out.println("****************************************************"+ Duration.between(start,finish).toMillis()+"***"+ count);
            Thread.sleep(2000);
        }

    }
}
