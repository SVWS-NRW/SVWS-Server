<template>
	<svws-ui-content-card>
		<svws-ui-toggle v-model="filterFehler" /> Nur Ergebnisse mit Fehlern anzeigen
		<s-laufbahnplanung-belegpruefungsart v-model="art" no-auto />
		<svws-ui-data-table :items="filtered" :no-data="false" clickable :clicked="schueler" @update:clicked="schueler=$event" :columns="cols">
			<template #cell(schueler)="{value: s}: {value: Schueler}">
				<svws-ui-icon @click.stop="gotoLaufbahnplanung(s.id)" class="mr-2 text-primary hover:opacity-50 cursor-pointer"> <i-ri-link /> </svws-ui-icon>{{ s.nachname }}, {{ s.vorname }}
				<svws-ui-badge v-if="s.status !== 2" type="light" size="big" :short="true">
					{{ SchuelerStatus.fromID(s.status)?.bezeichnung }}
				</svws-ui-badge>
			</template>
			<template #cell(ergebnis)="{value: f}: {value: GostBelegpruefungErgebnis}">
				{{ counter(f.fehlercodes) }}
			</template>
		</svws-ui-data-table>
	</svws-ui-content-card>
	<svws-ui-content-card>
		<s-laufbahnplanung-fehler :fehlerliste="schueler.ergebnis.fehlercodes" :belegpruefungs-art="gostBelegpruefungsArt()" />
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import type { GostBelegpruefungsErgebnisse, List, Schueler, GostBelegpruefungErgebnisFehler, GostBelegpruefungErgebnis } from '@svws-nrw/svws-core';
	import { ArrayList, GostBelegungsfehlerArt, SchuelerStatus } from '@svws-nrw/svws-core';
	import type { ComputedRef, WritableComputedRef} from 'vue';
	import { toRaw} from 'vue';
	import { computed, ref } from 'vue';
	import type { Config } from '~/components/Config';

	const props = defineProps<{
		config: Config;
		listBelegpruefungsErgebnisse: () =>List<GostBelegpruefungsErgebnisse>;
		gostBelegpruefungsArt: () => 'ef1'|'gesamt';
		setGostBelegpruefungsArt: (value: 'ef1'|'gesamt') => Promise<void>;
		gotoLaufbahnplanung: (id: number) => Promise<void>;
	}>();

	const cols = [{key: 'schueler', label: 'Name, Vorname'}, {key: 'ergebnis', label: 'Anzahl Fehler'}]

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

	const art: WritableComputedRef<'ef1'|'gesamt'|'auto'> = computed({
		get: () => props.gostBelegpruefungsArt(),
		set: (value) => void props.setGostBelegpruefungsArt(value)
	});


	function counter(fehlercodes: List<GostBelegpruefungErgebnisFehler>): number {
		let res = 0;
		for (const fehler of fehlercodes)
			if (!!fehler &&
				(GostBelegungsfehlerArt.fromKuerzel(fehler.art) ===
					GostBelegungsfehlerArt.BELEGUNG ||
					GostBelegungsfehlerArt.fromKuerzel(fehler.art) ===
					GostBelegungsfehlerArt.SCHRIFTLICHKEIT))
				res++;
		return res;
	}
</script>
