package de.svws_nrw.transpiler.typescript;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree.Kind;

import de.svws_nrw.transpiler.Transpiler;
import de.svws_nrw.transpiler.TranspilerException;

/**
 * Diese Klasse analysiert den Java-Code einer OpenAPI-Methode und stellt die Informationen
 * für den Transpiler zur Verfügung.
 */
public final class ApiMethod {

	/** Der Name der API, dem diese API-Methode zugeordnet ist */
	public final String api;

	/** Der Name der API-Methode */
	public final String name;

	/** Die URL auf welche die API-Methode angewendet wird */
	public final String path;

	/** Der Typ der verwendeten HTTP-Methode */
	public final ApiHttpMethod httpMethod;

	/** Der erste Mime-Type, welcher für das Ergebnis der API-Methode bei dem Transpiler verwendet wird.*/
	public final ApiMimeType producesFirst;

	/** Die Mime-Types, welche für die Ergebnisse der API-Methode verwendet werden können.*/
	public final List<ApiMimeType> produces;

	/** Der erste Mime-Type, welcher für den Input der API-Methode bei dem Transpiler verwendet wird.*/
	public final ApiMimeType consumesFirst;

	/** Die Mime-Types, welche für den Input der API-Methode verwendet werden können.*/
	public final List<ApiMimeType> consumes;

	/** Dokumentation der Methode: Eine kurze Zusammenfassung */
	public final String docSummary;

	/** Dokumentation der Methode: Eine ausführlichere Beschreibung */
	public final String docDescription;

	/** Die unterschiedlichen HTTP-Responses, welche beim Aufruf der Methode auftreten können und dokumentiert wurden. */
	public final List<ApiResponse> responses = new ArrayList<>();

	/** Die HTTP-Response, welche im Erfolgsfall verwendet wird. */
	public final ApiResponse returnResponse;

	/** Der Request-Body der API-Methode */
	public final ApiRequestBody requestBody;

	/** Die Pfad-Parameter dieser API-Methode */
	public final ApiPathParams pathParams;


	private static final String strExceptionUnhandledPathAnnotation = "Transpiler Exception: Unhandled value argument for Path annotation.";


	private ApiMethod(final Transpiler transpiler, final ApiClassAnnotations classAnnotations, final ClassTree classTree, final MethodTree method) {
		api = classAnnotations.tag;
		name = method.getName().toString();
		String tmpPathClass = classAnnotations.path;
		final String tmpPathMethod = ApiMethod.getPath(transpiler, method);
		if (tmpPathMethod == null)
			throw new TranspilerException("Null not expected here");
		if ((!tmpPathClass.endsWith("/")) && (!tmpPathMethod.startsWith("/")))
			tmpPathClass += "/";
		path = tmpPathClass + tmpPathMethod;
		httpMethod = ApiHttpMethod.get(transpiler, method);
		docSummary = ApiMethod.getDocSummary(transpiler, classTree, method);
		docDescription = ApiMethod.getDocDescription(transpiler, classTree, method);

		// Response-Objekt erstellen und einen Vektor dieser Response-Objekt hier erzeugen, außerdem Erkennung des Response-Codes für eine erfolgreiche Rückgabe von Informationen
		final List<AnnotationTree> annotationMethodApiResponses = transpiler.getAnnotationList("io.swagger.v3.oas.annotations.responses.ApiResponse", method);
		ApiResponse tmp200 = null;
		for (final AnnotationTree annotationMethodApiResponse : annotationMethodApiResponses) {
			final ApiResponse response = new ApiResponse(transpiler, annotationMethodApiResponse);
			responses.add(response);
			final int responseCode = response.responseCode;
			if ((responseCode >= 200) && (responseCode < 300))
				tmp200 = response;
		}
		responses.sort((a, b) -> Integer.compare(a.responseCode, b.responseCode));
		if (tmp200 == null)
			throw new TranspilerException("Transpiler Error: Missing http response code annotation for method " + name + " of class "
					+ classTree.getSimpleName().toString());
		returnResponse = tmp200;
		final List<ApiMimeType> tmpProduces = ApiMimeType.fromMethodTree(transpiler, method, "jakarta.ws.rs.Produces");
		if (tmpProduces.isEmpty()) // nehme den Standard der Java-API-Klasse
			this.produces = classAnnotations.produces;
		else
			this.produces = tmpProduces;
		this.producesFirst = this.produces.getFirst();
		final List<ApiMimeType> tmpConsumes = ApiMimeType.fromMethodTree(transpiler, method, "jakarta.ws.rs.Consumes");
		if (tmpConsumes.isEmpty()) // nehme den Standard der Java-API-Klasse
			this.consumes = classAnnotations.consumes;
		else
			this.consumes = tmpConsumes;
		this.consumesFirst = this.consumes.getFirst();

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
	public static ApiMethod getMethod(final Transpiler transpiler, final ApiClassAnnotations classAnnotations, final ClassTree classTree,
			final MethodTree method) {
		// Only methods with a Path annotation are api methods
		final String path = ApiMethod.getPath(transpiler, method);
		if (path == null)
			return null;
		return new ApiMethod(transpiler, classAnnotations, classTree, method);
	}

	private static String getPath(final Transpiler transpiler, final MethodTree methodTree) {
		final AnnotationTree annotationPath = transpiler.getAnnotation("jakarta.ws.rs.Path", methodTree);
		if (annotationPath == null)
			return null;
		final Map<String, ExpressionTree> args = Transpiler.getArguments(annotationPath);
		final ExpressionTree value = args.get("value");
		if ((value.getKind() == Kind.STRING_LITERAL) && (value instanceof final LiteralTree literal) && (literal.getValue() instanceof final String path))
			return path;
		throw new TranspilerException(strExceptionUnhandledPathAnnotation);
	}

	private static String getDocSummary(final Transpiler transpiler, final ClassTree classTree, final MethodTree method) {
		final AnnotationTree annotationMethodOperation = transpiler.getAnnotation("io.swagger.v3.oas.annotations.Operation", method);
		if (annotationMethodOperation == null)
			throw new TranspilerException("Transpiler Exception: Missing Operation annotation on method %s in class %s."
					.formatted(method.getName(), classTree.getSimpleName()));
		final Map<String, ExpressionTree> args = Transpiler.getArguments(annotationMethodOperation);
		final ExpressionTree value = args.get("summary");
		if (value == null) {
			throw new TranspilerException("Transpiler Exception: Missing summary value for @Operation annotation on method %s in class %s."
					.formatted(method.getName(), classTree.getSimpleName()));
		}
		if ((value.getKind() == Kind.STRING_LITERAL) && (value instanceof final LiteralTree literal) && (literal.getValue() instanceof final String summary))
			return summary;
		throw new TranspilerException(strExceptionUnhandledPathAnnotation);
	}

	private static String getDocDescription(final Transpiler transpiler, final ClassTree classTree, final MethodTree method) {
		final AnnotationTree annotationMethodOperation = transpiler.getAnnotation("io.swagger.v3.oas.annotations.Operation", method);
		if (annotationMethodOperation == null)
			throw new TranspilerException("Transpiler Exception: Missing Operation annotation on method %s in class %s."
					.formatted(method.getName(), classTree.getSimpleName()));
		final Map<String, ExpressionTree> args = Transpiler.getArguments(annotationMethodOperation);
		final ExpressionTree value = args.get("description");
		if (value == null)
			throw new TranspilerException("Transpiler Exception: Missing description value for @Operation annotation on method %s in class %s."
					.formatted(method.getName(), classTree.getSimpleName()));
		if ((value.getKind() == Kind.STRING_LITERAL) && (value instanceof final LiteralTree literal)
				&& (literal.getValue() instanceof final String description))
			return description;
		throw new TranspilerException(strExceptionUnhandledPathAnnotation);
	}

	/**
	 * Gibt die von dieser Klasse benötigten Klassen-Imports zurück.
	 *
	 * @return eine Map mit den benötigten Imports, d.h. Paare von
	 *         Klassennamen und der Paketnamen
	 */
	public Map<String, String> getImportsRequired() {
		final HashMap<String, String> imports = new HashMap<>();
		// Ergänze die Typen von den möglichen HTTP-Responses
		for (final ApiResponse response : responses) {
			if (response.content != null) {
				for (final Map.Entry<String, String> entry : response.content.importsRequired.entrySet()) {
					final String value = imports.get(entry.getKey());
					if (value != null) {
						if (!value.equals(entry.getValue()))
							throw new TranspilerException("Transpiler Error: Transpiler cannot handle classes in the API with the same name in"
									+ " different packages.");
						continue;
					}
					imports.put(entry.getKey(), entry.getValue());
				}
			}
		}
		// Ergänze ggf. die Typen des Request-Body
		if (requestBody.exists && (requestBody.content != null)) {
			for (final Map.Entry<String, String> entry : requestBody.content.importsRequired.entrySet()) {
				final String value = imports.get(entry.getKey());
				if (value != null) {
					if (!value.equals(entry.getValue()))
						throw new TranspilerException("Transpiler Error: Transpiler cannot handle classes in the API with the same name in"
								+ " different packages.");
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
		final StringBuilder sb = new StringBuilder();
		sb.append("\t/**" + System.lineSeparator());
		sb.append("\t * Implementierung der " + httpMethod + "-Methode " + name + " für den Zugriff auf die URL https://{hostname}" + path
				+ System.lineSeparator());
		sb.append("\t *" + System.lineSeparator());
		sb.append("\t * " + docDescription + System.lineSeparator());
		sb.append("\t *" + System.lineSeparator());
		sb.append("\t * Mögliche HTTP-Antworten:" + System.lineSeparator());
		for (final ApiResponse response : responses) {
			sb.append("\t *   Code " + response.responseCode + ": " + response.description + System.lineSeparator());
			if (response.content != null) {
				sb.append("\t *     - Mime-Type: " + response.content.mimetype + System.lineSeparator());
				sb.append("\t *     - Rückgabe-Typ: " + response.content.datatype + System.lineSeparator());
			}
		}
		boolean isFirstParam = true;
		final String requestBodyType = getRequestBodyType();
		if (requestBodyType != null) {
			isFirstParam = false;
			sb.append("\t *" + System.lineSeparator());
			sb.append("\t * @param {" + requestBodyType + "} data - der Request-Body für die HTTP-Methode" + System.lineSeparator());
		}
		for (final Map.Entry<String, String> pathParam : this.pathParams.params) {
			if (isFirstParam) {
				sb.append("\t *" + System.lineSeparator());
				isFirstParam = false;
			}
			sb.append("\t * @param {" + pathParam.getValue() + "} " + pathParam.getKey() + " - der Pfad-Parameter " + pathParam.getKey()
					+ System.lineSeparator());
		}
		if ((returnResponse != null) && (returnResponse.content != null)) {
			sb.append("\t *" + System.lineSeparator());
			sb.append("\t * @returns " + returnResponse.description);
			sb.append(System.lineSeparator());
		}
		sb.append("\t */" + System.lineSeparator());
		return sb.toString();
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
			if (returnResponse.content.isArrayType) {
				datatype = switch (datatype) {
					case "Byte", "Short", "Integer", "Long", "Float", "Double" -> "number";
					case "Character", "String" -> "string";
					case "Boolean" -> "boolean";
					default -> datatype;
				};
				return "Promise<List<" + datatype + ">>";
			}
			datatype = switch (datatype) {
				case "Byte", "Short", "Integer", "Long", "Float", "Double" -> "number | null";
				case "Character", "String" -> "string | null";
				case "Boolean" -> "boolean | null";
				default -> datatype;
			};
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
		final boolean isPartial = (httpMethod == ApiHttpMethod.PATCH) || ((httpMethod == ApiHttpMethod.POST) && (returnResponse.responseCode == 201));
		return getTSType(requestBody.content, isPartial);
	}


	private static String getTSType(final ApiContent content, final boolean isPartial) {
		String datatype = (content.isArrayType) ? content.arrayElementType : content.datatype;
		if (content.isArrayType) {
			datatype = switch (datatype) {
				case "Byte", "Short", "Integer", "Long", "Float", "Double" -> "number";
				case "Character", "String" -> "string";
				case "Boolean" -> "boolean";
				default -> datatype;
			};
			if (isPartial)
				datatype = "Partial<" + datatype + ">";
			return "List<" + datatype + ">";
		}
		datatype = switch (datatype) {
			case "Byte", "Short", "Integer", "Long", "Float", "Double" -> "number | null";
			case "Character", "String" -> "string | null";
			case "Boolean" -> "boolean | null";
			default -> datatype;
		};
		if (isPartial)
			datatype = "Partial<" + datatype + ">";
		return datatype;
	}


	private static String getTSArrayElementType(final ApiContent content) {
		if (!content.isArrayType)
			throw new TranspilerException("Transpiler Error: getTSArrayElementType invoked on non-array content.");
		return switch (content.arrayElementType) {
			case "Byte", "Short", "Integer", "Long", "Float", "Double" -> "number";
			case "Character", "String" -> "string";
			case "Boolean" -> "boolean";
			default -> content.arrayElementType;
		};
	}

	private static boolean isTSPrimitive(final String type) {
		return switch (type) {
			case "number", "string", "boolean", "number | null", "string | null", "boolean | null" -> true;
			default -> false;
		};
	}


	private String getTSMethodHead() {
		final StringBuilder sb = new StringBuilder();
		sb.append("\tpublic async " + this.name + "(");
		boolean notFirstParam = false;
		final String requestBodyType = getRequestBodyType();
		if (requestBodyType != null) {
			sb.append("data : " + requestBodyType);
			notFirstParam = true;
		}
		for (final Map.Entry<String, String> pathParam : this.pathParams.params) {
			if (notFirstParam)
				sb.append(", ");
			else
				notFirstParam = true;
			sb.append(pathParam.getKey() + " : " + pathParam.getValue());
		}
		sb.append(") : " + this.getReturnType());
		return sb.toString();
	}


	private void transpileRequestBody(final StringBuilder sb) {
		// Prüfe, ob überhaupt ein Request-Body vorliegt
		if (!requestBody.exists || (requestBody.content == null))
			return;
		// Prüfe, ob der Request-Body in JSON umgewandelt werden muss
		if (requestBody.content.mimetype == ApiMimeType.APPLICATION_JSON) {
			if (requestBody.content.isArrayType) {
				if (isTSPrimitive(getTSArrayElementType(requestBody.content))) {
					sb.append("\t\tconst body : string = \"[\" + (data.toArray() as Array<" + getTSArrayElementType(requestBody.content)
							+ ">).map(d => JSON.stringify(d)).join() + \"]\";" + System.lineSeparator());
				} else {
					if ((httpMethod == ApiHttpMethod.PATCH) || ((httpMethod == ApiHttpMethod.POST) && (returnResponse.responseCode == 201))) {
						sb.append("\t\tconst body : string = \"[\" + (data.toArray() as Array<" + requestBody.content.arrayElementType + ">).map(d => "
								+ requestBody.content.arrayElementType + ".transpilerToJSONPatch(d)).join() + \"]\";" + System.lineSeparator());
					} else {
						sb.append("\t\tconst body : string = \"[\" + (data.toArray() as Array<" + requestBody.content.arrayElementType + ">).map(d => "
								+ requestBody.content.arrayElementType + ".transpilerToJSON(d)).join() + \"]\";" + System.lineSeparator());
					}
				}
			} else {
				if (isTSPrimitive(getTSType(requestBody.content, false))) {
					sb.append("\t\tconst body : string = JSON.stringify(data);" + System.lineSeparator());
				} else {
					if ((httpMethod == ApiHttpMethod.PATCH) || ((httpMethod == ApiHttpMethod.POST) && (returnResponse.responseCode == 201))) {
						sb.append("\t\tconst body : string = " + requestBody.content.datatype + ".transpilerToJSONPatch(data);" + System.lineSeparator());
					} else {
						sb.append("\t\tconst body : string = " + requestBody.content.datatype + ".transpilerToJSON(data);" + System.lineSeparator());
					}
				}
			}
		}
	}


	private static String transpileResultDatatypeConversion(final String datatype) {
		return switch (datatype) {
			case "Long", "Float", "Double" -> "parseFloat(JSON.parse(text))";
			case "Byte", "Short", "Integer" -> "parseInt(JSON.parse(text))";
			case "Character", "String" -> "JSON.parse(text).toString()";
			case "Boolean" -> "(text === \"true\")";
			default -> datatype + ".transpilerFromJSON(text)";
		};
	}


	private static String transpileResultDatatype(final String datatype) {
		return switch (datatype) {
			case "Byte", "Short", "Integer", "Long", "Float", "Double" -> "number";
			case "Character", "String" -> "string";
			case "Boolean" -> "boolean";
			default -> datatype;
		};
	}


	private void transpileCodeForJsonResult(final StringBuilder sb) {
		final String datatype = (returnResponse.content.isArrayType) ? returnResponse.content.arrayElementType : returnResponse.content.datatype;
		final String conversion = transpileResultDatatypeConversion(datatype);
		final String resultDatatype = transpileResultDatatype(datatype);
		if (returnResponse.content.isArrayType) {
			sb.append("\t\tconst obj = JSON.parse(result);" + System.lineSeparator());
			sb.append("\t\tconst ret = new ArrayList<" + resultDatatype + ">();" + System.lineSeparator());
			sb.append("\t\tobj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(" + conversion + "); });"
					+ System.lineSeparator());
			sb.append("\t\treturn ret;" + System.lineSeparator());
		} else {
			sb.append("\t\tconst text = result;" + System.lineSeparator());
			sb.append("\t\treturn " + conversion + ";" + System.lineSeparator());
		}
	}


	private void transpileBodyForPostMethod(final StringBuilder sb) {
		if (returnResponse.content == null) {
			if (this.consumesFirst == ApiMimeType.APPLICATION_JSON) {
				sb.append("\t\tawait super.postJSON(path, " + (requestBody.exists ? "body" : null) + ");" + System.lineSeparator());
				sb.append("\t\treturn;" + System.lineSeparator());
				return;
			}
			if (this.consumesFirst == ApiMimeType.MULTIPART_FORM_DATA) {
				sb.append("\t\tawait super.postMultipart(path, " + (requestBody.exists ? "data" : null) + ");" + System.lineSeparator());
				sb.append("\t\treturn;" + System.lineSeparator());
				return;
			}
			throw new TranspilerException("Transpiler Error: POST which produces nothing and consumes " + this.consumesFirst
					+ " not yet implemented in the transpiler.");
		}
		final ApiMimeType producesType = (returnResponse.content == null) ? this.producesFirst : returnResponse.content.mimetype;
		if ((producesType == ApiMimeType.APPLICATION_JSON) && (this.consumesFirst == ApiMimeType.APPLICATION_JSON)) {
			sb.append("\t\tconst result : string = await super.postJSON(path, " + (requestBody.exists ? "body" : null) + ");" + System.lineSeparator());
			transpileCodeForJsonResult(sb);
			return;
		} else if ((producesType == ApiMimeType.PDF) && (this.consumesFirst == ApiMimeType.APPLICATION_JSON)) {
			sb.append("\t\tconst result : ApiFile = await super.postJSONtoPDF(path, " + (requestBody.exists ? "body" : null) + ");" + System.lineSeparator());
			sb.append("\t\treturn result;" + System.lineSeparator());
			return;
		} else if ((producesType == ApiMimeType.ZIP) && (this.consumesFirst == ApiMimeType.APPLICATION_JSON)) {
			sb.append("\t\tconst result : ApiFile = await super.postJSONtoZIP(path, " + (requestBody.exists ? "body" : null) + ");" + System.lineSeparator());
			sb.append("\t\treturn result;" + System.lineSeparator());
			return;
		} else if ((producesType == ApiMimeType.APPLICATION_OCTET_STREAM) && (this.consumesFirst == ApiMimeType.APPLICATION_JSON)) {
			sb.append("\t\tconst result : ApiFile = await super.postJSONtoOctetStream(path, " + (requestBody.exists ? "body" : null) + ");" + System.lineSeparator());
			sb.append("\t\treturn result;" + System.lineSeparator());
			return;
		} else if ((producesType == ApiMimeType.APPLICATION_JSON) && (this.consumesFirst == ApiMimeType.MULTIPART_FORM_DATA)) {
			sb.append("\t\tconst result : string = await super.postMultipart(path, " + (requestBody.exists ? "data" : null) + ");"
					+ System.lineSeparator());
			transpileCodeForJsonResult(sb);
			return;
		}
		throw new TranspilerException("Transpiler Error: POST which produces " + producesType + " and consumes " + this.consumesFirst
				+ " not yet implemented in the transpiler.");
	}


	private void transpileBodyForGetMethod(final StringBuilder sb) {
		final ApiMimeType producesType = (returnResponse.content == null) ? this.producesFirst : returnResponse.content.mimetype;
		switch (producesType) {
			case ApiMimeType.APPLICATION_JSON -> {
				sb.append("\t\tconst result : string = await super.getJSON(path);" + System.lineSeparator());
				transpileCodeForJsonResult(sb);
			}
			case ApiMimeType.TEXT_PLAIN -> {
				sb.append("\t\tconst text : string = await super.getText(path);" + System.lineSeparator());
				sb.append("\t\treturn text;" + System.lineSeparator());
			}
			case ApiMimeType.PDF -> {
				sb.append("\t\tconst data : ApiFile = await super.getPDF(path);" + System.lineSeparator());
				sb.append("\t\treturn data;" + System.lineSeparator());
			}
			case ApiMimeType.APPLICATION_OCTET_STREAM -> {
				sb.append("\t\tconst data : ApiFile = await super.getOctetStream(path);" + System.lineSeparator());
				sb.append("\t\treturn data;" + System.lineSeparator());
			}
			case ApiMimeType.SQLITE -> {
				sb.append("\t\tconst data : ApiFile = await super.getSQLite(path);" + System.lineSeparator());
				sb.append("\t\treturn data;" + System.lineSeparator());
			}
			default -> throw new TranspilerException("Transpiler Error: GET which produces " + producesType + " not yet implemented in the transpiler.");
		}
	}


	private void transpileBodyForPatchMethod(final StringBuilder sb) {
		switch (this.consumesFirst) {
			case ApiMimeType.APPLICATION_JSON ->
				sb.append("\t\treturn super.patchJSON(path, body);" + System.lineSeparator());
			case ApiMimeType.TEXT_PLAIN ->
				sb.append("\t\treturn super.patchText(path, body);" + System.lineSeparator());
			default ->
				throw new TranspilerException("Transpiler Error: PATCH which consumes " + this.consumesFirst + " not yet implemented in the transpiler.");
		}
	}


	private void transpileBodyForPutMethod(final StringBuilder sb) {
		switch (this.consumesFirst) {
			case ApiMimeType.APPLICATION_JSON ->
				sb.append("\t\treturn super.putJSON(path, body);" + System.lineSeparator());
			case ApiMimeType.TEXT_PLAIN ->
				sb.append("\t\treturn super.putText(path, body);" + System.lineSeparator());
			default ->
				throw new TranspilerException("Transpiler Error: PUT which consumes " + this.consumesFirst + " not yet implemented in the transpiler.");
		}
	}


	private void transpileBodyForDeleteMethod(final StringBuilder sb) {
		final ApiMimeType producesType = (returnResponse.content == null) ? this.producesFirst : returnResponse.content.mimetype;
		if ((producesType == ApiMimeType.APPLICATION_JSON) && (this.consumesFirst == ApiMimeType.APPLICATION_JSON)) {
			if (returnResponse.content == null) {
				sb.append("\t\tawait super.deleteJSON(path, " + (requestBody.exists ? "body" : null) + ");" + System.lineSeparator());
				sb.append("\t\treturn;" + System.lineSeparator());
			} else {
				sb.append("\t\tconst result : string = await super.deleteJSON(path, " + (requestBody.exists ? "body" : null) + ");"
						+ System.lineSeparator());
				transpileCodeForJsonResult(sb);
			}
		} else if (this.consumesFirst == ApiMimeType.TEXT_PLAIN) {
			if (returnResponse.content == null) {
				sb.append("\t\treturn super.deleteText(path, " + (requestBody.exists ? "body" : null) + ");" + System.lineSeparator());
			} else {
				throw new TranspilerException("Transpiler Error: POST which produces " + producesType + " and consumes " + this.consumesFirst
						+ " not yet implemented in the transpiler.");
			}
		} else
			throw new TranspilerException("Transpiler Error: DELETE which consumes " + this.consumesFirst + " not yet implemented in the transpiler.");
	}



	private String getTSMethodBody() {
		final StringBuilder sb = new StringBuilder();
		sb.append("{" + System.lineSeparator());
		// Erstelle die URL für den HTTP-Request, ...
		if (pathParams.params.isEmpty()) {
			sb.append("\t\tconst path = \"" + path.replace("\\", "\\\\") + "\";" + System.lineSeparator());
		} else {
			sb.append("\t\tconst path = \"" + path.replace("\\", "\\\\") + "\"" + System.lineSeparator());
			for (int i = 0; i < this.pathParams.params.size(); i++) {
				final Map.Entry<String, String> pathParam = this.pathParams.params.get(i);
				String replaceParam = pathParam.getKey();
				if ("number".equals(pathParam.getValue()))
					replaceParam += ".toString()";
				sb.append("\t\t\t.replace(/{" + pathParam.getKey() + "\\s*(:[^{}]+({[^{}]+})*)?}/g, " + replaceParam + ")");
				if (i == (this.pathParams.params.size() - 1))
					sb.append(";");
				sb.append(System.lineSeparator());
			}
		}
		// ... generiere ggf. Code für das Erzeugen des Request-Bodys, ...
		transpileRequestBody(sb);
		// ... den restlichen Code in Abhängigkeit der HTTP-Methode ...
		switch (this.httpMethod) {
			case POST -> transpileBodyForPostMethod(sb);
			case GET -> transpileBodyForGetMethod(sb);
			case PATCH -> transpileBodyForPatchMethod(sb);
			case PUT -> transpileBodyForPutMethod(sb);
			case DELETE -> transpileBodyForDeleteMethod(sb);
			default -> throw new TranspilerException("Transpiler Error: HTTP-Methode " + this.httpMethod + " wird noch nicht vom Transpiler unterstützt.");
		}
		// ... und schließe die Methode ab.
		sb.append("\t}" + System.lineSeparator());
		sb.append(System.lineSeparator());
		return sb.toString();
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
			final String datatype = (requestBody.content.isArrayType) ? requestBody.content.arrayElementType : requestBody.content.datatype;
			if ("Object".equals(datatype))
				return false;
		}
		if ((returnResponse.content != null) && (returnResponse.content.mimetype == ApiMimeType.APPLICATION_JSON)) {
			final String datatype = (returnResponse.content.isArrayType) ? returnResponse.content.arrayElementType : returnResponse.content.datatype;
			if ("Object".equals(datatype))
				return false;
		}
		return true;
	}

}
