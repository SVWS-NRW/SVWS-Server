<template>
	<Teleport to=".svws-ui-header--actions" v-if="isMounted">
		<svws-ui-modal-hilfe class="ml-auto"> <s-gost-klausurplanung-schienen-hilfe /> </svws-ui-modal-hilfe>
	</Teleport>
	<Teleport to=".router-tab-bar--subnav" v-if="isMounted">
		<s-gost-klausurplanung-quartal-auswahl show-alle-jahrgaenge />
	</Teleport>

	<svws-ui-modal v-if="showModalTerminGrund" :show="showModalTerminGrund" size="big">
		<template #modalTitle>
			Nachschreiber in folgenden Hauptterminen zulassen:
		</template>
		<template #modalContent>
			<svws-ui-table :columns="cols">
				<template #noData>
					<slot name="noData">
						&nbsp;
					</slot>
				</template>
				<template #body>
					<template v-for="termin in state.manager.terminHtGetMengeByAbijahrAndHalbjahrAndQuartal(state.jahrgangsdaten.abiturjahr, state.halbjahr, state.quartal)" :key="termin.id">
						<div class="svws-ui-tr" style="grid-template-columns: 2rem 8rem 4rem minmax(4rem, 1fr)">
							<div class="svws-ui-td">
								<svws-ui-checkbox :title="state.manager.schuelerklausurterminNtGetMengeByTermin(termin).size() > 0 ? 'Termin enthält Nachschreiber' : ''"
									:disabled="state.manager.schuelerklausurterminNtGetMengeByTermin(termin).size() > 0" v-model="termin.nachschreiberZugelassen"
									@update:model-value="nachschreiberZugelassen => state.patchKlausurtermin(termin.id, { nachschreiberZugelassen } )" />
							</div>
							<div class="svws-ui-td">
								{{ termin.datum !== null ? DateUtils.gibDatumGermanFormat(termin.datum) : "N.N." }}
							</div>
							<div class="svws-ui-td">
								{{ state.manager.schuelerklausurterminGetMengeByTermin(termin).size() }}
							</div>
							<div class="svws-ui-td">
								{{ [...state.manager.kursklausurGetMengeByTermin(termin)].map(k => state.manager.kursKurzbezeichnungByKursklausur(k)).join(", ") }}
							</div>
						</div>
					</template>
				</template>
			</svws-ui-table>
		</template>
		<template #modalActions>
			<svws-ui-button type="primary" @click="showModalTerminGrund = false"> OK </svws-ui-button>
		</template>
	</svws-ui-modal>
	<svws-ui-modal v-model:show="showModalAutomatischBlocken" size="small">
		<template #modalTitle>
			Automatisch blocken
		</template>
		<template #modalContent>
			<svws-ui-checkbox type="toggle" :disabled="gleiche_fachart_auf_selbe_termine" v-model="nachschreiber_der_selben_klausur_auf_selbe_termine" class="text-left">
				Nachschreiber aus gleichem Kurs zusammenfassen
			</svws-ui-checkbox>
			<svws-ui-checkbox type="toggle" v-model="gleiche_fachart_auf_selbe_termine" @update:model-value="value => nachschreiber_der_selben_klausur_auf_selbe_termine = value ? false : nachschreiber_der_selben_klausur_auf_selbe_termine" class="text-left">
				Nachschreiber mit gleichem Fach und gleicher Kursart zusammenfassen
			</svws-ui-checkbox>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="showModalAutomatischBlocken = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="primary" @click="blocken"> Blocken </svws-ui-button>
		</template>
	</svws-ui-modal>

	<s-gost-klausurplanung-layout sidebar-title="In Planung"
		:sidebar-drop-enabled="nachschreiberDragDataHatTermin(dragData)"
		@sidebar-drop="onDrop(undefined)">
		<template #sidebar>
			<s-gost-klausurplanung-sidebar-liste :empty="state.manager.schuelerklausurterminNtAktuellOhneTerminGetMengeByHalbjahrAndQuartal(state.jahrgangsdaten.abiturjahr, state.halbjahr, state.quartal).isEmpty()">
				<template #empty>
					<span>Aktuell keine Nachschreibklausuren zu planen.</span>
					<span class="opacity-50">Bereits geplante Klausuren können hier zurückgelegt werden.</span>
				</template>
				<template #beforeList>
					<div class="mb-2 px-1">
						<svws-ui-checkbox :model-value="selectedNachschreiber.containsAll(state.manager.schuelerklausurterminNtAktuellOhneTerminGetMengeByHalbjahrAndQuartal(state.jahrgangsdaten.abiturjahr, state.halbjahr, state.quartal))" @update:model-value="selectedNachschreiber.containsAll(state.manager.schuelerklausurterminNtAktuellOhneTerminGetMengeByHalbjahrAndQuartal(state.jahrgangsdaten.abiturjahr, state.halbjahr, state.quartal)) ? selectedNachschreiber.clear() : selectedNachschreiber.addAll(state.manager.schuelerklausurterminNtAktuellOhneTerminGetMengeByHalbjahrAndQuartal(state.jahrgangsdaten.abiturjahr, state.halbjahr, state.quartal))">Alle auswählen</svws-ui-checkbox>
					</div>
				</template>
				<s-gost-klausurplanung-sidebar-eintrag v-for="schuelertermin in state.manager.schuelerklausurterminNtAktuellOhneTerminGetMengeByHalbjahrAndQuartal(state.jahrgangsdaten.abiturjahr, state.halbjahr, state.quartal)" :key="schuelertermin.id"
					:id="`nachschreiber-sidebar-${schuelertermin.id}`"
					:data="schuelertermin"
					:draggable="draggable(schuelertermin)"
					selectable
					:checked="selectedNachschreiber.contains(schuelertermin)"
					:selected="selectedNachschreiber.contains(schuelertermin)"
					:dragging="isDraggedNachschreiber(schuelertermin)"
					:title="presenter.schuelerNameBySchuelerklausurtermin(schuelertermin)"
					@update:checked="selectedNachschreiber.contains(schuelertermin) ? selectedNachschreiber.remove(schuelertermin) : selectedNachschreiber.add(schuelertermin)"
					@dragstart="onDrag($event, schuelertermin);$event.stopPropagation()"
					@dragend="onDrag($event, undefined);$event.stopPropagation()">
					<template #badge>
						<s-gost-klausurplanung-kurs-badge :schuelerklausurtermin="schuelertermin" :tooltip="false" />
					</template>
					<template #titleMeta>
						{{ state.manager.kursLehrerKuerzelByKursklausur(state.manager.kursklausurBySchuelerklausurtermin(schuelertermin)) }}
					</template>
					<template #meta>
						<span v-if="multijahrgang()" class="opacity-60">{{ presenter.schuelerklausurterminJahrgangText(schuelertermin) }}</span>
						<span>{{ state.manager.vorgabeBySchuelerklausurtermin(schuelertermin).dauer }} Min.</span>
					</template>
					<template #tooltip>
						<dl class="grid grid-cols-[auto_minmax(0,1fr)] gap-x-3 gap-y-1">
							<dt class="col-span-2 text-base font-bold">{{ state.manager.kursKurzbezeichnungByKursklausur(state.manager.kursklausurBySchuelerklausurtermin(schuelertermin)) }}</dt>
							<dt class="opacity-60">Fachlehrer</dt>
							<dd>{{ presenter.kursLehrerNameText(state.manager.kursklausurBySchuelerklausurtermin(schuelertermin)) }}</dd>
							<dt class="opacity-60">Kursgröße</dt>
							<dd>{{ state.manager.kursAnzahlSchuelerGesamtByKursklausur(state.manager.kursklausurBySchuelerklausurtermin(schuelertermin)) }}</dd>
							<dt class="opacity-60">Klausurschreiber</dt>
							<dd>{{ state.manager.kursAnzahlKlausurschreiberByKursklausur(state.manager.kursklausurBySchuelerklausurtermin(schuelertermin)) }}</dd>
							<dt class="opacity-60">Klausurdauer</dt>
							<dd>{{ state.manager.vorgabeBySchuelerklausurtermin(schuelertermin).dauer }} Min.</dd>
							<dt class="opacity-60">Klausurdatum</dt>
							<dd>{{ presenter.schuelerklausurterminVorgaengerDatumText(schuelertermin) }}</dd>
							<dt class="opacity-60">Schiene</dt>
							<dd>{{ presenter.kursSchieneText(state.manager.kursklausurBySchuelerklausurtermin(schuelertermin)) }}</dd>
							<dt class="opacity-60">Versäumnisgrund</dt>
							<dd class="whitespace-pre-wrap">{{ presenter.schuelerklausurterminVorgaengerBemerkung(schuelertermin) ?? "-" }}</dd>
						</dl>
					</template>
				</s-gost-klausurplanung-sidebar-eintrag>
			</s-gost-klausurplanung-sidebar-liste>
		</template>
		<template #workspace>
			<div class="flex flex-col gap-4 w-full">
				<div v-if="multijahrgang()" class="flex flex-col gap-4 rounded-lg bg-ui-warning-weak px-6 py-3 min-w-120 w-full">
					<span class="leading-tight text-headline-md gap-1">
						<span v-if="(!state.zeigeAlleJahrgaenge && state.manager.terminNtMengeEnthaeltFremdeJgstByAbijahrAndHalbjahrAndQuartalMultijahrgang(state.jahrgangsdaten.abiturjahr, state.halbjahr, state.quartal, true))" class="icon i-ri-alert-fill icon-ui-danger px-4" />
						<span>Jahrgangsübergreifende Planung</span>
						<span v-if="(!state.zeigeAlleJahrgaenge && state.manager.terminNtMengeEnthaeltFremdeJgstByAbijahrAndHalbjahrAndQuartalMultijahrgang(state.jahrgangsdaten.abiturjahr, state.halbjahr, state.quartal, true))"> aktiviert, da jahrgangsgemischte Termine existieren</span>
					</span>
					<ul>
						<li class="flex font-bold">
							<span>{{ state.manager.schuelerklausurterminNtAktuellGetMengeByHalbjahrAndQuartal(state.jahrgangsdaten.abiturjahr, state.halbjahr, state.quartal).size() }} Nachschreiber im aktuellen Jahrgang,&nbsp;</span>
							<span v-if="state.manager.schuelerklausurterminNtAktuellOhneTerminGetMengeByHalbjahrAndQuartal(state.jahrgangsdaten.abiturjahr, state.halbjahr, state.quartal).size() === 0" class="text-ui-success">alle zugewiesen.</span>
							<span v-else class="text-ui-danger">nicht alle zugewiesen.</span>
						</li>
						<template v-for="pair in GostKlausurplanManager.halbjahreParallelUndAktivGetMenge(state.jahrgangsdaten.abiturjahr, state.halbjahr, false)" :key="pair.a">
							<li class="flex" v-if="state.manager.schuelerklausurterminNtAktuellGetMengeByHalbjahrAndQuartal(pair.a, pair.b, state.quartal).size() > 0">
								<span>{{ state.manager.schuelerklausurterminNtAktuellGetMengeByHalbjahrAndQuartal(pair.a, pair.b, state.quartal).size() }} Nachschreiber im Jahrgang {{ pair.b.jahrgang }},&nbsp;</span>
								<span v-if="state.manager.schuelerklausurterminNtAktuellOhneTerminGetMengeByHalbjahrAndQuartal(pair.a, pair.b, state.quartal).size() === 0" class="text-ui-success">alle zugewiesen.</span>
								<span v-else class="text-ui-danger">nicht alle zugewiesen.</span>
								<svws-ui-button type="icon" @click="gotoNachschreiber(pair.a, pair.b)" :title="`Zur Planung des Jahrgangs`" size="small"><span class="icon i-ri-link" /></svws-ui-button>
							</li>
						</template>
					</ul>
				</div>
				<div class="flex justify-between items-start">
					<div class="flex flex-wrap items-center gap-2 w-full">
						<svws-ui-button :disabled="!hatKompetenzUpdate" @click="state.erzeugeKlausurtermin(state.quartal, false)"><span class="icon i-ri-add-line" />Neuer Nachschreibtermin</svws-ui-button>
						<svws-ui-button :disabled="!hatKompetenzUpdate" type="secondary" @click="showModalTerminGrund = true"><span class="icon i-ri-checkbox-circle-line" />Haupttermin zulassen</svws-ui-button>
						<svws-ui-button type="secondary" :disabled="!hatKompetenzUpdate || selectedNachschreiber.isEmpty()" @click="showModalAutomatischBlocken = true"><span class="icon i-ri-sparkling-line" />Automatisch blocken <svws-ui-spinner :spinning="loading" /></svws-ui-button>
					</div>
				</div>
			</div>
			<div class="w-full overflow-hidden">
				<div class="h-full w-full grow grid gap-4 overflow-y-auto" style="grid-template-columns: repeat(auto-fill, minmax(45rem, 1fr));">
					<template v-if="termine.size()">
						<s-gost-klausurplanung-nachschreiber-termin v-for="termin of termine" :key="termin.id"
							:termin="() => termin"
							:drag-data
							:on-drag
							:on-drop
							:draggable
							:termin-selected="terminSelected?.id===termin.id"
							@click="terminSelected=(terminSelected?.id===termin.id?undefined:termin);$event.stopPropagation()"
							:klausur-css-classes
							:show-schuelerklausuren="true"
							:goto-kalenderdatum
							:goto-raumzeit-termin />
					</template>
					<template v-else>
						<div class="shadow-inner rounded-lg h-48" />
						<div class="shadow-inner rounded-lg h-48" />
						<div class="shadow-inner rounded-lg h-48" />
					</template>
				</div>
			</div>
		</template>
	</s-gost-klausurplanung-layout>
</template>

<script setup lang="ts">
	import { computed, ref, onMounted, type HTMLAttributes } from 'vue';
	import type { GostKlausurplanungDragData, GostKlausurplanungDropZone, GostNachschreiberDragData } from "./SGostKlausurplanung";
	import { isGostNachschreiberDragData } from "./SGostKlausurplanung";
	import { useKlausurplanungDragAndDrop } from "./SGostKlausurplanungDragUtils";
	import { useKlausurplanungPresenter } from "./SGostKlausurplanungPresenter";
	import { GostKlausurtermin } from '@core/core/data/gost/klausuren/GostKlausurtermin';
	import { GostKursklausur } from '@core/core/data/gost/klausuren/GostKursklausur';
	import { GostNachschreibterminblockungKonfiguration } from '@core/core/data/gost/klausuren/GostNachschreibterminblockungKonfiguration';
	import { GostSchuelerklausurtermin } from '@core/core/data/gost/klausuren/GostSchuelerklausurtermin';
	import { BenutzerKompetenz } from '@core/core/types/benutzer/BenutzerKompetenz';
	import type { GostHalbjahr } from '@core/core/types/gost/GostHalbjahr';
	import { ListUtils } from '@core/core/utils/ListUtils';
	import { ArrayList } from '@core/java/util/ArrayList';
	import { HashSet } from '@core/java/util/HashSet';
	import type { JavaSet } from '@core/java/util/JavaSet';
	import type { List } from '@core/java/util/List';
	import { useBenutzerState } from '@ui/states/BenutzerState';
	import { useGostKlausurplanungState } from '@ui/states/GostKlausurplanungState';
	import type { DataTableColumn } from '@ui/types';
	import { GostKlausurplanManager } from '@core/core/utils/gost/klausuren/GostKlausurplanManager';
	import { DateUtils } from '@core/core/utils/DateUtils';

	const { gotoKalenderdatum, gotoNachschreiber, gotoRaumzeitTermin } = defineProps<{
		gotoKalenderdatum: (datum: string | undefined, termin: GostKlausurtermin | undefined) => Promise<void>;
		gotoNachschreiber: (abiturjahr: number, halbjahr: GostHalbjahr) => Promise<void>;
		gotoRaumzeitTermin: (abiturjahr: number, halbjahr: GostHalbjahr, idtermin: number | undefined) => Promise<void>;
	}>();
	const state = useGostKlausurplanungState();
	const benutzerState = useBenutzerState();
	const presenter = useKlausurplanungPresenter(state);

	const showModalTerminGrund = ref<boolean>(false);
	const showModalAutomatischBlocken = ref<boolean>(false);
	const nachschreiber_der_selben_klausur_auf_selbe_termine = ref(false);
	const gleiche_fachart_auf_selbe_termine = ref(false);

	const multijahrgang = () => state.zeigeAlleJahrgaenge || state.manager.terminNtMengeEnthaeltFremdeJgstByAbijahrAndHalbjahrAndQuartalMultijahrgang(state.jahrgangsdaten.abiturjahr, state.halbjahr, state.quartal, true);

	const selectedNachschreiber = ref<JavaSet<GostSchuelerklausurtermin>>(new HashSet<GostSchuelerklausurtermin>());

	const loading = ref<boolean>(false);


	const hatKompetenzUpdate = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN));

	const terminSelected = ref<GostKlausurtermin | undefined>(undefined);
	const { dragData, setDragData } = useKlausurplanungDragAndDrop(() => terminSelected.value = undefined);

	async function blocken() {
		showModalAutomatischBlocken.value = false;
		loading.value = true;
		const config = new GostNachschreibterminblockungKonfiguration();
		config.termine = termine.value;
		selectedNachschreiber.value.retainAll(state.manager.schuelerklausurterminNtAktuellOhneTerminGetMengeByHalbjahrAndQuartal(state.jahrgangsdaten.abiturjahr, state.halbjahr, state.quartal));
		config.schuelerklausurtermine = new ArrayList<GostSchuelerklausurtermin>(selectedNachschreiber.value);
		config._regel_nachschreiber_der_selben_klausur_auf_selbe_termine_verteilen = nachschreiber_der_selben_klausur_auf_selbe_termine.value;
		config._regel_gleiche_fachart_auf_selbe_termine_verteilen = gleiche_fachart_auf_selbe_termine.value;
		await state.blockenNachschreiber(config);
		selectedNachschreiber.value.clear();
		loading.value = false;
	}

	function draggable(data: GostKlausurplanungDragData) {
		return hatKompetenzUpdate.value && (data instanceof GostSchuelerklausurtermin);
	}

	function createNachschreiberDragData(schuelertermin: GostSchuelerklausurtermin): GostNachschreiberDragData {
		selectedNachschreiber.value.retainAll(state.manager.schuelerklausurterminNtAktuellOhneTerminGetMengeByHalbjahrAndQuartal(state.jahrgangsdaten.abiturjahr, state.halbjahr, state.quartal));
		if (selectedNachschreiber.value.contains(schuelertermin) && (selectedNachschreiber.value.size() > 1)) {
			return { type: "nachschreiber", items: new ArrayList<GostSchuelerklausurtermin>(selectedNachschreiber.value) };
		}
		return { type: "nachschreiber", items: ListUtils.create1(schuelertermin) };
	}

	function onDrag(event: DragEvent | undefined, data: GostKlausurplanungDragData): void {
		if (data === undefined) {
			setDragData(undefined);
		} else if (data instanceof GostSchuelerklausurtermin) {
			const nachschreiberDragData = createNachschreiberDragData(data);
			setDragImage(event, nachschreiberDragData);
			setDragData(nachschreiberDragData);
		} else {
			setDragData(data);
		}
	}

	function setDragImage(event: DragEvent | undefined, data: GostNachschreiberDragData): void {
		if ((event?.dataTransfer === null) || (event?.dataTransfer === undefined)) {
			return;
		}
		if (data.items.size() <= 1) {
			return;
		}
		const preview = document.createElement("div");
		preview.className = "fixed -top-1000 left-0 flex flex-col gap-2";
		preview.style.width = `${(event.currentTarget instanceof HTMLElement) ? event.currentTarget.getBoundingClientRect().width : 384}px`;
		for (const item of data.items) {
			const card = document.getElementById(`nachschreiber-sidebar-${item.id}`)?.cloneNode(true);
			if (!(card instanceof HTMLElement)) {
				continue;
			}
			card.removeAttribute("id");
			preview.append(card);
		}
		if (preview.childElementCount === 0) {
			return;
		}
		document.body.append(preview);
		event.dataTransfer.setDragImage(preview, 16, 16);
		window.setTimeout(() => preview.remove(), 0);
	}

	function nachschreiberDragDataHatTermin(data: GostKlausurplanungDragData): boolean {
		if (!isGostNachschreiberDragData(data)) {
			return false;
		}
		for (const item of data.items) {
			if (item.idTermin !== null) {
				return true;
			}
		}
		return false;
	}

	function removeSelectedNachschreiber(items: List<GostSchuelerklausurtermin>): void {
		for (const item of items) {
			selectedNachschreiber.value.remove(item);
		}
	}

	function isDraggedNachschreiber(schuelertermin: GostSchuelerklausurtermin): boolean {
		if (!isGostNachschreiberDragData(dragData.value)) {
			return false;
		}
		if (dragData.value.items.size() <= 1) {
			return false;
		}
		return dragData.value.items.contains(schuelertermin);
	}

	async function patchNachschreiberTermin(items: List<GostSchuelerklausurtermin>, idTermin: number | null): Promise<boolean> {
		const patchItems = new ArrayList<GostSchuelerklausurtermin>();
		for (const item of items) {
			if (item.idTermin !== idTermin) {
				patchItems.add(item);
			}
		}
		if (patchItems.isEmpty()) {
			return false;
		}
		await state.patchSchuelerklausurtermine(patchItems, { idTermin });
		removeSelectedNachschreiber(items);
		return true;
	}

	const onDrop = async (zone: GostKlausurplanungDropZone) => {
		const data = dragData.value;
		if (!isGostNachschreiberDragData(data)) {
			return;
		}
		if (zone === undefined) {
			await patchNachschreiberTermin(data.items, null);
			return;
		}
		if (!(zone instanceof GostKlausurtermin) || !state.manager.schuelerklausurterminePassenInNachschreibtermin(zone, data.items)) {
			return;
		}
		if (await patchNachschreiberTermin(data.items, zone.id)) {
			terminSelected.value = zone;
		}
	};

	const termine = computed(() => state.manager.terminNTGetMengeByAbijahrAndHalbjahrAndQuartalMultijahrgang(state.jahrgangsdaten.abiturjahr, state.halbjahr, state.quartal, multijahrgang()));

	function hatKonfliktMitGezogenenNachschreibern(klausur: GostKursklausur, data: GostNachschreiberDragData): boolean {
		for (const item of data.items) {
			if (state.manager.konfliktZuKursklausurBySchuelerklausur(item, klausur)) {
				return true;
			}
		}
		return false;
	}

	function istGezogenerNachschreiber(klausur: GostSchuelerklausurtermin, data: GostNachschreiberDragData): boolean {
		for (const item of data.items) {
			if (klausur.id === item.id) {
				return true;
			}
		}
		return false;
	}

	function hatKonfliktMitGezogenenNachschreiberSchuelern(klausur: GostSchuelerklausurtermin, data: GostNachschreiberDragData): boolean {
		const idSchueler = state.manager.schuelerklausurGetByIdOrException(klausur.idSchuelerklausur).idSchueler;
		for (const item of data.items) {
			if ((klausur.id !== item.id) && (idSchueler === state.manager.schuelerklausurGetByIdOrException(item.idSchuelerklausur).idSchueler)) {
				return true;
			}
		}
		return false;
	}

	const klausurCssClasses = (klausur: GostKlausurplanungDragData, termin: GostKlausurtermin | undefined): HTMLAttributes["class"] => {
		let hatKonflikt = false;
		const data = dragData.value;
		if ((klausur instanceof GostKursklausur) && isGostNachschreiberDragData(data)) {
			hatKonflikt = hatKonfliktMitGezogenenNachschreibern(klausur, data);
		} else if ((klausur instanceof GostSchuelerklausurtermin) && isGostNachschreiberDragData(data)) {
			hatKonflikt = !istGezogenerNachschreiber(klausur, data) && hatKonfliktMitGezogenenNachschreiberSchuelern(klausur, data);
		} else if ((klausur instanceof GostSchuelerklausurtermin) && (termin !== undefined)) {
			hatKonflikt = state.manager.konfliktPaarGetMengeTerminAndSchuelerklausurtermin(termin, klausur).size() > 0;
		}
		if (!hatKonflikt) {
			return undefined;
		}
		return "bg-ui-danger text-ui-ondanger";
	};

	const isMounted = ref(false);
	onMounted(() => {
		isMounted.value = true;
	});

	const cols: DataTableColumn[] = [
		{ key: "id", label: " ", fixedWidth: 2 },
		{ key: "datum", label: "Datum", fixedWidth: 8 },
		{ key: "size", label: "#SuS", fixedWidth: 4 },
		{ key: "faecher", label: "Kurse" },
	];

</script>
