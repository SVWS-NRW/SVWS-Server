export type EnmNotenWarnstufe = 'gelb' | 'rot';

/**
 * Ermittelt die Warnfarbe einer Note in der ENM-Notenübersicht.
 */
export function ermittleNotenWarnstufe(jahrgang: string, klassenkuerzel: string, wert: string | null): EnmNotenWarnstufe | null {
	if (wert === null) {
		return null;
	}
	const note = wert.trim().replace(/[−–]/g, '-');
	if (['EF', 'Q1', 'Q2'].includes(jahrgang.toUpperCase())) {
		return ['4-', '5+', '5', '5-', '6'].includes(note) ? 'rot' : null;
	}
	if (!/^(05|06|07|08|09|10)/.test(klassenkuerzel)) {
		return null;
	}
	if (['4+', '4', '4-'].includes(note)) {
		return 'gelb';
	}
	return ['5+', '5', '5-', '6'].includes(note) ? 'rot' : null;
}
