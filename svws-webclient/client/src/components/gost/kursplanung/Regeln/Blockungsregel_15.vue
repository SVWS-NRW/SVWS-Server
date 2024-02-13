<template>
	<BlockungsregelBase v-model="regel" @update:model-value="e => emit('update:modelValue', e)" :regel-typ="regel_typ" :regeln="regeln" :get-ergebnismanager="getErgebnismanager"
		@regel-hinzugefuegen="regel_hinzufuegen" @regel-speichern="emit('regelSpeichern')" @regel-entfernen="e=>emit('regelEntfernen', e)" :disabled="disabled" :cols="cols">
		<template #regelRead="{regel: r}">
			<div class="svws-ui-td" role="cell">
				{{ getDatenmanager().kursGetName(r.parameter.get(0)) }}
			</div>
			<div class="svws-ui-td" role="cell">
				{{ r.parameter.get(1) }}
			</div>
		</template>
		<template #regelEdit>
			<parameter-kurs v-model="kurs1" :map-faecher="mapFaecher" :kurse="kurse_filtered" label="Kurs hat" />
			<svws-ui-input-number placeholder="maximale Schülerzahl" v-model="anzahl" :min="0" :max="100" />
		</template>
	</BlockungsregelBase>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { DataTableColumn } from "@ui";
	import type { GostBlockungsergebnisManager, List, GostFach, GostBlockungsdatenManager, GostBlockungRegel } from "@core";
	import { GostBlockungKurs, ArrayList, GostKursblockungRegelTyp } from "@core";

	const props = defineProps<{
		getErgebnismanager: () => GostBlockungsergebnisManager;
		getDatenmanager: () => GostBlockungsdatenManager;
		modelValue: GostBlockungRegel | undefined;
		mapFaecher: Map<number, GostFach>;
		regeln: GostBlockungRegel[];
		disabled: boolean;
	}>();

	const emit = defineEmits<{
		(e: 'update:modelValue', v: GostBlockungRegel | undefined): void;
		(e: 'regelSpeichern'): void;
		(e: 'regelEntfernen', v: GostBlockungRegel): void;
	}>()

	const regel = computed<GostBlockungRegel | undefined>({
		get: () => props.modelValue,
		set: (value) => emit('update:modelValue', value)
	});

	const regel_typ = GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL;

	const kurse_filtered = computed<List<GostBlockungKurs>>(() => {
		const usedIDs = new Set<number>();
		for (const r of props.regeln)
			usedIDs.add(r.parameter.get(0));
		const result = new ArrayList<GostBlockungKurs>();
		for (const k of props.getDatenmanager().kursGetListeSortiertNachKursartFachNummer())
			if (!usedIDs.has(k.id))
				result.add(k);
		return result;
	});

	const kurs1 = computed<GostBlockungKurs>({
		get: () => {
			if (regel.value === undefined)
				return new GostBlockungKurs();
			const kurs = props.getDatenmanager().kursGet(regel.value.parameter.get(0));
			return kurs || new GostBlockungKurs()
		},
		set: (value) => {
			if (regel.value !== undefined)
				regel.value.parameter.set(0, value.id)
		}
	});

	const anzahl = computed<number>({
		get: () => {
			if (regel.value === undefined)
				return 0;
			return regel.value.parameter.get(1);
		},
		set: (value) => {
			if (regel.value !== undefined)
				regel.value.parameter.set(1, value)
		}
	})

	const regel_hinzufuegen = (r: GostBlockungRegel) => {
		if (kurse_filtered.value.isEmpty())
			return;
		r.parameter.add(kurse_filtered.value.get(0).id);
		r.parameter.add(1);
		regel.value = r;
	}

	const cols: DataTableColumn[] = [
		{key: 'kurs', label: 'Kurs hat'},
		{key: 'anzahl', label: 'maximale Schülerzahl' },
	]

</script>
