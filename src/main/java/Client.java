import io.grpc.*;
import org.example.CMPE275Lab1.HelloRequest;
import org.example.CMPE275Lab1.HelloResponse;
import org.example.CMPE275Lab1.HelloServiceGrpc;
import org.example.CMPE275Lab1.HelloServiceOuterClass;

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
        HelloServiceGrpc.HelloServiceBlockingStub stub = HelloServiceGrpc.newBlockingStub(channel);
        HelloRequest request =
                HelloRequest.newBuilder()
                        .setFirstName("Nikhil")
                        .build();

        // Finally, make the call using the stub
        HelloResponse response =
                stub.hello(request);

        System.out.println(response);

        // A Channel should be shutdown before stopping the process.
        channel.shutdownNow();
    }
}
