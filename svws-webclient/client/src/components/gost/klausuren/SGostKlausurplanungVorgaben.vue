<template>
	<Teleport to=".svws-ui-header--actions" v-if="isMounted">
		<svws-ui-modal-hilfe> <s-gost-klausurplanung-vorgaben-hilfe /> </svws-ui-modal-hilfe>
	</Teleport>
	<Teleport to=".router-tab-bar--subnav" v-if="isMounted">
		<s-gost-klausurplanung-quartal-auswahl />
	</Teleport>
	<div class="page page-flex-row">
		<div class="grow min-w-fit max-w-350 flex flex-col gap-4 overflow-y-hidden">
			<div class="text-headline-md">Klausurvorgaben</div>
			<svws-ui-table scroll id="vorgabenTable" :items="vorgaben()" :columns="cols" v-model:clicked="selectedVorgabeRow" :clickable="hatKompetenzUpdate" @click="startEdit" selectable :lock-selectable="!hatKompetenzUpdate"
				v-model="selected" :no-data="vorgaben().isEmpty()"
				:no-data-text="'Keine ' + (state.jahrgangsdaten?.abiturjahr === -1 ? 'Vorlagen für ' : '') + 'Klausurvorgaben für das ' + (state.quartal !== 0 ? state.quartal + '. Quartal im' : '') + ' Halbjahr ' + state.halbjahr.kuerzel + ' vorhanden.'">
				<template #cell(status)="{ rowData }">
					<svws-ui-tooltip v-if="state.manager.istVorgabeVerwendetByKursklausur(rowData)" position="top" :indicator="false">
						<span class="icon i-ri-lock-line -my-0.5" />
						<template #content>Vorgabe wird in geplanten Klausuren verwendet</template>
					</svws-ui-tooltip>
				</template>
				<template #cell(idFach)="{ rowData }">
					<span class="svws-ui-badge inline-flex items-center gap-1" :style="`color: var(--color-text-uistatic); background-color: ${getBgColor(state.manager.fachOrNullByVorgabe(rowData)?.kuerzel ?? null)}`">
						{{ fachBezeichnungByVorgabe(rowData) }}
						<svws-ui-tooltip v-if="state.manager.fachOrNullByVorgabe(rowData) === null" position="top" :indicator="false">
							<button type="button" class="inline-flex items-center justify-center" @click.stop="gotoFach(rowData.idFach)" title="Zum Fach">
								<span class="icon-sm icon-ui-warning i-ri-alert-line -my-0.5" />
							</button>
							<template #content>
								<span>Das Fach mit der ID {{ rowData.idFach }} ist nicht als Fach der Oberstufe definiert.</span>
								<button type="button" class="inline-flex items-center justify-center ml-1" @click.stop="gotoFach(rowData.idFach)" title="Zum Fach">
									<span class="icon i-ri-link" />
								</button>
							</template>
						</svws-ui-tooltip>
					</span>
				</template>
				<template #cell(quartal)="{value}">
					{{ value }}.
				</template>
				<template #cell(dauer)="{ value, rowData }">
					<span class="inline-flex items-center gap-1">
						<span :title="`${Math.floor(value / 60)}:${value % 60 < 10 ? '0' : ''}${value % 60} h`">{{ value }}</span>
						<svws-ui-tooltip v-if="dauerWarnung(rowData) !== null" position="top" :indicator="false">
							<span class="icon-sm icon-ui-warning i-ri-alert-line -my-0.5" />
							<template #content>{{ dauerWarnung(rowData) }}</template>
						</svws-ui-tooltip>
					</span>
					<span class="hidden">({{ Math.floor(value / 60) }}:{{ value % 60 < 10 ? '0' : '' }}{{ value % 60 }})</span>
				</template>
				<template #cell(auswahlzeit)="{ value }">
					<span :class="{'opacity-25': !value}">{{ value }}</span>
				</template>
				<template #cell(istGklMoeglich)="{ value, rowData }">
					<button v-if="vorgabeHatGklMoeglich(rowData)" type="button" class="inline-flex items-center justify-center" :title="value ? 'Gleichwertige komplexe Lernleistung möglich' : 'Keine Gleichwertige komplexe Lernleistung möglich'" :class="{'cursor-pointer hover:opacity-70': hatKompetenzUpdate}" @click.stop="toggleVorgabeBoolean(rowData, 'istGklMoeglich')" :disabled="!hatKompetenzUpdate">
						<span class="icon i-ri-presentation-line -my-0.5" :class="{'opacity-25': !value}" />
					</button>
				</template>
				<template #cell(istMdlPruefung)="{ value, rowData }">
					<button v-if="(state.jahrgangsdaten !== undefined) && (rowData.idFach >= 0) && (state.halbjahr.id !== GostHalbjahr.Q22.id) && vorgabeIstModerneFremdsprache(rowData)" type="button" class="inline-flex items-center justify-center" :title="value ? 'Mündliche Kommunikationsprüfung' : 'Normale Klausur'" :class="{'cursor-pointer hover:opacity-70': hatKompetenzUpdate}" @click.stop="toggleVorgabeBoolean(rowData, 'istMdlPruefung')" :disabled="!hatKompetenzUpdate">
						<span class="icon i-ri-chat-1-line -my-0.5" :class="{'opacity-25': !value}" />
					</button>
				</template>
				<template #cell(istAudioNotwendig)="{ value }">
					<span class="inline-flex items-center justify-center">
						<span class="icon i-ri-headphone-line -my-0.5" v-if="value" />
					</span>
				</template>
				<template #cell(istVideoNotwendig)="{ value }">
					<span class="inline-flex items-center justify-center">
						<span class="icon i-ri-vidicon-line -my-0.5" v-if="value" />
					</span>
				</template>
				<template #cell(bemerkungVorgabe)="{ value }">
					<span v-if="(value !== null) && (value.trim().length > 0)" class="line-clamp-1 leading-tight -my-0.5">{{ value }}</span>
				</template>
				<template #actions>
					<div class="flex w-full items-center gap-1">
						<div class="ml-auto flex items-center gap-1">
							<svws-ui-tooltip v-if="selectedHatDauerWarnung" position="top" :indicator="false">
								<svws-ui-button type="transparent" :disabled="!hatKompetenzUpdate" @click="setAusgewaehlteDauerAufApoVorgabe">
									<span class="icon i-ri-restart-line" />Dauer zurücksetzen
								</svws-ui-button>
								<template #content>Klausurdauer auf APO-GOSt-Vorgaben setzen</template>
							</svws-ui-tooltip>
							<svws-ui-button type="transparent" :disabled="!hatKompetenzUpdate" @click="state.erzeugeVorgabenAusVorlage(state.quartal)" v-if="state.jahrgangsdaten?.abiturjahr !== -1"><span class="icon i-ri-upload-2-line" />Aus Vorlage importieren</svws-ui-button>
							<svws-ui-button type="transparent" :disabled="!hatKompetenzUpdate" @click="state.erzeugeDefaultKlausurvorgaben(state.quartal)" v-else><span class="icon i-ri-upload-2-line" />Standard-Vorlagen anlegen</svws-ui-button>
							<svws-ui-tooltip v-if="selectedHatVerwendeteVorgaben" position="top" :indicator="false">
								<svws-ui-button type="trash" disabled />
								<template #content>Es sind Vorgaben selektiert, die Grundlage für geplante Klausuren sind.</template>
							</svws-ui-tooltip>
							<svws-ui-button v-else type="trash" :disabled="(selected.length === 0) || !hatKompetenzUpdate" @click="loescheVorgaben(selected)" />
							<svws-ui-button type="icon" @click="neueVorgabe" :disabled="!hatKompetenzUpdate || (selectedVorgabeRow !== undefined)" title="Neue Vorgabe erstellen"><span class="icon i-ri-add-line" /></svws-ui-button>
						</div>
					</div>
				</template>
			</svws-ui-table>
		</div>
		<div v-if="hatKompetenzUpdate" class="min-w-100 max-w-100 flex flex-col gap-8" id="vorgabenEdit">
			<template v-if="selected.length === 0">
				<div class="flex flex-row justify-between">
					<span class="text-headline-md">{{ activeVorgabe.id === 0 ? 'Neue Vorgabe erstellen' : (activeVorgabe.id > 0 ? 'Vorgabe bearbeiten' : 'Bearbeiten') }}</span>
					<template v-if="activeVorgabe.id > 0">
						<svws-ui-button type="trash" @click="loescheVorgaben([activeVorgabe])" :disabled="(activeVorgabe.id < 0) || (activeVorgabe.idFach === -1) || (activeVorgabe.kursart === '') || (activeVorgabe.quartal === -1) || (state.manager.istVorgabeVerwendetByKursklausur(activeVorgabe))" />
					</template>
				</div>
				<template v-if="activeVorgabe.id < 0">
					<span class="opacity-50">Zum Bearbeiten eine Vorgabe in der Tabelle auswählen oder mit <span class="icon i-ri-add-line text-button -my-0.5" /> eine neue erstellen.</span>
				</template>
				<template v-else>
					<div class="flex flex-col gap-4">
						<svws-ui-input-wrapper>
							<svws-ui-select :items="faecherSortiert" :item-text="(fach : GostFach) => fach.bezeichnung || ''" :model-value="activeVorgabe.idFach !== -1 ? state.manager.fachOrNullByVorgabe(activeVorgabe) ?? undefined : undefined" @update:model-value="fach => activeVorgabe.idFach = fach?.id ?? -1" title="Fach" :disabled="activeVorgabe.id !== 0" />
							<span v-if="(activeVorgabe.id > 0) && (state.manager.fachOrNullByVorgabe(activeVorgabe) === null)" class="text-ui-danger text-sm leading-tight">{{ fachFehltText(activeVorgabe) }}</span>
							<svws-ui-radio-group id="rbgKursart" :row="true">
								<svws-ui-radio-option v-for="kursart in formKursarten" v-model="activeVorgabe.kursart" :key="kursart" :value="kursart" name="formKursarten" :label="kursart" :disabled="activeVorgabe.id !== 0" />
							</svws-ui-radio-group>
							<svws-ui-radio-group id="rbgQuartal" :row="true">
								<svws-ui-radio-option v-for="quartal in formQuartale" :key="quartal" :value="quartal" name="formQuartale" :label="quartal+'. Quartal'" v-model="activeVorgabe.quartal" :disabled="activeVorgabe.id !== 0" />
							</svws-ui-radio-group>
							<svws-ui-spacing />
							<div class="flex items-start gap-1">
								<svws-ui-input-number class="flex-1" placeholder="Dauer (Minuten)" :model-value="activeVorgabe.dauer" @change="dauer => activeVorgabe.id !== 0 ? state.patchKlausurvorgabe({ dauer: dauer! }, activeVorgabe.id) : activeVorgabe.dauer = dauer!" :validation="validiereDauer" :disabled="activeVorgabe.id < 0" />
								<svws-ui-tooltip class="mt-3 shrink-0" position="bottom" :indicator="false">
									<svws-ui-button type="icon" @click="setDauerAufApoVorgabe" :disabled="activeVorgabe.id <= 0">
										<span class="icon i-ri-restart-line" />
									</svws-ui-button>
									<template #content>Auf Vorgaben in APO-GOSt zurücksetzen</template>
								</svws-ui-tooltip>
							</div>
							<svws-ui-input-number placeholder="Auswahlzeit (Minuten)" type="number" :model-value="activeVorgabe.auswahlzeit" @change="auswahlzeit => activeVorgabe.id !== 0 ? state.patchKlausurvorgabe({auswahlzeit: auswahlzeit!}, activeVorgabe.id) : activeVorgabe.auswahlzeit = auswahlzeit!" :disabled="activeVorgabe.id < 0" />
							<svws-ui-spacing />
							<div v-if="vorgabeHatGklMoeglich(activeVorgabe)">
								<label class="block font-bold mb-1" for="rbgGklMoeglich">Gleichwertiger komplexer Leistungsnachweis</label>
								<svws-ui-radio-group id="rbgGklMoeglich" :row="true">
									<svws-ui-radio-option v-for="value in formMoeglichNichtMoeglich" :class="value.key ? 'order-1' : 'order-0'" :key="value.label" :value="value.key" name="formGklMoeglich" :label="value.label" v-model="istGklMoeglich" :disabled="activeVorgabe.id < 0" />
								</svws-ui-radio-group>
							</div>
							<div v-if="(state.jahrgangsdaten !== undefined) && (activeVorgabe.idFach >= 0) && (state.halbjahr.id !== GostHalbjahr.Q22.id) && vorgabeIstModerneFremdsprache(activeVorgabe)" class="mt-4">
								<label class="block font-bold mb-1" for="rbgMdlPruefung">Mündliche Kommunikationsprüfung</label>
								<svws-ui-radio-group id="rbgMdlPruefung" :row="true">
									<svws-ui-radio-option v-for="value in formJaNein" :class="value.name === 'Ja' ? 'order-1' : 'order-0'" :key="value.name" :value="value.key" name="formMdlPruefung" :label="value.name" v-model="istMdlPruefung" :disabled="activeVorgabe.id < 0">
										<span class="icon i-ri-chat-1-line -my-1 -mx-0.5" v-if="value.name === 'Ja'" />
									</svws-ui-radio-option>
								</svws-ui-radio-group>
							</div>
							<div class="border-t border-ui-25 pt-4 font-bold">Klausurdetails</div>
							<div>
								<label class="sr-only" for="rbgAudioNotwendig">Audio notwendig: </label>
								<svws-ui-radio-group id="rbgAudioNotwendig" :row="true">
									<svws-ui-radio-option v-for="value in formJaNein" :class="value.name === 'Ja' ? 'order-1' : 'order-0'" :key="value.name" :value="value.key" name="formAudioNotwendig" :label="value.name === 'Ja' ? 'Mit Audioteil' : 'Ohne Audio'" v-model="istAudioNotwendig" :disabled="activeVorgabe.id < 0">
										<span class="icon i-ri-headphone-line -my-1 -mx-0.5" v-if="value.name === 'Ja'" />
									</svws-ui-radio-option>
								</svws-ui-radio-group>
							</div>
							<div>
								<label class="sr-only" for="rbgVideoNotwendig">Video notwendig: </label>
								<svws-ui-radio-group id="rbgVideoNotwendig" :row="true">
									<svws-ui-radio-option v-for="value in formJaNein" :class="value.name === 'Ja' ? 'order-1' : 'order-0'" :key="value.name" :value="value.key" name="formVideoNotwendig" :label="value.name === 'Ja' ? 'Mit Videoteil' : 'Ohne Video'" v-model="istVideoNotwendig" :disabled="activeVorgabe.id < 0">
										<span class="icon i-ri-vidicon-line -my-1 -mx-0.5" v-if="value.name === 'Ja'" />
									</svws-ui-radio-option>
								</svws-ui-radio-group>
							</div>
							<svws-ui-spacing />
							<svws-ui-textarea-input placeholder="Bemerkungen" :model-value="activeVorgabe.bemerkungVorgabe" @change="bemerkungVorgabe => activeVorgabe.id !== 0 ? state.patchKlausurvorgabe({bemerkungVorgabe}, activeVorgabe.id) : activeVorgabe.bemerkungVorgabe = bemerkungVorgabe" resizeable="vertical" :disabled="activeVorgabe.id < 0" />
						</svws-ui-input-wrapper>
					</div>
					<div v-if="activeVorgabe.id === 0" class="flex gap-1 flex-wrap justify-start mt-9">
						<div v-if="(activeVorgabe.idFach === -1) || (activeVorgabe.kursart === '') || (activeVorgabe.quartal === -1)" class="mb-3 leading-tight opacity-50"><span class="icon i-ri-information-line inline align-text-top mr-0.5" />Um die Vorgabe zu speichern, müssen Fach, Kursart und Quartal ausgewählt werden.</div>
						<svws-ui-button type="secondary" @click="cancelEdit">Abbrechen</svws-ui-button>
						<svws-ui-button @click="saveKlausurvorgabe" :disabled="(activeVorgabe.idFach === -1) || (activeVorgabe.kursart === '') || (activeVorgabe.quartal === -1)">Speichern</svws-ui-button>
					</div>
				</template>
			</template>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { Ref } from 'vue';
	import { watch, computed, ref, onMounted, onUnmounted, triggerRef } from 'vue';
	import { useBenutzerState, useGostKlausurplanungState, type DataTableColumn } from "@ui";
	import type { Comparator, GostFach, List, ValidatorFehler } from "@core";
	import { GostHalbjahr, BenutzerKompetenz, ArrayList, GostKlausurvorgabe } from "@core";
	import { ValidatorGostKlausurdauer } from "./validation/ValidatorGostKlausurdauer";
	import { useKlausurplanungPresenter } from "./SGostKlausurplanungPresenter";

	const { gotoFach } = defineProps<{
		gotoFach: (idFach: number) => Promise<void>;
	}>();
	const state = useGostKlausurplanungState();
	const benutzerState = useBenutzerState();
	const presenter = useKlausurplanungPresenter(state);

	const hatKompetenzUpdate = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN));

	const vorgaben = () => state.manager.vorgabeGetMengeByHalbjahrAndQuartal(state.jahrgangsdaten.abiturjahr, state.halbjahr, state.quartal);

	const selectedVorgabeRow = ref<GostKlausurvorgabe>();
	const activeVorgabe: Ref<GostKlausurvorgabe> = ref(new GostKlausurvorgabe());

	const selected = ref<GostKlausurvorgabe[]>([]);
	watch([() => state.jahrgangsdaten, () => state.halbjahr], () => {
		selected.value = [];
	});
	const selectedHatVerwendeteVorgaben = computed<boolean>(() => selected.value.some(vorgabe => state.manager.istVorgabeVerwendetByKursklausur(vorgabe)));
	const selectedHatDauerWarnung = computed<boolean>(() => selected.value.some(vorgabe => dauerWarnung(vorgabe) !== null));

	const formKursarten = computed(() => ["GK", "LK"]);
	const formJaNein = computed(() => [{ key: true, name: "Ja" }, { key: false, name: "Nein" }]);
	const formMoeglichNichtMoeglich = computed(() => [{ key: true, label: "möglich" }, { key: false, label: "nicht möglich" }]);
	const formQuartale = computed(() => [1, 2]);
	const gklInHalbjahrMoeglich = computed<boolean>(() => {
		const abiturjahr = state.jahrgangsdaten.abiturjahr;
		return ((abiturjahr === -1) || (abiturjahr >= 2030)) && (state.halbjahr.id !== GostHalbjahr.Q22.id);
	});

	const faecherSortiert = computed(() => {
		const result = new ArrayList(state.manager.getFaecherManager(state.jahrgangsdaten.abiturjahr).getFaecherSchriftlichMoeglich());
		result.sort(fachComparator);
		return result;
	});

	const fachComparator: Comparator<GostFach> = {
		compare: (a, b) => {
			const compareBezeichnung = (a.bezeichnung ?? "").localeCompare(b.bezeichnung ?? "");
			return compareBezeichnung !== 0 ? compareBezeichnung : a.kuerzel.localeCompare(b.kuerzel);
		},
	};

	const istGklMoeglich = computed<boolean>({
		get: () => activeVorgabe.value.istGklMoeglich,
		set: (value) => {
			activeVorgabe.value.istGklMoeglich = value;
			if (activeVorgabe.value.id !== 0) {
				void state.patchKlausurvorgabe({ istGklMoeglich: value }, activeVorgabe.value.id);
			}
		},
	});

	const istMdlPruefung = computed<boolean>({
		get: () => activeVorgabe.value.istMdlPruefung,
		set: (value) => {
			activeVorgabe.value.istMdlPruefung = value;
			if (activeVorgabe.value.id !== 0) {
				void state.patchKlausurvorgabe({ istMdlPruefung: value }, activeVorgabe.value.id);
			}
		},
	});

	function vorgabeHatGklMoeglich(vorgabe: GostKlausurvorgabe) {
		return gklInHalbjahrMoeglich.value && (vorgabe.kursart === 'GK');
	}

	function fachBezeichnungByVorgabe(vorgabe: GostKlausurvorgabe): string {
		return state.manager.fachOrNullByVorgabe(vorgabe)?.bezeichnung ?? `Fach-ID ${vorgabe.idFach}`;
	}

	function fachFehltText(vorgabe: GostKlausurvorgabe): string | undefined {
		return state.manager.fachOrNullByVorgabe(vorgabe) === null ? `Fach mit ID ${vorgabe.idFach} ist nicht als Fach der Oberstufe definiert.` : undefined;
	}

	function vorgabeIstModerneFremdsprache(vorgabe: GostKlausurvorgabe): boolean {
		const fach = state.manager.fachOrNullByVorgabe(vorgabe);
		return (fach !== null) && state.manager.getFaecherManager(vorgabe.abiturjahrgang).fachIstModerneFremdsprache(fach.id);
	}

	function vorgabeIstNeuEinsetzendeFremdsprache(vorgabe: GostKlausurvorgabe): boolean {
		const fach = state.manager.fachOrNullByVorgabe(vorgabe);
		return (fach !== null) && fach.istFremdSpracheNeuEinsetzend;
	}

	function berechneGostKlausurdauerByVorgabeOrNull(vorgabe: GostKlausurvorgabe): number | null {
		return state.manager.fachOrNullByVorgabe(vorgabe) === null ? null : state.manager.berechneGostKlausurdauerByVorgabe(vorgabe);
	}

	function setDauerAufApoVorgabe() {
		void setDauerAufGostVorgabe([activeVorgabe.value]);
	}

	async function setAusgewaehlteDauerAufApoVorgabe() {
		await setDauerAufGostVorgabe(selected.value.filter(vorgabe => dauerWarnung(vorgabe) !== null));
	}

	async function setDauerAufGostVorgabe(vorgaben: GostKlausurvorgabe[]) {
		const patches = new ArrayList<Partial<GostKlausurvorgabe>>();
		for (const vorgabe of vorgaben) {
			if (vorgabe.id <= 0) {
				continue;
			}
			const dauer = berechneGostKlausurdauerByVorgabeOrNull(vorgabe);
			if (dauer === null) {
				continue;
			}
			patches.add({ id: vorgabe.id, dauer });
		}
		if (patches.isEmpty()) {
			return;
		}
		await state.patchKlausurvorgaben(patches);
	}

	function validiereDauer(vorgabe: GostKlausurvorgabe = activeVorgabe.value): List<ValidatorFehler> {
		const validator = new ValidatorGostKlausurdauer(
			() => (vorgabe.id > 0) && (state.manager.fachOrNullByVorgabe(vorgabe) !== null) ? vorgabe : null,
			vorgabe => state.manager.berechneGostKlausurdauerByVorgabe(vorgabe),
			vorgabe => vorgabeIstNeuEinsetzendeFremdsprache(vorgabe)
		);
		validator.run();
		return validator.getFehler();
	}

	function dauerWarnung(vorgabe: GostKlausurvorgabe): string | null {
		const fehler = validiereDauer(vorgabe);
		return fehler.isEmpty() ? null : fehler.get(0).getFehlermeldung();
	}

	const istAudioNotwendig = computed<boolean>({
		get: () => activeVorgabe.value.istAudioNotwendig,
		set: (value) => {
			activeVorgabe.value.istAudioNotwendig = value;
			if (activeVorgabe.value.id !== 0) {
				void state.patchKlausurvorgabe({ istAudioNotwendig: value }, activeVorgabe.value.id);
			}
		},
	});

	const istVideoNotwendig = computed<boolean>({
		get: () => activeVorgabe.value.istVideoNotwendig,
		set: (value) => {
			activeVorgabe.value.istVideoNotwendig = value;
			if (activeVorgabe.value.id !== 0) {
				void state.patchKlausurvorgabe({ istVideoNotwendig: value }, activeVorgabe.value.id);
			}
		},
	});

	function toggleVorgabeBoolean(vorgabe: GostKlausurvorgabe, key: 'istGklMoeglich' | 'istMdlPruefung') {
		if (!hatKompetenzUpdate.value) {
			return;
		}
		const value = !vorgabe[key];
		vorgabe[key] = value;
		if (activeVorgabe.value.id === vorgabe.id) {
			triggerRef(activeVorgabe);
		}
		void state.patchKlausurvorgabe({ [key]: value }, vorgabe.id);
	}

	const neueVorgabe = () => {
		activeVorgabe.value.id = 0;
	};

	const saveKlausurvorgabe = async () => {
		if ((activeVorgabe.value.idFach === -1) || (activeVorgabe.value.kursart === "") || (activeVorgabe.value.quartal === -1)) {
			console.log("Eingabefehler");
			return;
		}
		if (activeVorgabe.value.id === 0) {
			try {
				await state.erzeugeKlausurvorgabe(activeVorgabe.value);
				activeVorgabe.value = new GostKlausurvorgabe();
			} catch (error) {
				console.log("Vorgabe konnte nicht erzeugt werden, wahrscheinlich existiert sie schon.", activeVorgabe.value);
			}
		}
	};

	const loescheVorgaben = async (vorgaben: GostKlausurvorgabe[]) => {
		if (vorgaben.length === 0) {
			return;
		}
		await state.loescheKlausurvorgaben(ArrayList.of(...vorgaben));
		selected.value = [];
		selectedVorgabeRow.value = undefined;
		activeVorgabe.value = new GostKlausurvorgabe();
	};

	const cancelEdit = () => {
		selectedVorgabeRow.value = undefined;
		activeVorgabe.value = new GostKlausurvorgabe();
	};

	const startEdit = () => {
		if (selectedVorgabeRow.value !== undefined) {
			const v = state.manager.vorgabeGetByIdOrException(selectedVorgabeRow.value.id);
			if (activeVorgabe.value.id === v.id) {
				cancelEdit();
			} else {
				activeVorgabe.value = v;
			}
		}
	};

	const isMounted = ref(false);
	onMounted(() => {
		isMounted.value = true;
		globalThis.addEventListener('click', handleClick);
	});

	onUnmounted(() => {
		globalThis.removeEventListener('click', handleClick);
	});

	const cols = computed<DataTableColumn[]>(() => [
		{ key: 'status', label: '', align: "center", tooltip: 'Status', fixedWidth: 2.5 },
		{ key: 'idFach', label: 'Fach', span: 1.25, sortable: true },
		{ key: 'kursart', label: 'Kursart', span: 0.5, sortable: true },
		{ key: 'quartal', label: 'Quartal', span: 0.5, sortable: true },
		{ key: 'dauer', label: 'Dauer', tooltip: 'Dauer in Minuten', span: 0.5, sortable: true },
		{ key: 'auswahlzeit', label: 'Auswahlzeit', tooltip: 'Auswahlzeit in Minuten', span: 0.5, sortable: false },
		...(gklInHalbjahrMoeglich.value ? [{ key: 'istGklMoeglich', label: 'G', align: "center", tooltip: 'Gleichwertige komplexe Lernleistung möglich', fixedWidth: 2.5 } satisfies DataTableColumn] : []),
		...(state.halbjahr.id !== GostHalbjahr.Q22.id ? [{ key: 'istMdlPruefung', label: 'M', align: "center", tooltip: 'Mündliche Kommunikationsprüfung', fixedWidth: 2.5 } satisfies DataTableColumn] : []),
		{ key: 'istAudioNotwendig', label: 'A', align: "center", tooltip: 'Mit Audioteil', fixedWidth: 2.5 },
		{ key: 'istVideoNotwendig', label: 'V', align: "center", tooltip: 'Mit Videoteil', fixedWidth: 2.5 },
		{ key: 'bemerkungVorgabe', label: 'Bemerkung', span: 1.25 },
	]);

	function getBgColor(kuerzel: string | null) {
		return presenter.fachFarbeByKuerzel(kuerzel);
	}

	function handleClick(e: MouseEvent) {
		const vT = document.getElementById('vorgabenTable');
		const vE = document.getElementById('vorgabenEdit');

		if ((vE !== null) && (vT !== null) &&
			!vT.contains(e.target as Node) &&
			!vE.contains(e.target as Node) &&
			!(((e.target as HTMLElement).parentElement?.parentElement?.classList.contains("svws-ui-dropdown-list")) ?? false) &&
			!(((e.target as HTMLElement).parentElement?.parentElement?.parentElement?.classList.contains("svws-ui-dropdown-list")) ?? false)
		) {
			activeVorgabe.value = new GostKlausurvorgabe();
			selectedVorgabeRow.value = undefined;
		}
	}

</script>
