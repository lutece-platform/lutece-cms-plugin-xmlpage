/*
 * Copyright (c) 2002-2013, Mairie de Paris
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

import fr.paris.lutece.plugins.xmlpage.util.XmlPageFileUtils;
import fr.paris.lutece.plugins.xmlpage.util.XmlPageXercesErrorHandler;
import fr.paris.lutece.portal.service.mail.MailService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.html.HtmlTemplate;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.File;
import java.io.IOException;

import java.util.Collection;
import java.util.Iterator;


/**
 * Service for Daemon FetchXmlFilesDaemon
 */
public class FetchXmlFilesService
{
    private static final String PROPERTY_DIRECTORY_TEMP_EXTENSION = "xmlpage.directory.temp.extension";
    private static final String PROPERTY_PAGE_MAIL_VALIDATION_OK_MESSAGE = "skin/plugins/xmlpage/validation_ok.html";
    private static final String PROPERTY_PAGE_MAIL_VALIDATION_KO_MESSAGE = "skin/plugins/xmlpage/validation_ko.html";
    private static final String PROPERTY_PAGE_MAIL_PUBLICATION_OK_MESSAGE = "skin/plugins/xmlpage/publication_ok.html";
    private static final String PROPERTY_PAGE_MAIL_PUBLICATION_KO_MESSAGE = "skin/plugins/xmlpage/publication_ko.html";
    private static final String BOOKMARK_MAIL_GROUP_NAME = "@group_name@";
    private static final String BOOKMARK_MAIL_ERROR_MESSAGE = "@error_message@";
    private static final String PATH_SEPARATOR = "/";
    private static final String EMPTY_STRING = "";
    private static final String LINE_SEPARATOR = System.getProperty( "line.separator" );
    private static final String XML_VALIDATOR_XML_READER = "org.apache.xerces.parsers.SAXParser";
    private static final String XML_VALIDATOR_FEATURE_NAMESPACE = "http://xml.org/sax/features/namespaces";
    private static final String XML_VALIDATOR_FEATURE_VALIDATION = "http://xml.org/sax/features/validation";
    private static final String XML_VALIDATOR_FEATURE_VALIDATION_SCHEMA = "http://apache.org/xml/features/validation/schema";
    private static final String XML_VALIDATOR_VALIDATION_SCHEMA_FULL_CHECKING = "http://apache.org/xml/features/validation/schema-full-checking";
    private static final String XML_VALIDATOR_PROPERTY_SCHEMA_NO_NAMESPACE = "http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation";
    private static FetchXmlFilesService _singleton = new FetchXmlFilesService(  );

    /**
     * Returns the instance of the singleton
     * @return The instance of the singleton
     */
    public static FetchXmlFilesService getInstance(  )
    {
        return _singleton;
    }

    /**
     * Method called by Daemon FecthXmlFilesDaemon
     * @return String contening a message to log
     */
    public String fetchXmlFiles(  )
    {
        Collection listGroup = XmlPageLoaderProperties.getAllXmlPageByGroup(  );
        Iterator itListGroup = listGroup.iterator(  );
        StringBuffer sbFinalResult = new StringBuffer(  );

        while ( itListGroup.hasNext(  ) )
        {
            XmlPageGroup xmlPageGroup = (XmlPageGroup) itListGroup.next(  );
            AppLogService.debug( "fetchXmlPages - Group name = " + xmlPageGroup.getName(  ) );

            StringBuffer sbGroupResult = new StringBuffer(  );

            // Check presence of transfert lock
            if ( checkLockFileDetected( xmlPageGroup.getLockTransfertPath(  ) ) )
            {
                sbGroupResult.append( xmlPageGroup.getName(  ) );
                sbGroupResult.append( " - The lock transfert file exists !" );
                sbGroupResult.append( LINE_SEPARATOR );
            }

            // Check presence of publication lock
            else if ( checkLockFileDetected( xmlPageGroup.getLockPublicationPath(  ) ) )
            {
                sbGroupResult.append( xmlPageGroup.getName(  ) );
                sbGroupResult.append( " - The lock publication file exists !" );
                sbGroupResult.append( LINE_SEPARATOR );
            }
            else
            {
                // Creating lock file
                if ( ( xmlPageGroup.getLockPublicationPath(  ) != null ) &&
                        !XmlPageFileUtils.createFile( xmlPageGroup.getLockPublicationPath(  ) ) )
                {
                    sbGroupResult.append( xmlPageGroup.getName(  ) );
                    sbGroupResult.append( " - Error during creation of lock file !" );
                    sbGroupResult.append( LINE_SEPARATOR );
                }

                // Validate all XML Files before copying
                sbGroupResult.append( validateAllXmlFiles( xmlPageGroup ) );

                // If all XML files are valid, copy files into WEB directory
                if ( sbGroupResult.length(  ) == 0 )
                {
                    sbGroupResult.append( copyAllXmlAndResources( xmlPageGroup ) );
                }

                // Emptying content cache
                if ( sbGroupResult.length(  ) == 0 )
                {
                    XmlPageService.getInstance(  ).resetCache(  );
                }

                // Deleting lock file
                if ( ( xmlPageGroup.getLockPublicationPath(  ) != null ) &&
                        !XmlPageFileUtils.removeFile( xmlPageGroup.getLockPublicationPath(  ) ) )
                {
                    sbGroupResult.append( xmlPageGroup.getName(  ) );
                    sbGroupResult.append( " - Error during deletion of lock file !" );
                    sbGroupResult.append( LINE_SEPARATOR );
                }
            }

            if ( ( sbGroupResult != null ) && ( sbGroupResult.length(  ) > 0 ) )
            {
                AppLogService.error( "Group result = " + sbGroupResult.toString(  ) );
                sbFinalResult.append( "fetchXmlFiles for " + xmlPageGroup.getName(  ) + " = A problem occurred" );
                sbFinalResult.append( LINE_SEPARATOR );
            }
            else
            {
                sbFinalResult.append( "fetchXmlFiles for " + xmlPageGroup.getName(  ) + " = OK" );
                sbFinalResult.append( LINE_SEPARATOR );
            }
        }

        return sbFinalResult.toString(  );
    }

    /**
     * Detect a lock file
     * @param strLockFilePath File path for lock
     * @return true if lock file is detected, false otherwise
     */
    private boolean checkLockFileDetected( String strLockFilePath )
    {
        if ( strLockFilePath != null )
        {
            File lockTransfertFile = new File( strLockFilePath );

            if ( lockTransfertFile.exists(  ) )
            {
                AppLogService.info( "checkLockFileDetected : " + strLockFilePath + " - file exists !" );

                return true;
            }

            return false;
        }
        else
        {
            return false;
        }
    }

    /**
     * Validate all XML Files from a group with theirs XSD Schemas
     * @param xmlPageGroup group to validate its files
     * @return message for logging
     */
    private String validateAllXmlFiles( XmlPageGroup xmlPageGroup )
    {
        StringBuffer sbResultat = new StringBuffer(  );
        Iterator itLstXmlPageElement = xmlPageGroup.getListXmlPageElement(  ).keySet(  ).iterator(  );

        while ( itLstXmlPageElement.hasNext(  ) )
        {
            Object xmlPageElementKey = itLstXmlPageElement.next(  );

            if ( !validateXmlFile( xmlPageGroup,
                        (XmlPageElement) xmlPageGroup.getListXmlPageElement(  ).get( xmlPageElementKey ) ) )
            {
                sbResultat.append( xmlPageGroup.getName(  ) );
                sbResultat.append( " - " );
                sbResultat.append( xmlPageElementKey );
                sbResultat.append( " - XML File not valid !" );
                sbResultat.append( LINE_SEPARATOR );
            }
        }

        // Sending validation email
        sendValidationMail( xmlPageGroup, sbResultat.toString(  ) );

        return sbResultat.toString(  );
    }

    /**
     * Validate a XML File with its XSD Schema
     * @param xmlPageGroup group to validate its files
     * @param xmlPageElement XML File informations for validating
     * @return true if the XML File is correct, false otherwise
     */
    private boolean validateXmlFile( XmlPageGroup xmlPageGroup, XmlPageElement xmlPageElement )
    {
        String strXmlFilePath = xmlPageGroup.getInputFilesDirectoryPath(  ).concat( PATH_SEPARATOR )
                                            .concat( xmlPageElement.getXmlFileName(  ) );

        File xmlFile = new File( strXmlFilePath );

        String strXsdFilePath = xmlPageGroup.getXsdFilesDirectoryPath(  ).concat( PATH_SEPARATOR )
                                            .concat( xmlPageElement.getXsdSchema(  ) );

        // if validation is required, a file is ok if it exists and is valid
        // if validation is not required, we allow the file not to exist (no check made)
        if ( xmlPageElement.getIsValidationRequired(  ).booleanValue(  ) )
        {
            // if validation is required, we should check first that the file exists.
            if ( xmlFile.exists(  ) )
            {
                try
                {
                    XMLReader xmlReader = XMLReaderFactory.createXMLReader( XML_VALIDATOR_XML_READER );
                    xmlReader.setFeature( XML_VALIDATOR_FEATURE_NAMESPACE, true );
                    xmlReader.setFeature( XML_VALIDATOR_FEATURE_VALIDATION, true );
                    xmlReader.setFeature( XML_VALIDATOR_FEATURE_VALIDATION_SCHEMA, true );
                    xmlReader.setFeature( XML_VALIDATOR_VALIDATION_SCHEMA_FULL_CHECKING, true );
                    xmlReader.setProperty( XML_VALIDATOR_PROPERTY_SCHEMA_NO_NAMESPACE, strXsdFilePath );

                    xmlReader.setErrorHandler( new XmlPageXercesErrorHandler(  ) );
                    xmlReader.parse( strXmlFilePath );

                    return true;
                }
                catch ( IOException ioe )
                {
                    AppLogService.error( "validateXmlFile : IOException", ioe );

                    return false;
                }
                catch ( SAXException saxe )
                {
                    AppLogService.error( "validateXmlFile : SAXException", saxe );

                    return false;
                }
            }
            else
            {
                // if validation is required and file is not found, log an error
                AppLogService.error( "validateXmlFile : " + strXmlFilePath + " - file doesn't exist !" );

                return false;
            }
        }

        // if validation is not required : no check
        return true;
    }

    /**
     * Copy all XML Files and resources of a group into web directory
     * @param xmlPageGroup working group
     * @return message for logging
     */
    private String copyAllXmlAndResources( XmlPageGroup xmlPageGroup )
    {
        StringBuffer sbResultat = new StringBuffer(  );
        Iterator itLstXmlPageElement = xmlPageGroup.getListXmlPageElement(  ).values(  ).iterator(  );
        String strTempXmlDir = null;
        String strOriginalXmlDir = null;
        String strPropertyResourceDir = null;

        while ( itLstXmlPageElement.hasNext(  ) )
        {
            XmlPageElement xmlPageElement = (XmlPageElement) itLstXmlPageElement.next(  );

            if ( strTempXmlDir == null )
            {
                // Creating temporary XML directory
                strOriginalXmlDir = xmlPageElement.getXmlFilesDirectoryPath(  );
                strPropertyResourceDir = xmlPageElement.getResourceFilesDirectoryPath(  );
                strTempXmlDir = strOriginalXmlDir.concat( AppPropertiesService.getProperty( 
                            PROPERTY_DIRECTORY_TEMP_EXTENSION ) );
                XmlPageFileUtils.makeDirectory( strTempXmlDir );
            }

            // Copy XML File
            XmlPageFileUtils.copyFile( xmlPageGroup.getInputFilesDirectoryPath(  ), xmlPageElement.getXmlFileName(  ),
                strTempXmlDir );
        }

        // check whether the resources should be considered
        boolean bConsiderResources = ( ( strPropertyResourceDir != null ) &&
            ( !strPropertyResourceDir.equals( EMPTY_STRING ) ) );
        String strOriginalResourcesDir = null;
        String strTempResourcesDir = null;

        if ( bConsiderResources )
        {
            // initializing original and temp resource dirs
            strOriginalResourcesDir = AppPathService.getWebAppPath(  ).concat( PATH_SEPARATOR )
                                                    .concat( strPropertyResourceDir );
            strTempResourcesDir = strOriginalResourcesDir.concat( AppPropertiesService.getProperty( 
                        PROPERTY_DIRECTORY_TEMP_EXTENSION ) );

            // Creating temporary resources directory  
            XmlPageFileUtils.makeDirectory( strTempResourcesDir );

            // Copying all resource files
            File fInputDirectory = new File( xmlPageGroup.getInputFilesDirectoryPath(  ) );
            String[] strFileNames = fInputDirectory.list(  );
            Iterator itListExtension = xmlPageGroup.getListExtensionFileCopy(  ).iterator(  );

            while ( itListExtension.hasNext(  ) )
            {
                String strExtension = (String) itListExtension.next(  );

                for ( int i = 0; i < strFileNames.length; i++ )
                {
                    if ( strFileNames[i].endsWith( strExtension ) )
                    {
                        XmlPageFileUtils.copyFile( xmlPageGroup.getInputFilesDirectoryPath(  ), strFileNames[i],
                            strTempResourcesDir );
                    }
                }
            }
        }

        // Deleting original XML 
        XmlPageFileUtils.removeDirectory( strOriginalXmlDir );

        // Renaming temporary XML
        if ( !XmlPageFileUtils.renameDirectory( strTempXmlDir, strOriginalXmlDir ) )
        {
            sbResultat.append( "Can't rename directory : " + strTempXmlDir );
            sbResultat.append( LINE_SEPARATOR );
        }

        // Deleting original resources directories
        if ( bConsiderResources )
        {
            XmlPageFileUtils.removeDirectory( strOriginalResourcesDir );

            // Renaming temporary resources directories
            if ( !XmlPageFileUtils.renameDirectory( strTempResourcesDir, strOriginalResourcesDir ) )
            {
                sbResultat.append( "Can't rename directory : " + strTempResourcesDir );
                sbResultat.append( LINE_SEPARATOR );
            }
        }

        // Sending publication email
        sendPublicationMail( xmlPageGroup, sbResultat.toString(  ) );

        return sbResultat.toString(  );
    }

    /**
     * Method for sending validation email (OK or KO)
     * @param xmlPageGroup working group
     * @param strMessage message for errors
     */
    private static void sendValidationMail( XmlPageGroup xmlPageGroup, String strMessage )
    {
        String strSenderName = xmlPageGroup.getMailSenderName(  );
        String strSenderEmail = xmlPageGroup.getMailSenderEmail(  );
        String strMailSubject = null;
        HtmlTemplate templateMailValidation = null;

        if ( EMPTY_STRING.equals( strMessage ) )
        {
            strMailSubject = xmlPageGroup.getMailValidationOkSubject(  );
            templateMailValidation = AppTemplateService.getTemplate( PROPERTY_PAGE_MAIL_VALIDATION_OK_MESSAGE );
        }
        else
        {
            strMailSubject = xmlPageGroup.getMailValidationKoSubject(  );
            templateMailValidation = AppTemplateService.getTemplate( PROPERTY_PAGE_MAIL_VALIDATION_KO_MESSAGE );
            templateMailValidation.substitute( BOOKMARK_MAIL_ERROR_MESSAGE, strMessage );
        }

        templateMailValidation.substitute( BOOKMARK_MAIL_GROUP_NAME, xmlPageGroup.getName(  ) );

        MailService.sendMailHtml( xmlPageGroup.getMailRecipientList(  ), strSenderName, strSenderEmail, strMailSubject,
            templateMailValidation.getHtml(  ) );
    }

    /**
     * Method for sending publication email (OK or KO)
     * @param xmlPageGroup working group
     * @param strMessage message for errors
     */
    private static void sendPublicationMail( XmlPageGroup xmlPageGroup, String strMessage )
    {
        String strSenderName = xmlPageGroup.getMailSenderName(  );
        String strSenderEmail = xmlPageGroup.getMailSenderEmail(  );
        String strMailSubject = null;
        HtmlTemplate templateMailValidation = null;

        if ( EMPTY_STRING.equals( strMessage ) )
        {
            strMailSubject = xmlPageGroup.getMailPublicationOkSubject(  );
            templateMailValidation = AppTemplateService.getTemplate( PROPERTY_PAGE_MAIL_PUBLICATION_OK_MESSAGE );
        }
        else
        {
            strMailSubject = xmlPageGroup.getMailPublicationKoSubject(  );
            templateMailValidation = AppTemplateService.getTemplate( PROPERTY_PAGE_MAIL_PUBLICATION_KO_MESSAGE );
            templateMailValidation.substitute( BOOKMARK_MAIL_ERROR_MESSAGE, strMessage );
        }

        templateMailValidation.substitute( BOOKMARK_MAIL_GROUP_NAME, xmlPageGroup.getName(  ) );

        MailService.sendMailHtml( xmlPageGroup.getMailRecipientList(  ), strSenderName, strSenderEmail, strMailSubject,
            templateMailValidation.getHtml(  ) );
    }
}
