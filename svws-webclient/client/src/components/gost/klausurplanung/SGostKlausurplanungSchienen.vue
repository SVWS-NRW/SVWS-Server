<template>
	<Teleport to=".svws-ui-header--actions" v-if="isMounted">
		<svws-ui-modal-hilfe class="ml-auto"> <s-gost-klausurplanung-schienen-hilfe /> </svws-ui-modal-hilfe>
	</Teleport>
	<svws-ui-modal :show="showModalAutomatischBlocken" size="small">
		<template #modalTitle>
			Automatisch blocken
		</template>
		<template #modalContent>
			<svws-ui-radio-group :row="true">
				<svws-ui-radio-option v-for="a in KlausurterminblockungAlgorithmen.values()" :key="a.id" :value="a" v-model="algMode" :name="a.bezeichnung" :label="a.bezeichnung" />
			</svws-ui-radio-group>
			<svws-ui-spacing />
			<svws-ui-radio-group :row="true">
				<svws-ui-radio-option v-for="k in KlausurterminblockungModusKursarten.values()" :key="k.id" :value="k" v-model="lkgkMode" :name="k.bezeichnung" :label="k.bezeichnung" />
			</svws-ui-radio-group>
			<svws-ui-spacing :size="2" />
			<svws-ui-checkbox type="toggle" v-model="blockeGleicheLehrkraft" v-if="algMode.__ordinal === KlausurterminblockungAlgorithmen.NORMAL.__ordinal" class="text-left">
				Gleicher Termin falls gleiche Lehrkraft, Fach und Kursart
			</svws-ui-checkbox>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="showModalAutomatischBlocken().value = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="primary" @click="blocken"> Blocken </svws-ui-button>
		</template>
	</svws-ui-modal>

	<div class="page--content page--content--full relative">
		<svws-ui-content-card>
			<template #title>
				<s-gost-klausurplanung-quartal-auswahl :quartalsauswahl="quartalsauswahl" />
			</template>
			<div class="flex flex-col" @drop="onDrop(undefined)" @dragover="$event.preventDefault()">
				<div class="text-headline-md mb-2">Planung</div>
				<svws-ui-table :items="props.kursklausurmanager().kursklausurOhneTerminGetMengeByQuartal(props.quartalsauswahl.value)" :columns="cols">
					<template #noData>
						<div class="leading-tight flex flex-col gap-0.5">
							<span>Aktuell keine Klausuren zu planen.</span>
							<span class="opacity-50">Bereits geplante Einträge können hier zurückgelegt werden.</span>
						</div>
					</template>
					<template #header(schriftlich)>
						<svws-ui-tooltip>
							<i-ri-group-line />
							<template #content>Schriftlich/Insgesamt im Kurs</template>
						</svws-ui-tooltip>
					</template>
					<template #header(dauer)>
						<svws-ui-tooltip>
							<i-ri-time-line />
							<template #content>Dauer in Minuten</template>
						</svws-ui-tooltip>
					</template>
					<template #body>
						<div v-for="klausur in props.kursklausurmanager().kursklausurOhneTerminGetMengeByQuartal(props.quartalsauswahl.value)" class="svws-ui-tr cursor-grab active:cursor-grabbing" role="row"
							:key="klausur.id"
							:data="klausur"
							:draggable="true"
							@dragstart="onDrag(klausur)"
							@dragend="onDrag(undefined)"
							:class="klausurCssClasses(klausur, undefined)">
							<div class="svws-ui-td">
								<i-ri-draggable class="-m-0.5 -ml-3" />
								<span class="svws-ui-badge" :style="`--background-color: ${getBgColor(props.kursmanager.get(klausur.idKurs)!.kuerzel.split('-')[0])};`">{{ props.kursmanager.get(klausur.idKurs)!.kuerzel }}</span>
							</div>
							<div class="svws-ui-td">{{ getLehrerKuerzel(klausur.idKurs) }}</div>
							<div class="svws-ui-td svws-align-right">{{ klausur.schuelerIds.size() + "/" + props.kursmanager.get(klausur.idKurs)!.schueler.size() }}</div>
							<div class="svws-ui-td svws-align-right">{{ klausur.dauer }}</div>
							<div class="svws-ui-td svws-align-right"><span class="opacity-50">{{ klausur.kursSchiene.toString() }}</span></div>
							<div class="svws-ui-td svws-align-right -mr-0.5" v-if="!quartalsauswahl.value"><span class="opacity-50">{{ klausur.quartal }}.</span></div>
						</div>
					</template>
					<template #actions>
						<svws-ui-button class="-mr-3" type="transparent" @click="erzeugeKursklausurenAusVorgaben(quartalsauswahl.value)" title="Erstelle Klausuren aus den Vorgaben"><i-ri-upload-2-line />Aus Vorgaben erstellen</svws-ui-button>
					</template>
				</svws-ui-table>
			</div>
		</svws-ui-content-card>
		<svws-ui-content-card>
			<div class="flex flex-wrap gap-1 mb-5 py-1 w-full">
				<svws-ui-button @click="erzeugeKlausurtermin(quartalsauswahl.value)"><i-ri-add-line class="-ml-1" />Termin</svws-ui-button>
				<svws-ui-button type="secondary" @click="showModalAutomatischBlocken().value = true" :disabled="termine.size() > 0 || props.kursklausurmanager().kursklausurOhneTerminGetMengeByQuartal(props.quartalsauswahl.value).size() === 0"><i-ri-sparkling-line />Automatisch blocken <svws-ui-spinner :spinning="loading" /></svws-ui-button>
				<svws-ui-button type="danger" class="ml-auto" @click="loescheKlausurtermine(termine)" :disabled="termine.size() === 0"><i-ri-delete-bin-line />Alle löschen</svws-ui-button>
			</div>
			<div class="grid grid-cols-[repeat(auto-fill,minmax(20rem,1fr))] gap-4">
				<template v-if="termine.size()">
					<s-gost-klausurplanung-schienen-termin v-for="termin of termine" :key="termin.id"
						:termin="() => termin"
						:class="dropOverCssClasses(termin)"
						:kursklausurmanager="kursklausurmanager"
						:map-lehrer="mapLehrer"
						:drag-data="() => dragData"
						@dragover="terminSelected=termin"
						:on-drag="onDrag"
						:on-drop="onDrop"
						@click="terminSelected=termin;$event.stopPropagation()"
						:loesche-klausurtermine="loescheKlausurtermine"
						:patch-klausurtermin="patchKlausurtermin"
						:klausur-css-classes="klausurCssClasses"
						:kursmanager="kursmanager" />
				</template>
				<template v-else>
					<div class="h-48 border-2 border-dashed bg-white dark:bg-black rounded-xl border-black/10 dark:border-white/10 flex items-center justify-center p-3 text-center">
						<span class="opacity-50">Noch keine Termine angelegt.</span>
					</div>
				</template>
			</div>
		</svws-ui-content-card>
		<svws-ui-content-card>
			<template #title>
				<span class="pt-1 text-headline-md leading-none inline-flex gap-1">
					<template v-if="klausurKonflikte().size() > 0">
						<i-ri-alert-fill class="text-error -my-1" />
						<span>{{ klausurKonflikte().size() }} Kurse mit Konflikten</span>
					</template>
					<template v-else-if="terminSelected !== undefined || dragData !== undefined">
						<i-ri-checkbox-circle-fill class="text-success -my-1" />
						<span>Keine Konflikte</span>
					</template>
					<template v-else>
						<span>Konflikte</span>
					</template>
				</span>
			</template>
			<div v-if="klausurKonflikte().size() > 0" class="mt-6">
				<ul class="flex flex-col gap-3">
					<li v-for="klausur in klausurKonflikte()" :key="klausur.getKey().id">
						<span class="svws-ui-badge" :style="`--background-color: ${getBgColor(klausur.getKey().kursKurzbezeichnung.split('-')[0])};`">{{ klausur.getKey().kursKurzbezeichnung }}</span>
						<div class="leading-tight">
							{{ [...klausur.getValue()].map(sid => mapSchueler.get(sid)?.vorname + ' ' + mapSchueler.get(sid)?.nachname).join(", ") }}
						</div>
					</li>
				</ul>
			</div>
			<div v-else-if="terminSelected === undefined" class="mt-6 opacity-50 flex flex-col gap-2">
				<span>Klicke auf einen Termin oder verschiebe eine Klausur, um Details zu bestehenden bzw. entstehenden Konflikten anzuzeigen.</span>
			</div>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { JavaMapEntry, JavaSet} from "@core";
	import {GostKursklausur, GostKlausurtermin, ZulaessigesFach, HashSet, KlausurterminblockungAlgorithmen, GostKlausurterminblockungDaten, KlausurterminblockungModusKursarten, KlausurterminblockungModusQuartale} from "@core";
	import { computed, ref, onMounted } from 'vue';
	import type { GostKlausurplanungSchienenProps } from './SGostKlausurplanungSchienenProps';
	import type { GostKlausurplanungDragData, GostKlausurplanungDropZone } from "./SGostKlausurplanung";
	import type {DataTableColumn} from "@ui";

	const _showModalAutomatischBlocken = ref<boolean>(false);
	const showModalAutomatischBlocken = () => _showModalAutomatischBlocken;

	const props = defineProps<GostKlausurplanungSchienenProps>();

	const loading = ref<boolean>(false);

	const dragData = ref<GostKlausurplanungDragData>(undefined);
	const terminSelected = ref<GostKlausurtermin | undefined>(undefined);

	const onDrag = (data: GostKlausurplanungDragData) => {
		terminSelected.value = undefined;
		dragData.value = data;
	};

	function getLehrerKuerzel(kursid: number) {
		const kurs = props.kursmanager.get(kursid);
		const lehrerid = kurs?.lehrer;
		if (typeof lehrerid === 'number')
			return props.mapLehrer.get(lehrerid)?.kuerzel || ''
		return ''
	}
	const klausurKonflikte = () => {
		if (dragData.value !== undefined && terminSelected.value !== undefined) {
			if (dragData.value.quartal === terminSelected.value.quartal || terminSelected.value.quartal === 0)
				return props.kursklausurmanager().konflikteNeuMapKursklausurSchueleridsByTerminidAndKursklausurid(terminSelected.value.id, dragData.value.id).entrySet();
		} else if (terminSelected.value !== undefined)
			return props.kursklausurmanager().konflikteMapKursklausurSchueleridsByTerminid(terminSelected.value.id).entrySet();
		return new HashSet<JavaMapEntry<GostKursklausur, JavaSet<number>>>();
	}

	const onDrop = async (zone: GostKlausurplanungDropZone) => {
		if (dragData.value instanceof GostKursklausur) {
			const klausur = dragData.value;
			if (zone === undefined && klausur.idTermin != null)
				await props.patchKursklausur(klausur.id, {idTermin: null});
			else if (zone instanceof GostKlausurtermin) {
				const termin = zone;
				if (termin.id != klausur.idTermin)
					await props.patchKursklausur(klausur.id, {idTermin: termin.id});
			}
		}
	};

	const dropOverCssClasses = (termin: GostKlausurtermin) => ({
		"bg-success": dragData.value !== undefined && (dragData.value.quartal === termin.quartal || termin.quartal === 0),
		"opacity-25 border-transparent shadow-none": dragData.value !== undefined && (dragData.value.quartal !== termin.quartal && termin.quartal !== 0),
	});

	const termine = computed(() => props.quartalsauswahl.value === 0 ? props.kursklausurmanager().terminGetMengeAsList() : props.kursklausurmanager().terminGetMengeByQuartal(props.quartalsauswahl.value, true));

	const algMode = ref<KlausurterminblockungAlgorithmen>(KlausurterminblockungAlgorithmen.NORMAL);
	const lkgkMode = ref<KlausurterminblockungModusKursarten>(KlausurterminblockungModusKursarten.BEIDE);
	const blockeGleicheLehrkraft = ref(false);

	const blocken = async () => {
		loading.value = true;
		showModalAutomatischBlocken().value = false;
		const daten = new GostKlausurterminblockungDaten();
		daten.klausuren = props.kursklausurmanager().kursklausurOhneTerminGetMengeByQuartal(props.quartalsauswahl.value);
		daten.konfiguration.modusQuartale = KlausurterminblockungModusQuartale.GETRENNT.id;
		daten.konfiguration.algorithmus = algMode.value.id;
		daten.konfiguration.modusKursarten = lkgkMode.value.id;
		daten.konfiguration.regelBeiTerminenGleicheLehrkraftFachKursart = blockeGleicheLehrkraft.value;
		await props.blockenKursklausuren(daten);
		loading.value = false;
	};

	const klausurCssClasses = (klausur: GostKursklausur, termin: GostKlausurtermin | undefined) => {
		let konfliktfreiZuFremdtermin = false;
		for (const oTermin of termine.value) {
			if (oTermin.id !== klausur.idTermin && oTermin.quartal === klausur.quartal || oTermin.quartal === 0)
				konfliktfreiZuFremdtermin = props.kursklausurmanager().konflikteAnzahlZuTerminGetByTerminAndKursklausur(oTermin, klausur) === 0;
			if (konfliktfreiZuFremdtermin)
				break;
		}
		const konfliktZuEigenemTermin = termin === undefined || klausur === null ? false : props.kursklausurmanager().konflikteAnzahlZuEigenemTerminGetByKursklausur(klausur) > 0;
		return {
			"svws-ok": !konfliktZuEigenemTermin && konfliktfreiZuFremdtermin,
			"svws-warning": !konfliktfreiZuFremdtermin,
			"svws-error": konfliktZuEigenemTermin,
		}
	};

	const isMounted = ref(false);
	onMounted(() => {
		isMounted.value = true;
	});

	function calculateColumns() {
		const cols: DataTableColumn[] = [
			{ key: "kurs", label: "Kurs", span: 1.25 },
			{ key: "kuerzel", label: "Lehrkraft" },
			{ key: "schriftlich", label: "Schriftlich", span: 0.5, align: "right", minWidth: 3.25 },
			{ key: "dauer", label: "Dauer", tooltip: "Dauer in Minuten", span: 0.5, align: "right", minWidth: 3.25 },
			{ key: "kursSchiene", label: "S", tooltip: "Schiene", span: 0.25, align: "right", minWidth: 2.75 },
		];

		if (props.quartalsauswahl.value === 0) {
			cols.push({ key: "quartal", label: "Q", tooltip: "Quartal", span: 0.25, align: "right", minWidth: 2.75 })
		}

		return cols;
	}

	const cols = computed(() => calculateColumns());

	const getBgColor = (kuerzel: string | null) => ZulaessigesFach.getByKuerzelASD(kuerzel).getHMTLFarbeRGBA(1.0); // TODO: Fachkuerzel für Kursklausur

</script>

<style lang="postcss" scoped>
.page--content {
  @apply grid;
  grid-template-columns: minmax(22rem, 0.2fr) 1fr minmax(22rem, 0.2fr);
}
</style>
