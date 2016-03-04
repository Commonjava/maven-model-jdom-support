/**
 * Copyright (C) 2012 Apache Software Foundation (jdcasey@commonjava.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.maven.io.util;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.WriterFactory;
import org.jdom2.Comment;
import org.jdom2.Content;
import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.JDOMFactory;
import org.jdom2.ProcessingInstruction;
import org.jdom2.Text;
import org.jdom2.UncheckedJDOMFactory;
import org.jdom2.Verifier;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.EscapeStrategy;
import org.jdom2.output.Format;
import org.jdom2.output.Format.TextMode;
import org.jdom2.output.LineSeparator;
import org.jdom2.output.XMLOutputter;
import org.jdom2.output.support.AbstractXMLOutputProcessor;
import org.jdom2.output.support.FormatStack;
import org.jdom2.output.support.Walker;
import org.jdom2.util.NamespaceStack;

@SuppressWarnings( "unchecked" )
public abstract class AbstractJDOMWriter<T, TYPE extends AbstractJDOMWriter<T, TYPE>>
{
    protected final Format format = Format.getRawFormat();

    protected final JDOMFactory factory = new UncheckedJDOMFactory();

    protected AbstractJDOMWriter()
    {
        setTextMode( TextMode.PRESERVE );
        setLineSeparator( System.getProperty( "line.separator" ) );
        setOmitDeclaration( false );
        setOmitEncoding( false );
        setExpandEmptyElements( true );
    }

    protected AbstractJDOMWriter( final String encoding )
    {
        this();
        setEncoding( encoding );
    }

    public final void write( final T source, final Document document, final Writer writer )
        throws java.io.IOException
    {
        write( source, document, writer, format, null );
    }

    public final void write( final T source, final Document document, final Writer writer,
                             final DocumentModifier modifier )
        throws java.io.IOException
    {
        write( source, document, writer, format, modifier );
    }

    public final void write( final T source, final Document document, final Writer writer, final Format jdomFormat )
        throws java.io.IOException
    {
        write( source, document, writer, jdomFormat, null );
    }

    public final void write( final T source, final Document document, final Writer writer, final Format jdomFormat,
                             final DocumentModifier modifier )
        throws java.io.IOException
    {
        if ( modifier != null )
        {
            modifier.preProcess( document );
        }
        update( source, new IndentationCounter( 0 ), document.getRootElement() );
        if ( modifier != null )
        {
            modifier.postProcess( document );
        }
        // Override XMLOutputter to correct initial comment trailing newlines.
        final XMLOutputter outputter = new XMLOutputter(new AbstractXMLOutputProcessor()
        {
            /**
             * This will handle printing of a {@link Document}.
             *
             * @param out    <code>Writer</code> to use.
             * @param fstack the FormatStack
             * @param nstack the NamespaceStack
             * @param doc    <code>Document</code> to write.
             * @throws IOException if the destination Writer fails
             */
            @Override protected void printDocument(Writer out, FormatStack fstack, NamespaceStack nstack, Document doc)
                    throws IOException
            {

                // If there is no root element then we cannot use the normal ways to
                // access the ContentList because Document throws an exception.
                // so we hack it and just access it by index.
                List<Content> list = doc.hasRootElement() ? doc.getContent() :
                        new ArrayList<Content>(doc.getContentSize());
                if (list.isEmpty()) {
                    final int sz = doc.getContentSize();
                    for (int i = 0; i < sz; i++) {
                        list.add(doc.getContent(i));
                    }
                }

                printDeclaration(out, fstack);

                Walker walker = buildWalker(fstack, list, true);
                if (walker.hasNext()) {
                    while (walker.hasNext()) {

                        final Content c = walker.next();
                        // we do not ignore Text-like things in the Document.
                        // the walker creates the indenting for us.
                        if (c == null) {
                            // but, what we do is ensure it is all whitespace, and not CDATA
                            final String padding = walker.text();
                            if (padding != null && Verifier.isAllXMLWhitespace(padding) &&
                                    !walker.isCDATA()) {
                                // we do not use the escaping or text* method because this
                                // content is outside of the root element, and thus is not
                                // strict text.
                                write(out, padding);
                            }
                        } else {
                            switch (c.getCType()) {
                                case Comment :
                                    printComment(out, fstack, (Comment)c);
                                    // This modification we have made to the overridden method in order
                                    // to correct newline declarations.
                                    write(out, fstack.getLineSeparator());
                                    break;
                                case DocType :
                                    printDocType(out, fstack, (DocType)c);
                                    break;
                                case Element :
                                    printElement(out, fstack, nstack, (Element)c);
                                    if (walker.hasNext())
                                    {
                                        // This modification we have made to the overridden method in order
                                        // to correct newline declarations.
                                        write(out, fstack.getLineSeparator());
                                    }
                                    break;
                                case ProcessingInstruction :
                                    printProcessingInstruction(out, fstack,
                                            (ProcessingInstruction)c);
                                    break;
                                case Text :
                                    final String padding = ((Text)c).getText();
                                    if (padding != null && Verifier.isAllXMLWhitespace(padding)) {
                                        // we do not use the escaping or text* method because this
                                        // content is outside of the root element, and thus is not
                                        // strict text.
                                        write(out, padding);
                                    }
                                default :
                                    // do nothing.
                            }
                        }

                    }

                    if (fstack.getLineSeparator() != null) {
                        write(out, fstack.getLineSeparator());
                    }
                }
            }
        });

        outputter.setFormat( jdomFormat );
        outputter.output( document, writer );
    }

    public final void write( final T source, final File target )
        throws IOException, JDOMException
    {
        write( source, target, format, null );
    }

    public final void write( final T source, final File target, final DocumentModifier modifier )
        throws IOException, JDOMException
    {
        write( source, target, format, modifier );
    }

    public final void write( final T source, final File target, final Format format )
        throws IOException, JDOMException
    {
        write( source, target, format, null );
    }

    public final void write( final T source, final File target, final Format format, final DocumentModifier modifier )
        throws IOException, JDOMException
    {
        final SAXBuilder builder = new SAXBuilder();
        final Document doc = builder.build( target );

        Writer pomWriter = null;
        try
        {
            pomWriter = WriterFactory.newWriter( target, getEncoding() );
            write( source, doc, pomWriter, format, modifier );
            pomWriter.flush();
        }
        finally
        {
            IOUtil.close( pomWriter );
        }

    }

    protected abstract void update( T source, IndentationCounter indentationCounter, Element rootElement )
        throws IOException;

    public TYPE setEscapeStrategy( final EscapeStrategy strategy )
    {
        format.setEscapeStrategy( strategy );
        return (TYPE) this;
    }

    public EscapeStrategy getEscapeStrategy()
    {
        return format.getEscapeStrategy();
    }

    public TYPE setLineSeparator( final String separator )
    {
        format.setLineSeparator( separator );
        return (TYPE) this;
    }

    public TYPE setLineSeparator( final LineSeparator separator )
    {
        format.setLineSeparator( separator );
        return (TYPE) this;
    }

    public String getLineSeparator()
    {
        return format.getLineSeparator();
    }

    public TYPE setOmitEncoding( final boolean omitEncoding )
    {
        format.setOmitEncoding( omitEncoding );
        return (TYPE) this;
    }

    public boolean getOmitEncoding()
    {
        return format.getOmitEncoding();
    }

    public TYPE setOmitDeclaration( final boolean omitDeclaration )
    {
        format.setOmitDeclaration( omitDeclaration );
        return (TYPE) this;
    }

    public boolean getOmitDeclaration()
    {
        return format.getOmitDeclaration();
    }

    public TYPE setExpandEmptyElements( final boolean expandEmptyElements )
    {
        format.setExpandEmptyElements( expandEmptyElements );
        return (TYPE) this;
    }

    public boolean getExpandEmptyElements()
    {
        return format.getExpandEmptyElements();
    }

    public TYPE setIgnoreTrAXEscapingPIs( final boolean ignoreTrAXEscapingPIs )
    {
        format.setIgnoreTrAXEscapingPIs( ignoreTrAXEscapingPIs );
        return (TYPE) this;
    }

    public boolean getIgnoreTrAXEscapingPIs()
    {
        return format.getIgnoreTrAXEscapingPIs();
    }

    public TYPE setTextMode( final TextMode mode )
    {
        format.setTextMode( mode );
        return (TYPE) this;
    }

    public TextMode getTextMode()
    {
        return format.getTextMode();
    }

    public TYPE setIndent( final String indent )
    {
        format.setIndent( indent );
        return (TYPE) this;
    }

    public String getIndent()
    {
        return format.getIndent();
    }

    public TYPE setEncoding( final String encoding )
    {
        format.setEncoding( encoding );
        return (TYPE) this;
    }

    public String getEncoding()
    {
        return format.getEncoding();
    }

    public boolean isSpecifiedAttributesOnly()
    {
        return format.isSpecifiedAttributesOnly();
    }

    public TYPE setSpecifiedAttributesOnly( final boolean specifiedAttributesOnly )
    {
        format.setSpecifiedAttributesOnly( specifiedAttributesOnly );
        return (TYPE) this;
    }

}
