package de.svws_nrw.transpiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.IntersectionType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.UnionType;

import jakarta.validation.constraints.NotNull;

/**
 * Eine Klasse mit statischen Hilfsmethoden für den Zugriff aus Element-Objekte.
 */
public final class ElementUtils {

	private ElementUtils() {
		throw new IllegalStateException("Instantiation not allowed");
	}

	/**
	 * Prüfe, ob das Element eine NotNull-Annotation hat oder nicht.
	 *
	 * @param e   das zu prüfende Element
	 *
	 * @return true, falls es eine NotNull-Annotation hat, und ansonsten false
	 */
	public static boolean isNotNull(final Element e) {
		return (e.getAnnotationsByType(NotNull.class).length != 0);
	}

	/**
	 * Prüfe, ob das Element einen static-Modifier hat oder nicht.
	 *
	 * @param e   das zu prüfende Element
	 *
	 * @return true, falls der Modifier gesetzt ist, und ansonsten false
	 */
	public static boolean isStatic(final Element e) {
		return e.getModifiers().contains(Modifier.STATIC);
	}


	/**
	 * Prüft, ob der Typ des Elements ein primitiver Datentyp ist.
	 *
	 * @param e   das zu prüfende Element
	 *
	 * @return true, falls es sich um einen primitiven Datentyp handelt, und ansonsten false
	 */
	public static boolean isPrimitiveType(final Element e) {
		return e.asType().getKind().isPrimitive();
	}


	/**
	 * Gibt den Namen der Variable zurück.
	 *
	 * @param ve   das VariableElement
	 *
	 * @return der Name
	 */
	public static String getName(final VariableElement ve) {
		return "" + ve.getSimpleName();
	}


	/**
	 * Gibt das TypeElement zum Typ der Variable zurück, sofern es sich nicht
	 * um einen primitiven Typ handelt.
	 *
	 * @param ve   das VariableElement
	 *
	 * @return das TypeElement
	 */
	public static TypeElement getTypeElement(final VariableElement ve) {
		return ((ve.asType() instanceof final DeclaredType dt) && (dt.asElement() instanceof final TypeElement te)) ? te : null;
	}


	/**
	 * Gibt das ArrayElement zu der Variable zurück.
	 *
	 * @param ve   das VariableElement
	 *
	 * @return das ArrayElement
	 */
	public static ArrayType getArrayType(final VariableElement ve) {
		return (ve.asType() instanceof final ArrayType at) ? at : null;
	}


	/**
	 * Gibt den Type der Komponente des Arrays einer Variable zurück.
	 *
	 * @param ve   das VariableElement
	 *
	 * @return der Komponenten-Typ des Arrays
	 */
	public static TypeMirror getArrayComponentType(final VariableElement ve) {
		TypeMirror type = getArrayType(ve);
		while (type instanceof final ArrayType at)
			type = at.getComponentType();
		return type;
	}


	/**
	 * Gibt das zugehörige TypeElement für eine Variable zurück, sofern
	 * diese einen Array-Typ hat, welcher kein primitiver Typ ist.
	 *
	 * @param ve   das VariableElement
	 *
	 * @return das TypeElement
	 */
	public static TypeElement getArrayComponentTypeElement(final VariableElement ve) {
		return getTypeElement(getArrayComponentType(ve));
	}


	/**
	 * Gibt das zugehörige TypeElement des Typs zurück, sofern
	 * es sich um einen DeclaredType handelt.
	 *
	 * @param type   der type
	 *
	 * @return das TypeElement
	 */
	public static TypeElement getTypeElement(final TypeMirror type) {
		return ((type != null) && (type instanceof final DeclaredType dt) && (dt.asElement() instanceof final TypeElement te)) ? te : null;
	}


	/**
	 * Gibt den einfachen Typ (ohne Package) als String zurück.
	 *
	 * @param te    das TypeElement
	 *
	 * @return der einfache Typ als String
	 */
	public static String getSimpleTypeName(final TypeElement te) {
		return "" + te.getSimpleName();
	}


	/**
	 * Gibt den einfachen Typ (ohne Package) als String zurück.
	 * Im Falle eines primitiven Typs wird dieser zurückgegeben.
	 *
	 * @param ve    die Variable
	 *
	 * @return der einfache Typ als String
	 */
	public static String getSimpleTypeName(final VariableElement ve) {
		if (!(ve.asType() instanceof DeclaredType))
			return "" + ve.asType().toString();
		final var type = getTypeElement(ve);
		return (type == null) ? "" : "" + type.getSimpleName();
	}


	/**
	 * Gibt den qualifizierten Typ (mit Package) als String zurück.
	 *
	 * @param te    das TypeElement
	 *
	 * @return der qualifizierte Typ als String
	 */
	public static String getQualifiedTypeName(final TypeElement te) {
		return "" + te.getQualifiedName();
	}


	/**
	 * Gibt den qualifizierten Typ (mit Package) als String zurück.
	 * Im Falle eines primitiven Typs wird dieser zurückgegeben.
	 *
	 * @param ve    die Variable
	 *
	 * @return der qualifizierte Typ als String
	 */
	public static String getQualifiedTypeName(final VariableElement ve) {
		if (!(ve.asType() instanceof DeclaredType))
			return "" + ve.asType().toString();
		final var type = getTypeElement(ve);
		return (type == null) ? "" : "" + type.getQualifiedName();
	}


	/**
	 * Prüfe, ob die Variable ein Array ist oder nicht.
	 *
	 * @param ve   die zu prüfende Variable
	 *
	 * @return true, falls sie ein Array ist, und ansonsten false
	 */
	public static boolean isArray(final VariableElement ve) {
		return ve.asType().getKind() == TypeKind.ARRAY;
	}


	/**
	 * Prüfe, ob die Variable ein String ist oder nicht.
	 *
	 * @param ve   die zu prüfende Variable
	 *
	 * @return true, falls sie ein String ist, und ansonsten false
	 */
	public static boolean isString(final VariableElement ve) {
		final var type = getTypeElement(ve);
		return (type != null) && isString(type);
	}


	/**
	 * Prüfe, ob das TypeElement ein String ist oder nicht.
	 *
	 * @param te   das zu prüfende Element
	 *
	 * @return true, falls es ein String ist, und ansonsten false
	 */
	public static boolean isString(final TypeElement te) {
		return "java.lang.String".equals("" + te.getQualifiedName());
	}


	/** Ein Set mit den Klassen für boxed primitives und dem abstrakten Typ Number */
	private static final Set<String> setBoxedPrimitives = Set.of(
		"java.lang.Character",
		"java.lang.Boolean",
		"java.lang.Number",
		"java.lang.Byte",
		"java.lang.Short",
		"java.lang.Integer",
		"java.lang.Long",
		"java.lang.Float",
		"java.lang.Double"
	);

	/**
	 * Prüfe, ob das TypeElement ein boxed primitive ist oder nicht.
	 *
	 * @param te   das zu prüfende Element
	 *
	 * @return true, falls es ein boxed primitive ist, und ansonsten false
	 */
	public static boolean isBoxedPrimitive(final TypeElement te) {
		return setBoxedPrimitives.contains("" + te.getQualifiedName());
	}


	/**
	 * Prüfe, ob die Variable ein boxed primitive ist oder nicht.
	 *
	 * @param ve   die zu prüfende Variable
	 *
	 * @return true, falls sie ein boxed primitive ist, und ansonsten false
	 */
	public static boolean isBoxedPrimitive(final VariableElement ve) {
		final var type = getTypeElement(ve);
		return (type != null) && isBoxedPrimitive(type);
	}


	/**
	 * Prüfe, ob das TypeElement ein boxed primitive oder ein String ist oder nicht.
	 *
	 * @param te   das zu prüfende Element
	 *
	 * @return true, falls es ein boxed primitive oder ein String ist, und ansonsten false
	 */
	public static boolean isStringOrBoxedPrimitive(final TypeElement te) {
		return isString(te) || isBoxedPrimitive(te);
	}


	/**
	 * Prüfe, ob die Variable ein boxed primitive oder ein String ist oder nicht.
	 *
	 * @param ve   die zu prüfende Variable
	 *
	 * @return true, falls sie ein boxed primitive oder ein String ist, und ansonsten false
	 */
	public static boolean isStringOrBoxedPrimitive(final VariableElement ve) {
		final var type = getTypeElement(ve);
		return (type != null) && isStringOrBoxedPrimitive(type);
	}


	/**
	 * Prüfe, ob die Variable eine Collection ist oder nicht.
	 *
	 * @param ve   die zu prüfende Variable
	 *
	 * @return true, falls sie eine Collection ist, und ansonsten false
	 */
	public static boolean isCollection(final VariableElement ve) {
		final var type = getTypeElement(ve);
		return (type != null) && isCollection(type);
	}


	/**
	 * Prüfe, ob das TypeElement eine Collection ist oder nicht.
	 *
	 * @param te   das zu prüfende TypeElement
	 *
	 * @return true, falls es eine Collection ist, und ansonsten false
	 */
	public static boolean isCollection(final TypeElement te) {
		var tmp = te;
		while (tmp != null) {
			final String className = "" + tmp.getQualifiedName();
			if ("java.lang.Object".equals(className))
				return false;
			// Prüfe, ob dieser Typ das Collection-Interface implementiert
			for (final var type : tmp.getInterfaces())
				if ((type instanceof final DeclaredType dt) && (dt.asElement() instanceof final TypeElement typeInterface) && ("java.util.Collection".equals("" + typeInterface.getQualifiedName())))
					return true;
			tmp = ((te.getSuperclass() instanceof final DeclaredType dt) && (dt.asElement() instanceof final TypeElement superClass))
				? superClass : null;
		}
		return false;
	}


	/**
	 * Gibt das Type-Parameter-Element an der Stelle index zurück.
	 *
	 * @param te      das Type-Element, dessen Type-Parameter-Element zurückgegeben werden soll
	 * @param index   der Index des Type-Parameters (0-indiziert)
	 *
	 * @return das Type-Parameter-Element oder null
	 */
	public static TypeParameterElement getTypeParameter(final TypeElement te, final int index) {
		if (index >= te.getTypeParameters().size())
			return null;
		return te.getTypeParameters().get(index);
	}


	/**
	 * Gibt das Type-Parameter-Element an der Stelle index zurück.
	 *
	 * @param te      das Type-Element, dessen Type-Parameter-Element zurückgegeben werden soll
	 * @param index   der Index des Type-Parameters (0-indiziert)
	 *
	 * @return das Type-Parameter-Element oder null
	 */
	public static TypeElement getTypeOfTypeParameter(final TypeElement te, final int index) {
		if (index >= te.getTypeParameters().size())
			return null;
		final var e = te.getTypeParameters().get(index).getGenericElement();
		return (e instanceof final TypeElement type) ? type : null;
	}


	/**
	 * Gibt das Type-Parameter-Element an der Stelle index zurück.
	 *
	 * @param ve      die Variable, deren Type-Parameter-Element zurückgegeben werden soll
	 * @param index   der Index des Type-Parameters (0-indiziert)
	 *
	 * @return das Type-Parameter-Element oder null
	 */
	public static TypeElement getTypeOfTypeParameter(final VariableElement ve, final int index) {
		final var type = getTypeElement(ve);
		return (type == null) ? null : getTypeOfTypeParameter(type, index);
	}


	/**
	 * Ermittelt alle Type-Variablen, welche bei der Methode bei den Typen der Rückgabe
	 * und der Parameter genutzt werden.
	 *
	 * @param execElem   die Methode, der Konstruktor oder der Iniatializer
	 *
	 * @return die Map mit den genutzten Typ-Variablen zu geodnet zu ihren Bezeichnern
	 */
	public static Map<String, TypeVariable> getUsedTypeVariables(final ExecutableElement execElem) {
		final Map<String, TypeVariable> result = new HashMap<>();
		final List<TypeMirror> types = execElem.getParameters().stream().map(Element::asType).collect(Collectors.toList());
		types.add(execElem.getReturnType());
		while (!types.isEmpty()) {
			final TypeMirror type = types.getLast();
			types.removeLast();
			switch (type) {
				case final NoType nt -> { /**/ }
				case final PrimitiveType pt -> { /**/ }
				case final TypeVariable tv -> result.put(tv.asElement().getSimpleName().toString(), tv);
				case final DeclaredType dt -> types.addAll(dt.getTypeArguments());
				case final ArrayType at -> types.add(at.getComponentType());
				case final IntersectionType it -> types.addAll(it.getBounds());
				case final UnionType ut -> types.addAll(ut.getAlternatives());
				default -> throw new TranspilerException("TranspilerError: Unhandled type kind " + type.getKind());
			}
		}
		return result;
	}


	/**
	 * Ermittelt für das angegeben TypeElement cur den Typ des direkten Vorgängers im Vererbungsbaum.
	 * Kann ancestor nicht als direkter Vorgänger gefunden werden, so wird null zurückgegeben.
	 *
	 * @param cur        das TypeElement
	 * @param ancestor   der Vorgänger
	 *
	 * @return der type des Vorgängers oder null
	 */
	public static DeclaredType getDirectAncestorType(final TypeElement cur, final TypeElement ancestor) {
		final List<TypeMirror> ancTypes = new ArrayList<>(cur.getInterfaces());
		ancTypes.add(cur.getSuperclass());
		for (final TypeMirror ifType : ancTypes)
			if ((ifType instanceof final DeclaredType dt) && (dt.asElement() instanceof final TypeElement anc)
					&& (anc.getQualifiedName().equals(ancestor.getQualifiedName())))
				return dt;
		return null;
	}


	/**
	 * Löst die Typ-Parameter von elem in dem Vererbungspfades path auf, indem die Typ-Variablen rückwärts
	 * durch die jeweiligen Typ-Parameter bis zum Anfang des Pfades ersetzt werden. Wird elem mit null
	 * aufgerufen, so wird das letzte Element des Pfades als Elem angenommen.
	 *
	 * @param path   der Vererbungspfad
	 * @param elem   das Element, dessen type-Parameter mithilfe des Pfades aufgelöst werden
	 *
	 * @return eine Map von dem Typ-Parameter zu dem TypeMirror-Objekt (z.B. TypeVariable oder DeclaredType)
	 */
	public static Map<String, TypeMirror> resolveTypeVariables(final List<TypeElement> path, final TypeElement elem) {
		if ((elem == null) && ((path == null) || (path.isEmpty())))
			throw new TranspilerException("Transpiler Error: Fehlerhafter Aufruf der Methode.");
		// Ist der Pfad leer so erfolgt keine weitere Auflösung und die Typ-Variablen werden selbst als Ergebnis zurückgegeben
		if (path.isEmpty()) {
			final Map<String, TypeMirror> resolved = new LinkedHashMap<>();
			for (final TypeParameterElement typeParam : elem.getTypeParameters())
				resolved.put(typeParam.getSimpleName().toString(), typeParam.asType());
			return resolved;
		}
		// Führe zunächst den rekursiven Aufruf entlang des Pfades aus und benutze das Ergebnis für das Auflösen der typ-Variablen von elem
		final List<TypeElement> tmpList = new ArrayList<>(path);
		tmpList.removeLast();
		final Map<String, TypeMirror> lastResolved = resolveTypeVariables(tmpList, path.getLast());
		if (elem == null)
			return lastResolved;
		// Benutzer die Auflösung der Typ-Variablen des vorigen Element (rekursiver Aufruf) für das Auflösen der TypVariablen des aktuellen Elements
		final Map<String, TypeMirror> resolved = new LinkedHashMap<>();
		final DeclaredType ancestor = ElementUtils.getDirectAncestorType(path.getLast(), elem);
		if (ancestor.getTypeArguments().size() != elem.getTypeParameters().size())
			throw new TranspilerException("Transpiler Error: Invalid size of type parameter list.");
		for (int i = 0; i < elem.getTypeParameters().size(); i++) {
			final String typeVarName = elem.getTypeParameters().get(i).getSimpleName().toString();
			if (ancestor.getTypeArguments().get(i) instanceof final DeclaredType dt)
				resolved.put(typeVarName, dt);
			else if (ancestor.getTypeArguments().get(i) instanceof final TypeVariable tv)
				resolved.put(typeVarName, lastResolved.get(tv.asElement().getSimpleName().toString()));
			else
				throw new TranspilerException("Transpiler Error: Unhandled Type argument of type kind " + ancestor.getTypeArguments().get(i).getKind());
		}
		return resolved;
	}

}
