
export interface BiFunction<T, U, R> {

	apply(t : T, u : U) : R;

	// NOT SUPPORTED: type script interface does not support default function. The following would require an implementation in the implementing class
    // andThen<V>(after : Function<R, V>) : BiFunction<T, U, V>;

}


export function cast_java_util_function_BiFunction<T, U, R>(obj : unknown) : BiFunction<T, U, R> {
	return obj as BiFunction<T, U, R>;
}
