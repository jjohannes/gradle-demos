package de.jjohannes.codegen.java

import de.jjohannes.codegen.CodeGenerator
import de.jjohannes.codegen.CodeGeneratorConfig

object AppJavaGen : CodeGenerator {

    override fun fileName(conf: CodeGeneratorConfig) = with(conf) {
        "src/main/java/${projectPackage.get().replace('.', '/')}/${projectName.get()}App.java"
    }

    override fun generateContent(conf: CodeGeneratorConfig) = with(conf) {
        """
            package org.example;

            import org.jdom2.Document;
            import org.jdom2.input.SAXBuilder;

            import java.net.URL;

            public class ${projectName.get()}App {
                public static void main(String[] args) throws Exception {
                    URL conf = ${projectName.get()}App.class.getResource("../../conf.xml");
                    SAXBuilder saxBuilder = new SAXBuilder();
                    Document document = saxBuilder.build(conf);

                    int cycles = document.getRootElement().getChild("cycles").getAttribute("count").getIntValue();

                    System.out.println("Hello from ${projectName.get()}!");
                    System.out.println("You are running with " + cycles + " cycles!");
                }
            }
        """
    }
}