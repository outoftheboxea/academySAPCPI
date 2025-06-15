import groovy.xml.*
import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {


def body = message.getBody(String)
def root = new XmlParser().parseText(body)

// Modify value
root.status[0].value = "Processed"

// Convert back to string
def writer = new StringWriter()
new XmlNodePrinter(new PrintWriter(writer)).print(root)
message.setBody(writer.toString())

return message

}

/*
<invoice>
  <status>Pending</status>
</invoice>

*/