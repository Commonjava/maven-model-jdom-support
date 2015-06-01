/*
 * Copyright 2012, Apache Software Foundation
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.maven.artifact.repository.metadata.io.jdom;

import static org.apache.maven.io.util.WriterUtils.findAndReplaceSimpleElement;
import static org.apache.maven.io.util.WriterUtils.findAndReplaceSimpleLists;
import static org.apache.maven.io.util.WriterUtils.insertAtPreferredLocation;
import static org.apache.maven.io.util.WriterUtils.updateElement;

import java.io.IOException;
import java.util.Iterator;

import org.apache.maven.artifact.repository.metadata.Metadata;
import org.apache.maven.artifact.repository.metadata.Plugin;
import org.apache.maven.artifact.repository.metadata.Snapshot;
import org.apache.maven.artifact.repository.metadata.SnapshotVersion;
import org.apache.maven.artifact.repository.metadata.Versioning;
import org.apache.maven.io.util.AbstractJDOMWriter;
import org.apache.maven.io.util.IndentationCounter;
import org.jdom2.Element;
import org.jdom2.JDOMFactory;
import org.jdom2.UncheckedJDOMFactory;

/**
 * Class MetadataJDOMWriter.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings( "all" )
public class MetadataJDOMWriter
    extends AbstractJDOMWriter<Metadata, MetadataJDOMWriter>
{

    private static final String INDENT = "  ";

    // --------------------------/
    // - Class/Member Variables -/
    // --------------------------/

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

    public MetadataJDOMWriter()
    {
        factory = new UncheckedJDOMFactory();
        lineSeparator = "\n";
    } // -- org.apache.maven.artifact.repository.metadata.io.jdom.MetadataJDOMWriter()

    // -----------/
    // - Methods -/
    // -----------/

    /**
     * Method iteratePlugin.
     * 
     * @param counter
     * @param childTag
     * @param parentTag
     * @param list
     * @param parent
     */
    protected void iteratePlugin( final IndentationCounter counter, final Element parent,
                                  final java.util.Collection list, final java.lang.String parentTag,
                                  final java.lang.String childTag )
    {
        final boolean shouldExist = ( list != null ) && ( list.size() > 0 );
        final Element element = updateElement( counter, parent, parentTag, shouldExist );
        if ( shouldExist )
        {
            final Iterator it = list.iterator();
            Iterator elIt = element.getChildren( childTag, element.getNamespace() ).iterator();
            if ( !elIt.hasNext() )
            {
                elIt = null;
            }
            final IndentationCounter innerCount = new IndentationCounter( counter.getDepth() + 1 );
            while ( it.hasNext() )
            {
                final Plugin value = (Plugin) it.next();
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
                    el = factory.element( childTag, element.getNamespace() );
                    insertAtPreferredLocation( element, el, innerCount );
                }
                updatePlugin( value, childTag, innerCount, el );
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
        }
    } // -- void iteratePlugin( Counter, Element, java.util.Collection, java.lang.String, java.lang.String )

    /**
     * Method iterateSnapshotVersion.
     * 
     * @param counter
     * @param childTag
     * @param parentTag
     * @param list
     * @param parent
     */
    protected void iterateSnapshotVersion( final IndentationCounter counter, final Element parent,
                                           final java.util.Collection list, final java.lang.String parentTag,
                                           final java.lang.String childTag )
    {
        final boolean shouldExist = ( list != null ) && ( list.size() > 0 );
        final Element element = updateElement( counter, parent, parentTag, shouldExist );
        if ( shouldExist )
        {
            final Iterator it = list.iterator();
            Iterator elIt = element.getChildren( childTag, element.getNamespace() ).iterator();
            if ( !elIt.hasNext() )
            {
                elIt = null;
            }
            final IndentationCounter innerCount = new IndentationCounter( counter.getDepth() + 1 );
            while ( it.hasNext() )
            {
                final SnapshotVersion value = (SnapshotVersion) it.next();
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
                    el = factory.element( childTag, element.getNamespace() );
                    insertAtPreferredLocation( element, el, innerCount );
                }
                updateSnapshotVersion( value, childTag, innerCount, el );
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
        }
    } // -- void iterateSnapshotVersion( Counter, Element, java.util.Collection, java.lang.String, java.lang.String )

    /**
     * Method updateMetadata.
     * 
     * @param metadata
     * @param element
     * @param counter
     * @param xmlTag
     */
    protected void updateMetadata( final Metadata metadata, final String xmlTag, final IndentationCounter counter,
                                   final Element element )
    {
        final Element root = element;
        final IndentationCounter innerCount = new IndentationCounter( counter.getDepth() + 1 );
        findAndReplaceSimpleElement( innerCount,
                                     root,
                                     "groupId",
                                     metadata.getGroupId() == null ? null : metadata.getGroupId(),
                                     null );
        findAndReplaceSimpleElement( innerCount,
                                     root,
                                     "artifactId",
                                     metadata.getArtifactId() == null ? null : metadata.getArtifactId(),
                                     null );
        findAndReplaceSimpleElement( innerCount,
                                     root,
                                     "version",
                                     metadata.getVersion() == null ? null : metadata.getVersion(),
                                     null );
        updateVersioning( metadata.getVersioning(), "versioning", innerCount, root );
        iteratePlugin( innerCount, root, metadata.getPlugins(), "plugins", "plugin" );
    } // -- void updateMetadata( Metadata, String, Counter, Element )

    /**
     * Method updatePlugin.
     * 
     * @param plugin
     * @param element
     * @param counter
     * @param xmlTag
     */
    protected void updatePlugin( final Plugin plugin, final String xmlTag, final IndentationCounter counter,
                                 final Element element )
    {
        final Element root = element;
        final IndentationCounter innerCount = new IndentationCounter( counter.getDepth() + 1 );
        findAndReplaceSimpleElement( innerCount, root, "name", plugin.getName() == null ? null : plugin.getName(), null );
        findAndReplaceSimpleElement( innerCount,
                                     root,
                                     "prefix",
                                     plugin.getPrefix() == null ? null : plugin.getPrefix(),
                                     null );
        findAndReplaceSimpleElement( innerCount,
                                     root,
                                     "artifactId",
                                     plugin.getArtifactId() == null ? null : plugin.getArtifactId(),
                                     null );
    } // -- void updatePlugin( Plugin, String, Counter, Element )

    /**
     * Method updateSnapshot.
     * 
     * @param snapshot
     * @param element
     * @param counter
     * @param xmlTag
     */
    protected void updateSnapshot( final Snapshot snapshot, final String xmlTag, final IndentationCounter counter,
                                   final Element element )
    {
        final boolean shouldExist = ( snapshot != null );
        final Element root = updateElement( counter, element, xmlTag, shouldExist );
        if ( shouldExist )
        {
            final IndentationCounter innerCount = new IndentationCounter( counter.getDepth() + 1 );
            findAndReplaceSimpleElement( innerCount, root, "timestamp", snapshot.getTimestamp() == null ? null
                            : snapshot.getTimestamp(), null );
            findAndReplaceSimpleElement( innerCount, root, "buildNumber", snapshot.getBuildNumber() == 0 ? null
                            : String.valueOf( snapshot.getBuildNumber() ), "0" );
            findAndReplaceSimpleElement( innerCount,
                                         root,
                                         "localCopy",
                                         snapshot.isLocalCopy() == false ? null
                                                         : String.valueOf( snapshot.isLocalCopy() ),
                                         "false" );
        }
    } // -- void updateSnapshot( Snapshot, String, Counter, Element )

    /**
     * Method updateSnapshotVersion.
     * 
     * @param snapshotVersion
     * @param element
     * @param counter
     * @param xmlTag
     */
    protected void updateSnapshotVersion( final SnapshotVersion snapshotVersion, final String xmlTag,
                                          final IndentationCounter counter, final Element element )
    {
        final Element root = element;
        final IndentationCounter innerCount = new IndentationCounter( counter.getDepth() + 1 );
        findAndReplaceSimpleElement( innerCount, root, "classifier", snapshotVersion.getClassifier() == null ? null
                        : snapshotVersion.getClassifier(), "" );
        findAndReplaceSimpleElement( innerCount, root, "extension", snapshotVersion.getExtension() == null ? null
                        : snapshotVersion.getExtension(), null );
        findAndReplaceSimpleElement( innerCount, root, "value", snapshotVersion.getVersion() == null ? null
                        : snapshotVersion.getVersion(), null );
        findAndReplaceSimpleElement( innerCount, root, "updated", snapshotVersion.getUpdated() == null ? null
                        : snapshotVersion.getUpdated(), null );
    } // -- void updateSnapshotVersion( SnapshotVersion, String, Counter, Element )

    /**
     * Method updateVersioning.
     * 
     * @param versioning
     * @param element
     * @param counter
     * @param xmlTag
     */
    protected void updateVersioning( final Versioning versioning, final String xmlTag,
                                     final IndentationCounter counter, final Element element )
    {
        final boolean shouldExist = ( versioning != null );
        final Element root = updateElement( counter, element, xmlTag, shouldExist );
        if ( shouldExist )
        {
            final IndentationCounter innerCount = new IndentationCounter( counter.getDepth() + 1 );
            findAndReplaceSimpleElement( innerCount,
                                         root,
                                         "latest",
                                         versioning.getLatest() == null ? null : versioning.getLatest(),
                                         null );
            findAndReplaceSimpleElement( innerCount, root, "release", versioning.getRelease() == null ? null
                            : versioning.getRelease(), null );
            updateSnapshot( versioning.getSnapshot(), "snapshot", innerCount, root );
            findAndReplaceSimpleLists( innerCount, root, versioning.getVersions(), "versions", "version" );
            findAndReplaceSimpleElement( innerCount, root, "lastUpdated", versioning.getLastUpdated() == null ? null
                            : versioning.getLastUpdated(), null );
            iterateSnapshotVersion( innerCount,
                                    root,
                                    versioning.getSnapshotVersions(),
                                    "snapshotVersions",
                                    "snapshotVersion" );
        }
    } // -- void updateVersioning( Versioning, String, Counter, Element )

    @Override
    protected void update( final Metadata source, final IndentationCounter indentationCounter, final Element rootElement )
        throws IOException
    {
        updateMetadata( source, "metadata", indentationCounter, rootElement );
    }

}
