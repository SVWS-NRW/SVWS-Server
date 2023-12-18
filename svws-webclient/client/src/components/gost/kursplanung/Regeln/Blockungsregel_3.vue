<template>
	<BlockungsregelBase v-model="regel" @update:model-value="e => emit('update:modelValue', e)" :regel-typ="regel_typ" :regeln="regeln" :get-ergebnismanager="getErgebnismanager"
		@regel-hinzugefuegen="regel_hinzufuegen" @regel-speichern="emit('regelSpeichern')" @regel-entfernen="e=>emit('regelEntfernen', e)" :disabled="disabled" :cols="cols">
		<template #regelRead="{regel: r}">
			<div class="svws-ui-td" role="cell">
				{{ getKursbezeichnung(getKursFromId(kurse, r.parameter.get(0)), mapFaecher) }}
			</div>
			<div class="svws-ui-td" role="cell">
				{{ r.parameter.get(1) }}
			</div>
		</template>
		<template #regelEdit>
			<parameter-kurs v-model="kurs" :map-faecher="mapFaecher" :kurse="kurse" label="Sperre" />
			<parameter-schiene v-model="schiene" :schienen="schienen" label="in Schiene" />
		</template>
	</BlockungsregelBase>
</template>

<script setup lang="ts">

	import type { GostBlockungKurs, GostBlockungRegel, GostBlockungSchiene, GostBlockungsergebnisManager, GostFach} from "@core";
	import type { WritableComputedRef } from "vue";
	import { getKursbezeichnung, useRegelParameterKurs, useRegelParameterSchiene, getKursFromId } from '../composables';
	import { GostKursblockungRegelTyp } from "@core";
	import {computed} from "vue";
	import type {DataTableColumn} from "@ui";

	const props = defineProps<{
		getErgebnismanager: () => GostBlockungsergebnisManager;
		modelValue: GostBlockungRegel | undefined;
		mapFaecher: Map<number, GostFach>;
		kurse: Iterable<GostBlockungKurs>;
		schienen: Iterable<GostBlockungSchiene>;
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

	const regel_typ = GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE

	// eslint-disable-next-line vue/no-setup-props-destructure
	const kurs = useRegelParameterKurs(props.kurse, regel, 0)
	// eslint-disable-next-line vue/no-setup-props-destructure
	const schiene = useRegelParameterSchiene(props.schienen, regel, 1)

	const regel_hinzufuegen = (r: GostBlockungRegel) => {
		const [kurs] = props.kurse;
		r.parameter.add(kurs.id);
		r.parameter.add(1)
		regel.value = r;
	}

	const cols: DataTableColumn[] = [
		{key: 'kursart', label: 'Kurs gesperrt'},
		{key: 'in', label: 'in Schiene'},
	]
</script>

