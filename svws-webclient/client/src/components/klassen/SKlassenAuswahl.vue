<template>
	<svws-ui-secondary-menu>
		<template #headline>Klassen</template>
		<template #abschnitt>
			<abschnitt-auswahl :daten="schuljahresabschnittsauswahl" />
		</template>
		<template #content>
			<svws-ui-table :clickable="!manager().liste.auswahlExists()" :clicked="clickedEintrag" @update:clicked="klassendaten => gotoDefaultView(klassendaten.id)"
				:items="rowsFiltered" :model-value="[...props.manager().liste.auswahl()]" @update:model-value="items => setAuswahl(items)"
				:columns selectable count :filter-open="true" :filtered="filterChanged()" :filterReset scroll-into-view scroll allow-arrow-key-selection :focus-switching-enabled :focus-help-visible>
				<template #search>
					<svws-ui-text-input v-model="search" type="search" placeholder="Suchen" removable />
				</template>
				<template #filterAdvanced>
					<svws-ui-multi-select v-model="filterJahrgaenge" title="Jahrgang" :items="manager().jahrgaenge.list()" :item-text="text" :item-filter="find" />
					<svws-ui-multi-select v-model="filterLehrer" title="Klassenleitung" :items="manager().lehrer.list()" :item-text="text" :item-filter="find" />
					<svws-ui-multi-select v-model="filterSchulgliederung" title="Schulgliederung" :items="manager().schulgliederungen.list()" :item-text="textSchulgliederung" />
				</template>
				<template #cell(schueler)="{value}"> {{ value.size() }} </template>
				<template #cell(klassenLeitungen)="{value}">
					{{ lehrerkuerzel(value) }}
				</template>
				<template #actions>
					<div class="flex gap-5">
						<s-klassen-auswahl-sortierung-modal v-slot="{ openModal }" :setze-default-sortierung>
							<svws-ui-tooltip position="bottom">
								<svws-ui-button type="secondary" @click="openModal">
									Standardsortierung anwenden&nbsp;...
								</svws-ui-button>
								<template #content>
									Standardsortierung wiederherstellen
								</template>
							</svws-ui-tooltip>
						</s-klassen-auswahl-sortierung-modal>
						<svws-ui-tooltip v-if="hatKompetenzAendern" position="bottom">
							<svws-ui-button :disabled="activeViewType === ViewType.HINZUFUEGEN" type="icon" @click="gotoHinzufuegenView(true)" :has-focus="rowsFiltered.length === 0">
								<span class="icon i-ri-add-line" />
							</svws-ui-button>
							<template #content>
								Neue Klasse anlegen
							</template>
						</svws-ui-tooltip>
					</div>
				</template>
			</svws-ui-table>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";
	import type { KlassenAuswahlProps } from "./SKlassenAuswahlProps";
	import type{ JahrgangsDaten, KlassenDaten, LehrerListeEintrag, Schulgliederung } from "@core";
	import { BenutzerKompetenz } from "@core";
	import { ViewType } from "@ui";
	import {useRegionSwitch} from "~/components/useRegionSwitch";

	const props = defineProps<KlassenAuswahlProps>();
	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const schuljahr = computed<number>(() => props.schuljahresabschnittsauswahl().aktuell.schuljahr);

	const hatKompetenzAendern = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ALLGEMEIN_AENDERN));

	const columns = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: "asc", span: 0.5 },
		{ key: "klassenLeitungen", label: "Klassenleitung" },
		{ key: "schueler", label: "Schüler", span: 0.5, sortable: true },
	];

	function text(eintrag: LehrerListeEintrag | JahrgangsDaten): string {
		return eintrag.kuerzel ?? "";
	}

	function find(items: Iterable<LehrerListeEintrag | JahrgangsDaten>, search: string) {
		const list = [];
		for (const i of items)
			if ((i.kuerzel !== null) && i.kuerzel.toLocaleLowerCase().includes(search.toLocaleLowerCase()))
				list.push(i);
		return list;
	}

	function textSchulgliederung(schulgliederung: Schulgliederung): string {
		return schulgliederung.daten(schuljahr.value)?.kuerzel ?? '—';
	}

	const filterSchulgliederung = computed<Schulgliederung[]>({
		get: () => [...props.manager().schulgliederungen.auswahl()],
		set: (value) => {
			props.manager().schulgliederungen.auswahlClear();
			for (const v of value)
				props.manager().schulgliederungen.auswahlAdd(v);
			void props.setFilter();
		},
	});

	const filterJahrgaenge = computed<JahrgangsDaten[]>({
		get: () => [...props.manager().jahrgaenge.auswahl()],
		set: (value) => {
			props.manager().jahrgaenge.auswahlClear();
			for (const v of value)
				props.manager().jahrgaenge.auswahlAdd(v);
			void props.setFilter();
		},
	});

	const filterLehrer = computed<LehrerListeEintrag[]>({
		get: () => [...props.manager().lehrer.auswahl()],
		set: (value) => {
			props.manager().lehrer.auswahlClear();
			for (const v of value)
				props.manager().lehrer.auswahlAdd(v);
			void props.setFilter();
		},
	});

	const search = ref<string>("");

	const rowsFiltered = computed<KlassenDaten[]>(() => {
		const arr = [];
		for (const e of props.manager().filtered())
			if ((e.kuerzel !== null) && e.kuerzel.toLocaleLowerCase().includes(search.value.toLocaleLowerCase()))
				arr.push(e);
		arr.sort((a, b) => a.sortierung - b.sortierung);
		return arr;
	});

	async function filterReset() {
		props.manager().schulgliederungen.auswahlClear();
		props.manager().lehrer.auswahlClear();
		props.manager().jahrgaenge.auswahlClear();
		props.manager().setFilterNurSichtbar(false);
		await props.setFilter();
	}

	function filterChanged(): boolean {
		return (props.manager().schulgliederungen.auswahlExists()
			|| props.manager().lehrer.auswahlExists()
			|| props.manager().jahrgaenge.auswahlExists());
	}

	const clickedEintrag = computed(() => {
		if ((props.activeViewType === ViewType.GRUPPENPROZESSE) || (props.activeViewType === ViewType.HINZUFUEGEN))
			return null;
		return props.manager().hasDaten() ? props.manager().auswahl() : null;
	});

	async function setAuswahl(items : KlassenDaten[]) {
		props.manager().liste.auswahlClear();
		for (const item of items)
			if (props.manager().liste.hasValue(item))
				props.manager().liste.auswahlAdd(item);
		if (props.manager().liste.auswahlExists())
			await props.gotoGruppenprozessView(true);
		else
			await props.gotoDefaultView(props.manager().getVorherigeAuswahl()?.id);
	}

	function lehrerkuerzel(list: number[]) {
		let s = '';
		if (props.manager().hasDaten())
			for (const id of list) {
				const lehrer = props.manager().lehrer.get(id);
				if (lehrer !== null)
					if (s.length > 0)
						s += `, ${lehrer.kuerzel}`;
					else s = lehrer.kuerzel;
			}
		return s;
	}

</script>
