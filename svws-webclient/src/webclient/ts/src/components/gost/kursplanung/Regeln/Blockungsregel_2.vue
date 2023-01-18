<template>
	<BlockungsregelBase v-model="regel" @update:model-value="e => emit('update:modelValue', e)" :regel-typ="regel_typ" :regeln="regeln"
		@regel-hinzugefuegen="regel_hinzufuegen" @regel-speichern="emit('regelSpeichern')" @regel-entfernen="e=>emit('regelEntfernen', e)">
		<template #beschreibung="{ regel: r }">
			{{ getKursbezeichnung(r) }} auf Schiene {{ r.parameter.get(1) }} fixiert
		</template>
		Fixiere
		<parameter-kurs v-model="kurs" :data-faecher="dataFaecher" :blockung="blockung" />
		in
		<parameter-schiene v-model="schiene" :blockung="blockung" />
	</BlockungsregelBase>
</template>

<script setup lang="ts">

	import { GostBlockungRegel, GostKursblockungRegelTyp } from "@svws-nrw/svws-core-ts";
	import { computed, WritableComputedRef } from "vue";
	import { DataGostFaecher } from "~/apps/gost/DataGostFaecher";
	import { DataGostKursblockung } from "~/apps/gost/DataGostKursblockung";
	import { createKursbezeichnungsGetter, useRegelParameterKurs, useRegelParameterSchiene  } from '../composables';
	import { useKurse } from '../composables'

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

	const regel_typ = GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE

	const kurs = useRegelParameterKurs(props.blockung, regel, 0)
	const schiene = useRegelParameterSchiene(props.blockung, regel, 1)


	const kurse = useKurse(props.blockung)
	const regel_hinzufuegen = (r: GostBlockungRegel) => {
		r.parameter.add(kurse.value.get(0).id)
		r.parameter.add(1);
		regel.value = r;
	}

	const getKursbezeichnung = createKursbezeichnungsGetter(props.blockung)

</script>
