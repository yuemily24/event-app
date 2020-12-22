package Gateways;

import UseCases.EventManager;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ScheduleHTMLGateway {
    private final EventManager eventManager;

    /**
     * Constructor for Exporting a conference's schedule to HTML file.
     */
    public ScheduleHTMLGateway(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    /**
     * Creates an HTML document displaying the Event info of every Event in a table.
     */
    public void createHTML() {
        try {
            FileWriter scheduleDoc = new FileWriter("Conference_schedule.html", false);
            scheduleDoc.append("<!doctype html>\n" +
                    "\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "<meta charset=\"utf-8\">\n" +
                    "<link rel=\"preconnect\" href=\"https://fonts.gstatic.com\">\n" +
                    "<link href=\"https://fonts.googleapis.com/css2?family=Raleway:wght@300&display=swap\" rel=\"stylesheet\">\n" +
                    "<style>\n" +
                    "table {\n" +
                    "  border-collapse: collapse;\n" +
                    "  width: 100%;\n" +
                    "}\n" +
                    "\n" +
                    "th, td {\n" +
                    "  font-family: 'Raleway', sans-serif;\n" +
                    "  text-align: left;\n" +
                    "  padding: 8px;\n" +
                    "}\n" +
                    "\n" +
                    "tr:nth-child(even){background-color: #f2f2f2}\n" +
                    "\n" +
                    "th {\n" +
                    "  background-color: #058b8c;\n" +
                    "  color: white;\n" +
                    "}\n" +
                    "</style>" +
                    "</head>\n" +
                    "\n" +
                    "<body>\n" +
                    createTable() +
                    "</body>\n" +
                    "</html>");
            scheduleDoc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Creates a table containing the Title, Speakers, Time, Location, Current Signups, and Current Waitlist of all Events.
     *
     * @return The HTML formatted string containing the Event info of every Event.
     */
    private String createTable() {
        StringBuilder newTable = new StringBuilder("<table style=\"width:100%\">\n" +
                "<tr> \n" +
                "<th>Event Title</th> \n" +
                "<th>Speaker(s)</th> \n" +
                "<th>Time</th> \n" +
                "<th>Location</th>\n" +
                "<th>Current Signups / Capacity</th>\n" +
                "<th>Current Waitlist</th>\n" +
                "</tr>\n");
        for (String i : eventManager.getAllEvents()) {
            Map<String, List<String>> eventInfo = eventManager.getInfo(i);
            newTable.append("<tr>\n" + "<td>").append(eventInfo.get("title").get(0)).append("</td>\n").append( //Title
                    "<td>").append(getSpeakerNames(i)).append("</td>\n").append( //Speakers
                    "<td>").append(eventInfo.get("time").get(0)).append(" to ").append(eventInfo.get("time").get(1)).append("</td>\n").append( //Time
                    "<td>").append(eventInfo.get("location").get(0)).append("</td>\n").append( //Location
                    "<td>").append(eventInfo.get("signups").size()).append("/").append(eventInfo.get("capacity").get(0)).append("</td>\n").append( //Current Signups
                    "<td>").append(eventInfo.get("waitlist").size()).append("</td>\n").append( //Current Waitlist
                    "</tr>\n");
        }
        newTable.append("</table>\n");
        return newTable.toString();
    }

    /**
     * Gets the names of all the Speakers in a specific Event.
     *
     * @param eventID The Event to get the Speaker names from.
     * @return The list containing the names of the Speakers in the Event.
     */
    private String getSpeakerNames(String eventID) {
        StringBuilder speakersList = new StringBuilder();
        for (String speaker : eventManager.getInfo(eventID).get("speakers")) {
            speakersList.append(speaker).append("\n ");
        }
        return speakersList.toString();
    }
}
