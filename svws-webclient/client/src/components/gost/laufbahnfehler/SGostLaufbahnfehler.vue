<template>
	<div class="page--content">
		<Teleport to=".svws-sub-nav-target" v-if="isMounted">
			<svws-ui-sub-nav>
				<s-modal-gost-laufbahnfehler-alle-fachwahlen-loeschen @delete="resetFachwahlenAlle" />
			</svws-ui-sub-nav>
		</Teleport>
		<Teleport to=".svws-ui-header--actions" v-if="isMounted">
			<svws-ui-button-select type="secondary" :dropdown-actions="dropdownList" :disabled="apiStatus.pending">
				<template #icon> <i-ri-printer-line /><svws-ui-spinner :spinning="apiStatus.pending" /> </template>
			</svws-ui-button-select>
		</Teleport>
		<svws-ui-content-card title="Laufbahnplanungen im Jahrgang">
			<div class="flex flex-wrap gap-x-10 gap-y-3 items-center justify-between mb-5 content-card--headline">
				<div class="flex flex-wrap gap-x-5">
					<svws-ui-checkbox type="toggle" v-model="filterFehler">Nur Fehler</svws-ui-checkbox>
					<svws-ui-checkbox type="toggle" v-model="filterExterne">Externe</svws-ui-checkbox>
				</div>
				<s-laufbahnplanung-belegpruefungsart v-model="art" no-auto />
			</div>
			<svws-ui-table :items="filtered" :no-data="filtered.isEmpty()" no-data-html="Keine Laufbahnfehler gefunden." clickable :clicked="schueler" @update:clicked="schueler=$event" :columns="cols">
				<template #header(linkToSchueler)>
					<i-ri-group-line />
				</template>
				<template #header(ergebnis)>
					<svws-ui-tooltip class="w-6">
						<i-ri-alert-line class="text-headline-md -my-1 -mx-0.5" />
						<template #content>
							Anzahl der Fehler insgesamt
						</template>
					</svws-ui-tooltip>
				</template>
				<template #cell(linkToSchueler)="{ rowData }">
					<button type="button" @click.stop="gotoLaufbahnplanung(rowData.schueler.id)" class="button button--icon" title="Zur Laufbahnplanung">
						<i-ri-link />
					</button>
				</template>
				<template #cell(name)="{rowData}">
					<span class="line-clamp-1 leading-tight -my-0.5 break-all">{{ rowData.schueler.nachname }}, {{ rowData.schueler.vorname }}</span>
					<span v-if="rowData.schueler.status !== 2" class="svws-ui-badge text-sm font-bold !mt-0 !ml-1 !bg-light dark:!bg-white/5">{{ SchuelerStatus.fromID(rowData.schueler.status)?.bezeichnung || "" }}</span>
				</template>
				<template #cell(hinweise)="cell">
					<span v-if="counterAnzahlOderWochenstunden(cell.rowData.ergebnis.fehlercodes) > 0" class="opacity-75 -my-0.5"><i-ri-information-line /></span>
				</template>
				<template #cell(ergebnis)="{value: f}: {value: GostBelegpruefungErgebnis}">
					<span :class="counter(f.fehlercodes) === 0 ? 'opacity-25' : ''">{{ counter(f.fehlercodes) }}</span>
				</template>
			</svws-ui-table>
		</svws-ui-content-card>
		<svws-ui-content-card v-if="!filtered.isEmpty() && schueler">
			<template #title>
				<svws-ui-tooltip :indicator="false">
					<span class="text-headline-md" title="Zur Laufbahnplanung">{{ `${schueler?.schueler?.vorname} ${schueler?.schueler?.nachname}` }}</span>
					<template #content>
						ID: {{ schueler?.schueler?.id || '' }}
					</template>
				</svws-ui-tooltip>
			</template>
			<template #actions>
				<svws-ui-button type="transparent" @click="gotoLaufbahnplanung(schueler?.schueler?.id || 0)"><i-ri-link />Zur Laufbahnplanung</svws-ui-button>
			</template>
			<s-laufbahnplanung-fehler :fehlerliste="() => schueler.ergebnis.fehlercodes" :belegpruefungs-art="gostBelegpruefungsArt" />
			<s-laufbahnplanung-informationen :fehlerliste="() => schueler.ergebnis.fehlercodes" :belegpruefungs-art="gostBelegpruefungsArt" />
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { List, GostBelegpruefungErgebnisFehler, GostBelegpruefungErgebnis} from '@core';
	import type { ComputedRef, WritableComputedRef} from 'vue';
	import type { GostLaufbahnfehlerProps } from "./SGostLaufbahnfehlerProps";
	import type { DataTableColumn } from '@ui';
	import { ArrayList, GostBelegpruefungsArt, GostBelegungsfehlerArt, SchuelerStatus, GostBelegpruefungsErgebnisse } from '@core';
	import { computed, ref, toRaw, onMounted } from 'vue';

	const props = defineProps<GostLaufbahnfehlerProps>();

	const cols: DataTableColumn[] = [
		{key: "linkToSchueler", label: " ", fixedWidth: 1.75, align: "center"},
		{key: 'name', label: 'Name, Vorname', span: 2},
		{key: 'hinweise', label: 'K/WS', tooltip: 'Gibt an, ob Hinweise zu der Anzahl von Kursen oder Wochenstunden vorliegen', fixedWidth: 3.5, align: 'center'},
		{key: 'ergebnis', label: 'Fehler', tooltip: 'Anzahl der Fehler insgesamt', fixedWidth: 3.5, align: 'right', sortable: true},
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


	function counter(fehlercodes: List<GostBelegpruefungErgebnisFehler> | undefined): number {
		if (fehlercodes === undefined)
			return 0;
		let res = 0;
		for (const fehler of fehlercodes)
			if (GostBelegungsfehlerArt.fromKuerzel(fehler.art) !== GostBelegungsfehlerArt.HINWEIS)
				res++;
		return res;
	}

	function counterHinweise(fehlercodes: List<GostBelegpruefungErgebnisFehler> | undefined): number {
		if (fehlercodes === undefined)
			return 0;
		let res = 0;
		for (const fehler of fehlercodes)
			if (GostBelegungsfehlerArt.fromKuerzel(fehler.art) === GostBelegungsfehlerArt.HINWEIS)
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

	const dropdownList = [
		{ text: "Laufbahnwahlbogen markierter Schüler", action: () => downloadPDF("Laufbahnwahlbogen", true), default: true },
		{ text: "Laufbahnwahlbogen gefilterte Schüler", action: () => downloadPDF("Laufbahnwahlbogen", false) },
		{ text: "Laufbahnwahlbogen (nur Belegung) markierter Schüler", action: () => downloadPDF("Laufbahnwahlbogen (nur Belegung)", true) },
		{ text: "Laufbahnwahlbogen (nur Belegung) gefilterte Schüler", action: () => downloadPDF("Laufbahnwahlbogen (nur Belegung)", false) },
	]

	async function downloadPDF(title: string, single: boolean) {
		const list = new ArrayList<number>();
		if (single)
			list.add(schueler.value.schueler.id);
		else
			for (const s of filtered.value)
				list.add(s.schueler.id);
		const { data, name } = await props.getPdfWahlbogen(title, list);
		const link = document.createElement("a");
		link.href = URL.createObjectURL(data);
		link.download = name;
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
	grid-template-columns: minmax(20rem, 0.5fr) 1fr;
}
</style>
