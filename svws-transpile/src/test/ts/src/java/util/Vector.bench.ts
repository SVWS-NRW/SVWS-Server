import { l, n, s } from "@transpile/shared/TestObjects";
import { describe, test } from "vitest";

import { Vector } from "@core/java/util/Vector";

let v: Vector<unknown>;

describe.each([s, n, l])("java.util.Vector, getestet mit $name", ({ a, b, c, d, e }) => {
	test('Run benchmarks', async ({ bench }) => {
		v = new Vector();
		v.add(a);
		v.add(b);
		v.add(c);
		v.add(d);
		v.add(e);
		bench("Array from", () => {
			Array.from(v);
		});
		bench("toArray", () => {
			v.toArray(new Array<typeof a>());
		});
		bench("[...v] destructure", () => {
			const _ = [...v];
		});
		bench("for of loop", () => {
			const _ = [];
			for (const e of v) {
				_.push(e);
			}
		});
		bench("for loop", () => {
			const _ = [];
			for (let i = 0; i < v.size(); i++) {
				_.push(v.get(i));
			}
		});
	});
});

