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
package org.apache.maven.toolchain.model.io.jdom;

import static org.apache.maven.io.util.WriterUtils.findAndReplaceProperties;
import static org.apache.maven.io.util.WriterUtils.findAndReplaceSimpleElement;
import static org.apache.maven.io.util.WriterUtils.findAndReplaceXpp3DOM;

import java.io.IOException;
import java.util.Iterator;

import org.apache.maven.io.util.AbstractJDOMWriter;
import org.apache.maven.io.util.IndentationCounter;
import org.apache.maven.toolchain.model.PersistedToolchains;
import org.apache.maven.toolchain.model.ToolchainModel;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.jdom2.Element;
import org.jdom2.JDOMFactory;
import org.jdom2.Text;
import org.jdom2.UncheckedJDOMFactory;

/**
 * Class MavenToolchainsJDOMWriter.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings( "all" )
public class MavenToolchainsJDOMWriter
    extends AbstractJDOMWriter<PersistedToolchains, MavenToolchainsJDOMWriter>
{

    // --------------------------/
    // - Class/Member Variables -/
    // --------------------------/

    private static final String INDENT = "  ";

    /**
     * Field factory.
     */
    private final JDOMFactory factory;

    /**
     * Field lineSeparator.
     */
    private final String lineSeparator;

    // ----------------/
    // - Constructors -/
    // ----------------/

    public MavenToolchainsJDOMWriter()
    {
        factory = new UncheckedJDOMFactory();
        lineSeparator = "\n";
    } // -- org.apache.maven.toolchain.model.io.jdom.MavenToolchainsJDOMWriter()

    // -----------/
    // - Methods -/
    // -----------/

    /**
     * Method insertAtPreferredLocation.
     * 
     * @param parent
     * @param counter
     * @param child
     */
    protected void insertAtPreferredLocation( final Element parent, final Element child,
                                              final IndentationCounter counter )
    {
        int contentIndex = 0;
        int elementCounter = 0;
        final Iterator it = parent.getContent()
                                  .iterator();
        Text lastText = null;
        int offset = 0;
        while ( it.hasNext() && elementCounter <= counter.getCurrentIndex() )
        {
            final Object next = it.next();
            offset = offset + 1;
            if ( next instanceof Element )
            {
                elementCounter = elementCounter + 1;
                contentIndex = contentIndex + offset;
                offset = 0;
            }
            if ( next instanceof Text && it.hasNext() )
            {
                lastText = (Text) next;
            }
        }
        if ( lastText != null && lastText.getTextTrim()
                                         .length() == 0 )
        {
            lastText = lastText.clone();
        }
        else
        {
            String starter = lineSeparator;
            for ( int i = 0; i < counter.getDepth(); i++ )
            {
                starter = starter + INDENT; // TODO make settable?
            }
            lastText = factory.text( starter );
        }
        if ( parent.getContentSize() == 0 )
        {
            final Text finalText = lastText.clone();
            finalText.setText( finalText.getText()
                                        .substring( 0, finalText.getText()
                                                                .length() - INDENT.length() ) );
            parent.addContent( contentIndex, finalText );
        }
        parent.addContent( contentIndex, child );
        parent.addContent( contentIndex, lastText );
    } // -- void insertAtPreferredLocation( Element, Element, Counter )

    /**
     * Method iterate2ToolchainModel.
     * 
     * @param counter
     * @param childTag
     * @param list
     * @param parent
     */
    protected void iterate2ToolchainModel( final IndentationCounter counter, final Element parent,
                                           final java.util.Collection list, final java.lang.String childTag )
    {
        final Iterator it = list.iterator();
        Iterator elIt = parent.getChildren( childTag, parent.getNamespace() )
                              .iterator();
        if ( !elIt.hasNext() )
        {
            elIt = null;
        }
        final IndentationCounter innerCount = new IndentationCounter( counter.getDepth() + 1 );
        while ( it.hasNext() )
        {
            final ToolchainModel value = (ToolchainModel) it.next();
            Element el;
            if ( elIt != null && elIt.hasNext() )
            {
                el = (Element) elIt.next();
                if ( !elIt.hasNext() )
                {
                    elIt = null;
                }
            }
            else
            {
                el = factory.element( childTag, parent.getNamespace() );
                insertAtPreferredLocation( parent, el, innerCount );
            }
            updateToolchainModel( value, childTag, innerCount, el );
            innerCount.increaseCount();
        }
        if ( elIt != null )
        {
            while ( elIt.hasNext() )
            {
                elIt.next();
                elIt.remove();
            }
        }
    } // -- void iterate2ToolchainModel( Counter, Element, java.util.Collection, java.lang.String )

    /**
     * Method updatePersistedToolchains.
     * 
     * @param persistedToolchains
     * @param element
     * @param counter
     * @param xmlTag
     */
    protected void updatePersistedToolchains( final PersistedToolchains persistedToolchains, final String xmlTag,
                                              final IndentationCounter counter, final Element element )
    {
        final Element root = element;
        final IndentationCounter innerCount = new IndentationCounter( counter.getDepth() + 1 );
        iterate2ToolchainModel( innerCount, root, persistedToolchains.getToolchains(), "toolchain" );
    } // -- void updatePersistedToolchains( PersistedToolchains, String, Counter, Element )

    /**
     * Method updateToolchainModel.
     * 
     * @param toolchainModel
     * @param element
     * @param counter
     * @param xmlTag
     */
    protected void updateToolchainModel( final ToolchainModel toolchainModel, final String xmlTag,
                                         final IndentationCounter counter, final Element element )
    {
        final Element root = element;
        final IndentationCounter innerCount = new IndentationCounter( counter.getDepth() + 1 );
        findAndReplaceSimpleElement( innerCount, root, "type",
                                     toolchainModel.getType() == null ? null : toolchainModel.getType(), null );
        findAndReplaceProperties( innerCount, root, "provides", toolchainModel.getProvides() );
        findAndReplaceXpp3DOM( innerCount, root, "configuration", (Xpp3Dom) toolchainModel.getConfiguration() );
    } // -- void updateToolchainModel( ToolchainModel, String, Counter, Element )

    @Override
    protected void update( final PersistedToolchains source, final IndentationCounter indentationCounter,
                           final Element rootElement )
        throws IOException
    {
        updatePersistedToolchains( source, "toolchains", indentationCounter, rootElement );
    }

}
