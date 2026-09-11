import { describe, test } from 'vitest';

describe('Sonstige Benchmarks', () => {
	test('benches laufen', async ({ bench }) => {

		const a = "Tom";
		const b = "Hawk";

		bench('string concat', () => (a + 'A' + b));
		bench('template String', () => `${a}A${b}`);
	});
});