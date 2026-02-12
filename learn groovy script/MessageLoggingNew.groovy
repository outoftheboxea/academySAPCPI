// Sample Groovy Script to log a message in SAP CPI
// © SAP Integration Helper | https://www.youtube.com/@OutOfTheBoxEA

import com.sap.gateway.ip.core.customdev.util.Message
import java.util.HashMap

def Message processData(Message message) {
    def messageLog = messageLogFactory.getMessageLog(message)
    def trackingId = message.getProperty("TrackingID")

    if (messageLog != null) {
        messageLog.setStringProperty("Tracking Info", "Tracking ID: ${trackingId}")
        messageLog.addAttachmentAsString("Log", "This is a test log from Groovy script", "text/plain")
    }

    return message
}

Properties Map<String,Object>

<trackingId,12345A>
<Name,Prasad>
<ID,12345A-1234>

message.getProperty("trackingId") = 12345A = ${trackingId}
message.getProperty("ID") = 12345A-1234

