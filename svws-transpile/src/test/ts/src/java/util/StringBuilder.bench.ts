import { bench, describe } from "vitest";

describe(
	"java.lang.StringBuilder.reverse testen",
	() => {
		const value = "test";
		bench("reverse implementiert", () => {
			void value.split("").reverse().join("");
		});
		bench("for loop", () => {
			const a: string[] = [];
			for (const s of value) {
				a.unshift(s);
			}
			void a.join();
		});
		bench("destructure", () => {
			// eslint-disable-next-line @typescript-eslint/no-misused-spread
			void [...value].reverse().join();
		});
		bench("concat und charAt", () => {
			let a = "";
			for (let i = value.length - 1; i >= 0; i--) {
				a = a.concat(value.charAt(i));
			}
		});
		bench("concat und at", () => {
			let a = "";
			for (let i = value.length - 1; i >= 0; i--) {
				a = a.concat(value.at(i) ?? '');
			}
		});
	}
);
