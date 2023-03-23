import { CharSequence } from './CharSequence';

export interface Appendable {

    append(csq : CharSequence) : Appendable;
    append(csq : CharSequence, start : number, end : number) : Appendable;
    append(c : string) : Appendable;

}

export function cast_java_lang_Appendable(obj : unknown) : Appendable {
	return obj as Appendable;
}
