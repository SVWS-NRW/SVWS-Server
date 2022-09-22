export interface Comparable<T> {

    compareTo(o : T) : number;

}

export function cast_java_lang_Comparable<T>(obj : unknown) : Comparable<T> {
	return obj as Comparable<T>;
}
