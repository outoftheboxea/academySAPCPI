import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.*

def Message processData(Message message) {
  def xml = new XmlSlurper(false, false).parseText(message.getBody(String))
  xml.declareNamespace(ns: 'http://example.com')

  def amount = xml.'ns:amount'.text()
  message.setProperty("Amount", amount)
  return message
}

/*
<ns:invoice xmlns:ns="http://example.com">
  <ns:amount>200</ns:amount>
</ns:invoice>
*/