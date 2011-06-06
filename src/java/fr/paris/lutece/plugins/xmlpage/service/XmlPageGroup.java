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
package fr.paris.lutece.plugins.xmlpage.service;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Map;
import java.util.Set;


/**
 * This class is used  to gather all the XmlPage data generic to a group of pages.
 * It is only used by the daemon responsible for the copy and validation of the
 * xml files. It provides an organised structure more easy to handle for the daemon.
 */
public final class XmlPageGroup
{
    private String _strName;
    private String _strInputFilesDirectoryPath;
    private String _strLockPublicationPath;
    private String _strLockTransfertPath;
    private String _strXsdFilesDirectoryPath;
    private String _strMailSenderName;
    private String _strMailSenderEmail;
    private String _strMailRecipientList;
    private String _strMailValidationOkSubject;
    private String _strMailValidationKoSubject;
    private String _strMailPublicationOkSubject;
    private String _strMailPublicationKoSubject;
    private Set _listExtensionFileCopy;
    private Map _listXmlPageElement;

    /**
     * @return Returns the _listXmlPageElement.
     */
    public Map getListXmlPageElement(  )
    {
        return _listXmlPageElement;
    }

    /**
     * @param listXmlPageElement The _listXmlPageElement to set.
     */
    public void setListXmlPageElement( Map listXmlPageElement )
    {
        _listXmlPageElement = listXmlPageElement;
    }

    /**
     * @param strPageName the name of the searching page
     * @return Returns the XmlPageElement for a given page name
     */
    public XmlPageElement getXmlPageElement( String strPageName )
    {
        return (XmlPageElement) _listXmlPageElement.get( strPageName );
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
     * @return Returns the _strInputFilesDirectoryPath.
     */
    public String getInputFilesDirectoryPath(  )
    {
        return _strInputFilesDirectoryPath;
    }

    /**
     * @param strInputFilesDirectoryPath The _strInputFilesDirectoryPath to set.
     */
    public void setInputFilesDirectoryPath( String strInputFilesDirectoryPath )
    {
        _strInputFilesDirectoryPath = strInputFilesDirectoryPath;
    }

    /**
     * @return Returns the _listExtensionFileCopy.
     */
    public Set getListExtensionFileCopy(  )
    {
        return _listExtensionFileCopy;
    }

    /**
     * @param listExtensionFileCopy The _listExtensionFileCopy to set.
     */
    public void setListExtensionFileCopy( Set listExtensionFileCopy )
    {
        _listExtensionFileCopy = listExtensionFileCopy;
    }

    /**
     * @return Returns the _strLockPublicationPath.
     */
    public String getLockPublicationPath(  )
    {
        return _strLockPublicationPath;
    }

    /**
     * @param strLockPublicationPath The _strLockPublicationPath to set.
     */
    public void setLockPublicationPath( String strLockPublicationPath )
    {
        _strLockPublicationPath = strLockPublicationPath;
    }

    /**
     * @return Returns the _strLockTransfertPath.
     */
    public String getLockTransfertPath(  )
    {
        return _strLockTransfertPath;
    }

    /**
     * @param strLockTransfertPath The _strLockTransfertPath to set.
     */
    public void setLockTransfertPath( String strLockTransfertPath )
    {
        _strLockTransfertPath = strLockTransfertPath;
    }

    /**
     * @return Returns the _strMailRecipientList.
     */
    public String getMailRecipientList(  )
    {
        return _strMailRecipientList;
    }

    /**
     * @param strMailRecipientList The _strMailRecipientList to set.
     */
    public void setMailRecipientList( String strMailRecipientList )
    {
        _strMailRecipientList = strMailRecipientList;
    }

    /**
     * @return Returns the _strMailPublicationKoSubject.
     */
    public String getMailPublicationKoSubject(  )
    {
        return _strMailPublicationKoSubject;
    }

    /**
     * @param strMailPublicationKoSubject The _strMailPublicationKoSubject to set.
     */
    public void setMailPublicationKoSubject( String strMailPublicationKoSubject )
    {
        _strMailPublicationKoSubject = strMailPublicationKoSubject;
    }

    /**
     * @return Returns the _strMailPublicationOkSubject.
     */
    public String getMailPublicationOkSubject(  )
    {
        return _strMailPublicationOkSubject;
    }

    /**
     * @param strMailPublicationOkSubject The _strMailPublicationOkSubject to set.
     */
    public void setMailPublicationOkSubject( String strMailPublicationOkSubject )
    {
        _strMailPublicationOkSubject = strMailPublicationOkSubject;
    }

    /**
     * @return Returns the _strMailSenderEmail.
     */
    public String getMailSenderEmail(  )
    {
        return _strMailSenderEmail;
    }

    /**
     * @param strMailSenderEmail The _strMailSenderEmail to set.
     */
    public void setMailSenderEmail( String strMailSenderEmail )
    {
        _strMailSenderEmail = strMailSenderEmail;
    }

    /**
     * @return Returns the _strMailSenderName.
     */
    public String getMailSenderName(  )
    {
        return _strMailSenderName;
    }

    /**
     * @param strMailSenderName The _strMailSenderName to set.
     */
    public void setMailSenderName( String strMailSenderName )
    {
        _strMailSenderName = strMailSenderName;
    }

    /**
     * @return Returns the _strMailValidationKoSubject.
     */
    public String getMailValidationKoSubject(  )
    {
        return _strMailValidationKoSubject;
    }

    /**
     * @param strMailValidationKoSubject The _strMailValidationKoSubject to set.
     */
    public void setMailValidationKoSubject( String strMailValidationKoSubject )
    {
        _strMailValidationKoSubject = strMailValidationKoSubject;
    }

    /**
     * @return Returns the _strMailValidationOkSubject.
     */
    public String getMailValidationOkSubject(  )
    {
        return _strMailValidationOkSubject;
    }

    /**
     * @param strMailValidationOkSubject The _strMailValidationOkSubject to set.
     */
    public void setMailValidationOkSubject( String strMailValidationOkSubject )
    {
        _strMailValidationOkSubject = strMailValidationOkSubject;
    }

    /**
     * @return Returns the _strXsdFilesDirectoryPath.
     */
    public String getXsdFilesDirectoryPath(  )
    {
        return _strXsdFilesDirectoryPath;
    }

    /**
     * @param strXsdFilesDirectoryPath The _strXsdFilesDirectoryPath to set.
     */
    public void setXsdFilesDirectoryPath( String strXsdFilesDirectoryPath )
    {
        _strXsdFilesDirectoryPath = strXsdFilesDirectoryPath;
    }

    /**
     * toString method
     * @return String including all data of this bean
     */
    public String toString(  )
    {
        return new ToStringBuilder( this ).append( "_strName", _strName )
                                          .append( "_strInputFilesDirectoryPath", _strInputFilesDirectoryPath )
                                          .append( "_strLockPublicationPath", _strLockPublicationPath )
                                          .append( "_strLockTransfertPath", _strLockTransfertPath )
                                          .append( "_strXsdFilesDirectoryPath", _strXsdFilesDirectoryPath )
                                          .append( "_strMailSenderName", _strMailSenderName )
                                          .append( "_strMailSenderEmail", _strMailSenderEmail )
                                          .append( "_strMailRecipientList", _strMailRecipientList )
                                          .append( "_strMailValidationOkSubject", _strMailValidationOkSubject )
                                          .append( "_strMailValidationKoSubject", _strMailValidationKoSubject )
                                          .append( "_strMailPublicationOkSubject", _strMailPublicationOkSubject )
                                          .append( "_strMailPublicationKoSubject", _strMailPublicationKoSubject )
                                          .append( "_listExtensionFileCopy", _listExtensionFileCopy )
                                          .append( "_listXmlPageElement", _listXmlPageElement ).toString(  );
    }
}
