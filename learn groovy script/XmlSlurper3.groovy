import groovy.xml.*
import com.sap.gateway.ip.core.customdev.util.*;


def Message processData(Message message) {


  def xml = new XmlSlurper().parseText(message.getBody(String))
  def messageLog = messageLogFactory.getMessageLog(message)

  xml.item.each { item ->
  def name = item.name.text()
  def qty = item.qty.text()
  messageLog.setStringProperty("Item: ${name}", "Quantity: ${qty}")
}
return message
}

/*
<order>
  <item><name>Pen</name><qty>2</qty></item>
  <item><name>Book</name><qty>1</qty></item>
</order>
*/