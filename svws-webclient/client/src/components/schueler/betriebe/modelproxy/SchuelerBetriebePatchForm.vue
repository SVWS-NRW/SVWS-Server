<template>
	<svws-ui-input-wrapper :grid="2">
		<svws-ui-content-card :title="`Allgemein`">
			<svws-ui-input-wrapper :grid="2">
				<ui-select label="Betreuende Lehrkraft"
					v-model="model.betreuendeLehrkraft.value"
					:deep-search-attributes="['kuerzel']"
					:manager="lehrerManager"
					:readonly="!hatKompetenzBearbeiten" />
				<ui-select label="Ansprechpartner im Betrieb"
					v-model="model.ansprechpartner.value"
					:manager="ansprechpartnerManager"
					:readonly="!hatKompetenzBearbeiten" />
				<svws-ui-text-input placeholder="Betreuer/Ausbilder"
					v-model="model.proxy.nameAusbilder"
					@change="model.patch"
					:validation="() => model.getFehler('nameAusbilder')"
					:max-len="30" :readonly="!hatKompetenzBearbeiten" />
				<ui-select label="Beschäftigungsart" v-if="istBK"
					v-model="model.beschaeftigungsart.value"
					:manager="beschaeftigungsartenManager"
					searchable :readonly="!hatKompetenzBearbeiten" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-spacing :size="2" />
		<svws-ui-content-card :title="`Details zu ${model.betrieb.value?.name}`">
			<template #title>
				<svws-ui-input-wrapper :grid="2">
					<h3 :class="'content-card--headline'"> {{ `Details zu ${model.betrieb.value?.name}` }} </h3>
					<svws-ui-button v-if="(model.betrieb.value !== null) && hatKatalogeAnsehenKompetenz" class="rounded-md h-fit"
						type="secondary"
						@click="goToBetrieb(model.betrieb.value?.id ?? -1)">
						<span class="icon i-ri-link me-1" />zum Profil
					</svws-ui-button>
				</svws-ui-input-wrapper>
			</template>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Name" span="2"
					:model-value="model.betrieb.value?.name ?? ''"
					readonly />
				<svws-ui-text-input placeholder="Namensergänzung"
					:model-value="model.betrieb.value?.nameZusatz"
					readonly />
				<svws-ui-text-input placeholder="Branche"
					:model-value="model.betrieb.value?.branche"
					readonly />
				<svws-ui-text-input placeholder="Telefon"
					:model-value="model.betrieb.value?.telefon1"
					readonly />
				<svws-ui-text-input placeholder="2. Telefon"
					:model-value="model.betrieb.value?.telefon1"
					readonly />
				<svws-ui-text-input placeholder="Straße und Hausnummer"
					:model-value="adresse"
					readonly />
				<svws-ui-text-input placeholder="Ort"
					:model-value="model.ort.value?.ortsname"
					readonly />
				<svws-ui-text-input placeholder="Fax"
					:model-value="model.betrieb.value?.fax"
					readonly />
				<svws-ui-text-input placeholder="E-Mail"
					:model-value="model.betrieb.value?.eMail"
					readonly />
				<svws-ui-textarea-input placeholder="Bemerkungen" :span="'full'"
					:model-value="model.betrieb.value?.bemerkungen"
					readonly />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</svws-ui-input-wrapper>
</template>

<script setup lang="ts">

	import type { SchuelerBetrieb } from "@core/asd/data/schueler/SchuelerBetrieb";
	import { Schulform } from "@core/asd/types/schule/Schulform";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { AdressenUtils } from "@core/core/utils/AdressenUtils";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { useSchuleState } from "@ui/states/SchuleState";
	import { SelectManager } from "@ui/ui/controls/select/manager/SelectManager";
	import type { SchuelerBetriebeManager } from "@ui/ui/manager/schueler/SchuelerBetriebeManager";
	import { computed } from "vue";
	import { SchuelerBetriebeModelProxy } from "~/components/schueler/betriebe/modelproxy/SchuelerBetriebeModelProxy";

	const props = defineProps<{
		selectedBetrieb: SchuelerBetrieb;
		manager: () => SchuelerBetriebeManager;
		patch: (id: number, data: Partial<SchuelerBetrieb>) => Promise<boolean>;
		goToBetrieb: (idBetrieb: number) => Promise<void>;
	}>();
	const benutzerState = useBenutzerState();
	const schuleState = useSchuleState();

	const istBK = computed(() => {
		const erlaubteSchulformen = [Schulform.BK, Schulform.SB, Schulform.WB];
		return erlaubteSchulformen.includes(schuleState.schulform);
	});
	const hatKatalogeAnsehenKompetenz = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN));
	const hatKompetenzBearbeiten = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN));
	const lehrer = computed(() => props.manager().lehrerById.values());
	const beschaeftigungsarten = computed(() => props.manager().beschaeftigungsartenById.values());
	const ansprechpartner = computed(() => props.manager().ansprechpartnerById.values());
	const model = new SchuelerBetriebeModelProxy(() => props.selectedBetrieb, props.manager, (data) => props.patch(props.selectedBetrieb.id, data));

	const adresse = computed(() => AdressenUtils.combineStrasse(
		model.betrieb.value?.strasse ?? null,
		model.betrieb.value?.hausnummer ?? null,
		model.betrieb.value?.hausnummerZusatz ?? null));

	const lehrerManager = new SelectManager({
		options: lehrer,
		optionDisplayText: v => `${v.nachname}, ${v.vorname}`,
		selectionDisplayText: v => `${v.nachname}, ${v.vorname}`,
	});

	const ansprechpartnerManager = new SelectManager({
		options: ansprechpartner,
		optionDisplayText: v => `${v.name}, ${v.rufname}`,
		selectionDisplayText: v => `${v.name}, ${v.rufname}`,
	});

	const beschaeftigungsartenManager = new SelectManager({
		options: beschaeftigungsarten,
		optionDisplayText: v => v.bezeichnung ?? '—',
		selectionDisplayText: v => v.bezeichnung ?? '—',
	});

</script>
