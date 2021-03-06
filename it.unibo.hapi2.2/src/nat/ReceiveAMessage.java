package nat;
import java.util.Map;
  
  import ca.uhn.hl7v2.DefaultHapiContext;
  import ca.uhn.hl7v2.HL7Exception;
  import ca.uhn.hl7v2.HapiContext;
  import ca.uhn.hl7v2.app.*;
  import ca.uhn.hl7v2.examples.ExampleReceiverApplication;
 
  import ca.uhn.hl7v2.protocol.ReceivingApplication;
  import ca.uhn.hl7v2.protocol.ReceivingApplicationExceptionHandler;

  public class ReceiveAMessage {
    /**
51      * Example for how to send messages out
52      */
     public static void main(String[] args) throws Exception {
  
        /*
56         * Before we can send, let's create a server to listen for incoming
57         * messages. The following section of code establishes a server listening
58         * on port 1011 for new connections.
59         */
        int port = 54125; // The port to listen on
        boolean useTls = false; // Should we use TLS/SSL?
        HapiContext context = new DefaultHapiContext();
        HL7Service server = context.newServer(port, useTls);
  
        /*
66         * The server may have any number of "application" objects registered to
67         * handle messages. We are going to create an application to listen to
68         * ADT^A01 messages.
69         * 
70         * You might want to look at the source of ExampleReceiverApplication
71         * (it's a nested class below) to see how it works.
72         */
        ReceivingApplication  handler = new ExampleReceiverApplication();
        server.registerApplication("ADT", "A01", handler);
  
        /*
77         * We are going to register the same application to handle ADT^A02
78         * messages. Of course, we coud just as easily have specified a different
79         * handler.
80         */
        server.registerApplication("ADT", "A02", handler);
  
        /*
84         * Another option would be to specify a single application to handle all
85         * messages, like this:
86         * 
87         * server.registerApplication("*", "*", handler);
88         */
  
        /*
91         * If you want to be notified any time a new connection comes in or is
92         * lost, you might also want to register a connection listener (see the
93         * bottom of this class to see what the listener looks like). It's fine
94         * to skip this step.
95         */
        server.registerConnectionListener(new MyConnectionListener());
  
        /*
99         * If you want to be notified any processing failures when receiving,
100        * processing, or responding to messages with the server, you can 
101        * also register an exception handler. (See the bottom of this class
102        * to see what the listener looks like. ) It's also fine to skip this
103        * step, in which case exceptions will simply be logged. 
104        */
       server.setExceptionHandler(new MyExceptionHandler());
 
       // Start the server listening for messages
       server.startAndWait();
 
 

 
       // Stop the receiving server and client
//       server.stopAndWait();
 
    }
 
    /**
185     * Connection listener which is notified whenever a new
186     * connection comes in or is lost
187     */
    public static class MyConnectionListener implements ConnectionListener {
 
       public void connectionReceived(Connection theC) {
          System.out.println("New connection received: " + theC.getRemoteAddress().toString());
       }
 
       public void connectionDiscarded(Connection theC) {
          System.out.println("Lost connection from: " + theC.getRemoteAddress().toString());
       }
 
    }
 
    /**
     * Exception handler which is notified any time
     */
    public static class MyExceptionHandler implements ReceivingApplicationExceptionHandler {
 
       /**
206        * Process an exception.
207        * 
208        * @param theIncomingMessage
209        *            the incoming message. This is the raw message which was
210        *            received from the external system
211        * @param theIncomingMetadata
212        *            Any metadata that accompanies the incoming message. See {@link ca.uhn.hl7v2.protocol.Transportable#getMetadata()}
213        * @param theOutgoingMessage
214        *            the outgoing message. The response NAK message generated by
215        *            HAPI.
216        * @param theE
217        *            the exception which was received
218        * @return The new outgoing message. This can be set to the value provided
219        *         by HAPI in <code>outgoingMessage</code>, or may be replaced with
220        *         another message. <b>This method may not return <code>null</code></b>.
221        */
       public String processException(String theIncomingMessage, Map<String, Object> theIncomingMetadata, String theOutgoingMessage, Exception theE) throws HL7Exception {
           
          /*
225           * Here you can do any processing you like. If you want to change
226           * the response (NAK) message which will be returned you may do
227           * so, or just return the NAK which HAPI already created (theOutgoingMessage) 
228           */
          
          return theOutgoingMessage;
       }
 
    }
}
