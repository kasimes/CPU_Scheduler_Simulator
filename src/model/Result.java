package model;

/*

her işlemin sonucunu temsil eden sınıf

parametreler:
- statuseCode : işlemin durumu(başarılı, başarısız vb.)
- message : işlemle ilgili mesaj
- payload : işlem sonucu dönen veri tipi
*/


public class Result {

    private  int statusCode;
    private String message;
    private Object payload;

    public Result() {
    }

    public Result(int statusCode, String message, Object payload) {
        this.statusCode = statusCode;
        this.message = message;
        this.payload = payload;
    }

    //getter setter

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return  "Result{" +
                "statusCode=" + statusCode +
                ", message=" + message +
                ", payload=" + payload + '}';
    }
}


