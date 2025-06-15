import groovy.json.JsonSlurper
import groovy.json.JsonOutput
import com.sap.gateway.ip.core.customdev.util.*;
import java.text.SimpleDateFormat
import java.util.Date

def Message processData(Message message) {
  def now = new Date()
  def sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
  def timestamp = sdf.format(now)

  def response = [
    status: "success",
    timestamp: timestamp
  ]

  message.setBody(JsonOutput.toJson(response))
  return message

}

/*
{
  "status": "success",
  "timestamp": "2025-06-11T10:30:00Z"
}
*/