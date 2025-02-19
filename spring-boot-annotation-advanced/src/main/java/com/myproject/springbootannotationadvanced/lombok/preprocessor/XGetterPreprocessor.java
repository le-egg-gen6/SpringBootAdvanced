package com.myproject.springbootannotationadvanced.lombok.preprocessor;

import com.google.auto.service.AutoService;
import com.myproject.springbootannotationadvanced.lombok.annotation.XGetter;
import com.myproject.springbootannotationadvanced.lombok.sides.XAccessLevel;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import java.util.Arrays;
import java.util.Set;

/**
 * @author nguyenle
 * @since 4:45 PM Wed 2/19/2025
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes("XGetter")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class XGetterPreprocessor extends AbstractProcessor {

	private static final String METHOD_SIGNATURE_PATTERN = "%s %s %s() { %s }";

	private static final String GETTER_PATTERN = "get%s";

	private static final String GETTER_LAZY_INITIALIZE_FUNCTION_BODY_PATTERN = """
			if (this.%s == null) {
				this.%s = new %s();
			}
			return this.%s;
			""";

	private static final String GETTER_DEFAULT_FUNCTION_BODY_PATTERN = """
			return this.%s;
			""";

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		for (Element element : roundEnv.getElementsAnnotatedWith(XGetter.class)) {
			if (element.getKind() == ElementKind.CLASS) {
				processClass((TypeElement) element);
			} else if (element.getKind() == ElementKind.FIELD) {
				processField((VariableElement) element.getEnclosingElement());
			}
		}
		return true;
	}

	private void processClass(TypeElement classElement) {
		String className = classElement.getQualifiedName().toString();
		try {
			ClassPool classPool = ClassPool.getDefault();
			CtClass ctClass = classPool.getCtClass(className);
			XGetter classGetter = classElement.getAnnotation(XGetter.class);

			for (Element field : classElement.getEnclosedElements()) {
				if (field.getKind() == ElementKind.FIELD && !(field.getModifiers().contains(Modifier.STATIC))) {
					XGetter fieldGetter = field.getAnnotation(XGetter.class);
					XGetter effectiveFieldGetter = fieldGetter != null ? fieldGetter : classGetter;

					addGetter(ctClass, (VariableElement) field, effectiveFieldGetter, classGetter);

				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void processField(VariableElement fieldElement) {
		TypeElement classElement = (TypeElement) fieldElement.getEnclosingElement();
		String className = classElement.getQualifiedName().toString();

		try {
			ClassPool pool = ClassPool.getDefault();
			CtClass ctClass = pool.get(className);
			XGetter fieldGetter = fieldElement.getAnnotation(XGetter.class);
			XGetter classGetter = classElement.getAnnotation(XGetter.class);
			addGetter(ctClass, fieldElement, fieldGetter, classGetter);

			ctClass.writeFile();
			ctClass.detach();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addGetter(CtClass ctClass, VariableElement field, XGetter fieldGetter, XGetter classGetter) throws CannotCompileException {
		String fieldName = field.getSimpleName().toString();
		String fieldType = field.asType().toString();

		String getterName = String.format(
				GETTER_PATTERN,
				fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1)
		);

		if (Arrays.stream(ctClass.getMethods()).anyMatch(method -> method.getName().equals(getterName))) {
			return;
		}

		XAccessLevel  classAccessLevel = classGetter != null ? classGetter.value() : XAccessLevel.PUBLIC;
		XAccessLevel fieldAccessLevel = fieldGetter != null ? fieldGetter.value() : classAccessLevel;
		XAccessLevel effectiveAccessLevel = getLeastRestrictiveAccessLevel(classAccessLevel, fieldAccessLevel);

		boolean isLazyInitialization = classGetter != null && classGetter.lazy();
		if (fieldGetter != null) {
            isLazyInitialization = fieldGetter.lazy();
		}

		String visibilityAccessModifier = effectiveAccessLevel.getFunctionAccessModifier();

		if (effectiveAccessLevel == XAccessLevel.NONE) {
			return;
		}

		String getterCode;
		if (
			isLazyInitialization && isFieldPrimitive(field)
		) {
			getterCode = String.format(
					GETTER_LAZY_INITIALIZE_FUNCTION_BODY_PATTERN,
					fieldName, fieldName, fieldType, fieldName
			);
		} else {
			getterCode = String.format(
					GETTER_DEFAULT_FUNCTION_BODY_PATTERN,
					fieldName
			);
		}

		String methodSignature = String.format(
				METHOD_SIGNATURE_PATTERN,
				visibilityAccessModifier, fieldType, getterName, getterCode
		);

		CtMethod getterMethod = CtMethod.make(methodSignature, ctClass);
		ctClass.addMethod(getterMethod);
	}

	private XAccessLevel getLeastRestrictiveAccessLevel(XAccessLevel accessLevelA, XAccessLevel accessLevelB) {
		int minPriority = Math.min(accessLevelA.getPriority(), accessLevelB.getPriority());
		return XAccessLevel.getXAccessLevel(minPriority);
	}

	private boolean isFieldPrimitive(VariableElement field) {
		TypeMirror typeMirror = field.asType();
		return typeMirror.getKind().isPrimitive();
	}

}
