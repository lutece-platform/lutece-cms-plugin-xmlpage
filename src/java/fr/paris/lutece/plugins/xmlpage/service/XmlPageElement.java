/*
 * Copyright (c) 2002-2014, Mairie de Paris
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
package fr.paris.lutece.plugins.xmlpage.service;

import fr.paris.lutece.portal.service.resource.Resource;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Map;


/**
 * This class represents one XML Page element managed by this plugin
 */
public final class XmlPageElement implements Resource
{
    private String _strName;
    private String _strTitle;
    private String _strXmlFileName;
    private String _strXmlFilesDirectoryPath;
    private String _strXslFilesDirectoryPath;
    private String _strResourceFilesDirectoryPath;
    private String _strDisplayLink;
    private Map _listXslContent;
    private Boolean _bIsValidationRequired;
    private String _strXsdSchema;

    /**
     * @return Returns the _bIsValidationRequired.
     */
    public Boolean getIsValidationRequired(  )
    {
        return _bIsValidationRequired;
    }

    /**
     * @param bIsValidationRequired The _bIsValidationRequired to set.
     */
    public void setIsValidationRequired( Boolean bIsValidationRequired )
    {
        _bIsValidationRequired = ( bIsValidationRequired == null ) ? Boolean.FALSE : bIsValidationRequired;
    }

    /**
     * @return Returns the _strName.
     */
    public String getName(  )
    {
        return _strName;
    }

    /**
     * @param strName The _strName to set.
     */
    public void setName( String strName )
    {
        _strName = strName;
    }

    /**
     * @return Returns the _strTitle.
     */
    public String getTitle(  )
    {
        return _strTitle;
    }

    /**
     * @param strTitle The _strTitle to set.
     */
    public void setTitle( String strTitle )
    {
        _strTitle = strTitle;
    }

    /**
     * @return Returns the _strXmlFileName.
     */
    public String getXmlFileName(  )
    {
        return _strXmlFileName;
    }

    /**
     * @param strXmlFileName The _strXmlFileName to set.
     */
    public void setXmlFileName( String strXmlFileName )
    {
        _strXmlFileName = strXmlFileName;
    }

    /**
     * @return Returns the _strXsdSchema.
     */
    public String getXsdSchema(  )
    {
        return _strXsdSchema;
    }

    /**
     * @param strXsdSchema The _strXsdSchema to set.
     */
    public void setXsdSchema( String strXsdSchema )
    {
        _strXsdSchema = strXsdSchema;
    }

    /**
     * @return Returns the _strXmlFilesDirectoryPath.
     */
    public String getXmlFilesDirectoryPath(  )
    {
        return _strXmlFilesDirectoryPath;
    }

    /**
     * @param strXmlFilesDirectoryPath The _strXmlFilesDirectoryPath to set.
     */
    public void setXmlFilesDirectoryPath( String strXmlFilesDirectoryPath )
    {
        _strXmlFilesDirectoryPath = strXmlFilesDirectoryPath;
    }

    /**
     * @return Returns the _strXslFilesDirectoryPath.
     */
    public String getXslFilesDirectoryPath(  )
    {
        return _strXslFilesDirectoryPath;
    }

    /**
     * @param strXslFilesDirectoryPath The _strXslFilesDirectoryPath to set.
     */
    public void setXslFilesDirectoryPath( String strXslFilesDirectoryPath )
    {
        _strXslFilesDirectoryPath = strXslFilesDirectoryPath;
    }

    /**
     * @return Returns the _strResourceFilesDirectoryPath.
     */
    public String getResourceFilesDirectoryPath(  )
    {
        return _strResourceFilesDirectoryPath;
    }

    /**
     * @param strResourceFilesDirectoryPath The _strResourceFilesDirectoryPath to set.
     */
    public void setResourceFilesDirectoryPath( String strResourceFilesDirectoryPath )
    {
        _strResourceFilesDirectoryPath = strResourceFilesDirectoryPath;
    }

    /**
     * @return Returns the _strDisplayLink.
     */
    public String getDisplayLink(  )
    {
        return _strDisplayLink;
    }

    /**
     * @param strDisplayLink The _strDisplayLink to set.
     */
    public void setDisplayLink( String strDisplayLink )
    {
        _strDisplayLink = strDisplayLink;
    }

    /**
     * @return Returns the _listXslContent.
     */
    public Map getListXslContent(  )
    {
        return _listXslContent;
    }

    /**
     * @param listXslContent The _listXslContent to set.
     */
    public void setListXslContent( Map listXslContent )
    {
        _listXslContent = listXslContent;
    }

    /**
     * @param strStyleKey the output for which to retrieve the xslContent
     * @return Returns the XslContent for a given style
     */
    public XmlPageXslContent getXslContent( String strStyleKey )
    {
        return (XmlPageXslContent) _listXslContent.get( strStyleKey );
    }

    /**
     * toString method
     * @return String including all data of this bean
     */
    public String toString(  )
    {
        return new ToStringBuilder( this ).append( "_strName", _strName ).append( "_strTitle", _strTitle )
                                          .append( "_strXmlFileName", _strXmlFileName )
                                          .append( "_strXmlFilesDirectoryPath", _strXmlFilesDirectoryPath )
                                          .append( "_strXslFilesDirectoryPath", _strXslFilesDirectoryPath )
                                          .append( "_strResourceFilesDirectoryPath", _strResourceFilesDirectoryPath )
                                          .append( "_strDisplayLink", _strDisplayLink )
                                          .append( "_bIsValidationRequired", _bIsValidationRequired )
                                          .append( "_strXsdSchema", _strXsdSchema )
                                          .append( "_listXslContent", _listXslContent ).toString(  );
    }
}
