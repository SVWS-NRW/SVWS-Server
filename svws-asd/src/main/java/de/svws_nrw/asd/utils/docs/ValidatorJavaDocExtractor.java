package de.svws_nrw.asd.utils.docs;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;

import de.svws_nrw.transpiler.TranspilerLanguagePlugin;

/**
 * This class provides a transpiler for transpiling java source code
 * into another programming language that is supported by implementing
 * the {@link TranspilerLanguagePlugin} class. Therefore the
 * Java Compiler API (module java.compiler) and its abstract syntax tress
 * (AST, module jdk.compiler) are used.
 */
@SupportedSourceVersion(SourceVersion.RELEASE_21)
@SupportedAnnotationTypes("*")
public final class ValidatorJavaDocExtractor extends AbstractProcessor {

	/// Java input and compiler attributes

	/** All Java files used for the transpiler input */
	private final Iterable<? extends JavaFileObject> javaFiles;

	/** The reference to the compiler instance used by thsi transpiler */
	private final JavaCompiler compiler;

	/** The diagnostics collector of the java compiler to report infos, warnings and erros regarding the Java source code */
	private final DiagnosticCollector<JavaFileObject> diagnostics;

	/** The file manager für the Java source files */
	private final StandardJavaFileManager javaFileManager;

	/** A temporary variable used by the annotation processor for accessing the Tree-API of the java compiler  */
	private Trees trees;

	//** A reference to the type utils of the java compiler processing environment */
	//private Types typeUtils;

	//** A reference to the element utils of the java compiler processing environment */
	//private Elements elementUtils;


	/**
	 * Creates a new Scanner object for retrieving the JavaDoc-comments
	 *
	 * @param sources    the {@link File}-Objects for all Java-Sources
	 */
	public ValidatorJavaDocExtractor(final File... sources) {
		compiler = ToolProvider.getSystemJavaCompiler();
		diagnostics = new DiagnosticCollector<>();
		javaFileManager = compiler.getStandardFileManager(diagnostics, null, null);
		javaFiles = javaFileManager.getJavaFileObjects(sources);
	}


	/* Overrides {@link AbstractProcessor#init(ProcessingEnvironment)} for reading
	 * all trees of the Tree-API of the Java Compiler. */
	@Override
	public synchronized void init(final ProcessingEnvironment processingEnv) {
		super.init(processingEnv);
		trees = Trees.instance(processingEnv);
		//elementUtils = processingEnv.getElementUtils();
		//typeUtils = processingEnv.getTypeUtils();
	}


	/**
	 * Starts the extraction of the JavaDoc-Comments for all Java sources passed to the extractor
	 */
	public void extract() {
		System.out.println("Running JavaDoc extraction...");
		final ArrayList<String> options = new ArrayList<>();
		options.add("-encoding");
		options.add("UTF-8");
		final CompilationTask compilationTask = compiler.getTask(null, javaFileManager, diagnostics, options, null, javaFiles);
		compilationTask.setProcessors(Arrays.asList(this));
		if (Boolean.FALSE.equals(compilationTask.call())) {
			System.out.println("Fehler beim Extrahieren der JavaDoc-Kommentare!");
			final List<Diagnostic<? extends JavaFileObject>> diags = diagnostics.getDiagnostics();
			for (final Diagnostic<? extends JavaFileObject> diag : diags) {
				System.err.println(diag.getMessage(null));
			}
		}
	}


	/* Overrides {@link AbstractProcessor#process(Set, RoundEnvironment)} for processing
	 * the elements given by the parameter roundEnv. */
	@Override
	public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
		if (!roundEnv.processingOver()) {
   			for (final Element element : roundEnv.getRootElements()) {
   				System.out.println("  -> Extraction of: " + element.toString());
   				final TreePath path = trees.getPath(element);
            	extractJavaDocFromElement(trees.getDocComment(path));
            }
		}
		return true;
	}

    private void extractJavaDocFromElement(final String docComment) {
        // Hier DocComment prozessieren
    	System.out.println("docComment: " + docComment);
    }

}
