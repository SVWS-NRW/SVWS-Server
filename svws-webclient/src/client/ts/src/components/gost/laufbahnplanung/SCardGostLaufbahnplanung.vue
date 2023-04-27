<template>
	<svws-ui-content-card>
		<svws-ui-toggle v-model="filterFehler" /> Nur Ergebnisse mit Fehlern anzeigen
		<s-laufbahnplanung-belegpruefungsart :model-value="belegpruefungsart()" @update:model-value="setBelegpruefungsart($event)" />
		<svws-ui-data-table :items="filtered" :no-data="false" clickable :clicked="schueler" @update:clicked="schueler=$event" :columns="cols">
			<template #cell(schueler)="{ value: s}">
				{{ s.nachname }}, {{ s.vorname }}
			</template>
		</svws-ui-data-table>
	</svws-ui-content-card>
	<svws-ui-content-card>
		<s-laufbahnplanung-fehler :fehlerliste="schueler.ergebnis.fehlercodes" />
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import type { GostBelegpruefungsErgebnisse, List } from '@svws-nrw/svws-core';
	import { ArrayList } from '@svws-nrw/svws-core';
	import type { GostBelegpruefungsArt } from '@svws-nrw/svws-core';
	import type { ComputedRef, WritableComputedRef} from 'vue';
	import { toRaw} from 'vue';
	import { computed, ref } from 'vue';
	import type { Config } from '~/components/Config';

	const props = defineProps<{
		config: Config;
		listBelegpruefungsErgebnisse: () =>List<GostBelegpruefungsErgebnisse>;
		belegpruefungsart: () => GostBelegpruefungsArt;
		setBelegpruefungsart: (belegpruefungsart: GostBelegpruefungsArt) => Promise<void>;
	}>();

	const cols = [{key: 'schueler', label: 'Name, Vorname'}]

	const filtered: ComputedRef<List<GostBelegpruefungsErgebnisse>> = computed(()=>{
		if (!filterFehler.value)
			return props.listBelegpruefungsErgebnisse();
		const a: List<GostBelegpruefungsErgebnisse> = new ArrayList();
		for (const e of props.listBelegpruefungsErgebnisse())
			if (e.ergebnis.erfolgreich === false)
				a.add(e);
		return a;
	})

	const schueler_state = ref();
	const schueler: WritableComputedRef<GostBelegpruefungsErgebnisse> = computed({
		get: () => schueler_state.value && filtered.value.contains(toRaw(schueler_state.value))
			? schueler_state.value
			: filtered.value.get(0),
		set: (value) => schueler_state.value = value
	})

	const filterFehler: WritableComputedRef<boolean> = computed({
		get: () => props.config.getValue('gost.laufbahnplanung.filterFehler') === 'true',
		set: (value) =>	void props.config.setValue('gost.laufbahnplanung.filterFehler', value === true ? 'true':'false')
	});
</script>
