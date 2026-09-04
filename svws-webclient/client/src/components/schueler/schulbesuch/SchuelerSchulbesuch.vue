<template>
	<Teleport to=".svws-ui-header--actions" defer>
		<svws-ui-modal-hilfe>
			<hilfe-schueler-schulbesuch />
		</svws-ui-modal-hilfe>
	</Teleport>
	<div class="page page-grid-cards">
		<!-- Vorhandene Abschlüsse !-->
		<svws-ui-content-card title="Vorhandene Abschlüsse" v-if="!eigeneSchuleIstGrundschule">
			<svws-ui-input-wrapper :grid="2">
				<ui-select label="Höchster allgemeinbildender Abschluss"
					:manager="hoechsterAbschlussManager"
					v-model="model.hoechsterSchulabschluss.value"
					:readonly />
				<svws-ui-checkbox v-if="eigeneSchuleIstBKOderWBK"
					v-model="model.proxy.berufsabschlussVorhanden"
					:readonly>
					Berufsabschluss vorhanden
				</svws-ui-checkbox>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<div v-else />
		<svws-ui-content-card title="Schulbesuchsjahre" v-if="showSchulbesuchsjahre">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-input-number placeholder="Schulbesuchsjahre"
					:model-value="schulbesuchsjahre"
					readonly />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<div v-else />
		<schulbesuch-vorherige-schule :manager :go-to-schule :model :readonly />
		<!-- Entlassung von eigener Schule !-->
		<svws-ui-content-card title="Entlassung von eigener Schule">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Entlassen am" type="date" class="contentFocusField"
					v-model="model.proxy.entlassdatumDieseSchule"
					statistics :readonly />
				<ui-select label="Entlassjahrgang"
					:manager="jahrgaengeManager"
					v-model="model.idEntlassjahrgangDieseSchule.value"
					:readonly />
				<ui-select label="Entlassgrund"
					:manager="entlassgrundManager"
					v-model="model.idEntlassgrundDieseSchule.value"
					:readonly />
				<svws-ui-text-input placeholder="Art des Abschlusses" span="full"
					v-model="model.proxy.idAbschlussartDieseSchule"
					disabled statistics :readonly />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>

		<!-- Wechsel zu aufnehmender Schule !-->
		<svws-ui-content-card title="Wechsel zu aufnehmender Schule">
			<template #actions>
				<svws-ui-checkbox v-if="serverState.hasDev"
					v-model="wechselBevorstehend"
					:indeterminate="manager().daten.wechselBestaetigtAufnehmendeSchule === null"
					disabled :readonly>
					<!-- Disabled, solange keine Backend-Funktionalität für den Schulwechsel implementiert ist. -->
					Wechsel bevorstehend
				</svws-ui-checkbox>
				<svws-ui-checkbox v-model="model.proxy.wechselBestaetigtAufnehmendeSchule"
					:indeterminate="manager().daten.wechselBestaetigtAufnehmendeSchule === null"
					focus-class-content :readonly>
					Aufnahme bestätigt
				</svws-ui-checkbox>
			</template>
			<svws-ui-input-wrapper :grid="2">
				<ui-select label="Schule"
					:manager="aufnehmendeSchuleManager"
					v-model="model.idAufnehmendeSchule.value"
					:readonly />
				<svws-ui-button type="transparent"
					@click="goToSchule(manager().daten.idAufnehmendeSchule ?? -1)"
					:disabled="manager().daten.idAufnehmendeSchule === null"
					:readonly>
					<span class="icon i-ri-link" />Zur Schule
				</svws-ui-button>
				<svws-ui-text-input placeholder="Wechseldatum" type="date"
					:model-value="manager().daten.wechseldatumAufnehmendeSchule"
					readonly />
				<ui-select label="Wechselgrund"
					v-if="serverState.hasDev"
					:manager="schulwechselGrundSelectManager"
					:disabled="!wechselBevorstehend" :readonly />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>

		<!-- Kindergartenbesuch !-->
		<svws-ui-content-card title="Kindergartenbesuch" v-if="schuleHatPrimarstufe">
			<svws-ui-input-wrapper :grid="2">
				<ui-select label="Name des Kindergartens"
					:manager="kindergartenManager"
					v-model="model.idKindergarten.value"
					:readonly />
				<ui-select label="Dauer des Kindergartenbesuchs"
					:manager="dauerKindergartenbesuchManager"
					v-model="model.idDauerKindergartenbesuch.value"
					:readonly />
				<svws-ui-checkbox v-model="model.proxy.verpflichtungSprachfoerderkurs" :readonly>
					Verpflichtung für Sprachförderkurs
				</svws-ui-checkbox>
				<svws-ui-checkbox v-model="model.proxy.teilnahmeSprachfoerderkurs" :readonly>
					Teilnahme an Sprachförderkurs
				</svws-ui-checkbox>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<div v-else />

		<!-- Grundschulbesuch !-->
		<svws-ui-content-card title="Grundschulbesuch">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-input-number placeholder="Einschulung" class="contentFocusField"
					v-model="model.proxy.einschulungsjahrGrundschule"
					:validation="() => model.getFehler('einschulungsjahrGrundschule')"
					@change="model.patch"
					:min="1900" :max="2100"
					statistics :readonly />
				<ui-select label="Einschulungsart"
					:manager="einschulungsartenManager"
					v-model="model.idEinschulungsartGrundschule.value"
					:readonly />
				<ui-select label="EP-Jahre"
					:manager="grundschuleJahreEingangsphaseManager"
					v-model="model.idEingangsphaseGrundschule.value"
					:readonly />
				<ui-select label="Übergangsempfehlung Jg. 5"
					:manager="grundschuleUebergangsempfehlungManager"
					v-model="model.idUebergangsempfehlungGrundschule.value"
					:readonly />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>

		<!-- Sekundarstufe I !-->
		<svws-ui-content-card title="Sekundarstufe I" v-if="!eigeneSchuleIstGrundschule">
			<svws-ui-input-wrapper>
				<svws-ui-input-number placeholder="Jahr Wechsel Sek I" class="contentFocusField"
					v-model="model.proxy.wechseljahrSekI"
					:validation="() => model.getFehler('wechseljahrSekI')"
					@change="model.patch"
					:min="1900" :max="2100"
					statistics :readonly />
				<ui-select label="Erste Schulform Sek I"
					:manager="sekIErsteSchulformManager"
					v-model="model.kuerzelErsteSchulformSek1.value"
					:readonly />
				<svws-ui-input-number placeholder="Jahr Wechsel Sek II"
					v-model="model.proxy.wechseljahrSekII"
					:validation="() => model.getFehler('wechseljahrSekII')"
					@change="model.patch"
					:min="1900" :max="2100"
					statistics :readonly />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<div v-else />
		<schueler-schulbesuch-merkmale :manager
			:get-merkmale="() => props.manager().daten.merkmale"
			:add-merkmal
			:patch-merkmal
			:delete-merkmale
			:update-kompetenz />
		<schueler-schulbesuch-bisherige-schulen :manager
			:get-bisherige-schulen="() => props.manager().daten.bisherBesuchteSchulen"
			:add-bisherige-schule
			:patch-bisherige-schule
			:delete-bisherige-schulen
			:update-kompetenz />
	</div>
</template>

<script setup lang="ts">

	import { PrimarstufeSchuleingangsphaseBesuchsjahre } from '@core/asd/types/jahrgang/PrimarstufeSchuleingangsphaseBesuchsjahre';
	import { Einschulungsart } from '@core/asd/types/schueler/Einschulungsart';
	import { Uebergangsempfehlung } from '@core/asd/types/schueler/Uebergangsempfehlung';
	import { Kindergartenbesuch } from '@core/asd/types/schule/Kindergartenbesuch';
	import { SchulabschlussAllgemeinbildend } from '@core/asd/types/schule/SchulabschlussAllgemeinbildend';
	import { Schulform } from '@core/asd/types/schule/Schulform';
	import type { JahrgangsDaten } from '@core/core/data/jahrgang/JahrgangsDaten';
	import type { KatalogEntlassgrund } from '@core/core/data/kataloge/KatalogEntlassgrund';
	import type { SchulEintrag } from '@core/core/data/kataloge/SchulEintrag';
	import type { Kindergarten } from '@core/core/data/schule/Kindergarten';
	import { BenutzerKompetenz } from '@core/core/types/benutzer/BenutzerKompetenz';
	import { useBenutzerState } from '@ui/states/BenutzerState';
	import { useSchuleState } from '@ui/states/SchuleState';
	import { useServerState } from '@ui/states/ServerState';
	import { CoreTypeSelectManager } from '@ui/ui/controls/select/manager/CoreTypeSelectManager';
	import { SelectManager } from '@ui/ui/controls/select/manager/SelectManager';
	import type { SchuelerSchulbesuchProps } from './SchuelerSchulbesuchProps';
	import { computed, ref } from "vue";
	import { SchuelerSchulbesuchModelProxy } from "~/components/schueler/schulbesuch/modelProxy/SchuelerSchulbesuchModelProxy";
	import SchulbesuchVorherigeSchule from "~/components/schueler/schulbesuch/SchulbesuchVorherigeSchule.vue";

	const props = defineProps<SchuelerSchulbesuchProps>();
	const benutzerState = useBenutzerState();
	const schuleState = useSchuleState();
	const serverState = useServerState();

	const updateKompetenz = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN));
	const readonly = computed(() => !updateKompetenz.value);
	const model = new SchuelerSchulbesuchModelProxy(
		() => props.manager().daten,
		() => props.manager(),
		(data) => props.patch(props.manager().daten.id, data)
	);
	const schuljahr = computed(() => props.manager().schuljahr);

	const schuleHatPrimarstufe = computed(
		() => [Schulform.G, Schulform.FW, Schulform.WF, Schulform.GM, Schulform.KS, Schulform.S, Schulform.GE, Schulform.V].includes(schuleState.schulform));
	const eigeneSchuleIstBKOderWBK = computed(
		() => [Schulform.SB, Schulform.BK, Schulform.WB].includes(schuleState.schulform));
	const eigeneSchuleIstGrundschule = computed(() => schuleState.schulform === Schulform.G);

	const wechselBevorstehend = ref<boolean>(false);

	// --- Schulbesuchsjahre ---
	const showSchulbesuchsjahre = computed(
		() => [Schulform.FW, Schulform.G, Schulform.GE, Schulform.GY, Schulform.H, Schulform.R, Schulform.S, Schulform.KS, Schulform.SG, Schulform.SK,
			Schulform.SR, Schulform.V, Schulform.WF, Schulform.HI].includes(schuleState.schulform));
	const schulbesuchsjahre = computed<number | null>(() => {
		if ((model.proxy.einschulungsjahrGrundschule === null) || (model.proxy.idEingangsphaseGrundschule === null)) {
			return null;
		}
		const selectedEingangsPhase = PrimarstufeSchuleingangsphaseBesuchsjahre.data().getWertByIDOrNull(model.proxy.idEingangsphaseGrundschule);
		if (selectedEingangsPhase === null) {
			return null;
		}
		let result = schuleState.abschnitt.schuljahr - model.proxy.einschulungsjahrGrundschule;
		// Das dritte EP-Jahr wird nicht zum Schulbesuch dazugerechnet -> als würde man ein Jahr später eingeschult werden
		if (PrimarstufeSchuleingangsphaseBesuchsjahre.E3.statistikId() === selectedEingangsPhase.statistikId()) {
			result = result - 1;
		}
		return Math.max(result, 0);
	});


	// --- Toggle Schulauswahl ---

	const hoechsterAbschlussManager = new CoreTypeSelectManager({
		clazz: SchulabschlussAllgemeinbildend.class,
		schuljahr: schuljahr,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const jahrgaengeManager = new SelectManager<JahrgangsDaten>({
		options: computed(() => props.manager().jahrgaengeById.values()),
		optionDisplayText: j => j.bezeichnung ?? '-',
		selectionDisplayText: j => j.bezeichnung ?? '-',
	});

	const entlassgrundManager = new SelectManager<KatalogEntlassgrund>({
		options: computed(() => props.manager().entlassgruendeById.values()),
		optionDisplayText: s => s.bezeichnung,
		selectionDisplayText: s => s.bezeichnung,
	});

	const kindergartenManager = new SelectManager<Kindergarten>({
		options: computed(() => props.manager().kindergaertenById.values()),
		optionDisplayText: s => s.bezeichnung,
		selectionDisplayText: s => s.bezeichnung,
	});

	const dauerKindergartenbesuchManager = new CoreTypeSelectManager({
		clazz: Kindergartenbesuch.class,
		schuljahr: schuljahr,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const aufnehmendeSchuleManager = new SelectManager<SchulEintrag>({
		options: computed(() => props.manager().schulenById.values()),
		optionDisplayText: bezeichnungSchule,
		selectionDisplayText: bezeichnungSchule,
	});

	const einschulungsartenManager = new CoreTypeSelectManager({
		clazz: Einschulungsart.class,
		schuljahr: schuljahr,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const grundschuleJahreEingangsphaseManager = new CoreTypeSelectManager({
		clazz: PrimarstufeSchuleingangsphaseBesuchsjahre.class,
		schuljahr: schuljahr,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const grundschuleUebergangsempfehlungManager = new CoreTypeSelectManager({
		clazz: Uebergangsempfehlung.class,
		schuljahr: schuljahr,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const sekIErsteSchulformManager = new CoreTypeSelectManager({
		clazz: Schulform.class,
		schuljahr: schuljahr,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const schulwechselGrundSelectManager = new SelectManager({
		options: computed(() => props.manager().entlassgruendeById.values()),
		optionDisplayText: (option) => option.bezeichnung,
		selectionDisplayText: (option) => option.bezeichnung,
	});

	function bezeichnungSchule(s: SchulEintrag) {
		return `${s.schulnummerStatistik}: ${s.name}`;
	}

</script>
