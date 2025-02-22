package com.myproject.springbootannotationadvanced.lombok.preprocessor;

import com.google.auto.service.AutoService;
import com.myproject.springbootannotationadvanced.lombok.annotation.XToString;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author nguyenle
 * @since 4:50 PM Wed 2/19/2025
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes("XToString")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class XToStringPreprocessor extends AbstractProcessor {

	private static final String TO_STRING_METHOD_PATTERN = """
			@Override
			public String toString() { return %s; }
			""";

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		for (Element element : roundEnv.getElementsAnnotatedWith(XToString.class)) {
			if (element.getKind() != ElementKind.CLASS) {
				continue;
			}

			TypeElement typeElement = (TypeElement) element;
			XToString annotation = typeElement.getAnnotation(XToString.class);
			boolean includeFieldName = annotation.includeFieldName();
			String[] excludeFields = annotation.exclude();

			generateToStringMethod(typeElement, includeFieldName, excludeFields);
		}
		return true;
	}

	private void generateToStringMethod(TypeElement typeElement, boolean includeFieldName, String[] excludeFields) {
		String className = typeElement.getQualifiedName().toString();
		List<String> excludedFields = Arrays.asList(excludeFields);
		try {
			ClassPool classPool = ClassPool.getDefault();
			CtClass ctClass = classPool.getCtClass(className);

			StringBuilder sb = new StringBuilder("\"");

			for (Element element : typeElement.getEnclosedElements()) {
				if (element.getKind() == ElementKind.FIELD && !element.getModifiers().contains(Modifier.STATIC)) {
					String fieldName = element.getSimpleName().toString();
					if (!excludedFields.contains(element.getSimpleName().toString())) {
						sb.append(includeFieldName ? fieldName + "=" : "");
						sb.append("\" + this.").append(element.getSimpleName().toString()).append(" + \" ");
					}
				}
			}
			sb.append("\"");
			String methodSignature = String.format(
					TO_STRING_METHOD_PATTERN,
					sb
			);

			CtMethod method = CtMethod.make(methodSignature, ctClass);
			ctClass.addMethod(method);

			ctClass.writeFile();
			ctClass.detach();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
