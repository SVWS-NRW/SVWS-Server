import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class KlassenZuwanderungsgeschichteStatistikExport extends JavaObject {

	/**
	 * Die Schüler mit Zuwanderungsgeschichte insgesamt.
	 */
	public zuwanderungsgeschichteInsgesamt: number = 0;

	/**
	 * Die Schüler mit eigenem Zuzug.
	 */
	public zuwanderungsgeschichteEigenerZuzug: number = 0;

	/**
	 * Die Schüler mit mindestens einem im Ausland geborenen Elternteil.
	 */
	public zuwanderungsgeschichteElternteilZugezogen: number = 0;

	/**
	 * Die Schüler mit nicht deutscher Verkehrssprache in der Familie.
	 */
	public zuwanderungsgeschichteNichtDeutscheVerkehrssprache: number = 0;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.export.data.KlassenZuwanderungsgeschichteStatistikExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.export.data.KlassenZuwanderungsgeschichteStatistikExport'].includes(name);
	}

	public static readonly class = new Class<KlassenZuwanderungsgeschichteStatistikExport>('de.svws_nrw.asd.export.data.KlassenZuwanderungsgeschichteStatistikExport');

	public static transpilerFromJSON(json: string): KlassenZuwanderungsgeschichteStatistikExport {
		const obj = JSON.parse(json) as Partial<KlassenZuwanderungsgeschichteStatistikExport>;
		const result = new KlassenZuwanderungsgeschichteStatistikExport();
		if (obj.zuwanderungsgeschichteInsgesamt === undefined)
			throw new Error('invalid json format, missing attribute zuwanderungsgeschichteInsgesamt');
		result.zuwanderungsgeschichteInsgesamt = obj.zuwanderungsgeschichteInsgesamt;
		if (obj.zuwanderungsgeschichteEigenerZuzug === undefined)
			throw new Error('invalid json format, missing attribute zuwanderungsgeschichteEigenerZuzug');
		result.zuwanderungsgeschichteEigenerZuzug = obj.zuwanderungsgeschichteEigenerZuzug;
		if (obj.zuwanderungsgeschichteElternteilZugezogen === undefined)
			throw new Error('invalid json format, missing attribute zuwanderungsgeschichteElternteilZugezogen');
		result.zuwanderungsgeschichteElternteilZugezogen = obj.zuwanderungsgeschichteElternteilZugezogen;
		if (obj.zuwanderungsgeschichteNichtDeutscheVerkehrssprache === undefined)
			throw new Error('invalid json format, missing attribute zuwanderungsgeschichteNichtDeutscheVerkehrssprache');
		result.zuwanderungsgeschichteNichtDeutscheVerkehrssprache = obj.zuwanderungsgeschichteNichtDeutscheVerkehrssprache;
		return result;
	}

	public static transpilerToJSON(obj: KlassenZuwanderungsgeschichteStatistikExport): string {
		let result = '{';
		result += '"zuwanderungsgeschichteInsgesamt" : ' + obj.zuwanderungsgeschichteInsgesamt.toString() + ',';
		result += '"zuwanderungsgeschichteEigenerZuzug" : ' + obj.zuwanderungsgeschichteEigenerZuzug.toString() + ',';
		result += '"zuwanderungsgeschichteElternteilZugezogen" : ' + obj.zuwanderungsgeschichteElternteilZugezogen.toString() + ',';
		result += '"zuwanderungsgeschichteNichtDeutscheVerkehrssprache" : ' + obj.zuwanderungsgeschichteNichtDeutscheVerkehrssprache.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<KlassenZuwanderungsgeschichteStatistikExport>): string {
		let result = '{';
		if (obj.zuwanderungsgeschichteInsgesamt !== undefined) {
			result += '"zuwanderungsgeschichteInsgesamt" : ' + obj.zuwanderungsgeschichteInsgesamt.toString() + ',';
		}
		if (obj.zuwanderungsgeschichteEigenerZuzug !== undefined) {
			result += '"zuwanderungsgeschichteEigenerZuzug" : ' + obj.zuwanderungsgeschichteEigenerZuzug.toString() + ',';
		}
		if (obj.zuwanderungsgeschichteElternteilZugezogen !== undefined) {
			result += '"zuwanderungsgeschichteElternteilZugezogen" : ' + obj.zuwanderungsgeschichteElternteilZugezogen.toString() + ',';
		}
		if (obj.zuwanderungsgeschichteNichtDeutscheVerkehrssprache !== undefined) {
			result += '"zuwanderungsgeschichteNichtDeutscheVerkehrssprache" : ' + obj.zuwanderungsgeschichteNichtDeutscheVerkehrssprache.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_export_data_KlassenZuwanderungsgeschichteStatistikExport(obj: unknown): KlassenZuwanderungsgeschichteStatistikExport {
	return obj as KlassenZuwanderungsgeschichteStatistikExport;
}
