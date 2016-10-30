package JAgent;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import java.util.Objects;
import static java.lang.Thread.sleep;


public class Zbiornik1 extends Agent {
    protected void setup(){
        super.setup();
        int zawartosc=0;
        int max=1000;
        ACLMessage wlew = new ACLMessage(ACLMessage.INFORM);
        wlew.addReceiver(new AID("Wlew", AID.ISLOCALNAME));

        System.out.println("Zbiornik1 <---> Zbiornik2");

        while(true) {
            ACLMessage woda = receive();
            if (woda != null) {
                int dolana=Integer.parseInt(woda.getContent());
                if (zawartosc < max) {
                    zawartosc += dolana;
                    System.out.println("Zbiornik1 [" + zawartosc + "l]");
                    wlew.setContent("OK");
                    send(wlew);
                } else {
                    zawartosc += dolana;
                    System.out.println("Zbiornik1 MAX!");
                    //zamknij wlew
                    wlew.setContent("CLOSE");
                    send(wlew);

                    //przelej połowę
                    ACLMessage pwoda = new ACLMessage(ACLMessage.INFORM);
                    pwoda.addReceiver(new AID("Zbiornik2", AID.ISLOCALNAME));
                    while(true){
                        pwoda.setContent(String.valueOf(dolana));
                        send(pwoda);
                        System.out.println("Zbiornik1 >"+dolana+"l> Zbiornik2");
                        while(true){
                            ACLMessage status = receive();
                            if (status != null) {
                                if (!Objects.equals(status.getContent(), "OK"))
                                    System.out.println("Zbiornik1 <-X-> Zbiornik2");
                                else
                                    break;
                            }
                        }
                        zawartosc-=dolana;
                        System.out.println("Zbiornik1 [" + zawartosc + "l]");
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if(zawartosc<=max/2)
                            break;
                    }
                    //otwórz wlew
                    wlew.setContent("OK");
                    send(wlew);
                }
            }
        }
    }
}
