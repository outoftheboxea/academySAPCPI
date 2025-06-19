import groovy.json.JsonSlurper
import groovy.json.JsonOutput
import com.sap.gateway.ip.core.customdev.util.*;
import java.text.SimpleDateFormat
import java.util.Date

def Message processData(Message message) {
  def input = new JsonSlurper().parseText(message.getBody(String))

  def transformed = [
    order: [
      id: input.orderId
    ],
    customer: [
      name: input.customerName,
      location: input.city
    ]
  ]

  message.setBody(JsonOutput.toJson(transformed))
  return message


}

/*
{
  "orderId": "A100",
  "customerName": "John",
  "city": "Zurich"
}
*/