import { JavaObject } from '../../../java/lang/JavaObject';
import { Random } from '../../../java/util/Random';
import { HashMap } from '../../../java/util/HashMap';
import { NullPointerException } from '../../../java/lang/NullPointerException';
import { ArrayList } from '../../../java/util/ArrayList';
import { StundenplanblockungManagerKlasse, cast_de_svws_nrw_core_utils_stundenplanblockung_StundenplanblockungManagerKlasse } from '../../../core/utils/stundenplanblockung/StundenplanblockungManagerKlasse';
import type { List } from '../../../java/util/List';
import type { JavaMap } from '../../../java/util/JavaMap';

export class StundenplanblockungManagerKlasseMenge extends JavaObject {

	private readonly _menge : List<StundenplanblockungManagerKlasse> = new ArrayList<StundenplanblockungManagerKlasse>();

	private readonly _map : JavaMap<number, StundenplanblockungManagerKlasse> = new HashMap<number, StundenplanblockungManagerKlasse>();


	public constructor() {
		super();
	}

	/**
	 * Fügt die Klasse hinzu. <br>
	 * Wirft eine NullPointerException, falls die Klasse-ID bereits existiert.
	 *
	 * @param pKlasseID              Die Datenbank-ID der Klasse.
	 * @param pKuerzel               Das Kürzel der Klasse.
	 * @throws NullPointerException  Falls die Klasse-ID bereits existiert.
	 */
	public addOrException(pKlasseID : number, pKuerzel : string) : void {
		if (this._map.containsKey(pKlasseID))
			throw new NullPointerException("Die Klasse-ID " + pKlasseID + " existiert bereits!")
		const kl : StundenplanblockungManagerKlasse | null = new StundenplanblockungManagerKlasse(pKlasseID, pKuerzel);
		this._map.put(pKlasseID, kl);
		this._menge.add(kl);
	}

	/**
	 * Liefert das {@link StundenplanblockungKlasse}-Objekt zur übergebenen ID. <br>
	 * Wirft eine NullPointerException, falls die Klasse-ID unbekannt ist.
	 *
	 * @param pKlasseID              Die Datenbank-ID der Klasse.
	 * @return                       Das {@link StundenplanblockungKlasse}-Objekt zur übergebenen ID.
	 * @throws NullPointerException  Falls die Klasse-ID unbekannt ist.
	 */
	public getOrException(pKlasseID : number) : StundenplanblockungManagerKlasse {
		const klasse : StundenplanblockungManagerKlasse | null = this._map.get(pKlasseID);
		if (klasse === null)
			throw new NullPointerException("Klasse-ID " + pKlasseID + " unbekannt!")
		return klasse;
	}

	/**
	 * Liefert eine zufällige Klasse oder null, falls es gar keine Lehrkräfte gibt.
	 *
	 * @param pRandom  Das Random-Objekt zum Erzeugen von Zufallszahlen.
	 * @return         Eine zufällige Lehrkraft oder null, falls es gar keine Lehrkräfte gibt.
	 */
	public getRandomOrException(pRandom : Random) : StundenplanblockungManagerKlasse {
		const size : number = this._menge.size();
		if (size <= 0)
			throw new NullPointerException("Es gibt keine Klassen!")
		return this._menge.get(pRandom.nextInt(size));
	}

	/**
	 * Löscht die übergebene Klasse. <br>
	 * Wirft eine NullPointerException, falls die Klasse-ID unbekannt ist.
	 *
	 * @param pKlasseID              Die Datenbank-ID der Klasse.
	 * @throws NullPointerException  Falls die Klasse-ID unbekannt ist.
	 */
	public removeOrException(pKlasseID : number) : void {
		const klasse : StundenplanblockungManagerKlasse = this.getOrException(pKlasseID);
		this._map.remove(pKlasseID);
		this._menge.remove(klasse);
	}

	/**
	 * Liefert TRUE, falls die Klasse-ID existiert.
	 *
	 * @param pKlasseID  Die Datenbank-ID der Klasse.
	 * @return           TRUE, falls die Klasse-ID existiert.
	 */
	public exists(pKlasseID : number) : boolean {
		return this._map.containsKey(pKlasseID);
	}

	/**
	 * Liefert die Menge aller Klassen.
	 *
	 * @return Die Menge aller Klassen.
	 */
	public getMenge() : List<StundenplanblockungManagerKlasse> | null {
		return this._menge;
	}

	/**
	 * Liefert die Anzahl an Klassen.
	 *
	 * @return Die Anzahl an Klassen.
	 */
	public size() : number {
		return this._menge.size();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.stundenplanblockung.StundenplanblockungManagerKlasseMenge';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.stundenplanblockung.StundenplanblockungManagerKlasseMenge'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_stundenplanblockung_StundenplanblockungManagerKlasseMenge(obj : unknown) : StundenplanblockungManagerKlasseMenge {
	return obj as StundenplanblockungManagerKlasseMenge;
}
