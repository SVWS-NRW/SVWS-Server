import { describe, expect, test } from 'vitest';

import { Schuljahresabschnitt } from '@core/asd/data/schule/Schuljahresabschnitt';
import { UvPlanungsabschnitt } from '@core/core/data/uv/UvPlanungsabschnitt';
import { ArrayList } from '@core/java/util/ArrayList';
import { UvPlanungsabschnitteListeManager } from '@ui/ui/manager/unterrichtsverteilung/UvPlanungsabschnitteListeManager';

function fixture() {
	const halbjahr = Object.assign(new Schuljahresabschnitt(), { id: 1, schuljahr: 2026, abschnitt: 1 });
	const aktiv = Object.assign(new UvPlanungsabschnitt(), { id: 1, schuljahr: 2026, aktiv: true, gueltigVon: '2026-10-01', gueltigBis: '2026-10-31' });
	const auswahl = Object.assign(new UvPlanungsabschnitt(), { id: 2, schuljahr: 2026, aktiv: false, gueltigVon: '2026-08-01', gueltigBis: '2026-09-30' });
	const manager = new UvPlanungsabschnitteListeManager(1, 1, ArrayList.of(halbjahr), null, ArrayList.of(aktiv, auswahl), false);
	manager.setDaten(auswahl);
	return { manager, aktiv };
}

describe('UV-Planungsabschnitte – gemeinsame Überschneidungsprüfung', () => {
	test.each([
		['2026-09-01', '2026-09-30', true],
		['2026-09-01', '2026-10-01', false],
		['2026-10-10', '2026-10-20', false],
		['2026-09-01', '2026-11-30', false],
		['2026-11-01', null, true],
		['2026-09-01', null, false],
	])('prüft %s bis %s einschließlich Grenztagen und offenen Enden', (von, bis, erwartet) => {
		const { manager } = fixture();
		expect(manager.istKonfliktfreiZuAktivenPlanungsabschnitten(von, bis, true)).toBe(erwartet);
	});

	test('erkennt vollständige Umschließung in beiden Datumsfeldern und der Entwurfsprüfung', () => {
		const { manager } = fixture();
		expect(manager.validateGueltigVon('2026-09-01', '2026-11-30', true, false, true)).toBe(false);
		expect(manager.validateGueltigBis('2026-09-01', '2026-11-30', true, false, true)).toBe(false);
		expect(manager.planungsabschnittEntwurfGueltig({ beschreibung: 'Neu', aktiv: true, gueltigVon: '2026-09-01', gueltigBis: '2026-11-30' })).toBe(false);
		expect(manager.validateGueltigVon('2026-09-01', '2026-11-30', false, false, true)).toBe(true);
		expect(manager.validateGueltigBis('2026-09-01', '2026-11-30', false, true, true)).toBe(false);
	});

	test('schließt beim Bearbeiten den eigenen aktiven Abschnitt aus', () => {
		const { manager, aktiv } = fixture();
		manager.setDaten(aktiv);
		expect(manager.istKonfliktfreiZuAktivenPlanungsabschnitten(aktiv.gueltigVon, aktiv.gueltigBis)).toBe(true);
		expect(manager.istKonfliktfreiZuAktivenPlanungsabschnitten(aktiv.gueltigVon, aktiv.gueltigBis, true)).toBe(false);
	});
});
