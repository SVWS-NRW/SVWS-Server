<template>
	<ui-table-grid v-if="gridManager.daten.size() !== 0"
		name="Schulinterne Funktionen" :manager="() => gridManager" hide-selection>
		<template #header>
			<template v-for="column in gridColumns" :key="`header-${column.kuerzel}`">
				<th v-if="column.kuerzel !== 'buttons'" class="text-left">{{ column.name }}</th>
				<th v-else />
			</template>
		</template>
		<template #default="{ row }">
			<td class="text-left pb-1">
				{{ getFunktionText(row) }}
			</td>
			<td class="pr-3 pt-1 pb-1">
				<ui-table-actions v-if="!readonly" :actions="rowActions(row)" />
			</td>
		</template>
		<template #footer>
			<td class="col-span-full my-1 pr-3">
				<ui-table-actions v-if="!readonly" :actions="footerActions" always-visible />
			</td>
		</template>
	</ui-table-grid>

	<div v-else>
		<div v-if="katalogIstLeer">Keine Funktionen im Katalog angelegt.</div>
		<svws-ui-button v-else-if="!readonly" @click="openModal" type="secondary">Funktion hinzufügen</svws-ui-button>
		<div v-else>Keine Funktionen zugeordnet.</div>
	</div>
	<svws-ui-modal :show="modalIsOpen"
		@update:show="closeModal"
		:auto-close="false" size="small">
		<template #modalTitle>Funktion zuordnen</template>
		<template #modalContent>
			<ui-select-multi label="Funktionen"
				v-model="funktionenToAdd"
				:manager="funktionSelectManager"
				required />
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="closeModal">Abbrechen</svws-ui-button>
			<svws-ui-button @click="addSelectedFunktionen" :disabled="funktionenToAdd.length === 0">Hinzufügen</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";

	import type { LehrerFunktion } from "@core/asd/data/lehrer/LehrerFunktion";
	import type { SchulinterneFunktion } from "@core/core/data/schule/SchulinterneFunktion";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { ArrayList } from "@core/java/util/ArrayList";
	import type { List } from "@core/java/util/List";
	import { useAbschnittState } from "@ui/states/AbschnittState";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { useSchulinterneFunktionenState } from "@ui/states/kataloge/SchulinterneFunktionenState";
	import { SelectManager } from "@ui/ui/controls/select/manager/SelectManager";
	import { GridManager } from "@ui/ui/controls/tablegrid/GridManager";
	import type { TableAction } from "@ui/ui/controls/tablegrid/UiTableActions.vue";

	import { useLehrerAuswahlState } from "~/states/lehrer/LehrerAuswahlState";

	const benutzerState = useBenutzerState();
	const lehrerAuswahlState = useLehrerAuswahlState();
	const schulinterneFunktionenState = useSchulinterneFunktionenState();
	const abschnittState = useAbschnittState();

	const readonly = computed<boolean>(() => !benutzerState.benutzerHatKompetenz(BenutzerKompetenz.LEHRER_PERSONALDATEN_AENDERN));

	//# region ------------------------ Table ------------------------
	const gridColumns = [
		{ kuerzel: "funktion", name: "Funktion", width: "minmax(20rem,50rem)", hideable: false },
		{ kuerzel: "buttons", name: "Buttons", width: "4rem", hideable: false },
	];

	const footerActions = computed<TableAction[]>(() => [
		{ label: "Funktion hinzufügen", action: openModal, iconClasses: "i-ri-add-line", disabled: availableFunktionenToAdd.value.length === 0 },
	]);

	const gridManager = new GridManager<number, LehrerFunktion, List<LehrerFunktion>>({
		daten: computed<List<LehrerFunktion>>(() => {
			const funktionen = lehrerAuswahlState.manager.getAbschnittBySchuljahresabschnittsId(abschnittState.auswahl.id)?.funktionen;
			// 'funktionen' wird in LehrerAuswahlStateImpl in-place mutiert (keine Änderung der Objektreferenz). Durch dem
			// shallowRef (StateManager) gibt es daher in computed kein Update! Daher ist nötig, bei allen Änderungen
			// eine Kopie von ArrayList zu erstellen
			return funktionen === undefined ? new ArrayList() : new ArrayList(funktionen);
		}),
		getRowKey: row => row.id,
		columns: gridColumns,
	});

	function getFunktionText(row: LehrerFunktion): string {
		return schulinterneFunktionenState.schulinterneFunktionen.byId.get(row.idFunktion)?.bezeichnung ?? "—";
	}

	function rowActions(row: LehrerFunktion): TableAction[] {
		return [{ label: "Funktion Löschen", action: () => lehrerAuswahlState.removeFunktion(row), trash: true }];
	}
	//# endregion

	//# region ----------------------- Funktion hinzufügen ------------------------
	const modalIsOpen = ref<boolean>(false);
	const funktionenToAdd = ref<SchulinterneFunktion[]>([]);

	/** Gibt an, ob der Katalog der schulinternen Funktionen überhaupt keine Einträge enthält */
	const katalogIstLeer = computed<boolean>(() => schulinterneFunktionenState.schulinterneFunktionen.list.isEmpty());

	/** Liste an verfügbarer Funktionen - die noch nicht bereits hinzugefügt wurden */
	const availableFunktionenToAdd = computed<SchulinterneFunktion[]>(() => {
		const abschnitt = lehrerAuswahlState.manager.getAbschnittBySchuljahresabschnittsId(abschnittState.auswahl.id);
		const assignedIds = new Set(abschnitt === null ? [] : [...abschnitt.funktionen].map(funktion => funktion.idFunktion));
		return [...schulinterneFunktionenState.schulinterneFunktionen.list].filter(funktion => funktion.istSichtbar && !assignedIds.has(funktion.id));
	});

	const funktionDisplayText = (f: SchulinterneFunktion) => f.bezeichnung;
	const funktionSelectManager = new SelectManager<SchulinterneFunktion>({
		options: availableFunktionenToAdd,
		optionDisplayText: funktionDisplayText,
		selectionDisplayText: funktionDisplayText,
		sort: (a, b) => a.bezeichnung.localeCompare(b.bezeichnung),
	});

	function openModal() {
		funktionenToAdd.value = [];
		modalIsOpen.value = true;
	}

	function closeModal() {
		funktionenToAdd.value = [];
		modalIsOpen.value = false;
	}

	async function addSelectedFunktionen(): Promise<void> {
		if (funktionenToAdd.value.length === 0) {
			return;
		}
		const idAbschnittsdaten = lehrerAuswahlState.manager.getAbschnittBySchuljahresabschnittsId(abschnittState.auswahl.id)?.id;
		if (idAbschnittsdaten === undefined) {
			return;
		}
		const data = new ArrayList<Partial<LehrerFunktion>>();
		for (const funktion of funktionenToAdd.value) {
			data.add({ idAbschnittsdaten, idFunktion: funktion.id });
		}
		await lehrerAuswahlState.addFunktionen(data);
		closeModal();
	}
	//# endregion

</script>
