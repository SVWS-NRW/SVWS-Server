import { bench, describe } from 'vitest'

describe('Sonstige Benchmarks', () => {
	const a = "Tom";
	const b = "Hawk";

	bench('string concat', () => void (a + 'A' + b));
	bench('template String', () => void `${a}A${b}`);
})