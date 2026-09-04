<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1>Lernplattformen</h1>
		</div>
		<div class="secondary-menu--header" />
		<div class="secondary-menu--content">
			<svws-ui-table v-model="lernplattformen"
				v-model:clicked="selectedLernplattformen"
				:items="rowsFiltered" :columns
				clickable :selectable="!readonly" count :focus-switching-enabled :focus-help-visible scroll scroll-into-view filter-open>
				<template #search>
					<svws-ui-text-input v-model="searchTerm" type="search" placeholder="Suchen" />
				</template>
				<template #actions v-if="!readonly">
					<svws-ui-tooltip position="bottom">
						<svws-ui-button type="icon"
							@click="gotoHinzufuegenView(true)"
							:has-focus="noFilteredEntries" :disabled="isHinzufuegenView">
							<span class="icon i-ri-add-line" />
						</svws-ui-button>
						<template #content>
							Neue Lernplattform anlegen
						</template>
					</svws-ui-tooltip>
				</template>
			</svws-ui-table>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { Lernplattform } from "@core/core/data/schule/Lernplattform";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import type { DataTableColumn } from "@ui/types";
	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";
	import { ViewType } from "@ui/ui/nav/ViewType";
	import { computed, ref } from "vue";
	import type { LernplattformenAuswahlProps } from "~/components/schule/kataloge/lernplattformen/LernplattformenAuswahlProps";

	const props = defineProps<LernplattformenAuswahlProps>();
	const benutzerState = useBenutzerState();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();
	const readonly = computed<boolean>(() => !benutzerState.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const isHinzufuegenView = computed<boolean>(() => props.activeViewType === ViewType.HINZUFUEGEN);
	const isGruppenprozesseOrHinzufuegenView = computed<boolean>(() => (props.activeViewType === ViewType.GRUPPENPROZESSE) || isHinzufuegenView.value);
	const noFilteredEntries = computed<boolean>(() => props.manager().filtered().size() === 0);
	const searchTerm = ref<string>("");

	const rowsFiltered = computed<Lernplattform[]>(() => {
		const term = searchTerm.value.trim();
		if (term === '') {
			return [...props.manager().filtered()];
		}

		const termLower = searchTerm.value.toLocaleLowerCase();

		const arr = [];
		for (const e of props.manager().filtered()) {
			if (e.bezeichnung.toLocaleLowerCase().includes(termLower)) {
				arr.push(e);
			}
		}
		return arr;
	});

	const lernplattformen = computed<Lernplattform[]>({
		get: () => [...props.manager().liste.auswahl()],
		set: (v: Lernplattform[]) => {
			setAuswahl(v);
			void navigateToView();
		},
	});

	const selectedLernplattformen = computed<Lernplattform | null>({
		get: () => (!isGruppenprozesseOrHinzufuegenView.value && props.manager().hasDaten()) ? props.manager().auswahl() : null,
		set: (v: Lernplattform | null) => void props.gotoDefaultView(v?.id ?? null),
	});

	const columns: DataTableColumn[] = [
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, defaultSort: "asc" },
	];

	function setAuswahl(lernplattformen: Lernplattform[]) {
		props.manager().liste.auswahlClear();
		for (const lernplattform of lernplattformen) {
			if (props.manager().liste.hasValue(lernplattform)) {
				props.manager().liste.auswahlAdd(lernplattform);
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
