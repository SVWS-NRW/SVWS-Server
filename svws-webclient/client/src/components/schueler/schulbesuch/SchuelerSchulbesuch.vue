<template>
	<Teleport to=".svws-ui-header--actions" defer>
		<svws-ui-modal-hilfe>
			<hilfe-schueler-schulbesuch />
		</svws-ui-modal-hilfe>
	</Teleport>
	<div class="page page-grid-cards">
		<!-- Vor der Aufnahme besucht !-->
		<svws-ui-content-card title="Vor der Aufnahme besucht">
			<svws-ui-input-wrapper :grid="2">
				<ui-select label="Schule"
					:manager="vorherigeSchuleManager"
					v-model="model.vorherigeSchule.value"
					searchable :readonly />
				<svws-ui-button type="transparent"
					@click="goToSchule(manager().daten.idVorherigeSchule ?? -1)">
					<span class="icon i-ri-link" />Zur Schule
				</svws-ui-button>
				<svws-ui-text-input placeholder="allgemeine Herkunft"
					:model-value="model.vorherigeAllgHerkunft.value"
					statistics readonly />
				<svws-ui-text-input placeholder="Statistik-Schulnummer"
					:model-value="model.schulnummerStatistik.value"
					statistics readonly />
				<svws-ui-text-input placeholder="Entlassen am" type="date"
					v-model="model.proxy.vorigeEntlassdatum"
					statistics :readonly />
				<ui-select label="Entlassjahrgang"
					:manager="vorherigeEntlassjahrgaengeManager"
					v-model="model.vorigeEntlassjahrgang.value"
					searchable statistics
					:disabled="model.vorherigeSchule.value === undefined" :readonly />
				<svws-ui-text-input placeholder="Bemerkung" span="full"
					v-model="model.proxy.vorigeBemerkung"
					:validation="() => model.getFehler('vorigeBemerkung')"
					@commit="model.patch"
					:max-len="255" :readonly />
				<ui-select label="Entlassgrund"
					:manager="vorherigerEntlassgrundManager"
					v-model="model.vorigeEntlassgrundID.value"
					searchable :readonly />
				<svws-ui-text-input placeholder="höchster Abschluss, der von der anderen Schule mitgebracht wurde"
					v-model="model.proxy.vorigeAbschlussartID"
					disabled statistics :readonly />
				<!-- TODO: durch Ui-Select ersetzen: siehe Issue#3495-->
				<svws-ui-text-input placeholder="Versetzung" span="full"
					:model-value="model.vorigeArtLetzteVersetzung.value?.text ?? ''"
					readonly statistics />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>

		<!-- Entlassung von eigener Schule !-->
		<svws-ui-content-card title="Entlassung von eigener Schule">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Entlassen am" type="date" class="contentFocusField"
					v-model="model.proxy.entlassungDatum"
					statistics :readonly />
				<ui-select label="Entlassjahrgang"
					:manager="jahrgaengeManager"
					v-model="model.idEntlassjahrgang.value"
					searchable :readonly />
				<ui-select label="Entlassgrund"
					:manager="entlassgrundManager"
					v-model="model.entlassungGrundID.value"
					searchable :readonly />
				<svws-ui-text-input placeholder="Art des Abschlusses" span="full"
					v-model="model.proxy.entlassungAbschlussartID"
					disabled statistics :readonly />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>

		<!-- Wechsel zu aufnehmender Schule !-->
		<svws-ui-content-card title="Wechsel zu aufnehmender Schule">
			<template #actions>
				<svws-ui-checkbox title="Wechsel bevorstehend" v-if="serverState.hasDev"
					v-model="wechselBevorstehend"
					:indeterminate="manager().daten.aufnehmendBestaetigt === null"
					disabled :readonly>
					<!-- Disabled, solange keine Backend-Funktionalität für den Schulwechsel implementiert ist. -->
					Wechsel bevorstehend
				</svws-ui-checkbox>
				<svws-ui-checkbox title="Aufnahme bestätigt"
					v-model="model.proxy.aufnehmendBestaetigt"
					:indeterminate="manager().daten.aufnehmendBestaetigt === null"
					focus-class-content :readonly>
					Aufnahme bestätigt
				</svws-ui-checkbox>
			</template>
			<svws-ui-input-wrapper :grid="2">
				<ui-select label="Schule"
					:manager="aufnehmendeSchuleManager"
					v-model="model.idAufnehmendeSchule.value"
					searchable :readonly />
				<svws-ui-button type="transparent"
					@click="goToSchule(manager().daten.idAufnehmendeSchule ?? -1)"
					:disabled="manager().daten.idAufnehmendeSchule === null"
					:readonly>
					<span class="icon i-ri-link" />Zur Schule
				</svws-ui-button>
				<svws-ui-text-input placeholder="Wechseldatum" type="date"
					:model-value="manager().daten.aufnehmendWechseldatum"
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
					searchable :readonly />
				<ui-select label="Dauer des Kindergartenbesuchs"
					:manager="dauerKindergartenbesuchManager"
					v-model="model.idDauerKindergartenbesuch.value"
					searchable :readonly />
				<svws-ui-checkbox title="Verpflichtung f. Sprachförderkurs"
					v-model="model.proxy.verpflichtungSprachfoerderkurs"
					:readonly>
					Verpflichtung für Sprachförderkurs
				</svws-ui-checkbox>
				<svws-ui-checkbox title="Teilnahme an Sprachförderkurs"
					v-model="model.proxy.teilnahmeSprachfoerderkurs"
					:readonly>
					Teilnahme an Sprachförderkurs
				</svws-ui-checkbox>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<div v-else />

		<!-- Grundschulbesuch !-->
		<svws-ui-content-card title="Grundschulbesuch">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-input-number placeholder="Einschulung" class="contentFocusField"
					v-model="model.proxy.grundschuleEinschulungsjahr"
					:validation="() => model.getFehler('grundschuleEinschulungsjahr')"
					@commit="model.patch"
					:min="1900" :max="2100"
					statistics :readonly />
				<ui-select label="Einschulungsart"
					:manager="einschulungsartenManager"
					v-model="model.grundschuleEinschulungsartID.value"
					searchable :readonly />
				<ui-select label="EP-Jahre"
					:manager="grundschuleJahreEingangsphaseManager"
					v-model="model.idGrundschuleJahreEingangsphase.value"
					searchable :readonly />
				<ui-select label="Übergangsempfehlung Jg. 5"
					:manager="grundschuleUebergangsempfehlungManager"
					v-model="model.idGrundschuleUebergangsempfehlung.value"
					searchable :readonly />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>

		<!-- Sekundarstufe I !-->
		<svws-ui-content-card title="Sekundarstufe I">
			<svws-ui-input-wrapper>
				<svws-ui-input-number placeholder="Jahr Wechsel Sek I" class="contentFocusField"
					v-model="model.proxy.sekIWechsel"
					:validation="() => model.getFehler('sekIWechsel')"
					@commit="model.patch"
					:min="1900" :max="2100"
					statistics :readonly />
				<ui-select label="Erste Schulform Sek I"
					:manager="sekIErsteSchulformManager"
					v-model="model.sekIErsteSchulform.value"
					searchable :readonly />
				<svws-ui-input-number placeholder="Jahr Wechsel Sek II"
					v-model="model.proxy.sekIIWechsel"
					:validation="() => model.getFehler('sekIIWechsel')"
					@commit="model.patch"
					:min="1900" :max="2100"
					statistics :readonly />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<schueler-schulbesuch-merkmale :manager
			:get-merkmale="() => props.manager().daten.merkmale"
			:add-merkmal
			:patch-merkmal
			:delete-merkmale
			:update-kompetenz />
		<schueler-schulbesuch-bisherige-schulen :manager
			:get-bisherige-schulen="() => props.manager().daten.alleSchulen"
			:add-bisherige-schule
			:patch-bisherige-schule
			:delete-bisherige-schulen
			:update-kompetenz />
	</div>
</template>

<script setup lang="ts">

	import type { JahrgangsDaten, KatalogEntlassgrund, Kindergarten, SchulEintrag } from "@core";
	import { BenutzerKompetenz, Einschulungsart, Jahrgaenge, Kindergartenbesuch, PrimarstufeSchuleingangsphaseBesuchsjahre, Schulform, Uebergangsempfehlung } from "@core";
	import type { SchuelerSchulbesuchProps } from './SchuelerSchulbesuchProps';
	import { CoreTypeSelectManager, SelectManager, useSchuleState, useServerState } from "@ui";
	import { computed, ref } from "vue";
	import { SchuelerSchulbesuchModelProxy } from "~/components/schueler/schulbesuch/modelProxy/SchuelerSchulbesuchModelProxy";

	const props = defineProps<SchuelerSchulbesuchProps>();
	const schuleState = useSchuleState();
	const serverState = useServerState();

	const updateKompetenz = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN));
	const readonly = computed(() => !updateKompetenz.value);
	const model = new SchuelerSchulbesuchModelProxy(
		() => props.manager().daten,
		() => props.manager(),
		(data) => props.patch(props.manager().daten.id, data)
	);
	const schuljahr = computed(() => props.manager().schuljahr);
	const schuleHatPrimarstufe = computed(
		() => [Schulform.G, Schulform.FW, Schulform.WF, Schulform.GM, Schulform.KS, Schulform.S, Schulform.GE, Schulform.V].includes(schuleState.schulform));
	const wechselBevorstehend = ref<boolean>(false);

	const vorherigeSchuleManager = new SelectManager<SchulEintrag>({
		options: computed(() => props.manager().schulenById.values()),
		optionDisplayText: bezeichnungSchule,
		selectionDisplayText: bezeichnungSchule,
	});

	const vorherigeEntlassjahrgaengeManager = new CoreTypeSelectManager({
		clazz: Jahrgaenge.class,
		schuljahr: schuljahr,
		schulformen: model.vorherigeSchulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const vorherigerEntlassgrundManager = new SelectManager<KatalogEntlassgrund>({
		options: computed(() => props.manager().entlassgruendeById.values()),
		optionDisplayText: s => s.bezeichnung,
		selectionDisplayText: s => s.bezeichnung,
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
