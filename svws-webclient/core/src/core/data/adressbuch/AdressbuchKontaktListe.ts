import { AdressbuchEintrag } from '../../../core/data/adressbuch/AdressbuchEintrag';
import { AdressbuchKontakt, cast_de_svws_nrw_core_data_adressbuch_AdressbuchKontakt } from '../../../core/data/adressbuch/AdressbuchKontakt';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class AdressbuchKontaktListe extends AdressbuchEintrag {

	/**
	 * Die Kategorien dieses Kontakts
	 */
	public kontakte : List<AdressbuchKontakt> = new ArrayList<AdressbuchKontakt>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.adressbuch.AdressbuchKontaktListe';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.adressbuch.AdressbuchKontaktListe', 'de.svws_nrw.core.data.adressbuch.AdressbuchEintrag'].includes(name);
	}

	public static class = new Class<AdressbuchKontaktListe>('de.svws_nrw.core.data.adressbuch.AdressbuchKontaktListe');

}

export function cast_de_svws_nrw_core_data_adressbuch_AdressbuchKontaktListe(obj : unknown) : AdressbuchKontaktListe {
	return obj as AdressbuchKontaktListe;
}
