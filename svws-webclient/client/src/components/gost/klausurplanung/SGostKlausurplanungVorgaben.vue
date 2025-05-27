<template>
	<Teleport to=".svws-ui-header--actions" v-if="isMounted">
		<svws-ui-modal-hilfe> <s-gost-klausurplanung-vorgaben-hilfe /> </svws-ui-modal-hilfe>
	</Teleport>
	<Teleport to=".router-tab-bar--subnav" v-if="isMounted">
		<s-gost-klausurplanung-quartal-auswahl :quartalsauswahl="quartalsauswahl" :halbjahr="halbjahr" />
	</Teleport>
	<div class="page page-flex-row">
		<div class="grow min-w-fit flex flex-col gap-4 overflow-y-hidden">
			<div class="text-headline-md">Klausurvorgaben</div>
			<svws-ui-table scroll id="vorgabenTable" :items="vorgaben()" :columns="cols" v-model:clicked="selectedVorgabeRow" :clickable="hatKompetenzUpdate" @click="startEdit" :no-data="vorgaben().isEmpty()"
				:no-data-text="'Keine ' + (jahrgangsdaten?.abiturjahr === -1 ? 'Vorlagen für ' : '') + 'Klausurvorgaben für das ' + (quartalsauswahl.value !== 0 ? quartalsauswahl.value + '. Quartal im' : '') + ' Halbjahr ' + halbjahr.kuerzel + ' vorhanden.'">
				<template #cell(idFach)="{ value }">
					<span class="svws-ui-badge" :style="`color: var(--color-text-uistatic); background-color: ${getBgColor(kMan().getFaecherManager(jahrgangsdaten!.abiturjahr).get(value)?.kuerzel || null)}`">{{ kMan().getFaecherManager(jahrgangsdaten!.abiturjahr).get(value)?.bezeichnung }}</span>
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
					<span class="icon i-ri-chat-1-line -my-0.5" v-if="value" />
					<span v-else class="opacity-25">—</span>
				</template>
				<template #cell(istAudioNotwendig)="{ value }">
					<span class="icon i-ri-headphone-line -my-0.5" v-if="value" />
					<span v-else class="opacity-25">—</span>
				</template>
				<template #cell(istVideoNotwendig)="{ value }">
					<span class="icon i-ri-vidicon-line -my-0.5" v-if="value" />
					<span v-else class="opacity-25">—</span>
				</template>
				<template #cell(bemerkungVorgabe)="{ value }">
					<span v-if="value !== null && value.trim().length > 0" class="line-clamp-1 leading-tight -my-0.5">{{ value }}</span>
				</template>
				<template #actions>
					<svws-ui-button type="transparent" :disabled="!hatKompetenzUpdate" @click="erzeugeVorgabenAusVorlage(quartalsauswahl.value)" v-if="jahrgangsdaten?.abiturjahr !== -1"><span class="icon i-ri-upload-2-line" />Aus Vorlage importieren</svws-ui-button>
					<svws-ui-button type="transparent" :disabled="!hatKompetenzUpdate" @click="erzeugeDefaultKlausurvorgaben(quartalsauswahl.value)" v-else><span class="icon i-ri-upload-2-line" />Standard-Vorlagen anlegen</svws-ui-button>
					<svws-ui-button type="icon" @click="neueVorgabe" :disabled="!hatKompetenzUpdate || selectedVorgabeRow !== undefined" title="Neue Vorgabe erstellen"><span class="icon i-ri-add-line" /></svws-ui-button>
				</template>
			</svws-ui-table>
		</div>
		<div v-if="hatKompetenzUpdate" class="min-w-100 max-w-100 flex flex-col gap-8" id="vorgabenEdit">
			<div class="flex flex-row justify-between">
				<span class="text-headline-md">{{ activeVorgabe.id === 0 ? 'Neue Vorgabe erstellen' : (activeVorgabe.id > 0 ? 'Vorgabe bearbeiten' : 'Bearbeiten') }}</span>
				<template v-if="activeVorgabe.id > 0">
					<svws-ui-button type="danger" @click="loescheKlausurvorgabe" :disabled="activeVorgabe.id < 0 || activeVorgabe.idFach === -1 || activeVorgabe.kursart === '' || activeVorgabe.quartal === -1 || (kMan().istVorgabeVerwendetByKursklausur(activeVorgabe))"><span class="icon i-ri-delete-bin-line" />Löschen</svws-ui-button>
				</template>
			</div>
			<template v-if="activeVorgabe.id < 0">
				<span class="opacity-50">Zum Bearbeiten eine Vorgabe in der Tabelle auswählen oder mit <span class="icon i-ri-add-line text-button -my-0.5" /> eine neue erstellen.</span>
			</template>
			<template v-else>
				<div class="flex flex-col gap-4">
					<svws-ui-input-wrapper>
						<svws-ui-select :items="faecherSortiert" :item-text="(fach : GostFach) => fach.bezeichnung || ''" v-model="inputVorgabeFach" title="Fach" :disabled="activeVorgabe.id !== 0" />
						<svws-ui-radio-group id="rbgKursart" :row="true">
							<svws-ui-radio-option v-for="kursart in formKursarten" v-model="activeVorgabe.kursart" :key="kursart" :value="kursart" name="formKursarten" :label="kursart" :disabled="activeVorgabe.id !== 0" />
						</svws-ui-radio-group>
						<svws-ui-radio-group id="rbgQuartal" :row="true">
							<svws-ui-radio-option v-for="quartal in formQuartale" :key="quartal" :value="quartal+''" name="formQuartale" :label="quartal+'. Quartal'" :model-value="activeVorgabe.quartal+''" @click="activeVorgabe.quartal = quartal" :disabled="activeVorgabe.id !== 0" />
						</svws-ui-radio-group>
						<svws-ui-spacing />
						<svws-ui-input-number placeholder="Dauer (Minuten)" :model-value="activeVorgabe.dauer" @change="dauer => activeVorgabe.id !== 0 ? patchKlausurvorgabe({dauer: dauer!}, activeVorgabe.id) : activeVorgabe.dauer = dauer!" :disabled="activeVorgabe.id < 0" />
						<svws-ui-input-number placeholder="Auswahlzeit (Minuten)" type="number" :model-value="activeVorgabe.auswahlzeit" @change="auswahlzeit => activeVorgabe.id !== 0 ? patchKlausurvorgabe({auswahlzeit: auswahlzeit!}, activeVorgabe.id) : activeVorgabe.auswahlzeit = auswahlzeit!" :disabled="activeVorgabe.id < 0" />
						<svws-ui-spacing />
						<div>
							<label class="sr-only" for="rbgMdlPruefung">Mündliche Prüfung: </label>
							<svws-ui-radio-group id="rbgMdlPruefung" :row="true">
								<svws-ui-radio-option v-for="value in formJaNein" :class="value.name === 'Ja' ? 'order-1' : 'order-0'" :key="value.name" :value="value.name" name="formMdlPruefung" :label="value.name === 'Ja' ? 'Mündliche Prüfung' : 'Schriftlich'" :model-value="activeVorgabe.istMdlPruefung ? 'Ja' : 'Nein'" @click="activeVorgabe.id !== 0 ? patchKlausurvorgabe({istMdlPruefung: value.key}, activeVorgabe.id) : activeVorgabe.istMdlPruefung = value.key" :disabled="activeVorgabe.id < 0">
									<span class="icon i-ri-chat-1-line -my-1 -mx-0.5" v-if="value.name === 'Ja'" />
								</svws-ui-radio-option>
							</svws-ui-radio-group>
						</div>
						<div>
							<label class="sr-only" for="rbgAudioNotwendig">Audio notwendig: </label>
							<svws-ui-radio-group id="rbgAudioNotwendig" :row="true">
								<svws-ui-radio-option v-for="value in formJaNein" :class="value.name === 'Ja' ? 'order-1' : 'order-0'" :key="value.name" :value="value.name" name="formAudioNotwendig" :label="value.name === 'Ja' ? 'Mit Audioteil' : 'Ohne Audio'" :model-value="activeVorgabe.istAudioNotwendig ? 'Ja' : 'Nein'" @click="activeVorgabe.id !== 0 ? patchKlausurvorgabe({istAudioNotwendig: value.key}, activeVorgabe.id) : activeVorgabe.istAudioNotwendig = value.key" :disabled="activeVorgabe.id < 0">
									<span class="icon i-ri-headphone-line -my-1 -mx-0.5" v-if="value.name === 'Ja'" />
								</svws-ui-radio-option>
							</svws-ui-radio-group>
						</div>
						<div>
							<label class="sr-only" for="rbgVideoNotwendig">Video notwendig: </label>
							<svws-ui-radio-group id="rbgVideoNotwendig" :row="true">
								<svws-ui-radio-option v-for="value in formJaNein" :class="value.name === 'Ja' ? 'order-1' : 'order-0'" :key="value.name" :value="value.name" name="formVideoNotwendig" :label="value.name === 'Ja' ? 'Mit Videoteil' : 'Ohne Video'" :model-value="activeVorgabe.istVideoNotwendig ? 'Ja' : 'Nein'" @click="activeVorgabe.id !== 0 ? patchKlausurvorgabe({istVideoNotwendig: value.key}, activeVorgabe.id) : activeVorgabe.istVideoNotwendig = value.key" :disabled="activeVorgabe.id < 0">
									<span class="icon i-ri-vidicon-line -my-1 -mx-0.5" v-if="value.name === 'Ja'" />
								</svws-ui-radio-option>
							</svws-ui-radio-group>
						</div>
						<svws-ui-spacing />
						<svws-ui-textarea-input placeholder="Bemerkungen" :model-value="activeVorgabe.bemerkungVorgabe" @change="bemerkungVorgabe => activeVorgabe.id !== 0 ? patchKlausurvorgabe({bemerkungVorgabe}, activeVorgabe.id) : activeVorgabe.bemerkungVorgabe = bemerkungVorgabe" resizeable="vertical" :disabled="activeVorgabe.id < 0" />
					</svws-ui-input-wrapper>
				</div>
				<div v-if="activeVorgabe.id === 0" class="flex gap-1 flex-wrap justify-start mt-9">
					<div v-if="activeVorgabe.idFach === -1 || activeVorgabe.kursart === '' || activeVorgabe.quartal === -1" class="mb-3 leading-tight opacity-50"><span class="icon i-ri-information-line inline align-text-top mr-0.5" />Um die Vorgabe zu speichern, müssen Fach, Kursart und Quartal ausgewählt werden.</div>
					<svws-ui-button type="secondary" @click="cancelEdit">Abbrechen</svws-ui-button>
					<svws-ui-button @click="saveKlausurvorgabe" :disabled="activeVorgabe.idFach === -1 || activeVorgabe.kursart === '' || activeVorgabe.quartal === -1">Speichern</svws-ui-button>
				</div>
			</template>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { Ref , WritableComputedRef } from 'vue'
	import { computed, ref, onMounted, onUnmounted } from 'vue';
	import type { DataTableColumn } from "@ui";
	import type { GostFach } from "@core";
	import { BenutzerKompetenz, ArrayList, GostKlausurvorgabe, Fach } from "@core";
	import type { GostKlausurplanungVorgabenProps } from "./SGostKlausurplanungVorgabenProps";

	const props = defineProps<GostKlausurplanungVorgabenProps>();

	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN));

	const vorgaben = () => props.kMan().vorgabeGetMengeByHalbjahrAndQuartal(props.jahrgangsdaten === undefined ? -1 : props.jahrgangsdaten.abiturjahr, props.halbjahr, props.quartalsauswahl.value);

	const selectedVorgabeRow = ref<GostKlausurvorgabe>();
	const activeVorgabe: Ref<GostKlausurvorgabe> = ref(new GostKlausurvorgabe());

	const formKursarten = computed(() => ["GK", "LK"]);
	const formJaNein = computed(() => [{key: true, name: "Ja"}, {key: false, name: "Nein"}]);
	const formQuartale = computed(() => [1, 2]);
	const inputVorgabeFach: WritableComputedRef<GostFach | undefined> = computed({
		get() {
			const fach = props.kMan().getFaecherManager(props.jahrgangsdaten!.abiturjahr).get(activeVorgabe.value.idFach);
			return fach === null ? undefined : fach;
		},
		set(val) {
			if (val !== undefined)
				activeVorgabe.value.idFach = val.id;
		},
	});

	const faecherSortiert = computed(() => {
		const f = new ArrayList(props.kMan().getFaecherManager(props.jahrgangsdaten!.abiturjahr).faecher());
		//		f.sort({ compare: (a: GostFach, b: GostFach) => a.bezeichnung.localeCompare(b.bezeichnung) });
		return f;
	});

	const neueVorgabe = () => {
		activeVorgabe.value.id = 0;
	};

	const saveKlausurvorgabe = async () => {
		if (activeVorgabe.value.idFach === -1 || activeVorgabe.value.kursart === "" || activeVorgabe.value.quartal === -1) {
			console.log("Eingabefehler");
			return;
		}
		if (activeVorgabe.value.id === 0) {
			try {
				await props.erzeugeKlausurvorgabe(activeVorgabe.value);
				activeVorgabe.value = new GostKlausurvorgabe();
			} catch (error) {
				console.log("Vorgabe konnte nicht erzeugt werden, wahrscheinlich existiert sie schon.", activeVorgabe.value); // TODO
			}
		}
	};

	const loescheKlausurvorgabe = async () => {
		await props.loescheKlausurvorgabe(activeVorgabe.value.id);
		selectedVorgabeRow.value = undefined;
		activeVorgabe.value = new GostKlausurvorgabe();
	};

	const cancelEdit = () => {
		selectedVorgabeRow.value = undefined;
		activeVorgabe.value = new GostKlausurvorgabe();
	};

	const startEdit = () => {
		if (selectedVorgabeRow.value !== undefined) {
			const v = props.kMan().vorgabeGetByIdOrException(selectedVorgabeRow.value.id);
			if (activeVorgabe.value.id === v.id) {
				cancelEdit();
			} else {
				activeVorgabe.value = v;
			}
		}
	};

	// Check if component is mounted
	const isMounted = ref(false);
	onMounted(() => {
		isMounted.value = true;
		window.addEventListener('click', handleClick);
	});

	onUnmounted(() => {
		window.removeEventListener('click', handleClick);
	});

	const cols: DataTableColumn[] = [
		{key: 'idFach', label: 'Fach', span: 1.25, sortable: true},
		{key: 'kursart', label: 'Kursart', span: 0.5, sortable: true},
		{key: 'quartal', label: 'Quartal', span: 0.5, sortable: true},
		{key: 'dauer', label: 'Dauer', tooltip: 'Dauer in Minuten', span: 0.5, sortable: true},
		{key: 'auswahlzeit', label: 'Auswahlzeit', tooltip: 'Auswahlzeit in Minuten', span: 0.5, sortable: false},
		{key: 'istMdlPruefung', label: 'M', align: "center", tooltip: 'Mündliche Prüfung', fixedWidth: 2.5},
		{key: 'istAudioNotwendig', label: 'A', align: "center", tooltip: 'Mit Audioteil', fixedWidth: 2.5},
		{key: 'istVideoNotwendig', label: 'V', align: "center", tooltip: 'Mit Videoteil', fixedWidth: 2.5},
		{key: 'bemerkungVorgabe', label: 'Bemerkung', span: 1.25},
	];

	function getBgColor(kuerzel: string | null) {
		if (kuerzel === null)
			return 'rgb(220,220,220)';
		return Fach.getBySchluesselOrDefault(kuerzel).getHMTLFarbeRGBA(props.jahrgangsdaten!.abiturjahr-1, 1.0);
	}

	function handleClick(e: MouseEvent) {
		const vT = document.getElementById('vorgabenTable');
		const vE = document.getElementById('vorgabenEdit');

		if (vE !== null && vT !== null &&
			!vT.contains(e.target as Node) &&
			!vE.contains(e.target as Node) &&
			!(e.target as HTMLElement).parentElement?.parentElement?.classList.contains("svws-ui-dropdown-list") &&
			!(e.target as HTMLElement).parentElement?.parentElement?.parentElement?.classList.contains("svws-ui-dropdown-list")
		) {
			activeVorgabe.value = new GostKlausurvorgabe();
			selectedVorgabeRow.value = undefined;
		}
	}

</script>
