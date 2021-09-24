package com.example.grpc;
import com.example.csvparser.CSVParser;
import com.example.CMPE275.*;
import io.grpc.stub.StreamObserver;
import com.example.reader.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HelloServiceI extends LargeMessageServiceGrpc.LargeMessageServiceImplBase{
//    @Override
//    public void greeting(GreetingServiceOuterClass.HelloRequest request,
//                         StreamObserver<GreetingServiceOuterClass.HelloResponse> responseObserver) {
//        // HelloRequest has toString auto-generated.
////        System.out.println(request);
////        CSVParser csvParser = new CSVParser();
////        List<GreetingServiceOuterClass.HelloResponse> superList = csvParser.processInputFile("C:\\Users\\nikhi\\OneDrive\\Desktop\\Fall'21\\275\\lab-g01\\reader\\catalog.csv");
////        System.out.println(superList.size());
////
////        // You must use a builder to construct a new Protobuffer object
//////        GreetingServiceOuterClass.HelloResponse response = GreetingServiceOuterClass.HelloResponse.newBuilder()
//////                .setName(superList.get(0).getName())
//////                .setId(superList.get(0).getId())
//////                .setMesonet(superList.get(0).getMesonet())
//////                .build();
////        List<GreetingServiceOuterClass.HelloResponse> list = new ArrayList<>();
////        //GreetingServiceOuterClass.HelloResponse response = GreetingServiceOuterClass.HelloResponse.newBuilder().;
////        GreetingServiceOuterClass.Responses responses = GreetingServiceOuterClass.Responses.newBuilder().addAllResponse(superList).build();
////        // Use responseObserver to send a single response back
////
////        responseObserver.onNext(responses);
////
////        // When you are done, you must call onCompleted.
////        responseObserver.onCompleted();
//    }
    @Override
    public void largeMessage(GreetingServiceOuterClass.HelloRequest request,
                             StreamObserver<GreetingServiceOuterClass.Responses> responseObserver) {
        System.out.println(request);
        CSVParser csvParser = new CSVParser();
        List<GreetingServiceOuterClass.HelloResponse> superList = csvParser.processInputFile("C:\\Users\\nikhi\\OneDrive\\Desktop\\Fall'21\\275\\lab-g01\\reader\\catalog.csv");
        System.out.println(superList.size());

        // You must use a builder to construct a new Protobuffer object
//        GreetingServiceOuterClass.HelloResponse response = GreetingServiceOuterClass.HelloResponse.newBuilder()
//                .setName(superList.get(0).getName())
//                .setId(superList.get(0).getId())
//                .setMesonet(superList.get(0).getMesonet())
//                .build();
        //List<GreetingServiceOuterClass.HelloResponse> list = new ArrayList<>();

        //GreetingServiceOuterClass.HelloResponse response = GreetingServiceOuterClass.HelloResponse.newBuilder().;
        GreetingServiceOuterClass.Responses responses = GreetingServiceOuterClass.Responses.newBuilder().addAllResponse(superList.stream().limit(100).collect(Collectors.toList())).build();
        // Use responseObserver to send a single response back
       // responseObserver.onNext(responses);
        System.out.println("Super");
        responseObserver.onNext(responses);
        responseObserver.onCompleted();
    }



}
