<template>
	<Teleport to=".svws-ui-header--actions" defer>
		<wiedervorlage-modal type="lehrkraft" mode="create"
			:person-id="lehrerListeManager().daten().id"
			:person-name="`${lehrerListeManager().daten().vorname} ${lehrerListeManager().daten().nachname}`">
			<template #default="{openModal}">
				<svws-ui-button @click="openModal" type="secondary">
					<span class="icon i-ri-alarm-line" aria-hidden="true" /> Wiedervorlage anlegen
				</svws-ui-button>
			</template>
		</wiedervorlage-modal>
		<svws-ui-modal-hilfe> <hilfe-lehrer-individualdaten /> </svws-ui-modal-hilfe>
	</Teleport>

	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<template #actions>
				<svws-ui-checkbox :readonly v-model="modelProxy.proxy.istSichtbar" focus-class-content>
					Ist sichtbar
				</svws-ui-checkbox>
				<svws-ui-checkbox :readonly v-model="modelProxy.proxy.istRelevantFuerStatistik" statistics>
					Ist relevant für Statistik
				</svws-ui-checkbox>
			</template>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Kürzel"
					v-model="modelProxy.proxy.kuerzel"
					@commit="modelProxy.patch"
					:validation="() => modelProxy.getFehler('kuerzel')"
					:max-len="10"
					:readonly statistics required focus />
				<ui-select label="Personal-Typ"
					v-model="modelProxy.selectedPersonalTyp.value"
					:manager="personaltypManger"
					:validation="() => modelProxy.getFehler('personalTyp')"
					:readonly required searchable :removable="false" />
				<svws-ui-text-input placeholder="Nachname"
					v-model="modelProxy.proxy.nachname"
					@commit="modelProxy.patch"
					:validation="() => modelProxy.getFehler('nachname')"
					:max-len="120"
					:readonly required statistics />
				<svws-ui-text-input placeholder="Rufname"
					v-model="modelProxy.proxy.vorname"
					@commit="modelProxy.patch"
					:validation="() => modelProxy.getFehler('vorname')"
					:max-len="80"
					:readonly required statistics />
				<svws-ui-spacing />
				<ui-select label="Geschlecht"
					v-model="modelProxy.selectedGeschlecht.value"
					:manager="geschlechtManager"
					:validation="() => modelProxy.getFehler('geschlecht')"
					:readonly required searchable :removable="false" />
				<svws-ui-text-input placeholder="Geburtsdatum"
					v-model="modelProxy.proxy.geburtsdatum"
					type="date"
					@commit="modelProxy.patch"
					:validation="() => modelProxy.getFehler('geburtsdatum')"
					:readonly required statistics />
				<ui-select label="Staatsangehörigkeit"
					v-model="modelProxy.selectedStaatsangehoerigkeit.value"
					:manager="staatsangehoerigkeitManager"
					:validation="() => modelProxy.getFehler('idStaatsangehoerigkeit')"
					:readonly required searchable statistics :removable="false" />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Akademischer Grad"
					v-model="modelProxy.proxy.titel"
					@commit="modelProxy.patch"
					:validation="() => modelProxy.getFehler('titel')"
					:max-len="20"
					:readonly />
				<svws-ui-text-input placeholder="Amtsbezeichnung"
					v-model="modelProxy.proxy.amtsbezeichnung"
					@commit="modelProxy.patch"
					:validation="() => modelProxy.getFehler('amtsbezeichnung')"
					:max-len="15"
					:readonly />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Wohnort und Kontaktdaten">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Straße"
					v-model="modelProxy.adresse.value"
					@commit="modelProxy.patch"
					:validation="() => modelProxy.getFehler('strassenname')"
					:max-len="55"
					span="full"
					class="contentFocusField"
					:readonly />
				<ui-select label="Wohnort"
					v-model="modelProxy.selectedWohnort.value"
					:manager="wohnortManager"
					:validation="() => modelProxy.getFehler('wohnortID')"
					:readonly searchable />
				<ui-select label="Ortsteil"
					v-model="modelProxy.selectedOrtsteil.value"
					:manager="ortsteilManager"
					:validation="() => modelProxy.getFehler('ortsteilID')"
					:readonly searchable />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Telefon"
					type="tel"
					v-model="modelProxy.proxy.telefon"
					@commit="modelProxy.patch"
					:validation="() => modelProxy.getFehler('telefon')"
					:max-len="20"
					:readonly />
				<svws-ui-text-input placeholder="Mobil oder Fax"
					type="tel"
					v-model="modelProxy.proxy.telefonMobil"
					@commit="modelProxy.patch"
					:validation="() => modelProxy.getFehler('telefonMobil')"
					:max-len="20"
					:readonly />
				<svws-ui-text-input placeholder="Private E-Mail-Adresse"
					type="email"
					v-model="modelProxy.proxy.emailPrivat"
					@commit="modelProxy.patch"
					:validation="() => modelProxy.getFehler('emailPrivat')"
					:max-len="100"
					:readonly />
				<svws-ui-text-input placeholder="Schulische E-Mail-Adresse"
					type="email"
					v-model="modelProxy.proxy.emailDienstlich"
					@commit="modelProxy.patch"
					:validation="() => modelProxy.getFehler('emailDienstlich')"
					:max-len="100"
					:readonly />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Leitungsfunktionen">
			<svws-ui-table :columns="colsLeitungsfunktionen" :items="dataNotPatched().leitungsfunktionen" count>
				<template #cell(idLeitungsfunktion)="{ value }">
					{{ LehrerLeitungsfunktion.data().getWertByID(value)?.daten(schuljahr)?.text ?? '—' }}
				</template>
				<template #cell(beginn)="{ value }">
					{{ (value === null) || (JavaString.isBlank(value)) ? '—' : DateUtils.gibDatumGermanFormat(value) }}
				</template>
				<template #cell(ende)="{ value }">
					{{ (value === null) || (JavaString.isBlank(value)) ? '—' : DateUtils.gibDatumGermanFormat(value) }}
				</template>
			</svws-ui-table>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { NationalitaetenKatalogEintrag, OrtsteilKatalogEintrag } from "@core";
	import { BenutzerKompetenz, DateUtils, Geschlecht, JavaString, LehrerLeitungsfunktion, Nationalitaeten, PersonalTyp } from "@core";
	import { CoreTypeSelectManager, SelectManager, useSchuleState } from "@ui";
	import type { LehrerIndividualdatenProps } from "./LehrerIndividualdatenProps";
	import { LehrerIndividualdatenModelProxy } from "./modelproxy/LehrerIndividualdatenModelProxy";
	import WiedervorlageModal from "~/components/wiedervorlage/WiedervorlageModal.vue";

	const props = defineProps<LehrerIndividualdatenProps>();
	const schuleState = useSchuleState();

	const manager = () => props.lehrerListeManager();
	const dataNotPatched = () => props.lehrerListeManager().daten();
	const modelProxy = new LehrerIndividualdatenModelProxy(dataNotPatched, () => schuleState.validatorKontext, manager, props.orteById, props.ortsteileById, props.patch);

	const schuljahr = computed<number>(() => props.lehrerListeManager().getSchuljahr());

	const readonly = computed<boolean>(() => !props.benutzerKompetenzen.has(BenutzerKompetenz.LEHRERDATEN_AENDERN));

	const colsLeitungsfunktionen = [
		{ key: 'idLeitungsfunktion', label: 'Funktion', span: 2 },
		{ key: 'beginn', label: 'Von', span: 1 },
		{ key: 'ende', label: 'Bis', span: 1 },
	];


	/**
	 * Selects
	 */
	const personaltypManger = new SelectManager({
		options: PersonalTyp.values(),
		optionDisplayText: typ => typ.bezeichnung,
		selectionDisplayText: typ => typ.bezeichnung,
	});

	const geschlechtManager = new SelectManager({
		options: Geschlecht.values(),
		optionDisplayText: geschlecht => geschlecht.text,
		selectionDisplayText: geschlecht => geschlecht.text,
	});

	const staatsangehoerigkeitManager = new CoreTypeSelectManager({
		clazz: Nationalitaeten.class,
		optionDisplayText: nationalitaet => nationalitaet.staatsangehoerigkeit,
		selectionDisplayText: nationalitaet => nationalitaet.staatsangehoerigkeit,
		sort: staatsangehoerigkeitSort,
	});

	function staatsangehoerigkeitSort(a: NationalitaetenKatalogEintrag, b: NationalitaetenKatalogEintrag): number {
		const va = a.staatsangehoerigkeit;
		const vb = b.staatsangehoerigkeit;
		if ((va.length > 0) && (vb.length > 0)) {
			return va.localeCompare(vb);
		} else if ((va.length > 0) && (vb.length === 0)) {
			return -1;
		} else if ((va.length === 0) && (vb.length > 0)) {
			return 1;
		}
		return 0;
	}


	const orte = computed(() => props.orteById.values());
	const wohnortManager = new SelectManager({
		options: orte,
		optionDisplayText: ort => `${ort.plz ?? '—'} ${ort.ortsname ?? '—'}`,
		selectionDisplayText: ort => `${ort.plz ?? '—'} ${ort.ortsname ?? '—'}`,
	});

	const ortsteile = computed<Array<OrtsteilKatalogEintrag>>(() => {
		const result: Array<OrtsteilKatalogEintrag> = [];
		if (modelProxy.proxy.wohnortID === null) {
			return result;
		}
		for (const ortsteil of props.ortsteileById.values()) {
			if (ortsteil.ort_id === modelProxy.proxy.wohnortID) {
				result.push(ortsteil);
			}
		}
		return result;
	});
	const ortsteilManager = new SelectManager({
		options: ortsteile,
		optionDisplayText: ortsteil => ortsteil.ortsteil ?? '—',
		selectionDisplayText: ortsteil => ortsteil.ortsteil ?? '—',
		sort: ortsteilSort,
	});

	function ortsteilSort(a: OrtsteilKatalogEintrag, b: OrtsteilKatalogEintrag): number {
		if ((a.ortsteil !== null) && (b.ortsteil !== null)) {
			return a.ortsteil.localeCompare(b.ortsteil);
		} else if ((a.ortsteil !== null) && (b.ortsteil === null)) {
			return -1;
		} else if ((a.ortsteil === null) && (b.ortsteil !== null)) {
			return 1;
		}
		return 0;
	}

</script>
