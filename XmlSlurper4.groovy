import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.*

def Message processData(Message message) {
    def body = message.getBody(String)
    def xml = new XmlSlurper().parseText(body)

    if (!xml.amount || !xml.currency) {
        throw new Exception("Missing <amount> or <currency>")
    }

    return message
}

/*
<invoice>
    <amount>100</amount>
    <currency>USD</currency>
</invoice>

*/