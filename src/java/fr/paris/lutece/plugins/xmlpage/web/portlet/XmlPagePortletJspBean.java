/*
 * Copyright (c) 2002-2011, Mairie de Paris
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
package fr.paris.lutece.plugins.xmlpage.web.portlet;

import fr.paris.lutece.plugins.xmlpage.business.portlet.XmlPagePortlet;
import fr.paris.lutece.plugins.xmlpage.business.portlet.XmlPagePortletHome;
import fr.paris.lutece.plugins.xmlpage.service.XmlPageService;
import fr.paris.lutece.portal.business.portlet.PortletHome;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.portal.web.portlet.PortletJspBean;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides the user interface to manage XmlPage Portlet features
 */
public class XmlPagePortletJspBean extends PortletJspBean
{
    ///////////////////////////////////////////////////////////////////////////////////
    // Constants
    private static final String PROPERTY_PREFIX = "portlet.xmlpage";
    private static final String PARAMETER_PAGE_ID = "page_id";
    private static final String PARAMETER_PORTLET_ID = "portlet_id";
    private static final String PARAMETER_PORTLET_TYPE_ID = "portlet_type_id";
    private static final String PARAMETER_XMLPAGE_NAME = "xmlpage_name";
    private static final String PARAMETER_VALUE_SEPARATOR = "-";
    private static final String CONSTANT_EMPTY_STRING = "";
    private static final String MARK_XML_PAGES_LIST = "xml_pages_list";
    private static final String MARK_XML_PAGE_ID = "xml_page_id";

    /**
     * Returns the properties prefix used for XmlPage portlet and defined in lutece.properties file
     *
     * @return the value of the property prefix
     */
    public String getPropertiesPrefix(  )
    {
        return PROPERTY_PREFIX;
    }

    /**
     * Returns the XmlPage Portlet form of creation
     *
     * @param request The Http rquest
     * @return the html code of the XmlPage portlet form
     */
    public String getCreate( HttpServletRequest request )
    {
        String strPageId = request.getParameter( PARAMETER_PAGE_ID );
        String strPortletTypeId = request.getParameter( PARAMETER_PORTLET_TYPE_ID );
        HashMap model = new HashMap(  );
        model.put( MARK_XML_PAGES_LIST, XmlPageService.getInstance(  ).getXmlPageList(  ) );

        HtmlTemplate template = getCreateTemplate( strPageId, strPortletTypeId, model );

        return template.getHtml(  );
    }

    /**
     * Returns the XmlPage Portlet form for update
     *
     * @param request The Http request
     * @return the html code of the XmlPage portlet form
     */
    public String getModify( HttpServletRequest request )
    {
        String strPortletId = request.getParameter( PARAMETER_PORTLET_ID );
        int nPortletId = Integer.parseInt( strPortletId );
        XmlPagePortlet portlet = (XmlPagePortlet) PortletHome.findByPrimaryKey( nPortletId );
        String strParamValue = portlet.getPageName(  ).concat( PARAMETER_VALUE_SEPARATOR ).concat( portlet.getStyle(  ) );
        HashMap model = new HashMap(  );
        model.put( MARK_XML_PAGES_LIST, XmlPageService.getInstance(  ).getXmlPageList(  ) );
        model.put( MARK_XML_PAGE_ID, strParamValue );

        HtmlTemplate template = getModifyTemplate( portlet, model );

        return template.getHtml(  );
    }

    /**
     * Treats the creation form of a new XmlPage portlet
     *
     * @param request The Http request
     * @return The jsp URL which displays the view of the created XmlPage portlet
     */
    public String doCreate( HttpServletRequest request )
    {
        XmlPagePortlet portlet = new XmlPagePortlet(  );

        // recovers portlet specific attributes
        String strPageId = request.getParameter( PARAMETER_PAGE_ID );
        int nPageId = Integer.parseInt( strPageId );
        String strXmlPageName = request.getParameter( PARAMETER_XMLPAGE_NAME );
        int indexSeparator = strXmlPageName.lastIndexOf( PARAMETER_VALUE_SEPARATOR );
        String strPageName = strXmlPageName.substring( 0, indexSeparator );
        String strStyle = strXmlPageName.substring( indexSeparator + 1 );

        String strStyleId = request.getParameter( Parameters.STYLE );

        if ( ( strXmlPageName == null ) || strXmlPageName.trim(  ).equals( CONSTANT_EMPTY_STRING ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        // recovers portlet common attributes
        setPortletCommonData( request, portlet );

        String strName = portlet.getName(  );

        if ( ( strName == null ) || strName.trim(  ).equals( CONSTANT_EMPTY_STRING ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        portlet.setPageId( nPageId );
        portlet.setPageName( strPageName );
        portlet.setStyle( strStyle );

        // Creates the portlet
        XmlPagePortletHome.getInstance(  ).create( portlet );

        // displays the page with the new portlet
        return getPageUrl( portlet.getPageId(  ) );
    }

    /**
     * Treats the update form of the XmlPage portlet whose identifier is in the http request
     *
     * @param request The Http request
     * @return The jsp URL which displays the view of the updated portlet
     */
    public String doModify( HttpServletRequest request )
    {
        // recovers portlet attributes
        String strPortletId = request.getParameter( PARAMETER_PORTLET_ID );
        int nPortletId = Integer.parseInt( strPortletId );
        XmlPagePortlet portlet = (XmlPagePortlet) PortletHome.findByPrimaryKey( nPortletId );

        String strStyleId = request.getParameter( Parameters.STYLE );

        if ( ( strStyleId == null ) || strStyleId.trim(  ).equals( CONSTANT_EMPTY_STRING ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        // recovers portlet common attributes
        setPortletCommonData( request, portlet );

        // mandatory field
        String strName = portlet.getName(  );

        if ( strName.trim(  ).equals( CONSTANT_EMPTY_STRING ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        // recovers portlet specific attributes
        String strXmlPageName = request.getParameter( PARAMETER_XMLPAGE_NAME );
        int indexSeparator = strXmlPageName.lastIndexOf( PARAMETER_VALUE_SEPARATOR );
        String strPageName = strXmlPageName.substring( 0, indexSeparator );
        String strStyle = strXmlPageName.substring( indexSeparator + 1 );
        portlet.setPageName( strPageName );
        portlet.setStyle( strStyle );

        // updates the portlet
        portlet.update(  );

        // displays the page with the new portlet
        return getPageUrl( portlet.getPageId(  ) );
    }
}
