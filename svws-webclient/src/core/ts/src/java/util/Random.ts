import { JavaObject } from '../lang/JavaObject';

export class Random extends JavaObject {

    public constructor(seed ?: number) {
    	super();
    	// TODO use seed - see for example https://github.com/BluSpring/java-util-random/blob/master/src/index.ts
    }

    public nextInt(bound : number) : number {
        return Math.floor(Math.random() * Math.floor(bound));
    }

    public nextLong(bound ?: number) : number {
        // TODO using number has drawbacks... bigint as an alternaive has other drawbacks regarding transpiled java code
        // bound should be less or equal to 9007199254740991
        return Math.floor(Math.random() * Math.floor(!bound ? 9007199254740991 : bound));
    }

    public nextDouble() : number {
        return Math.random();
    }

    public nextBoolean() : boolean {
        return Math.random() < 0.5 ? true : false;
    }

	public isTranspiledInstanceOf(name : string): boolean {
		return [
            'java.util.Random',
            'java.lang.Object',
            'java.io.Serializable'
        ].includes(name);
	}

}


export function cast_java_util_Random(obj : unknown) : Random {
	return obj as Random;
}
