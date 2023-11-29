import { JavaEnum } from '../../java/lang/JavaEnum';
import { JavaString } from '../../java/lang/JavaString';

export class ServerMode extends JavaEnum<ServerMode> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<ServerMode> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, ServerMode> = new Map<string, ServerMode>();

	/**
	 * STABLE: Es werden nur Funktionen angeboten, die als stabil und getestet angesehen werden
	 */
	public static readonly STABLE : ServerMode = new ServerMode("STABLE", 0, "stable");

	/**
	 * BETA: Es werden auch Funktionen angeboten, welche als weitgehend stabil angesegen werden, sich aber noch in der abschließenden Entwicklung befinden
	 */
	public static readonly BETA : ServerMode = new ServerMode("BETA", 1, "beta");

	/**
	 * ALPHA: Es werden auch Funktionen angeboten, welche noch nicht als stabil gelten, aber zumindest weitgehend implementiert sind
	 */
	public static readonly ALPHA : ServerMode = new ServerMode("ALPHA", 2, "alpha");

	/**
	 * DEV: Es werden alle Funktionen angeboten, auch wenn diese noch nicht stabil sind.
	 */
	public static readonly DEV : ServerMode = new ServerMode("DEV", 3, "dev");

	/**
	 * Der Server-Mode als Text, welche in textuellen Konfigurationen genutzt werden kann
	 */
	public readonly text : string;

	/**
	 * Erzeugt einen neuen Modus für die Aufzählung.
	 *
	 * @param text   der Text für den Modus
	 */
	private constructor(name : string, ordinal : number, text : string) {
		super(name, ordinal);
		ServerMode.all_values_by_ordinal.push(this);
		ServerMode.all_values_by_name.set(name, this);
		this.text = text;
	}

	public toString() : string {
		return this.text;
	}

	/**
	 * Prüft, ob der Modus, in welchem der Server betrieben wird ausreicht, um
	 * eine Funktion, welche in diesem Modus unterstützt werden soll,
	 * zu unterstützen oder nicht.
	 *
	 * @param serverMode   der Modus, in welchem der Server betrieben wird
	 *
	 * @return true, falls die Nutzung erlaubt ist, ansonsten false
	 */
	public checkServerMode(serverMode : ServerMode | null) : boolean {
		return (serverMode as unknown === ServerMode.STABLE as unknown) && (this as unknown === ServerMode.STABLE as unknown) || (serverMode as unknown === ServerMode.BETA as unknown) && (this as unknown !== ServerMode.DEV as unknown) && (this as unknown !== ServerMode.ALPHA as unknown) || (serverMode as unknown === ServerMode.ALPHA as unknown) && (this as unknown !== ServerMode.DEV as unknown) || (serverMode as unknown === ServerMode.DEV as unknown);
	}

	/**
	 * Liefert das {@link ServerMode}-Objekt anhand des übergebenen Textes.
	 * Der Vergleich des Textes erfolgt dabei case-insensitive. Ist der übergebene
	 * Wert ungültig, so wird als Default STABLE zurückgegeben, so dass ein
	 * Rückgabewert für die Methode garantiert werden kann.
	 *
	 * @param text   Der Text des Server-Modes.
	 *
	 * @return das {@link ServerMode}-Objekt anhand des übergebenen Textes.
	 */
	public static getByText(text : string | null) : ServerMode {
		for (const mode of this.values())
			if (JavaString.equalsIgnoreCase(mode.text, text))
				return mode;
		return ServerMode.STABLE;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<ServerMode> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : ServerMode | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.ServerMode', 'java.lang.Enum'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_ServerMode(obj : unknown) : ServerMode {
	return obj as ServerMode;
}
