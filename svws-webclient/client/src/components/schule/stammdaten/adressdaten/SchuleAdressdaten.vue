<template>
	<div class="flex flex-col w-full h-full overflow-hidden">
		<div class="page page-grid-cards">
			<svws-ui-input-wrapper :grid="1">
				<svws-ui-content-card title="Stammdaten">
					<svws-ui-input-wrapper :grid="2">
						<svws-ui-text-input placeholder="Bezeichnung 1" class="contentFocusField"
							v-model="schuleModel.proxy.bezeichnung1"
							:validation="() => schuleModel.getFehler('bezeichnung1')"
							@change="schuleModel.patch"
							:readonly="!hatKompetenzUpdate" :max-len="50" required />
						<svws-ui-text-input placeholder="Schulnummer"
							:model-value="schuleState.stammdaten.schulNr.toString()"
							readonly statistics />
						<svws-ui-text-input placeholder="Bezeichnung 2"
							v-model="schuleModel.proxy.bezeichnung2"
							:validation="() => schuleModel.getFehler('bezeichnung2')"
							@change="schuleModel.patch"
							:readonly="!hatKompetenzUpdate" :max-len="50" />
						<svws-ui-text-input placeholder="Schulform"
							:model-value="textSchulform"
							readonly />
						<svws-ui-text-input placeholder="Bezeichnung 3"
							v-model="schuleModel.proxy.bezeichnung3"
							:validation="() => schuleModel.getFehler('bezeichnung3')"
							@change="schuleModel.patch"
							:readonly="!hatKompetenzUpdate" :max-len="50" />
						<svws-ui-spacing />
					</svws-ui-input-wrapper>
				</svws-ui-content-card>
				<svws-ui-spacing :size="2" />
				<svws-ui-content-card title="Kontaktinformationen">
					<svws-ui-input-wrapper :grid="2">
						<svws-ui-text-input placeholder="Straße" class="contentFocusField" span="full"
							v-model="schuleModel.strasse.value"
							:validation="() => schuleModel.getFehler('strassenname')"
							@change="schuleModel.patch"
							:readonly="!hatKompetenzUpdate" :max-len="55" />
						<svws-ui-text-input placeholder="PLZ"
							v-model="schuleModel.proxy.plz"
							:validation="() => schuleModel.getFehler('plz')"
							@change="schuleModel.patch"
							:readonly="!hatKompetenzUpdate" :max-len="10" />
						<svws-ui-text-input placeholder="Ort"
							v-model="schuleModel.proxy.ort"
							:validation="() => schuleModel.getFehler('ort')"
							@change="schuleModel.patch"
							:readonly="!hatKompetenzUpdate" :max-len="50" />
						<svws-ui-text-input placeholder="Telefon" type="tel"
							v-model="schuleModel.proxy.telefon"
							:validation="() => schuleModel.getFehler('telefon')"
							@change="schuleModel.patch"
							:readonly="!hatKompetenzUpdate" :max-len="20" />
						<svws-ui-text-input placeholder="Fax" type="tel"
							v-model="schuleModel.proxy.fax"
							:validation="() => schuleModel.getFehler('fax')"
							@change="schuleModel.patch"
							:readonly="!hatKompetenzUpdate" :max-len="20" />
						<svws-ui-text-input placeholder="Homepage"
							v-model="schuleModel.proxy.webAdresse"
							:validation="() => schuleModel.getFehler('webAdresse')"
							@change="schuleModel.patch"
							:readonly="!hatKompetenzUpdate" :max-len="100" />
						<svws-ui-text-input placeholder="E-Mail-Adresse" type="email"
							v-model="schuleModel.proxy.email"
							:validation="() => schuleModel.getFehler('email')"
							@change="schuleModel.patch"
							:readonly="!hatKompetenzUpdate" :max-len="100" />
					</svws-ui-input-wrapper>
				</svws-ui-content-card>
				<svws-ui-spacing :size="2" />
				<svws-ui-content-card title="Teilstandorte">
					<schule-teilstandorte-table :readonly="!hatKompetenzUpdate"
						:get-list-teilstandorte
						@open-patch-modal="editTeilstandort"
						@delete-teilstandorte="deleteTeilstandorte"
						@add-modal="addTeilstandort" />
					<schule-teilstandort-modal v-if="teilstandortModel !== null"
						:show="showModalTeilstandort"
						:data="teilstandortModel"
						:readonly="!hatKompetenzUpdate"
						:is-patch="currentTeilstandortMode === Mode.PATCH"
						:get-list-teilstandorte
						@save="saveTeilstandort"
						@close-modal="closeModalTeilstandort" />
				</svws-ui-content-card>
				<svws-ui-spacing :size="2" />
			</svws-ui-input-wrapper>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref, shallowRef } from "vue";

	import { Teilstandort } from "@core/core/data/schule/Teilstandort";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { ArrayList } from "@core/java/util/ArrayList";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { useSchuleState } from "@ui/states/SchuleState";

	import { SchuleStammdatenModelProxy } from "~/components/schule/stammdaten/adressdaten/modelproxy/SchuleStammdatenModelProxy";
	import { TeilstandortModelProxy } from "~/components/schule/stammdaten/adressdaten/modelproxy/TeilstandortModelProxy";
	import type { SchuleAdressdatenProps } from "~/components/schule/stammdaten/adressdaten/SchuleAdressdatenProps";

	const props = defineProps<SchuleAdressdatenProps>();
	const benutzerState = useBenutzerState();
	const schuleState = useSchuleState();

	enum Mode { ADD, PATCH, DEFAULT }
	const currentTeilstandortMode = ref<Mode>(Mode.DEFAULT);
	const showModalTeilstandort = ref<boolean>(false);

	const schuleModel = new SchuleStammdatenModelProxy(() => schuleState.stammdaten, props.patch);
	const teilstandortModel = shallowRef<TeilstandortModelProxy | null>(null);
	const hatKompetenzUpdate = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN));

	function enterDefaultMode() {
		setTeilstandortMode(Mode.DEFAULT);
		closeModalTeilstandort();
	}

	function addTeilstandort() {
		setDefaultCreateModel();
		setTeilstandortMode(Mode.ADD);
		openModalTeilstandort();
	}

	async function saveTeilstandort() {
		if (teilstandortModel.value === null) {
			return;
		}
		const { referenziertInAnderenTabellen, ...addData } = teilstandortModel.value.proxy;
		if (currentTeilstandortMode.value === Mode.ADD) {
			await props.addTeilstandorteintrag(addData);
		} else if (currentTeilstandortMode.value === Mode.PATCH) {
			await teilstandortModel.value.patch();
		}

		enterDefaultMode();
	}

	function editTeilstandort(teilstandort: Teilstandort) {
		teilstandortModel.value = new TeilstandortModelProxy(() => teilstandort, (data) => props.patchTeilstandorteintrag(data, teilstandort.adrMerkmal ?? "-"));
		setTeilstandortMode(Mode.PATCH);
		openModalTeilstandort();
	}

	async function deleteTeilstandorte(teilstandorte: Teilstandort[]) {
		if (teilstandorte.length === 0) {
			return;
		}

		const adrMerkmale = new ArrayList<string>();
		for (const teilstandort of teilstandorte) {
			adrMerkmale.add(teilstandort.adrMerkmal);
		}

		await props.deleteTeilstandorteintraege(adrMerkmale);
	}

	function openModalTeilstandort() {
		showModalTeilstandort.value = true;
	}

	function closeModalTeilstandort() {
		teilstandortModel.value = null;
		setTeilstandortMode(Mode.DEFAULT);
		showModalTeilstandort.value = false;
	}

	function setTeilstandortMode(newMode: Mode) {
		currentTeilstandortMode.value = newMode;
	}

	function setDefaultCreateModel(): void {
		const defaultTeilstandort = new Teilstandort();
		teilstandortModel.value = new TeilstandortModelProxy(() => defaultTeilstandort);
	}

	const textSchulform = computed<string>(() => {
		return schuleState.schulform.daten(schuleState.abschnitt.schuljahr)?.text ?? "—";
	});

</script>
