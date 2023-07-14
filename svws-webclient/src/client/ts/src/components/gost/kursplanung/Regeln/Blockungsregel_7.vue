<template>
	<BlockungsregelBase v-model="regel" @update:model-value="e => emit('update:modelValue', e)" :regel-typ="regel_typ" :regeln="regeln"
		@regel-hinzugefuegen="regel_hinzufuegen" @regel-speichern="emit('regelSpeichern')" @regel-entfernen="e=>emit('regelEntfernen', e)" :disabled="disabled">
		<template #beschreibung="{ regel: r }">
			{{ getKursbezeichnung(getKursFromId(kurse, r.parameter.get(0)), mapFaecher) }} nie zusammen mit {{ getKursbezeichnung(getKursFromId(kurse, r.parameter.get(1)), mapFaecher) }}
		</template>
		<parameter-kurs v-model="kurs1" :map-faecher="mapFaecher" :kurse="kurse" />
		<span class="leading-none">nie zusammen mit</span>
		<parameter-kurs v-model="kurs2" :map-faecher="mapFaecher" :kurse="kurse" />
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

	const regel_typ = GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS

	const kurs1 = useRegelParameterKurs(props.kurse, regel, 0)
	const kurs2 = useRegelParameterKurs(props.kurse, regel, 1)

	const regel_hinzufuegen = (r: GostBlockungRegel) => {
		r.parameter.add(props.kurse[0].id);
		r.parameter.add(props.kurse[0].id);
		regel.value = r;
	}

</script>

