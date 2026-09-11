<template>
	<div v-if="zeitraster !== undefined" class="page grid grid-cols-1 content-start gap-6 overflow-auto">
		<svws-ui-content-card title="Allgemeine Daten" class="col-span-full">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input :disabled="loading || !hatKompetenzAendern" placeholder="Bezeichnung" :model-value="zeitraster.bezeichnung ?? ''" @change="patchBezeichnung" span="full" required />
				<div class="col-span-full grid min-w-0 grid-cols-2 gap-4">
					<svws-ui-text-input :disabled="loading || !hatKompetenzAendern" placeholder="Gültig von" :model-value="zeitraster.gueltigVon" @change="patchGueltigVon" type="date" required />
					<svws-ui-text-input :disabled="loading || !hatKompetenzAendern" placeholder="Gültig bis" :model-value="zeitraster.gueltigBis" @change="patchGueltigBis" type="date" />
				</div>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<div class="svws-ui-stundenplan svws-ui-stundenplan--mode-planung svws-ohne-zeitachse" v-if="listZeitrasterEintraege.length > 0">
			<div class="svws-ui-stundenplan--head">
				<div class="inline-flex gap-1 items-center justify-center opacity-50 text-sm font-bold pb-0.5" />
				<div v-for="wochentag in wochentagRange" :key="wochentag.id" @click="updateSelected(wochentag)" class="svws-wochentag-label group" :class="{ 'svws-selected': selected === wochentag }">
					<span class="px-2 py-1 rounded-xs group group-hover:bg-ui-25!"> {{ wochentag.beschreibung }}</span>
				</div>
			</div>
			<div class="svws-ui-stundenplan--body">
				<div class="svws-ui-stundenplan--zeitraster svws-zeitachse">
					<div v-for="stunde in stundenRange" :key="stunde" @click="updateSelected(stunde)" class="svws-ui-stundenplan--stunde text-center justify-center cursor-pointer" :class="{ 'svws-selected-stunde': selected === stunde }">
						<div class="text-headline-sm">
							{{ stunde }}.&nbsp;Stunde
						</div>
					</div>
				</div>
				<div class="svws-ui-stundenplan--zeitraster" v-for="wochentag in wochentagRange" :key="wochentag.id" :class="{ 'svws-selected': selected === wochentag }">
					<template v-for="eintrag in getEintraegeByWochentag(wochentag.id)" :key="eintrag.id">
						<div class="svws-ui-stundenplan--stunde cursor-pointer" @click="updateSelected(eintrag)" :style="posZeitraster(eintrag.stunde)" :class="{ 'svws-selected': isEintragSelected(eintrag) }">
							<div class="svws-ui-stundenplan--unterricht">
								<span>{{ eintrag.stunde }}</span>
								<div class="flex content-start">
									{{ minutesToTimeString(eintrag.beginn) }}–{{ minutesToTimeString(eintrag.ende) }}
								</div>
							</div>
						</div>
					</template>
				</div>
			</div>
		</div>
		<div v-else class="text-ui-secondary">Es wurden noch keine Zeitraster-Einträge für dieses Zeitraster angelegt.</div>

		<div class="min-w-0 flex flex-col gap-4">
			<div class="flex gap-3 flex-wrap justify-stretch">
				<svws-ui-button :disabled="loading || !hatKompetenzAendern" class="grow" type="secondary" @click="addStunde">
					<span class="icon i-ri-calendar-event-line" />
					<span class="icon i-ri-add-line" />{{ maxStunde + 1 }}. Stunde
				</svws-ui-button>
				<svws-ui-button :disabled="loading || !hatKompetenzAendern" class="grow" type="secondary" @click="addWochentag" v-if="maxWochentag < 7">
					<span class="icon i-ri-calendar-event-line" />
					<span class="icon i-ri-add-line" />{{ nextWochentagName }}
				</svws-ui-button>
			</div>

			<ui-card icon="i-ri-add-line" title="Alle Zeitraster erstellen" :is-open="actionZeitraster" @update:is-open="(isOpen) => actionZeitraster = isOpen">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input :disabled="loading || !hatKompetenzAendern" placeholder="Unterrichtsbeginn" :model-value="minutesToTimeString(settings.beginn)" @change="updateBeginn" type="time" />
					<svws-ui-input-number :disabled="loading || !hatKompetenzAendern" placeholder="Stundendauer (min)" :model-value="settings.dauer" @change="v => settings.dauer = v ?? 45" :min="5" :max="120" />
					<svws-ui-input-number :disabled="loading || !hatKompetenzAendern" placeholder="Pausenzeit (min)" :model-value="settings.pause" @change="v => settings.pause = v ?? 5" :min="0" :max="60" />
					<svws-ui-input-number :disabled="loading || !hatKompetenzAendern" placeholder="Anzahl Stunden" :model-value="settings.stundenMax" @change="v => settings.stundenMax = v ?? 6" :min="1" :max="12" />
					<svws-ui-button :disabled="loading || !hatKompetenzAendern" type="secondary" @click="addBlock" :title="`Alle Zeitraster Montag - Freitag, 1.-${settings.stundenMax}. Stunde erstellen`">
						<span class="icon i-ri-calendar-event-line" />
						<span class="icon i-ri-add-line" />Mo-Fr / 1.-{{ settings.stundenMax }}. erstellen
					</svws-ui-button>
				</svws-ui-input-wrapper>
			</ui-card>

			<div class="flex gap-3 flex-wrap justify-stretch">
				<svws-ui-button class="grow" type="secondary" @click="exportJSON" title="Alle Zeitrastereinträge als JSON-Datei exportieren">
					<span class="icon i-ri-upload-2-line" />Exportieren
				</svws-ui-button>
				<svws-ui-button :disabled="loading || !hatKompetenzAendern" class="grow" type="secondary" @click="triggerImport" title="Zeitrastereinträge aus einer JSON-Datei importieren">
					<span class="icon i-ri-download-2-line" />Importieren...
				</svws-ui-button>
				<label class="hidden" for="uv-zeitraster-import-json">JSON-Datei</label>
				<input id="uv-zeitraster-import-json" ref="fileInput" type="file" accept=".json" class="hidden" @change="importJSON">
			</div>

			<div class="flex flex-wrap items-center gap-3 border-t border-ui pt-3" aria-label="Aktionen zur Rasterauswahl">
				<span class="text-sm text-ui-secondary">{{ auswahlText }}</span>
				<svws-ui-button type="secondary" :disabled="loading || !hatKompetenzAendern || !isUvZeitrasterEintrag(selected)" @click="openZeitDialog">Zeiten bearbeiten</svws-ui-button>
				<svws-ui-button type="danger" :disabled="loading || !hatKompetenzAendern || selected === undefined" @click="deleteAuswahl">{{ loeschText }}</svws-ui-button>
			</div>
			<svws-ui-modal v-model:show="showZeitDialog" :auto-close="!dialogLoading" :close-in-title="!dialogLoading">
				<template #modalTitle>Zeiten bearbeiten</template>
				<template #modalContent>
					<p class="mb-3">{{ zeitEntwurf === null ? '' : Wochentag.fromIDorException(zeitEntwurf.wochentag).beschreibung + ', ' + zeitEntwurf.stunde + '. Stunde' }}</p>
					<svws-ui-notification v-if="dialogError" type="error" class="mb-3">{{ dialogError }}</svws-ui-notification>
					<svws-ui-input-wrapper :grid="2">
						<svws-ui-text-input v-model="zeitBeginn" type="time" placeholder="Beginn" required :disabled="dialogLoading" />
						<svws-ui-text-input v-model="zeitEnde" type="time" placeholder="Ende" required :disabled="dialogLoading" />
					</svws-ui-input-wrapper>
				</template>
				<template #modalActions>
					<svws-ui-button type="secondary" :disabled="dialogLoading" @click="showZeitDialog = false">Abbrechen</svws-ui-button>
					<svws-ui-button :disabled="dialogLoading || !hatKompetenzAendern" @click="saveZeitDialog">Speichern</svws-ui-button>
				</template>
			</svws-ui-modal>
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
	import { useUvState } from "@ui/states/UvState";

	const props = defineProps<{
		schulform: Schulform | undefined;
		zeitraster: UvZeitraster | undefined;
		selected: Wochentag | number | UvZeitrasterEintrag | undefined;
		setSelection: (value: Wochentag | number | UvZeitrasterEintrag | undefined) => void;
	}>();
	const state = useUvState();
	const hatKompetenzAendern = computed(() => state.hatKompetenzAendern);
	const loading = ref(false);
	async function patchBezeichnung(bezeichnung: string | null | undefined): Promise<void> {
		const zeitraster = props.zeitraster;
		if (zeitraster === undefined) {
			return;
		}
		await state.patchZeitraster(zeitraster.id, { bezeichnung: bezeichnung ?? '' });
	}

	async function patchGueltigVon(gueltigVon: string | null | undefined): Promise<void> {
		const zeitraster = props.zeitraster;
		if (zeitraster === undefined) {
			return;
		}
		await state.patchZeitraster(zeitraster.id, { gueltigVon: gueltigVon ?? '' });
	}

	async function patchGueltigBis(gueltigBis: string | null | undefined): Promise<void> {
		const zeitraster = props.zeitraster;
		if (zeitraster === undefined) {
			return;
		}
		await state.patchZeitraster(zeitraster.id, { gueltigBis: gueltigBis ?? null });
	}

	const showZeitDialog = ref(false);
	const dialogLoading = ref(false);
	const dialogError = ref('');
	const zeitEntwurf = ref<UvZeitrasterEintrag | null>(null);
	const zeitBeginn = ref<string | null>('');
	const zeitEnde = ref<string | null>('');
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
	function openZeitDialog() {
		if (!isUvZeitrasterEintrag(props.selected) || !hatKompetenzAendern.value || loading.value) {
			return;
		}
		zeitEntwurf.value = Object.assign(new UvZeitrasterEintrag(), props.selected);
		zeitBeginn.value = minutesToTimeString(props.selected.beginn);
		zeitEnde.value = minutesToTimeString(props.selected.ende);
		dialogError.value = '';
		showZeitDialog.value = true;
	}
	async function saveZeitDialog() {
		if (dialogLoading.value || !hatKompetenzAendern.value || zeitEntwurf.value === null) {
			return;
		}
		const beginn = timeStringToMinutes(zeitBeginn.value);
		const ende = timeStringToMinutes(zeitEnde.value);
		if (beginn === null || ende === null || ende <= beginn) {
			dialogError.value = 'Bitte gültige Zeiten eingeben. Das Ende muss nach dem Beginn liegen.';
			return;
		}
		dialogLoading.value = true;
		dialogError.value = '';
		try {
			await state.patchZeitrasterEintrag(zeitEntwurf.value.idZeitraster, zeitEntwurf.value.id, { beginn, ende });
			showZeitDialog.value = false;
		} catch {
			dialogError.value = 'Speichern fehlgeschlagen. Die Eingaben bleiben erhalten.';
		} finally {
			dialogLoading.value = false;
		}
	}

	const actionZeitraster = ref(false);
	const fileInput = ref<HTMLInputElement | null>(null);

	const settings = ref({
		beginn: 480, // 8:00
		dauer: 45,
		pause: 5,
		// stundenMax: props.schulform?.kuerzel === 'G' ? 6 : 9,
		stundenMax: 9,
	});

	const listZeitrasterEintraege = computed<UvZeitrasterEintrag[]>(() => {
		if (props.zeitraster === undefined) {
			return [];
		}
		return [...state.uvManager.zeitrasterEintragGetMengeByZeitraster(props.zeitraster.id)];
	});

	const maxStunde = computed<number>(() => {
		if (listZeitrasterEintraege.value.length === 0) {
			return 0;
		}
		return Math.max(...listZeitrasterEintraege.value.map(e => e.stunde));
	});

	const maxWochentag = computed<number>(() => {
		if (listZeitrasterEintraege.value.length === 0) {
			return 0;
		}
		return Math.max(...listZeitrasterEintraege.value.map(e => e.wochentag));
	});

	const minWochentag = computed<number>(() => {
		if (listZeitrasterEintraege.value.length === 0) {
			return 1;
		}
		return Math.min(...listZeitrasterEintraege.value.map(e => e.wochentag));
	});

	const wochentagRange = computed<Wochentag[]>(() => {
		const min = minWochentag.value;
		const max = maxWochentag.value;
		const result: Wochentag[] = [];
		for (let i = min; i <= max; i++) {
			if (i >= 1 && i <= 7) {
				result.push(Wochentag.fromIDorException(i));
			}
		}
		return result;
	});

	function getEintraegeByWochentag(wochentag: number): UvZeitrasterEintrag[] {
		return listZeitrasterEintraege.value.filter(e => e.wochentag === wochentag);
	}

	const stundenRange = computed(() => {
		const max = maxStunde.value;
		if (max === 0) {
			return [];
		}
		return Array.from({ length: max }, (_, i) => i + 1);
	});

	const nextWochentagName = computed(() => {
		const next = maxWochentag.value + 1;
		if (next < 1 || next > 7) {
			return '';
		}
		return Wochentag.fromIDorException(next).kuerzel;
	});

	function updateSelected(event: Wochentag | number | UvZeitrasterEintrag) {
		if (event === props.selected) {
			props.setSelection(undefined);
		} else {
			props.setSelection(event);
		}
	}

	function isEintragSelected(eintrag: UvZeitrasterEintrag): boolean {
		if (props.selected === undefined) {
			return false;
		}
		if (isUvZeitrasterEintrag(props.selected)) {
			return props.selected.id === eintrag.id;
		}
		if (typeof props.selected === 'number') {
			return props.selected === eintrag.stunde;
		}
		return false;
	}

	function isUvZeitrasterEintrag(value: unknown): value is UvZeitrasterEintrag {
		return value instanceof UvZeitrasterEintrag;
	}

	function isWochentag(value: unknown): value is Wochentag {
		return value instanceof Wochentag;
	}

	function posZeitraster(stunde: number) {
		return {
			gridRowStart: stunde,
			gridRowEnd: stunde + 1,
		};
	}

	function minutesToTimeString(minutes: number): string {
		const h = Math.floor(minutes / 60);
		const m = minutes % 60;
		return `${h.toString().padStart(2, '0')}:${m.toString().padStart(2, '0')}`;
	}

	function timeStringToMinutes(time: string | null): number | null {
		if (time === null) {
			return null;
		}
		const [h, m] = time.split(':').map(Number);
		return (h * 60) + m;
	}

	function updateBeginn(value: string | null) {
		const min = timeStringToMinutes(value);
		if (min !== null) {
			settings.value.beginn = min;
		}
	}

	function getEintraegeByStunde(stunde: number): UvZeitrasterEintrag[] {
		return listZeitrasterEintraege.value.filter(e => e.stunde === stunde);
	}

	async function addStundeImpl() {
		if (props.zeitraster === undefined) {
			return;
		}
		const neueStunde = maxStunde.value + 1;
		const wochentagMin = minWochentag.value;
		const wochentagMax = maxWochentag.value;
		const letzterEintrag = listZeitrasterEintraege.value.find(e => e.stunde === maxStunde.value);
		const beginn = letzterEintrag ? letzterEintrag.ende + settings.value.pause : settings.value.beginn;

		const eintraege: Partial<UvZeitrasterEintrag>[] = [];
		for (let tag = wochentagMin; tag <= wochentagMax; tag++) {
			eintraege.push({
				idZeitraster: props.zeitraster.id,
				wochentag: tag,
				stunde: neueStunde,
				beginn: beginn,
				ende: beginn + settings.value.dauer,
			});
		}
		await state.addZeitrasterEintraege(eintraege);
	}

	async function addWochentagImpl() {
		if (props.zeitraster === undefined) {
			return;
		}
		const neuerTag = maxWochentag.value + 1;
		if (neuerTag > 7) {
			return;
		}
		const stundeMax = maxStunde.value;

		const eintraege: Partial<UvZeitrasterEintrag>[] = [];
		for (let stunde = 1; stunde <= stundeMax; stunde++) {
			const refEintrag = listZeitrasterEintraege.value.find(e => e.stunde === stunde);
			eintraege.push({
				idZeitraster: props.zeitraster.id,
				wochentag: neuerTag,
				stunde,
				beginn: refEintrag?.beginn ?? settings.value.beginn + ((stunde - 1) * (settings.value.dauer + settings.value.pause)),
				ende: refEintrag?.ende ?? settings.value.beginn + ((stunde - 1) * (settings.value.dauer + settings.value.pause)) + settings.value.dauer,
			});
		}
		await state.addZeitrasterEintraege(eintraege);
	}

	async function addBlockImpl() {
		if (props.zeitraster === undefined) {
			return;
		}
		await state.createZeitrasterBlock({
			idZeitraster: props.zeitraster.id,
			stundeMax: settings.value.stundenMax,
			beginn: settings.value.beginn,
			dauer: settings.value.dauer,
			pause: settings.value.pause,
		});
		actionZeitraster.value = false;
	}

	async function deleteSelectedEintragImpl() {
		const zeitraster = props.zeitraster;
		if (!isUvZeitrasterEintrag(props.selected) || zeitraster === undefined) {
			return;
		}
		await state.deleteZeitrasterEintraege(zeitraster.id, [props.selected.id]);
		props.setSelection(undefined);
	}

	async function deleteWochentagImpl(wochentag: Wochentag) {
		const zeitraster = props.zeitraster;
		if (zeitraster === undefined) {
			return;
		}
		const ids = getEintraegeByWochentag(wochentag.id).map(e => e.id);
		if (ids.length === 0) {
			return;
		}
		await state.deleteZeitrasterEintraege(zeitraster.id, ids);
		props.setSelection(undefined);
	}

	async function deleteStundeImpl(stunde: number) {
		const zeitraster = props.zeitraster;
		if (zeitraster === undefined) {
			return;
		}
		const ids = getEintraegeByStunde(stunde).map(e => e.id);
		if (ids.length === 0) {
			return;
		}
		await state.deleteZeitrasterEintraege(zeitraster.id, ids);
		props.setSelection(undefined);
	}

	function exportJSON() {
		const arr = listZeitrasterEintraege.value.map(e => UvZeitrasterEintrag.transpilerToJSON(e));
		const blob = new Blob(['[' + arr.join(',') + ']'], { type: "application/json" });
		const link = document.createElement("a");
		link.href = URL.createObjectURL(blob);
		link.download = `ExportZeitraster_${props.zeitraster?.bezeichnung ?? 'export'}.json`;
		link.target = "_blank";
		link.click();
		URL.revokeObjectURL(link.href);
	}

	function triggerImport() {
		fileInput.value?.click();
	}

	async function importJSONImpl(event: Event) {
		const target = event.target as HTMLInputElement;
		const file = target.files?.[0];
		const zeitraster = props.zeitraster;
		if (!file || zeitraster === undefined) {
			return;
		}

		const text = await file.text();
		const data = JSON.parse(text) as Array<{ wochentag: number; stunde: number; beginn: number; ende: number }>;

		const eintraege: Partial<UvZeitrasterEintrag>[] = data.map(d => ({
			idZeitraster: zeitraster.id,
			wochentag: d.wochentag,
			stunde: d.stunde,
			beginn: d.beginn,
			ende: d.ende,
		}));

		await state.addZeitrasterEintraege(eintraege);
		target.value = '';
	}

	async function addStunde() {
		if (loading.value || !hatKompetenzAendern.value || props.zeitraster === undefined) {
			return;
		}
		loading.value = true;
		try {
			await addStundeImpl();
		} catch {
			// Die Eingaben bleiben für einen erneuten Versuch erhalten.
		} finally {
			loading.value = false;
		}
	}

	async function addWochentag() {
		if (loading.value || !hatKompetenzAendern.value || props.zeitraster === undefined) {
			return;
		}
		loading.value = true;
		try {
			await addWochentagImpl();
		} catch {
			// Die Eingaben bleiben für einen erneuten Versuch erhalten.
		} finally {
			loading.value = false;
		}
	}

	async function addBlock() {
		if (loading.value || !hatKompetenzAendern.value || props.zeitraster === undefined) {
			return;
		}
		loading.value = true;
		try {
			await addBlockImpl();
		} catch {
			// Die Eingaben bleiben für einen erneuten Versuch erhalten.
		} finally {
			loading.value = false;
		}
	}

	async function deleteSelectedEintrag() {
		if (loading.value || !hatKompetenzAendern.value || props.zeitraster === undefined) {
			return;
		}
		loading.value = true;
		try {
			await deleteSelectedEintragImpl();
		} catch {
			// Die Auswahl bleibt für einen erneuten Versuch erhalten.
		} finally {
			loading.value = false;
		}
	}

	async function deleteWochentag(wochentag: Wochentag) {
		if (loading.value || !hatKompetenzAendern.value || props.zeitraster === undefined) {
			return;
		}
		loading.value = true;
		try {
			await deleteWochentagImpl(wochentag);
		} catch {
			// Die Auswahl bleibt für einen erneuten Versuch erhalten.
		} finally {
			loading.value = false;
		}
	}

	async function deleteStunde(stunde: number) {
		if (loading.value || !hatKompetenzAendern.value || props.zeitraster === undefined) {
			return;
		}
		loading.value = true;
		try {
			await deleteStundeImpl(stunde);
		} catch {
			// Die Auswahl bleibt für einen erneuten Versuch erhalten.
		} finally {
			loading.value = false;
		}
	}



	async function importJSON(event: Event) {
		if (loading.value || !hatKompetenzAendern.value || props.zeitraster === undefined) {
			return;
		}
		loading.value = true;
		try {
			await importJSONImpl(event);
		} catch {
			// Die ausgewählte Datei bleibt für einen erneuten Versuch erhalten.
		} finally {
			loading.value = false;
		}
	}

</script>
