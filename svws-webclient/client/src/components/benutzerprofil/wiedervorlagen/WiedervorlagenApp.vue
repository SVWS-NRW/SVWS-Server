<template>
	<!--	Header -->
	<header class="svws-ui-header">
		<div class="svws-ui-header--title">
			<div class="svws-headline-wrapper">
				<h2 class="svws-headline">
					Wiedervorlagen
				</h2>
				<span class="svws-subline inline-flex gap-x-3 gap-y-1 items-center">
					<span class="mt-1">{{ benutzerState.benutzerdaten.anzeigename }}</span>
					<svws-ui-badge type="light" title="ID" class="font-mono m-0" size="small">
						ID: {{ benutzerState.benutzerdaten.id }}
					</svws-ui-badge>
				</span>
			</div>
		</div>
		<div class="svws-ui-header--actions" />
	</header>

	<div class="page">
		<!--	Filter	-->
		<div class="bg-ui-neutral rounded-md w-full pt-1 pb-2 px-1 mb-5">
			<div class="flex flex-col md:flex-row flex-wrap lg:flex-nowrap gap-x-3 gap-y-1">
				<div class="max-w-[30em] md:max-width-auto md:basis-[30em] shrink">
					<svws-ui-text-input type="search" placeholder="Suche in Bemerkung/Name" v-model="filter.search" />
				</div>
				<div class="max-w-[30em] md:max-width-auto md:basis-[30em] shrink">
					<svws-ui-text-input type="date" placeholder="Wiedervorlage bis" v-model="filter.tsWiedervorlage" class="max-w-[30em]" />
				</div>
				<div class="flex md:basis-full lg:basis-auto md:mt-[0.6em]">
					<svws-ui-checkbox type="toggle" v-model="filter.toggleUnerledigt">
						Nur unerledigte
					</svws-ui-checkbox>
				</div>
			</div>
		</div>

		<!--	Table	-->
		<ui-table-grid name="Wiedervorlagen" :manager="() => gridManager" class="pb-6 select-text">
			<template #header>
				<template v-for="column in gridColumns" :key="`header-${column.kuerzel}`">
					<th v-if="column.kuerzel === 'auswahl'" class="flex items-start justify-center">
						<svws-ui-checkbox :model-value="bulkChecked" disabled title="Alle Wiedervorlagen an-/abwählen" />
					</th>
					<th v-else-if="column.kuerzel === 'rowActions'" />
					<th v-else class="text-left">{{ column.name }}</th>
				</template>
			</template>
			<template #default="{ row }">
				<td class="flex items-start justify-center">
					<svws-ui-checkbox :model-value="selection.includes(row)" disabled title="Wiedervorlage an-/abwählen" />
				</td>
				<td class="text-left">
					<template v-if="row.tsWiedervorlage !== null">
						{{ formatToLocalDate(getDateFromDateTime(row.tsWiedervorlage) ?? null) }}
					</template>
				</td>
				<td class="text-left">
					{{ getPerson(row.typPerson) }}
				</td>
				<td class="text-left flex flex-row">
					<template v-if="row.idPerson !== null">
						<button type="button" @click.stop="goToPerson(row)" class="button button--icon p-0! h-[1.6em]! w-[1.6em]!" title="Schüler ansehen">
							<span class="icon i-ri-link" />
						</button>
						<span>{{ row.namePerson }}</span>
					</template>
					<template v-else>—</template>
				</td>
				<td class="text-left line-clamp-6">
					{{ row.bemerkung }}
				</td>
				<td class="text-left">
					{{ row.nameBenutzerAngelegt }}
				</td>
				<td class="text-left">
					<template v-if="row.tsAngelegt !== null">
						{{ formatToLocalDate(getDateFromDateTime(row.tsAngelegt) ?? null) }}
					</template>
				</td>
				<td class="text-left">
					{{ row.nameBenutzerErledigt ?? "—" }}
				</td>
				<td class="text-left">
					<template v-if="row.tsErledigt !== null">
						{{ formatToLocalDate(getDateFromDateTime(row.tsErledigt) ?? null) }}
					</template>
					<template v-else>—</template>
				</td>
				<td class="text-left">
					{{ row.automatischErledigt ? 'an' : 'aus' }}
				</td>
				<td>
					<ui-table-actions :actions="rowActions(row)" :items="row" />
				</td>
			</template>
			<template #footer>
				<td class="col-span-full my-1">
					<ui-table-actions :actions="bulkActions" :items="selection" always-visible />
				</td>
			</template>
		</ui-table-grid>

		<!--  Texthinweise - leere Tabellen -->
		<template v-if="!hasWiedervorlagen">
			<div class="mb-6">Es liegen noch keine Wiedervorlagen vor.</div>
		</template>
		<template v-else-if="gridManager.daten.length === 0">
			<div>Mit den gesetzten Filter liegen keine Wiedervorlagen vor.</div>
			<div class="mt-2">Filter zurücksetzen, um alle Wiedervorlagen zu sehen.</div>
			<div class="mt-6 mb-8">
				<svws-ui-button @click="resetFilters">Filter zurücksetzen</svws-ui-button>
			</div>
		</template>
	</div>

	<wiedervorlage-modal v-model="modal.visible"
		type="allgemein"
		:mode="modal.status ?? undefined"
		:data="modal.data" />
</template>

<script setup lang="ts">
	import { computed, ref } from "vue";
	import { GridManager, useBenutzerState, useWiedervorlageState, type TableActions, useNotificationsState } from "@ui";
	import { WiedervorlageEintrag } from "@core";
	import { getDateFromDateTime, formatToLocalDate, formatDateToDateTime } from "~/utils/date";
	import type { WiedervorlagenAppProps } from "./WiedervorlagenAppProps";

	const props = defineProps<WiedervorlagenAppProps>();

	const benutzerState = useBenutzerState();
	const wiedervorlageState = useWiedervorlageState();
	const notificationState = useNotificationsState();

	const hasWiedervorlagen = computed(() => wiedervorlageState.wiedervorlagenListe.size() > 0);

	function getPerson(personID: null | number) {
		switch (personID) {
			case 1:
				return "Lehrkraft";
			case 2:
				return "Schüler/Schülerin";
			case 3:
				return "Erziehungsberechtigte";
			default:
				return "Allgemein";
		}
	}

	//# region ------------------------ Table ------------------------
	const gridColumns = [
		{ kuerzel: "auswahl", name: "Auswahl", width: "3rem", hideable: false },
		{ kuerzel: "tsWiedervorlage", name: "Wiedervorlage am", width: "minmax(7rem, 0.25fr)", hideable: false },
		{ kuerzel: "typPerson", name: "Art", width: "minmax(8.5rem, 0.5fr)", hideable: false },
		{ kuerzel: "idPerson", name: "Name", width: "minmax(8rem, 0.5fr)", hideable: false },
		{ kuerzel: "bemerkung", name: "Bemerkung", width: "minmax(12rem, 5fr)", hideable: false },
		{ kuerzel: "idBenutzer", name: "Angelegt von", width: "minmax(8rem, 0.5fr)", hideable: false },
		{ kuerzel: "tsAngelegt", name: "Angelegt am", width: "minmax(7rem, 0.25fr)", hideable: false },
		{ kuerzel: "idBenutzerErledigt", name: "Erledigt von", width: "minmax(8rem, 0.5fr)", hideable: false },
		{ kuerzel: "tsErledigt", name: "Erledigt am", width: "minmax(7rem, 0.25fr)", hideable: false },
		{ kuerzel: "automatischErledigt", name: "Automatisch löschen", width: "7rem", hideable: false },
		{ kuerzel: "rowActions", name: "Row-Actions", width: '7em' },
	];

	/** Prüft, ob ein Suchbegriff dem Inhalt der Felder "bemerkung" oder "namePerson" von Wiedervorlagen entspricht */
	function matchesSearch(wiedervorlage: WiedervorlageEintrag, searchValue: string): boolean {
		const matchesName = wiedervorlage.namePerson !== null && wiedervorlage.namePerson.toLowerCase().includes(searchValue);
		const matchesBemerkung = wiedervorlage.bemerkung.toLowerCase().includes(searchValue);
		return matchesName || matchesBemerkung;
	}

	/** Prüft, ob eine Wiedervorlage bis zu gewähltem Datum vorliegt */
	function matchesDate(wiedervorlage: WiedervorlageEintrag): boolean {
		const dateAsDateTime = formatDateToDateTime(filter.value.tsWiedervorlage);
		if (dateAsDateTime === undefined || wiedervorlage.tsWiedervorlage === null) {
			return false;
		}

		return wiedervorlage.tsWiedervorlage <= dateAsDateTime;
	}

	const gridManager = new GridManager<string, WiedervorlageEintrag, WiedervorlageEintrag[]>({
		daten: computed<WiedervorlageEintrag[]>(() => {
			const searchValue = filter.value.search.trim().toLowerCase();
			const wiedervorlagen: WiedervorlageEintrag[] = [];

			for (const eintrag of wiedervorlageState.wiedervorlagenListe) {
				// bei Sucheingabe auf Übereinstimmung prüfen
				if (searchValue !== "" && !matchesSearch(eintrag, searchValue)) {
					continue;
				}
				// bei Datumseingabe auf Übereinstimmung prüfen
				if (filter.value.tsWiedervorlage !== "" && !matchesDate(eintrag)) {
					continue;
				}
				// bei aktiven Toggle zeige nur unerledigte Wiedervorlagen an - sonst alle
				if (filter.value.toggleUnerledigt && eintrag.tsErledigt !== null) {
					continue;
				}
				wiedervorlagen.push(eintrag);
			}

			return wiedervorlagen;
		}),
		getRowKey: row => `${row.id}`,
		columns: gridColumns,
	});

	//# endregion

	//# region ------------------------ Filters ------------------------
	const filter = ref({ search: "", tsWiedervorlage: "", toggleUnerledigt: true });

	function resetFilters() {
		filter.value = { search: "", tsWiedervorlage: "", toggleUnerledigt: false };
	}
	//# endregion

	//# region ----------------------- Auswahl & Actions ------------------------
	type modalType = {
		visible: boolean,
		status: null | "create" | "edit",
		id: null | number,
		data: WiedervorlageEintrag
	};

	const modal = ref<modalType>({
		visible: false,
		status: null,
		id: null,
		data: new WiedervorlageEintrag(),
	});

	/* currently only implemented as readonly checkboxes and action buttons	 */
	const selection = ref<WiedervorlageEintrag[]>([]);

	const bulkChecked = computed(() => selection.value.length > 0);
	const bulkActions = computed(() => {
		return [
			{
				label: "Allgemeine Wiedervorlage anlegen",
				action: () => modal.value = { visible: true, status: "create", id: null, data: new WiedervorlageEintrag() },
				iconClasses: "i-ri-add-line",
			},
			{
				label: "Ausgewählte Wiedervorlagen löschen",
				action: () => {},
				iconClasses: "i-ri-delete-bin-line icon-ui-danger",
				disabled: true },
		];
	});

	function rowActions(row: WiedervorlageEintrag): TableActions<WiedervorlageEintrag>[] {
		const isErledigt = row.tsErledigt !== null;

		return [
			{
				label: `Wiedervorlage als ${isErledigt ? 'unerledigt' : 'erledigt'} markieren`,
				action: () => setWiedervorlageErledigung(row),
				iconClasses: isErledigt ? "i-ri-close-line" : "i-ri-check-line",
			},
			{
				label: "Wiedervorlage bearbeiten",
				action: () => modal.value = { visible: true, status: "edit", id: row.id, data: row },
				iconClasses: "i-ri-edit-2-line",
				disabled: row.tsErledigt !== null,
			},
			{ label: "Wiedervorlage löschen", action: () => { }, iconClasses: "i-ri-delete-bin-line icon-ui-danger", disabled: true },
		];
	}

	async function setWiedervorlageErledigung(row: WiedervorlageEintrag) {
		const erledigungStatus = await wiedervorlageState.toggleWiedervorlageErledigung(row);
		const erledigtText = erledigungStatus === true ? "erledigt" : "unerledigt";

		const text = row.namePerson !== null ?
			`Wiedervorlage für "${row.namePerson}" als ${erledigtText} markiert: "${row.bemerkung}"`
			: `Wiedervorlage als erledigt markiert: "${row.bemerkung}"`;
		notificationState.success("Gespeichert", text);
	}
	//# endregion
</script>
