import type { Ref, WritableComputedRef } from 'vue';
import { computed } from 'vue';
import type { GostBlockungRegel } from "@core";
import { GostBlockungKurs, GostBlockungSchiene, GostKursart, SchuelerListeEintrag, GostFach } from "@core";

export function getKursFromId(kurse: Iterable<GostBlockungKurs>, kursId: number): GostBlockungKurs {
	for (const kurs of kurse)
		if (kurs.id === kursId)
			return kurs;
	return new GostBlockungKurs();
}

export function getKursbezeichnung(kurs: GostBlockungKurs, mapFaecher: Map<number, GostFach>) {
	const kuerzel = mapFaecher.get(kurs.fach_id)?.kuerzelAnzeige;
	const kursart = kurs.kursart > 0 ? GostKursart.fromID(kurs.kursart) : 'kursart-fehlt';
	const suffix = kurs.suffix ? "-" + kurs.suffix : "";
	return `${kuerzel}-${kursart}${kurs.nummer}${suffix}`
}

export function useRegelParameterKursart(regel: Ref<GostBlockungRegel | undefined>, parameter: number): WritableComputedRef<GostKursart> {
	return computed({
		get: () => regel.value === undefined ? GostKursart.LK : GostKursart.fromID(regel.value.parameter.get(parameter)),
		set: (value) => { if (regel.value) regel.value.parameter.set(parameter, value.id) }
	})
}

export function useRegelParameterKurs(kurse: Iterable<GostBlockungKurs>, regel: Ref<GostBlockungRegel | undefined>, parameter: number): WritableComputedRef<GostBlockungKurs> {
	return computed({
		get: () => {
			for (const kurs of kurse)
				if (kurs.id === regel.value?.parameter.get(parameter))
					return kurs;
			return new GostBlockungKurs();
		},
		set: (value) => { if (regel.value) regel.value.parameter.set(parameter, value.id)	}
	})
}

export function useRegelParameterFach(faecher: Iterable<GostFach> | Map<number, GostFach>, regel: Ref<GostBlockungRegel | undefined>, parameter: number): WritableComputedRef<GostFach> {
	return computed({
		get: () => {
			if (faecher instanceof Map)
				faecher = faecher.values();
			for (const fach of faecher)
				if (fach.id === regel.value?.parameter.get(parameter))
					return fach;
			return new GostFach();
		},
		set: (value) => { if (regel.value) regel.value.parameter.set(parameter, value.id)	}
	})
}

export function useRegelParameterSchiene(schienen: Iterable<GostBlockungSchiene>, regel: Ref<GostBlockungRegel | undefined>, parameter: number): WritableComputedRef<GostBlockungSchiene> {
	return computed({
		get: () => {
			for (const schiene of schienen)
				if (schiene.nummer === regel.value?.parameter.get(parameter))
					return schiene;
			return new GostBlockungSchiene();
		},
		set: (value) => { if (regel.value) regel.value.parameter.set(parameter, value.nummer) }
	})
}

export function useRegelParameterSchueler (mapSchueler: Map<number, SchuelerListeEintrag>, regel: Ref<GostBlockungRegel | undefined>, parameter: number): WritableComputedRef<SchuelerListeEintrag> {
	return computed({
		get: () => {
			if (regel.value === undefined)
				return new SchuelerListeEintrag();
			return mapSchueler.get(regel.value.parameter.get(parameter)) || new SchuelerListeEintrag();
		},
		set: (value) => {
			if (regel.value)
				regel.value.parameter.set(parameter, value.id)
		}
	})
}