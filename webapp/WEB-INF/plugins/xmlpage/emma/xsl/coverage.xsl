<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    
    <xsl:template match="report">
        <html>
            <head>
                <style>
                    
                    div.barchart
                    {
                    background-color: #EEEEEE;
                    border: #808080 1px solid;
                    height: 1.3em;
                    margin: 0px;
                    padding: 0px;
                    width: 100px;
                    }
                    
                    div.barchart span.text
                    {
                    display: block;
                    position: absolute;
                    margin: 0px;
                    padding: 0px;
                    text-align: center;
                    width: 100px;
                    height: 1.3em;
                    
                    }
                    
                    div.barchart div.good
                    {
                    background-color: #859ECF;
                    height: 1.3em;
                    margin: 0px;
                    padding: 0px;
                    }
                    
                    div.barchart div.bad
                    {
                    background-color: #DC143C;
                    height: 1.3em;
                    margin: 0px;
                    padding: 0px;
                    border: solid gray;
                    border-width:1px
                    }
                    
                    div.barchart div.notyet
                    {
                    background-color: #EEEEEE;
                    height: 1.3em;
                    margin: 0px;
                    padding: 0px;
                    }
                    
                    div.barchart div.emma-text
                    {
                    padding: 0px;
                    height: 1.3em;
                    }
                    
                    div.percent-good
                    {
                    color: #00AA00;
                    }
                    div.percent-bad
                    {
                    color: #AA0000;
                    }
                    
                    table.emma-table {
                    background-color: White;
                    border: 1px solid #C6C9C1;
                    border-collapse: collapse;
                    margin-bottom: 10px;
                    width: 100%;
                    }
                    /* table cell */
                    table.emma-table td.percent-good {
                    color:00FF00;
                    border-left: 1px solid #C6C9C1;
                    vertical-align: middle;
                    text-align: right;
                    }

                    table.emma-table td.package {
                    font-weigth: bold;
                    padding-left: 10px;
                    }

                    table.emma-table td.class {
                    padding-left: 30px;
                    }

                    table.emma-table td.percent-bad {
                    color:FF0000;
                    border-left: 1px solid #C6C9C1;
                    vertical-align: middle;
                    text-align: right;
                    }

                    /* table cell */
                    table.emma-table td.barchart {
                    border-right: 1px solid #C6C9C1;
                    vertical-align: middle;
                    }
                    table.emma-table th {
                    background-color: #E6EAF3;
                    border-bottom: 1px solid #C6C9C1;
                    border-left: 1px solid #C6C9C1;
                    border-right: 1px solid #C6C9C1;
                    color: #1C2861;
                    font-weight: bold;
                    height: 20px;
                    vertical-align: middle;
                    }
                    /* basic row style */
                    table.emma-table tr.package {
                    background-color: #E6EAF3;
                    border: 1px dotted #C6C9C1;
                    }
                    table.emma-table tr.class {
                    background-color: #F8F8F8;
                    }
                    /* style for even rows */
                    table.emma-table tr.even-row {
                    background-color: White;
                    }
                    /* styles for odd rows */
                    table.emma-table tr.odd-row {
                    background-color: #F8F8F8;
                    }
                    
                </style>
            </head>
            <body>
                <h2 class="page-title">
                    Couverture des tests unitaires
                </h2>
                <xsl:apply-templates select="stats"/>
                <xsl:apply-templates select="data/all"/>
            </body>
        </html>
    </xsl:template>
    
    <xsl:template match="stats">
        <h3>Statistiques générales</h3>
        <ul>
            <li>Packages : <xsl:value-of select="packages/@value"/></li>
            <li>Classes : <xsl:value-of select="classes/@value"/></li>
            <li>Methodes : <xsl:value-of select="methods/@value"/></li>
            <li>Fichiers sources : <xsl:value-of select="srcfiles/@value"/></li>
            <li>Lignes de source : <xsl:value-of select="srclines/@value"/></li>
        </ul>
    </xsl:template>
    
    <xsl:template match="data/all">
        <h3>Statistiques globales de couverture</h3>
        <table class="emma-table">
            <tr>
                <th colspan="2">Classes</th>
                <th colspan="2">Methodes</th>
                <th colspan="2">Blocs</th>
                <th colspan="2">Lignes</th>
            </tr>
            <tr>
                <xsl:apply-templates select="coverage"/>
            </tr>
        </table>
        <h3>Statistiques de couverture détaillées par package</h3>
        <table class="emma-table">
            <tr>
                <th>Package</th>
                <th colspan="2">Classes</th>
                <th colspan="2">Methodes</th>
                <th colspan="2">Blocs</th>
                <th colspan="2">Lignes</th>
            </tr>
            
            <xsl:apply-templates select="package"/>
        </table>
    </xsl:template>
    
    <xsl:template match="package">
        <tr class="package">
            <td class="package"><xsl:value-of select="@name"/></td>
            <xsl:apply-templates select="coverage"/>
        </tr>
        <xsl:apply-templates select="srcfile"/>
    </xsl:template>
    
    <xsl:template match="srcfile">
        <tr class="class">
            <td class="class"><xsl:value-of select="substring-before(@name,'.')"/></td>
            <xsl:apply-templates select="coverage"/>
        </tr>
    </xsl:template>

    <xsl:template match="coverage">
        
        <xsl:call-template name="bar-chart">
            <xsl:with-param name="percent" select="substring-before(@value,'%')"/>
            <xsl:with-param name="values" select="substring-after(@value,'%')"/>
        </xsl:call-template>
        
    </xsl:template>
    
    
    <xsl:template name="bar-chart">
        <xsl:param name="percent"/>
        <xsl:param name="values"/>
        
        <xsl:choose>
            <xsl:when test="number($percent) > 79">
                <td class="percent-good">
                    <div class="percent-good">
                        <xsl:value-of select="$percent"/> %
                    </div>
                </td>    
                <td class="barchart">
                    <div class="barchart">
                        <div class="good" style="width:{$percent}px">
                            <span class="text">
                                <xsl:value-of select="$values"/>         
                            </span>
                        </div>
                    </div>
                </td>
                
            </xsl:when>
            <xsl:when test="number($percent) = 0">
                <td class="percent-bad">
                    <div class="percent-bad">
                        <xsl:value-of select="$percent"/> %
                    </div>
                </td>    
                <td class="barchart">
                    <div class="barchart">
                        <div class="notyet" >
                            <span class="text">
                                <xsl:value-of select="$values"/>         
                            </span>
                        </div>
                    </div>
                </td>
            </xsl:when>
            <xsl:when test="number($percent) > 0">
                <td class="percent-bad">
                    <div class="percent-bad">
                        <xsl:value-of select="$percent"/> %
                    </div>
                </td>    
                <td class="barchart">
                    <div class="barchart">
                        <div class="good" style="width:{$percent}px">
                            <span class="text">
                                <xsl:value-of select="$values"/>         
                            </span>
                        </div>
                    </div>
                </td>
            </xsl:when>
        </xsl:choose>
    </xsl:template>  
    
</xsl:stylesheet>
