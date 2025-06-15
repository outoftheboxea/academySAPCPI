import groovy.json.JsonSlurper
import groovy.json.JsonOutput
import com.sap.gateway.ip.core.customdev.util.Message


def Message processData(Message message) {

  def body = message.getBody(String)
  def json = new JsonSlurper().parseText(body)

  if (!json.order?.id || !json.order?.customer?.name) {
    throw new Exception("Missing mandatory fields: order.id or customer.name")
  }

  return message

}
