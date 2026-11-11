<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1>Lehrkräfte</h1>
			<div>
				<abschnitt-auswahl />
			</div>
		</div>
		<div class="secondary-menu--header" />
		<div class="secondary-menu--content">
			<svws-ui-table :lock-selectable="pendingStateManagerRegistry().pendingStateExists()" :clickable="!lehrerAuswahlState.liste.auswahlExists()"
				:clicked="clickedEintrag" @update:clicked="lehrerDaten => lehrerAuswahlState.navigator.select(lehrerDaten.id)" :items="lehrerAuswahlState.filter.visible"
				:model-value="[...lehrerAuswahlState.liste.auswahl()]" @update:model-value="items => setAuswahl(items)" :columns selectable count
				:filter-open="true" :filtered="lehrerAuswahlState.filter.isActive" :filterReset="lehrerAuswahlState.filter.setDefault" scroll-into-view scroll v-model:sort-by-and-order="sortByAndOrder"
				:sort-by-multi allow-arrow-key-selection :focus-switching-enabled :focus-help-visible>
				<template #search>
					<svws-ui-text-input v-model="lehrerAuswahlState.filter.search" type="search" placeholder="Suchen" />
				</template>
				<template #filterAdvanced>
					<svws-ui-multi-select v-model="lehrerAuswahlState.filter.personaltypenModel" title="Personaltyp" :items="lehrerAuswahlState.filter.options.personaltypen" :item-text="textPersonaltyp"
						class="col-span-full" />
					<div class="col-span-full flex flex-wrap gap-x-5">
						<svws-ui-checkbox type="toggle" v-model="lehrerAuswahlState.filter.nurSichtbarModel">Nur Sichtbare</svws-ui-checkbox>
						<svws-ui-checkbox type="toggle" v-model="lehrerAuswahlState.filter.nurStatistikrelevantModel">Nur Statistik-Relevante</svws-ui-checkbox>
					</div>
				</template>
				<template #actions>
					<svws-ui-tooltip v-if="serverState.hasDev && hatKompetenzAendern" position="bottom">
						<svws-ui-button :disabled="lehrerAuswahlState.navigator.activeViewType === ViewType.HINZUFUEGEN" type="icon" @click="lehrerAuswahlState.navigator.gotoHinzufuegen(true)"
							:has-focus="lehrerAuswahlState.filter.visible.isEmpty()">
							<span class="icon i-ri-add-line" />
						</svws-ui-button>
						<template #content>
							Neuen Lehrer anlegen
						</template>
					</svws-ui-tooltip>
				</template>
			</svws-ui-table>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";

	import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import type { PersonalTyp } from "@core/core/types/PersonalTyp";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { useServerState } from "@ui/states/ServerState";
	import type { SortByAndOrder } from "@ui/types";
	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";
	import { ViewType } from "@ui/ui/nav/ViewType";

	import { useLehrerAuswahlState } from "~/states/lehrer/LehrerAuswahlState";

	import type { LehrerAuswahlProps } from "./LehrerAuswahlProps";

	const props = defineProps<LehrerAuswahlProps>();
	const benutzerState = useBenutzerState();
	const serverState = useServerState();
	const lehrerAuswahlState = useLehrerAuswahlState();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const hatKompetenzAendern = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.LEHRERDATEN_AENDERN));

	const columns = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: "asc" },
		{ key: "nachname", label: "Nachname", sortable: true, span: 2 },
		{ key: "vorname", label: "Rufname", sortable: true, span: 2 },
	];

	function textPersonaltyp(personaltyp: PersonalTyp): string {
		return personaltyp.bezeichnung;
	}

	const sortByMulti = computed<Map<string, boolean>>(() => {
		const map = new Map<string, boolean>();
		for (const { field, ascending } of lehrerAuswahlState.filter.orderGet()) {
			map.set(field, ascending);
		}
		return map;
	});

	const sortByAndOrder = computed<SortByAndOrder | undefined>({
		get: () => {
			const list = lehrerAuswahlState.filter.orderGet();
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
			lehrerAuswahlState.filter.orderUpdate(value.key, value.order);
		},
	});

	const clickedEintrag = computed(() => {
		if ((lehrerAuswahlState.navigator.activeViewType === ViewType.GRUPPENPROZESSE) || (lehrerAuswahlState.navigator.activeViewType === ViewType.HINZUFUEGEN)) {
			return null;
		}
		return lehrerAuswahlState.stammdaten.hasDaten ? lehrerAuswahlState.selected : null;
	});

	async function setAuswahl(items: LehrerListeEintrag[]) {
		await lehrerAuswahlState.setMultiSelection(items);
		if (lehrerAuswahlState.liste.auswahlExists()) {
			await lehrerAuswahlState.navigator.gotoGruppenprozesse(true);
		} else {
			await lehrerAuswahlState.navigator.select(lehrerAuswahlState.stammdaten.vorherige?.id);
		}
	}

</script>
