
export interface JavaFunction<T, R> {

	apply(t : T) : R;

	// NOT SUPPORTED: type script interface does not support default function. The following would require an implementation in the implementing class
	// compose<V>(before : Function<V, T>): Function<V, R>
    // andThen(after : Function<T, R>) : Consumer<T, R>;
	// identity<T>() : Function<T, T>

}


export function cast_java_util_function_Function<T, R>(obj : unknown) : JavaFunction<T, R> {
	return obj as JavaFunction<T, R>;
}
