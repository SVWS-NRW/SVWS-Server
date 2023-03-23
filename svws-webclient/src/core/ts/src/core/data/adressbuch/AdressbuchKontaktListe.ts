import { AdressbuchEintrag } from '../../../core/data/adressbuch/AdressbuchEintrag';
import { AdressbuchKontakt, cast_de_nrw_schule_svws_core_data_adressbuch_AdressbuchKontakt } from '../../../core/data/adressbuch/AdressbuchKontakt';
import { List } from '../../../java/util/List';
import { Vector } from '../../../java/util/Vector';

export class AdressbuchKontaktListe extends AdressbuchEintrag {

	/**
	 * Die Kategorien dieses Kontakts
	 */
	public kontakte : List<AdressbuchKontakt | null> | null = new Vector();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.adressbuch.AdressbuchEintrag', 'de.nrw.schule.svws.core.data.adressbuch.AdressbuchKontaktListe'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_data_adressbuch_AdressbuchKontaktListe(obj : unknown) : AdressbuchKontaktListe {
	return obj as AdressbuchKontaktListe;
}
