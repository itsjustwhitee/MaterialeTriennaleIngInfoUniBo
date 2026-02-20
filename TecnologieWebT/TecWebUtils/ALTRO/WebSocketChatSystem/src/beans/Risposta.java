package beans;

public class Risposta {
    private String messaggio;
    private boolean valid;
    private boolean chiusa;

    public Risposta() {
        super();
        this.messaggio = "";
        this.valid = true;
        this.chiusa = false;
    }

    public String getMessaggio() {
        return messaggio;
    }

    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isChiusa() {
        return valid;
    }

    public void setChiusa(boolean chiusa) {
        this.chiusa = chiusa;
    }

}