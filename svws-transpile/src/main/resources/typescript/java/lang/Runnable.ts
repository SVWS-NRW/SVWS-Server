
export interface Runnable {

	run() : void;

}


export function cast_java_lang_Runnable(obj : unknown) : Runnable {
	return obj as Runnable;
}
