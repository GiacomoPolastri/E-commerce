package model.session.mo;

/**
 *
 * @author Giacomo Polastri
 */
public class Carrello {
    
    private long codiceProdotto , quantita;

    
    @Override
    public String toString(){
        return (String) (getCodiceProdotto() + " " + getQuantità());
    }

    public long getCodiceProdotto() {
        return codiceProdotto;
    }

    public void setCodiceProdotto(long CodiceProdotto) {
        this.codiceProdotto = CodiceProdotto;
    }

    public long getQuantità() {
        return quantita;
    }

    public void setQuantità(long quantita) {
        this.quantita = quantita;
    }

}
