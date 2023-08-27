<template>
	<BlockungsregelBase v-model="regel" @update:model-value="e => emit('update:modelValue', e)" :regel-typ="regel_typ" :regeln="regeln"
		@regel-hinzugefuegen="regel_hinzufuegen" @regel-speichern="emit('regelSpeichern')" @regel-entfernen="e=>emit('regelEntfernen', e)" :disabled="disabled">
		<template #beschreibung="{ regel: r }">
			{{ getDatenmanager().kursGetName(r.parameter.get(0)) }} hat externe Schüler: {{ r.parameter.get(1) }}
		</template>
		<parameter-kurs v-model="kurs1" :map-faecher="mapFaecher" :kurse="kurse_filtered.toArray() as GostBlockungKurs[]" />
		<span class="leading-none">hat externe Schüler: </span>
		<svws-ui-text-input placeholder="Anzahl" v-model="anzahl" type="number" />
	</BlockungsregelBase>
</template>

<script setup lang="ts">

	import type { WritableComputedRef } from "vue";
	import { GostBlockungKurs, type List, type GostBlockungRegel, type GostFach, type GostBlockungsdatenManager, ArrayList, GostKursblockungRegelTyp } from "@core";
	import { computed } from "vue";

	const props = defineProps<{
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

	const regel: WritableComputedRef<GostBlockungRegel | undefined> = computed({
		get: () => props.modelValue,
		set: (value) => emit('update:modelValue', value)
	});

	const regel_typ = GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN;

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

	const kurs1 : WritableComputedRef<GostBlockungKurs> = computed({
		get: () => {
			if (regel.value === undefined)
				return new GostBlockungKurs();
			const kurs = props.getDatenmanager().kursGet(regel.value.parameter.get(0));
			return kurs || new GostBlockungKurs()
		},
		set: (value) => {
			if (regel.value)
				regel.value.parameter.set(0, value.id)
		}
	});

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
		if (kurse_filtered.value.isEmpty())
			return;
		r.parameter.add(kurse_filtered.value.get(0).id);
		r.parameter.add(1);
		regel.value = r;
	}

</script>
