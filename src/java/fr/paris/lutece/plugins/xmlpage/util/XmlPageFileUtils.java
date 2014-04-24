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
package fr.paris.lutece.plugins.xmlpage.util;

import fr.paris.lutece.portal.service.util.AppLogService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.nio.channels.FileChannel;


/**
 * File Utilities for XmlPage Plugin
 */
public class XmlPageFileUtils
{
    /**
     * Default constructor
     */
    protected XmlPageFileUtils(  )
    {
        // nothing to do
    }

    /**
    * Make a directory using the path given in parameter
    * @param strDirectoryPath the path of new directory
    * @return true if and only if the directory was created, along with all necessary parent directories; false otherwise
    */
    public static boolean makeDirectory( String strDirectoryPath )
    {
        File fDirectory = new File( strDirectoryPath );
        AppLogService.debug( "Creating directory : " + strDirectoryPath );

        return fDirectory.mkdirs(  );
    }

    /**
     * Create a file using the path given in parameter
     * @param strFilePath file path to create
     * @return true if the named file does not exist and was successfully created; false if the named file already exists
     */
    public static boolean createFile( String strFilePath )
    {
        AppLogService.debug( "Creating file : " + strFilePath );

        File fFile = new File( strFilePath );
        boolean bReturn = false;

        try
        {
            bReturn = fFile.createNewFile(  );
        }
        catch ( IOException ioe )
        {
            AppLogService.error( ioe.getMessage(  ), ioe );
        }

        return bReturn;
    }

    /**
     * Copy file from directory strDirectory to directory strNewDirectory
     * @param strDirectory original directory contening the file
     * @param strFileName file name to copy
     * @param strNewDirectory destination directory for the copied file
     */
    public static void copyFile( String strDirectory, String strFileName, String strNewDirectory )
    {
        try
        {
            AppLogService.debug( "Copying file \"" + strFileName + "\" from directory \"" + strDirectory +
                "\" to directory \"" + strNewDirectory + "\"" );

            FileInputStream fInputStream = new FileInputStream( strDirectory.concat( "/" ).concat( strFileName ) );
            FileOutputStream fOutputStream = new FileOutputStream( strNewDirectory.concat( "/" ).concat( strFileName ) );
            FileChannel fInputChannel = fInputStream.getChannel(  );
            FileChannel fOutputChannel = fOutputStream.getChannel(  );
            fInputChannel.transferTo( 0, fInputChannel.size(  ), fOutputChannel );

            fInputChannel.close(  );
            fOutputChannel.close(  );

            fInputStream.close(  );
            fOutputStream.close(  );
            AppLogService.debug( "Copying file done" );
        }
        catch ( IOException ioe )
        {
            AppLogService.error( ioe.getMessage(  ), ioe );
        }
    }

    /**
     * Rename a file or directory with another name
     * @param strOriginalDirectory original directory path
     * @param strNewDirectory new directory path
     * @return true if and only if the renaming succeeded; false otherwise
     */
    public static boolean renameDirectory( String strOriginalDirectory, String strNewDirectory )
    {
        AppLogService.debug( "Renaming directory from \"" + strOriginalDirectory + "\" to \"" + strNewDirectory + "\"" );

        File fOriginal = new File( strOriginalDirectory );
        File fNew = new File( strNewDirectory );

        return fOriginal.renameTo( fNew );
    }

    /**
     * Delete a file
     * @param strFilePath path of deleted file
     * @return true if and only if the file is successfully deleted; false otherwise
     */
    public static boolean removeFile( String strFilePath )
    {
        AppLogService.debug( "Removing file \"" + strFilePath + "\"" );

        return removeDirectory( new File( strFilePath ) );
    }

    /**
     * Delete a directory and everything in it
     * @param strDirectoryPath path of deleted directory
     * @return true if and only if the directory is successfully deleted; false otherwise
     */
    public static boolean removeDirectory( String strDirectoryPath )
    {
        AppLogService.debug( "Removing directory \"" + strDirectoryPath + "\"" );

        return removeDirectory( new File( strDirectoryPath ) );
    }

    /**
     * Delete a directory and everything in it
     * @param fDirectoryPath File of directory
     * @return true if and only if the file or directory is successfully deleted; false otherwise
     */
    public static boolean removeDirectory( File fDirectoryPath )
    {
        if ( fDirectoryPath.isDirectory(  ) )
        {
            File[] childs = fDirectoryPath.listFiles(  );

            for ( int i = 0; i < childs.length; i++ )
            {
                if ( !removeDirectory( childs[i] ) )
                {
                    return false;
                }
            }
        }

        return fDirectoryPath.delete(  );
    }
}
