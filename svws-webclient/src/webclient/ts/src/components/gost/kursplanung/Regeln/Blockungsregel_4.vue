<template>
	<BlockungsregelBase v-model="regel" @update:model-value="e => emit('update:modelValue', e)" :regel-typ="regel_typ" :regeln="regeln"
		@regel-hinzugefuegen="regel_hinzufuegen" @regel-speichern="emit('regelSpeichern')" @regel-entfernen="e=>emit('regelEntfernen', e)">
		<template #beschreibung="{ regel: r }">
			{{ name(r.parameter.get(0)) }} in {{ getKursbezeichnung(getKursFromId(kurse, r.parameter.get(1)), mapFaecher) }} fixiert
		</template>
		Fixiere
		<parameter-schueler v-model="schueler" :list-schueler="listSchueler" />
		in
		<parameter-kurs v-model="kurs" :map-faecher="mapFaecher" :kurse="kurse" />
	</BlockungsregelBase>
</template>

<script setup lang="ts">

	import { GostBlockungKurs, GostBlockungRegel, GostFach, GostKursblockungRegelTyp, SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, WritableComputedRef } from "vue";
	import { useRegelParameterKurs, useRegelParameterSchueler, getKursbezeichnung, getKursFromId } from '../composables';

	const props = defineProps<{
		modelValue: GostBlockungRegel | undefined;
		mapFaecher: Map<number, GostFach>;
		kurse: GostBlockungKurs[];
		listSchueler: SchuelerListeEintrag[];
		regeln: GostBlockungRegel[];
	}>();

	const emit = defineEmits<{
		(e: 'update:modelValue', v: GostBlockungRegel | undefined): void;
		(e: 'regelSpeichern'): void;
		(e: 'regelEntfernen', v: GostBlockungRegel): void;
	}>()

	const regel: WritableComputedRef<GostBlockungRegel | undefined> = computed({
		get: () => props.modelValue,
		set: (value) => emit('update:modelValue', value)
	});

	const regel_typ = GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS

	const schueler = useRegelParameterSchueler(props.listSchueler, regel, 0)
	const kurs = useRegelParameterKurs(props.kurse, regel, 1)

	const regel_hinzufuegen = (r: GostBlockungRegel) => {
		r.parameter.add(props.listSchueler[0].id);
		r.parameter.add(props.kurse[0].id);
		regel.value = r;
	}

	const name = (id: number) => {
		const schueler = props.listSchueler.find(s => s.id === id);
		return schueler ? `${schueler.nachname}, ${schueler.vorname}` : "";
	}

</script>

