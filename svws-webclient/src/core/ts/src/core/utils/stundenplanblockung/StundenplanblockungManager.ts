import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { StundenplanblockungManagerFachMenge, cast_de_nrw_schule_svws_core_utils_stundenplanblockung_StundenplanblockungManagerFachMenge } from '../../../core/utils/stundenplanblockung/StundenplanblockungManagerFachMenge';
import { StundenplanblockungManagerLehrkraftMenge, cast_de_nrw_schule_svws_core_utils_stundenplanblockung_StundenplanblockungManagerLehrkraftMenge } from '../../../core/utils/stundenplanblockung/StundenplanblockungManagerLehrkraftMenge';
import { StundenplanblockungManagerLerngruppeMenge, cast_de_nrw_schule_svws_core_utils_stundenplanblockung_StundenplanblockungManagerLerngruppeMenge } from '../../../core/utils/stundenplanblockung/StundenplanblockungManagerLerngruppeMenge';
import { StundenplanblockungManagerRaumMenge, cast_de_nrw_schule_svws_core_utils_stundenplanblockung_StundenplanblockungManagerRaumMenge } from '../../../core/utils/stundenplanblockung/StundenplanblockungManagerRaumMenge';
import { StundenplanblockungManagerKopplungMenge, cast_de_nrw_schule_svws_core_utils_stundenplanblockung_StundenplanblockungManagerKopplungMenge } from '../../../core/utils/stundenplanblockung/StundenplanblockungManagerKopplungMenge';
import { StundenplanblockungManagerSchule, cast_de_nrw_schule_svws_core_utils_stundenplanblockung_StundenplanblockungManagerSchule } from '../../../core/utils/stundenplanblockung/StundenplanblockungManagerSchule';
import { StundenplanblockungManagerStatistik, cast_de_nrw_schule_svws_core_utils_stundenplanblockung_StundenplanblockungManagerStatistik } from '../../../core/utils/stundenplanblockung/StundenplanblockungManagerStatistik';
import { StundenplanblockungManagerKlasseMenge, cast_de_nrw_schule_svws_core_utils_stundenplanblockung_StundenplanblockungManagerKlasseMenge } from '../../../core/utils/stundenplanblockung/StundenplanblockungManagerKlasseMenge';

export class StundenplanblockungManager extends JavaObject {

	private readonly _manager_sc : StundenplanblockungManagerSchule;

	private readonly _manager_fa : StundenplanblockungManagerFachMenge;

	private readonly _manager_kl : StundenplanblockungManagerKlasseMenge;

	private readonly _manager_ko : StundenplanblockungManagerKopplungMenge;

	private readonly _manager_le : StundenplanblockungManagerLehrkraftMenge;

	private readonly _manager_gr : StundenplanblockungManagerLerngruppeMenge;

	private readonly _manager_ra : StundenplanblockungManagerRaumMenge;

	private readonly _manager_st : StundenplanblockungManagerStatistik;


	/**
	 * Erzeugt einen neuen, leeren Manager.
	 */
	public constructor() {
		super();
		this._manager_sc = new StundenplanblockungManagerSchule();
		this._manager_fa = new StundenplanblockungManagerFachMenge();
		this._manager_kl = new StundenplanblockungManagerKlasseMenge();
		this._manager_ko = new StundenplanblockungManagerKopplungMenge();
		this._manager_le = new StundenplanblockungManagerLehrkraftMenge();
		this._manager_gr = new StundenplanblockungManagerLerngruppeMenge();
		this._manager_ra = new StundenplanblockungManagerRaumMenge();
		this._manager_st = new StundenplanblockungManagerStatistik();
	}

	/**
	 * Liefert den Manager zur Verwaltung der globalen Schulkonfiguration.
	 * 
	 * @return Den Manager zur Verwaltung der globalen Schulkonfiguration.
	 */
	public getSchule() : StundenplanblockungManagerSchule {
		return this._manager_sc;
	}

	/**
	 * Liefert den Manager zur Verwaltung der Lehrkräfte.
	 * 
	 * @return Den Manager zur Verwaltung der Lehrkräfte.
	 */
	public getLehrkraefte() : StundenplanblockungManagerLehrkraftMenge {
		return this._manager_le;
	}

	/**
	 * Liefert den Manager zur Verwaltung der Lerngruppen.
	 * 
	 * @return Den Manager zur Verwaltung der Lerngruppen.
	 */
	public getLerngruppen() : StundenplanblockungManagerLerngruppeMenge {
		return this._manager_gr;
	}

	/**
	 * Liefert den Manager zur Verwaltung der Klassen.
	 * 
	 * @return Den Manager zur Verwaltung der Klassen.
	 */
	public getKlassen() : StundenplanblockungManagerKlasseMenge {
		return this._manager_kl;
	}

	/**
	 * Liefert den Manager zur Verwaltung der Fächer.
	 * 
	 * @return Den Manager zur Verwaltung der Fächer.
	 */
	public getFaecher() : StundenplanblockungManagerFachMenge {
		return this._manager_fa;
	}

	/**
	 * Liefert den Manager zur Verwaltung der Räume.
	 * 
	 * @return Den Manager zur Verwaltung der Räume.
	 */
	public getRaeume() : StundenplanblockungManagerRaumMenge {
		return this._manager_ra;
	}

	/**
	 * Liefert den Manager zur Verwaltung der Kopplungen.
	 * 
	 * @return Den Manager zur Verwaltung der Kopplungen.
	 */
	public getKopplungen() : StundenplanblockungManagerKopplungMenge {
		return this._manager_ko;
	}

	/**
	 * Überprüft alle Daten auf ihre Konsistenz. <br>
	 * Wirft eine Exception, falls die Daten nicht konsistent sind.  
	 */
	public miscCheckConsistencyOrException() : void {
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.utils.stundenplanblockung.StundenplanblockungManager'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_utils_stundenplanblockung_StundenplanblockungManager(obj : unknown) : StundenplanblockungManager {
	return obj as StundenplanblockungManager;
}
