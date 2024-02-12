<template>
	<BlockungsregelBase v-model="regel" @update:model-value="e => emit('update:modelValue', e)" :regel-typ="regel_typ" :regeln="regeln" :get-ergebnismanager="getErgebnismanager"
		@regel-hinzugefuegen="regel_hinzufuegen" @regel-speichern="emit('regelSpeichern')" @regel-entfernen="e=>emit('regelEntfernen', e)" :disabled="disabled" :cols="cols">
		<template #regelRead="{regel: r}">
			<div class="svws-ui-td" role="cell">
				{{ name(r.parameter.get(0)) }}
			</div>
			<div class="svws-ui-td" role="cell">
				{{ name(r.parameter.get(1)) }}
			</div>
			<div class="svws-ui-td" role="cell">
				{{ getErgebnismanager().getFach(r.parameter.get(2)).bezeichnung ?? '??' }}
			</div>
		</template>
		<template #regelEdit>
			<parameter-schueler v-model="schueler1" :map-schueler="mapSchueler" label="Schüler zusammen" />
			<parameter-schueler v-model="schueler2" :map-schueler="mapSchueler" label="mit Schüler" />
			<parameter-fach v-model="fach" :map-faecher="mapFaecher" label="in Fach" />
		</template>
	</BlockungsregelBase>
</template>

<script setup lang="ts">

	import type { WritableComputedRef } from "vue";
	import { computed } from "vue";
	import type { DataTableColumn } from "@ui";
	import type { GostBlockungRegel, GostBlockungsergebnisManager, GostFach, SchuelerListeEintrag } from "@core";
	import { GostKursblockungRegelTyp } from "@core";
	import { useRegelParameterFach, useRegelParameterSchueler } from '../composables';

	const props = defineProps<{
		getErgebnismanager: () => GostBlockungsergebnisManager;
		modelValue: GostBlockungRegel | undefined;
		mapFaecher: Map<number, GostFach>;
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

	const regel_typ = GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH;

	// eslint-disable-next-line vue/no-setup-props-destructure
	const schueler1 = useRegelParameterSchueler(props.mapSchueler, regel, 0)
	// eslint-disable-next-line vue/no-setup-props-destructure
	const schueler2 = useRegelParameterSchueler(props.mapSchueler, regel, 1)
	// eslint-disable-next-line vue/no-setup-props-destructure
	const fach = useRegelParameterFach(props.mapFaecher, regel, 2)

	const regel_hinzufuegen = (r: GostBlockungRegel) => {
		r.parameter.add(props.mapSchueler.values().next().value.id);
		r.parameter.add(props.mapSchueler.values().next().value.id);
		r.parameter.add(props.mapFaecher.values().next().value.id);
		regel.value = r;
	}

	const name = (id: number) => {
		const schueler = props.mapSchueler.get(id);
		return schueler ? `${schueler.nachname}, ${schueler.vorname}` : "";
	}

	const cols: DataTableColumn[] = [
		{key: 'schueler', label: 'Schüler zusammen'},
		{key: 'schueler', label: 'mit Schüler'},
		{key: 'in', label: 'in Fach'},
	]

</script>

