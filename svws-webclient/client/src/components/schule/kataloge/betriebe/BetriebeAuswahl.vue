<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1>Betriebe</h1>
		</div>
		<div class="secondary-menu--header" />
		<div class="secondary-menu--content">
			<svws-ui-table v-model="betriebe"
				v-model:clicked="selectedBetriebe"
				:items="props.manager().filtered()" :columns
				clickable :selectable="!readonly" count :focus-switching-enabled :focus-help-visible scroll scroll-into-view filter-open>
				<template #search>
					<svws-ui-text-input v-model="searchTerm" type="search" placeholder="Suchen" />
				</template>
				<template #filterAdvanced>
					<svws-ui-checkbox type="toggle" v-model="showOnlyVisibleBetriebe">Nur Sichtbare</svws-ui-checkbox>
				</template>
				<template #actions v-if="!readonly">
					<svws-ui-tooltip position="bottom">
						<svws-ui-button type="icon"
							@click="gotoHinzufuegenView(true)"
							:has-focus="noFilteredEntries" :disabled="isHinzufuegenView">
							<span class="icon i-ri-add-line" />
						</svws-ui-button>
						<template #content>
							Neuen Betrieb anlegen
						</template>
					</svws-ui-tooltip>
				</template>
			</svws-ui-table>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { Betrieb } from "@core/core/data/schule/Betrieb";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";
	import { ViewType } from "@ui/ui/nav/ViewType";
	import { computed } from "vue";
	import type { BetriebeAuswahlProps } from "~/components/schule/kataloge/betriebe/BetriebeAuswahlProps";

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();
	const props = defineProps<BetriebeAuswahlProps>();
	const benutzerState = useBenutzerState();

	const readonly = computed<boolean>(() => !benutzerState.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const isHinzufuegenView = computed<boolean>(() => props.activeViewType === ViewType.HINZUFUEGEN);
	const isGruppenprozesseOrHinzufuegenView = computed<boolean>(() => (props.activeViewType === ViewType.GRUPPENPROZESSE) || isHinzufuegenView.value);
	const noFilteredEntries = computed<boolean>(() => props.manager().filtered().size() === 0);
	const searchTerm = computed<string>({
		get: () => props.manager().searchTerm,
		set: (v: string) => {
			props.manager().searchTerm = v;
			void props.setFilter();
		},
	});

	const betriebe = computed<Betrieb[]>({
		get: () => [...props.manager().liste.auswahl()],
		set: (v: Betrieb[]) => {
			setAuswahl(v);
			void navigateToView();
		},
	});

	const showOnlyVisibleBetriebe = computed<boolean>({
		get: () => props.manager().filterNurSichtbar,
		set: (value: boolean) => {
			props.manager().filterNurSichtbar = value;
			void props.setFilter();
		},
	});

	const selectedBetriebe = computed<Betrieb | null>({
		get: () => (!isGruppenprozesseOrHinzufuegenView.value && props.manager().hasDaten()) ? props.manager().auswahl() : null,
		set: (v: Betrieb | null) => void props.gotoDefaultView(v?.id ?? null),
	});

	const columns = [
		{ key: "name", label: "Name", sortable: true, defaultSort: "asc" },
	];

	function setAuswahl(betriebe: Betrieb[]): void {
		props.manager().liste.auswahlClear();
		for (const data of betriebe) {
			if (props.manager().liste.hasValue(data)) {
				props.manager().liste.auswahlAdd(data);
			}
		}
	}

	async function navigateToView(): Promise<void> {
		if (props.manager().liste.auswahlExists()) {
			await props.gotoGruppenprozessView(true);
		} else {
			await props.gotoDefaultView(props.manager().getVorherigeAuswahl()?.id);
		}
	}

</script>
