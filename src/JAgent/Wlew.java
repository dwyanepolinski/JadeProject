package JAgent;

import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import java.util.Objects;


public class Wlew extends Agent{
    protected void setup(){
        super.setup();
        final int litrns=100;

        System.out.println("Wlew <---> Zbiornik1, zalewam Zbiornik1 (10000l - "+String.valueOf(litrns)+"l/s)");

        addBehaviour(new TickerBehaviour(this, 1000){
            protected void onTick(){
                ACLMessage woda = new ACLMessage(ACLMessage.INFORM);
                woda.addReceiver(new AID("Zbiornik1", AID.ISLOCALNAME));
                woda.setContent(String.valueOf(litrns));
                send(woda);
                System.out.println("Wlew >"+litrns+"l> Zbiornik1");
                while(true) {
                    ACLMessage status = receive();
                    if (status != null) {
                        if (!Objects.equals(status.getContent(), "OK"))
                            System.out.println("Wlew <-X-> Zbiornik1");
                        else
                            break;
                    }
                }
            }
        });
    }
}