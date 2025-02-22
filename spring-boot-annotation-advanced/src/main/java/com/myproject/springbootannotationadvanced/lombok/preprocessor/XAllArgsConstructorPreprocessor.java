package com.myproject.springbootannotationadvanced.lombok.preprocessor;

import com.google.auto.service.AutoService;
import com.myproject.springbootannotationadvanced.lombok.annotation.XAllArgsConstructor;
import com.myproject.springbootannotationadvanced.lombok.sides.XAccessLevel;
import javassist.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author nguyenle
 * @since 4:51 PM Wed 2/19/2025
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes("XAllArgsConstructor")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class XAllArgsConstructorPreprocessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(XAllArgsConstructor.class)) {
            if (element.getKind() == ElementKind.CLASS) {
                XAllArgsConstructor annotation = element.getAnnotation(XAllArgsConstructor.class);
                generateAllArgsConstructor((TypeElement) element, annotation);
            }
        }
        return true;
    }

    private void generateAllArgsConstructor(TypeElement element, XAllArgsConstructor annotation) {
        String className = element.getQualifiedName().toString();
        try {
            ClassPool classPool = ClassPool.getDefault();
            CtClass ctClass = classPool.get(className);

            XAccessLevel level = annotation.level();
            boolean force = annotation.force();

            if (level == XAccessLevel.NONE) {
                return;
            }

            List<CtField> declaredFields = List.of(ctClass.getDeclaredFields());
            List<CtClass> paramsTypes = new ArrayList<>();
            for (CtField field : declaredFields) {
                paramsTypes.add(field.getType());
            }

            boolean isAnotherAllArgsConstructorExisted = false;

            for (CtConstructor constructor : ctClass.getConstructors()) {
                if (isConstructorIsAllArgs(constructor, paramsTypes)) {
                    isAnotherAllArgsConstructorExisted = true;
                    if (force) {
                        constructor.setModifiers(resolveModifier(level));
                    }
                    break;
                }
            }

            if (!isAnotherAllArgsConstructorExisted) {
                CtConstructor allArgsConstructor = new CtConstructor(paramsTypes.toArray(new CtClass[0]), ctClass);
                allArgsConstructor.setModifiers(resolveModifier(level));

                StringBuilder constructorBody = new StringBuilder("{");
                for (int i = 0; i < paramsTypes.size(); i++) {
                    constructorBody.append("this.").append(declaredFields.get(i).getName())
                            .append(" = $").append(i + 1).append(";");
                }
                constructorBody.append("}");

                allArgsConstructor.setBody(constructorBody.toString());
                ctClass.addConstructor(allArgsConstructor);
            }

            ctClass.writeFile();
            ctClass.detach();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean isConstructorIsAllArgs(CtConstructor constructor, List<CtClass> paramsTypes) {
        try {
            CtClass[] constructorParams = constructor.getParameterTypes();
            return Arrays.equals(constructorParams, paramsTypes.toArray(new CtClass[0]));
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
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
