<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Bezeichnung" class="contentFocusField" span="2"
						v-model="model.proxy.bezeichnung"
						:validation="() => model.getFehler('bezeichnung')"
						@change="model.patch"
						:max-len="250" required :readonly="!hatKompetenzUpdate" />
					<ui-select label="Einwilligungsschlüssel" class="col-span-full"
						v-model="model.einwilligungsschluessel.value"
						:manager="einwilligungsschluesselManager"
						:readonly />
					<svws-ui-textarea-input placeholder="Beschreibung" span="full"
						v-model="model.proxy.beschreibung"
						@change="patchBeschreibung" />
					<svws-ui-text-input placeholder="Personenart" span="2"
						:model-value="textPersonTyp"
						readonly />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Ansicht & Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						v-model="model.proxy.sortierung"
						:validation="() => model.getFehler('sortierung')"
						@change="model.patch"
						:min="0"
						:readonly
						:removable="false" required />
					<svws-ui-spacing />
					<svws-ui-checkbox v-model="model.proxy.istSichtbar"
						:readonly>
						Sichtbar
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { EinwilligungsschluesselKatalogEintrag } from "@core/asd/data/schule/EinwilligungsschluesselKatalogEintrag";
	import { Einwilligungsschluessel } from "@core/asd/types/schule/Einwilligungsschluessel";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { PersonTyp } from "@core/core/types/schule/PersonTyp";
	import { ArrayList } from "@core/java/util/ArrayList";
	import type { List } from "@core/java/util/List";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { useSchuleState } from "@ui/states/SchuleState";
	import { CoreTypeSelectManager } from "@ui/ui/controls/select/manager/CoreTypeSelectManager";
	import type { EinwilligungsartenDatenProps } from "./EinwilligungsartenDatenProps";
	import { computed, watch } from "vue";
	import { EinwilligungsartModelProxy } from "~/components/schule/kataloge/einwilligungsarten/modelproxy/EinwilligungsartModelProxy";

	const props = defineProps<EinwilligungsartenDatenProps>();
	const benutzerState = useBenutzerState();
	const schuleState = useSchuleState();

	const hatKompetenzUpdate = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const model = new EinwilligungsartModelProxy(() => props.manager().daten(), props.manager, schuleState.abschnitt.schuljahr, props.patch);
	const readonly = computed(() => !hatKompetenzUpdate.value);

	const einwilligungsschluesselFilter = {
		key: "isNotUsed",
		apply: (options: List<EinwilligungsschluesselKatalogEintrag>) => {
			const filtered = new ArrayList<EinwilligungsschluesselKatalogEintrag>();
			for (const option of options) {
				if (!einwilligungsschluesselIsUsed(option)) {
					filtered.add(option);
				}
			}
			return filtered;
		},
	};

	function einwilligungsschluesselIsUsed(einwilligungsschluessel: EinwilligungsschluesselKatalogEintrag) {
		for (const einwilligungsart of props.manager().liste.list()) {
			if ((einwilligungsart.id !== props.manager().auswahl().id)
				&& (einwilligungsart.idPersonTyp === props.manager().auswahl().idPersonTyp)
				&& (einwilligungsart.schluessel === einwilligungsschluessel.schluessel)) {
				return true;
			}
		}
		return false;
	}

	const einwilligungsschluesselManager = new CoreTypeSelectManager({
		filters: [einwilligungsschluesselFilter],
		clazz: Einwilligungsschluessel.class,
		schuljahr: schuleState.abschnitt.schuljahr,
		schulformen: schuleState.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const textPersonTyp = computed<string>(() => {
		const personTyp = PersonTyp.getByID(model.proxy.idPersonTyp) ?? null;
		return personTyp ? personTyp.bezeichnung : "";
	});

	async function patchBeschreibung(v: string | null) {
		await props.patch({ beschreibung: v });
	}

	watch(() => props.manager().auswahl(), async () => {
		einwilligungsschluesselManager.updateFilteredOptions();
	}, { immediate: true });

	watch(() => props.manager().auswahl().idPersonTyp, async () => {
		einwilligungsschluesselManager.updateFilteredOptions();
	}, { immediate: true });

</script>
