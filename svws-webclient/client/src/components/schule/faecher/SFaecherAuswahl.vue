<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<span>Fächer</span>
		</template>
		<template #abschnitt>
			<abschnitt-auswahl :daten="schuljahresabschnittsauswahl" />
		</template>
		<template #header />
		<template #content>
			<svws-ui-table :clickable="!fachListeManager().liste.auswahlExists()" :clicked="clickedEintrag" @update:clicked="fachdaten => gotoDefaultView(fachdaten.id)" :items="fachListeManager().filtered()"
				:model-value="[...props.fachListeManager().liste.auswahl()]" @update:model-value="items => setAuswahl(items)" :columns :filter-open="true" selectable count scroll-into-view scroll allow-arrow-key-selection enable-focus-switching>
				<template #filterAdvanced>
					<svws-ui-checkbox type="toggle" v-model="filterNurSichtbare">Nur Sichtbare</svws-ui-checkbox>
				</template>
				<template #actions>
					<template v-if="fachListeManager().schulform().daten(schuljahr)?.hatGymOb ?? false">
						<s-faecher-auswahl-sortierung-sek-i-i-modal v-slot="{ openModal }" :setze-default-sortierung-sek-i-i :set-filter>
							<svws-ui-button type="secondary" @click="openModal">Standardsortierung Sek II anwenden …</svws-ui-button>
						</s-faecher-auswahl-sortierung-sek-i-i-modal>
					</template>
					<svws-ui-tooltip position="bottom" v-if="hatKompetenzAendern">
						<svws-ui-button :disabled="activeViewType === ViewType.HINZUFUEGEN" type="icon" @click="gotoHinzufuegenView(true)" :has-focus="fachListeManager().filtered().size() === 0">
							<span class="icon i-ri-add-line" />
						</svws-ui-button>
						<template #content>
							Neues Fach anlegen
						</template>
					</svws-ui-tooltip>
				</template>
			</svws-ui-table>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { FaecherAuswahlProps } from "./SFaecherAuswahlProps";
	import { ViewType } from "@ui";
	import { ServerMode, BenutzerKompetenz, type FachDaten } from "@core";

	const props = defineProps<FaecherAuswahlProps>();

	const hatKompetenzAendern = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));

	const schuljahr = computed(() => props.schuljahresabschnittsauswahl().aktuell.schuljahr)

	const columns = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: 'asc' },
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, span: 3 }
	];

	const filterNurSichtbare = computed<boolean>({
		get: () => props.fachListeManager().filterNurSichtbar(),
		set: (value) => {
			props.fachListeManager().setFilterNurSichtbar(value);
			void props.setFilter();
		}
	});

	const clickedEintrag = computed(() => {
		if ((props.activeViewType === ViewType.GRUPPENPROZESSE) || (props.activeViewType === ViewType.HINZUFUEGEN))
			return null;
		return props.fachListeManager().hasDaten() ? props.fachListeManager().auswahl() : null;
	});

	async function setAuswahl(items : FachDaten[]) {
		props.fachListeManager().liste.auswahlClear();
		for (const item of items)
			if (props.fachListeManager().liste.hasValue(item))
				props.fachListeManager().liste.auswahlAdd(item);
		if (props.fachListeManager().liste.auswahlExists())
			await props.gotoGruppenprozessView(true);
		else
			await props.gotoDefaultView(props.fachListeManager().getVorherigeAuswahl()?.id);
	}

</script>
