export interface Comparator<T> {

    compare(o1 : T, o2 : T) : number;

    // TODO methods of functional interface

}


export function cast_java_util_Comparator<T>(obj : unknown) : Comparator<T> {
	return obj as Comparator<T>;
}
