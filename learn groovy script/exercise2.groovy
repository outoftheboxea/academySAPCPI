import com.sap.gateway.ip.core.customdev.util.Message
import com.sap.gateway.ip.core.customdev.util.MessageLog

def Message processData(Message message) {
    def trackingId = message.getProperty("TrackingID")

    if (messageLog != null && trackingId != null) {
        messageLog.setStringProperty("Tracking Info", "Tracking ID: ${trackingId}")
    }

    return message
}
