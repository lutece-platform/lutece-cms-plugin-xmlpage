/*
 * Copyright (c) 2002-2012, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.xmlpage.util;

import fr.paris.lutece.plugins.xmlpage.service.XmlPageElement;
import fr.paris.lutece.plugins.xmlpage.service.XmlPageXslContent;
import fr.paris.lutece.portal.service.html.XmlTransformerService;
import fr.paris.lutece.portal.service.util.AppLogService;

import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.stream.StreamSource;


/**
 * Content utility class for selecting transformation result.
 */
public class XmlPageContentUtils
{
    private static final String PARAMETER_XSL_URL_XMLPAGE = "urlXmlpage";
    private static final String PARAMETER_XSL_PATH_RESOURCES = "pathResources";
    private static final String PATH_SEPARATOR = "/";

    /**
     * Default constructor
     */
    protected XmlPageContentUtils(  )
    {
        // nothing to do
    }

    /**
     * Returns the Html Content of the XmlPageElement
     * If the content is not found in cache, generate it and add it to the cache.
     *
     * @param xmlPageElement The XmlPageElement object
     * @param strStyle style format wanted
     * @return the html code of the dbPage
     */
    public static String getContent( XmlPageElement xmlPageElement, String strStyle, boolean tranformCache,
        Map<String, String> mapParameters )
    {
        String strContent = "";

        if ( xmlPageElement != null )
        {
            XmlPageXslContent xslContent = xmlPageElement.getXslContent( strStyle );

            if ( xslContent != null )
            {
                AppLogService.debug( "getContent for " + strStyle + " for page " + xmlPageElement.getName(  ) );

                try
                {
                    strContent = xslContent.getResultContent(  );

                    //If transform cache is not active or if content is not already transform
                    if ( !tranformCache || ( strContent == null ) )
                    {
                        // Do the XSLT transformation of XML File
                        AppLogService.debug( "Transformation of XML File " + xmlPageElement.getName(  ) + " using XSL" );

                        String strXmlFilePath = xmlPageElement.getXmlFilesDirectoryPath(  ).concat( PATH_SEPARATOR )
                                                              .concat( xmlPageElement.getXmlFileName(  ) );
                        String strXslFilePath = xmlPageElement.getXslFilesDirectoryPath(  ).concat( PATH_SEPARATOR )
                                                              .concat( xslContent.getFileName(  ) );

                        StreamSource sourceXml = new StreamSource( strXmlFilePath );
                        StreamSource sourceStyleSheet = new StreamSource( strXslFilePath );

                        Map<String, String> mapParams = new HashMap<String, String>(  );
                        mapParams.put( PARAMETER_XSL_URL_XMLPAGE, xmlPageElement.getDisplayLink(  ) );
                        mapParams.put( PARAMETER_XSL_PATH_RESOURCES, xmlPageElement.getResourceFilesDirectoryPath(  ) );

                        if ( mapParameters != null )
                        {
                            mapParams.putAll( mapParameters );
                        }

                        XmlTransformerService xmlTransformerService = new XmlTransformerService(  );
                        strContent = xmlTransformerService.transformBySourceWithXslCache( sourceXml, sourceStyleSheet,
                                strXslFilePath, mapParams, null );
                        xslContent.setResultContent( strContent );
                    }
                }
                catch ( Exception e )
                {
                    AppLogService.error( e.getMessage(  ), e );

                    return null;
                }
            }
        }

        return strContent;
    }
}
