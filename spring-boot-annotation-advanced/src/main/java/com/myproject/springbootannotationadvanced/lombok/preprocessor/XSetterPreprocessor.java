package com.myproject.springbootannotationadvanced.lombok.preprocessor;

import com.google.auto.service.AutoService;
import com.myproject.springbootannotationadvanced.lombok.annotation.XSetter;
import com.myproject.springbootannotationadvanced.lombok.sides.XAccessLevel;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import java.util.Arrays;
import java.util.Set;

/**
 * @author nguyenle
 * @since 4:50 PM Wed 2/19/2025
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes("XSetter")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class XSetterPreprocessor extends AbstractProcessor {

	private static final String METHOD_SIGNATURE_PATTERN = "%s void %s(%s %s) { %s }";

	private static final String SETTER_PATTERN = "set%s";

	private static final String SETTER_FUNCTION_BODY_PATTERN = """
			this.%s = %s;
			""";

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		for (Element element : roundEnv.getElementsAnnotatedWith(XSetter.class)) {
			if (element.getKind() == ElementKind.CLASS) {
				processClass((TypeElement) element);
			} else if (element.getKind() == ElementKind.FIELD && !(element.getModifiers().contains(Modifier.STATIC))) {
				processField((VariableElement) element);
			}
		}
		return true;
	}

	private void processClass(TypeElement classElement) {
		String className = classElement.getQualifiedName().toString();
		try {
			ClassPool classPool = ClassPool.getDefault();
			CtClass ctClass = classPool.getCtClass(className);

			XSetter classSetter = classElement.getAnnotation(XSetter.class);
			for (Element field : classElement.getEnclosedElements()) {
				if (field.getKind() == ElementKind.FIELD && !(field.getModifiers().contains(Modifier.STATIC))) {
					XSetter fieldSetter = field.getAnnotation(XSetter.class);
					XSetter effectiveSetter = fieldSetter != null ? fieldSetter : classSetter;

					addSetter(ctClass, (VariableElement) field, effectiveSetter, classSetter);
				}
			}

			ctClass.writeFile();
			ctClass.detach();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void processField(VariableElement fieldElement) {
		TypeElement classElement = (TypeElement) fieldElement.getEnclosingElement();
		String className = classElement.getQualifiedName().toString();

		try {
			ClassPool classPool = ClassPool.getDefault();
			CtClass ctClass = classPool.getCtClass(className);
			XSetter classSetter = classElement.getAnnotation(XSetter.class);
			XSetter fieldSetter = fieldElement.getAnnotation(XSetter.class);

			addSetter(ctClass, fieldElement, fieldSetter, classSetter);

			ctClass.writeFile();
			ctClass.detach();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void addSetter(CtClass ctClass, VariableElement fieldElement, XSetter fieldSetter, XSetter classSetter) throws CannotCompileException {
		String fieldName = fieldElement.getSimpleName().toString();
		String fieldType = fieldElement.asType().toString();

		String setterName = String.format(
				SETTER_PATTERN,
				fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1)
		);

		if (Arrays.stream(ctClass.getDeclaredMethods()).anyMatch((method) -> method.getName().equals(setterName))) {
			return;
		}

		XAccessLevel  classAccessLevel = classSetter != null ? classSetter.value() : XAccessLevel.PUBLIC;
		XAccessLevel fieldAccessLevel = fieldSetter != null ? fieldSetter.value() : classAccessLevel;
		XAccessLevel effectiveAccessLevel = getLeastRestrictiveAccessLevel(classAccessLevel, fieldAccessLevel);

		String visibilityAccessModifier = effectiveAccessLevel.getFunctionAccessModifier();

		if (effectiveAccessLevel == XAccessLevel.NONE) {
			return;
		}

		String setterCode = String.format(
				SETTER_FUNCTION_BODY_PATTERN,
				fieldName, fieldName
		);

		String methodSignature = String.format(
				METHOD_SIGNATURE_PATTERN,
				visibilityAccessModifier, setterName, fieldType, fieldName, setterCode
		);

		CtMethod method = CtMethod.make(methodSignature, ctClass);
		ctClass.addMethod(method);
	}

	private XAccessLevel getLeastRestrictiveAccessLevel(XAccessLevel accessLevelA, XAccessLevel accessLevelB) {
		int minPriority = Math.min(accessLevelA.getPriority(), accessLevelB.getPriority());
		return XAccessLevel.getXAccessLevel(minPriority);
	}

}
