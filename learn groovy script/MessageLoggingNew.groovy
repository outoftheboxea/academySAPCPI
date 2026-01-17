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
