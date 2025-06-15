import groovy.json.JsonSlurper
import groovy.json.JsonOutput
import com.sap.gateway.ip.core.customdev.util.Message


def Message processData(Message message) {

  def body = message.getBody(String)
  def json = new JsonSlurper().parseText(body)

  json.status = "Processed"

  def modifiedJson = JsonOutput.toJson(json)
  message.setBody(modifiedJson)

  return message

}
