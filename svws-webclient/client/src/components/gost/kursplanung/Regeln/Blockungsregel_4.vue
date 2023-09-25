<template>
	<BlockungsregelBase v-model="regel" @update:model-value="e => emit('update:modelValue', e)" :regel-typ="regel_typ" :regeln="regeln"
		@regel-hinzugefuegen="regel_hinzufuegen" @regel-speichern="emit('regelSpeichern')" @regel-entfernen="e=>emit('regelEntfernen', e)" :disabled="disabled">
		<template #beschreibung="{ regel: r }">
			{{ name(r.parameter.get(0)) }} in {{ getKursbezeichnung(getKursFromId(kurse, r.parameter.get(1)), mapFaecher) }} fixiert
		</template>
		Fixiere
		<parameter-schueler v-model="schueler" :map-schueler="mapSchueler" />
		in
		<parameter-kurs v-model="kurs" :map-faecher="mapFaecher" :kurse="kurse" />
	</BlockungsregelBase>
</template>

<script setup lang="ts">

	import type { GostBlockungKurs, GostBlockungRegel, GostFach, SchuelerListeEintrag } from "@core";
	import type { WritableComputedRef } from "vue";
	import { useRegelParameterKurs, useRegelParameterSchueler, getKursbezeichnung, getKursFromId } from '../composables';
	import { GostKursblockungRegelTyp } from "@core";
	import { computed } from "vue";

	const props = defineProps<{
		modelValue: GostBlockungRegel | undefined;
		mapFaecher: Map<number, GostFach>;
		kurse: Iterable<GostBlockungKurs>;
		mapSchueler: Map<number, SchuelerListeEintrag>;
		regeln: GostBlockungRegel[];
		disabled: boolean;
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

	// eslint-disable-next-line vue/no-setup-props-destructure
	const schueler = useRegelParameterSchueler(props.mapSchueler, regel, 0)
	// eslint-disable-next-line vue/no-setup-props-destructure
	const kurs = useRegelParameterKurs(props.kurse, regel, 1)

	const regel_hinzufuegen = (r: GostBlockungRegel) => {
		const [kurs] = props.kurse;
		r.parameter.add(props.mapSchueler.values().next().value.id);
		r.parameter.add(kurs.id);
		regel.value = r;
	}

	const name = (id: number) => {
		const schueler = props.mapSchueler.get(id);
		return schueler ? `${schueler.nachname}, ${schueler.vorname}` : "";
	}

</script>

