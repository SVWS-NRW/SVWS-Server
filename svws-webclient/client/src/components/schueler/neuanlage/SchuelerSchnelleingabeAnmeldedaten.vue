<template>
	<svws-ui-content-card title="Anmeldedaten" class="col-span-full">
		<svws-ui-input-wrapper :grid="4">
			<ui-select label="Status"
				v-model="stammdatenModel.status.value"
				:manager="statusManager"
				:validation="() => stammdatenModel.getFehler('status')"
				:removable="false" required
				:readonly />
			<svws-ui-text-input placeholder="Schuljahresabschnitt"
				:model-value="schuljahresabschnitt"
				readonly required />
			<svws-ui-text-input placeholder="Jahrgang"
				:model-value="jahrgang"
				readonly required />
			<ui-select label="Klasse"
				v-model="lernabschnittModel.klasse.value"
				:manager="klassenManager"
				searchable
				:readonly />
			<svws-ui-spacing />
			<ui-select label="Einschulungsart" v-if="schulenMitPrimaerstufe"
				v-model="schulbesuchModel.idEinschulungsartGrundschule.value"
				:manager="einschulungsartManager"
				:readonly />
			<svws-ui-text-input placeholder="Anmeldedatum" type="date"
				v-model="stammdatenModel.proxy.anmeldedatum"
				@change="stammdatenModel.patch"
				:readonly />
			<svws-ui-text-input placeholder="Aufnahmedatum" type="date"
				v-model="stammdatenModel.proxy.aufnahmedatum"
				@change="stammdatenModel.patch"
				:readonly />
			<svws-ui-text-input placeholder="Beginn Bildungsgang" type="date" v-if="schulenMitBKoderSK"
				v-model="stammdatenModel.proxy.beginnBildungsgang"
				@change="stammdatenModel.patch"
				:readonly />
			<svws-ui-input-number placeholder="Dauer Bildungsgang" v-if="schulenMitBKoderSK"
				v-model="stammdatenModel.proxy.dauerBildungsgang"
				@change="stammdatenModel.patch"
				:readonly />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed } from "vue";

	import { Einschulungsart } from "@core/asd/types/schueler/Einschulungsart";
	import { SchuelerStatus } from "@core/asd/types/schueler/SchuelerStatus";
	import { Schulform } from "@core/asd/types/schule/Schulform";
	import { useAbschnittState } from "@ui/states/AbschnittState";
	import { useJahrgaengeState } from "@ui/states/kataloge/JahrgaengeState";
	import { useSchuleState } from "@ui/states/SchuleState";
	import { CoreTypeSelectManager } from "@ui/ui/controls/select/manager/CoreTypeSelectManager";
	import { SelectManager } from "@ui/ui/controls/select/manager/SelectManager";
	import type { SchuelerSchnelleingabeManager } from "@ui/ui/manager/schueler/SchuelerSchnelleingabeManager";

	import type { SchuelerIndividualdatenModelProxy } from "../individualdaten/modelproxy/SchuelerIndividualdatenModelProxy";
	import type { SchuelerLernabschnittAllgemeinModelProxy } from "../lernabschnitte/allgemein/modelproxy/SchuelerLernabschnittAllgemeinModelProxy";
	import type { SchuelerSchulbesuchModelProxy } from "../schulbesuch/modelProxy/SchuelerSchulbesuchModelProxy";

	const props = defineProps<{
		manager: () => SchuelerSchnelleingabeManager;
		stammdatenModel: SchuelerIndividualdatenModelProxy;
		schulbesuchModel: SchuelerSchulbesuchModelProxy;
		lernabschnittModel: SchuelerLernabschnittAllgemeinModelProxy;
		schulenMitPrimaerstufe: boolean;
		readonly: boolean;
	}>();
	const abschnittState = useAbschnittState();
	const schuleState = useSchuleState();
	const jahrgaengeState = useJahrgaengeState();

	const schulenMitBKoderSK = computed(() => (schuleState.schulform === Schulform.BK) || (schuleState.schulform === Schulform.SK));

	const klassen = computed(() => {
		return [...props.manager().klassenAktuell]
			.filter(k => k.idJahrgang === props.lernabschnittModel.proxy.jahrgangID);
	});

	const schuljahresabschnitt = computed<string>(() => {
		const abschnitt = [...props.manager().schuljahresabschnitte].find(i => i.id === props.lernabschnittModel.proxy.schuljahresabschnitt) ?? null;
		if (abschnitt === null) {
			return '';
		}
		return `${abschnitt.schuljahr}/${(abschnitt.schuljahr + 1) % 100}.${abschnitt.abschnitt}`;
	});

	const jahrgang = computed<string | null>(() => Array.from(jahrgaengeState.jahrgaenge.list)
		.find(i => i.id === (props.lernabschnittModel.proxy.jahrgangID))?.kuerzel ?? null);

	const statusManager = new CoreTypeSelectManager({
		clazz: SchuelerStatus.class,
		schuljahr: abschnittState.auswahl.schuljahr,
		schulformen: schuleState.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text" });

	const klassenManager = new SelectManager({
		options: klassen,
		optionDisplayText: i => i.kuerzel ?? '',
		selectionDisplayText: i => i.kuerzel ?? '',
	});

	const einschulungsartManager = new CoreTypeSelectManager({
		clazz: Einschulungsart.class,
		schulformen: schuleState.schulform,
		schuljahr: schuleState.abschnitt.schuljahr,
		optionDisplayText: i => i.text,
		selectionDisplayText: i => i.text,
	});

</script>
