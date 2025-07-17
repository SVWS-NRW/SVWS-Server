<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1>Fächer</h1>
			<div>
				<abschnitt-auswahl :daten="schuljahresabschnittsauswahl" />
			</div>
		</div>
		<div class="secondary-menu--header" />
		<div class="secondary-menu--content">
			<svws-ui-table :clickable="!manager().liste.auswahlExists()" :clicked="clickedEintrag" @update:clicked="fachdaten => gotoDefaultView(fachdaten.id)" :items="manager().filtered()"
				:model-value="[...props.manager().liste.auswahl()]" @update:model-value="items => setAuswahl(items)" :columns :filter-open="true" selectable count scroll-into-view scroll allow-arrow-key-selection
				:focus-switching-enabled :focus-help-visible>
				<template #filterAdvanced>
					<svws-ui-checkbox type="toggle" v-model="filterNurSichtbare">Nur Sichtbare</svws-ui-checkbox>
				</template>
				<template #actions>
					<template v-if="(manager().liste.size() > 0) && (manager().schulform().daten(schuljahr)?.hatGymOb ?? false)">
						<s-faecher-auswahl-sortierung-sek-i-i-modal v-slot="{ openModal }" :setze-default-sortierung-sek-i-i :set-filter>
							<svws-ui-button type="secondary" @click="openModal">Standardsortierung Sek II anwenden …</svws-ui-button>
						</s-faecher-auswahl-sortierung-sek-i-i-modal>
					</template>
					<svws-ui-tooltip position="bottom" v-if="hatKompetenzAendern">
						<svws-ui-button :disabled="activeViewType === ViewType.HINZUFUEGEN" type="icon" @click="gotoHinzufuegenView(true)" :has-focus="manager().filtered().size() === 0">
							<span class="icon i-ri-add-line" />
						</svws-ui-button>
						<template #content>
							Neues Fach anlegen
						</template>
					</svws-ui-tooltip>
				</template>
			</svws-ui-table>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { FaecherAuswahlProps } from "./SFaecherAuswahlProps";
	import { useRegionSwitch, ViewType } from "@ui";
	import type { FaecherListeEintrag} from "@core";
	import { BenutzerKompetenz } from "@core";

	const props = defineProps<FaecherAuswahlProps>();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const hatKompetenzAendern = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));

	const schuljahr = computed(() => props.schuljahresabschnittsauswahl().aktuell.schuljahr)

	const columns = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: 'asc' },
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, span: 3 },
	];

	const filterNurSichtbare = computed<boolean>({
		get: () => props.manager().filterNurSichtbar(),
		set: (value) => {
			props.manager().setFilterNurSichtbar(value);
			void props.setFilter();
		},
	});

	const clickedEintrag = computed(() => {
		if ((props.activeViewType === ViewType.GRUPPENPROZESSE) || (props.activeViewType === ViewType.HINZUFUEGEN))
			return null;
		return props.manager().hasDaten() ? props.manager().auswahl() : null;
	});

	async function setAuswahl(items : FaecherListeEintrag[]) {
		props.manager().liste.auswahlClear();
		for (const item of items)
			if (props.manager().liste.hasValue(item))
				props.manager().liste.auswahlAdd(item);
		if (props.manager().liste.auswahlExists())
			await props.gotoGruppenprozessView(true);
		else
			await props.gotoDefaultView(props.manager().getVorherigeAuswahl()?.id);
	}

</script>
