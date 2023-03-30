
<template>
	<div class="space-y-3">
		<div class="rounded bg-white px-4 py-3 shadow-dark-20 shadow">
			<Blockungsregel_1 v-model="regel" :schienen="schienen" :regeln="regeln[1].value" @regel-speichern="regelSpeichern" @regel-entfernen="regelEntfernen" />
		</div>
		<div class="rounded bg-white px-4 py-3 shadow-dark-20 shadow">
			<Blockungsregel_6 v-model="regel" :schienen="schienen" :regeln="regeln[6].value" @regel-speichern="regelSpeichern" @regel-entfernen="regelEntfernen" />
		</div>
		<div class="rounded bg-white px-4 py-3 shadow-dark-20 shadow">
			<Blockungsregel_2 v-model="regel" :kurse="kurse" :schienen="schienen" :map-faecher="mapFaecher" :regeln="regeln[2].value" @regel-speichern="regelSpeichern" @regel-entfernen="regelEntfernen" />
		</div>
		<div class="rounded bg-white px-4 py-3 shadow-dark-20 shadow">
			<Blockungsregel_3 v-model="regel" :kurse="kurse" :schienen="schienen" :map-faecher="mapFaecher" :regeln="regeln[3].value" @regel-speichern="regelSpeichern" @regel-entfernen="regelEntfernen" />
		</div>
		<div class="rounded bg-white px-4 py-3 shadow-dark-20 shadow">
			<Blockungsregel_7 v-model="regel" :kurse="kurse" :map-faecher="mapFaecher" :regeln="regeln[7].value" @regel-speichern="regelSpeichern" @regel-entfernen="regelEntfernen" />
		</div>
		<div class="rounded bg-white px-4 py-3 shadow-dark-20 shadow">
			<Blockungsregel_8 v-model="regel" :kurse="kurse" :map-faecher="mapFaecher" :regeln="regeln[8].value" @regel-speichern="regelSpeichern" @regel-entfernen="regelEntfernen" />
		</div>
		<div class="rounded bg-white px-4 py-3 shadow-dark-20 shadow">
			<Blockungsregel_4 v-model="regel" :kurse="kurse" :map-faecher="mapFaecher" :map-schueler="mapSchueler" :regeln="regeln[4].value" @regel-speichern="regelSpeichern" @regel-entfernen="regelEntfernen" />
		</div>
		<div class="rounded bg-white px-4 py-3 shadow-dark-20 shadow">
			<Blockungsregel_5 v-model="regel" :kurse="kurse" :map-faecher="mapFaecher" :map-schueler="mapSchueler" :regeln="regeln[5].value" @regel-speichern="regelSpeichern" @regel-entfernen="regelEntfernen" />
		</div>
		<div class="rounded bg-white px-4 py-3 shadow-dark-20 shadow">
			<Blockungsregel_10 v-model="regel" :regeln="regeln[10].value" @regel-speichern="regelSpeichern" @regel-entfernen="regelEntfernen" />
		</div>
	</div>
</template>

<script setup lang="ts">

	import { GostBlockungKurs, GostBlockungRegel, GostBlockungSchiene, GostBlockungsdatenManager, GostFach, GostFaecherManager, SchuelerListeEintrag } from "@svws-nrw/svws-core";
	import { computed, ComputedRef, ShallowRef, shallowRef, WritableComputedRef } from 'vue';

	const props = defineProps<{
		getDatenmanager: () => GostBlockungsdatenManager;
		patchRegel: (data: GostBlockungRegel, id: number) => Promise<void>;
		addRegel: (regel: GostBlockungRegel) => Promise<GostBlockungRegel | undefined>;
		removeRegel: (id: number) => Promise<GostBlockungRegel | undefined>;
		faecherManager: GostFaecherManager;
		mapSchueler: Map<number, SchuelerListeEintrag>;
	}>();

	const mapFaecher: ComputedRef<Map<number, GostFach>> = computed(() => {
		const result = new Map<number, GostFach>();
		const faecher = props.faecherManager.faecher() || [];
		for (const fach of faecher)
			result.set(fach.id, fach);
		return result;
	});

	const schienen: ComputedRef<GostBlockungSchiene[]> = computed(() => {
		return props.getDatenmanager().getMengeOfSchienen()?.toArray() as GostBlockungSchiene[];
	});

	const kurse: ComputedRef<GostBlockungKurs[]> = computed(() => {
		return props.getDatenmanager().getKursmengeSortiertNachKursartFachNummer()?.toArray() as GostBlockungKurs[];
	});

	const _regel: ShallowRef<GostBlockungRegel | undefined> = shallowRef(undefined);

	const regel: WritableComputedRef<GostBlockungRegel|undefined> = computed({
		get: () => _regel.value,
		set: value => {
			_regel.value = value;
			if (value === undefined || value.id < 1)
				return;
			void props.patchRegel(value, value.id);
		}
	})

	const alle_regeln: ComputedRef<GostBlockungRegel[]> = computed(() => props.getDatenmanager().getMengeOfRegeln().toArray() as GostBlockungRegel[]);
	const regeln: ComputedRef<GostBlockungRegel[]>[] = [];
	for (let i = 1; i < 11; i++)
		regeln[i] = computed(() => alle_regeln.value.filter(r => r.typ === i));

	async function regelEntfernen(r: GostBlockungRegel) {
		await props.removeRegel(r.id);
		if (r.id === regel.value?.id)
			regel.value = undefined;
	}

	async function regelSpeichern() {
		if (regel.value === undefined)
			return;
		await props.addRegel(regel.value);
		regel.value = undefined;
	}

</script>
