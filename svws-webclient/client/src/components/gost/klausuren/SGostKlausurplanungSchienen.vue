<template>
	<Teleport to=".svws-ui-header--actions" v-if="isMounted">
		<svws-ui-modal-hilfe class="ml-auto"> <s-gost-klausurplanung-schienen-hilfe /> </svws-ui-modal-hilfe>
	</Teleport>
	<Teleport to=".router-tab-bar--subnav" v-if="isMounted">
		<s-gost-klausurplanung-quartal-auswahl />
	</Teleport>
	<svws-ui-modal v-model:show="showModalAutomatischBlocken" size="small">
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
				Gleicher Termin, falls gleiche Lehrkraft, Fach und Kursart
			</svws-ui-checkbox>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="showModalAutomatischBlocken = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="primary" @click="blocken"> Blocken </svws-ui-button>
		</template>
	</svws-ui-modal>
	<s-gost-klausurplanung-layout sidebar-title="In Planung"
		:sidebar-drop-enabled="(dragData !== undefined) && (dragData instanceof GostKursklausur) && (dragData.idTermin !== null)"
		@sidebar-drop="onDrop($event, undefined)">
		<template #sidebar>
			<s-gost-klausurplanung-sidebar-liste :empty="kursklausurenOhneTermin.isEmpty()">
				<template #empty>
					<span>Aktuell keine Klausuren zu planen.</span>
				</template>
				<template #beforeList>
					<div v-if="!kursklausurenOhneTermin.isEmpty()" class="mb-2 flex items-center gap-2 px-1">
						<svws-ui-checkbox :disabled="!hatKompetenzUpdate" :model-value="alleKlausurenInPlanungAusgewaehlt" :indeterminate="einigeKlausurenInPlanungAusgewaehlt"
							@update:model-value="toggleAlleKlausurenInPlanung">
							Alle auswählen
						</svws-ui-checkbox>
					</div>
				</template>
				<s-gost-klausurplanung-sidebar-eintrag v-for="klausur in kursklausurenOhneTermin" :key="klausur.id"
					:data="klausur"
					:draggable="draggable(klausur)"
					selectable
					:checked="isKursklausurSelected(klausur)"
					:select-disabled="!hatKompetenzUpdate"
					:selected="isKursklausurSelected(klausur)"
					@update:checked="toggleKursklausurSelection(klausur)"
					@dragstart="($event) => onDrag($event, klausur)"
					@dragend="onDrag($event, undefined)">
					<template #badge>
						<s-gost-klausurplanung-kurs-badge :kursklausur="klausur" />
					</template>
					<template #titleMeta>
						{{ state.manager.kursLehrerKuerzelByKursklausur(klausur) }}
					</template>
					<template #meta>
						<span>{{ state.manager.schuelerklausurGetMengeByKursklausur(klausur).size() }}/{{ state.manager.kursAnzahlSchuelerGesamtByKursklausur(klausur) }}</span>
						<span>{{ state.manager.vorgabeByKursklausur(klausur).dauer }} Min.</span>
						<span v-if="state.quartal === 0" class="opacity-50">{{ state.manager.vorgabeByKursklausur(klausur).quartal }}. Quartal</span>
					</template>
					<template #tooltip>
						<dl class="grid grid-cols-[auto_minmax(0,1fr)] gap-x-3 gap-y-1">
							<dt class="col-span-2 text-base font-bold">{{ state.manager.kursKurzbezeichnungByKursklausur(klausur) }}</dt>
							<dt class="opacity-60">Fachlehrer</dt>
							<dd>{{ presenter.kursLehrerNameText(klausur) }}</dd>
							<dt class="opacity-60">Kursgröße</dt>
							<dd>{{ state.manager.kursAnzahlSchuelerGesamtByKursklausur(klausur) }}</dd>
							<dt class="opacity-60">Klausurschreiber</dt>
							<dd>{{ state.manager.kursAnzahlKlausurschreiberByKursklausur(klausur) }}</dd>
							<dt class="opacity-60">Klausurdauer</dt>
							<dd>{{ state.manager.vorgabeByKursklausur(klausur).dauer }} Min.</dd>
							<dt class="opacity-60">Quartal</dt>
							<dd>{{ state.manager.vorgabeByKursklausur(klausur).quartal }}. Quartal</dd>
							<dt class="opacity-60">Schiene</dt>
							<dd>{{ presenter.kursSchieneText(klausur) }}</dd>
							<dt class="opacity-60">Letzte Klausur</dt>
							<dd>{{ presenter.kursklausurVorterminDatumText(klausur) }}</dd>
						</dl>
					</template>
				</s-gost-klausurplanung-sidebar-eintrag>
				<template #actions>
					<svws-ui-button v-if="selectedKursklausurenInPlanung.length > 0" type="trash" :disabled="!hatKompetenzUpdate"
						:title="`${selectedKursklausurenInPlanung.length} ausgewählte Einträge löschen`"
						@click="loescheSelectedKursklausuren" />
					<svws-ui-button :disabled="!hatKompetenzUpdate || (state.abschnitt === undefined)" type="transparent" @click="erzeugeKursklausurenAusVorgabenOrModal" title="Erstelle Klausuren aus den Vorgaben"><span class="icon i-ri-upload-2-line" />Aus Vorgaben erstellen</svws-ui-button>
				</template>
			</s-gost-klausurplanung-sidebar-liste>
		</template>
		<template #workspace>
			<div class="flex justify-between items-start">
				<div class="flex flex-wrap items-center gap-2 w-full">
					<svws-ui-button :disabled="!hatKompetenzUpdate || (state.abschnitt === undefined)" @click="state.erzeugeKlausurtermin(state.quartal, true)"><span class="icon i-ri-add-line -ml-1" />Termin<template v-if="termine.size() === 0"> hinzufügen</template></svws-ui-button>
					<svws-ui-button type="transparent" @click="showModalAutomatischBlocken = true" :disabled="!hatKompetenzUpdate || (state.manager.kursklausurOhneTerminGetMengeByAbijahrAndHalbjahrAndQuartal(state.jahrgangsdaten.abiturjahr, state.halbjahr, state.quartal).size() === 0)"><span class="icon i-ri-sparkling-line" />Automatisch blocken <svws-ui-spinner :spinning="loading" /></svws-ui-button>
					<svws-ui-button type="transparent" :disabled="!hatKompetenzUpdate" class="hover--danger ml-auto" @click="state.setSelectedTermin(undefined); state.loescheKlausurtermine(termine)" v-if="termine.size() > 0" title="Alle Termine löschen"><span class="icon i-ri-delete-bin-line" />Alle löschen</svws-ui-button>
				</div>
			</div>
			<div class="grow overflow-auto grid gap-4 pt-2 -mt-2" style="grid-template-columns: repeat(auto-fill,minmax(22rem,1fr));">
				<template v-if="termine.size()">
					<template v-for="termin of termine" :key="termin.id">
						<s-gost-klausurplanung-schienen-termin :id="'termin' + termin.id"
							class="gost_klausurtermin"
							:draggable :on-drag :on-drop :drag-data
							@dragover="setDragHoverTermin(termin)"
							@dragleave="clearDragHoverTermin($event, termin)"
							@click="props.gotoSchienen(state.selectedTermin?.id === termin.id ? undefined : termin);$event.stopPropagation()" :termin="() => termin" :termin-selected="state.selectedTermin?.id === termin.id"
							:klausur-css-classes :goto-kalenderdatum :goto-nachschreiber :goto-raumzeit-termin />
					</template>
				</template>
				<template v-else>
					<div class="shadow-inner rounded-lg h-48" />
					<div class="shadow-inner rounded-lg h-48" />
					<div class="shadow-inner rounded-lg h-48" />
				</template>
			</div>
		</template>
		<template #aside>
			<div v-if="quartalKonfliktDrop !== undefined" class="pt-14" @click.stop>
				<div class="rounded-lg bg-ui-brand/10 p-2">
					<div class="text-headline-md leading-tight inline-flex gap-1">
						<span class="icon i-ri-alert-line icon-ui-warning" />
						<span>Klausur-Quartal passt nicht zum Termin-Quartal</span>
					</div>
					<div class="mt-4 rounded-lg border border-ui-warning bg-ui-warning-weak px-3 py-2 text-sm leading-tight">
						<s-gost-klausurplanung-kurs-badge :kursklausur="quartalKonfliktDrop.kursklausur" :tooltip="false" :show-bemerkungen="false" />
						<span class="ml-2">liegt im {{ quartalKonfliktDrop.klausurQuartal }}. Quartal, der Termin erlaubt {{ quartalKonfliktDrop.terminQuartalText }}.</span>
					</div>
				</div>
			</div>
			<s-gost-klausurplanung-konflikte v-else-if="(asideTermin !== undefined) || (pendingKursklausurDrop !== undefined)" class="pt-14" @click.stop
				:termine="asideTermin === undefined ? [] : [asideTermin]"
				kontext="termin"
				:highlight="asideTermin !== undefined"
				:pending-kursklausur-drop />
		</template>
	</s-gost-klausurplanung-layout>
	<s-gost-klausurplanung-modal v-model:show="modalVorgaben" :text="modalError" :jump-to="props.gotoVorgaben" jump-to-text="Zu den Klausurvorgaben" abbrechen-text="OK" />
	<s-gost-klausurplanung-modal v-model:show="modalKlausurHatRaeume" text="Die Kursklausur hat bereits eine oder mehrere Raumzuweisungen. Beim Fortfahren werden diese gelöscht." :weiter="verschiebeKlausurTrotzRaumzuweisung" />
</template>

<script setup lang="ts">

	import type { GostHalbjahr } from "@core";
	import { BenutzerKompetenz, GostKursklausur, GostKlausurtermin, KlausurterminblockungAlgorithmen, GostKlausurterminblockungDaten, KlausurterminblockungModusKursarten, KlausurterminblockungModusQuartale } from "@core";
	import { computed, ref, onMounted, onUnmounted } from 'vue';
	import type { GostKlausurplanungDragData, GostKlausurplanungDropZone } from "./SGostKlausurplanung";
	import { useBenutzerState, useGostKlausurplanungState } from "@ui";
	import { useKlausurplanungPresenter } from "./SGostKlausurplanungPresenter";
	import { useKlausurplanungDragAndDrop } from "./SGostKlausurplanungDragUtils";

	const props = defineProps<{
		gotoKalenderdatum: (datum: string | undefined, termin: GostKlausurtermin | undefined) => Promise<void>;
		gotoNachschreiber: (abiturjahr: number, halbjahr: GostHalbjahr) => Promise<void>;
		gotoRaumzeitTermin: (abiturjahr: number, halbjahr: GostHalbjahr, idtermin: number | undefined) => Promise<void>;
		gotoSchienen: (termin: GostKlausurtermin | undefined) => Promise<void>;
		gotoVorgaben: () => Promise<void>;
	}>();
	const state = useGostKlausurplanungState();
	const benutzerState = useBenutzerState();
	const presenter = useKlausurplanungPresenter(state);

	const showModalAutomatischBlocken = ref<boolean>(false);
	const hatKompetenzUpdate = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN));

	const loading = ref<boolean>(false);

	const { dragData, onDrag: setDragDataOnDrag, scrollSelectedTerminIntoView } = useKlausurplanungDragAndDrop(() => state.setSelectedTermin(undefined));
	const dragHoverTermin = ref<GostKlausurtermin | undefined>(undefined);
	const asideTermin = computed<GostKlausurtermin | undefined>(() => dragHoverTermin.value ?? state.selectedTermin);
	const kursklausurenOhneTermin = computed(() => state.manager.kursklausurOhneTerminGetMengeByAbijahrAndHalbjahrAndQuartal(state.jahrgangsdaten.abiturjahr, state.halbjahr, state.quartal));
	const selectedKursklausuren = ref<GostKursklausur[]>([]);
	const selectedKursklausurenInPlanung = computed(() => selectedKursklausuren.value.filter(klausur => kursklausurenOhneTermin.value.contains(klausur)));
	const alleKlausurenInPlanungAusgewaehlt = computed<boolean>(() => !kursklausurenOhneTermin.value.isEmpty() && (selectedKursklausurenInPlanung.value.length === kursklausurenOhneTermin.value.size()));
	const einigeKlausurenInPlanungAusgewaehlt = computed<boolean>(() => (selectedKursklausurenInPlanung.value.length > 0) && !alleKlausurenInPlanungAusgewaehlt.value);
	const pendingKursklausurDrop = computed(() => ((asideTermin.value !== undefined) && (dragData.value instanceof GostKursklausur) && state.manager.kursklausurPasstInTermin(asideTermin.value, dragData.value))
		? { termin: asideTermin.value, kursklausur: dragData.value }
		: undefined);
	const quartalKonfliktDrop = computed(() => {
		if ((asideTermin.value === undefined) || !(dragData.value instanceof GostKursklausur) || state.manager.kursklausurPasstInTermin(asideTermin.value, dragData.value)) {
			return undefined;
		}
		return {
			kursklausur: dragData.value,
			klausurQuartal: state.manager.vorgabeByKursklausur(dragData.value).quartal,
			terminQuartalText: presenter.terminQuartalLangText(asideTermin.value),
		};
	});

	function onDrag(event: DragEvent | undefined, data: GostKlausurplanungDragData): void {
		setDragDataOnDrag(event, data);
		if (data === undefined) {
			dragHoverTermin.value = undefined;
		}
	}

	function setDragHoverTermin(termin: GostKlausurtermin): void {
		if (dragData.value instanceof GostKursklausur) {
			dragHoverTermin.value = termin;
		}
	}

	function clearDragHoverTermin(event: DragEvent, termin: GostKlausurtermin): void {
		const currentTarget = event.currentTarget;
		const relatedTarget = event.relatedTarget;
		if ((currentTarget instanceof HTMLElement) && (relatedTarget instanceof Node) && currentTarget.contains(relatedTarget)) {
			return;
		}
		if (dragHoverTermin.value?.id === termin.id) {
			dragHoverTermin.value = undefined;
		}
	}

	function isKursklausurSelected(klausur: GostKursklausur): boolean {
		return selectedKursklausurenInPlanung.value.some(selected => selected.id === klausur.id);
	}

	function toggleKursklausurSelection(klausur: GostKursklausur): void {
		if (isKursklausurSelected(klausur)) {
			selectedKursklausuren.value = selectedKursklausuren.value.filter(selected => selected.id !== klausur.id);
			return;
		}
		selectedKursklausuren.value = [...selectedKursklausurenInPlanung.value, klausur];
	}

	function toggleAlleKlausurenInPlanung(value: boolean): void {
		selectedKursklausuren.value = value ? [...kursklausurenOhneTermin.value] : [];
	}

	async function loescheSelectedKursklausuren(): Promise<void> {
		await state.loescheKursklausuren(selectedKursklausurenInPlanung.value);
		selectedKursklausuren.value = [];
	}

	const modalVorgaben = ref<boolean>(false);

	let klausurMoveDropZone: GostKlausurplanungDropZone = undefined;
	let klausurMoveDragData: GostKlausurplanungDragData = undefined;
	const modalKlausurHatRaeume = ref<boolean>(false);

	const modalError = ref<string | undefined>(undefined);

	async function erzeugeKursklausurenAusVorgabenOrModal() {
		const ergebnis = await state.erzeugeKursklausurenAusVorgaben(state.quartal);
		if (ergebnis.description !== null) {
			modalError.value = ergebnis.description;
			modalVorgaben.value = true;
		}
	}

	const onDrop = async (event: DragEvent | undefined, zone: GostKlausurplanungDropZone) => {
		dragHoverTermin.value = undefined;
		if (dragData.value instanceof GostKursklausur) {
			const klausur = dragData.value;
			if ((zone instanceof GostKlausurtermin) && (zone.id === klausur.idTermin)) {
				return;
			}
			klausurMoveDropZone = zone;
			klausurMoveDragData = dragData.value;
			if ((klausur.idTermin !== null) && state.manager.hatRaumzuteilungByKursklausur(klausur)) {
				modalKlausurHatRaeume.value = true;
				return;
			} else {
				await verschiebeKlausurTrotzRaumzuweisung();
			}
		}
	};

	async function verschiebeKlausurTrotzRaumzuweisung() {
		if (klausurMoveDragData instanceof GostKursklausur) {
			if ((klausurMoveDropZone === undefined) && (klausurMoveDragData.idTermin !== null)) {
				await state.patchKlausur(klausurMoveDragData, { idTermin: null });
			} else if (klausurMoveDropZone instanceof GostKlausurtermin) {
				const termin = klausurMoveDropZone;
				if (termin.id !== klausurMoveDragData.idTermin) {
					await state.patchKlausur(klausurMoveDragData, { idTermin: termin.id });
					state.setSelectedTermin(klausurMoveDropZone);
				}
			}
		}
	}

	const termine = computed(() => state.manager.terminHtGetMengeByAbijahrAndHalbjahrAndQuartal(state.jahrgangsdaten.abiturjahr, state.halbjahr, state.quartal));

	const algMode = ref<KlausurterminblockungAlgorithmen>(KlausurterminblockungAlgorithmen.NORMAL);
	const lkgkMode = ref<KlausurterminblockungModusKursarten>(KlausurterminblockungModusKursarten.BEIDE);
	const blockeGleicheLehrkraft = ref(false);

	function draggable(data: GostKlausurplanungDragData) {
		return hatKompetenzUpdate.value && (data instanceof GostKursklausur);
	}

	const blocken = async () => {
		loading.value = true;
		showModalAutomatischBlocken.value = false;
		const daten = new GostKlausurterminblockungDaten();
		daten.kursklausuren = state.manager.kursklausurOhneTerminGetMengeByAbijahrAndHalbjahrAndQuartal(state.jahrgangsdaten.abiturjahr, state.halbjahr, state.quartal);
		daten.konfiguration.modusQuartale = KlausurterminblockungModusQuartale.GETRENNT.id;
		daten.konfiguration.algorithmus = algMode.value.id;
		daten.konfiguration.modusKursarten = lkgkMode.value.id;
		daten.konfiguration.regelBeiTerminenGleicheLehrkraftFachKursart = blockeGleicheLehrkraft.value;
		await state.blockenKursklausuren(daten);
		loading.value = false;
	};

	const klausurCssClasses = (kl: GostKlausurplanungDragData, termin: GostKlausurtermin | undefined) => {
		const klausur = kl as GostKursklausur;
		const konfliktZuEigenemTermin = termin === undefined ? false : state.manager.konflikteAnzahlZuEigenemTerminGetByKursklausur(klausur) > 0;
		const konfliktBeiDragInTermin = termin === undefined ? false : kursklausurHatKonfliktBeiDragInTermin(klausur, termin);
		return {
			"bg-ui-danger text-ui-ondanger": konfliktZuEigenemTermin || konfliktBeiDragInTermin,
		};
	};

	function kursklausurHatKonfliktBeiDragInTermin(klausur: GostKursklausur, termin: GostKlausurtermin): boolean {
		if (!(dragData.value instanceof GostKursklausur) || !state.manager.kursklausurPasstInTermin(termin, dragData.value)) {
			return false;
		}
		for (const konflikt of state.manager.konflikteNeuKursklausurSchuelerByTerminAndKursklausur(termin, dragData.value)) {
			if (konflikt.a.id === klausur.id) {
				return true;
			}
		}
		return false;
	}

	const isMounted = ref(false);
	onMounted(() => {
		isMounted.value = true;
		scrollSelectedTerminIntoView(state.selectedTermin);
		globalThis.addEventListener('click', handleClick);
	});

	onUnmounted(() => {
		globalThis.removeEventListener('click', handleClick);
	});

	function handleClick(e: MouseEvent) {
		if (state.selectedTermin === undefined) {
			return;
		}
		let target = e.target as HTMLElement | null;
		let isInsideTermin = false;
		while (target) {
			if (target.classList.contains("gost_klausurtermin") || target.classList.contains("tooltip")) {
				isInsideTermin = true;
				break;
			}
			target = target.parentElement;
		}
		if (!isInsideTermin) {
			props.gotoSchienen(undefined).catch(() => {});
		}
	}
</script>
