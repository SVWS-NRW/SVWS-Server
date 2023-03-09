
export interface Predicate<T> {

    test(t : T) : boolean;
    
	// NOT SUPPORTED: type script interface does not support default function. The following would require an implementation in the implementing class
    // and(other : Predicate<? super T>) : Predicate<T>
    // negate() : Predicate<T>
    // or(other : Predicate<? super T>) : Predicate<T>
    // isEqual<T>(targetRef : JavaObject) : Predicate<T>
    // not<T>(target : Predicate<? super T>) : Predicate<T>

}


export function cast_java_util_function_Predicate<T>(obj : unknown) : Predicate<T> {
	return obj as Predicate<T>;
}
