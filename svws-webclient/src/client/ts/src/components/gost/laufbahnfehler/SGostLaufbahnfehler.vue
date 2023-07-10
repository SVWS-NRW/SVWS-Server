<template>
	<div class="page--content">
		<Teleport to=".router-tab-bar--subnav-target" v-if="isMounted">
			<svws-ui-sub-nav>
				<svws-ui-button size="small" type="transparent" @click.prevent="download_file" title="Wahlbögen herunterladen" :disabled="apiStatus.pending">
					Wahlbögen herunterladen <svws-ui-spinner :spinning="apiStatus.pending" />
				</svws-ui-button>
				<s-modal-gost-laufbahnfehler-alle-fachwahlen-loeschen @delete="resetFachwahlenAlle" />
			</svws-ui-sub-nav>
		</Teleport>
		<svws-ui-content-card>
			<div class="flex justify-between items-center gap-12 mb-4 mt-1">
				<s-laufbahnplanung-belegpruefungsart v-model="art" no-auto />
				<svws-ui-toggle v-model="filterExterne">keine Externen</svws-ui-toggle>
				<svws-ui-toggle v-model="filterFehler">Nur Fehler</svws-ui-toggle>
			</div>
			<div v-if="filtered.isEmpty()">
				Keine Laufbahnfehler vorhanden.
			</div>
			<svws-ui-data-table v-else :items="filtered" :no-data="false" clickable :clicked="schueler" @update:clicked="schueler=$event" :columns="cols">
				<template #cell(schueler)="{value: s}: {value: Schueler}">
					<svws-ui-button type="icon" size="small" @click.stop="gotoLaufbahnplanung(s.id)">
						<i-ri-link />
					</svws-ui-button>
					<div class="flex justify-between w-full ml-2">
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
		<svws-ui-content-card v-if="!filtered.isEmpty()" :title="`${schueler.schueler.vorname} ${schueler.schueler.nachname} (ID: ${schueler.schueler.id})`" class="sticky top-8" large-title>
			<template #actions>
				<svws-ui-button type="secondary" @click.stop="gotoLaufbahnplanung(schueler.schueler.id)">
					<i-ri-group-line />
					Zur Laufbahnplanung
				</svws-ui-button>
			</template>
			<s-laufbahnplanung-fehler :fehlerliste="() => schueler.ergebnis.fehlercodes" :belegpruefungs-art="gostBelegpruefungsArt" />
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { ComputedRef, WritableComputedRef} from 'vue';
	import { computed, ref, toRaw, onMounted } from 'vue';
	import type { GostBelegpruefungsErgebnisse, List, Schueler, GostBelegpruefungErgebnisFehler, GostBelegpruefungErgebnis} from '@core';
	import { ArrayList, GostBelegpruefungsArt, GostBelegungsfehlerArt, SchuelerStatus } from '@core';
	import type { GostLaufbahnfehlerProps } from "./SGostLaufbahnfehlerProps";
	import type { DataTableColumn } from '@ui';

	const props = defineProps<GostLaufbahnfehlerProps>();

	const cols: DataTableColumn[] = [{key: 'schueler', label: 'Name, Vorname', span: 1, sortable: true}, {key: 'ergebnis', label: 'Fehler', tooltip: 'Anzahl der Fehler insgesamt', fixedWidth: 6, align: 'right', sortable: true}];

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
			: filtered.value.get(0),
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
