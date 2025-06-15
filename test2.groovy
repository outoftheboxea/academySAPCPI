import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
    
// Get the body content
    def body = message.getBody(String.class)

// Your code here
    body = body.replace('Hello', 'HelloAcademy')

// Set the body back
    message.setBody(body)

    return message
}
