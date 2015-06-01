package org.apache.maven.io.util;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.WriterFactory;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.JDOMFactory;
import org.jdom2.UncheckedJDOMFactory;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.EscapeStrategy;
import org.jdom2.output.Format;
import org.jdom2.output.Format.TextMode;
import org.jdom2.output.LineSeparator;
import org.jdom2.output.XMLOutputter;

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
        final XMLOutputter outputter = new XMLOutputter();
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
