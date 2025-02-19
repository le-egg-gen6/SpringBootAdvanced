package com.myproject.springbootannotationadvanced.core.container;

import com.myproject.springbootannotationadvanced.core.annotation.XAutowired;
import com.myproject.springbootannotationadvanced.core.annotation.XComponent;
import com.myproject.springbootannotationadvanced.core.annotation.XQualifier;
import com.myproject.springbootannotationadvanced.core.annotation.XRepository;
import com.myproject.springbootannotationadvanced.core.annotation.XService;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author nguyenle
 * @since 2:47 PM Wed 2/19/2025
 */
public class DIContainer {

	private Map<String, Object> beans = new HashMap<>();

	private Map<String, Class<?>> beanClasses = new HashMap<>();

	private Map<String, Set<String>> qualifierMap = new HashMap<>();

	public void scanPackages(String... packages) {
		for (String pkg : packages) {
			findComponents(pkg);
		}
		initializeBeans();
	}

	public Object getBean(String name) {
		return beans.get(name);
	}

	public <T> T getBean(Class<T> type) {
		for (Object bean : beans.values()) {
			if (type.isInstance(bean)) {
				return type.cast(bean);
			}
		}
		throw new RuntimeException("No bean found of type: " + type.getName());
	}

	private void findComponents(String basePackage) {
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			String path = basePackage.replace('.', '/');
			Enumeration<URL> resources = classLoader.getResources(path);

			while (resources.hasMoreElements()) {
				URL resource = resources.nextElement();
				File directory = new File(resource.getFile());
				if (directory.exists()) {
					scanDirectory(directory, basePackage);
				}
			}
		} catch (Exception ex) {
			throw new RuntimeException("Failed to scan package " + basePackage, ex);
		}
	}

	private void scanDirectory(File directory, String basePackage) {
		File[] files = directory.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					scanDirectory(file, basePackage + "." + file.getName());
				} else if (file.getName().endsWith(".class")) {
					String className = basePackage + "." + file.getName().replace(".class", "");
					processClass(className);
				}
			}
		}
	}

	private void processClass(String className) {
		try {
			Class<?> clazz = Class.forName(className);
			if (isClassComponent(clazz)) {
				String beanName = getBeanName(clazz);
				beanClasses.put(beanName, clazz);

				processQualifiers(clazz, beanName);
			}
		} catch (ClassNotFoundException ex) {
			throw new RuntimeException("Failed to load class " + className, ex);
		}
	}

	private boolean isClassComponent(Class<?> clazz) {
		return clazz.isAnnotationPresent(XComponent.class)
			|| clazz.isAnnotationPresent(XService.class)
			|| clazz.isAnnotationPresent(XRepository.class);
	}

	private void processQualifiers(Class<?> clazz, String beanName) {
		XQualifier qualifier = clazz.getAnnotation(XQualifier.class);
		if (qualifier != null) {
			String qualifierValue = qualifier.value();
			qualifierMap.computeIfAbsent(qualifierValue, k -> new HashSet<>()).add(beanName);
		}
	}

	private String getBeanName(Class<?> clazz) {
		String name = "";

		if (clazz.isAnnotationPresent(XComponent.class)) {
			name = clazz.getAnnotation(XComponent.class).value();
		} else if (clazz.isAnnotationPresent(XService.class)) {
			name = clazz.getAnnotation(XService.class).value();
		} else if (clazz.isAnnotationPresent(XRepository.class)) {
			name = clazz.getAnnotation(XRepository.class).value();
		}

		if (name.isEmpty()) {
			name = clazz.getSimpleName();
			name = name.substring(0, 1).toLowerCase() + name.substring(1);
		}

		return name;
	}

	private void initializeBeans() {
		for (Map.Entry<String, Class<?>> entry : beanClasses.entrySet()) {
			String beanName = entry.getKey();
			Class<?> clazz = entry.getValue();
			Object bean = createBean(clazz);
			beans.put(beanName, bean);
		}

		for (Object bean : beans.values()) {
			injectDependencies(bean);
		}
	}

	private Object createBean(Class<?> clazz) {
		try {
			Constructor<?>[] constructors = clazz.getDeclaredConstructors();

			Constructor<?> autowiredConstructor = null;

			for (Constructor<?> constructor : constructors) {
				if (constructor.isAnnotationPresent(XAutowired.class)) {
					if (autowiredConstructor != null) {
						throw new RuntimeException("Multiple @XAutowired constructors found in " + clazz.getName());
					}
					autowiredConstructor = constructor;
				}
			}

			if (autowiredConstructor == null) {
				for (Constructor<?> constructor : constructors) {
					if (constructor.getParameterCount() == 0) {
						constructor.setAccessible(true);
						return constructor.newInstance();
					}
				}
			}

			if (autowiredConstructor != null) {
				autowiredConstructor.setAccessible(true);
				Object[] dependencies = resolveDependencies(autowiredConstructor);
				return autowiredConstructor.newInstance(dependencies);
			}

			throw new RuntimeException("No suitable constructor found in " + clazz.getName());
		} catch (Exception ex) {
			throw new RuntimeException("Failed to create bean " + clazz, ex);
		}
	}

	private Object[] resolveDependencies(Constructor<?> constructor) {
		Parameter[] parameters = constructor.getParameters();
		Object[] dependencies = new Object[parameters.length];

		for (int i = 0; i < parameters.length; i++) {
			Parameter parameter = parameters[i];
			XQualifier qualifier = parameter.getAnnotation(XQualifier.class);

			if (qualifier != null) {
				dependencies[i] = findDependencyByQualifier(parameter.getType(), qualifier.value());
			} else {
				dependencies[i] = findDependencyByClass(parameter.getType());
			}
		}

		return dependencies;
	}

	private void injectDependencies(Object bean) {
		Class<?> clazz = bean.getClass();

		for (Field field : clazz.getDeclaredFields()) {
			if (field.isAnnotationPresent(XAutowired.class)) {
				field.setAccessible(true);
				Object dependency = null;

				XQualifier qualifier = field.getDeclaredAnnotation(XQualifier.class);
				if (qualifier != null) {
					dependency = findDependencyByQualifier(field.getType(), qualifier.value());
				} else {
					dependency = findDependencyByClass(field.getType());
				}

				try {
					field.set(bean, dependency);
				} catch (Exception ex) {
					throw new RuntimeException("Failed to inject dependency " + dependency, ex);
				}
			}
		}
	}

	private Object findDependencyByQualifier(Class<?> type, String qualifier) {
		Set<String> qualifiedBeans = qualifierMap.get(qualifier);
		if (qualifiedBeans == null || qualifiedBeans.isEmpty()) {
			throw new RuntimeException("No bean found with qualifier: " + qualifier);
		}

		for (String beanName : qualifiedBeans) {
			Object bean = beans.get(beanName);
			if (type.isInstance(bean)) {
				return bean;
			}
		}

		throw new RuntimeException("No bean found of type " + type.getName() +
			" with qualifier: " + qualifier);
	}

	private Object findDependencyByClass(Class<?> clazz) {
		for (Object bean : beans.values()) {
			if (clazz.isInstance(bean)) {
				return bean;
			}
		}
		throw new RuntimeException("No bean found of type: " + clazz.getName());
	}
}
