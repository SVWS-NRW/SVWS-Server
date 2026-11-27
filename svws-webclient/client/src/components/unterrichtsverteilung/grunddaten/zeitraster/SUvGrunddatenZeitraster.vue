<template>
	<div v-if="zeitraster !== undefined" class="page grid grid-cols-1 content-start gap-6 overflow-auto">
		<svws-ui-notification v-if="fehler" type="error">{{ fehler }}</svws-ui-notification>
		<svws-ui-content-card title="Allgemeine Daten" class="col-span-full">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input :disabled="loading || !hatKompetenzAendern"
					placeholder="Bezeichnung"
					:model-value="zeitraster.bezeichnung ?? ''"
					@change="patchBezeichnung"
					span="full"
					required />
				<div class="col-span-full grid min-w-0 grid-cols-2 gap-4">
					<svws-ui-text-input :disabled="loading || !hatKompetenzAendern"
						placeholder="Gültig von"
						:model-value="zeitraster.gueltigVon"
						@change="patchGueltigVon"
						type="date"
						required />
					<svws-ui-text-input :disabled="loading || !hatKompetenzAendern"
						placeholder="Gültig bis"
						:model-value="zeitraster.gueltigBis"
						@change="patchGueltigBis"
						type="date" />
				</div>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<s-uv-zeitraster-ansicht v-if="listZeitrasterEintraege.length > 0"
			:tage="rasterTage"
			:stunden="grenzen.stunden"
			:selected
			:set-selection />
		<div v-else class="text-ui-secondary">Es wurden noch keine Zeitraster-Einträge für dieses Zeitraster angelegt.</div>

		<div class="min-w-0 flex flex-col gap-4">
			<div class="flex gap-3 flex-wrap justify-stretch">
				<svws-ui-button :disabled="loading || !hatKompetenzAendern || listZeitrasterEintraege.length === 0"
					class="grow"
					type="secondary"
					@click="addStunde">
					<span class="icon i-ri-calendar-event-line" />
					<span class="icon i-ri-add-line" />{{ maxStunde + 1 }}. Stunde
				</svws-ui-button>
				<svws-ui-button :disabled="loading || !hatKompetenzAendern || listZeitrasterEintraege.length === 0"
					class="grow"
					type="secondary"
					@click="addWochentag"
					v-if="maxWochentag < 7">
					<span class="icon i-ri-calendar-event-line" />
					<span class="icon i-ri-add-line" />{{ nextWochentagName }}
				</svws-ui-button>
			</div>

			<ui-card icon="i-ri-add-line"
				title="Alle Zeitraster erstellen"
				:is-open="actionZeitraster"
				@update:is-open="(isOpen) => actionZeitraster = isOpen">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input :disabled="loading || !hatKompetenzAendern"
						placeholder="Unterrichtsbeginn"
						:model-value="DateUtils.gibZeitStringOfMinuten(settings.beginn)"
						@change="updateBeginn"
						type="time" />
					<svws-ui-input-number :disabled="loading || !hatKompetenzAendern"
						placeholder="Stundendauer (min)"
						:model-value="settings.dauer"
						@change="v => settings.dauer = v ?? 45"
						:min="5"
						:max="120" />
					<svws-ui-input-number :disabled="loading || !hatKompetenzAendern"
						placeholder="Pausenzeit (min)"
						:model-value="settings.pause"
						@change="v => settings.pause = v ?? 5"
						:min="0"
						:max="60" />
					<svws-ui-input-number :disabled="loading || !hatKompetenzAendern"
						placeholder="Anzahl Stunden"
						:model-value="settings.stundenMax"
						@change="v => settings.stundenMax = v ?? 6"
						:min="1"
						:max="12" />
					<svws-ui-button :disabled="loading || !hatKompetenzAendern"
						type="secondary"
						@click="addBlock"
						:title="`Alle Zeitraster Montag - Freitag, 1.-${settings.stundenMax}. Stunde erstellen`">
						<span class="icon i-ri-calendar-event-line" />
						<span class="icon i-ri-add-line" />Mo-Fr / 1.-{{ settings.stundenMax }}. erstellen
					</svws-ui-button>
				</svws-ui-input-wrapper>
			</ui-card>

			<div class="flex flex-wrap items-center gap-3 border-t border-ui pt-3" aria-label="Aktionen zur Rasterauswahl">
				<span class="text-sm text-ui-secondary">{{ auswahlText }}</span>
				<svws-ui-button type="secondary"
					:disabled="loading || !hatKompetenzAendern || !isUvZeitrasterEintrag(selected)"
					@click="openZeitDialog">
					Zeiten bearbeiten
				</svws-ui-button>
				<svws-ui-button type="trash" :title="loeschText" :disabled="loading || !hatKompetenzAendern || selected === undefined" @click="deleteAuswahl" />
			</div>
			<s-uv-zeitraster-eintrag-dialog v-model:show="showZeitDialog"
				:eintrag="zeitEintrag"
				:darf-speichern="hatKompetenzAendern"
				:speichern="speichereEintragZeiten" />
		</div>
	</div>
	<div v-else class="page">
		<div class="text-ui-secondary">Kein Zeitraster ausgewählt.</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref, watch } from "vue";

	import type { Schulform } from "@core/asd/types/schule/Schulform";
	import type { UvZeitraster } from "@core/core/data/uv/UvZeitraster";
	import { UvZeitrasterEintrag } from "@core/core/data/uv/UvZeitrasterEintrag";
	import { Wochentag } from "@core/core/types/Wochentag";
	import { DateUtils } from "@core/core/utils/DateUtils";
	import { UvManager } from "@core/core/utils/uv/UvManager";
	import { useUvState } from "@ui/states/UvState";

	import { createUvPresenter } from "../../UvPresenter";

	import SUvZeitrasterAnsicht from "./SUvZeitrasterAnsicht.vue";
	import SUvZeitrasterEintragDialog from "./SUvZeitrasterEintragDialog.vue";

	const props = defineProps<{
		schulform: Schulform | undefined;
		zeitraster: UvZeitraster | undefined;
		selected: Wochentag | number | UvZeitrasterEintrag | undefined;
		setSelection: (value: Wochentag | number | UvZeitrasterEintrag | undefined) => void;
	}>();
	const state = useUvState();
	const presenter = computed(() => createUvPresenter(state.uvManager));
	const hatKompetenzAendern = computed(() => state.hatKompetenzAendern);
	const loading = ref(false);
	const fehler = ref('');
	watch(() => props.zeitraster?.id, () => {
		fehler.value = '';
	});
	// Grunddaten des Zeitrasters

	async function patchBezeichnung(bezeichnung: string | null | undefined): Promise<void> {
		const zeitraster = props.zeitraster;
		if (zeitraster === undefined) {
			return;
		}
		await state.grunddaten.patchZeitraster(zeitraster.id, { bezeichnung: bezeichnung ?? '' });
	}

	async function patchGueltigVon(gueltigVon: string | null | undefined): Promise<void> {
		const zeitraster = props.zeitraster;
		if (zeitraster === undefined) {
			return;
		}
		await state.grunddaten.patchZeitraster(zeitraster.id, { gueltigVon: gueltigVon ?? '' });
	}

	async function patchGueltigBis(gueltigBis: string | null | undefined): Promise<void> {
		const zeitraster = props.zeitraster;
		if (zeitraster === undefined) {
			return;
		}
		await state.grunddaten.patchZeitraster(zeitraster.id, { gueltigBis: gueltigBis ?? null });
	}

	const showZeitDialog = ref(false);
	const zeitEintrag = ref<UvZeitrasterEintrag | null>(null);

	watch(() => props.zeitraster?.id, () => {
		showZeitDialog.value = false;
	});
	const auswahlText = computed(() => {
		const item = props.selected;
		if (isUvZeitrasterEintrag(item)) {
			return `${Wochentag.fromIDorException(item.wochentag).beschreibung}, ${item.stunde}. Stunde`;
		}
		if (isWochentag(item)) {
			return `${item.beschreibung}: ${getEintraegeByWochentag(item.id).length} Einträge`;
		}
		if (typeof item === 'number') {
			return `${item}. Stunde: ${getEintraegeByStunde(item).length} Einträge`;
		}
		return 'Eintrag, Wochentag oder Stunde im Raster auswählen';
	});
	const loeschText = computed(() => {
		if (isWochentag(props.selected)) {
			return 'Tageseinträge löschen';
		}
		if (typeof props.selected === 'number') {
			return 'Stundeneinträge löschen';
		}
		return 'Eintrag löschen';
	});
	async function deleteAuswahl() {
		if (isWochentag(props.selected)) {
			await deleteWochentag(props.selected);
		} else if (typeof props.selected === 'number') {
			await deleteStunde(props.selected);
		} else {
			await deleteSelectedEintrag();
		}
	}
	// Einzelnen Zeitabschnitt im Dialog bearbeiten

	function openZeitDialog(): void {
		if (!isUvZeitrasterEintrag(props.selected) || !hatKompetenzAendern.value || loading.value) {
			return;
		}
		zeitEintrag.value = props.selected;
		showZeitDialog.value = true;
	}

	async function speichereEintragZeiten(eintrag: UvZeitrasterEintrag, beginn: number, ende: number): Promise<void> {
		await state.grunddaten.patchZeitrasterEintrag(eintrag.idZeitraster, eintrag.id, { beginn, ende });
	}

	const actionZeitraster = ref(false);

	const settings = ref({
		beginn: 480, // 8:00
		dauer: 45,
		pause: 5,
		stundenMax: 9,
	});

	const listZeitrasterEintraege = computed<UvZeitrasterEintrag[]>(() => {
		if (props.zeitraster === undefined) {
			return [];
		}
		return [...state.uvManager.zeitrasterEintragGetMengeByZeitraster(props.zeitraster.id)];
	});

	const grenzen = computed(() => props.zeitraster === undefined ? { maxStunde: 0, maxWochentag: 0, wochentage: [], stunden: [], naechsterWochentag: '' } : presenter.value.zeitrasterAnsicht(props.zeitraster.id));
	const maxStunde = computed(() => grenzen.value.maxStunde);
	const maxWochentag = computed(() => grenzen.value.maxWochentag);
	const wochentagRange = computed(() => grenzen.value.wochentage);

	// Rasterpositionen und Auswahl

	function getEintraegeByWochentag(wochentag: number): UvZeitrasterEintrag[] {
		return props.zeitraster === undefined ? [] : [...state.uvManager.zeitrasterEintragGetMengeByZeitrasterAndWochentag(props.zeitraster.id, wochentag)];
	}

	const rasterTage = computed(() => wochentagRange.value.map(wochentag => ({ wochentag, eintraege: getEintraegeByWochentag(wochentag.id) })));
	const nextWochentagName = computed(() => grenzen.value.naechsterWochentag);

	function isUvZeitrasterEintrag(value: unknown): value is UvZeitrasterEintrag {
		return value instanceof UvZeitrasterEintrag;
	}

	function isWochentag(value: unknown): value is Wochentag {
		return value instanceof Wochentag;
	}

	function uhrzeitAlsMinuten(value: string | null): number | null {
		if (value === null || !/^([01]\d|2[0-3]):[0-5]\d$/.test(value)) {
			return null;
		}
		return DateUtils.gibMinutenOfZeitAsString(value);
	}

	function updateBeginn(value: string | null) {
		const min = uhrzeitAlsMinuten(value);
		if (min !== null) {
			settings.value.beginn = min;
		}
	}

	function getEintraegeByStunde(stunde: number): UvZeitrasterEintrag[] {
		return props.zeitraster === undefined ? [] : [...state.uvManager.zeitrasterEintragGetMengeByZeitrasterAndStunde(props.zeitraster.id, stunde)];
	}

	type NeuerZeitrasterEintrag = {
		idZeitraster: number;
		wochentag: number;
		stunde: number;
		beginn: number;
		ende: number;
	};

	// Neue Rastereinträge berechnen und gemeinsam speichern

	function pruefeZeitEinstellungen(): void {
		const { beginn, dauer, pause } = settings.value;
		if (!Number.isInteger(beginn) || beginn < 0 || beginn >= 1440 || !Number.isInteger(dauer) || dauer <= 0 || dauer >= 1440
			|| !Number.isInteger(pause) || pause < 0 || pause >= 1440) {
			throw new Error('Bitte einen gültigen Unterrichtsbeginn, eine positive Stundendauer und eine nicht negative Pause in ganzen Minuten eingeben.');
		}
	}

	async function speichereNeueEintraege(eintraege: NeuerZeitrasterEintrag[]): Promise<void> {
		if (eintraege.length === 0) {
			return;
		}
		for (const eintrag of eintraege) {
			if (UvManager.zeitrasterEintragZeitenGueltig(eintrag.beginn, eintrag.ende) === false) {
				throw new Error('Die Zeiten müssen zwischen 00:00 und 23:59 liegen; das Ende muss nach dem Beginn liegen.');
			}
			state.uvManager.zeitrasterEintragPruefePosition(eintrag.idZeitraster, eintrag.wochentag, eintrag.stunde);
		}
		await state.grunddaten.addZeitrasterEintraege(eintraege);
	}

	async function addStundeImpl() {
		const zeitraster = props.zeitraster;
		if (zeitraster === undefined) {
			return;
		}
		pruefeZeitEinstellungen();
		const { dauer, pause } = settings.value;
		const eintraege: NeuerZeitrasterEintrag[] = [];
		for (const tag of wochentagRange.value) {
			const letzter = getEintraegeByWochentag(tag.id).at(-1);
			if (letzter !== undefined) {
				const beginn = letzter.ende + pause;
				eintraege.push({ idZeitraster: zeitraster.id, wochentag: tag.id, stunde: maxStunde.value + 1, beginn, ende: beginn + dauer });
			}
		}
		await speichereNeueEintraege(eintraege);
	}

	async function addWochentagImpl() {
		const zeitraster = props.zeitraster;
		if (zeitraster === undefined || maxWochentag.value >= 7) {
			return;
		}
		pruefeZeitEinstellungen();
		const { beginn, dauer, pause } = settings.value;
		const eintraege: NeuerZeitrasterEintrag[] = [];
		for (const stunde of grenzen.value.stunden) {
			const referenz = getEintraegeByStunde(stunde).at(0);
			const start = referenz?.beginn ?? beginn + ((stunde - 1) * (dauer + pause));
			eintraege.push({ idZeitraster: zeitraster.id, wochentag: maxWochentag.value + 1, stunde, beginn: start, ende: referenz?.ende ?? start + dauer });
		}
		await speichereNeueEintraege(eintraege);
	}

	async function addBlockImpl() {
		const zeitraster = props.zeitraster;
		if (zeitraster === undefined) {
			return;
		}
		pruefeZeitEinstellungen();
		const { beginn, dauer, pause, stundenMax } = settings.value;
		if (!Number.isInteger(stundenMax) || stundenMax < 1 || stundenMax > 12) {
			throw new Error('Bitte eine Stundenanzahl zwischen 1 und 12 eingeben.');
		}
		const eintraege: NeuerZeitrasterEintrag[] = [];
		for (let tag = 1; tag <= 5; tag++) {
			for (let stunde = 1; stunde <= stundenMax; stunde++) {
				const start = beginn + ((stunde - 1) * (dauer + pause));
				eintraege.push({ idZeitraster: zeitraster.id, wochentag: tag, stunde, beginn: start, ende: start + dauer });
			}
		}
		await speichereNeueEintraege(eintraege);
		actionZeitraster.value = false;
	}

	// Einträge, Tage und Stunden aus dem Raster entfernen

	async function deleteSelectedEintragImpl() {
		const zeitraster = props.zeitraster;
		if (!isUvZeitrasterEintrag(props.selected) || zeitraster === undefined) {
			return;
		}
		await state.grunddaten.deleteZeitrasterEintraege(zeitraster.id, [props.selected.id]);
		props.setSelection(undefined);
	}

	async function deleteWochentagImpl(wochentag: Wochentag) {
		const zeitraster = props.zeitraster;
		if (zeitraster === undefined) {
			return;
		}
		const ids = getEintraegeByWochentag(wochentag.id).map(eintrag => eintrag.id);
		if (ids.length === 0) {
			return;
		}
		await state.grunddaten.deleteZeitrasterEintraege(zeitraster.id, ids);
		props.setSelection(undefined);
	}

	async function deleteStundeImpl(stunde: number) {
		const zeitraster = props.zeitraster;
		if (zeitraster === undefined) {
			return;
		}
		const ids = getEintraegeByStunde(stunde).map(eintrag => eintrag.id);
		if (ids.length === 0) {
			return;
		}
		await state.grunddaten.deleteZeitrasterEintraege(zeitraster.id, ids);
		props.setSelection(undefined);
	}

	// Rasteraktionen mit Ladezustand und Fehlermeldung

	async function addStunde() {
		if (loading.value || !hatKompetenzAendern.value || props.zeitraster === undefined) {
			return;
		}
		loading.value = true;
		fehler.value = '';
		try {
			await addStundeImpl();
		} catch (error) {
			fehler.value = error instanceof Error ? error.message : 'Die Änderung konnte nicht gespeichert werden.';
		} finally {
			loading.value = false;
		}
	}

	async function addWochentag() {
		if (loading.value || !hatKompetenzAendern.value || props.zeitraster === undefined) {
			return;
		}
		loading.value = true;
		fehler.value = '';
		try {
			await addWochentagImpl();
		} catch (error) {
			fehler.value = error instanceof Error ? error.message : 'Die Änderung konnte nicht gespeichert werden.';
		} finally {
			loading.value = false;
		}
	}

	async function addBlock() {
		if (loading.value || !hatKompetenzAendern.value || props.zeitraster === undefined) {
			return;
		}
		loading.value = true;
		fehler.value = '';
		try {
			await addBlockImpl();
		} catch (error) {
			fehler.value = error instanceof Error ? error.message : 'Die Änderung konnte nicht gespeichert werden.';
		} finally {
			loading.value = false;
		}
	}

	async function deleteSelectedEintrag() {
		if (loading.value || !hatKompetenzAendern.value || props.zeitraster === undefined) {
			return;
		}
		loading.value = true;
		fehler.value = '';
		try {
			await deleteSelectedEintragImpl();
		} catch (error) {
			fehler.value = error instanceof Error ? error.message : 'Die Auswahl konnte nicht gelöscht werden.';
		} finally {
			loading.value = false;
		}
	}

	async function deleteWochentag(wochentag: Wochentag) {
		if (loading.value || !hatKompetenzAendern.value || props.zeitraster === undefined) {
			return;
		}
		loading.value = true;
		fehler.value = '';
		try {
			await deleteWochentagImpl(wochentag);
		} catch (error) {
			fehler.value = error instanceof Error ? error.message : 'Die Auswahl konnte nicht gelöscht werden.';
		} finally {
			loading.value = false;
		}
	}

	async function deleteStunde(stunde: number) {
		if (loading.value || !hatKompetenzAendern.value || props.zeitraster === undefined) {
			return;
		}
		loading.value = true;
		fehler.value = '';
		try {
			await deleteStundeImpl(stunde);
		} catch (error) {
			fehler.value = error instanceof Error ? error.message : 'Die Auswahl konnte nicht gelöscht werden.';
		} finally {
			loading.value = false;
		}
	}


</script>
