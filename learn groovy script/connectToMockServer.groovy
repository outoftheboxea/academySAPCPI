// SAP Integration Helper by OutOfTheBoxEA
// https://www.youtube.com/@OutOfTheBoxEA

import com.sap.gateway.ip.core.customdev.util.Message
import java.net.URL
import java.net.HttpURLConnection
import java.io.InputStreamReader
import java.io.BufferedReader

def Message processData(Message message) {
    // Define the endpoint
    def serviceUrl = "https://sapmock.outoftheboxacademy.com/sap/opu/odata/sap/API_SALES_ORDER_SRV/"

    // Open connection
    URL url = new URL(serviceUrl)
    HttpURLConnection connection = (HttpURLConnection) url.openConnection()
    connection.setRequestMethod("GET")
    connection.setRequestProperty("Accept", "application/xml")

    // Get response
    def responseCode = connection.responseCode
    if (responseCode == 200) {
        def responseStream = connection.inputStream
        def reader = new BufferedReader(new InputStreamReader(responseStream))
        def responseXml = new StringBuilder()
        String line
        while ((line = reader.readLine()) != null) {
            responseXml.append(line)
        }
        reader.close()
        message.setBody(responseXml.toString())
    } else {
        message.setBody("Error: HTTP response code ${responseCode}")
    }

    return message
}
