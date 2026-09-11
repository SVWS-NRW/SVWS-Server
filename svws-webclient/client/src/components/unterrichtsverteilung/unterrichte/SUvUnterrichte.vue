<template>
	<div v-if="unterricht !== undefined" class="page page-grid-cards">
		<svws-ui-content-card class="col-span-full">
			<template #title>
				<div class="content-card--header"><h3 class="content-card--headline" title="Daten des Unterrichts">Daten des Unterrichts</h3>&nbsp;<svws-ui-badge type="light" title="ID" class="font-mono" size="small"> ID: {{ unterricht.id }} </svws-ui-badge></div>
			</template>
			<div class="mb-3 flex flex-wrap items-center gap-2">Lerngruppe <svws-ui-button type="icon" title="Lerngruppe öffnen" @click="gotoLerngruppe(unterricht.idLerngruppe)"><span class="icon i-ri-link" /></svws-ui-button></div>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Lerngruppe" :model-value="lerngruppeBezeichnung" readonly />
				<svws-ui-text-input placeholder="Fach" :model-value="fachBezeichnung" readonly />
				<svws-ui-select :readonly="!hatKompetenzAendern" :disabled="loading" title="Zeitraster" :key="unterricht.id" :items="zeitrasterEintraege" :item-text="formatZeitrasterLang"
					:model-value="selectedZeitrasterEintrag" @update:model-value="patchZeitraster" removable />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Lehrkräfte des Unterrichts" class="col-span-full">
			<svws-ui-table :items="unterrichtLerngruppenlehrer" :columns="columnsUnterrichtLerngruppenlehrer" :selectable="hatKompetenzAendern && !loading" v-model="selectedUnterrichtLerngruppenlehrer" count>
				<template #cell(kuerzel)="{ rowData }">
					{{ getLehrerKuerzelByUnterrichtLerngruppenlehrer(rowData) }} <svws-ui-button type="icon" title="UV-Lehrkraft öffnen" @click="gotoLehrerByZuordnung(rowData)"><span class="icon i-ri-link" /></svws-ui-button>
				</template>
				<template #cell(reihenfolge)="{ rowData }">
					{{ getLerngruppenlehrerByUnterrichtLerngruppenlehrer(rowData)?.reihenfolge ?? "—" }}
				</template>
				<template #cell(wochenstunden)="{ rowData }">
					{{ getLerngruppenlehrerByUnterrichtLerngruppenlehrer(rowData)?.wochenstunden ?? "—" }}
				</template>
				<template #actions v-if="hatKompetenzAendern">
					<svws-ui-select :disabled="loading" :model-value="undefined" @update:model-value="onAddUnterrichtLerngruppenlehrer"
						headless indeterminate autocomplete :items="availableUnterrichtLerngruppenlehrer" removable
						title="Lehrkraft hinzufügen..."
						:item-text="formatUnterrichtLerngruppenlehrerItem" />
					<svws-ui-button @click="removeUnterrichtLerngruppenlehrer" type="trash" :disabled="loading || !selectedUnterrichtLerngruppenlehrer.length" title="Ausgewählte Zuordnungen entfernen" />
				</template>
				<template #noData>
					Keine Lehrkräfte zugeordnet
				</template>
			</svws-ui-table>
		</svws-ui-content-card>
		<svws-ui-content-card title="Räume des Unterrichts" class="col-span-full">
			<svws-ui-table :items="unterrichtRaeume" :columns="columnsUnterrichtRaeume" :selectable="hatKompetenzAendern && !loading" v-model="selectedUnterrichtRaeume" count>
				<template #cell(kuerzel)="{ rowData }">
					{{ getRaumKuerzelByUnterrichtRaum(rowData) }} <svws-ui-button type="icon" title="Raum öffnen" @click="gotoRaum(rowData.idRaum)"><span class="icon i-ri-link" /></svws-ui-button>
				</template>
				<template #cell(bezeichnung)="{ rowData }">
					{{ getRaumBezeichnungByUnterrichtRaum(rowData) }}
				</template>
				<template #actions v-if="hatKompetenzAendern">
					<svws-ui-select :disabled="loading" :model-value="undefined" @update:model-value="onAddUnterrichtRaum"
						headless indeterminate autocomplete :items="availableUnterrichtRaeume" removable
						title="Raum hinzufügen..."
						:item-text="formatUnterrichtRaumItem" />
					<svws-ui-button @click="removeUnterrichtRaum" type="trash" :disabled="loading || !selectedUnterrichtRaeume.length" title="Ausgewählte Zuordnungen entfernen" />
				</template>
				<template #noData>
					Keine Räume zugeordnet
				</template>
			</svws-ui-table>
		</svws-ui-content-card>
	</div>
	<div v-else class="page page-flex-row max-w-480">
		<span class="text-ui-secondary">Kein Unterricht ausgewählt</span>
	</div>
</template>

<script setup lang="ts">
	import { computed, ref, shallowRef, watch } from "vue";

	import type { UvLerngruppenLehrer } from "@core/core/data/uv/UvLerngruppenLehrer";
	import type { UvRaum } from "@core/core/data/uv/UvRaum";
	import type { UvUnterricht } from "@core/core/data/uv/UvUnterricht";
	import type { UvUnterrichtLerngruppenlehrer } from "@core/core/data/uv/UvUnterrichtLerngruppenlehrer";
	import type { UvUnterrichtRaum } from "@core/core/data/uv/UvUnterrichtRaum";
	import type { UvZeitrasterEintrag } from "@core/core/data/uv/UvZeitrasterEintrag";
	import { ArrayList } from "@core/java/util/ArrayList";
	import { useUvState } from "@ui/states/UvState";

	import { useUvPresenter } from "../UvPresenter";

	const { unterricht, gotoLehrer } = defineProps<{
		unterricht: UvUnterricht | undefined;
		gotoLerngruppe: (id: number) => Promise<void>;
		gotoLehrer: (id: number) => Promise<void>;
		gotoRaum: (id: number) => Promise<void>;
	}>();

	const state = useUvState();
	const presenter = useUvPresenter(state.uvManager);
	const hatKompetenzAendern = computed(() => state.hatKompetenzAendern);
	const loading = ref(false);
	const zeitDraft = shallowRef<number | null | undefined>();
	const zeitId = computed(() => zeitDraft.value === undefined ? unterricht?.idZeitrasterEintrag : zeitDraft.value);
	watch(() => unterricht?.id, () => {
		zeitDraft.value = undefined;
		selectedUnterrichtLerngruppenlehrer.value = [];
		selectedUnterrichtRaeume.value = [];
	});

	const wochentageLang = ["", "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag"];

	const lerngruppeBezeichnung = computed(() => {
		if (unterricht === undefined) {
			return "";
		}
		const lerngruppe = state.uvManager.lerngruppeGetByIdOrNull(unterricht.idLerngruppe);
		return lerngruppe === null ? `LG-ID: ${unterricht.idLerngruppe}` : presenter.lerngruppeBezeichnung(lerngruppe);
	});

	const fachBezeichnung = computed(() => {
		if (unterricht === undefined) {
			return "";
		}
		const lerngruppe = state.uvManager.lerngruppeGetByIdOrNull(unterricht.idLerngruppe);
		return lerngruppe === null ? "—" : presenter.lerngruppeFach(lerngruppe);
	});

	const zeitrasterEintraege = computed<UvZeitrasterEintrag[]>(() => {
		if (state.planungsabschnitt === null || state.planungsabschnitt.id === -1) {
			return [];
		}
		const result: UvZeitrasterEintrag[] = [];
		for (const zr of state.uvManager.zeitrasterGetMengeByPlanungsabschnitt(state.planungsabschnitt)) {
			result.push(...state.uvManager.zeitrasterEintragGetMengeByZeitraster(zr.id));
		}
		return result;
	});

	const selectedZeitrasterEintrag = computed<UvZeitrasterEintrag | null>(() => {
		if (unterricht === undefined || zeitId.value === null || zeitId.value === undefined) {
			return null;
		}
		if (state.planungsabschnitt === null) {
			return null;
		}
		for (const zr of state.uvManager.zeitrasterGetMengeByPlanungsabschnitt(state.planungsabschnitt)) {
			try {
				return state.uvManager.zeitrasterEintragGetByIdOrException(zr.id, zeitId.value);
			} catch { /* naechstes Zeitraster versuchen */ }
		}
		return null;
	});

	function formatZeitrasterLang(e: UvZeitrasterEintrag): string {
		return `${wochentageLang[e.wochentag] ?? "?"} - ${e.stunde}. Stunde`;
	}

	async function patchZeitraster(eintrag: UvZeitrasterEintrag | null | undefined) {
		if (unterricht === undefined) {
			return;
		}
		if (!hatKompetenzAendern.value || loading.value) {
			return;
		}
		zeitDraft.value = eintrag?.id ?? null;
		const id = unterricht.id;
		const idUnterricht = unterricht.id;
		const idZeitrasterEintrag = zeitDraft.value;
		loading.value = true;
		try {
			await state.patchUnterricht(id, { idZeitrasterEintrag });
			if (unterricht.id === idUnterricht) {
				zeitDraft.value = undefined;
			}
		} finally {
			loading.value = false;
		}
	}

	// Lehrkraefte des Unterrichts
	const columnsUnterrichtLerngruppenlehrer = [
		{ key: "kuerzel", label: "Kürzel", sortable: true },
		{ key: "reihenfolge", label: "Reihenfolge", sortable: true, span: 0.4 },
		{ key: "wochenstunden", label: "Wochenstunden", sortable: true, span: 0.5 },
	];

	const selectedUnterrichtLerngruppenlehrer = ref<UvUnterrichtLerngruppenlehrer[]>([]);

	const unterrichtLerngruppenlehrer = computed(() => {
		if (unterricht === undefined) {
			return new ArrayList<UvUnterrichtLerngruppenlehrer>();
		}
		return state.uvManager.unterrichtLerngruppenlehrerGetMengeByUnterricht(unterricht);
	});

	function getLerngruppenlehrerByUnterrichtLerngruppenlehrer(
		zuordnung: UvUnterrichtLerngruppenlehrer
	): UvLerngruppenLehrer | undefined {
		return [...state.uvManager.lerngruppenLehrerGetMengeAsList()].find(lgl => lgl.id === zuordnung.idLerngruppenLehrer);
	}

	function getLehrerKuerzelByUnterrichtLerngruppenlehrer(zuordnung: UvUnterrichtLerngruppenlehrer): string {
		const lgl = getLerngruppenlehrerByUnterrichtLerngruppenlehrer(zuordnung);
		if (lgl === undefined) {
			return `ID: ${zuordnung.idLerngruppenLehrer}`;
		}
		return state.uvManager.lehrerGetByLerngruppenLehrer(lgl).kuerzel;
	}

	async function gotoLehrerByZuordnung(zuordnung: UvUnterrichtLerngruppenlehrer): Promise<void> {
		const lerngruppenlehrer = getLerngruppenlehrerByUnterrichtLerngruppenlehrer(zuordnung);
		if (lerngruppenlehrer === undefined) {
			return;
		}
		await gotoLehrer(lerngruppenlehrer.idLehrer);
	}

	const availableUnterrichtLerngruppenlehrer = computed(() => {
		if (unterricht === undefined) {
			return [];
		}
		let lerngruppe;
		try {
			lerngruppe = state.uvManager.lerngruppeGetByIdOrException(unterricht.idLerngruppe);
		} catch {
			return [];
		}
		const assignedIds = new Set(
			[...unterrichtLerngruppenlehrer.value].map(ul => ul.idLerngruppenLehrer)
		);
		return [...state.uvManager.lerngruppenLehrerGetMengeByLerngruppe(lerngruppe)].filter(
			lgl => !assignedIds.has(lgl.id)
		);
	});

	function formatUnterrichtLerngruppenlehrerItem(lgl: UvLerngruppenLehrer): string {
		const lehrer = state.uvManager.lehrerGetByLerngruppenLehrer(lgl);
		return `${lehrer.nachname}, ${lehrer.vorname} (${lehrer.kuerzel})`;
	}

	async function onAddUnterrichtLerngruppenlehrer(value: unknown) {
		const lgl = value as UvLerngruppenLehrer | null | undefined;
		if (lgl === undefined || lgl === null || unterricht === undefined) {
			return;
		}
		const data = {
			idUnterricht: unterricht.id,
			idLerngruppenLehrer: lgl.id,
		};
		if (loading.value || !hatKompetenzAendern.value) {
			return;
		}
		loading.value = true;
		try {
			await state.addUnterrichtLerngruppenlehrer(data);
		} finally {
			loading.value = false;
		}
	}

	// Raeume des Unterrichts
	const columnsUnterrichtRaeume = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, span: 0.4 },
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true },
	];

	const selectedUnterrichtRaeume = ref<UvUnterrichtRaum[]>([]);

	const unterrichtRaeume = computed(() => {
		if (unterricht === undefined) {
			return new ArrayList<UvUnterrichtRaum>();
		}
		return state.uvManager.unterrichtRaumGetMengeByUnterricht(unterricht);
	});

	const availableUnterrichtRaeume = computed(() => {
		if (unterricht === undefined) {
			return [];
		}
		const assignedIds = new Set(
			[...unterrichtRaeume.value].map(ur => ur.idRaum)
		);
		return [...state.uvManager.raumGetMengeAsList()].filter(
			r => !assignedIds.has(r.id)
		);
	});

	function getRaumKuerzelByUnterrichtRaum(zuordnung: UvUnterrichtRaum): string {
		try {
			return state.uvManager.raumGetByIdOrException(zuordnung.idRaum).kuerzel;
		} catch {
			return `ID: ${zuordnung.idRaum}`;
		}
	}

	function getRaumBezeichnungByUnterrichtRaum(zuordnung: UvUnterrichtRaum): string {
		try {
			return state.uvManager.raumGetByIdOrException(zuordnung.idRaum).beschreibung ?? "";
		} catch {
			return "—";
		}
	}

	function formatUnterrichtRaumItem(raum: UvRaum): string {
		return `${raum.kuerzel} ${raum.beschreibung ?? ""}`.trim();
	}

	async function onAddUnterrichtRaum(value: unknown) {
		const raum = value as UvRaum | null | undefined;
		if (raum === undefined || raum === null || unterricht === undefined) {
			return;
		}
		const data = {
			idUnterricht: unterricht.id,
			idRaum: raum.id,
		};
		if (loading.value || !hatKompetenzAendern.value) {
			return;
		}
		loading.value = true;
		try {
			await state.addUnterrichtRaum(data);
		} finally {
			loading.value = false;
		}
	}

	async function removeUnterrichtLerngruppenlehrer() {
		const items: UvUnterrichtLerngruppenlehrer[] = [...selectedUnterrichtLerngruppenlehrer.value];
		if (items.length === 0) {
			return;
		}
		const idUnterricht = unterricht?.id;
		if (idUnterricht === undefined) {
			return;
		}
		if (loading.value || !hatKompetenzAendern.value) {
			return;
		}
		loading.value = true;
		try {
			await state.delUnterrichtLerngruppenlehrer([...items]);
			if (unterricht?.id === idUnterricht) {
				selectedUnterrichtLerngruppenlehrer.value = [];
			}
		} finally {
			loading.value = false;
		}
	}

	async function removeUnterrichtRaum() {
		const items: UvUnterrichtRaum[] = [...selectedUnterrichtRaeume.value];
		if (items.length === 0) {
			return;
		}
		const idUnterricht = unterricht?.id;
		if (idUnterricht === undefined) {
			return;
		}
		if (loading.value || !hatKompetenzAendern.value) {
			return;
		}
		loading.value = true;
		try {
			await state.delUnterrichtRaum([...items]);
			if (unterricht?.id === idUnterricht) {
				selectedUnterrichtRaeume.value = [];
			}
		} finally {
			loading.value = false;
		}
	}

</script>
