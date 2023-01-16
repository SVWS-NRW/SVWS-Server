import { SchuelerListeEintrag } from '@svws-nrw/svws-core-ts';
import { routeGostKursplanungSchueler } from '~/router/apps/gost/kursplanung/RouteGostKursplanungSchueler';

export function useSchuelerListe(): SchuelerListeEintrag[] {
	return routeGostKursplanungSchueler.data.listSchueler.liste || []
}