package de.svws_nrw.transpiler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * This class represents the content of a resource file that is associated with
 * a java class and that is used by a language plugin.
 */
public class TranspilerResource {

	/** the package name of the associated class */
	public final String packageName;
	
	/** the name of the associated class */
	public final String className;
	
	/** the files extension of the resource file */
	public final String extension;
	
	/** the path of the resource file */
	public final Path path;
	
	/** the content of the resource file as an UTF-8 string */
	public final String data;
	
	
	/**
	 * Create a new transpiler resource object and reads the file content.
	 * 
	 * @param packageName    the package name of the associated class
	 * @param className      the name of the associated class
	 * @param extension      the files extension of the resource file
	 * @param path           the path of the resource file
	 * 
	 * @throws IOException   this exception is thrown if the resource file data cannot be read
	 */
	public TranspilerResource(final String packageName, final String className, final String extension, final Path path) throws IOException {
		this.packageName = packageName;
		this.className = className;
		this.extension = extension;
		this.path = path;
		this.data = Files.readString(path);
	}
	
}
