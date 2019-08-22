package gh.shin.web.value;


import java.io.Serializable;
import java.util.Objects;

public final class WebResponse implements Serializable {
    private static final long serialVersionUID = 1171639567536074947L;
    private final boolean success;
    private final String errorMessage;

    private WebResponse(boolean success, String errorMessage) {
        this.success = success;
        this.errorMessage = errorMessage;
    }

    public static WebResponse success() {
        return new WebResponse(true, null);
    }

    public static WebResponse error(String errorMessage) {
        if (errorMessage == null || errorMessage.length() < 1) {
            throw new RuntimeException("Errror message is not allowed empty message!");
        }
        return new WebResponse(false, errorMessage);
    }


    public boolean isSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof WebResponse)) return false;
        WebResponse webResponse = (WebResponse) o;
        return success == webResponse.success &&
            Objects.equals(errorMessage, webResponse.errorMessage);
    }


    @Override
    public int hashCode() {
        return Objects.hash(success, errorMessage);
    }

    @Override
    public String toString() {
        return "WebResponse{" +
            "success=" + success +
            ", errorMessage='" + errorMessage + '\'' +
            '}';
    }
}
