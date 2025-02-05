<template>
	<div class="page page-flex-row max-w-400">
		<Teleport to=".svws-sub-nav-target" v-if="isMounted && hatUpdateKompetenz">
			<svws-ui-sub-nav :focus-switching-enabled :focus-help-visible>
				<s-modal-laufbahnplanung-alle-fachwahlen-loeschen :gost-jahrgangsdaten="jahrgangsdaten" :reset-fachwahlen="resetFachwahlenAlle" />
				<svws-ui-button :disabled="apiStatus.pending" type="transparent" title="Planung importieren" @click="showModalImport = true"><span class="icon i-ri-download-2-line" /> Importieren…</svws-ui-button>
				<s-laufbahnplanung-import-modal v-model:show="showModalImport" multiple :import-laufbahnplanung />
				<svws-ui-button :disabled="apiStatus.pending" type="transparent" title="Planung exportieren" @click="export_laufbahnplanung"><span class="icon i-ri-upload-2-line" />Exportiere {{ auswahl.length > 0 ? 'Auswahl':'alle' }}</svws-ui-button>
			</svws-ui-sub-nav>
		</Teleport>
		<Teleport to=".svws-ui-header--actions" v-if="isMounted">
			<svws-ui-button-select type="secondary" :dropdown-actions :disabled="apiStatus.pending">
				<template #icon> <svws-ui-spinner spinning v-if="apiStatus.pending" /> <span class="icon i-ri-printer-line" v-else /> </template>
			</svws-ui-button-select>
		</Teleport>
		<div class="min-w-120 h-full flex flex-col">
			<div class="flex flex-wrap gap-x-10 gap-y-3 items-center justify-between mb-5 content-card--headline">
				<div class="flex flex-wrap gap-x-5">
					<svws-ui-checkbox type="toggle" :model-value="filterFehler()" @update:model-value="setFilterFehler">Nur Fehler</svws-ui-checkbox>
					<svws-ui-checkbox type="toggle" :model-value="filterExterne()" @update:model-value="setFilterExterne">Externe ausblenden</svws-ui-checkbox>
				</div>
				<svws-ui-radio-group class="radio--row">
					<svws-ui-radio-option v-model="art" value="ef1" name="ef1" label="EF.1" />
					<svws-ui-radio-option v-model="art" value="gesamt" name="gesamt" label="Gesamt" />
				</svws-ui-radio-group>
			</div>
			<svws-ui-table :items="filtered" :no-data="filtered.isEmpty()" no-data-html="Keine Laufbahnfehler gefunden."
				clickable :clicked="schueler" @update:clicked="schueler=$event" :columns selectable v-model="auswahl" scroll>
				<template #header(linkToSchueler)>
					<span class="icon i-ri-group-line" />
				</template>
				<template #header(ergebnis)>
					<svws-ui-tooltip class="w-6">
						<span class="icon i-ri-alert-line -my-1 -mx-0.5" />
						<template #content>
							Anzahl der Fehler insgesamt
						</template>
					</svws-ui-tooltip>
				</template>
				<template #cell(linkToSchueler)="{ rowData }">
					<button type="button" @click.stop="gotoLaufbahnplanung(rowData.schueler.id)" class="button button--icon" title="Zur Laufbahnplanung">
						<span class="icon i-ri-link" />
					</button>
				</template>
				<template #cell(name)="{rowData}">
					<span class="line-clamp-1 leading-tight -my-0.5 break-all">{{ rowData.schueler.nachname }}, {{ rowData.schueler.vorname }}</span>
					<span v-if="rowData.schueler.status !== 2" class="svws-ui-badge text-sm font-bold mt-0 ml-1 bg-ui-contrast-25">
						{{ SchuelerStatus.data().getWertByKuerzel("" + rowData.schueler.status)?.daten(schuljahr)?.text ?? '—' }}
					</span>
				</template>
				<template #cell(hinweise)="cell">
					<span v-if="counterAnzahlOderWochenstunden(cell.rowData.ergebnis.fehlercodes) > 0" class="opacity-75 -my-0.5"><span class="icon i-ri-information-line" /></span>
				</template>
				<template #cell(ergebnis)="{value: f}: {value: GostBelegpruefungErgebnis}">
					<span :class="counter(f.fehlercodes) === 0 ? 'opacity-25' : ''">{{ counter(f.fehlercodes) }}</span>
				</template>
			</svws-ui-table>
		</div>
		<div v-if="!filtered.isEmpty() && schueler" class="min-w-120 h-full overflow-y-hidden flex flex-col">
			<div class="flex flex-row w-full mb-2">
				<div class="grow">
					<svws-ui-tooltip :indicator="false">
						<span class="text-headline-md" title="Zur Laufbahnplanung">{{ `${schueler?.schueler?.vorname} ${schueler?.schueler?.nachname}` }}</span>
						<template #content>
							ID: {{ schueler?.schueler?.id || '' }}
						</template>
					</svws-ui-tooltip>
				</div>
				<div>
					<div class="flex flex-col">
						<svws-ui-button type="transparent" @click="gotoLaufbahnplanung(schueler?.schueler?.id || 0)"><span class="icon i-ri-link" />Zur Laufbahnplanung</svws-ui-button>
						<svws-ui-button type="transparent" @click="gotoSprachenfolge(schueler?.schueler?.id || 0)"><span class="icon i-ri-link" />Zur Sprachenfolge</svws-ui-button>
					</div>
				</div>
			</div>
			<div class="h-full grow overflow-y-auto flex flex-col">
				<div class="pb-2">
					<s-laufbahnplanung-fehler :fehlerliste="() => schueler.ergebnis.fehlercodes" :belegpruefungs-art="gostBelegpruefungsArt" />
				</div>
				<div>
					<s-laufbahnplanung-informationen :fehlerliste="() => schueler.ergebnis.fehlercodes" :belegpruefungs-art="gostBelegpruefungsArt" />
				</div>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref, toRaw, onMounted } from 'vue';
	import type { GostLaufbahnfehlerProps } from "./SGostLaufbahnfehlerProps";
	import type { DataTableColumn } from '@ui';
	import type { List, GostBelegpruefungErgebnisFehler, GostBelegpruefungErgebnis} from '@core';
	import { ArrayList, GostBelegpruefungsArt, GostBelegungsfehlerArt, SchuelerStatus, GostBelegpruefungsErgebnisse, BenutzerKompetenz } from '@core';
	import { useRegionSwitch } from "~/components/useRegionSwitch";

	const props = defineProps<GostLaufbahnfehlerProps>();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const schuljahr = computed<number>(() => props.jahrgangsdaten().abiturjahr - 1);

	const hatUpdateKompetenz = computed<boolean>(() => {
		return props.benutzerKompetenzen.has(BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN)
			|| (props.benutzerKompetenzen.has(BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)
				&& props.benutzerKompetenzenAbiturjahrgaenge.has(props.jahrgangsdaten().abiturjahr))
	});

	const columns: DataTableColumn[] = [
		{key: "linkToSchueler", label: " ", fixedWidth: 1.75, align: "center"},
		{key: 'name', label: 'Name, Vorname', span: 2},
		{key: 'hinweise', label: 'K/WS', tooltip: 'Gibt an, ob Hinweise zu der Anzahl von Kursen oder Wochenstunden vorliegen', fixedWidth: 3.5, align: 'center'},
		{key: 'ergebnis', label: 'Fehler', tooltip: 'Anzahl der Fehler insgesamt', fixedWidth: 3.5, align: 'right', sortable: true},
	];

	const showModalImport = ref<boolean>(false);
	const auswahl = ref<GostBelegpruefungsErgebnisse[]>([]);

	const hasFilter = computed<boolean>(() => props.filterFehler() || props.filterExterne());

	const filtered = computed<List<GostBelegpruefungsErgebnisse>>(() => {
		if (!hasFilter.value)
			return props.listBelegpruefungsErgebnisse();
		const a: List<GostBelegpruefungsErgebnisse> = new ArrayList();
		for (const e of props.listBelegpruefungsErgebnisse()) {
			if (props.filterFehler() && e.ergebnis.erfolgreich)
				continue;
			if ((props.filterExterne()) && (SchuelerStatus.data().getWertByKuerzel("" + e.schueler.status) === SchuelerStatus.EXTERN))
				continue;
			a.add(e);
		}
		return a;
	})

	const schueler_state = ref<GostBelegpruefungsErgebnisse>();
	const schueler = computed<GostBelegpruefungsErgebnisse>({
		get: () => (schueler_state.value !== undefined) && filtered.value.contains(toRaw(schueler_state.value))
			? schueler_state.value
			: filtered.value.isEmpty() ? new GostBelegpruefungsErgebnisse() : filtered.value.get(0),
		set: (value) => schueler_state.value = value,
	})

	const art = computed<'ef1'|'gesamt'|'auto'>({
		get: () => {
			return props.gostBelegpruefungsArt() === GostBelegpruefungsArt.EF1 ? 'ef1' : 'gesamt';
		},
		set: (value) => {
			if (value === 'auto')
				return;
			void props.setGostBelegpruefungsArt(value === 'ef1' ? GostBelegpruefungsArt.EF1 : GostBelegpruefungsArt.GESAMT);
		},
	});


	function counter(fehlercodes: List<GostBelegpruefungErgebnisFehler> | undefined): number {
		if (fehlercodes === undefined)
			return 0;
		let res = 0;
		for (const fehler of fehlercodes)
			if (GostBelegungsfehlerArt.fromKuerzel(fehler.art) !== GostBelegungsfehlerArt.HINWEIS)
				res++;
		return res;
	}

	function counterAnzahlOderWochenstunden(fehlercodes: List<GostBelegpruefungErgebnisFehler> | undefined) : number {
		if (fehlercodes === undefined)
			return 0;
		let res = 0;
		for (const fehler of fehlercodes)
			if ((GostBelegungsfehlerArt.fromKuerzel(fehler.art) === GostBelegungsfehlerArt.HINWEIS) &&
				(fehler.code.startsWith("WST") || fehler.code.startsWith("ANZ")))
				res++;
		return res;
	}

	const dropdownActions = computed(() => {
		return [
			{ text: `Laufbahnwahlbogen (gesamt)`, action: () => downloadPDF("Laufbahnwahlbogen", 1, false), default: true },
			{ text: `Laufbahnwahlbogen (einzeln)`, action: () => downloadPDF("Laufbahnwahlbogen", 1, true) },
			{ text: `Laufbahnwahlbogen (gesamt, nur Belegung)`, action: () => downloadPDF("Laufbahnwahlbogen (nur Belegung)", 0, false) },
			{ text: `Laufbahnwahlbogen (einzeln, nur Belegung)`, action: () => downloadPDF("Laufbahnwahlbogen (nur Belegung)", 0, true) },
			{ text: `Ergebnisliste (nur Summen)`, action: () => downloadPDF("Ergebnisliste Laufbahnwahlen", 0, false) },
			{ text: `Ergebnisliste (nur Summen und Fehler)`, action: () => downloadPDF("Ergebnisliste Laufbahnwahlen", 1, false) },
			{ text: `Ergebnisliste (vollständig)`, action: () => downloadPDF("Ergebnisliste Laufbahnwahlen", 2, false) },
		];
	});

	async function downloadPDF(title: string, detaillevel: number, einzelpdfs: boolean) {
		const list = new ArrayList<number>();
		if (auswahl.value.length > 0) {
			for (const e of filtered.value)
				if (auswahl.value.includes(e))
					list.add(e.schueler.id);
		}
		if (list.isEmpty())	{
			list.add(schueler.value.schueler.id);
		}
		const { data, name } = await props.getPdfLaufbahnplanung(title, list, detaillevel, einzelpdfs);
		const link = document.createElement("a");
		link.href = URL.createObjectURL(data);
		link.download = name;
		link.target = "_blank";
		link.click();
		URL.revokeObjectURL(link.href);
	}

	async function export_laufbahnplanung() {
		const list = new ArrayList<number>();
		const arr = auswahl.value.length > 0 ? auswahl.value : filtered.value;
		for (const s of arr)
			list.add(s.schueler.id);
		const { data, name } = await props.exportLaufbahnplanung(list);
		const link = document.createElement("a");
		link.href = URL.createObjectURL(data);
		link.download = name;
		link.target = "_blank";
		link.click();
		URL.revokeObjectURL(link.href);
	}

	const isMounted = ref(false);
	onMounted(() => isMounted.value = true);

</script>

<style lang="postcss" scoped>

	@reference "../../../../../ui/src/assets/styles/index.css"

	.page--content {
		@apply grid overflow-y-hidden overflow-x-auto h-full pb-3 pt-6 gap-x-8 lg:gap-x-12;
		grid-auto-rows: 100%;
		grid-template-columns: minmax(20rem, 0.5fr) 1fr;
		grid-auto-columns: max-content;
	}

	.scrollbar-thin {
		scrollbar-gutter: stable;
		scrollbar-width: thin;
		scrollbar-color: rgba(0,0,0,0.2) transparent;
	}

</style>
