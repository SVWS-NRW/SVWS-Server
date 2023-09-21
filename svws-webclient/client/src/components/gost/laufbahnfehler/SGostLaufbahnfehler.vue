<template>
	<div class="page--content">
		<Teleport to=".router-tab-bar--subnav-target" v-if="isMounted">
			<svws-ui-sub-nav>
				<s-modal-gost-laufbahnfehler-alle-fachwahlen-loeschen @delete="resetFachwahlenAlle" />
			</svws-ui-sub-nav>
		</Teleport>
		<Teleport to=".svws-ui-header--actions" v-if="isMounted">
			<svws-ui-button type="secondary" @click.prevent="download_file" title="Wahlbögen herunterladen" :disabled="apiStatus.pending">
				<i-ri-printer-line />Wahlbögen herunterladen <svws-ui-spinner :spinning="apiStatus.pending" />
			</svws-ui-button>
		</Teleport>
		<svws-ui-content-card title="Laufbahnfehler">
			<template #actions>
				<s-laufbahnplanung-belegpruefungsart v-model="art" no-auto />
			</template>
			<div class="flex flex-wrap gap-10 mb-3 -mt-3">
				<svws-ui-checkbox type="toggle" v-model="filterFehler">Nur Fehler</svws-ui-checkbox>
				<svws-ui-checkbox type="toggle" v-model="filterExterne">Keine Externe</svws-ui-checkbox>
			</div>
			<svws-ui-data-table :items="filtered" :no-data="filtered.isEmpty()" no-data-html="Keine Laufbahnfehler vorhanden." clickable :clicked="schueler" @update:clicked="schueler=$event" :columns="cols">
				<template #cell(schueler)="{value: s}: {value: Schueler}">
					<div class="flex gap-2 w-full">
						<div>{{ s.nachname }}, {{ s.vorname }}</div>
						<svws-ui-badge v-if="s.status !== 2" size="big" title="Status" class="-my-0.5 leading-none">
							{{ SchuelerStatus.fromID(s.status)?.bezeichnung }}
						</svws-ui-badge>
					</div>
				</template>
				<template #cell(ergebnis)="{value: f}: {value: GostBelegpruefungErgebnis}">
					<span :class="counter(f.fehlercodes) === 0 ? 'opacity-25' : ''">{{ counter(f.fehlercodes) }}</span>
				</template>
			</svws-ui-data-table>
		</svws-ui-content-card>
		<svws-ui-content-card v-if="!filtered.isEmpty()" :title="`Details zu Schüler ID: ${schueler.schueler.id}`" class="sticky top-8">
			<div class="text-headline inline-flex gap-x-5 -mt-3 flex-wrap" :class="counter(schueler.ergebnis.fehlercodes) === 0 ? 'mb-6' : 'mb-1'">
				<span>{{ `${schueler?.schueler?.vorname} ${schueler?.schueler?.nachname}` }}</span>
				<svws-ui-button type="secondary" @click.stop="gotoLaufbahnplanung(schueler.schueler.id)">
					<i-ri-link />
					Zur Laufbahnplanung
				</svws-ui-button>
			</div>
			<s-laufbahnplanung-fehler :fehlerliste="() => schueler.ergebnis.fehlercodes" :belegpruefungs-art="gostBelegpruefungsArt" />
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { List, Schueler, GostBelegpruefungErgebnisFehler, GostBelegpruefungErgebnis} from '@core';
	import { GostBelegpruefungsErgebnisse} from '@core';
	import type { ComputedRef, WritableComputedRef} from 'vue';
	import type { GostLaufbahnfehlerProps } from "./SGostLaufbahnfehlerProps";
	import type { DataTableColumn } from '@ui';
	import { ArrayList, GostBelegpruefungsArt, GostBelegungsfehlerArt, SchuelerStatus } from '@core';
	import { computed, ref, toRaw, onMounted } from 'vue';

	const props = defineProps<GostLaufbahnfehlerProps>();

	const cols: DataTableColumn[] = [
		{key: 'schueler', label: 'Name, Vorname', span: 1, sortable: true},
		{key: 'ergebnis', label: 'Fehler', tooltip: 'Anzahl der Fehler insgesamt', fixedWidth: 6, align: 'right', sortable: true}
	];

	const filtered: ComputedRef<List<GostBelegpruefungsErgebnisse>> = computed(()=>{
		if ((!filterFehler.value) && (!filterExterne.value))
			return props.listBelegpruefungsErgebnisse();
		const a: List<GostBelegpruefungsErgebnisse> = new ArrayList();
		for (const e of props.listBelegpruefungsErgebnisse()) {
			if (filterFehler.value && e.ergebnis.erfolgreich)
				continue;
			if ((filterExterne.value) && (SchuelerStatus.fromID(e.schueler.status) === SchuelerStatus.EXTERN))
				continue;
			a.add(e);
		}
		return a;
	})

	const schueler_state = ref();
	const schueler: WritableComputedRef<GostBelegpruefungsErgebnisse> = computed({
		get: () => schueler_state.value && filtered.value.contains(toRaw(schueler_state.value))
			? schueler_state.value
			: filtered.value.isEmpty() ? new GostBelegpruefungsErgebnisse() : filtered.value.get(0),
		set: (value) => schueler_state.value = value
	})

	const filterFehler: WritableComputedRef<boolean> = computed({
		get: () => props.config.getValue('gost.laufbahnfehler.filterFehler') === 'true',
		set: (value) =>	void props.config.setValue('gost.laufbahnfehler.filterFehler', value === true ? 'true' : 'false')
	});

	const filterExterne: WritableComputedRef<boolean> = computed({
		get: () => props.config.getValue('gost.laufbahnfehler.filterExterne') === 'true',
		set: (value) =>	void props.config.setValue('gost.laufbahnfehler.filterExterne', value === true ? 'true' : 'false')
	});

	const art: WritableComputedRef<'ef1'|'gesamt'|'auto'> = computed<'ef1'|'gesamt'|'auto'>({
		get: () => {
			return props.gostBelegpruefungsArt() === GostBelegpruefungsArt.EF1 ? 'ef1' : 'gesamt';
		},
		set: (value) => {
			if (value === 'auto')
				return;
			void props.setGostBelegpruefungsArt(value === 'ef1' ? GostBelegpruefungsArt.EF1 : GostBelegpruefungsArt.GESAMT);
		}
	});


	function counter(fehlercodes: List<GostBelegpruefungErgebnisFehler>): number {
		let res = 0;
		for (const fehler of fehlercodes)
			if (!!fehler &&
				(GostBelegungsfehlerArt.fromKuerzel(fehler.art) ===
					GostBelegungsfehlerArt.BELEGUNG ||
					GostBelegungsfehlerArt.fromKuerzel(fehler.art) ===
					GostBelegungsfehlerArt.SCHULSPEZIFISCH ||
					GostBelegungsfehlerArt.fromKuerzel(fehler.art) ===
					GostBelegungsfehlerArt.SCHRIFTLICHKEIT))
				res++;
		return res;
	}

	async function download_file() {
		const pdf = await props.getPdfWahlbogen();
		const link = document.createElement("a");
		link.href = URL.createObjectURL(pdf);
		link.download = `Laufbahnplanung_${props.abiturjahr}.pdf`;
		link.target = "_blank";
		link.click();
		URL.revokeObjectURL(link.href);
	}

	const isMounted = ref(false);
	onMounted(() => {
		isMounted.value = true;
	});

</script>

<style lang="postcss" scoped>
.page--content {
  @apply gap-y-3;
  grid-template-columns: minmax(20rem, 0.5fr) 1fr;
}
</style>
