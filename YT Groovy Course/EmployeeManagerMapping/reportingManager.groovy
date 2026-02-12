import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.*

Message processData(Message message) {

    // 1. Read TARGET payload (after mapping) - streaming
    def targetReader = message.getBody(java.io.Reader)
    def targetXml = new XmlParser().parse(targetReader)

    // 2. Read SOURCE payload from property
    def sourcePayload = message.getProperty("SOURCE_PAYLOAD")
    if (!sourcePayload) {
        message.setBody(XmlUtil.serialize(targetXml))
        return message
    }

    def sourceXml = new XmlSlurper().parseText(sourcePayload)

    // 3. Build EMP -> MGR lookup
    Map<String, String> mgrMap = [:]
    sourceXml.Employee.each { e ->
        def empId = e.EMP_ID.text()
        def mgrId = e.MGR_ID.text()
        if (mgrId) {
            mgrMap[empId] = mgrId
        }
    }

    // 4. Populate REPORTING_LINE
    targetXml.EmployeeLine.each { empLine ->
        def empId = empLine.EMP_ID.text()
        def rl = empLine.REPORTING_LINE[0]
        rl.children().clear()

        def mgr = mgrMap[empId]
        Set visited = []

        while (mgr && !visited.contains(mgr)) {
            rl.appendNode("manager", mgr)
            visited << mgr
            mgr = mgrMap[mgr]
        }
    }

    // 5. Cleanup property (CPI way)
    message.setProperty("SOURCE_PAYLOAD", null)

    message.setBody(XmlUtil.serialize(targetXml))
    return message
}
