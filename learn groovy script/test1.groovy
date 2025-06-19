import com.sap.gateway.ip.core.customdev.util.Message;
def Message processData(Message message) {
  // Get the body content
  def payload = message.getBody(String.class);
  // Set the body back
  message.setBody("Hello,"+payload);
  return message;
}
