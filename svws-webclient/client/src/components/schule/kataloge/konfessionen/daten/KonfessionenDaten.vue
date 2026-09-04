<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<ui-select label="Konfession ASD-Kürzel"
						v-model="model.selectedKonfession.value"
						:manager="konfessionKuerzelSelectManager"
						:validation="() => model.getFehler('idReligion')"
						searchable statistics :readonly="!hatKompetenzUpdate" :removable="false" required />
					<ui-select label="Konfession ASD-Text"
						v-model="model.selectedKonfession.value"
						:manager="konfessionTextSelectManager"
						:validation="() => model.getFehler('idReligion')"
						searchable statistics :readonly="!hatKompetenzUpdate" :removable="false" required />
					<svws-ui-text-input placeholder="Interne Bezeichnung"
						v-model="model.proxy.bezeichnung"
						@change="model.patch"
						:validation="() => model.getFehler('bezeichnung')"
						:max-len="30" :readonly="!hatKompetenzUpdate"
						required />
					<svws-ui-text-input placeholder="Zeugnisbezeichnung"
						v-model="model.proxy.bezeichnungZeugnis"
						@change="model.patch"
						:validation="() => model.getFehler('bezeichnungZeugnis')"
						:max-len="50" :readonly="!hatKompetenzUpdate" />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Ansicht & Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						v-model="model.proxy.sortierung"
						@change="model.patch"
						:validation="() => model.getFehler('sortierung')"
						:readonly="!hatKompetenzUpdate"
						:min="0" :max="32000"
						:removable="false" required />
					<svws-ui-spacing />
					<svws-ui-checkbox v-model="model.proxy.istSichtbar" :readonly="!hatKompetenzUpdate">
						Sichtbar
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">
	import { computed } from "vue";
	import type { KonfessionenDatenProps } from "./KonfessionenDatenProps";
	import { KonfessionModelProxy } from "~/components/schule/kataloge/konfessionen/modelproxy/KonfessionModelProxy";
	import { Religion } from "@core/asd/types/schule/Religion";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { useSchuleState } from "@ui/states/SchuleState";
	import { CoreTypeSelectManager } from "@ui/ui/controls/select/manager/CoreTypeSelectManager";

	const props = defineProps<KonfessionenDatenProps>();
	const benutzerState = useBenutzerState();
	const schuleState = useSchuleState();

	const hatKompetenzUpdate = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));

	const model = new KonfessionModelProxy(
		() => props.manager().daten(),
		() => props.manager().liste.list(),
		props.patch
	);

	const konfessionKuerzelSelectManager = new CoreTypeSelectManager({
		clazz: Religion.class,
		schuljahr: schuleState.abschnitt.schuljahr,
		schulformen: schuleState.schulform,
		optionDisplayText: "kuerzel",
		selectionDisplayText: "kuerzel",
	});

	const konfessionTextSelectManager = new CoreTypeSelectManager({
		clazz: Religion.class,
		schuljahr: schuleState.abschnitt.schuljahr,
		schulformen: schuleState.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});
</script>
