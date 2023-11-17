<template>
	<Teleport to=".svws-ui-header--actions" v-if="isMounted">
		<svws-ui-modal-hilfe> <s-gost-klausurplanung-vorgaben-hilfe /> </svws-ui-modal-hilfe>
	</Teleport>
	<Teleport to=".router-tab-bar--subnav" v-if="isMounted">
		<s-gost-klausurplanung-quartal-auswahl :quartalsauswahl="quartalsauswahl" :halbjahr="halbjahr" />
	</Teleport>
	<div class="page--content page--content--full">
		<svws-ui-content-card title="Klausurvorgaben">
			<svws-ui-table id="vorgabenTable" :items="vorgaben()" :columns="cols" v-model:clicked="selectedVorgabeRow" clickable @click="startEdit">
				<template #cell(idFach)="{ value }">
					<span class="svws-ui-badge" :style="{ '--background-color': getBgColor(faecherManager.get(value)?.kuerzel || null) }">{{ faecherManager.get(value)?.bezeichnung }}</span>
				</template>
				<template #cell(quartal)="{value}">
					{{ value }}.
				</template>
				<template #cell(dauer)="{ value }">
					<span :title="`${Math.floor(value / 60)}:${value % 60 < 10 ? '0' : ''}${value % 60} h`">{{ value }}</span>
					<span class="hidden">({{ Math.floor(value / 60) }}:{{ value % 60 < 10 ? '0' : '' }}{{ value % 60 }})</span>
				</template>
				<template #cell(auswahlzeit)="{ value }">
					<span :class="{'opacity-25': !value}">{{ value }}</span>
				</template>
				<template #cell(istMdlPruefung)="{ value }">
					<i-ri-chat1-line v-if="value" class="-my-0.5" />
					<span v-else class="opacity-25">—</span>
				</template>
				<template #cell(istAudioNotwendig)="{ value }">
					<i-ri-headphone-line v-if="value" class="-my-0.5" />
					<span v-else class="opacity-25">—</span>
				</template>
				<template #cell(istVideoNotwendig)="{ value }">
					<i-ri-vidicon-line v-if="value" class="-my-0.5" />
					<span v-else class="opacity-25">—</span>
				</template>
				<template #cell(bemerkungVorgabe)="{ value }">
					<span v-if="value !== null && value.trim().length > 0" class="line-clamp-1 leading-tight -my-0.5">{{ value }}</span>
				</template>
				<template #actions>
					<svws-ui-button type="transparent" @click="erzeugeVorgabenAusVorlage(quartalsauswahl.value)" v-if="jahrgangsdaten?.abiturjahr !== -1"><i-ri-upload-2-line />Aus Vorlage importieren</svws-ui-button>
					<svws-ui-button type="transparent" @click="erzeugeDefaultKlausurvorgaben(quartalsauswahl.value)" v-else><i-ri-upload-2-line />Standard-Vorlagen anlegen</svws-ui-button>
					<svws-ui-button type="icon" @click="neueVorgabe" :disabled="selectedVorgabeRow !== undefined" title="Neue Vorgabe erstellen"><i-ri-add-line /></svws-ui-button>
				</template>
			</svws-ui-table>
		</svws-ui-content-card>
		<svws-ui-content-card id="vorgabenEdit" :title="activeVorgabe.idVorgabe === 0 ? 'Neue Vorgabe erstellen' : (activeVorgabe.idVorgabe > 0 ? 'Vorgabe bearbeiten' : 'Bearbeiten')" class="sticky top-0 -ml-2">
			<template #actions v-if="activeVorgabe.idVorgabe > 0">
				<svws-ui-button type="danger" @click="loescheKlausurvorgabe" :disabled="activeVorgabe.idVorgabe < 0 || activeVorgabe.idFach === -1 || activeVorgabe.kursart === '' || activeVorgabe.quartal === -1 || (kursklausurmanager !== undefined && kursklausurmanager().istVorgabeVerwendetByVorgabe(activeVorgabe))"><i-ri-delete-bin-line />Löschen</svws-ui-button>
			</template>
			<template v-if="activeVorgabe.idVorgabe < 0">
				<span class="opacity-50">Zum Bearbeiten eine Vorgabe in der Tabelle auswählen oder mit <i-ri-add-line class="inline-block text-button -mt-1" /> eine neue erstellen.</span>
			</template>
			<template v-else>
				<div class="flex flex-col gap-4">
					<svws-ui-input-wrapper>
						<svws-ui-select :items="faecherSortiert" :item-text="(fach : GostFach) => fach.bezeichnung || ''" v-model="inputVorgabeFach" title="Fach" :disabled="activeVorgabe.idVorgabe !== 0" />
						<svws-ui-radio-group id="rbgKursart" :row="true">
							<svws-ui-radio-option v-for="kursart in formKursarten" v-model="activeVorgabe.kursart" :key="kursart" :value="kursart" name="formKursarten" :label="kursart" :disabled="activeVorgabe.idVorgabe !== 0" />
						</svws-ui-radio-group>
						<svws-ui-radio-group id="rbgQuartal" :row="true">
							<svws-ui-radio-option v-for="quartal in formQuartale" :key="quartal" :value="quartal+''" name="formQuartale" :label="quartal+'. Quartal'" :model-value="activeVorgabe.quartal+''" @click="activeVorgabe.quartal = quartal" :disabled="activeVorgabe.idVorgabe !== 0" />
						</svws-ui-radio-group>
						<svws-ui-spacing />
						<svws-ui-text-input placeholder="Dauer (Minuten)" type="number" :model-value="activeVorgabe.dauer" @change="dauer => activeVorgabe.idVorgabe !== 0 ? patchKlausurvorgabe({dauer: parseInt(dauer)}, activeVorgabe.idVorgabe) : activeVorgabe.dauer = parseInt(dauer)" :disabled="activeVorgabe.idVorgabe < 0" />
						<svws-ui-text-input placeholder="Auswahlzeit (Minuten)" type="number" :model-value="activeVorgabe.auswahlzeit" @change="auswahlzeit => activeVorgabe.idVorgabe !== 0 ? patchKlausurvorgabe({auswahlzeit: parseInt(auswahlzeit)}, activeVorgabe.idVorgabe) : activeVorgabe.auswahlzeit = parseInt(auswahlzeit)" :disabled="activeVorgabe.idVorgabe < 0" />
						<svws-ui-spacing />
						<div>
							<label class="sr-only" for="rbgMdlPruefung">Mündliche Prüfung: </label>
							<svws-ui-radio-group id="rbgMdlPruefung" :row="true">
								<svws-ui-radio-option v-for="value in formJaNein" :class="value.name === 'Ja' ? 'order-1' : 'order-0'" :key="value.name" :value="value.name" name="formMdlPruefung" :label="value.name === 'Ja' ? 'Mündliche Prüfung' : 'Schriftlich'" :model-value="activeVorgabe.istMdlPruefung ? 'Ja' : 'Nein'" @click="activeVorgabe.idVorgabe !== 0 ? patchKlausurvorgabe({istMdlPruefung: value.key}, activeVorgabe.idVorgabe) : activeVorgabe.istMdlPruefung = value.key" :disabled="activeVorgabe.idVorgabe < 0">
									<i-ri-chat1-line v-if="value.name === 'Ja'" />
									<template v-else>
										<i-ri-checkbox-blank-circle-line class="radio--indicator-icon--blank" />
										<i-ri-checkbox-circle-line class="radio--indicator-icon--checked" />
									</template>
								</svws-ui-radio-option>
							</svws-ui-radio-group>
						</div>
						<div>
							<label class="sr-only" for="rbgAudioNotwendig">Audio notwendig: </label>
							<svws-ui-radio-group id="rbgAudioNotwendig" :row="true">
								<svws-ui-radio-option v-for="value in formJaNein" :class="value.name === 'Ja' ? 'order-1' : 'order-0'" :key="value.name" :value="value.name" name="formAudioNotwendig" :label="value.name === 'Ja' ? 'Mit Audioteil' : 'Ohne Audio'" :model-value="activeVorgabe.istAudioNotwendig ? 'Ja' : 'Nein'" @click="activeVorgabe.idVorgabe !== 0 ? patchKlausurvorgabe({istAudioNotwendig: value.key}, activeVorgabe.idVorgabe) : activeVorgabe.istAudioNotwendig = value.key" :disabled="activeVorgabe.idVorgabe < 0">
									<i-ri-headphone-line v-if="value.name === 'Ja'" />
									<template v-else>
										<i-ri-checkbox-blank-circle-line class="radio--indicator-icon--blank" />
										<i-ri-checkbox-circle-line class="radio--indicator-icon--checked" />
									</template>
								</svws-ui-radio-option>
							</svws-ui-radio-group>
						</div>
						<div>
							<label class="sr-only" for="rbgVideoNotwendig">Video notwendig: </label>
							<svws-ui-radio-group id="rbgVideoNotwendig" :row="true">
								<svws-ui-radio-option v-for="value in formJaNein" :class="value.name === 'Ja' ? 'order-1' : 'order-0'" :key="value.name" :value="value.name" name="formVideoNotwendig" :label="value.name === 'Ja' ? 'Mit Videoteil' : 'Ohne Video'" :model-value="activeVorgabe.istVideoNotwendig ? 'Ja' : 'Nein'" @click="activeVorgabe.idVorgabe !== 0 ? patchKlausurvorgabe({istVideoNotwendig: value.key}, activeVorgabe.idVorgabe) : activeVorgabe.istVideoNotwendig = value.key" :disabled="activeVorgabe.idVorgabe < 0">
									<i-ri-vidicon-line v-if="value.name === 'Ja'" />
									<template v-else>
										<i-ri-checkbox-blank-circle-line class="radio--indicator-icon--blank" />
										<i-ri-checkbox-circle-line class="radio--indicator-icon--checked" />
									</template>
								</svws-ui-radio-option>
							</svws-ui-radio-group>
						</div>
						<svws-ui-spacing />
						<svws-ui-textarea-input placeholder="Bemerkungen" :model-value="activeVorgabe.bemerkungVorgabe" @change="bemerkungVorgabe => activeVorgabe.idVorgabe !== 0 ? patchKlausurvorgabe({bemerkungVorgabe}, activeVorgabe.idVorgabe) : activeVorgabe.bemerkungVorgabe = bemerkungVorgabe" resizeable="vertical" :disabled="activeVorgabe.idVorgabe < 0" />
					</svws-ui-input-wrapper>
				</div>
				<div v-if="activeVorgabe.idVorgabe === 0" class="flex gap-1 flex-wrap justify-start mt-9">
					<div v-if="activeVorgabe.idFach === -1 || activeVorgabe.kursart === '' || activeVorgabe.quartal === -1" class="mb-3 leading-tight opacity-50"><i-ri-information-line class="inline align-text-top mr-0.5" />Um die Vorgabe zu speichern, müssen Fach, Kursart und Quartal ausgewählt werden.</div>
					<svws-ui-button type="secondary" @click="cancelEdit">Abbrechen</svws-ui-button>
					<svws-ui-button @click="saveKlausurvorgabe" :disabled="activeVorgabe.idFach === -1 || activeVorgabe.kursart === '' || activeVorgabe.quartal === -1">Speichern</svws-ui-button>
				</div>
			</template>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { GostFach } from "@core";
	import type { Ref , WritableComputedRef } from 'vue'
	import {ArrayList, GostKlausurvorgabe, ZulaessigesFach} from "@core";
	import { computed, ref, onMounted } from 'vue';
	import type { GostKlausurplanungVorgabenProps } from "./SGostKlausurplanungVorgabenProps";

	const props = defineProps<GostKlausurplanungVorgabenProps>();

	const vorgaben = () => props.klausurvorgabenmanager().vorgabeGetMengeByQuartal(props.halbjahr, props.quartalsauswahl.value);

	const selectedVorgabeRow = ref<GostKlausurvorgabe>();
	const activeVorgabe: Ref<GostKlausurvorgabe> = ref(new GostKlausurvorgabe());

	const formKursarten = computed(() => ["GK", "LK"]);
	const formJaNein = computed(() => [{key: true, name: "Ja"}, {key: false, name: "Nein"}]);
	const formQuartale = computed(() => [1, 2]);
	const inputVorgabeFach: WritableComputedRef<GostFach | undefined> = computed({
		get() {
			const fach = props.faecherManager.get(activeVorgabe.value.idFach);
			return fach === null ? undefined : fach;
		},
		set(val) {
			if (val !== undefined)
				activeVorgabe.value.idFach = val.id;
		}
	});

	const faecherSortiert = computed(() => {
		const f = new ArrayList(props.faecherManager.faecher());
		//		f.sort({ compare: (a: GostFach, b: GostFach) => a.bezeichnung.localeCompare(b.bezeichnung) });
		return f;
	});

	const neueVorgabe = () => {
		activeVorgabe.value.idVorgabe = 0;
	};

	const saveKlausurvorgabe = async () => {
		if (activeVorgabe.value.idFach === -1 || activeVorgabe.value.kursart === "" || activeVorgabe.value.quartal === -1) {
			console.log("Eingabefehler");
			return;
		}
		if (activeVorgabe.value.idVorgabe === 0) {
			try {
				await props.erzeugeKlausurvorgabe(activeVorgabe.value);
				activeVorgabe.value = new GostKlausurvorgabe();
			} catch (error) {
				console.log("Vorgabe konnte nicht erzeugt werden, wahrscheinlich existiert sie schon.", activeVorgabe.value); // TODO
			}
		}
	};

	const loescheKlausurvorgabe = async () => {
		await props.loescheKlausurvorgabe(activeVorgabe.value.idVorgabe);
		selectedVorgabeRow.value = undefined;
		activeVorgabe.value = new GostKlausurvorgabe();
	};

	const cancelEdit = () => {
		selectedVorgabeRow.value = undefined;
		activeVorgabe.value = new GostKlausurvorgabe();
	};

	const startEdit = () => {
		if (selectedVorgabeRow.value !== undefined) {
			const v = props.klausurvorgabenmanager().vorgabeGetByIdOrException(selectedVorgabeRow.value.idVorgabe);
			if (activeVorgabe.value.idVorgabe === v.idVorgabe) {
				cancelEdit();
			} else {
				activeVorgabe.value = v !== null ? v : new GostKlausurvorgabe();
			}
		}
	};

	// Check if component is mounted
	const isMounted = ref(false);
	onMounted(() => {
		isMounted.value = true;
	});

	const cols = [
		{key: 'idFach', label: 'Fach', span: 1.25, sortable: true},
		{key: 'kursart', label: 'Kursart', span: 0.5, sortable: true},
		{key: 'quartal', label: 'Quartal', span: 0.5, sortable: true},
		{key: 'dauer', label: 'Dauer', tooltip: 'Dauer in Minuten', span: 0.5, sortable: true},
		{key: 'auswahlzeit', label: 'Auswahlzeit', tooltip: 'Auswahlzeit in Minuten', span: 0.5, sortable: false},
		{key: 'istMdlPruefung', label: 'M', align: "center", tooltip: 'Mündliche Prüfung', fixedWidth: 2.5},
		{key: 'istAudioNotwendig', label: 'A', align: "center", tooltip: 'Mit Audioteil', fixedWidth: 2.5},
		{key: 'istVideoNotwendig', label: 'V', align: "center", tooltip: 'Mit Videoteil', fixedWidth: 2.5},
		{key: 'bemerkungVorgabe', label: 'Bemerkung', span: 1.25}
	];

	const getBgColor = (kuerzel: string | null) => ZulaessigesFach.getByKuerzelASD(kuerzel).getHMTLFarbeRGBA(1.0);

	window.addEventListener('click', function(e) {
		const vT = document.getElementById('vorgabenTable');
		const vE = document.getElementById('vorgabenEdit');
		const vL = document.getElementById('svws-ui-dropdown-list-id');
		if (vE !== null && vT !== null && vL === null)
			activeVorgabe.value = new GostKlausurvorgabe();
	});

</script>

<style lang="postcss" scoped>
.page--content {
  @apply grid;
  grid-template-columns: 1fr minmax(20rem, 0.25fr);
}
</style>
