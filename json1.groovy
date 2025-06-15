import groovy.json.JsonSlurper
import groovy.json.JsonOutput
import com.sap.gateway.ip.core.customdev.util.Message


def Message processData(Message message) {

  def body = message.getBody(String)
  def json = new JsonSlurper().parseText(body)

  def orderId = json.order.id
  def country = json.order.customer.country

  message.setProperty("OrderID", orderId)
  message.setProperty("CustomerCountry", country)

  return message

}

/*
{
  "order": {
    "id": "ORD123",
    "customer": {
      "name": "Alice",
      "country": "CH"
    }
  }
}
*/