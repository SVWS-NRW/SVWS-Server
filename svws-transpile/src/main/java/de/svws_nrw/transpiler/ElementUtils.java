package de.svws_nrw.transpiler;

import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

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


}
