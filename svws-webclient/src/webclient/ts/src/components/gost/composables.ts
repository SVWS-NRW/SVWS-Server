import { SchuelerListeEintrag } from '@svws-nrw/svws-core-ts';
import { injectMainApp } from '../../apps/Main';

export function useSchuelerListe(): SchuelerListeEintrag[] {
	const { apps: { gost } } = injectMainApp();

	return gost.listAbiturjahrgangSchueler.liste || []
}