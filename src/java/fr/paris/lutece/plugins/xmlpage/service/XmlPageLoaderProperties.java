/*
 * Copyright (c) 2002-2009, Mairie de Paris
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
import fr.paris.lutece.portal.service.resource.ResourceLoader;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.filesystem.DirectoryNotFoundException;
import fr.paris.lutece.util.filesystem.FileSystemUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;


/**
 *
 * This class delivers XML Page configuration from property files
 */
public class XmlPageLoaderProperties implements ResourceLoader
{
    private static final String PROPERTY_XMLPAGE_FILES_PATH = "xmlpage.properties.files.path";
    private static final String PROPERTY_GROUP_PATH_SOURCES = "group.path.sources";
    private static final String PROPERTY_GROUP_PATH_RESOURCES = "group.path.resources";
    private static final String PROPERTY_GROUP_PATH_XML = "group.path.xml";
    private static final String PROPERTY_GROUP_PATH_XSL = "group.path.xsl";
    private static final String PROPERTY_GROUP_PATH_XSD = "group.path.xsd";
    private static final String PROPERTY_GROUP_LOCK_PUBLICATION_PATH = "group.lock.publication.file";
    private static final String PROPERTY_GROUP_LOCK_TRANSFERT_PATH = "group.lock.transfert.file";
    private static final String PROPERTY_GROUP_EXTENSION_COPY = "group.extension.copy";
    private static final String PROPERTY_GROUP_DISPLAY_LINK = "group.display.link";
    private static final String PROPERTY_GROUP_MAIL_SENDER_NAME = "group.mail.sender.name";
    private static final String PROPERTY_GROUP_MAIL_SENDER_EMAIL = "group.mail.sender.email";
    private static final String PROPERTY_GROUP_MAIL_RECIPIENT_LIST = "group.mail.recipient.list";
    private static final String PROPERTY_GROUP_MAIL_VALIDATION_OK_SUBJECT = "group.mail.validation.ok.subject";
    private static final String PROPERTY_GROUP_MAIL_VALIDATION_KO_SUBJECT = "group.mail.validation.ko.subject";
    private static final String PROPERTY_GROUP_MAIL_PUBLICATION_OK_SUBJECT = "group.mail.publication.ok.subject";
    private static final String PROPERTY_GROUP_MAIL_PUBLICATION_KO_SUBJECT = "group.mail.publication.ko.subject";
    private static final String PROPERTY_GROUP_RESERVED_KEY = "group";
    private static final String PROPERTY_FILE_XML_FILE = ".xml.file";
    private static final String PROPERTY_FILE_TITLE = ".title";
    private static final String PROPERTY_FILE_XSL_PART = ".xsl.";
    private static final String PROPERTY_FILE_XSL_FILE_SUFFIXE = ".file";
    private static final String PROPERTY_FILE_IS_VALIDATION_REQUIRED = ".validation.isRequired";
    private static final String PROPERTY_FILE_XSD_SCHEMA = ".validation.xsd.file";
    private static final String EXTENSION_XMLPAGE_PROPERTY_FILE = ".properties";
    private static final String BOOLEAN_TRUE_VALUE = "1";
    private static final String PATH_SEPARATOR = "/";
    private static final String SEPARATOR_LIST_EXTENSION = ",";
    private static final String SEPARATOR_FILE_EXTENSION = ".";
    private static final String SEPARATOR_PROPERTY_KEY = ".";

    /**
     * Implementation of the ResourceLoader interface
     * @return A collection of resources
     */
    public Collection getResources(  )
    {
        Collection listXmlPageElement = new ArrayList(  );
        String strRootDirectory = AppPathService.getWebAppPath(  );

        // get the files in the configured reference directory for xmlpage
        String strPath = AppPropertiesService.getProperty( PROPERTY_XMLPAGE_FILES_PATH );

        try
        {
            Collection<File> listFiles = FileSystemUtil.getFiles( strRootDirectory, strPath );

            for ( File file : listFiles )
            {
                String strFileName = file.getName(  );

                // for each file that is a .properties file, load the properties in the XmlpageGroup structure
                // and add the initialized xmlpages from that structure into the list of resources to return.
                if ( strFileName.endsWith( EXTENSION_XMLPAGE_PROPERTY_FILE ) )
                {
                    Properties properties = loadPropertiesFromFile( file );
                    Map xmlPagesMap = loadListXmlPage( properties );
                    listXmlPageElement.addAll( xmlPagesMap.values(  ) );
                }
            }
        }
        catch ( DirectoryNotFoundException e )
        {
            throw new AppException( e.getMessage(  ), e );
        }

        return listXmlPageElement;
    }

    /**
     * Implementation of the ResourceLoader interface
     * @param strPageId The resource Id
     * @return The Resource
     */
    public Resource getResource( String strPageId )
    {
        Resource resource = null;

        // get the files in the configured reference directory for xmlpage
        String strRootDirectory = AppPathService.getWebAppPath(  );
        String strPath = AppPropertiesService.getProperty( PROPERTY_XMLPAGE_FILES_PATH );

        try
        {
            // loop in all propertie files and look for the given page name.
            // if found : stop and return the corresponding XmlPage
            for ( File file : FileSystemUtil.getFiles( strRootDirectory, strPath ) )
            {
                String strFileName = file.getName(  );

                if ( strFileName.endsWith( EXTENSION_XMLPAGE_PROPERTY_FILE ) )
                {
                    Properties properties = loadPropertiesFromFile( file );

                    if ( checkExistXmlPage( properties, strPageId ) )
                    {
                        resource = loadXmlPage( properties, strPageId );

                        break;
                    }
                }
            }
        }
        catch ( DirectoryNotFoundException e )
        {
            throw new AppException( e.getMessage(  ), e );
        }

        return resource;
    }

    /**
     * Get All XmlPageGroup from all the properties definied for this plugin
     * This method return a collection of XmlPageGroup objects.
     * Each XmlPageGroup provides all the group properties and a list of XmlPage object found for the group.
     * This method - and the XmlPageGroup structure - is only used by the deamon responsible
     * for the copy and validation of the files.
     * @return Collection of XmlPageGroup
     */
    public static Collection getAllXmlPageByGroup(  )
    {
        Collection listXmlPageGroup = new ArrayList(  );

        // get the files in the configured reference directory for xmlpage
        String strRootDirectory = AppPathService.getWebAppPath(  );
        String strPath = AppPropertiesService.getProperty( PROPERTY_XMLPAGE_FILES_PATH );
        Collection<File> listFiles;

        try
        {
            for ( File file : FileSystemUtil.getFiles( strRootDirectory, strPath ) )
            {
                String strFileName = file.getName(  );

                // for each file that is a .properties file, load the properties in the XmlpageGroup structure
                // (containing initialized xmlpages) and add the initialized structure in the list of groups to return.
                if ( strFileName.endsWith( EXTENSION_XMLPAGE_PROPERTY_FILE ) )
                {
                    XmlPageGroup xmlPageGroup = loadXmlPageGroup( file );
                    listXmlPageGroup.add( xmlPageGroup );
                }
            }
        }
        catch ( DirectoryNotFoundException e )
        {
            throw new AppException( e.getMessage(  ), e );
        }

        return listXmlPageGroup;
    }

    /**
     * Return the set of properties
     * @param file The Property File to load
     * @return the properties found in the given file
     */
    private static Properties loadPropertiesFromFile( File file )
    {
        AppLogService.debug( "XmlPageLoaderProperties - loadPropertiesFromFile(" + file.getName(  ) + ")" );

        Properties properties = new Properties(  );

        try
        {
            FileInputStream is = new FileInputStream( file );
            properties.load( is );
        }
        catch ( IOException e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }

        return properties;
    }

    /**
     * Return the list of different XML File managed
     * @param properties describing the group
     * @return the list of all XML Files name
     */
    private static Set findAllXmlPageNames( Properties properties )
    {
        Enumeration enumPropertiesNames = properties.propertyNames(  );
        HashSet listXmlPageName = new HashSet(  );

        while ( enumPropertiesNames.hasMoreElements(  ) )
        {
            String strPropertyName = (String) enumPropertiesNames.nextElement(  );
            String strXmlPageName = strPropertyName.substring( 0, strPropertyName.indexOf( SEPARATOR_PROPERTY_KEY ) );
            listXmlPageName.add( strXmlPageName );
        }

        // Delete all reserved words
        listXmlPageName.remove( PROPERTY_GROUP_RESERVED_KEY );

        return listXmlPageName;
    }

    /**
     * Check that the given set of properties contains information about the asked page.
     * @param properties describing the group
     * @param strXmlPageName the XML page name
     * @return true if the page name exist for this set of properties, false otherwise.
     */
    private static boolean checkExistXmlPage( Properties properties, String strXmlPageName )
    {
        return findAllXmlPageNames( properties ).contains( strXmlPageName );
    }

    /**
     * Return the XML page corresponding to
     * @param properties describing the group
     * @param strXmlPageName the XML page name
     * @return the XmlPageElement object
     */
    private static XmlPageElement loadXmlPage( Properties properties, String strXmlPageName )
    {
        // load data common to the whole group
        String strXmlFilesDirectoryPath = AppPathService.getWebAppPath(  )
                                                        .concat( properties.getProperty( PROPERTY_GROUP_PATH_XML ) );
        String strXslFilesDirectoryPath = AppPathService.getWebAppPath(  )
                                                        .concat( properties.getProperty( PROPERTY_GROUP_PATH_XSL ) );
        String strResourceFilesDirectoryPath = properties.getProperty( PROPERTY_GROUP_PATH_RESOURCES );

        // load page data and initialize XmlPageElement
        XmlPageElement xmlPageElement = new XmlPageElement(  );
        xmlPageElement.setName( strXmlPageName );
        xmlPageElement.setTitle( properties.getProperty( strXmlPageName + PROPERTY_FILE_TITLE ) );
        xmlPageElement.setXmlFilesDirectoryPath( strXmlFilesDirectoryPath );
        xmlPageElement.setXslFilesDirectoryPath( strXslFilesDirectoryPath );
        xmlPageElement.setDisplayLink( properties.getProperty( PROPERTY_GROUP_DISPLAY_LINK ) );
        xmlPageElement.setResourceFilesDirectoryPath( strResourceFilesDirectoryPath );
        xmlPageElement.setXmlFileName( properties.getProperty( strXmlPageName + PROPERTY_FILE_XML_FILE ) );
        xmlPageElement.setXsdSchema( properties.getProperty( strXmlPageName + PROPERTY_FILE_XSD_SCHEMA ) );

        String strIsValidationRequired = properties.getProperty( strXmlPageName + PROPERTY_FILE_IS_VALIDATION_REQUIRED );
        Boolean bIsValidationRequired = ( BOOLEAN_TRUE_VALUE.equals( strIsValidationRequired ) ) ? Boolean.TRUE
                                                                                                 : Boolean.FALSE;
        xmlPageElement.setIsValidationRequired( bIsValidationRequired );

        // load the xsl styles configuration into the XslContent structure
        xmlPageElement.setListXslContent( getMapXslContent( properties, strXmlPageName ) );

        return xmlPageElement;
    }

    /**
     * Return the list of XML Files for one propertie file
     * @param properties describing the group
     * @return the list of XML Files
     */
    private static Map loadListXmlPage( Properties properties )
    {
        // get the page names in the propertie set
        Set listXmlPageName = findAllXmlPageNames( properties );
        Map listXmlPage = new HashMap( listXmlPageName.size(  ) );
        Iterator itListXmlPageName = listXmlPageName.iterator(  );

        while ( itListXmlPageName.hasNext(  ) )
        {
            String strXmlPageName = (String) itListXmlPageName.next(  );
            XmlPageElement xmlPageElement = loadXmlPage( properties, strXmlPageName );
            listXmlPage.put( strXmlPageName, xmlPageElement );
        }

        return listXmlPage;
    }

    /**
     * find XSL keys into properties for the specific strFileName
     * @param properties describing the group
     * @param strXmlPageName name of the Xml page
     * @return Map contening all XSL informations for different outputs of this XML file
     */
    private static Map getMapXslContent( Properties properties, String strXmlPageName )
    {
        Map mapResult = new HashMap(  );

        Enumeration enumPropKeys = properties.keys(  );

        while ( enumPropKeys.hasMoreElements(  ) )
        {
            String strPropKey = (String) enumPropKeys.nextElement(  );

            if ( strPropKey.startsWith( strXmlPageName + PROPERTY_FILE_XSL_PART ) &&
                    strPropKey.endsWith( PROPERTY_FILE_XSL_FILE_SUFFIXE ) )
            {
                XmlPageXslContent xmlPageXslContent = new XmlPageXslContent(  );
                String strPropOutput = strPropKey.substring( ( strXmlPageName + PROPERTY_FILE_XSL_PART ).length(  ),
                        strPropKey.lastIndexOf( SEPARATOR_PROPERTY_KEY ) );
                xmlPageXslContent.setFileName( properties.getProperty( strXmlPageName + PROPERTY_FILE_XSL_PART +
                        strPropOutput + PROPERTY_FILE_XSL_FILE_SUFFIXE ) );
                mapResult.put( strPropOutput, xmlPageXslContent );
            }
        }

        return mapResult;
    }

    /**
     * Return the group of XML Pages
     * @param file The Property File to load
     * @return xmlPageGroup the initialized structure containing the group infos and the XmlPageElement objects loaded from the properties
     */
    private static XmlPageGroup loadXmlPageGroup( File file )
    {
        AppLogService.debug( "XmlPageLoaderProperties - loadXmlPageGroup(" + file.getName(  ) + ")" );

        XmlPageGroup xmlPageGroup = new XmlPageGroup(  );
        String strFileName = file.getName(  );
        String strGroupId = strFileName.substring( 0, strFileName.lastIndexOf( SEPARATOR_FILE_EXTENSION ) );

        Properties properties = loadPropertiesFromFile( file );

        // read page data in the properties file
        // set the information relative to the group
        xmlPageGroup.setName( strGroupId );
        xmlPageGroup.setInputFilesDirectoryPath( properties.getProperty( PROPERTY_GROUP_PATH_SOURCES ) );
        xmlPageGroup.setXsdFilesDirectoryPath( AppPathService.getWebAppPath(  ).concat( PATH_SEPARATOR )
                                                             .concat( properties.getProperty( PROPERTY_GROUP_PATH_XSD ) ) );
        xmlPageGroup.setLockPublicationPath( properties.getProperty( PROPERTY_GROUP_LOCK_PUBLICATION_PATH ) );
        xmlPageGroup.setLockTransfertPath( properties.getProperty( PROPERTY_GROUP_LOCK_TRANSFERT_PATH ) );
        xmlPageGroup.setMailSenderName( properties.getProperty( PROPERTY_GROUP_MAIL_SENDER_NAME ) );
        xmlPageGroup.setMailSenderEmail( properties.getProperty( PROPERTY_GROUP_MAIL_SENDER_EMAIL ) );
        xmlPageGroup.setMailRecipientList( properties.getProperty( PROPERTY_GROUP_MAIL_RECIPIENT_LIST ) );
        xmlPageGroup.setMailPublicationKoSubject( properties.getProperty( PROPERTY_GROUP_MAIL_PUBLICATION_KO_SUBJECT ) );
        xmlPageGroup.setMailPublicationOkSubject( properties.getProperty( PROPERTY_GROUP_MAIL_PUBLICATION_OK_SUBJECT ) );
        xmlPageGroup.setMailValidationKoSubject( properties.getProperty( PROPERTY_GROUP_MAIL_VALIDATION_KO_SUBJECT ) );
        xmlPageGroup.setMailValidationOkSubject( properties.getProperty( PROPERTY_GROUP_MAIL_VALIDATION_OK_SUBJECT ) );
        xmlPageGroup.setListExtensionFileCopy( getListExtensionFileCopy( properties ) );

        // set the information for the pages found
        xmlPageGroup.setListXmlPageElement( loadListXmlPage( properties ) );

        AppLogService.debug( "xmlPageGroup = " + xmlPageGroup );

        return xmlPageGroup;
    }

    /**
     * read the properties and extract the file's extension that have to be copied without any control
     * @param properties describing the group
     * @return list of file's extension for direct copy
     */
    private static Set getListExtensionFileCopy( Properties properties )
    {
        String strExtensionGroup = properties.getProperty( PROPERTY_GROUP_EXTENSION_COPY );
        Set listExtensionFileCopy = new HashSet(  );

        // extracts each item (separated by a comma) from the file's extensions
        StringTokenizer strTokens = new StringTokenizer( strExtensionGroup, SEPARATOR_LIST_EXTENSION );

        while ( strTokens.hasMoreTokens(  ) )
        {
            String strName = strTokens.nextToken(  );
            listExtensionFileCopy.add( strName );
        }

        return listExtensionFileCopy;
    }
}
