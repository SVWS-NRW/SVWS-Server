import { AdressbuchEintrag } from '../../../core/data/adressbuch/AdressbuchEintrag';
import { AdressbuchKontakt, cast_de_svws_nrw_core_data_adressbuch_AdressbuchKontakt } from '../../../core/data/adressbuch/AdressbuchKontakt';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';

export class AdressbuchKontaktListe extends AdressbuchEintrag {

	/**
	 * Die Kategorien dieses Kontakts
	 */
	public kontakte : List<AdressbuchKontakt | null> | null = new ArrayList();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.adressbuch.AdressbuchKontaktListe';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.adressbuch.AdressbuchKontaktListe', 'de.svws_nrw.core.data.adressbuch.AdressbuchEintrag'].includes(name);
	}

}

export function cast_de_svws_nrw_core_data_adressbuch_AdressbuchKontaktListe(obj : unknown) : AdressbuchKontaktListe {
	return obj as AdressbuchKontaktListe;
}
