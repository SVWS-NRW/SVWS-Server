import { JavaObject } from '../../../java/lang/JavaObject';
import { VomSchulbesuchZurueckgestelltStatistikExport } from '../../../asd/export/data/VomSchulbesuchZurueckgestelltStatistikExport';
import { AbgaengerStatistikExport } from '../../../asd/export/data/AbgaengerStatistikExport';
import { KlassenStatistikExport } from '../../../asd/export/data/KlassenStatistikExport';
import { ArrayList } from '../../../java/util/ArrayList';
import { KlinikschuleStatistikExport } from '../../../asd/export/data/KlinikschuleStatistikExport';
import { ReligionszugehoerigkeitenStatistikExport } from '../../../asd/export/data/ReligionszugehoerigkeitenStatistikExport';
import { AbiturpruefungsergebnisseStatistikExport } from '../../../asd/export/data/AbiturpruefungsergebnisseStatistikExport';
import { UnterrichtsverteilungStatistikExport } from '../../../asd/export/data/UnterrichtsverteilungStatistikExport';
import { InternatsplaetzeStatistikExport } from '../../../asd/export/data/InternatsplaetzeStatistikExport';
import { SchuleStatistikExport } from '../../../asd/export/data/SchuleStatistikExport';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { SchuelerZahlenStatistikExport } from '../../../asd/export/data/SchuelerZahlenStatistikExport';
import { LehrerStatistikExport } from '../../../asd/export/data/LehrerStatistikExport';

export class StatistikExport extends JavaObject {

	/**
	 * Die Daten der Schule (B01).
	 */
	public schuleStatistikExport: SchuleStatistikExport = new SchuleStatistikExport();

	/**
	 * Die Religionszugehörigkeiten der Schueler (S42).
	 */
	public religionszugehoerigkeitenStatistikExport: List<ReligionszugehoerigkeitenStatistikExport> = new ArrayList<ReligionszugehoerigkeitenStatistikExport>();

	/**
	 * Vom Schulbesuch zurueckgestellte Kinder (S43).
	 */
	public vomSchulbesuchZurueckgestelltStatistikExport: VomSchulbesuchZurueckgestelltStatistikExport = new VomSchulbesuchZurueckgestelltStatistikExport();

	/**
	 * Die Summen der Abgänger (V51).
	 */
	public abgaengerStatistikExport: List<AbgaengerStatistikExport> = new ArrayList<AbgaengerStatistikExport>();

	/**
	 * Die Daten der Lehrer (L61).
	 */
	public lehrerStatistikExport: List<LehrerStatistikExport> = new ArrayList<LehrerStatistikExport>();

	/**
	 * Die Daten zum Unterricht (U71).
	 */
	public unterrichtsverteilungStatistikExport: List<UnterrichtsverteilungStatistikExport> = new ArrayList<UnterrichtsverteilungStatistikExport>();

	/**
	 * Die Daten der (Teil-) Klassen (K81).
	 */
	public klassenStatistikExport: List<KlassenStatistikExport> = new ArrayList<KlassenStatistikExport>();

	/**
	 * Die Summendaten der Schüler (K84).
	 */
	public schuelerZahlenStatistikExport: SchuelerZahlenStatistikExport = new SchuelerZahlenStatistikExport();

	/**
	 * Die durchschnittlichen Schülerzahlen der Klinikschule (K89).
	 */
	public klinikschuleStatistikExport: KlinikschuleStatistikExport = new KlinikschuleStatistikExport();

	/**
	 * Die Abiturprüfungsergebnisse (X93).
	 */
	public abiturpruefungsergebnisseStatistikExport: List<AbiturpruefungsergebnisseStatistikExport> = new ArrayList<AbiturpruefungsergebnisseStatistikExport>();

	/**
	 * Die Daten zu den Internatsplätzen der Schule (X97).
	 */
	public internatsplaetzeStatistikExport: InternatsplaetzeStatistikExport = new InternatsplaetzeStatistikExport();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.export.data.StatistikExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.export.data.StatistikExport'].includes(name);
	}

	public static readonly class = new Class<StatistikExport>('de.svws_nrw.asd.export.data.StatistikExport');

}

export function cast_de_svws_nrw_asd_export_data_StatistikExport(obj: unknown): StatistikExport {
	return obj as StatistikExport;
}
