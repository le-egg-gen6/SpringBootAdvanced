package com.myproject.springbootannotationadvanced.lombok.preprocessor;

import com.google.auto.service.AutoService;
import com.myproject.springbootannotationadvanced.lombok.annotation.XSlf4j;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import java.util.Set;

/**
 * @author nguyenle
 * @since 4:50 PM Wed 2/19/2025
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes("XSlf4j")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class XSlf4jPreprocessor extends AbstractProcessor {

    private static final String LOGGER_INJECT_PATTERN = """
            private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger("%s");
            """;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(XSlf4j.class)) {
            if (element.getKind() == ElementKind.CLASS) {
                modifyClas((TypeElement) element);
            }
        }
        return true;
    }

    private void modifyClas(TypeElement classElement) {
        String className = classElement.getQualifiedName().toString();
        XSlf4j annotation = classElement.getAnnotation(XSlf4j.class);
        String topic = annotation.topic().isEmpty() ? className : annotation.topic();

        try {
            ClassPool classPool = ClassPool.getDefault();
            CtClass ctClass = classPool.getCtClass(className);

            CtField[] ctDeclaredFields = ctClass.getDeclaredFields();
            for (CtField field : ctDeclaredFields) {
                if (field.getType().getName().equals("org.slf4j.Logger")) {
                    return;
                }
            }

            CtField ctField = CtField.make(String.format(LOGGER_INJECT_PATTERN, topic), ctClass);
            ctClass.addField(ctField);

            ctClass.writeFile();
            ctClass.detach();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
