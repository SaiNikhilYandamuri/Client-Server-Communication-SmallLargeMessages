package com.example.grpc;
import com.example.csvparser.CSVParser;
import com.example.CMPE275.*;
import io.grpc.stub.StreamObserver;
import com.example.reader.Station;
import java.util.List;

public class HelloServiceI extends GreetingServiceGrpc.GreetingServiceImplBase{
    @Override
    public void greeting(GreetingServiceOuterClass.HelloRequest request,
                         StreamObserver<GreetingServiceOuterClass.HelloResponse> responseObserver) {
        // HelloRequest has toString auto-generated.
        System.out.println(request);
        CSVParser csvParser = new CSVParser();
        List<Station> superList = csvParser.processInputFile("C:\\Users\\nikhi\\OneDrive\\Desktop\\Fall'21\\275\\lab-g01\\reader\\catalog.csv");
        System.out.println(superList.size());
        // You must use a builder to construct a new Protobuffer object
        GreetingServiceOuterClass.HelloResponse response = GreetingServiceOuterClass.HelloResponse.newBuilder()
                .setName(superList.get(0).getName())
                .setId(superList.get(0).getId())
                .setMesonet(superList.get(0).getMesonet())
                .build();

        // Use responseObserver to send a single response back
        responseObserver.onNext(response);

        // When you are done, you must call onCompleted.
        responseObserver.onCompleted();
    }

}
