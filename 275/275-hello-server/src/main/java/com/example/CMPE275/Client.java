package com.example.CMPE275;
import io.grpc.*;

public class Client {

    public static void main( String[] args ) throws Exception
    {
        // Channel is the abstraction to connect to a service endpoint
        // Let's use plaintext communication because we don't have certs
        final ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:8080")
                .usePlaintext(true)
                .build();

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
        System.out.println("Awesome");
        LargeMessageServiceGrpc.LargeMessageServiceBlockingStub stub = LargeMessageServiceGrpc.newBlockingStub(channel);
        System.out.println("Awesome123");
        GreetingServiceOuterClass.HelloRequest request = GreetingServiceOuterClass.HelloRequest.newBuilder().setCity("C7959").build();
        System.out.println("Awesome456");
        GreetingServiceOuterClass.Responses responses = stub.largeMessage(request);
        System.out.println("Awesome789");
        System.out.println(responses.getResponseList());
        channel.shutdownNow();
    }
}
