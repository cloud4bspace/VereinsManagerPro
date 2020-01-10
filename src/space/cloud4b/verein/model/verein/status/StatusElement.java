package space.cloud4b.verein.model.verein.status;

public class StatusElement {

    private int statusId;
    private String statusText;
    private int statusElementId;
    private int statusElementKey;
    private String statusElementTextLang;
    private String statusElementTextKurz;
    private String statusElementSymbol;

    public StatusElement(int statusId, String statusText) {
        this.statusId = statusId;
        this.statusElementTextLang = statusText;
    }

    public StatusElement(int statusElementKey, String statusElementTextLang, String statusElementTextKurz,
                         String statusElementSymbol) {
        this.statusElementKey = statusElementKey;
        this.statusElementTextLang = statusElementTextLang;
        this.statusElementTextKurz = statusElementTextKurz;
        this.statusElementSymbol = statusElementSymbol;
    }

    public StatusElement(int statusId, String statusText, int statusElementId, int statusElementKey,
                         String statusElementTextLang, String statusElementTextKurz, String statusElementSymbol) {
        this.statusId = statusId;
        this.statusText = statusText;
        this.statusElementId = statusElementId;
        this.statusElementKey = statusElementKey;
        this.statusElementTextLang = statusElementTextLang;
        this.statusElementTextKurz = statusElementTextKurz;
        this.statusElementSymbol = statusElementSymbol;
    }

    public int getStatusElementKey() {
        return statusElementKey;
    }

    public String toString() {
        if (!statusElementSymbol.isEmpty()) {
            return statusElementTextLang + " (" + statusElementSymbol + ")";
        } else {
            return this.statusElementTextLang;
        }
    }

    public String getStatusElementTextLang() {
        return statusElementTextLang;
    }

    public int getStatusId() {
        return this.statusId;
    }

    public String getStatusText() {
        return statusText;
    }
}
