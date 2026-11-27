import type { UvAuswahlStatus } from './UvPlanung';

/** Einheitliche Hervorhebung für Einträge in UV-Auswahlansichten. */
export function uvAuswahlKlasse(status: UvAuswahlStatus): string {
	switch (status) {
		case 'direkt': {
			return 'border-ui-brand bg-ui-selected ring-2 ring-ui-brand';
		}
		case 'bezogen': {
			return 'border-ui-brand bg-ui-selected';
		}
		case 'unbeteiligt': {
			return 'opacity-70 hover:opacity-100 focus-within:opacity-100';
		}
		default: {
			return '';
		}
	}
}

/** Verbindet die Auswahlmarkierung der Matrix mit ihrem Belegungsstatus. */
export function uvAuswahlMatrixKlasse(status: UvAuswahlStatus, offen: boolean): string {
	if (status === 'direkt' || status === 'bezogen') {
		return uvAuswahlKlasse(status);
	}
	const belegung = offen ? 'border-ui-warning bg-ui-warning-weak hover:border-ui-brand' : 'border-ui-success bg-ui-success/10 hover:border-ui-brand';
	return `${belegung} ${uvAuswahlKlasse(status)}`;
}
