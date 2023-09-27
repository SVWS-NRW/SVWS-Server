<template>
	<BlockungsregelBase v-model="regel" @update:model-value="e => emit('update:modelValue', e)" :regel-typ="regel_typ" :regeln="regeln"
		@regel-hinzugefuegen="regel_hinzufuegen" @regel-speichern="emit('regelSpeichern')" @regel-entfernen="e=>emit('regelEntfernen', e)" :disabled="disabled" :cols="cols">
		<template #regelRead="{regel: r}">
			<div class="svws-ui-td" role="cell">
				{{ getKursbezeichnung(getKursFromId(kurse, r.parameter.get(0)), mapFaecher) }}
			</div>
			<div class="svws-ui-td" role="cell">
				{{ getKursbezeichnung(getKursFromId(kurse, r.parameter.get(1)), mapFaecher) }}
			</div>
		</template>
		<template #regelEdit>
			<parameter-kurs v-model="kurs1" :map-faecher="mapFaecher" :kurse="kurse" label="Kurs nie zusammen" />
			<parameter-kurs v-model="kurs2" :map-faecher="mapFaecher" :kurse="kurse" label="mit Kurs" />
		</template>
	</BlockungsregelBase>
</template>

<script setup lang="ts">

	import type { GostBlockungKurs, GostBlockungRegel, GostFach} from "@core";
	import type { WritableComputedRef } from "vue";
	import { useRegelParameterKurs, getKursbezeichnung, getKursFromId } from '../composables';
	import { GostKursblockungRegelTyp } from "@core";
	import {computed} from "vue";
	import type {DataTableColumn} from "@ui";

	const props = defineProps<{
		modelValue: GostBlockungRegel | undefined;
		mapFaecher: Map<number, GostFach>;
		kurse: Iterable<GostBlockungKurs>;
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

	// eslint-disable-next-line vue/no-setup-props-destructure
	const kurs1 = useRegelParameterKurs(props.kurse, regel, 0)
	// eslint-disable-next-line vue/no-setup-props-destructure
	const kurs2 = useRegelParameterKurs(props.kurse, regel, 1)

	const regel_hinzufuegen = (r: GostBlockungRegel) => {
		const [kurs] = props.kurse;
		r.parameter.add(kurs.id);
		r.parameter.add(kurs.id);
		regel.value = r;
	}

	const cols: DataTableColumn[] = [
		{key: 'kurs1', label: 'Kurs nie zusammen'},
		{key: 'kurs2', label: 'mit Kurs'},
	]

</script>

