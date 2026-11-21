import { describe, expect, test } from 'vitest';

import { ermittleNotenWarnstufe } from '../../../src/components/enm/EnmNotenWarnstufe';

describe('ermittleNotenWarnstufe', () => {
	test('kennzeichnet ausreichende Noten in der Mittelstufe gelb', () => {
		expect(ermittleNotenWarnstufe('08', '08a', '4')).toBe('gelb');
		expect(ermittleNotenWarnstufe('08', '08a', '4−')).toBe('gelb');
	});

	test('kennzeichnet mangelhafte Noten in der Mittelstufe rot', () => {
		expect(ermittleNotenWarnstufe('09', '09b', '5+')).toBe('rot');
		expect(ermittleNotenWarnstufe('09', '09b', '6')).toBe('rot');
	});

	test('wendet in der Oberstufe die Defizitgrenze an', () => {
		expect(ermittleNotenWarnstufe('EF', 'EF', '4-')).toBe('rot');
		expect(ermittleNotenWarnstufe('Q1', 'Q1', '4')).toBeNull();
	});

	test('liefert ohne Note oder für andere Jahrgänge keine Warnstufe', () => {
		expect(ermittleNotenWarnstufe('08', '08a', null)).toBeNull();
		expect(ermittleNotenWarnstufe('11', '11a', '5')).toBeNull();
	});
});

