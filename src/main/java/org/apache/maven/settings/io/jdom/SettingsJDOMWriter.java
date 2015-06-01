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

package org.apache.maven.settings.io.jdom;

import static org.apache.maven.io.util.WriterUtils.findAndReplaceProperties;
import static org.apache.maven.io.util.WriterUtils.findAndReplaceSimpleElement;
import static org.apache.maven.io.util.WriterUtils.findAndReplaceSimpleLists;
import static org.apache.maven.io.util.WriterUtils.findAndReplaceXpp3DOM;
import static org.apache.maven.io.util.WriterUtils.insertAtPreferredLocation;
import static org.apache.maven.io.util.WriterUtils.updateElement;

import java.io.IOException;
import java.util.Iterator;

import org.apache.maven.io.util.AbstractJDOMWriter;
import org.apache.maven.io.util.IndentationCounter;
import org.apache.maven.settings.Activation;
import org.apache.maven.settings.ActivationFile;
import org.apache.maven.settings.ActivationOS;
import org.apache.maven.settings.ActivationProperty;
import org.apache.maven.settings.IdentifiableBase;
import org.apache.maven.settings.Mirror;
import org.apache.maven.settings.Profile;
import org.apache.maven.settings.Proxy;
import org.apache.maven.settings.Repository;
import org.apache.maven.settings.RepositoryBase;
import org.apache.maven.settings.RepositoryPolicy;
import org.apache.maven.settings.Server;
import org.apache.maven.settings.Settings;
import org.apache.maven.settings.TrackableBase;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.jdom2.Element;
import org.jdom2.JDOMFactory;
import org.jdom2.UncheckedJDOMFactory;

/**
 * Class SettingsJDOMWriter.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings( "all" )
public class SettingsJDOMWriter
    extends AbstractJDOMWriter<Settings, SettingsJDOMWriter>
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

    public SettingsJDOMWriter()
    {
        factory = new UncheckedJDOMFactory();
        lineSeparator = "\n";
    } // -- org.apache.maven.settings.io.jdom.SettingsJDOMWriter()

    // -----------/
    // - Methods -/
    // -----------/

    /**
     * Method iterateMirror.
     * 
     * @param counter
     * @param childTag
     * @param parentTag
     * @param list
     * @param parent
     */
    protected void iterateMirror( final IndentationCounter counter, final Element parent,
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
                final Mirror value = (Mirror) it.next();
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
                updateMirror( value, childTag, innerCount, el );
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
    } // -- void iterateMirror( Counter, Element, java.util.Collection, java.lang.String, java.lang.String )

    /**
     * Method iterateProfile.
     * 
     * @param counter
     * @param childTag
     * @param parentTag
     * @param list
     * @param parent
     */
    protected void iterateProfile( final IndentationCounter counter, final Element parent,
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
                final Profile value = (Profile) it.next();
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
                updateProfile( value, childTag, innerCount, el );
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
    } // -- void iterateProfile( Counter, Element, java.util.Collection, java.lang.String, java.lang.String )

    /**
     * Method iterateProxy.
     * 
     * @param counter
     * @param childTag
     * @param parentTag
     * @param list
     * @param parent
     */
    protected void iterateProxy( final IndentationCounter counter, final Element parent,
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
                final Proxy value = (Proxy) it.next();
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
                updateProxy( value, childTag, innerCount, el );
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
    } // -- void iterateProxy( Counter, Element, java.util.Collection, java.lang.String, java.lang.String )

    /**
     * Method iterateRepository.
     * 
     * @param counter
     * @param childTag
     * @param parentTag
     * @param list
     * @param parent
     */
    protected void iterateRepository( final IndentationCounter counter, final Element parent,
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
                final Repository value = (Repository) it.next();
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
                updateRepository( value, childTag, innerCount, el );
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
    } // -- void iterateRepository( Counter, Element, java.util.Collection, java.lang.String, java.lang.String )

    /**
     * Method iterateServer.
     * 
     * @param counter
     * @param childTag
     * @param parentTag
     * @param list
     * @param parent
     */
    protected void iterateServer( final IndentationCounter counter, final Element parent,
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
                final Server value = (Server) it.next();
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
                updateServer( value, childTag, innerCount, el );
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
    } // -- void iterateServer( Counter, Element, java.util.Collection, java.lang.String, java.lang.String )

    /**
     * Method updateActivation.
     * 
     * @param activation
     * @param element
     * @param counter
     * @param xmlTag
     */
    protected void updateActivation( final Activation activation, final String xmlTag,
                                     final IndentationCounter counter, final Element element )
    {
        final boolean shouldExist = ( activation != null );
        final Element root = updateElement( counter, element, xmlTag, shouldExist );
        if ( shouldExist )
        {
            final IndentationCounter innerCount = new IndentationCounter( counter.getDepth() + 1 );
            findAndReplaceSimpleElement( innerCount,
                                         root,
                                         "activeByDefault",
                                         activation.isActiveByDefault() == false ? null
                                                         : String.valueOf( activation.isActiveByDefault() ),
                                         "false" );
            findAndReplaceSimpleElement( innerCount,
                                         root,
                                         "jdk",
                                         activation.getJdk() == null ? null : activation.getJdk(),
                                         null );
            updateActivationOS( activation.getOs(), "os", innerCount, root );
            updateActivationProperty( activation.getProperty(), "property", innerCount, root );
            updateActivationFile( activation.getFile(), "file", innerCount, root );
        }
    } // -- void updateActivation( Activation, String, Counter, Element )

    /**
     * Method updateActivationFile.
     * 
     * @param activationFile
     * @param element
     * @param counter
     * @param xmlTag
     */
    protected void updateActivationFile( final ActivationFile activationFile, final String xmlTag,
                                         final IndentationCounter counter, final Element element )
    {
        final boolean shouldExist = ( activationFile != null );
        final Element root = updateElement( counter, element, xmlTag, shouldExist );
        if ( shouldExist )
        {
            final IndentationCounter innerCount = new IndentationCounter( counter.getDepth() + 1 );
            findAndReplaceSimpleElement( innerCount, root, "missing", activationFile.getMissing() == null ? null
                            : activationFile.getMissing(), null );
            findAndReplaceSimpleElement( innerCount, root, "exists", activationFile.getExists() == null ? null
                            : activationFile.getExists(), null );
        }
    } // -- void updateActivationFile( ActivationFile, String, Counter, Element )

    /**
     * Method updateActivationOS.
     * 
     * @param activationOS
     * @param element
     * @param counter
     * @param xmlTag
     */
    protected void updateActivationOS( final ActivationOS activationOS, final String xmlTag,
                                       final IndentationCounter counter, final Element element )
    {
        final boolean shouldExist = ( activationOS != null );
        final Element root = updateElement( counter, element, xmlTag, shouldExist );
        if ( shouldExist )
        {
            final IndentationCounter innerCount = new IndentationCounter( counter.getDepth() + 1 );
            findAndReplaceSimpleElement( innerCount,
                                         root,
                                         "name",
                                         activationOS.getName() == null ? null : activationOS.getName(),
                                         null );
            findAndReplaceSimpleElement( innerCount, root, "family", activationOS.getFamily() == null ? null
                            : activationOS.getFamily(), null );
            findAndReplaceSimpleElement( innerCount,
                                         root,
                                         "arch",
                                         activationOS.getArch() == null ? null : activationOS.getArch(),
                                         null );
            findAndReplaceSimpleElement( innerCount, root, "version", activationOS.getVersion() == null ? null
                            : activationOS.getVersion(), null );
        }
    } // -- void updateActivationOS( ActivationOS, String, Counter, Element )

    /**
     * Method updateActivationProperty.
     * 
     * @param activationProperty
     * @param element
     * @param counter
     * @param xmlTag
     */
    protected void updateActivationProperty( final ActivationProperty activationProperty, final String xmlTag,
                                             final IndentationCounter counter, final Element element )
    {
        final boolean shouldExist = ( activationProperty != null );
        final Element root = updateElement( counter, element, xmlTag, shouldExist );
        if ( shouldExist )
        {
            final IndentationCounter innerCount = new IndentationCounter( counter.getDepth() + 1 );
            findAndReplaceSimpleElement( innerCount, root, "name", activationProperty.getName() == null ? null
                            : activationProperty.getName(), null );
            findAndReplaceSimpleElement( innerCount, root, "value", activationProperty.getValue() == null ? null
                            : activationProperty.getValue(), null );
        }
    } // -- void updateActivationProperty( ActivationProperty, String, Counter, Element )

    /**
     * Method updateIdentifiableBase.
     * 
     * @param identifiableBase
     * @param element
     * @param counter
     * @param xmlTag
     */
    protected void updateIdentifiableBase( final IdentifiableBase identifiableBase, final String xmlTag,
                                           final IndentationCounter counter, final Element element )
    {
        final boolean shouldExist = ( identifiableBase != null );
        final Element root = updateElement( counter, element, xmlTag, shouldExist );
        if ( shouldExist )
        {
            final IndentationCounter innerCount = new IndentationCounter( counter.getDepth() + 1 );
            findAndReplaceSimpleElement( innerCount, root, "id", identifiableBase.getId() == null ? null
                            : identifiableBase.getId(), "default" );
        }
    } // -- void updateIdentifiableBase( IdentifiableBase, String, Counter, Element )

    /**
     * Method updateMirror.
     * 
     * @param mirror
     * @param element
     * @param counter
     * @param xmlTag
     */
    protected void updateMirror( final Mirror mirror, final String xmlTag, final IndentationCounter counter,
                                 final Element element )
    {
        final Element root = element;
        final IndentationCounter innerCount = new IndentationCounter( counter.getDepth() + 1 );
        findAndReplaceSimpleElement( innerCount,
                                     root,
                                     "mirrorOf",
                                     mirror.getMirrorOf() == null ? null : mirror.getMirrorOf(),
                                     null );
        findAndReplaceSimpleElement( innerCount, root, "name", mirror.getName() == null ? null : mirror.getName(), null );
        findAndReplaceSimpleElement( innerCount, root, "url", mirror.getUrl() == null ? null : mirror.getUrl(), null );
        findAndReplaceSimpleElement( innerCount,
                                     root,
                                     "layout",
                                     mirror.getLayout() == null ? null : mirror.getLayout(),
                                     null );
        findAndReplaceSimpleElement( innerCount, root, "mirrorOfLayouts", mirror.getMirrorOfLayouts() == null ? null
                        : mirror.getMirrorOfLayouts(), "default,legacy" );
        findAndReplaceSimpleElement( innerCount, root, "id", mirror.getId() == null ? null : mirror.getId(), "default" );
    } // -- void updateMirror( Mirror, String, Counter, Element )

    /**
     * Method updateProfile.
     * 
     * @param profile
     * @param element
     * @param counter
     * @param xmlTag
     */
    protected void updateProfile( final Profile profile, final String xmlTag, final IndentationCounter counter,
                                  final Element element )
    {
        final Element root = element;
        final IndentationCounter innerCount = new IndentationCounter( counter.getDepth() + 1 );
        updateActivation( profile.getActivation(), "activation", innerCount, root );
        findAndReplaceProperties( innerCount, root, "properties", profile.getProperties() );
        iterateRepository( innerCount, root, profile.getRepositories(), "repositories", "repository" );
        iterateRepository( innerCount, root, profile.getPluginRepositories(), "pluginRepositories", "pluginRepository" );
        findAndReplaceSimpleElement( innerCount,
                                     root,
                                     "id",
                                     profile.getId() == null ? null : profile.getId(),
                                     "default" );
    } // -- void updateProfile( Profile, String, Counter, Element )

    /**
     * Method updateProxy.
     * 
     * @param proxy
     * @param element
     * @param counter
     * @param xmlTag
     */
    protected void updateProxy( final Proxy proxy, final String xmlTag, final IndentationCounter counter,
                                final Element element )
    {
        final Element root = element;
        final IndentationCounter innerCount = new IndentationCounter( counter.getDepth() + 1 );
        findAndReplaceSimpleElement( innerCount,
                                     root,
                                     "active",
                                     proxy.isActive() == true ? null : String.valueOf( proxy.isActive() ),
                                     "true" );
        findAndReplaceSimpleElement( innerCount,
                                     root,
                                     "protocol",
                                     proxy.getProtocol() == null ? null : proxy.getProtocol(),
                                     "http" );
        findAndReplaceSimpleElement( innerCount,
                                     root,
                                     "username",
                                     proxy.getUsername() == null ? null : proxy.getUsername(),
                                     null );
        findAndReplaceSimpleElement( innerCount,
                                     root,
                                     "password",
                                     proxy.getPassword() == null ? null : proxy.getPassword(),
                                     null );
        findAndReplaceSimpleElement( innerCount,
                                     root,
                                     "port",
                                     proxy.getPort() == 8080 ? null : String.valueOf( proxy.getPort() ),
                                     "8080" );
        findAndReplaceSimpleElement( innerCount, root, "host", proxy.getHost() == null ? null : proxy.getHost(), null );
        findAndReplaceSimpleElement( innerCount,
                                     root,
                                     "nonProxyHosts",
                                     proxy.getNonProxyHosts() == null ? null : proxy.getNonProxyHosts(),
                                     null );
        findAndReplaceSimpleElement( innerCount, root, "id", proxy.getId() == null ? null : proxy.getId(), "default" );
    } // -- void updateProxy( Proxy, String, Counter, Element )

    /**
     * Method updateRepository.
     * 
     * @param repository
     * @param element
     * @param counter
     * @param xmlTag
     */
    protected void updateRepository( final Repository repository, final String xmlTag,
                                     final IndentationCounter counter, final Element element )
    {
        final Element root = element;
        final IndentationCounter innerCount = new IndentationCounter( counter.getDepth() + 1 );
        updateRepositoryPolicy( repository.getReleases(), "releases", innerCount, root );
        updateRepositoryPolicy( repository.getSnapshots(), "snapshots", innerCount, root );
        findAndReplaceSimpleElement( innerCount,
                                     root,
                                     "id",
                                     repository.getId() == null ? null : repository.getId(),
                                     null );
        findAndReplaceSimpleElement( innerCount,
                                     root,
                                     "name",
                                     repository.getName() == null ? null : repository.getName(),
                                     null );
        findAndReplaceSimpleElement( innerCount,
                                     root,
                                     "url",
                                     repository.getUrl() == null ? null : repository.getUrl(),
                                     null );
        findAndReplaceSimpleElement( innerCount,
                                     root,
                                     "layout",
                                     repository.getLayout() == null ? null : repository.getLayout(),
                                     "default" );
    } // -- void updateRepository( Repository, String, Counter, Element )

    /**
     * Method updateRepositoryBase.
     * 
     * @param repositoryBase
     * @param element
     * @param counter
     * @param xmlTag
     */
    protected void updateRepositoryBase( final RepositoryBase repositoryBase, final String xmlTag,
                                         final IndentationCounter counter, final Element element )
    {
        final boolean shouldExist = ( repositoryBase != null );
        final Element root = updateElement( counter, element, xmlTag, shouldExist );
        if ( shouldExist )
        {
            final IndentationCounter innerCount = new IndentationCounter( counter.getDepth() + 1 );
            findAndReplaceSimpleElement( innerCount,
                                         root,
                                         "id",
                                         repositoryBase.getId() == null ? null : repositoryBase.getId(),
                                         null );
            findAndReplaceSimpleElement( innerCount, root, "name", repositoryBase.getName() == null ? null
                            : repositoryBase.getName(), null );
            findAndReplaceSimpleElement( innerCount, root, "url", repositoryBase.getUrl() == null ? null
                            : repositoryBase.getUrl(), null );
            findAndReplaceSimpleElement( innerCount, root, "layout", repositoryBase.getLayout() == null ? null
                            : repositoryBase.getLayout(), "default" );
        }
    } // -- void updateRepositoryBase( RepositoryBase, String, Counter, Element )

    /**
     * Method updateRepositoryPolicy.
     * 
     * @param repositoryPolicy
     * @param element
     * @param counter
     * @param xmlTag
     */
    protected void updateRepositoryPolicy( final RepositoryPolicy repositoryPolicy, final String xmlTag,
                                           final IndentationCounter counter, final Element element )
    {
        final boolean shouldExist = ( repositoryPolicy != null );
        final Element root = updateElement( counter, element, xmlTag, shouldExist );
        if ( shouldExist )
        {
            final IndentationCounter innerCount = new IndentationCounter( counter.getDepth() + 1 );
            findAndReplaceSimpleElement( innerCount, root, "enabled", repositoryPolicy.isEnabled() == true ? null
                            : String.valueOf( repositoryPolicy.isEnabled() ), "true" );
            findAndReplaceSimpleElement( innerCount,
                                         root,
                                         "updatePolicy",
                                         repositoryPolicy.getUpdatePolicy() == null ? null
                                                         : repositoryPolicy.getUpdatePolicy(),
                                         null );
            findAndReplaceSimpleElement( innerCount,
                                         root,
                                         "checksumPolicy",
                                         repositoryPolicy.getChecksumPolicy() == null ? null
                                                         : repositoryPolicy.getChecksumPolicy(),
                                         null );
        }
    } // -- void updateRepositoryPolicy( RepositoryPolicy, String, Counter, Element )

    /**
     * Method updateServer.
     * 
     * @param server
     * @param element
     * @param counter
     * @param xmlTag
     */
    protected void updateServer( final Server server, final String xmlTag, final IndentationCounter counter,
                                 final Element element )
    {
        final Element root = element;
        final IndentationCounter innerCount = new IndentationCounter( counter.getDepth() + 1 );
        findAndReplaceSimpleElement( innerCount,
                                     root,
                                     "username",
                                     server.getUsername() == null ? null : server.getUsername(),
                                     null );
        findAndReplaceSimpleElement( innerCount,
                                     root,
                                     "password",
                                     server.getPassword() == null ? null : server.getPassword(),
                                     null );
        findAndReplaceSimpleElement( innerCount,
                                     root,
                                     "privateKey",
                                     server.getPrivateKey() == null ? null : server.getPrivateKey(),
                                     null );
        findAndReplaceSimpleElement( innerCount,
                                     root,
                                     "passphrase",
                                     server.getPassphrase() == null ? null : server.getPassphrase(),
                                     null );
        findAndReplaceSimpleElement( innerCount, root, "filePermissions", server.getFilePermissions() == null ? null
                        : server.getFilePermissions(), null );
        findAndReplaceSimpleElement( innerCount,
                                     root,
                                     "directoryPermissions",
                                     server.getDirectoryPermissions() == null ? null : server.getDirectoryPermissions(),
                                     null );
        findAndReplaceXpp3DOM( innerCount, root, "configuration", (Xpp3Dom) server.getConfiguration() );
        findAndReplaceSimpleElement( innerCount, root, "id", server.getId() == null ? null : server.getId(), "default" );
    } // -- void updateServer( Server, String, Counter, Element )

    /**
     * Method updateSettings.
     * 
     * @param settings
     * @param element
     * @param counter
     * @param xmlTag
     */
    protected void updateSettings( final Settings settings, final String xmlTag, final IndentationCounter counter,
                                   final Element element )
    {
        final Element root = element;
        final IndentationCounter innerCount = new IndentationCounter( counter.getDepth() + 1 );
        findAndReplaceSimpleElement( innerCount, root, "localRepository", settings.getLocalRepository() == null ? null
                        : settings.getLocalRepository(), null );
        findAndReplaceSimpleElement( innerCount, root, "interactiveMode", settings.isInteractiveMode() == true ? null
                        : String.valueOf( settings.isInteractiveMode() ), "true" );
        findAndReplaceSimpleElement( innerCount,
                                     root,
                                     "usePluginRegistry",
                                     settings.isUsePluginRegistry() == false ? null
                                                     : String.valueOf( settings.isUsePluginRegistry() ),
                                     "false" );
        findAndReplaceSimpleElement( innerCount,
                                     root,
                                     "offline",
                                     settings.isOffline() == false ? null : String.valueOf( settings.isOffline() ),
                                     "false" );
        iterateProxy( innerCount, root, settings.getProxies(), "proxies", "proxy" );
        iterateServer( innerCount, root, settings.getServers(), "servers", "server" );
        iterateMirror( innerCount, root, settings.getMirrors(), "mirrors", "mirror" );
        iterateProfile( innerCount, root, settings.getProfiles(), "profiles", "profile" );
        findAndReplaceSimpleLists( innerCount, root, settings.getActiveProfiles(), "activeProfiles", "activeProfile" );
        findAndReplaceSimpleLists( innerCount, root, settings.getPluginGroups(), "pluginGroups", "pluginGroup" );
    } // -- void updateSettings( Settings, String, Counter, Element )

    /**
     * Method updateTrackableBase.
     * 
     * @param trackableBase
     * @param element
     * @param counter
     * @param xmlTag
     */
    protected void updateTrackableBase( final TrackableBase trackableBase, final String xmlTag,
                                        final IndentationCounter counter, final Element element )
    {
        final boolean shouldExist = ( trackableBase != null );
        final Element root = updateElement( counter, element, xmlTag, shouldExist );
        if ( shouldExist )
        {
            final IndentationCounter innerCount = new IndentationCounter( counter.getDepth() + 1 );
        }
    } // -- void updateTrackableBase( TrackableBase, String, Counter, Element )

    @Override
    protected void update( final Settings source, final IndentationCounter indentationCounter, final Element rootElement )
        throws IOException
    {
        updateSettings( source, "settings", indentationCounter, rootElement );
    }

}
