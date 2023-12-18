<template>
	<BlockungsregelBase v-model="regel" @update:model-value="e => emit('update:modelValue', e)" :regel-typ="regel_typ" :regeln="regeln" :get-ergebnismanager="getErgebnismanager"
		@regel-hinzugefuegen="regel_hinzufuegen" @regel-speichern="emit('regelSpeichern')" @regel-entfernen="e=>emit('regelEntfernen', e)" :disabled="disabled" :cols="cols">
		<template #regelRead="{regel: r}">
			<div class="svws-ui-td" role="cell">
				{{ name(r.parameter.get(0)) }}
			</div>
			<div class="svws-ui-td" role="cell">
				{{ getKursbezeichnung(getKursFromId(kurse, r.parameter.get(1)), mapFaecher) }}
			</div>
		</template>
		<template #regelEdit>
			<parameter-schueler v-model="schueler" :map-schueler="mapSchueler" label="Fixiere" />
			<parameter-kurs v-model="kurs" :map-faecher="mapFaecher" :kurse="kurse" label="in Kurs" />
		</template>
	</BlockungsregelBase>
</template>

<script setup lang="ts">

	import type { GostBlockungKurs, GostBlockungRegel, GostBlockungsergebnisManager, GostFach, SchuelerListeEintrag } from "@core";
	import type { WritableComputedRef } from "vue";
	import { useRegelParameterKurs, useRegelParameterSchueler, getKursbezeichnung, getKursFromId } from '../composables';
	import { GostKursblockungRegelTyp } from "@core";
	import {computed} from "vue";
	import type {DataTableColumn} from "@ui";

	const props = defineProps<{
		getErgebnismanager: () => GostBlockungsergebnisManager;
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

	const cols: DataTableColumn[] = [
		{key: 'schueler', label: 'Schüler fixiert'},
		{key: 'in', label: 'in Kurs'},
	]

</script>

