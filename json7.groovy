import groovy.json.JsonSlurper
import groovy.json.JsonOutput
import com.sap.gateway.ip.core.customdev.util.*;
import java.text.SimpleDateFormat
import java.util.Date

def Message processData(Message message) {
  def body = message.getBody(String)
  def orders = new JsonSlurper().parseText(body)

  def filtered = orders.findAll { it.country == "DE" }

  message.setBody(JsonOutput.toJson(filtered))
  return message

}

/*
[
  {
    "id": "1",
    "country": "DE"
  },
  {
    "id": "2",
    "country": "CH"
  }
]
*/