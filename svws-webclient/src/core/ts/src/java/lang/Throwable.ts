import { Class } from './Class';
import { JavaObject } from './JavaObject';
import { TranspiledObject } from './TranspiledObject';


function prepareAttributeOrderForStringify() {
    return function(key : any, value : any) {
      if (value instanceof Object && !(value instanceof Array) && !(value instanceof Function)) {
        return Object.entries(value).sort().reduce((result : any, e) => {
            result[e[0]] = e[1];
            return result;
        }, {});
      }
      return value;
    }
}

export class Throwable extends Error implements TranspiledObject {

    private _cause : Throwable | null = null;

    constructor(param1? : Throwable | string | null, param2? : Throwable) {
        super(typeof param1 === "string" ? param1 : param1?.toString());
        Object.setPrototypeOf(this, new.target.prototype);
        this.name = this.constructor.name;

        // TODO handle this.stack and .stack from Throwable parameter
        if ((typeof param1 === "undefined") && (typeof param2 === "undefined")) {
            this.message = "";
        } else if ((typeof param1 === "string") && (typeof param2 === "undefined")) {
            this.message = param1;
        } else if ((typeof param1 === "string") && (param2 instanceof Throwable)) {
            this.message = param1;
            this._cause = param2;
        } else if ((param1 instanceof Throwable) && (typeof param2 === "undefined")) {
            this.message = param1.toString().valueOf();
            this._cause = param1;
        }
    }

    public getMessage() : string {
        return this.message;
    }

    public getLocalizedMessage() : string {
        return this.getMessage();
    }

    public getCause() : Throwable | null {
        return this._cause;
    }

    public printStackTrace() : void {
        console.error(this.stack);
    }

    public getClass<T extends TranspiledObject>() : Class<T> {
        return new Class(this);
    }

    public hashCode() : number {
        return JavaObject._hashCode(JSON.stringify(this, prepareAttributeOrderForStringify()));
    }

    public equals(obj : any) : boolean {
        if (!(typeof obj === "object"))
            return false;
        if (!(obj instanceof Throwable))
            return false;
        if (obj === null)
            return false;
        return (this === obj);
    }

    public clone() : JavaObject {
        return { ...this };
    }

    public toString() : string {
        return JSON.stringify(this, prepareAttributeOrderForStringify());
    }

	isTranspiledInstanceOf(name : string): boolean {
		return [
            'java.lang.Throwable',
            'java.lang.Object',
            'java.lang.Serializable',
        ].includes(name);
	}

}


export function cast_java_lang_Throwable(obj : unknown) : Throwable {
	return obj as Throwable;
}
