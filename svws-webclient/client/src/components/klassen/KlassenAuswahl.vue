<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1>Klassen</h1>
			<div><abschnitt-auswahl /></div>
		</div>
		<div class="secondary-menu--header" />
		<div class="secondary-menu--content">
			<svws-ui-table :clickable="!klassenState.liste.auswahlExists()" :clicked="clickedEintrag" @update:clicked="klassendaten => klassenState.navigator.select(klassendaten.id)"
				:items="klassenState.filter.visible" :model-value="[...klassenState.liste.auswahl()]" @update:model-value="items => setAuswahl(items)"
				:columns selectable count :filter-open="true" :filtered="klassenState.filter.isActive" :filterReset="klassenState.filter.setDefault" scroll-into-view scroll allow-arrow-key-selection
				:focus-switching-enabled :focus-help-visible>
				<template #search>
					<svws-ui-text-input v-model="klassenState.filter.search" type="search" placeholder="Suchen" />
				</template>
				<template #filterAdvanced>
					<svws-ui-multi-select v-model="klassenState.filter.jahrgaengeModel" title="Jahrgang" :items="klassenState.filter.options.jahrgaenge" :item-text="text" :item-filter="find" />
					<svws-ui-multi-select v-model="klassenState.filter.lehrerModel" title="Klassenleitung" :items="klassenState.filter.options.lehrer" :item-text="text" :item-filter="find" />
					<svws-ui-multi-select v-model="klassenState.filter.schulgliederungenModel" title="Schulgliederung" :items="klassenState.filter.options.schulgliederungen" :item-text="textSchulgliederung" />
				</template>
				<template #cell(anzahlZugeordneteSchueler)="{value}"> {{ value }} </template>
				<template #cell(idsKlassenleitungen)="{value}">
					{{ lehrerkuerzel(value) }}
				</template>
				<template #actions v-if="hatKompetenzAendern">
					<div class="flex gap-5">
						<template v-if="klassenState.liste.size() > 0">
							<klassen-auswahl-sortierung-modal v-slot="{ openModal }">
								<svws-ui-tooltip position="bottom">
									<svws-ui-button type="secondary" @click="openModal">
										Standardsortierung anwenden&nbsp;...
									</svws-ui-button>
									<template #content>
										Standardsortierung wiederherstellen
									</template>
								</svws-ui-tooltip>
							</klassen-auswahl-sortierung-modal>
						</template>
						<svws-ui-tooltip position="bottom">
							<svws-ui-button :disabled="klassenState.navigator.activeViewType === ViewType.HINZUFUEGEN" type="icon"
								@click="klassenState.navigator.gotoHinzufuegen(true)" :has-focus="klassenState.filter.visible.isEmpty()">
								<span class="icon i-ri-add-line" />
							</svws-ui-button>
							<template #content>
								Neue Klasse anlegen
							</template>
						</svws-ui-tooltip>
					</div>
				</template>
			</svws-ui-table>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";

	import type { KlassenListeEintrag } from "@core/asd/data/klassen/KlassenListeEintrag";
	import type { Schulgliederung } from "@core/asd/types/schule/Schulgliederung";
	import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
	import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { useAbschnittState } from "@ui/states/AbschnittState";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";
	import { ViewType } from "@ui/ui/nav/ViewType";

	import { useKlassenAuswahlState } from "~/states/klassen/KlassenAuswahlState";

	const klassenState = useKlassenAuswahlState();
	const benutzerState = useBenutzerState();
	const abschnittState = useAbschnittState();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const hatKompetenzAendern = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ALLGEMEIN_AENDERN));

	const columns = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: "asc", span: 0.5 },
		{ key: "idsKlassenleitungen", label: "Klassenleitung" },
		{ key: "anzahlZugeordneteSchueler", label: "Schüler", span: 0.5, sortable: true },
	];

	function text(eintrag: LehrerListeEintrag | JahrgangsDaten): string {
		return eintrag.kuerzel ?? "";
	}

	function find(items: Iterable<LehrerListeEintrag | JahrgangsDaten>, search: string) {
		const list = [];
		for (const i of items) {
			if ((i.kuerzel !== null) && i.kuerzel.toLocaleLowerCase().includes(search.toLocaleLowerCase())) {
				list.push(i);
			}
		}
		return list;
	}

	function textSchulgliederung(schulgliederung: Schulgliederung): string {
		return schulgliederung.daten(abschnittState.auswahl.schuljahr)?.kuerzel ?? '—';
	}

	const clickedEintrag = computed(() => {
		if ((klassenState.navigator.activeViewType === ViewType.GRUPPENPROZESSE) || (klassenState.navigator.activeViewType === ViewType.HINZUFUEGEN)) {
			return null;
		}
		return klassenState.stammdaten.hasDaten ? klassenState.selected : null;
	});

	async function setAuswahl(items: KlassenListeEintrag[]) {
		await klassenState.setMultiSelection(items);
		if (klassenState.liste.auswahlExists()) {
			await klassenState.navigator.gotoGruppenprozesse(true);
		} else {
			await klassenState.navigator.select(klassenState.stammdaten.vorherige?.id);
		}
	}

	function lehrerkuerzel(list: number[]) {
		let s = '';
		if (klassenState.stammdaten.hasDaten) {
			for (const id of list) {
				const lehrer = klassenState.lehrer.get(id);
				if (lehrer !== null) {
					if (s.length > 0) {
						s += `, ${lehrer.kuerzel}`;
					} else {
						s = lehrer.kuerzel;
					}
				}
			}
		}
		return s;
	}

</script>
