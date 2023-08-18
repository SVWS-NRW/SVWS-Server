<template>
	<div class="flex flex-col pt-7">
		<Blockungsregel_1 v-model="regel" :schienen="schienen" :regeln="regeln[1].value" @regel-speichern="regelSpeichern" @regel-entfernen="regelEntfernen" :disabled="disabled" />
		<Blockungsregel_6 v-model="regel" :schienen="schienen" :regeln="regeln[6].value" @regel-speichern="regelSpeichern" @regel-entfernen="regelEntfernen" :disabled="disabled" />
		<Blockungsregel_2 v-model="regel" :kurse="kurse" :schienen="schienen" :map-faecher="mapFaecher" :regeln="regeln[2].value" @regel-speichern="regelSpeichern" @regel-entfernen="regelEntfernen" :disabled="disabled" />
		<Blockungsregel_3 v-model="regel" :kurse="kurse" :schienen="schienen" :map-faecher="mapFaecher" :regeln="regeln[3].value" @regel-speichern="regelSpeichern" @regel-entfernen="regelEntfernen" :disabled="disabled" />
		<Blockungsregel_7 v-model="regel" :kurse="kurse" :map-faecher="mapFaecher" :regeln="regeln[7].value" @regel-speichern="regelSpeichern" @regel-entfernen="regelEntfernen" :disabled="disabled" />
		<Blockungsregel_8 v-model="regel" :kurse="kurse" :map-faecher="mapFaecher" :regeln="regeln[8].value" @regel-speichern="regelSpeichern" @regel-entfernen="regelEntfernen" :disabled="disabled" />
		<Blockungsregel_9 v-model="regel" :get-datenmanager="getDatenmanager" :map-faecher="mapFaecher" :regeln="regeln[9].value" @regel-speichern="regelSpeichern" @regel-entfernen="regelEntfernen" :disabled="disabled" />
		<Blockungsregel_4 v-model="regel" :kurse="kurse" :map-faecher="mapFaecher" :map-schueler="mapSchueler" :regeln="regeln[4].value" @regel-speichern="regelSpeichern" @regel-entfernen="regelEntfernen" :disabled="disabled" />
		<Blockungsregel_5 v-model="regel" :kurse="kurse" :map-faecher="mapFaecher" :map-schueler="mapSchueler" :regeln="regeln[5].value" @regel-speichern="regelSpeichern" @regel-entfernen="regelEntfernen" :disabled="disabled" />
		<Blockungsregel_10 v-model="regel" :regeln="regeln[10].value" @regel-speichern="regelSpeichern" @regel-entfernen="regelEntfernen" :disabled="disabled" />
	</div>
</template>

<script setup lang="ts">

	import type { GostBlockungKurs, GostBlockungRegel, GostBlockungSchiene, GostBlockungsdatenManager, GostFach, GostFaecherManager, SchuelerListeEintrag } from "@core";
	import type { ComputedRef, ShallowRef, WritableComputedRef } from 'vue';
	import { computed, shallowRef, ref } from 'vue';

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

	// eslint-disable-next-line vue/no-setup-props-destructure
	const disabled = ref<boolean>(props.getDatenmanager().ergebnisGetListeSortiertNachBewertung().size() !== 1);

	const schienen: ComputedRef<GostBlockungSchiene[]> = computed(() => {
		return props.getDatenmanager().schieneGetListe()?.toArray() as GostBlockungSchiene[];
	});

	const kurse: ComputedRef<GostBlockungKurs[]> = computed(() => {
		return props.getDatenmanager().kursGetListeSortiertNachKursartFachNummer()?.toArray() as GostBlockungKurs[];
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

	const alle_regeln: ComputedRef<GostBlockungRegel[]> = computed(() => props.getDatenmanager().regelGetListe().toArray() as GostBlockungRegel[]);
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
		const id = regel.value.id
		id > 0
			? await props.patchRegel(regel.value, id)
			: await props.addRegel(regel.value);
		regel.value = undefined;
	}

</script>
