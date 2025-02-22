package com.myproject.springbootannotationadvanced.lombok.preprocessor;

import com.google.auto.service.AutoService;
import com.myproject.springbootannotationadvanced.lombok.annotation.XNoArgsConstructor;
import com.myproject.springbootannotationadvanced.lombok.sides.XAccessLevel;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.Modifier;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import java.util.Set;

/**
 * @author nguyenle
 * @since 4:45 PM Wed 2/19/2025
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes("XNoArgsConstructor")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class XNoArgsConstructorPreprocessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(XNoArgsConstructor.class)) {
            if (element.getKind() == ElementKind.CLASS) {
                XNoArgsConstructor annotation = element.getAnnotation(XNoArgsConstructor.class);
                generateNoArgsConstructor((TypeElement) element, annotation);
            }
        }
        return true;
    }

    private void generateNoArgsConstructor(TypeElement element, XNoArgsConstructor annotation) {
        String className = element.getQualifiedName().toString();
        try {
            ClassPool classPool = ClassPool.getDefault();
            CtClass ctClass = classPool.get(className);

            boolean force = annotation.force();
            XAccessLevel accessLevel = annotation.level();

            if (accessLevel == XAccessLevel.NONE) {
                return;
            }

            boolean hasNoArgsConstructor = false;
            for (CtConstructor constructor : ctClass.getDeclaredConstructors()) {
                if (constructor.getParameterTypes().length == 0) {
                    hasNoArgsConstructor = true;
                    if (force) {
                        constructor.setModifiers(resolveModifier(accessLevel));
                    }
                    break;
                }
            }

            if (!hasNoArgsConstructor) {
                CtConstructor noArgsConstructor = new CtConstructor(new CtClass[0], ctClass);
                noArgsConstructor.setModifiers(resolveModifier(accessLevel));
                ctClass.addConstructor(noArgsConstructor);
            }

            ctClass.writeFile();
            ctClass.detach();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private int resolveModifier(XAccessLevel accessLevel) {
        switch (accessLevel) {
            case PUBLIC:
                return Modifier.PUBLIC;
            case PROTECTED:
                return Modifier.PROTECTED;
            case PRIVATE:
                return Modifier.PRIVATE;
            default:
                return 0;
        }
    }

}
