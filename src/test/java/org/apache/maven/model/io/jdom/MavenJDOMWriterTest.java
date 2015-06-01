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
package org.apache.maven.model.io.jdom;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.IOUtil;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class MavenJDOMWriterTest
{

    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

    @Test
    public void antlibWithElementNamespace()
        throws Exception
    {
        InputStream in = Thread.currentThread()
                               .getContextClassLoader()
                               .getResourceAsStream( "rat-user.pom" );

        final File file = temp.newFile();
        final OutputStream out = new FileOutputStream( file );
        IOUtil.copy( in, out );
        in.close();
        out.close();

        in = new FileInputStream( file );
        final Model model = new MavenXpp3Reader().read( in );
        in.close();

        System.out.println( "Writing model: " + model + " to file: " + file );

        new MavenJDOMWriter( model ).write( model, file );
    }

}
