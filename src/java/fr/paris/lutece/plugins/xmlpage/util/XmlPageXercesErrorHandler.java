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
package fr.paris.lutece.plugins.xmlpage.util;

import fr.paris.lutece.portal.service.util.AppLogService;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


/**
 * Specific ErrorHandler to validate XML Input Files for XmlPage Plugin.
 */
public class XmlPageXercesErrorHandler implements ErrorHandler
{
    /**
     * Receive notification of a recoverable error.
     * @param saxpe The warning information encapsulated in a SAX parse exception.
     * @throws SAXException Any SAX exception, possibly wrapping another exception.
     */
    public void error( SAXParseException saxpe ) throws SAXException
    {
        StringBuffer sb = new StringBuffer(  );
        sb.append( "Parsing Error (" );
        sb.append( saxpe.getSystemId(  ) );
        sb.append( " - Line " );
        sb.append( saxpe.getLineNumber(  ) );
        sb.append( ") " );
        sb.append( saxpe.getMessage(  ) );
        AppLogService.info( sb.toString(  ) );
        throw new SAXException( sb.toString(  ) );
    }

    /**
     * Receive notification of a non-recoverable error.
         * @param saxpe The warning information encapsulated in a SAX parse exception.
         * @throws SAXException Any SAX exception, possibly wrapping another exception.
     */
    public void fatalError( SAXParseException saxpe ) throws SAXException
    {
        StringBuffer sb = new StringBuffer(  );
        sb.append( "Parsing Fatal Error (" );
        sb.append( saxpe.getSystemId(  ) );
        sb.append( " - Line " );
        sb.append( saxpe.getLineNumber(  ) );
        sb.append( ") " );
        sb.append( saxpe.getMessage(  ) );
        throw new SAXException( sb.toString(  ) );
    }

    /**
     * Receive notification of a warning.
         * @param saxpe The warning information encapsulated in a SAX parse exception.
         * @throws SAXException Any SAX exception, possibly wrapping another exception.
     */
    public void warning( SAXParseException saxpe ) throws SAXException
    {
        StringBuffer sb = new StringBuffer(  );
        sb.append( "Parsing Warning (" );
        sb.append( saxpe.getSystemId(  ) );
        sb.append( " - Line " );
        sb.append( saxpe.getLineNumber(  ) );
        sb.append( ") " );
        sb.append( saxpe.getMessage(  ) );
        AppLogService.info( sb.toString(  ) );
    }
}
