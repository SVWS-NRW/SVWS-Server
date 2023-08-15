<template>
	<BlockungsregelBase v-model="regel" @update:model-value="e => emit('update:modelValue', e)" :regel-typ="regel_typ" :regeln="regeln"
		@regel-hinzugefuegen="regel_hinzufuegen" @regel-speichern="emit('regelSpeichern')" @regel-entfernen="e=>emit('regelEntfernen', e)" :disabled="disabled">
		<template #beschreibung="{ regel: r }">
			{{ getKursbezeichnung(getKursFromId(kurse, r.parameter.get(0)), mapFaecher) }} hat externe Schüler: {{ r.parameter.get(1) }}
		</template>
		<parameter-kurs v-model="kurs1" :map-faecher="mapFaecher" :kurse="kurse" />
		<span class="leading-none">hat externe Schüler: </span>
		<svws-ui-text-input placeholder="Anzahl" v-model="anzahl" type="number" />
	</BlockungsregelBase>
</template>

<script setup lang="ts">

	import type { GostBlockungKurs, GostBlockungRegel, GostFach} from "@core";
	import type { WritableComputedRef } from "vue";
	import { useRegelParameterKurs, getKursbezeichnung, getKursFromId } from '../composables';
	import { GostKursblockungRegelTyp } from "@core";
	import { computed } from "vue";

	const props = defineProps<{
		modelValue: GostBlockungRegel | undefined;
		mapFaecher: Map<number, GostFach>;
		kurse: GostBlockungKurs[];
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

	const regel_typ = GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN;

	const kurs1 = useRegelParameterKurs(props.kurse, regel, 0)
	const anzahl : WritableComputedRef<number> = computed({
		get: () => {
			if (regel.value === undefined)
				return 0;
			return regel.value.parameter.get(1);
		},
		set: (value) => {
			if (regel.value)
				regel.value.parameter.set(1, value)
		}
	})

	const regel_hinzufuegen = (r: GostBlockungRegel) => {
		r.parameter.add(props.kurse[0].id);
		r.parameter.add(props.kurse[0].id);
		regel.value = r;
	}

</script>
