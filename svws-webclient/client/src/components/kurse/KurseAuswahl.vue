<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1>Kurse</h1>
			<div><abschnitt-auswahl /></div>
		</div>
		<div class="secondary-menu--header" />
		<div class="secondary-menu--content">
			<svws-ui-table :clickable="!kurseAuswahlState.liste.auswahlExists()" :clicked="clickedEintrag" @update:clicked="kursDaten => kurseAuswahlState.navigator.select(kursDaten.id)"
				:items="kurseAuswahlState.filter.visible" :model-value="[...kurseAuswahlState.liste.auswahl()]" @update:model-value="items => setAuswahl(items)"
				:columns selectable count :filter-open="true" :filtered="kurseAuswahlState.filter.isActive" :filterReset="kurseAuswahlState.filter.setDefault" scroll-into-view scroll
				v-model:sort-by-and-order="sortByAndOrder" :sort-by-multi allow-arrow-key-selection :focus-switching-enabled :focus-help-visible>
				<template #search>
					<svws-ui-text-input v-model="kurseAuswahlState.filter.search" type="search" placeholder="Suche nach Kurs" />
				</template>
				<template #filterAdvanced>
					<svws-ui-checkbox type="toggle" v-model="kurseAuswahlState.filter.nurSichtbarModel">Nur Sichtbare</svws-ui-checkbox>
					<svws-ui-multi-select v-model="kurseAuswahlState.filter.schuelerModel" title="Schüler" :items="kurseAuswahlState.filter.options.schueler" :item-text="textSchueler" :item-filter="findSchueler" autocomplete />
					<svws-ui-multi-select v-model="kurseAuswahlState.filter.faecherModel" title="Fach" :items="kurseAuswahlState.filter.options.faecher" :item-text="text" :item-filter="find" autocomplete />
					<svws-ui-multi-select v-model="kurseAuswahlState.filter.lehrerModel" title="Fachlehrer" :items="kurseAuswahlState.filter.options.lehrer" :item-text="text" :item-filter="find" autocomplete />
					<svws-ui-multi-select v-model="kurseAuswahlState.filter.jahrgaengeModel" title="Jahrgang" :items="kurseAuswahlState.filter.options.jahrgaenge" :item-text="text" :item-filter="find" autocomplete />
					<svws-ui-multi-select v-model="kurseAuswahlState.filter.schulgliederungenModel" title="Schulgliederung" :items="kurseAuswahlState.filter.options.schulgliederungen" :item-text="text_schulgliederung" autocomplete />
				</template>
				<template #cell(idLehrer)="{ value }"> {{ getLehrerKuerzel(value) }} </template>
				<template #cell(idJahrgaenge)="{ value }"> {{ getJahrgangsKuerzel(value) }} </template>
				<template #cell(schueler)="{ value }">{{ value.size() }}</template>
				<template #actions v-if="serverState.hasDev && hatKompetenzAendern">
					<svws-ui-tooltip position="bottom">
						<svws-ui-button :disabled="kurseAuswahlState.navigator.activeViewType === ViewType.HINZUFUEGEN" type="icon"
							@click="kurseAuswahlState.navigator.gotoHinzufuegen(true)" :has-focus="kurseAuswahlState.filter.visible.isEmpty()">
							<span class="icon i-ri-add-line" />
						</svws-ui-button>
						<template #content>
							Neuen Kurs anlegen
						</template>
					</svws-ui-tooltip>
				</template>
			</svws-ui-table>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";

	import type { KursDaten } from "@core/asd/data/kurse/KursDaten";
	import type { Schulgliederung } from "@core/asd/types/schule/Schulgliederung";
	import type { FachDaten } from "@core/core/data/fach/FachDaten";
	import { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
	import { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
	import type { SchuelerListeEintrag } from "@core/core/data/schueler/SchuelerListeEintrag";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import type { List } from "@core/java/util/List";
	import { useAbschnittState } from "@ui/states/AbschnittState";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { useServerState } from "@ui/states/ServerState";
	import type { DataTableColumn, SortByAndOrder } from "@ui/types";
	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";
	import { ViewType } from "@ui/ui/nav/ViewType";

	import { useKurseAuswahlState } from "~/states/kurse/KurseAuswahlState";

	const benutzerState = useBenutzerState();
	const serverState = useServerState();
	const abschnittState = useAbschnittState();
	const kurseAuswahlState = useKurseAuswahlState();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();
	const hatKompetenzAendern = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ALLGEMEIN_AENDERN));

	const columns: DataTableColumn[] = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: "asc" },
		{ key: "idLehrer", label: "Fachlehrer", sortable: true },
		{ key: "idJahrgaenge", label: "JG", tooltip: "Jahrgang", sortable: true, span: 0.5 },
		{ key: "schueler", label: "Schüler", span: 0.5, align: "right" },
	];

	const sortByMulti = computed<Map<string, boolean>>(() => {
		const map = new Map<string, boolean>();
		for (const { field, ascending } of kurseAuswahlState.filter.orderGet()) {
			map.set(field === "kuerzel" ? "kurse" : field, ascending);
		}
		return map;
	});

	const sortByAndOrder = computed<SortByAndOrder | undefined>({
		get: () => {
			const list = kurseAuswahlState.filter.orderGet();
			if (list.length === 0) {
				return undefined;
			} else {
				const { field: key, ascending: order } = list[0];
				return { key, order };
			}
		},
		set: (value) => {
			if ((value === undefined) || (value.key === null)) {
				return;
			}
			kurseAuswahlState.filter.orderUpdate(value.key, value.order);
		},
	});

	function text(eintrag: LehrerListeEintrag | JahrgangsDaten | FachDaten): string {
		if (eintrag instanceof LehrerListeEintrag) {
			return `${eintrag.kuerzel} (${eintrag.nachname}, ${eintrag.vorname})`;
		} else if (eintrag instanceof JahrgangsDaten) {
			return eintrag.kuerzel ?? "";
		} else {
			return `${eintrag.kuerzel} (${eintrag.bezeichnung})`;
		}
	}

	function find(items: Iterable<LehrerListeEintrag | JahrgangsDaten | FachDaten>, search: string) {
		const list = [];
		for (const i of items) {
			if ((i.kuerzel !== null) && i.kuerzel.toLocaleLowerCase().includes(search.toLocaleLowerCase())) {
				list.push(i);
			}
		}
		return list;
	}

	function textSchueler(eintrag: SchuelerListeEintrag): string {
		return eintrag.nachname + ", " + eintrag.vorname;
	}

	const findSchueler = (items: Iterable<SchuelerListeEintrag>, search: string) => {
		const list = [];
		for (const i of items) {
			if ((i.nachname.toLocaleLowerCase().includes(search.toLocaleLowerCase())) || (i.vorname.toLocaleLowerCase().includes(search.toLocaleLowerCase()))) {
				list.push(i);
			}
		}
		return list;
	};

	function text_schulgliederung(schulgliederung: Schulgliederung): string {
		return schulgliederung.daten(abschnittState.auswahl.schuljahr)?.kuerzel ?? '—';
	}

	const clickedEintrag = computed(() => {
		if ((kurseAuswahlState.navigator.activeViewType === ViewType.GRUPPENPROZESSE) || (kurseAuswahlState.navigator.activeViewType === ViewType.HINZUFUEGEN)) {
			return null;
		}
		return kurseAuswahlState.stammdaten.hasDaten ? kurseAuswahlState.selected : null;
	});

	async function setAuswahl(items: KursDaten[]) {
		await kurseAuswahlState.setMultiSelection(items);
		if (kurseAuswahlState.liste.auswahlExists()) {
			await kurseAuswahlState.navigator.gotoGruppenprozesse(true);
		} else {
			await kurseAuswahlState.navigator.select(kurseAuswahlState.stammdaten.vorherige?.id);
		}
	}

	// TODO komma-separierte Liste mit Zusatzkräften
	function getLehrerKuerzel(idLehrer: number) {
		const lehrer = kurseAuswahlState.lehrer.get(idLehrer);
		if (lehrer === null) {
			return "---";
		}
		return lehrer.kuerzel;
	}

	/**
	 * Ermittel eine komma-separierte Liste der Kürzel der Jahrgänge mit den übergebenen IDs.
	 *
	 * @param jahrgaengeIds   die Liste von Jahrgangs-IDs
	 */
	function getJahrgangsKuerzel(jahrgaengeIds: List<number>): string {
		return [...jahrgaengeIds].map(jgId => kurseAuswahlState.jahrgaenge.get(jgId)?.kuerzel)
			.filter(jgKuerzel => (jgKuerzel !== undefined) && (jgKuerzel !== ''))
			.join(',');
	}

</script>
