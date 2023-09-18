
export interface Consumer<T> {

	accept(t : T) : void;

	// NOT SUPPORTED: type script interface does not support default function. The following would require an implementation in the implementing class
    // andThen(after : Consumer<T>) : Consumer<T>;

}


export function cast_java_util_function_Consumer<T>(obj : unknown) : Consumer<T> {
	return obj as Consumer<T>;
}
