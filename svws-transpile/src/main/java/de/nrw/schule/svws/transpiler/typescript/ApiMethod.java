package de.nrw.schule.svws.transpiler.typescript;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree.Kind;

import de.nrw.schule.svws.transpiler.Transpiler;
import de.nrw.schule.svws.transpiler.TranspilerException;

/**
 * Diese Klasse analysiert den Java-Code einer OpenAPI-Methode und stellt die Informationen 
 * für den Transpiler zur Verfügung.
 */
public class ApiMethod {
	
	/** Der Name der API, dem diese API-Methode zugeordnet ist */
	public final String api; 
	
	/** Der Name der API-Methode */
	public final String name;
	
	/** Die URL auf welche die API-Methode angewendet wird */
	public final String path;
	
	/** Der Typ der verwendeten HTTP-Methode */
	public final ApiHttpMethod httpMethod;

	/** Der Mime-Type, welcher für das Ergebnis der API-Method verwendet wird.*/
	public final ApiMimeType produces;
	
	/** Der Mime-Type, welcher für den Input der API-Methode verwendet wird.*/
	public final ApiMimeType consumes;
	
	/** Dokumentation der Methode: Eine kurze Zusammenfassung */
	public final String docSummary;
	
	/** Dokumentation der Methode: Eine ausführlichere Beschreibung */
	public final String docDescription;
	
	/** Die unterschiedlichen HTTP-Responses, welche beim Aufruf der Methode auftreten können und dokumentiert wurden. */
	public final Vector<ApiResponse> responses = new Vector<>();
	
	/** Die HTTP-Response, welche im Erfolgsfall verwendet wird. */
	public final ApiResponse returnResponse;
	
	/** Der Request-Body der API-Methode */
	public final ApiRequestBody requestBody;
	
	/** Die Pfad-Parameter dieser API-Methode */
	public final ApiPathParams pathParams;
	
	
	private ApiMethod(Transpiler transpiler, ApiClassAnnotations classAnnotations, ClassTree classTree, MethodTree method) {
		api = classAnnotations.tag;
		name = method.getName().toString();
		String tmpPathClass = classAnnotations.path;
		String tmpPathMethod = ApiMethod.getPath(transpiler, method);
		if ((!tmpPathClass.endsWith("/")) && (!tmpPathMethod.startsWith("/")))
			tmpPathClass += "/";
		path = tmpPathClass + tmpPathMethod;
		httpMethod = ApiHttpMethod.get(transpiler, method);
		docSummary = ApiMethod.getDocSummary(transpiler, classTree, method);
		docDescription = ApiMethod.getDocDescription(transpiler, classTree, method);
		
		// Response-Objekt erstellen und einen Vektor dieser Response-Objekt hier erzeugen, außerdem Erkennung des Response-Codes für eine erfolgreiche Rückgabe von Informationen 
		Vector<AnnotationTree> annotationMethodApiResponses = transpiler.getAnnotationList("io.swagger.v3.oas.annotations.responses.ApiResponse", method);
		ApiResponse tmp200 = null;
		for (AnnotationTree annotationMethodApiResponse : annotationMethodApiResponses) {
			ApiResponse response = new ApiResponse(transpiler, annotationMethodApiResponse);
			responses.add(response);
			int responseCode = response.responseCode;
			if ((responseCode >= 200) && (responseCode < 300))
				tmp200 = response;
		}
		responses.sort((a, b) -> Integer.compare(a.responseCode, b.responseCode));
		if (tmp200 == null)
			throw new TranspilerException("Transpiler Error: Missing http response code annotation for method " + name + " of class " + classTree.getSimpleName().toString());
		returnResponse = tmp200;
		ApiMimeType tmpProduces = ApiMethod.getMimeType(transpiler, method, "jakarta.ws.rs.Produces");
		if (tmpProduces == null) // nehme den Standard der Java-API-Klasse
			this.produces = classAnnotations.produces;
		else
			this.produces = tmpProduces;
		ApiMimeType tmpConsumes = ApiMethod.getMimeType(transpiler, method, "jakarta.ws.rs.Consumes");
		if (tmpConsumes == null) // nehme den Standard der Java-API-Klasse
			this.consumes = classAnnotations.consumes;
		else
			this.consumes = tmpConsumes;

		this.requestBody = new ApiRequestBody(transpiler, method);
		this.pathParams = new ApiPathParams(transpiler, method);
	}
	
	/**
	 * Erstellt ein neues Objekt dieser Klasse durch Analyse der übergebenen Java-Methode.
	 * 
	 * @param transpiler         der zu verwendende Transpiler
	 * @param classAnnotations   die Informationen zu den Annotationen der API-Klasse 
	 * @param classTree          die Java-API-Klasse, in welcher die API-Methode implementiert wurde
	 * @param method             die Java-API-Methode
	 * 
	 * @return das neue Objekt
	 */
	public static ApiMethod getMethod(Transpiler transpiler, ApiClassAnnotations classAnnotations, ClassTree classTree, MethodTree method) {
		// Only methods with a Path annotation are api methods
		String path = ApiMethod.getPath(transpiler, method);
		if (path == null)
			return null;
		return new ApiMethod(transpiler, classAnnotations, classTree, method);
	}
	
	private static String getPath(Transpiler transpiler, MethodTree methodTree) {
		AnnotationTree annotationPath = transpiler.getAnnotation("jakarta.ws.rs.Path", methodTree);
		if (annotationPath == null)
			return null;
		Map<String, ExpressionTree> args = transpiler.getArguments(annotationPath);
		ExpressionTree value = args.get("value");
		if ((value.getKind() == Kind.STRING_LITERAL) && (value instanceof LiteralTree literal) && (literal.getValue() instanceof String path))
			return path;
		throw new TranspilerException("Transpiler Exception: Unhandled value argument for Path annotation.");		
	}
	
	private static String getDocSummary(Transpiler transpiler, ClassTree classTree, MethodTree method) {
		AnnotationTree annotationMethodOperation = transpiler.getAnnotation("io.swagger.v3.oas.annotations.Operation", method);
		if (annotationMethodOperation == null)
			throw new TranspilerException("Transpiler Exception: Missing Operation annotation on method " + method.getName() + " in class " + classTree.getSimpleName().toString() + ".");		
		Map<String, ExpressionTree> args = transpiler.getArguments(annotationMethodOperation);
		ExpressionTree value = args.get("summary");
        if(value == null) {
            throw new TranspilerException("Transpiler Exception: Missing summary value for @Operation annotation on method " + method.getName() + " in class " + classTree.getSimpleName().toString() + ".");
        }
		if ((value.getKind() == Kind.STRING_LITERAL) && (value instanceof LiteralTree literal) && (literal.getValue() instanceof String summary))
			return summary;
		throw new TranspilerException("Transpiler Exception: Unhandled value argument for Path annotation.");		
	}

	private static String getDocDescription(Transpiler transpiler, ClassTree classTree, MethodTree method) {
		AnnotationTree annotationMethodOperation = transpiler.getAnnotation("io.swagger.v3.oas.annotations.Operation", method);
		if (annotationMethodOperation == null)
			throw new TranspilerException("Transpiler Exception: Missing Operation annotation on method " + method.getName() + " in class " + classTree.getSimpleName().toString() + ".");		
		Map<String, ExpressionTree> args = transpiler.getArguments(annotationMethodOperation);
		ExpressionTree value = args.get("description");
        if(value == null) {
            throw new TranspilerException("Transpiler Exception: Missing description value for @Operation annotation on method " + method.getName() + " in class " + classTree.getSimpleName().toString() + ".");
        }
		if ((value.getKind() == Kind.STRING_LITERAL) && (value instanceof LiteralTree literal) && (literal.getValue() instanceof String description))
			return description;
		throw new TranspilerException("Transpiler Exception: Unhandled value argument for Path annotation.");		
	}

	/**
	 * Bestimmt den Mime-Type, welche durch die angebenen Annotation spezifiziert wurde.
	 * 
	 * @param transpiler   der für die Analyse zu verwendende Transpiler
	 * @param methodTree   die Java-API-Methode
	 * @param annotation   die Annotation 
	 * 
	 * @return der Mime-Type oder null, falls ie Annotation keinen Mime-Type spezifiziert
	 */
	private static ApiMimeType getMimeType(Transpiler transpiler, MethodTree methodTree, String annotation) {
		AnnotationTree annotationTree = transpiler.getAnnotation(annotation, methodTree);
		if (annotationTree == null)
			return null;
		Map<String, ExpressionTree> args = transpiler.getArguments(annotationTree);
		ExpressionTree value = args.get("value");
		if ((value.getKind() == Kind.STRING_LITERAL) && (value instanceof LiteralTree literal) && (literal.getValue() instanceof String str))
			return ApiMimeType.get(str);
		if (value.getKind() == Kind.MEMBER_SELECT)
			return ApiMimeType.get(value.toString());
		throw new TranspilerException("Transpiler Exception: Unhandled value argument for Consumes annotation of kind " + value.getKind() + ".");
	}
	

	/**
	 * Gibt die von dieser Klasse benötigten Klassen-Imports zurück.
	 * 
	 * @return eine Map mit den benötigten Imports, d.h. Paare von 
	 *         Klassennamen und der Paketnamen
	 */
	public HashMap<String, String> getImportsRequired() {
		HashMap<String, String> imports = new HashMap<>();
		// Ergänze die Typen von den möglichen HTTP-Responses
		for (ApiResponse response : responses) {
			if (response.content != null) {
				for (Map.Entry<String, String> entry : response.content.importsRequired.entrySet()) {
					String value = imports.get(entry.getKey());
					if (value != null) {
						if (!value.equals(entry.getValue()))
							throw new TranspilerException("Transpiler Error: Transpiler cannot handle classes in the API with the same name in different packages.");
						continue;
					}
					imports.put(entry.getKey(), entry.getValue());
				}
			}
		}
		// Ergänze ggf. die Typen des Request-Body
		if (requestBody.exists && (requestBody.content != null)) {
			for (Map.Entry<String, String> entry : requestBody.content.importsRequired.entrySet()) {
				String value = imports.get(entry.getKey());
				if (value != null) {
					if (!value.equals(entry.getValue()))
						throw new TranspilerException("Transpiler Error: Transpiler cannot handle classes in the API with the same name in different packages.");
					continue;
				}
				imports.put(entry.getKey(), entry.getValue());
			}			
		}
		return imports;
	}


	/**
	 * Erstellt zu dieser Methode den JSDoc-Kommentar
	 * 
	 * @return der JSDoc-Kommentar
	 */
	public String getJSDoc() {
		String result = "\t/**" + System.lineSeparator();
		result += "\t * Implementierung der " + httpMethod + "-Methode " + name + " für den Zugriff auf die URL https://{hostname}" + path + System.lineSeparator();
		result += "\t * " + System.lineSeparator();
		result += "\t * " + docDescription + System.lineSeparator();
		result += "\t * " + System.lineSeparator();
		result += "\t * Mögliche HTTP-Antworten: " + System.lineSeparator();
		for (ApiResponse response : responses) {
			result += "\t *   Code " + response.responseCode + ": " + response.description + System.lineSeparator();
			if (response.content != null) {
				result += "\t *     - Mime-Type: " + response.content.mimetype + System.lineSeparator(); 
				result += "\t *     - Rückgabe-Typ: " + response.content.datatype + System.lineSeparator();
			}
		}
		boolean isFirstParam = true;
		if (requestBody.exists && (requestBody.content != null)) {
			if (isFirstParam) {
				result += "\t * " + System.lineSeparator();
				isFirstParam = false;
			}
			result += "\t * @param {" + requestBody.content.datatype + "} data - der Request-Body für die HTTP-Methode" + System.lineSeparator();
		}
		for (Map.Entry<String, String> pathParam : this.pathParams.params) {
			if (isFirstParam) {
				result += "\t * " + System.lineSeparator();
				isFirstParam = false;
			}
			result += "\t * @param {" + pathParam.getValue() + "} " + pathParam.getKey() + " - der Pfad-Parameter " + pathParam.getKey() + System.lineSeparator();
		}
		if ((returnResponse != null) && (returnResponse.content != null)) {
			result += "\t * " + System.lineSeparator();
			result += "\t * @returns " + returnResponse.description;
			result += System.lineSeparator();
		}
		return result + "\t */" + System.lineSeparator();		
	}
	
	/**
	 * Gibt den Return-Typ für diese Methode zurück. Dies ist immer eine 
	 * Promise.
	 *  
	 * @return ein String mit dem Return-Typ 
	 */
	public String getReturnType() {
		if ((returnResponse != null) && (returnResponse.content != null)) {
			String datatype = (returnResponse.content.isArrayType) ? returnResponse.content.arrayElementType : returnResponse.content.datatype;
			datatype = switch (datatype) {
				case "Byte", "Short", "Integer", "Long", "Float", "Double" -> "Number";
				case "Character" -> "String";
				default -> datatype;
			};
			if (returnResponse.content.isArrayType)
				return "Promise<List<" + datatype + ">>";
			return "Promise<" + datatype + ">";
		}
		return "Promise<void>";
	}
	
	/**
	 * Gibt den Typen des Request-Body zurück. Wurde kein Typ spezifiziert,
	 * so wird null zurückgegeben.
	 * 
	 * @return der Typ des Request-Body
	 */
	public String getRequestBodyType() {
		if ((!requestBody.exists) || (requestBody.content == null))
			return null;
		return requestBody.content.datatype;
	}
	
	
	private static String getTSType(ApiContent content) {
		return switch (content.datatype) {
			case "Byte", "Short", "Integer", "Long", "Float", "Double" -> "Number";
			case "Character", "String" -> "String";
			default -> content.datatype;
		};			
	}
	
	
	private String getTSMethodHead() {
		String result = "\tpublic async " + this.name + "(";
		boolean notFirstParam = false;
		if (requestBody.exists && (requestBody.content != null)) {
			result += "data : ";
			String tstype = getTSType(requestBody.content);			
			if (httpMethod == ApiHttpMethod.PATCH)
				result += "Partial<" + tstype + ">";
			else 
				result += tstype;
			notFirstParam = true;
		}
		for (Map.Entry<String, String> pathParam : this.pathParams.params) {
			if (notFirstParam)
				result += ", ";
			else
				notFirstParam = true;
			result += pathParam.getKey() + " : " + pathParam.getValue();
		}
		result += ") : " + this.getReturnType();
		return result;
	}


	private String getTSMethodBody() {
		String result = "{" + System.lineSeparator();
		// Erstelle die URL für den HTTP-Request
		if (pathParams.params.size() == 0) {
			result += "\t\tlet path : string = \"" + path + "\";" + System.lineSeparator();
		} else {
			result += "\t\tlet path : string = \"" + path + "\"" + System.lineSeparator();
			for (int i = 0; i < this.pathParams.params.size(); i++) {
				Map.Entry<String, String> pathParam = this.pathParams.params.get(i);
				String replaceParam = pathParam.getKey();
				if ("number".equals(pathParam.getValue()))
					replaceParam += ".toString()";
				result += "\t\t\t\t.replace(/{" + pathParam.getKey() + "\\s*(:[^}]+)?}/g, " + replaceParam + ")";
				if (i == this.pathParams.params.size() - 1)
					result += ";";
				result += System.lineSeparator();
			}
		}
		// Code für die Handhabung des Request-Bodys
		if (requestBody.exists && (requestBody.content != null)) {
			// Prüfe, ob der Request-Body in JSON umgewandelt werden muss
			if (requestBody.content.mimetype == ApiMimeType.APPLICATION_JSON) {
				if (requestBody.content.isArrayType) {
					if (httpMethod == ApiHttpMethod.PATCH)
						throw new TranspilerException("Transpiler Error: Patch Methods are currently not supported for array based json objects (method: " + name + " in API " + api + ")");
                    result += "\t\tlet body : string = \"[\" + data.toArray().map(d => { Object.transpilerToJSON(d) }).join() + \"]\";" + System.lineSeparator();
				} else {
					String tsType = getTSType(requestBody.content); 
					if ("String".equals(tsType) || "Number".equals(tsType) || "Boolean".equals(tsType)) {
						result += "\t\tlet body : string = JSON.stringify(data);" + System.lineSeparator();
					} else {
						if (httpMethod == ApiHttpMethod.PATCH) {
							result += "\t\tlet body : string = " + requestBody.content.datatype + ".transpilerToJSONPatch(data);" + System.lineSeparator();
						} else {
							result += "\t\tlet body : string = " + requestBody.content.datatype + ".transpilerToJSON(data);" + System.lineSeparator();
						}
					}
				}
			}
		}
		// fetch-Code
		if (this.httpMethod == ApiHttpMethod.POST) {
			if ((this.produces == ApiMimeType.APPLICATION_JSON) && (this.consumes == ApiMimeType.APPLICATION_JSON)) {
				if (returnResponse.content == null) {
					result += "\t\tawait super.postJSON(path, " + (requestBody.exists ? "body" : null) + ");" + System.lineSeparator();
					result += "\t\treturn;" + System.lineSeparator();
				} else {
					result += "\t\tconst result : string = await super.postJSON(path, " + (requestBody.exists ? "body" : null) + ");" + System.lineSeparator();
					String datatype = (returnResponse.content.isArrayType) ? returnResponse.content.arrayElementType : returnResponse.content.datatype;
					String conversion = switch (datatype) {
						case "Long", "Float", "Double" -> "parseFloat(JSON.parse(text))";
						case "Byte", "Short", "Integer" -> "parseInt(JSON.parse(text))";
						case "Character", "String" -> "JSON.parse(text).toString()";
						case "Boolean" -> "(text === \"true\")";
						default -> datatype + ".transpilerFromJSON(text)";
					};
					String resultDatatype = switch (datatype) {
						case "Byte", "Short", "Integer", "Long", "Float", "Double" -> "Number";
						case "Character", "String" -> "String";
						default -> datatype;
					};
					if (returnResponse.content.isArrayType) {
						result += "\t\tconst obj = JSON.parse(result);" + System.lineSeparator();
						result += "\t\tlet ret = new Vector<" + resultDatatype + ">();" + System.lineSeparator();
						result += "\t\tobj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(" + conversion + "); });" + System.lineSeparator();
						result += "\t\treturn ret;" + System.lineSeparator();
					} else {
						result += "\t\tconst text = result;" + System.lineSeparator();
						result += "\t\treturn " + conversion + ";" + System.lineSeparator();
					}
				}
			} else if ((this.produces == ApiMimeType.APPLICATION_JSON) && (this.consumes == ApiMimeType.MULTIPART_FORM_DATA)) {
				result += "\t\tconst result : string = await super.postMultipart(path, " + (requestBody.exists ? "data" : null) + ");" + System.lineSeparator();
				String datatype = (returnResponse.content.isArrayType) ? returnResponse.content.arrayElementType : returnResponse.content.datatype;
				String conversion = switch (datatype) {
					case "Long", "Float", "Double" -> "parseFloat(JSON.parse(text))";
					case "Byte", "Short", "Integer" -> "parseInt(JSON.parse(text))";
					case "Character", "String" -> "JSON.parse(text).toString()";
					case "Boolean" -> "(text === \"true\")";
					default -> datatype + ".transpilerFromJSON(text)";
				};
				String resultDatatype = switch (datatype) {
					case "Byte", "Short", "Integer", "Long", "Float", "Double" -> "Number";
					case "Character", "String" -> "String";
					default -> datatype;
				};
				if (returnResponse.content.isArrayType) {
					result += "\t\tconst obj = JSON.parse(result);" + System.lineSeparator();
					result += "\t\tlet ret = new Vector<" + resultDatatype + ">();" + System.lineSeparator();
					result += "\t\tobj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(" + conversion + "); });" + System.lineSeparator();
					result += "\t\treturn ret;" + System.lineSeparator();
				} else {
					result += "\t\tconst text = result;" + System.lineSeparator();
					result += "\t\treturn " + conversion + ";" + System.lineSeparator();
				}
			} else
				throw new TranspilerException("Transpiler Error: POST which produces " + this.produces + " and consumes " + this.consumes + " not yet implemented in the transpiler.");
		} else if (this.httpMethod == ApiHttpMethod.GET) {
			if (this.produces == ApiMimeType.APPLICATION_JSON) {
				result += "\t\tconst result : string = await super.getJSON(path);" + System.lineSeparator();
				String datatype = (returnResponse.content.isArrayType) ? returnResponse.content.arrayElementType : returnResponse.content.datatype;
				String conversion = switch (datatype) {
					case "Long", "Float", "Double" -> "parseFloat(JSON.parse(text))";
					case "Byte", "Short", "Integer" -> "parseInt(JSON.parse(text))";
					case "Character", "String" -> "JSON.parse(text).toString()";
					case "Boolean" -> "(text === \"true\")";
					default -> datatype + ".transpilerFromJSON(text)";
				};
				String resultDatatype = switch (datatype) {
					case "Byte", "Short", "Integer", "Long", "Float", "Double" -> "Number";
					case "Character", "String" -> "String";
					default -> datatype;
				};
				if (returnResponse.content.isArrayType) {
					result += "\t\tconst obj = JSON.parse(result);" + System.lineSeparator();
					result += "\t\tlet ret = new Vector<" + resultDatatype + ">();" + System.lineSeparator();
					result += "\t\tobj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(" + conversion + "); });" + System.lineSeparator();
					result += "\t\treturn ret;" + System.lineSeparator();
				} else {
					result += "\t\tconst text = result;" + System.lineSeparator();
					result += "\t\treturn " + conversion + ";" + System.lineSeparator();
				}
			} else if (this.produces == ApiMimeType.TEXT_PLAIN) {
				result += "\t\tconst text : string = await super.getText(path);" + System.lineSeparator();
				result += "\t\treturn String(text);" + System.lineSeparator();
			} else if (this.produces == ApiMimeType.PDF) {
				result += "\t\tconst data : Blob = await super.getPDF(path);" + System.lineSeparator();
				result += "\t\treturn data;" + System.lineSeparator();
			} else if (this.produces == ApiMimeType.APPLICATION_OCTET_STREAM) {
				result += "\t\tconst data : Blob = await super.getOctetStream(path);" + System.lineSeparator();
				result += "\t\treturn data;" + System.lineSeparator();
			} else if (this.produces == ApiMimeType.SQLITE) {
				result += "\t\tconst data : Blob = await super.getSQLite(path);" + System.lineSeparator();
				result += "\t\treturn data;" + System.lineSeparator();
			} else
				throw new TranspilerException("Transpiler Error: GET which produces " + this.produces + " not yet implemented in the transpiler.");
		} else if (this.httpMethod == ApiHttpMethod.PATCH) {
			if (this.consumes == ApiMimeType.APPLICATION_JSON) {
				result += "\t\treturn super.patchJSON(path, body);" + System.lineSeparator();
			} else if (this.consumes == ApiMimeType.TEXT_PLAIN) {
				result += "\t\treturn super.patchText(path, body);" + System.lineSeparator();
			} else 
				throw new TranspilerException("Transpiler Error: PATCH which consumes " + this.consumes + " not yet implemented in the transpiler.");
		} else if (this.httpMethod == ApiHttpMethod.PUT) {
			if (this.consumes == ApiMimeType.APPLICATION_JSON) {
				result += "\t\treturn super.putJSON(path, body);" + System.lineSeparator();
			} else if (this.consumes == ApiMimeType.TEXT_PLAIN) {
				result += "\t\treturn super.putText(path, body);" + System.lineSeparator();
			} else 
				throw new TranspilerException("Transpiler Error: PUT which consumes " + this.consumes + " not yet implemented in the transpiler.");
		} else if (this.httpMethod == ApiHttpMethod.DELETE) {
			if ((this.produces == ApiMimeType.APPLICATION_JSON) && (this.consumes == ApiMimeType.APPLICATION_JSON)) {
				if (returnResponse.content == null) {
					result += "\t\tawait super.deleteJSON(path, " + (requestBody.exists ? "body" : null) + ");" + System.lineSeparator();
					result += "\t\treturn;" + System.lineSeparator();
				} else {
					result += "\t\tconst result : string = await super.deleteJSON(path, " + (requestBody.exists ? "body" : null) + ");" + System.lineSeparator();
					String datatype = (returnResponse.content.isArrayType) ? returnResponse.content.arrayElementType : returnResponse.content.datatype;
					String conversion = switch (datatype) {
						case "Long", "Float", "Double" -> "parseFloat(JSON.parse(text))";
						case "Byte", "Short", "Integer" -> "parseInt(JSON.parse(text))";
						case "Character", "String" -> "JSON.parse(text).toString()";
						case "Boolean" -> "(text === \"true\")";
						default -> datatype + ".transpilerFromJSON(text)";
					};
					String resultDatatype = switch (datatype) {
						case "Byte", "Short", "Integer", "Long", "Float", "Double" -> "Number";
						case "Character", "String" -> "String";
                        case "Boolean" -> "Boolean";
						default -> datatype;
					};
					if (returnResponse.content.isArrayType) {
						result += "\t\tconst obj = JSON.parse(result);" + System.lineSeparator();
						result += "\t\tlet ret = new Vector<" + resultDatatype + ">();" + System.lineSeparator();
						result += "\t\tobj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(" + conversion + "); });" + System.lineSeparator();
						result += "\t\treturn ret;" + System.lineSeparator();
					} else {
						result += "\t\tconst text = result;" + System.lineSeparator();
						result += "\t\treturn " + conversion + ";" + System.lineSeparator();
					}
				}
			} else if (this.consumes == ApiMimeType.TEXT_PLAIN) {
				if (returnResponse.content == null) {
					result += "\t\treturn super.deleteText(path, " + (requestBody.exists ? "body" : null) + ");" + System.lineSeparator();
				} else {
					throw new TranspilerException("Transpiler Error: POST which produces " + this.produces + " and consumes " + this.consumes + " not yet implemented in the transpiler.");
				}
			} else 
				throw new TranspilerException("Transpiler Error: DELETE which consumes " + this.consumes + " not yet implemented in the transpiler.");
		} else {
			throw new TranspilerException("Transpiler Error: HTTP-Methode " + this.httpMethod + " wird noch nicht vom Transpiler unterstützt.");
		}
		result += "\t}" + System.lineSeparator();
		result += System.lineSeparator();
		return result;
	}

	/**
	 * Gibt den Type-Script-Code für diese API-Methode zurück.
	 * 
	 * @return der Type-Script-Code dieser Methode
	 */
	public String getTSMethod() {
		return getJSDoc() + getTSMethodHead() + " " + getTSMethodBody() + System.lineSeparator();
	}


	/**
	 * Prüft, ob die API-Methode von dem Transpiler transpiliert werden kann.
	 * 
	 * @return true, falls die Methode vermutlich transpiliert werden kann und false,
	 *         falls bekannte Gründe dagegen sprechen (siehe Implementierung)
	 */
	public boolean isTranspilable() {
	    if (requestBody.exists && (requestBody.content != null) && (requestBody.content.mimetype == ApiMimeType.APPLICATION_JSON)) {
	        String datatype = (requestBody.content.isArrayType) ? requestBody.content.arrayElementType : requestBody.content.datatype;
	        if ("Object".equals(datatype))
	            return false;
        }
        if ((returnResponse.content != null) && (this.produces == ApiMimeType.APPLICATION_JSON)) {
            String datatype = (returnResponse.content.isArrayType) ? returnResponse.content.arrayElementType : returnResponse.content.datatype;
            if ("Object".equals(datatype))
                return false;
        }
        return true;
	}

}
