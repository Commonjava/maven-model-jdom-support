JDom Support for Common Maven Models
====================================

This project simply extracts the Modello source files from the Apache Maven distribution source zip, and re-runs the Modello Maven plugin on them using the 'jdom-writer' goal. The point of this exercise is to provide a reusable artifact that contains the JDom ModelWriter classes for common models found in Maven. The advantage to the JDom writers is that they can preserve existing order of elements, formatting, and comments.

Currently, this project produces writers for the following models:

- pom.xml
- settings.xml
- toolchains.xml
- maven-metadata.xml

Example
--------

To write changes to a Maven pom.xml, with formats/comments preserved:

    File pom = new File( "pom.xml" );
    Writer writer = null;
    try
    {
        final SAXBuilder builder = new SAXBuilder();
        builder.setIgnoringBoundaryWhitespace( false );
        builder.setIgnoringElementContentWhitespace( false );
    
        final Document doc = builder.build( pom );
    
        String encoding = model.getModelEncoding();
        if ( encoding == null )
        {
            encoding = "UTF-8";
        }
    
        final Format format = Format.getRawFormat().setEncoding( encoding ).setTextMode( TextMode.PRESERVE );
    
        writer = WriterFactory.newWriter( pom, encoding );
    
        new MavenJDOMWriter().write( model, doc, writer, format );
    }
    finally
    {
        IOUtil.close( writer );
    }

To use this code, you'll have to add the JDom dependency to your project's pom.xml:

    <dependency>
      <groupId>org.jdom</groupId>
      <artifactId>jdom</artifactId>
      <version>1.1</version>
    </dependency>

