import { JavaObject } from '../../../java/lang/JavaObject';
import { SchuldateiKatalogeintrag } from '../../../schuldatei/v1/data/SchuldateiKatalogeintrag';
import { SchuldateiManager } from '../../../schuldatei/v1/utils/SchuldateiManager';
import { SchuldateiOrganisationseinheit } from '../../../schuldatei/v1/data/SchuldateiOrganisationseinheit';
import { IllegalArgumentException } from '../../../java/lang/IllegalArgumentException';

export class SchuldateiOrganisationseinheitManager extends JavaObject {

	/**
	 * Die Referenz auf den Manager für die Schuldatei
	 */
	private readonly _managerSchuldatei : SchuldateiManager;

	/**
	 * Das Datenobjekt für die Schuldatei
	 */
	private readonly _organisationseinheit : SchuldateiOrganisationseinheit;


	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit der übergebenen Organisationseinheit und
	 * dem zugehörigen Manager für die Schuldatei.
	 *
	 * @param managerSchuldatei      der Manager für die Schuldatei
	 * @param organisationseinheit   die Organisationseinheit aus der Schuldatei
	 */
	public constructor(managerSchuldatei : SchuldateiManager, organisationseinheit : SchuldateiOrganisationseinheit) {
		super();
		this._managerSchuldatei = managerSchuldatei;
		this._organisationseinheit = organisationseinheit;
		if ((organisationseinheit.oeart !== null) && (!this._managerSchuldatei.katalogOrganisationseinheitarten.hatEintrag(organisationseinheit.oeart)))
			throw new IllegalArgumentException("Die Art " + organisationseinheit.oeart + " der Organisationseinheit mit der Schulnummer " + organisationseinheit.schulnummer + " hat keinen zugehörigen Katalog-Eintrag.")
	}

	/**
	 * Gibt die Schulnummer der Organisationseinheit zurück.
	 *
	 * @return die Schulnummer
	 */
	public getSchulnummer() : number {
		return this._organisationseinheit.schulnummer;
	}

	/**
	 * Gib die Bundeslandkennung (NRW) der Organisationseinheit zurück.
	 *
	 * @return die Bundeslandkennung
	 */
	public getBundeslandkennung() : string | null {
		return this._organisationseinheit.bundeslandkennung;
	}

	/**
	 * Gibt die eindeutige Identifier für das XSCHULE-Format zurück.
	 *
	 * @return der Identifier
	 */
	public getXscid() : string | null {
		return this._organisationseinheit.xscid;
	}

	/**
	 * Gibt die Art der Organisationseinheit aus dem Katalog der Schuldatei zurück.
	 *
	 * @return die Art der Organisationseinheit
	 */
	public getArt() : string | null {
		return this._organisationseinheit.oeart;
	}

	/**
	 * Gibt die Bezeichnung der Art der Organisationseinheit aus dem Katalog der Schuldatei zurück.
	 *
	 * @return die Bezeichnung der Art der Organisationseinheit
	 */
	public getArtBezeichnung() : string | null {
		const eintrag : SchuldateiKatalogeintrag | null = this._managerSchuldatei.katalogOrganisationseinheitarten.getEintrag(this._organisationseinheit.oeart);
		return (eintrag === null) ? "" : eintrag.bezeichnung;
	}

	/**
	 * Gibt die amtliche Bezeichnung der Organisationseinheit zurück
	 *
	 * @return die amtliche Bezeichnung
	 */
	public getAmtlicheBezeichnung() : string {
		return this._organisationseinheit.amtsbez;
	}

	/**
	 * Gibt das Datum der Errichtung der Organisationseinheit zurück.
	 *
	 * @return das Datum der Errichtung der Organisationseinheit
	 */
	public getDatumErrichtung() : string | null {
		return this._organisationseinheit.errichtung;
	}

	/**
	 * Gibt das Datum der Auflösung der Organisationseinheit zurück.
	 *
	 * @return das Datum der Auflösung
	 */
	public getDatumAufloesung() : string | null {
		return this._organisationseinheit.aufloesung;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.schuldatei.v1.utils.SchuldateiOrganisationseinheitManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.schuldatei.v1.utils.SchuldateiOrganisationseinheitManager'].includes(name);
	}

}

export function cast_de_svws_nrw_schuldatei_v1_utils_SchuldateiOrganisationseinheitManager(obj : unknown) : SchuldateiOrganisationseinheitManager {
	return obj as SchuldateiOrganisationseinheitManager;
}
