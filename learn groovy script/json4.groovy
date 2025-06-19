import groovy.json.JsonSlurper
import groovy.json.JsonOutput
import com.sap.gateway.ip.core.customdev.util.*;


def Message processData(Message message) {
  def body = message.getBody(String)
  def json = new JsonSlurper().parseText(body)
  def messageLog = messageLogFactory.getMessageLog(message)

  def items = json.items
  items.each { item ->
  messageLog.setStringProperty("Item - ${item.productId}", "Qty: ${item.qty}")
}

return message
}

/*
{
  "orderId": "ORD456",
  "items": [
    {
      "productId": "P100",
      "qty": 2
    },
    {
      "productId": "P200",
      "qty": 5
    }
  ]
}
*/