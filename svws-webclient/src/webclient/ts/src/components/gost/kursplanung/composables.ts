import { List, GostBlockungKurs, Vector, GostBlockungSchiene, GostBlockungRegel, GostKursart, SchuelerListeEintrag } from '@svws-nrw/svws-core-ts';
import { ComputedRef, computed, Ref, WritableComputedRef } from 'vue';
import { injectMainApp } from '../../../apps/Main';
import { useSchuelerListe } from '../composables';

/**
 * Liste der Kurse für die aktuelle Kursblockung
 *
 * @export
 * @returns ComputedRef<List<GostBlockungKurs> | Vector<GostBlockungKurs>>
 */
export function useKurse(): ComputedRef<List<GostBlockungKurs> | Vector<GostBlockungKurs>> {
  const { apps: { gost } } = injectMainApp();
  return computed(()=> gost.dataKursblockung.datenmanager?.getKursmengeSortiertNachKursartFachNummer() || new Vector<GostBlockungKurs>())
}

export function useSchienen(): ComputedRef<List<GostBlockungSchiene> | Vector<GostBlockungSchiene>> {
	const { apps: { gost }} = injectMainApp();
	return computed(()=> gost.dataKursblockung.datenmanager?.getMengeOfSchienen() || new Vector<GostBlockungSchiene>())
}

/**
 *	Erzeugt Getter zum Auslesen der Kursbezeichung für einen Kurs.
 *  Beim Erzugen des Getters wird über `parameter` festlegt, in welchem Regelparameter der Kurs gespeichert ist
 *
 * @export
 * @param {number} [parameter=0]
 * @returns
 */
export function createKursbezeichnungsGetter(parameter: number = 0) {
	const { apps: { gost } } = injectMainApp();
	return  (regel: GostBlockungRegel): String => {
		const manager = gost.dataKursblockung.datenmanager
		if (manager === undefined)
			throw new Error("Der Kursblockungsmanager ist nicht verfügbar")
		const kurs = manager.getKurs(regel.parameter.get(parameter).valueOf())
		return manager.getNameOfKurs(kurs.id)
	}
}

export function useRegelParameterKursart(regel: Ref<GostBlockungRegel | undefined>, parameter: number): WritableComputedRef<GostKursart> {
	return computed({
		get(): GostKursart {
			const id = regel.value?.parameter.get(parameter)
			if (id)
				return GostKursart.fromID(id.valueOf())
			return GostKursart.LK
		},
		set(val: GostKursart) {
			console.log(regel.value?.parameter.get(parameter))
			if (regel.value)
				regel.value.parameter.set(parameter, val.id)	
		}
	})
}

export function useRegelParameterKurs(regel: Ref<GostBlockungRegel | undefined>, parameter: number): WritableComputedRef<GostBlockungKurs> {
  const kurse = useKurse()
	return computed({
		get(): GostBlockungKurs {
			for (const k of kurse.value)
				if (k.id === regel.value?.parameter.get(parameter))
					return k;
			return new GostBlockungKurs()
		},
		set(val: GostBlockungKurs) {
			if (regel.value)
				regel.value.parameter.set(parameter, val.id)	
		}
	})
}

export function useRegelParameterSchiene(regel: Ref<GostBlockungRegel | undefined>, parameter: number): WritableComputedRef<GostBlockungSchiene> {
	const schienen = useSchienen()
	return computed({
		get(): GostBlockungSchiene {
			for (const schiene of schienen.value)
				if (schiene.nummer === regel.value?.parameter.get(parameter))
					return schiene;
			return new GostBlockungSchiene()
		},
		set(val: GostBlockungSchiene) {
			if (regel.value)
				regel.value.parameter.set(parameter, val.nummer)	
		}
	})
}

export function useRegelParameterSchueler (regel: Ref<GostBlockungRegel | undefined>, parameter: number): WritableComputedRef<SchuelerListeEintrag> {
	const schuelerliste = useSchuelerListe()
  return computed({
		get(): SchuelerListeEintrag {
			for (const s of schuelerliste)
				if (s.id === regel.value?.parameter.get(parameter))
					return s;
			return new SchuelerListeEintrag()
		},
		set(val: SchuelerListeEintrag) {
			if (regel.value)
				regel.value.parameter.set(parameter, val.id)	
		}
	})
}