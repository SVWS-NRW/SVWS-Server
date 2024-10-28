<template>
	<svws-ui-secondary-menu>
		<template #headline>Klassen</template>
		<template #abschnitt>
			<abschnitt-auswahl :daten="schuljahresabschnittsauswahl" />
		</template>
		<template #content>
			<svws-ui-table :clickable="!klassenListeManager().liste.auswahlExists()" :clicked="clickedEintrag" @update:clicked="klassendaten => gotoDefaultView(klassendaten.id)"
				:items="rowsFiltered" :model-value="[...props.klassenListeManager().liste.auswahl()]" @update:model-value="items => setAuswahl(items)"
				:columns selectable count :filter-open="true" :filtered="filterChanged()" :filterReset scroll-into-view scroll allow-arrow-key-selection>
				<template #search>
					<svws-ui-text-input v-model="search" type="search" placeholder="Suchen" removable />
				</template>
				<template #filterAdvanced>
					<svws-ui-multi-select v-model="filterJahrgaenge" title="Jahrgang" :items="klassenListeManager().jahrgaenge.list()" :item-text="text" :item-filter="find" />
					<svws-ui-multi-select v-model="filterLehrer" title="Klassenleitung" :items="klassenListeManager().lehrer.list()" :item-text="text" :item-filter="find" />
					<svws-ui-multi-select v-model="filterSchulgliederung" title="Schulgliederung" :items="klassenListeManager().schulgliederungen.list()" :item-text="text_schulgliederung" />
				</template>
				<template #cell(schueler)="{value}"> {{ value.size() }} </template>
				<template #cell(klassenLehrer)="{value}">
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
						<svws-ui-tooltip position="bottom">
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
	import type { JahrgangsDaten, KlassenDaten, LehrerListeEintrag, Schulgliederung } from "@core";
	import type { KlassenAuswahlProps } from "./SKlassenAuswahlProps";
	import { ViewType } from "@ui";

	const props = defineProps<KlassenAuswahlProps>();

	const schuljahr = computed<number>(() => props.schuljahresabschnittsauswahl().aktuell.schuljahr);

	const columns = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: "asc", span: 0.5 },
		{ key: "klassenLehrer", label: "Klassenleitung" },
		{ key: "schueler", label: "Schüler", span: 0.5, sortable: true }
	];

	function text(klasse: LehrerListeEintrag | JahrgangsDaten): string {
		return klasse.kuerzel ?? "";
	}

	const find = (items: Iterable<LehrerListeEintrag | JahrgangsDaten>, search: string) => {
		const list = [];
		for (const i of items)
			if ((i.kuerzel !== null) && i.kuerzel.toLocaleLowerCase().includes(search.toLocaleLowerCase()))
				list.push(i);
		return list;
	}

	function text_schulgliederung(schulgliederung: Schulgliederung): string {
		return schulgliederung.daten(schuljahr.value)?.kuerzel ?? '—';
	}

	const filterSchulgliederung = computed<Schulgliederung[]>({
		get: () => [...props.klassenListeManager().schulgliederungen.auswahl()],
		set: (value) => {
			props.klassenListeManager().schulgliederungen.auswahlClear();
			for (const v of value)
				props.klassenListeManager().schulgliederungen.auswahlAdd(v);
			void props.setFilter();
		}
	});

	const filterJahrgaenge = computed<JahrgangsDaten[]>({
		get: () => [...props.klassenListeManager().jahrgaenge.auswahl()],
		set: (value) => {
			props.klassenListeManager().jahrgaenge.auswahlClear();
			for (const v of value)
				props.klassenListeManager().jahrgaenge.auswahlAdd(v);
			void props.setFilter();
		}
	});

	const filterLehrer = computed<LehrerListeEintrag[]>({
		get: () => [...props.klassenListeManager().lehrer.auswahl()],
		set: (value) => {
			props.klassenListeManager().lehrer.auswahlClear();
			for (const v of value)
				props.klassenListeManager().lehrer.auswahlAdd(v);
			void props.setFilter();
		}
	});

	const search = ref<string>("");

	const rowsFiltered = computed<KlassenDaten[]>(() => {
		const arr = [];
		for (const e of props.klassenListeManager().filtered())
			if ((e.kuerzel !== null) && e.kuerzel.toLocaleLowerCase().includes(search.value.toLocaleLowerCase()))
				arr.push(e);
		arr.sort((a, b) => a.sortierung - b.sortierung);
		return arr;
	});

	async function filterReset() {
		props.klassenListeManager().schulgliederungen.auswahlClear();
		props.klassenListeManager().lehrer.auswahlClear();
		props.klassenListeManager().jahrgaenge.auswahlClear();
		props.klassenListeManager().setFilterNurSichtbar(false);
		await props.setFilter();
	}

	function filterChanged(): boolean {
		return (props.klassenListeManager().schulgliederungen.auswahlExists()
			|| props.klassenListeManager().lehrer.auswahlExists()
			|| props.klassenListeManager().jahrgaenge.auswahlExists());
	}

	const clickedEintrag = computed(() => ((props.activeViewType === ViewType.GRUPPENPROZESSE) || (props.activeViewType === ViewType.HINZUFUEGEN)) ? null
		: (props.klassenListeManager().hasDaten() ? props.klassenListeManager().auswahl() : null));

	async function setAuswahl(items : KlassenDaten[]) {
		props.klassenListeManager().liste.auswahlClear();
		for (const item of items)
			if (props.klassenListeManager().liste.hasValue(item))
				props.klassenListeManager().liste.auswahlAdd(item);
		if (props.klassenListeManager().liste.auswahlExists())
			await props.gotoGruppenprozessView(true);
		else
			await props.gotoDefaultView(props.klassenListeManager().getVorherigeAuswahl()?.id);
	}

	function lehrerkuerzel(list: number[]) {
		let s = '';
		if (props.klassenListeManager().hasDaten())
			for (const id of list) {
				const lehrer = props.klassenListeManager().lehrer.get(id);
				if (lehrer !== null) {
					if (s.length > 0)
						s += `, ${lehrer.kuerzel}`;
					else s = lehrer.kuerzel;
				}
			}
		return s;
	}

</script>
