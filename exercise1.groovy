import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
    // Step 1: Read body
    def body = message.getBody(String)

    // Step 2: Add header
    message.setHeader('ProcessedBy', 'GroovyScript')

    // Step 3: Convert to uppercase
    body = body.toUpperCase()

    // Step 4: Set modified body
    message.setBody(body)

    return message
}
