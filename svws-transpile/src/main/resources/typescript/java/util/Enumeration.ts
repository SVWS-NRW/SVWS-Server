export interface Enumeration<E> {

    hasMoreElements() : boolean;
    nextElement() : E;

}


export function cast_java_util_Enumeration<E>(obj : unknown) : Enumeration<E> {
	return obj as Enumeration<E>;
}
