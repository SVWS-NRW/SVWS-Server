<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1>Vermerkarten</h1>
		</div>
		<div class="secondary-menu--header" />
		<div class="secondary-menu--content">
			<svws-ui-table v-model="vermerkarten"
				v-model:clicked="selectedVermerkarten"
				:items="props.manager().filtered()" :columns
				clickable :selectable="!readonly" count :focus-switching-enabled :focus-help-visible scroll scroll-into-view filter-open>
				<template #search>
					<svws-ui-text-input v-model="searchTerm" type="search" placeholder="Suchen" />
				</template>
				<template #filterAdvanced>
					<svws-ui-checkbox type="toggle" v-model="showOnlyVisibleVermerkarten">Nur Sichtbare</svws-ui-checkbox>
				</template>
				<template #actions v-if="!readonly">
					<svws-ui-tooltip position="bottom">
						<svws-ui-button type="icon"
							@click="openModalVermerkarten"
							:has-focus="noFilteredEntries" :disabled="isHinzufuegenView">
							<span class="icon i-ri-add-line" />
						</svws-ui-button>
						<template #content>
							Neue Vermerkart anlegen
						</template>
					</svws-ui-tooltip>
				</template>
			</svws-ui-table>
		</div>
		<!--
			Das Modal ist hier direkt implementiert, weil es einzigartig ist und aktuell keine weitere Verwendung findet.
		 	Hinweis auf Verordnung zur Schülerdatenverwaltung muss jedes Mal vor der Neuanlage einer Vermerktart als gelesen bestätigt werden
		-->
		<svws-ui-modal v-model:show="showModalVermerkarten" size="medium">
			<template #modalTitle>
				Neue Vermerkart anlegen
			</template>
			<template #modalContent>
				<VermerkartenNotify class="text-center m-2 text-lg" />
				<div class="mt-7 flex flex-row gap-4 justify-end">
					<svws-ui-button type="secondary" @click="closeModalVermerkarten">Abbrechen</svws-ui-button>
					<svws-ui-button @click="acceptModalVermerkarten">
						Verstanden
					</svws-ui-button>
				</div>
			</template>
		</svws-ui-modal>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";
	import type { VermerkartEintrag } from "@core";
	import { BenutzerKompetenz } from "@core";
	import type { DataTableColumn } from "@ui";
	import { useBenutzerState, useRegionSwitch, ViewType } from "@ui";
	import type { VermerkartenAuswahlProps } from "./VermerkartenAuswahlProps";

	const props = defineProps<VermerkartenAuswahlProps>();
	const benutzerState = useBenutzerState();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();
	const readonly = computed<boolean>(() => !benutzerState.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const isHinzufuegenView = computed<boolean>(() => props.activeViewType === ViewType.HINZUFUEGEN);
	const isGruppenprozesseOrHinzufuegenView = computed<boolean>(() => (props.activeViewType === ViewType.GRUPPENPROZESSE) || isHinzufuegenView.value);
	const noFilteredEntries = computed<boolean>(() => props.manager().filtered().size() === 0);
	const showModalVermerkarten = ref<boolean>(false);
	const searchTerm = computed<string>({
		get: () => props.manager().searchTerm,
		set: (v: string) => {
			props.manager().searchTerm = v;
			void props.setFilter();
		},
	});

	const vermerkarten = computed<VermerkartEintrag[]>({
		get: () => [...props.manager().liste.auswahl()],
		set: (v: VermerkartEintrag[]) => {
			setAuswahl(v);
			void navigateToView();
		},
	});

	const showOnlyVisibleVermerkarten = computed<boolean>({
		get: () => props.manager().filterNurSichtbar(),
		set: (value: boolean) => {
			props.manager().setFilterNurSichtbar(value);
			void props.setFilter();
		},
	});

	const selectedVermerkarten = computed<VermerkartEintrag | null>({
		get: () => (!isGruppenprozesseOrHinzufuegenView.value && props.manager().hasDaten()) ? props.manager().auswahl() : null,
		set: (v: VermerkartEintrag | null) => void props.gotoDefaultView(v?.id ?? null),
	});

	const columns: DataTableColumn[] = [
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, defaultSort: "asc", span: 2 },
	];

	function openModalVermerkarten() {
		showModalVermerkarten.value = true;
	}

	function closeModalVermerkarten() {
		showModalVermerkarten.value = false;
	}

	async function acceptModalVermerkarten() {
		showModalVermerkarten.value = false;
		await props.gotoHinzufuegenView(true);
	}

	function setAuswahl(vermerkarten: VermerkartEintrag[]): void {
		props.manager().liste.auswahlClear();
		for (const data of vermerkarten) {
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
