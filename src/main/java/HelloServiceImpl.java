import io.grpc.stub.StreamObserver;
import org.example.CMPE275Lab1.HelloRequest;
import org.example.CMPE275Lab1.HelloResponse;
import org.example.CMPE275Lab1.HelloServiceGrpc;
import org.example.CMPE275Lab1.HelloServiceOuterClass;

public class HelloServiceImpl extends HelloServiceGrpc.HelloServiceImplBase {
    public void greeting(HelloRequest request,
                         StreamObserver<HelloResponse> responseObserver) {
        // HelloRequest has toString auto-generated.
        System.out.println(request);

        // You must use a builder to construct a new Protobuffer object
        HelloResponse response = HelloResponse.newBuilder()
                .setGreeting("Hello there, " + request.getFirstName() +" "+ request.getLastName())
                .build();

        // Use responseObserver to send a single response back
        responseObserver.onNext(response);

        // When you are done, you must call onCompleted.
        responseObserver.onCompleted();
    }

}
