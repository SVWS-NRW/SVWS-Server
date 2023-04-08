import { bench, describe } from "vitest";
import { ArrayList } from "~/index";
import { l, n, s } from "../../shared/TestObjects";

let v: ArrayList<unknown>;

describe.each([s, n, l])(
	"java.util.ArrayList, getestet mit $name",
	({ a, b, c, d, e }) => {
		v = new ArrayList();
		v.add(a);
		v.add(b);
		v.add(c);
		v.add(d);
		v.add(e);
		bench( "Array from", () => {
			Array.from(v);
		});
		bench( "toArray", () => {
			v.toArray(new Array<typeof a>());
		});
		bench( "[...v] destructure", () => {
			[...v];
		});
		bench( "for of loop", () => {
			const arr = []
			for (const e of v) arr.push(e)
		});
		bench( "for loop", () => {
			const arr = []
			for (let i = 0; i < v.size(); i++) arr.push(v.get(i))
		});
	}
);
