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
