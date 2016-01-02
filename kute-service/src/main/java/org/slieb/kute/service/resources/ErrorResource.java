package org.slieb.kute.service.resources;

public class ErrorResource extends AbstractHTMLResource {

    private final Exception exception;

    public ErrorResource(String path,
                         Exception exception) {
        super(path);
        this.exception = exception;
    }

    @Override
    public String getContent() {
        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html>");

        html.append("<html>");
        html.append("<body>");
        html.append("<h1>")
                .append(exception.getClass()).append(" - ")
                .append(exception.getMessage()).append("</h1>");

        html.append("<p>");
        for (StackTraceElement element : exception.getStackTrace()) {
            html.append(element.toString()).append("<br />");
        }
        html.append("</p>");

        Throwable current = exception.getCause();
        while (current != null) {
            html.append("<b>Caused by: ").append(current.getClass()).append("</b>");
            html.append("<p>");
            for (StackTraceElement element : current.getStackTrace()) {
                html.append(element.toString()).append("<br />");
            }
            html.append("</p>");
            current = current.getCause();
        }


        html.append("</body>");
        html.append("</html>");

        return html.toString();
    }
}
