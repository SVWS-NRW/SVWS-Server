import { describe, expect, test } from 'vitest';

import { FachDaten } from '@core/core/data/fach/FachDaten';
import { UvFach } from '@core/core/data/uv/UvFach';
import { UvManager } from '@core/core/utils/uv/UvManager';
import { ArrayList } from '@core/java/util/ArrayList';

import { uvFachGueltigkeitValidator, UvGueltigkeitValidator } from '~/components/unterrichtsverteilung/UvGueltigkeitValidator';

describe('UV-Gültigkeitsprüfung', () => {
	test.each([
		[null, null, false],
		['2026-02-30', null, false],
		['2026-08-01', 'kein Datum', false],
		['2026-08-01', '2026-07-31', false],
		['2026-08-01', '2026-08-01', true],
		['2026-08-01', null, true],
	])('prüft den Zeitraum %s bis %s', (von, bis, erwartet) => {
		expect(new UvGueltigkeitValidator(() => von, () => bis).run()).toBe(erwartet);
	});

	test('prüft inklusive Grenzen und offene Enden mit dem echten UV-Manager', () => {
		const manager = new UvManager(new ArrayList(), ArrayList.of(Object.assign(new FachDaten(), { id: 1 })));
		const bestand = Object.assign(new UvFach(), { id: 1, idFach: 1, gueltigVon: '2026-08-01', gueltigBis: '2027-07-31' });
		manager.fachAdd(bestand);
		const neu = Object.assign(new UvFach(), { idFach: 1, gueltigVon: '2027-07-31' });
		expect(uvFachGueltigkeitValidator(manager, neu).run()).toBe(false);
		neu.gueltigVon = '2027-08-01';
		expect(uvFachGueltigkeitValidator(manager, neu).run()).toBe(true);
		neu.gueltigVon = '2025-08-01';
		expect(uvFachGueltigkeitValidator(manager, neu).run()).toBe(false);
	});

	test('ignoriert den eigenen Eintrag und andere Schulfächer', () => {
		const manager = new UvManager(new ArrayList(), ArrayList.of(Object.assign(new FachDaten(), { id: 1 })));
		const bestand = Object.assign(new UvFach(), { id: 1, idFach: 1, gueltigVon: '2026-08-01' });
		manager.fachAdd(bestand);
		expect(uvFachGueltigkeitValidator(manager, bestand).run()).toBe(true);
		const anderesFach = Object.assign(new UvFach(), { idFach: 2, gueltigVon: '2026-08-01' });
		expect(uvFachGueltigkeitValidator(manager, anderesFach).run()).toBe(true);
	});
});
