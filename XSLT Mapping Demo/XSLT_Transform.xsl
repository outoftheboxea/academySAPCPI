<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" 
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:output method="xml" indent="yes"/>

  <xsl:template match="/Order">
    <OrderInfo>
      <ID><xsl:value-of select="OrderID"/></ID>
      <CustomerName><xsl:value-of select="Customer/Name"/></CustomerName>
      <CustomerEmail><xsl:value-of select="Customer/Email"/></CustomerEmail>
      <OrderLines>
        <xsl:for-each select="Items/Item">
          <Line>
            <ProductName><xsl:value-of select="Product"/></ProductName>
            <Qty><xsl:value-of select="Quantity"/></Qty>
          </Line>
        </xsl:for-each>
      </OrderLines>
    </OrderInfo>
  </xsl:template>

</xsl:stylesheet>
