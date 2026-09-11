<template>
	<div v-if="lehrer !== undefined" class="page page-grid-cards">
		<svws-ui-content-card class="col-span-full">
			<template #title>
				<div class="flex flex-wrap items-center gap-2"><h3 class="content-card--headline" title="Daten der Lehrkraft">Daten der Lehrkraft</h3><svws-ui-badge type="light" title="ID" class="font-mono" size="big"> UV-ID: {{ lehrer.id }} </svws-ui-badge><svws-ui-badge type="light" title="ID" class="font-mono" size="big"> K-ID: {{ lehrer.idKLehrer }} </svws-ui-badge></div>
			</template>
			<svws-ui-input-wrapper :grid="2">
				<div class="flex gap-2 items-end col-span-2 max-w-96">
					<svws-ui-text-input :readonly="!hatKompetenzAendern || lehrer.idKLehrer !== null" :disabled="loading" placeholder="Kürzel" :model-value="form.kuerzel" @change="kuerzel => patch({ kuerzel: kuerzel ?? undefined })" class="w-full" />
					<svws-ui-button v-if="lehrer.idKLehrer !== null" type="icon" @click="gotoLehrer(lehrer.idKLehrer)" title="Lehrerdaten öffnen"> <span class="icon i-ri-link" /> </svws-ui-button>
				</div>
				<svws-ui-text-input :readonly="!hatKompetenzAendern || lehrer.idKLehrer !== null" :disabled="loading" placeholder="Nachname" :model-value="form.nachname" @change="nachname => patch({ nachname: nachname ?? undefined })" />
				<svws-ui-text-input :readonly="!hatKompetenzAendern || lehrer.idKLehrer !== null" :disabled="loading" placeholder="Vorname" :model-value="form.vorname" @change="vorname => patch({ vorname: vorname ?? undefined })" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>

		<svws-ui-content-card title="Unterrichtsfächer" class="col-span-full pt-5">
			<lehrer-personaldaten-unterrichtsfaecher :hat-update-kompetenz="hatKompetenzAendern && !loading"
				:lehrer-unterrichtsfaecher="getLehrerUnterrichtsfaecher"
				:map-faecher="getMapFaecher"
				:patch-lehrer-unterrichtsfach="state.patchLehrerUnterrichtsfach"
				:add-lehrer-unterrichtsfach
				:remove-lehrer-unterrichtsfach="state.removeLehrerUnterrichtsfach" />
		</svws-ui-content-card>

		<svws-ui-content-card title="Pflichtstundensoll" class="col-span-full pt-5">
			<svws-ui-table :items="listPflichtstundensoll" :columns="colsPflichtstundensoll" scroll>
				<template #cell(pflichtstdSoll)="{ rowData }">
					<svws-ui-input-number headless :steps="false" :decimal-places="2" :min="0" :model-value="rowData.pflichtstdSoll" :disabled="loading" :readonly="!hatKompetenzAendern" placeholder="Pflichtstunden" @change="value => patchPflichtstundensoll(rowData.id, value)" />
				</template>
				<template #cell(loeschen)="{ rowData }">
					<svws-ui-button v-if="hatKompetenzAendern" type="trash" title="Pflichtstundensoll löschen" :disabled="loading" @click="deletePflichtstundensoll(rowData)" />
				</template>
				<template #cell(gueltigVon)="{ rowData }">
					<svws-ui-text-input headless type="date" class="my-0 pr-2 [&.text-input--date>.svws-icon]:hidden [&.text-input--date>input::-webkit-calendar-picker-indicator]:hidden" placeholder="Gültig von" required :model-value="rowData.gueltigVon" :disabled="loading" :readonly="!hatKompetenzAendern" @change="value => patchGueltigkeit(rowData, 'gueltigVon', value)" />
				</template>
				<template #cell(gueltigBis)="{ rowData }">
					<div class="w-full" :class="{ '[&:not(:focus-within)_.text-input--headless]:opacity-0': rowData.gueltigBis === null || rowData.gueltigBis === '' }">
						<svws-ui-text-input headless type="date" class="my-0 pr-2 [&.text-input--date>.svws-icon]:hidden [&.text-input--date>input::-webkit-calendar-picker-indicator]:hidden" placeholder="Gültig bis" :model-value="rowData.gueltigBis" :disabled="loading" :readonly="!hatKompetenzAendern" @change="value => patchGueltigkeit(rowData, 'gueltigBis', value)" />
					</div>
				</template>
				<template #actions v-if="hatKompetenzAendern">
					<div class="flex w-full items-center">
						<div class="ml-auto flex items-center gap-1">
							<span v-if="lehrer.idKLehrer !== null" :title="tooltipImportPflichtstundensoll">
								<svws-ui-button type="icon" @click="handleImportPflichtstundensollFromPersonalabschnittsdaten" :disabled="loading || !canImportPflichtstundensoll" :title="canImportPflichtstundensoll ? 'Pflichtstundensoll aus Personalabschnittsdaten importieren' : undefined">
									<span class="icon i-ri-upload-2-line" />
								</svws-ui-button>
							</span>
							<s-uv-grunddaten-lehrer-pflichtstundensoll-neu-modal v-slot="{ openModal }" :lehrer>
								<svws-ui-button @click="openModal" type="icon"><span class="icon i-ri-add-line" /></svws-ui-button>
							</s-uv-grunddaten-lehrer-pflichtstundensoll-neu-modal>
						</div>
					</div>
				</template>
			</svws-ui-table>
		</svws-ui-content-card>


		<svws-ui-content-card title="Anrechnungsstunden" class="col-span-full pt-5">
			<svws-ui-table :items="listAnrechnungen" :columns="colsAnrechnungsstunden" count scroll>
				<template #cell(anrechnungsgrundKrz)="{ rowData }">
					<div class="whitespace-normal break-words">
						<div class="text-sm text-ui-secondary">{{ anrechnungsart(rowData.anrechnungsgrundKrz) }}</div>
						<div>{{ presenter.anrechnungsgrundText(abschnittState.auswahl.schuljahr, rowData.anrechnungsgrundKrz) }}</div>
					</div>
				</template>
				<template #cell(anzahlStunden)="{ rowData }">
					<svws-ui-input-number headless :steps="false" :decimal-places="2" :min="0" :model-value="rowData.anzahlStunden" :disabled="loading" :readonly="!hatKompetenzAendern" placeholder="Anrechnungsstunden" @change="value => patchAnrechnungsstunde(rowData.id, value)" />
				</template>
				<template #cell(loeschen)="{ rowData }">
					<svws-ui-button v-if="hatKompetenzAendern" type="trash" title="Anrechnungsstunden löschen" :disabled="loading" @click="deleteAnrechnungsstunden(rowData)" />
				</template>
				<template #cell(gueltigVon)="{ rowData }">
					<svws-ui-text-input headless type="date" class="my-0 pr-2 [&.text-input--date>.svws-icon]:hidden [&.text-input--date>input::-webkit-calendar-picker-indicator]:hidden" placeholder="Gültig von" required :model-value="rowData.gueltigVon" :disabled="loading" :readonly="!hatKompetenzAendern" @change="value => patchGueltigkeit(rowData, 'gueltigVon', value)" />
				</template>
				<template #cell(gueltigBis)="{ rowData }">
					<div class="w-full" :class="{ '[&:not(:focus-within)_.text-input--headless]:opacity-0': rowData.gueltigBis === null || rowData.gueltigBis === '' }">
						<svws-ui-text-input headless type="date" class="my-0 pr-2 [&.text-input--date>.svws-icon]:hidden [&.text-input--date>input::-webkit-calendar-picker-indicator]:hidden" placeholder="Gültig bis" :model-value="rowData.gueltigBis" :disabled="loading" :readonly="!hatKompetenzAendern" @change="value => patchGueltigkeit(rowData, 'gueltigBis', value)" />
					</div>
				</template>
				<template #actions v-if="hatKompetenzAendern">
					<div class="flex w-full items-center">
						<div class="ml-auto flex items-center gap-1">
							<span v-if="lehrer.idKLehrer !== null" :title="tooltipImportAnrechnungsstunden">
								<svws-ui-button type="icon" @click="handleImportAnrechnungsstundenFromPersonalabschnittsdaten" :disabled="loading || !canImportAnrechnungsstunden" :title="canImportAnrechnungsstunden ? 'Anrechnungsstunden aus Personalabschnittsdaten importieren' : undefined">
									<span class="icon i-ri-upload-2-line" />
								</svws-ui-button>
							</span>
							<s-uv-grunddaten-lehrer-anrechnungsstunden-neu-modal v-slot="{ openModal }" :lehrer>
								<svws-ui-button-select type="secondary" :dropdown-actions="[
									{ key: 1, text: 'Anrechnung', action: () => openModal(3) },
									{ key: 2, text: 'Mehrleistung', action: () => openModal(1) },
									{ key: 3, text: 'Minderleistung', action: () => openModal(2) },
								] as Iterable<Item>">
									<template #icon><span class="icon i-ri-add-line" /></template>
								</svws-ui-button-select>
							</s-uv-grunddaten-lehrer-anrechnungsstunden-neu-modal>
						</div>
					</div>
				</template>
			</svws-ui-table>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";

	import type { FachDaten } from "@core/core/data/fach/FachDaten";
	import type { LehrerUnterrichtsfach } from "@core/core/data/lehrer/LehrerUnterrichtsfach";
	import { UvLehrer } from "@core/core/data/uv/UvLehrer";
	import type { UvLehrerAnrechnungsstunden } from "@core/core/data/uv/UvLehrerAnrechnungsstunden";
	import type { UvLehrerPflichtstundensoll } from "@core/core/data/uv/UvLehrerPflichtstundensoll";
	import { ArrayList } from "@core/java/util/ArrayList";
	import { useAbschnittState } from "@ui/states/AbschnittState";
	import { useUvState } from "@ui/states/UvState";
	import type { DataTableColumn } from "@ui/types";

	import { useUvPresenter } from "../../UvPresenter";
	import LehrerPersonaldatenUnterrichtsfaecher from "~/components/lehrer/personaldaten/LehrerPersonaldatenUnterrichtsfaecher.vue";

	const props = defineProps<{
		lehrer: UvLehrer | undefined;
		gotoLehrer: (id: number) => Promise<void>;
	}>();
	const abschnittState = useAbschnittState();
	const state = useUvState();
	const presenter = useUvPresenter(state.uvManager);
	const hatKompetenzAendern = computed(() => state.hatKompetenzAendern);
	const loading = ref(false);
	const form = computed(() => props.lehrer ?? new UvLehrer());
	async function patch(changes: Partial<UvLehrer>): Promise<void> {
		if ((props.lehrer === undefined) || loading.value || !hatKompetenzAendern.value) {
			return;
		}
		loading.value = true;
		try {
			await state.patchLehrer(props.lehrer.id, changes);
		} finally {
			loading.value = false;
		}
	}

	function getLehrerUnterrichtsfaecher() {
		const result = new ArrayList<LehrerUnterrichtsfach>();
		if (props.lehrer === undefined) {
			return result;
		}
		for (const f of state.uvManager.lehrerUnterrichtsfachGetMengeByLehrer(props.lehrer)) {
			result.add(f);
		}
		return result;
	}

	function getMapFaecher(): Map<number, FachDaten> {
		const map = new Map<number, FachDaten>();
		for (const fach of state.uvManager.fachdatenGetMenge()) {
			map.set(fach.id, fach);
		}
		return map;
	}

	const listAnrechnungen = computed<UvLehrerAnrechnungsstunden[]>(() => {
		if (props.lehrer === undefined) {
			return [];
		}
		return [...state.uvManager.lehrerAnrechnungsstundenGetMengeByLehrer(props.lehrer)];
	});
	const listPflichtstundensoll = computed<UvLehrerPflichtstundensoll[]>(() => {
		if (props.lehrer === undefined) {
			return [];
		}
		return [...state.uvManager.lehrerPflichtstundensollGetMengeByLehrer(props.lehrer)];
	});
	const canImportPflichtstundensoll = computed(() => (props.lehrer !== undefined) && (listPflichtstundensoll.value.length === 0));
	const canImportAnrechnungsstunden = computed(() => (props.lehrer !== undefined) && (listAnrechnungen.value.length === 0));

	type Item = {
		text: string;
		action: () => void | Promise<any>;
		default?: boolean;
		separator?: boolean;
	};

	const tooltipImportPflichtstundensollDisabled = "Import aus Personalabschnittsdaten nur möglich, wenn noch keine Pflichtstunden-Einträge vorhanden sind.";
	const tooltipImportAnrechnungsstundenDisabled = "Import aus Personalabschnittsdaten nur möglich, wenn noch keine Anrechnungsstunden vorhanden sind.";
	const tooltipImportPflichtstundensoll = computed(() => {
		return canImportPflichtstundensoll.value ? "Pflichtstundensoll aus Personalabschnittsdaten importieren" : tooltipImportPflichtstundensollDisabled;
	});
	const tooltipImportAnrechnungsstunden = computed(() => {
		return canImportAnrechnungsstunden.value ? "Anrechnungsstunden aus Personalabschnittsdaten importieren" : tooltipImportAnrechnungsstundenDisabled;
	});

	function anrechnungsart(grund: string): string {
		if (grund.startsWith('1')) {
			return 'Mehrleistung';
		}
		if (grund.startsWith('2')) {
			return 'Minderleistung';
		}
		return 'Anrechnung';
	}

	async function patchGueltigkeit(item: UvLehrerAnrechnungsstunden | UvLehrerPflichtstundensoll,
		feld: 'gueltigVon' | 'gueltigBis', value: string | null) {
			const gueltigVon = feld === 'gueltigVon' ? value : item.gueltigVon;
			let gueltigBis = item.gueltigBis;
			if (feld === 'gueltigBis') {
				gueltigBis = value === '' ? null : value;
			}
			if (gueltigVon === null || gueltigVon === '' || (gueltigBis !== null && gueltigBis !== '' && gueltigBis < gueltigVon)) {
				return;
			}
			if (loading.value || !hatKompetenzAendern.value) {
				return;
			}
			loading.value = true;
			try {
				if ('anrechnungsgrundKrz' in item) {
					await state.patchAnrechnungsstunde(item.id, { gueltigVon, gueltigBis });
				} else {
					await state.patchPflichtstundensoll(item.id, { gueltigVon, gueltigBis });
				}
			} finally {
				loading.value = false;
			}
		}

	async function patchPflichtstundensoll(id: number, pflichtstdSoll: number | null) {
		if (pflichtstdSoll === null || loading.value || !hatKompetenzAendern.value) {
			return;
		}
		loading.value = true;
		try {
			await state.patchPflichtstundensoll(id, { pflichtstdSoll });
		} finally {
			loading.value = false;
		}
	}

	async function deletePflichtstundensoll(item: UvLehrerPflichtstundensoll) {
		if (loading.value || !hatKompetenzAendern.value) {
			return;
		}
		loading.value = true;
		try {
			await state.delPflichtstundensoll([item]);
		} finally {
			loading.value = false;
		}
	}

	async function patchAnrechnungsstunde(id: number, anzahlStunden: number | null) {
		if (anzahlStunden === null || loading.value || !hatKompetenzAendern.value) {
			return;
		}
		loading.value = true;
		try {
			await state.patchAnrechnungsstunde(id, { anzahlStunden });
		} finally {
			loading.value = false;
		}
	}

	async function deleteAnrechnungsstunden(item: UvLehrerAnrechnungsstunden) {
		if (loading.value || !hatKompetenzAendern.value) {
			return;
		}
		loading.value = true;
		try {
			await state.delAnrechnungsstunden([item]);
		} finally {
			loading.value = false;
		}
	}

	const colsAnrechnungsstunden = computed<DataTableColumn[]>(() => [
		{ key: "anrechnungsgrundKrz", label: "Anrechnung / Grund", sortable: true, defaultSort: 'asc' },
		{ key: "anzahlStunden", label: "Stunden", sortable: false, fixedWidth: 8, align: 'right' },
		{ key: "gueltigVon", label: "Gültig von", sortable: true, fixedWidth: 10, align: 'left' },
		{ key: "gueltigBis", label: "Gültig bis", sortable: true, fixedWidth: 10, align: 'left' },
		{ key: "loeschen", label: "", tooltip: "Löschen", fixedWidth: 3 },
	]);

	const colsPflichtstundensoll = computed<DataTableColumn[]>(() => [
		{ key: "pflichtstdSoll", label: "Stunden", sortable: true, defaultSort: 'asc', fixedWidth: 8, align: 'left' },
		{ key: "gueltigVon", label: "Gültig von", sortable: true, minWidth: 10, align: 'left' },
		{ key: "gueltigBis", label: "Gültig bis", sortable: true, minWidth: 10, align: 'left' },
		{ key: "loeschen", label: "", tooltip: "Löschen", fixedWidth: 3 },
	]);

	async function handleImportPflichtstundensollFromPersonalabschnittsdaten() {
		const lehrer = props.lehrer;
		if (!canImportPflichtstundensoll.value || lehrer === undefined) {
			return;
		}
		await state.importPflichtstundensollFromPersonalabschnittsdaten(lehrer);
	}

	async function handleImportAnrechnungsstundenFromPersonalabschnittsdaten() {
		const lehrer = props.lehrer;
		if (!canImportAnrechnungsstunden.value || lehrer === undefined) {
			return;
		}
		await state.importAnrechnungsstundenFromPersonalabschnittsdaten(lehrer);
	}

	async function addLehrerUnterrichtsfach(eintrag: Partial<LehrerUnterrichtsfach>) {
		if (props.lehrer === undefined || loading.value || !hatKompetenzAendern.value) {
			return;
		}
		loading.value = true;
		try {
			await state.addLehrerUnterrichtsfach(props.lehrer, eintrag);
		} finally {
			loading.value = false;
		}
	}

</script>
