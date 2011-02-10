<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">


<xsl:template match="portlet">
	<div class="portlet .-lutece-border-radius">
		<xsl:if test="not(string(display-portlet-title)='1')">
			<div class="portlet-header">
				<xsl:value-of disable-output-escaping="yes" select="portlet-name" />
			</div>
		</xsl:if>

		<div class="portlet-content">
			<xsl:apply-templates select="xmlpage-portlet" />
		</div>
	</div>
</xsl:template>

<xsl:template match="xmlpage-portlet">
		
	       <xsl:choose>
	         <xsl:when test="normalize-space(xmlpage-portlet-content)">
	         	<xsl:apply-templates select="xmlpage-portlet-content" />
	         </xsl:when>
	         <xsl:otherwise>
	         	&#160;
	         </xsl:otherwise>
	       </xsl:choose>
	
</xsl:template>

<xsl:template match="xmlpage-portlet-content">
	<xsl:value-of disable-output-escaping="yes" select="." />
</xsl:template>

</xsl:stylesheet>
