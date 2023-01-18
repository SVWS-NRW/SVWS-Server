<template>
	<BlockungsregelBase v-model="regel" @update:model-value="e => emit('update:modelValue', e)" :regel-typ="regel_typ" :regeln="regeln"
		@regel-hinzugefuegen="regel_hinzufuegen" @regel-speichern="emit('regelSpeichern')" @regel-entfernen="e=>emit('regelEntfernen', e)">
		<template #beschreibung="{ regel: r }">
			{{ kursbezeichnung1(r) }} nie zusammen mit {{ kursbezeichnung2(r) }}
		</template>
		<parameter-kurs v-model="kurs1" :data-faecher="dataFaecher" :blockung="blockung" />
		immer zusammen mit
		<parameter-kurs v-model="kurs2" :data-faecher="dataFaecher" :blockung="blockung" />
	</BlockungsregelBase>
</template>

<script setup lang="ts">

	import { GostBlockungRegel, GostKursblockungRegelTyp } from "@svws-nrw/svws-core-ts";
	import { WritableComputedRef, computed } from "vue";
	import { DataGostFaecher } from "~/apps/gost/DataGostFaecher";
	import { DataGostKursblockung } from "~/apps/gost/DataGostKursblockung";
	import { useKurse, useRegelParameterKurs, createKursbezeichnungsGetter } from '../composables';

	const props = defineProps<{
		modelValue: GostBlockungRegel | undefined;
		dataFaecher: DataGostFaecher;
		blockung: DataGostKursblockung;
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

	const regel_typ = GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS

	const kurse = useKurse(props.blockung)

	const kurs1 = useRegelParameterKurs(props.blockung, regel, 0)
	const kurs2 = useRegelParameterKurs(props.blockung, regel, 1)

	const regel_hinzufuegen = (r: GostBlockungRegel) => {
		r.parameter.add(kurse.value.get(0).id)
		r.parameter.add(kurse.value.get(0).id);
		regel.value = r;
	}

	const kursbezeichnung1 = createKursbezeichnungsGetter(props.blockung, 0)
	const kursbezeichnung2 = createKursbezeichnungsGetter(props.blockung, 1)

</script>
