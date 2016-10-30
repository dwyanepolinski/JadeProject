package JAgent;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import static java.lang.Thread.sleep;


public class Zbiornik3 extends Agent {
    protected void setup(){
        super.setup();
        int zawartosc=0;
        int max=1000;
        ACLMessage wlew = new ACLMessage(ACLMessage.INFORM);
        wlew.addReceiver(new AID("Zbiornik2", AID.ISLOCALNAME));

        System.out.println("Zbiornik2 <---> Odpływ Awaryjny");

        while(true) {
            ACLMessage woda = receive();
            if (woda != null) {
                int dolana=Integer.parseInt(woda.getContent());
                if (zawartosc < max) {
                    zawartosc += dolana;
                    System.out.println("Zbiornik3 [" + zawartosc + "l]");
                    wlew.setContent("OK");
                    send(wlew);
                } else {
                    zawartosc += dolana;
                    System.out.println("Zbiornik3 MAX!");
                    //zamknij wlew
                    wlew.setContent("CLOSE");
                    send(wlew);

                    //przelej całość
                    while(true){
                        zawartosc-=dolana;
                        System.out.println("Zbiornik3 >"+dolana+"l> Odpływ awaryjny");
                        System.out.println("Zbiornik3 [" + zawartosc + "l]");
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if(zawartosc==0)
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
