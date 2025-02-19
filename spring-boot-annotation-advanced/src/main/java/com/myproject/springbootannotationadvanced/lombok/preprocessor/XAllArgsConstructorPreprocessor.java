package com.myproject.springbootannotationadvanced.lombok.preprocessor;

import com.google.auto.service.AutoService;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

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
		return false;
	}
}
