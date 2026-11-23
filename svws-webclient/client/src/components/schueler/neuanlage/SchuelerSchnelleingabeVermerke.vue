<template>
	<svws-ui-content-card title="Vermerke anlegen" class="col-span-full">
		<ui-table-grid class="max-h-72! max-w-full [&_tbody>tr]:cursor-pointer"
			:manager="() => gridManager"
			name="Vermerke">
			<template #header>
				<template v-for="col of gridManager.cols.values()" :key="col.name">
					<th v-if="col.kuerzel === 'Auswahl'" class="flex items-start justify-center">
						<svws-ui-checkbox :model-value="bulkAllChecked"
							:indeterminate="bulkIntermediate"
							:disabled="gridManager.daten.size() === 0"
							aria-label="Alle Vermerke auswählen"
							@update:model-value="toggleBulkAll" />
					</th>
					<th v-else-if="col.kuerzel === 'RowActions'" />
					<th v-else class="flex items-start justify-center">
						{{ col.kuerzel }}
					</th>
				</template>
			</template>
			<template #default="{ row }">
				<td v-if="!readonly" class="flex items-start justify-center" @click.stop>
					<svws-ui-checkbox :model-value="selectedVermerke.includes(row)"
						@update:model-value="toggleBulkSelection(row, $event)" />
				</td>
				<td class="flex items-start justify-center" @click="patch(row)">{{ bezeichnung(row.idVermerkart) }}</td>
				<td class="flex items-start justify-center" @click="patch(row)">{{ row.bemerkung }}</td>
				<td class="flex items-start justify-center" @click="patch(row)">{{ row.angelegtVon }}</td>
				<td class="flex items-start justify-center" @click="patch(row)">{{ row.geaendertVon }}</td>
				<td class="flex items-start justify-center" @click="patch(row)">{{ row.datum }}</td>
				<td v-if="!readonly" class="flex items-start justify-center" @click.stop>
					<ui-table-actions :actions="getRowActions(row)" />
				</td>
			</template>
			<template v-if="!readonly" #footer>
				<td class="col-span-full">
					<div class="w-full flex items-center justify-end py-1">
						<ui-table-actions :actions="footerActions" always-visible />
					</div>
				</td>
			</template>
		</ui-table-grid>
		<svws-ui-modal v-if="model !== null" :show="modalIsShown" @update:show="closeModal">
			<template #modalTitle>{{ (currentMode === Mode.PATCH) ? "Vermerk bearbeiten" : "Vermerk hinzufügen" }}</template>
			<template #modalContent>
				<ui-select label="Vermerkart"
					v-model="model.vermerkart.value"
					:manager="vermerkartenManager"
					:validation="() => model!.getFehler('idVermerkart')"
					:removable="false" required :readonly />
				<svws-ui-textarea-input placeholder="Bemerkung" class="col-span-full"
					v-model="model.proxy.bemerkung"
					:validation="() => model!.getFehler('bemerkung')"
					:autoresize="true" :readonly />
				<svws-ui-notification type="warning" v-if="keineVermerkartenVorhanden">
					Die Liste der Vermerkarten ist leer. Es sollte mindestens eine Vermerkart unter Schule/Kataloge angelegt werden, damit zusätzliche Vermerke
					eine gültige Zuordnung haben.
				</svws-ui-notification>
				<div class="mt-7 flex flex-row gap-4 justify-end">
					<svws-ui-button type="secondary"
						@click="closeModal">
						Abbrechen
					</svws-ui-button>
					<svws-ui-button @click="sendRequest"
						:disabled="saveDisabled">
						Speichern
					</svws-ui-button>
				</div>
			</template>
		</svws-ui-modal>
	</svws-ui-content-card>
</template>
<script setup lang="ts">

	import { computed, ref, shallowRef, watch } from "vue";

	import { SchuelerVermerke } from "@core/core/data/schueler/SchuelerVermerke";
	import { ArrayList } from "@core/java/util/ArrayList";
	import type { List } from "@core/java/util/List";
	import { useVermerkartenState } from "@ui/states/kataloge/VermerkartenState";
	import { SelectManager } from "@ui/ui/controls/select/manager/SelectManager";
	import { GridManager } from "@ui/ui/controls/tablegrid/GridManager";
	import type { TableAction } from "@ui/ui/controls/tablegrid/UiTableActions.vue";
	import type { SchuelerSchnelleingabeManager } from "@ui/ui/manager/schueler/SchuelerSchnelleingabeManager";

	import { SchuelerVermerkeModelProxy } from "../vermerke/modelProxy/SchuelerVermerkeModelProxy";

	const props = defineProps<{
		manager: () => SchuelerSchnelleingabeManager;
		getVermerke: () => List<SchuelerVermerke>;
		addVermerk: (data: Partial<SchuelerVermerke>) => Promise<void>;
		patchVermerk: (data: Partial<SchuelerVermerke>, idEintrag: number) => Promise<boolean>;
		deleteVermerke: (idsEintraege: List<number>) => Promise<void>;
		readonly: boolean;
	}>();

	const vermerkartenState = useVermerkartenState();
	const manager = () => props.manager();
	const selectedVermerke = shallowRef<SchuelerVermerke[]>([]);
	const keineVermerkartenVorhanden = computed(() => vermerkartenState.vermerkarten.list.size() === 0);
	const model = shallowRef<SchuelerVermerkeModelProxy | null>(null);
	const saveDisabled = computed<boolean>(() =>
		(model.value === null) || model.value.hatBlockierendeFehler() || keineVermerkartenVorhanden.value || props.readonly
	);

	const vermerkartenManager = new SelectManager({
		options: computed(() => vermerkartenState.vermerkarten.list),
		optionDisplayText: i => i.bezeichnung ?? "",
		selectionDisplayText: i => i.bezeichnung ?? "",
	});

	// --- requests ---

	function add() {
		const neuerVermerk = new SchuelerVermerke();
		neuerVermerk.idVermerkart = vermerkartenState.vermerkarten.byId.values().next().value?.id ?? null;
		model.value = new SchuelerVermerkeModelProxy(() => neuerVermerk, false);
		setMode(Mode.ADD);
		openModal();
	}

	async function sendRequest() {
		if (model.value === null) {
			return;
		}
		if (currentMode.value === Mode.ADD) {
			const { id, datum, angelegtVon, geaendertVon, ...partialDataWithoutId } = model.value.proxy;
			partialDataWithoutId.idSchueler = manager().stammdaten.id;
			await props.addVermerk(partialDataWithoutId);
		}
		if (currentMode.value === Mode.PATCH) {
			await model.value.patch();
		}
		enterDefaultMode();
	}

	function patch(vermerkEintrag: SchuelerVermerke) {
		model.value = new SchuelerVermerkeModelProxy(
			() => vermerkEintrag,
			false,
			(data) => props.patchVermerk(data, vermerkEintrag.id)
		);
		setMode(Mode.PATCH);
		openModal();
	}

	async function deleteEntries(eintraege: SchuelerVermerke[]) {
		if (eintraege.length === 0) {
			return;
		}
		const ids = new ArrayList<number>();
		for (const s of eintraege) {
			ids.add(s.id);
		}
		await props.deleteVermerke(ids);
		selectedVermerke.value = [];
	}

	// --- modal ---
	const modalIsShown = ref<boolean>(false);

	function openModal() {
		modalIsShown.value = true;
	}

	function closeModal() {
		model.value = null;
		setMode(Mode.DEFAULT);
		modalIsShown.value = false;
	}

	// --- mode ---

	enum Mode { ADD, PATCH, DEFAULT }

	const currentMode = ref<Mode>(Mode.DEFAULT);

	function setMode(newMode: Mode) {
		currentMode.value = newMode;
	}

	function enterDefaultMode() {
		setMode(Mode.DEFAULT);
		closeModal();
		model.value = null;
	}

	// --- util ---

	function bezeichnung(idVermerkArt: number | null): string {
		return vermerkartenState.vermerkarten.byId.get(idVermerkArt ?? -1)?.bezeichnung ?? "";
	}

	const gridManager = computed(() => new GridManager<number, SchuelerVermerke, List<SchuelerVermerke>>({
		daten: computed(() => props.getVermerke()),
		getRowKey: vermerkEintrag => vermerkEintrag.id,
		columns: [
			...(props.readonly ? [] : [{ kuerzel: "Auswahl", name: "Auswahl", width: "2rem", hideable: false }]),
			{ kuerzel: "Vermerkart", name: "Vermerkart", width: "minmax(15rem, 1fr)" },
			{ kuerzel: "Bemerkung", name: "Bemerkung", width: "minmax(10rem, 2fr)" },
			{ kuerzel: "Angelegt von", name: "Angelegt von", width: "minmax(6rem, 1fr)" },
			{ kuerzel: "Geändert von", name: "Geändert von", width: "minmax(6rem, 1fr)" },
			{ kuerzel: "Erstellt am", name: "Erstellt am", width: "minmax(5rem, 7rem)" },
			...(props.readonly ? [] : [{ kuerzel: "RowActions", name: "Zeilenaktionen", width: "4rem", hideable: false }]),
		],
	}));

	watch(() => props.getVermerke(), (liste) => {
		const ids = new Set(selectedVermerke.value.map(v => v.id));
		selectedVermerke.value = [...liste].filter(v => ids.has(v.id));
	});

	const bulkAllChecked = computed(() =>
		(selectedVermerke.value.length === gridManager.value.daten.size())
		&& (selectedVermerke.value.length > 0)
	);

	const bulkIntermediate = computed(() =>
		(selectedVermerke.value.length > 0)
		&& (selectedVermerke.value.length < gridManager.value.daten.size())
	);

	function toggleBulkAll(value: boolean): void {
		selectedVermerke.value = value ? [...gridManager.value.daten] : [];
	}

	function toggleBulkSelection(vermerkEintrag: SchuelerVermerke, value: boolean): void {
		selectedVermerke.value = value
			? [...selectedVermerke.value, vermerkEintrag]
			: selectedVermerke.value.filter(v => v.id !== vermerkEintrag.id);
	}

	function getRowActions(vermerkEintrag: SchuelerVermerke): TableAction[] {
		return [
			{
				label: "Vermerk bearbeiten",
				iconClasses: "i-ri-edit-2-line",
				action: () => patch(vermerkEintrag),
			},
			{
				label: "Vermerk löschen",
				trash: true,
				action: () => void deleteEntries([vermerkEintrag]),
			},
		];
	}

	const footerActions = computed<TableAction[]>(() => [
		{
			label: "Vermerk hinzufügen",
			iconClasses: "i-ri-add-line",
			action: () => add(),
		}, {
			label: "Ausgewählte Vermerke löschen",
			trash: true,
			action: () => void deleteEntries(selectedVermerke.value),
			disabled: selectedVermerke.value.length === 0,
		},
	]);

</script>
