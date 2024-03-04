<template>
	<svws-ui-content-card overflow-scroll :class="{'mr-16': blockungstabelleVisible}">
		<svws-ui-table :items="GostKursart.values()" :columns="cols" disable-footer scroll has-background :style="!blockungstabelleVisible ? 'margin-left: 0; margin-right: 0; opacity: 0;' : ''">
			<template #header>
				<div role="row" class="svws-ui-tr select-none">
					<div role="columnheader" class="svws-ui-td svws-divider" :class="allow_regeln ? 'col-span-7' : 'col-span-6'">
						<div class="flex items-center justify-between w-full -my-2">
							<div class="flex flex-row gap-2">
								<span>Schiene</span>
							</div>
							<div class="flex flex-row gap-2">
								<svws-ui-button type="transparent" size="small" @click="setZeigeSchienenbezeichnungen(!zeigeSchienenbezeichnungen())" :title="zeigeSchienenbezeichnungen() ? 'Zeige nur die Schienenummer':'Zeige den vollständigen Schienennamen'">
									<i-ri-text v-if="zeigeSchienenbezeichnungen()" />
									<i-ri-expand-height-line v-else />
								</svws-ui-button>
								<template v-if="allow_regeln">
									<svws-ui-button type="icon" size="small" @click="add_schiene" title="Schiene hinzufügen">
										<i-ri-add-line />
									</svws-ui-button>
								</template>
							</div>
						</div>
					</div>
					<div role="columnheader" class="svws-ui-td svws-align-center !overflow-visible !px-0" v-for="(s, index) in schienen" :key="s.id" :class="{'svws-divider': index + 1 < schienen.size()}">
						<div class="flex justify-center text-center items-center w-full relative">
							<svws-ui-tooltip v-if="s.id === edit_schienenname" keep-open>
								<span class="opacity-50 border-b border-transparent">{{ s.nummer }}</span>
								<template #content>
									<div class="py-2">
										<svws-ui-text-input :model-value="s.bezeichnung" focus headless
											@change="name => patch_schiene(s, name)"
											@blur="name => patch_schiene(s, name)"
											@keyup.escape="edit_schienenname=undefined" class="text-center" />
									</div>
								</template>
							</svws-ui-tooltip>
							<template v-else-if="zeigeSchienenbezeichnungen()">
								<div>
									<span style="writing-mode: vertical-lr;" :class="{ 'border-l border-dotted hover:border-transparent': allow_regeln }" class="cursor-text rotate-180 normal-nums min-h-[1.5ch] w-full inline-flex justify-center" :title="'Namen bearbeiten (' + s.bezeichnung + ')'" @click="allow_regeln && (edit_schienenname = s.id)">
										{{ s.bezeichnung }}
									</span>
									<i-ri-delete-bin-line v-if="allow_del_schiene(s)" @click="del_schiene(s)" class="cursor-pointer absolute w-4 h-4 top-1/2 transform -translate-y-1/2 right-px text-sm opacity-50 hover:opacity-100 hover:text-error" />
								</div>
							</template>
							<template v-else>
								<span :class="{ 'border-b border-dotted hover:border-transparent': allow_regeln }" class="cursor-text normal-nums min-w-[1.5ch] h-full inline-flex items-center justify-center" :title="'Namen bearbeiten (' + s.bezeichnung + ')'" @click="allow_regeln && (edit_schienenname = s.id)">{{ s.nummer }}</span>
								<i-ri-delete-bin-line v-if="allow_del_schiene(s)" @click="del_schiene(s)" class="cursor-pointer absolute w-4 h-4 top-1/2 transform -translate-y-1/2 right-px text-sm opacity-50 hover:opacity-100 hover:text-error" />
							</template>
						</div>
					</div>
				</div>
				<div role="row" class="svws-ui-tr select-none">
					<div role="columnheader" class="svws-ui-td svws-divider" :class="allow_regeln ? 'col-span-7' : 'col-span-6'">
						Schülerzahl
					</div>
					<div role="columnheader" class="svws-ui-td svws-align-center !px-0" v-for="(s, index) in schienen" :key="s.id" :class="{'svws-divider': index + 1 < schienen.size()}">
						<template v-if="getAnzahlSchuelerSchiene(s.id) > 0">
							<span class="inline-flex items-center gap-1">{{ getAnzahlSchuelerSchiene(s.id) }}</span>
						</template>
						<span v-else class="opacity-25">0</span>
					</div>
				</div>
				<div role="row" class="svws-ui-tr select-none">
					<div role="columnheader" class="svws-ui-td svws-divider" :class="allow_regeln ? 'col-span-7' : 'col-span-6'">
						Kollisionen
					</div>
					<div role="columnheader" class="svws-ui-td svws-align-center !px-0" v-for="(s, index) in schienen" :key="s.id" :class="{'text-error': getAnzahlKollisionenSchiene(s.id) > 0, 'svws-divider': index + 1 < schienen.size()}">
						<svws-ui-tooltip v-if="getAnzahlKollisionenSchiene(s.id) > 0" autosize>
							<span class="inline-flex items-center"><i-ri-alert-line />{{ getAnzahlKollisionenSchiene(s.id) }}</span>
							<template #content>
								<template v-for="list, indexTooltip of getErgebnismanager().getOfSchieneTooltipKurskollisionenAsData(s.id)" :key="indexTooltip">
									<p>
										<template v-for="pair, listIndex of list" :key="pair.a.id">
											<span v-if="listIndex === 0" class="font-bold">{{ getDatenmanager().kursGetName(pair.a.id) }}:&nbsp;</span>
											<span v-else>{{ getDatenmanager().kursGetName(pair.a.id) }} ({{ pair.b ?? 0 }}){{ listIndex !== list.size()-1 ? ',&nbsp;': '' }}</span>
										</template>
									</p>
								</template>
							</template>
						</svws-ui-tooltip>
						<span v-else class="opacity-25 font-normal">0</span>
					</div>
				</div>
				<div role="row" class="svws-ui-tr select-none">
					<div role="columnheader" class="svws-ui-td svws-align-center" aria-label="Alle auswählen">
						<svws-ui-checkbox :model-value="getDatenmanager().kursGetAnzahl() === getKursauswahl().size()" :indeterminate="(getKursauswahl().size() > 0) && (getKursauswahl().size() < getDatenmanager().kursGetAnzahl())"
							@update:model-value="updateKursauswahl" headless />
					</div>
					<div role="columnheader" class="svws-ui-td svws-sortable-column" @click="kurssortierung.value = (kurssortierung.value === 'kursart') ? 'fach' : 'kursart'" :class="{'col-span-2': allow_regeln, 'col-span-1': !allow_regeln, 'svws-active': kurssortierung.value === 'kursart'}">
						<span>Kurs</span>
						<span class="svws-sorting-icon">
							<i-ri-arrow-up-down-line class="svws-sorting-asc" :class="{'svws-active': kurssortierung.value === 'kursart'}" />
							<i-ri-arrow-up-down-line class="svws-sorting-desc" :class="{'svws-active': kurssortierung.value === 'kursart'}" />
						</span>
					</div>
					<div role="columnheader" class="svws-ui-td">Lehrkraft</div>
					<div class="svws-ui-td svws-align-center" title="Kooperation">Koop</div>
					<div class="svws-ui-td svws-align-center" title="Fachwahlen">
						<svws-ui-tooltip>
							FW
							<template #content>
								Fachwahlen
							</template>
						</svws-ui-tooltip>
					</div>
					<div class="svws-ui-td svws-align-center svws-divider" title="Differenz">Diff</div>
					<!--Schienen-->
					<template v-if="allow_regeln">
						<template v-for="(schiene, index) in schienen" :key="schiene.id">
							<div @dragover="if (istDropZoneSchiene(schiene).value) $event.preventDefault();" @drop="openModalRegelKursartSchiene(schiene)" class="svws-ui-td svws-align-center text-black/25 dark:text-white/25 !p-0 hover:text-black dark:hover:text-white relative group" role="columnheader"
								:class="{ 'bg-primary/5 text-primary hover:text-primary dark:text-primary dark:hover:text-primary': istDropZoneSchiene(schiene).value, 'svws-divider': index + 1 < schienen.size() }">
								<div :key="schiene.id" @click="openModalRegelKursartSchiene(schiene)" class="select-none cursor-pointer text-center" :class="{'cursor-grabbing': dragSperreSchiene !== undefined}" :draggable="true" @dragstart="dragSchieneStarted(schiene)" @dragend="dragSchieneEnded">
									<span class="rounded-sm w-3 absolute top-1 left-1 max-w-[0.75rem] cursor-grab">
										<i-ri-draggable class="w-4 -ml-0.5 text-black opacity-25 group-hover:opacity-100 group-hover:text-black" />
									</span>
									<i-ri-lock-unlock-line class="inline-block" />
								</div>
							</div>
						</template>
					</template>
					<template v-else>
						<div v-for="(schiene, index) in schienen" :key="schiene.id" role="columnheader" class="svws-ui-td !px-0 svws-align-center font-normal text-black/10 dark:text-white/10" :class="{'svws-divider': index + 1 < schienen.size() }">
							<span>
								<i-ri-lock-unlock-line class="inline-block" />
							</span>
						</div>
					</template>
				</div>
			</template>

			<template #body>
				<template v-if="kurssortierung.value === 'fach'">
					<template v-for="fachwahlen in mapFachwahlStatistik().values()" :key="fachwahlen.id">
						<template v-for="kursart in GostKursart.values()" :key="kursart.id">
							<s-gost-kursplanung-kursansicht-fachwahl v-if="istFachwahlVorhanden(fachwahlen, kursart).value"
								:fachwahlen="fachwahlen" :kursart="kursart" :get-kursauswahl="getKursauswahl"
								:faecher-manager="faecherManager" :get-datenmanager="getDatenmanager" :hat-ergebnis="hatErgebnis" :get-ergebnismanager="getErgebnismanager"
								:map-lehrer="mapLehrer" :allow-regeln="allow_regeln" :schueler-filter="schuelerFilter"
								:fachwahlen-anzahl="getAnzahlFachwahlen(fachwahlen, kursart)"
								:regeln-update="regelnUpdate" :update-kurs-schienen-zuordnung="updateKursSchienenZuordnung"
								:patch-kurs="patchKurs" :add-kurs="addKurs" :remove-kurse="removeKurse" :add-kurs-lehrer="addKursLehrer"
								:remove-kurs-lehrer="removeKursLehrer" :add-schiene-kurs="addSchieneKurs" :remove-schiene-kurs="removeSchieneKurs" :split-kurs="splitKurs" :combine-kurs="combineKurs"
								:kurse-und-schienen-in-rechteck="kurseUndSchienenInRechteck" :api-status="apiStatus"
								:set-drag="setDrag" :set-drop="setDrop" :highlight-kurs-auf-anderen-kurs="highlightKursAufAnderenKurs"
								:highlight-kurs-verschieben="highlightKursVerschieben" :highlight-rechteck="highlightRechteck" :highlight-rechteck-drop="highlightRechteckDrop"
								:is-dragging="isDragging" :reset-drag="resetDrag" :reset-drag-over="resetDragOver" :set-drag-over="setDragOver" :reset-drop="resetDrop" :show-tooltip="showTooltip" />
						</template>
					</template>
				</template>
				<template v-else>
					<template v-for="kursart in GostKursart.values()" :key="kursart.id">
						<template v-for="fachwahlen in mapFachwahlStatistik().values()" :key="fachwahlen.id">
							<s-gost-kursplanung-kursansicht-fachwahl v-if="istFachwahlVorhanden(fachwahlen, kursart).value"
								:fachwahlen="fachwahlen" :kursart="kursart" :get-kursauswahl="getKursauswahl"
								:faecher-manager="faecherManager" :get-datenmanager="getDatenmanager" :hat-ergebnis="hatErgebnis" :get-ergebnismanager="getErgebnismanager"
								:map-lehrer="mapLehrer" :allow-regeln="allow_regeln" :schueler-filter="schuelerFilter"
								:fachwahlen-anzahl="getAnzahlFachwahlen(fachwahlen, kursart)"
								:regeln-update="regelnUpdate" :update-kurs-schienen-zuordnung="updateKursSchienenZuordnung"
								:patch-kurs="patchKurs" :add-kurs="addKurs" :remove-kurse="removeKurse" :add-kurs-lehrer="addKursLehrer"
								:remove-kurs-lehrer="removeKursLehrer" :add-schiene-kurs="addSchieneKurs" :remove-schiene-kurs="removeSchieneKurs" :split-kurs="splitKurs" :combine-kurs="combineKurs"
								:kurse-und-schienen-in-rechteck="kurseUndSchienenInRechteck" :api-status="apiStatus"
								:set-drag="setDrag" :set-drop="setDrop" :highlight-kurs-auf-anderen-kurs="highlightKursAufAnderenKurs"
								:highlight-kurs-verschieben="highlightKursVerschieben" :highlight-rechteck="highlightRechteck" :highlight-rechteck-drop="highlightRechteckDrop"
								:is-dragging="isDragging" :reset-drag="resetDrag" :reset-drag-over="resetDragOver" :set-drag-over="setDragOver" :reset-drop="resetDrop" :show-tooltip="showTooltip" />
						</template>
					</template>
				</template>
			</template>
		</svws-ui-table>
		<s-gost-kursplanung-kursansicht-modal-regel-schienen :get-ergebnis-manager="getErgebnismanager" :regeln-update="regelnUpdate" ref="modalRegelKursartSchienen" />
		<s-gost-kursplanung-kursansicht-modal-regel-kurse :get-ergebnis-manager="getErgebnismanager" :regeln-update="regelnUpdate" ref="modal_regel_kurse" />
		<s-gost-kursplanung-kursansicht-modal-combine-kurse :get-datenmanager="getDatenmanager" :combine-kurs="combineKurs" ref="modal_combine_kurse" />
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { WritableComputedRef } from "vue";
	import { computed, onMounted, ref, toRaw } from "vue";
	import type { ApiStatus } from "~/components/ApiStatus";
	import type { DataTableColumn } from "@ui";
	import type { GostKursplanungSchuelerFilter } from "./GostKursplanungSchuelerFilter";
	import type { GostBlockungKursLehrer, GostBlockungsdatenManager, GostBlockungsergebnisKurs, GostBlockungsergebnisManager, GostFach, GostFaecherManager, GostHalbjahr, GostStatistikFachwahl, JavaSet, LehrerListeEintrag, List , GostBlockungRegelUpdate} from "@core";
	import { GostKursart, GostStatistikFachwahlHalbjahr, HashSet, ZulaessigesFach , GostBlockungKurs, GostBlockungSchiene } from "@core";

	const props = defineProps<{
		getDatenmanager: () => GostBlockungsdatenManager;
		getKursauswahl: () => JavaSet<number>,
		getErgebnismanager: () => GostBlockungsergebnisManager;
		regelnUpdate: (update: GostBlockungRegelUpdate) => Promise<void>;
		updateKursSchienenZuordnung: (idKurs: number, idSchieneAlt: number, idSchieneNeu: number) => Promise<boolean>;
		patchSchiene: (data: Partial<GostBlockungSchiene>, id : number) => Promise<void>;
		addSchiene: () => Promise<GostBlockungSchiene | undefined>;
		removeSchiene: (s: GostBlockungSchiene) => Promise<GostBlockungSchiene | undefined>;
		patchKurs: (data: Partial<GostBlockungKurs>, kurs_id: number) => Promise<void>;
		addKurs: (fach_id : number, kursart_id : number) => Promise<GostBlockungKurs | undefined>;
		removeKurse: (ids: Iterable<number>) => Promise<void>;
		combineKurs: (kurs1 : GostBlockungKurs, fach2: GostBlockungKurs | GostBlockungsergebnisKurs | undefined | null) => Promise<void>;
		splitKurs: (kurs: GostBlockungKurs) => Promise<void>;
		addKursLehrer: (kurs_id: number, lehrer_id: number) => Promise<GostBlockungKursLehrer | undefined>;
		removeKursLehrer: (kurs_id: number, lehrer_id: number) => Promise<void>;
		addSchieneKurs: (kurs: GostBlockungKurs) => Promise<void>;
		removeSchieneKurs: (kurs: GostBlockungKurs) => Promise<void>;
		ergebnisHochschreiben: () => Promise<void>;
		ergebnisAktivieren: () => Promise<boolean>;
		kurssortierung: WritableComputedRef<'fach' | 'kursart'>;
		existiertSchuljahresabschnitt: boolean;
		hatErgebnis: boolean;
		schuelerFilter: () => GostKursplanungSchuelerFilter;
		faecherManager: GostFaecherManager;
		halbjahr: GostHalbjahr;
		mapLehrer: Map<number, LehrerListeEintrag>;
		mapFachwahlStatistik: () => Map<number, GostStatistikFachwahl>;
		blockungstabelleVisible: boolean;
		toggleBlockungstabelle: () => void;
		zeigeSchienenbezeichnungen: () => boolean;
		setZeigeSchienenbezeichnungen: (value: boolean) => void;
		apiStatus: ApiStatus;
	}>();

	const edit_schienenname = ref<number|undefined>(undefined);

	const istFachwahlVorhanden = (fachwahlen: GostStatistikFachwahl, kursart: GostKursart) => computed<boolean>(() => {
		const anzahl = props.getDatenmanager().kursGetListeByFachUndKursart(fachwahlen.id, kursart.id).size();
		return (anzahl > 0) || (allow_regeln.value && getAnzahlFachwahlen(fachwahlen, kursart) > 0);
	});

	const kurse = computed(() => props.kurssortierung.value === 'fach'
		? props.getDatenmanager().kursGetListeSortiertNachFachKursartNummer()
		: props.getDatenmanager().kursGetListeSortiertNachKursartFachNummer())

	const schienen = computed<List<GostBlockungSchiene>>(() => props.getDatenmanager().schieneGetListe());

	const allow_regeln = computed<boolean>(() => (props.getDatenmanager().ergebnisGetListeSortiertNachBewertung().size() === 1));

	function calculateColumns() {
		const cols: DataTableColumn[] = [];
		cols.push({ key: "auswahl", label: "Kursauswahl", fixedWidth: 1.5, align: 'center' });
		if (allow_regeln.value)
			cols.push({ key: "actions", label: "Actions", fixedWidth: 1.5, align: 'center' });
		cols.push({ key: "kurs", label: "Kurs", span: 1.75, minWidth: 8 },
			{ key: "lehrer", label: "Lehrer", span: 1.5, minWidth: 6 },
			{ key: "koop", label: "Kooperation", align: 'center', fixedWidth: 3.75 },
			{ key: "FW", label: "Fachwahl", align: 'center', fixedWidth: 3.75 },
			{ key: "Diff", label: "Diff", align: 'center', fixedWidth: 3.75 });
		for (let i = 0; i < schienen.value.size(); i++)
			cols.push({ key: "schiene_" + (i+1), label: "schiene_" + (i+1), fixedWidth: 3.75, align: 'center' });
		return cols;
	}

	const cols = computed(() => calculateColumns());

	function getAnzahlSchuelerSchiene(idSchiene: number): number {
		return props.hatErgebnis ? props.getErgebnismanager().getOfSchieneAnzahlSchueler(idSchiene) : 0;
	}

	function allow_del_schiene(schiene: GostBlockungSchiene): boolean {
		if (!props.hatErgebnis)
			return false;
		return props.getDatenmanager().schieneGetIsRemoveAllowed(schiene.id) && props.getErgebnismanager().getOfSchieneRemoveAllowed(schiene.id);
	}

	function updateKursauswahl() {
		const auswahl = props.getKursauswahl();
		const allSelected = (props.getDatenmanager().kursGetAnzahl() === auswahl.size());
		if (allSelected)
			auswahl.clear();
		else
			for (const kurs of props.getDatenmanager().kursGetListeSortiertNachFachKursartNummer())
				auswahl.add(kurs.id);
	}

	function getAnzahlKollisionenSchiene(idSchiene: number): number {
		return props.hatErgebnis ? props.getErgebnismanager().getOfSchieneAnzahlSchuelerMitKollisionen(idSchiene) : 0;
	}

	async function patch_schiene(schiene: GostBlockungSchiene, bezeichnung: string) {
		await props.patchSchiene({ bezeichnung }, schiene.id);
		edit_schienenname.value = undefined;
	}

	async function add_schiene() {
		// TODO: Update cols value mit neuer Anzahl von Schienen
		return await props.addSchiene();
	}

	async function del_schiene(schiene: GostBlockungSchiene) {
		// TODO: Update cols value mit neuer Anzahl von Schienen
		return await props.removeSchiene(schiene);
	}

	const isMounted = ref(false);
	onMounted(() => isMounted.value = true);

	function getAnzahlFachwahlen(fachwahlen: GostStatistikFachwahl, kursart: GostKursart) : number {
		const fach_halbjahr : GostStatistikFachwahlHalbjahr = fachwahlen.fachwahlen[props.halbjahr.id] || new GostStatistikFachwahlHalbjahr();
		const gostfach : GostFach | null = props.faecherManager.get(fachwahlen.id);
		if (gostfach === null)
			return 0;
		const zulFach : ZulaessigesFach = ZulaessigesFach.getByKuerzelASD(gostfach.kuerzel);
		switch (kursart) {
			case GostKursart.LK: return fach_halbjahr.wahlenLK;
			case GostKursart.GK: return (zulFach === ZulaessigesFach.PX) || (zulFach === ZulaessigesFach.VX) ? 0 : fach_halbjahr.wahlenGK;
			case GostKursart.ZK: return fach_halbjahr.wahlenZK;
			case GostKursart.PJK: return (zulFach === ZulaessigesFach.PX) ? fach_halbjahr.wahlenGK : 0;
			case GostKursart.VTF: return (zulFach === ZulaessigesFach.VX) ? fach_halbjahr.wahlenGK : 0;
			default: return 0;
		}
	}

	const dragSperreSchiene = ref<GostBlockungSchiene | undefined>(undefined);

	const modalRegelKursartSchienen = ref();

	const istDropZoneSchiene = (schiene: GostBlockungSchiene) => computed<boolean>(() => {
		return (dragSperreSchiene.value !== undefined && (dragSperreSchiene.value.id !== schiene.id));
	});

	function openModalRegelKursartSchiene(schiene: GostBlockungSchiene) {
		const andereSchiene = (dragSperreSchiene.value === undefined) ? schiene : dragSperreSchiene.value;
		modalRegelKursartSchienen.value.openModal(andereSchiene, schiene);
	}

	function dragSchieneStarted(schiene: GostBlockungSchiene) {
		dragSperreSchiene.value = schiene;
	}

	function dragSchieneEnded() {
		dragSperreSchiene.value = undefined;
	}

	const modal_regel_kurse = ref();
	const modal_combine_kurse = ref();

	/** Dieser Teil organisiert das Drag und Drop von Kursen und Schienen */
	const dragKurs 				=	ref<GostBlockungKurs|null>(null);
	const dragSchiene 		= ref<GostBlockungSchiene|null>(null);
	const dragFachID			= ref<number|null>(null);
	const dragOverKurs 		= ref<GostBlockungKurs|null>(null);
	const dragOverSchiene = ref<GostBlockungSchiene|null>(null);
	const dropKurs 				=	ref<GostBlockungKurs|null>(null);
	const dropSchiene 		= ref<GostBlockungSchiene|null>(null);
	const dropKurs2				= ref<GostBlockungKurs|null>(null);
	const dropSchiene2 		= ref<GostBlockungSchiene|null>(null);
	const dropFachID			= ref<number|null>(null);
	const showTooltip 		= ref<{kursID: number; schieneID: number;}>({kursID: -1, schieneID: -1});
	const kurseUndSchienenInRechteck = ref<[JavaSet<number>, JavaSet<number>] | null>(null);

	/** ist das Drag-Objekt ein Kurs, der auf der Schiene liegt? */
	const isKursDragging = computed(() => {
		if (dragKurs.value === null || dragSchiene.value === null)
			return false;
		return props.getErgebnismanager().getOfKursOfSchieneIstZugeordnet(dragKurs.value.id, dragSchiene.value.id);
	})

	/** Soll ein Kurs verschoben werden, dann befinden wir uns im gleichen Kurs, aber auf einer anderen Schiene */
	const highlightKursVerschieben = (kurs: GostBlockungKurs) => computed<boolean>(() => {
		if (dragKurs.value === null || dragSchiene.value === null || dragOverKurs.value === null || dragOverSchiene.value === null)
			return false;
		// wird ein leeres Feld gezogen, dann ist das kein Grund für ein Highlight
		if (!isKursDragging.value)
			return false;
		// befindet sich der Kurs außerhalb der eigenen Zeile, false
		if (dragKurs.value.id !== dragOverKurs.value.id)
			return false;
		// kurs auf Kurs und gleiche Schiene ist ungültig
		if (dragOverKurs.value.id === dragKurs.value.id && dragOverSchiene.value.id === dragSchiene.value.id)
			return false;
		// kurs auf kurs aber andere Schiene ist ok, Kurs fixierteVerschieben
		if (dragOverKurs.value.id === dragKurs.value.id && dragOverSchiene.value.id !== dragSchiene.value.id && kurs.id === dragOverKurs.value.id)
			return true;
		return false;
	})

	/** Es soll ein Kurs auf einen anderen Kurs gezogen werden, um gemeinsame Sache zu machen */
	const highlightKursAufAnderenKurs = (kurs: GostBlockungKurs, schiene: GostBlockungSchiene) => computed<boolean>(() => {
		if (dragKurs.value === null || dragSchiene.value === null || dragOverSchiene.value === null || dragOverKurs.value === null)
			return false;
		// wenn kein Kurs gezogen wird, dann kann auch kein Highlighting stattfinden
		if (!isKursDragging.value)
			return false;
		// kurs auf Kurs ist ungültig
		if (kurs.id === dragKurs.value.id)
			return false;
		if (kurs.id !== dragOverKurs.value.id || schiene.id !== dragOverSchiene.value.id)
			return false;
		// kurs auf anderen kurs ist ok, Kurs mit Kurs Sperren/Fixieren etc
		return props.getErgebnismanager().getOfKursOfSchieneIstZugeordnet(dragOverKurs.value.id, dragOverSchiene.value.id);
	})

	/** Wird ein Rechteck gezogen, so wird ein Feld über mehrere Kurse hinweg bewegt und landet nicht auf einem anderen Kurs */
	const highlightRechteck = (kurs: GostBlockungKurs, schiene: GostBlockungSchiene) => computed<boolean>(() => {
		if (dragKurs.value === null || dragSchiene.value === null || dragOverSchiene.value === null || dragOverKurs.value === null)
			return false;
		// ein Kurs in der gleichen Zeile, also wenn Kursauswahl = 1, dann nicht erlauben, weil wir verschieben
		if (isKursDragging.value && kurseInRechteckSet.value.size() === 1)
			return false;
		// wenn ich auf einem Kurs lande, dann will ich kein Rechteck, sondern eine Kurs mit Kurs-Aktion, es sei denn ich bin im gleichen Kurs oder ich habe keinen Kurs gezogen
		if (isKursDragging.value && props.getErgebnismanager().getOfKursOfSchieneIstZugeordnet(dragOverKurs.value.id, dragOverSchiene.value.id))
			return false;
		// ist der aktuelle Kurs nicht Teil des Rechtecks, dann nicht highlighten
		if (!kurseInRechteckSet.value.contains(kurs.id))
			return false;
		return ((schiene.nummer <= dragSchiene.value.nummer && schiene.nummer >= dragOverSchiene.value.nummer) || (schiene.nummer >= dragSchiene.value.nummer && schiene.nummer <= dragOverSchiene.value.nummer))
	})

	/** Wird ein Rechteck gezogen, so wird ein Feld über mehrere Kurse hinweg bewegt und landet nicht auf einem anderen Kurs */
	const highlightRechteckDrop = (kurs: GostBlockungKurs, schiene: GostBlockungSchiene) => computed<boolean>(() => {
		if (kurseUndSchienenInRechteck.value === null)
			return false;
		const [kurse, schienen] = kurseUndSchienenInRechteck.value;
		return (!kurse.contains(kurs.id)) || (!schienen.contains(schiene.nummer));
	})

	/** Dieses computed ermittelt ein Set von Kursen, die innerhalb des Rechtecks liegen */
	const kurseInRechteckSet = computed<JavaSet<number>>(() => {
		const range = new HashSet<number>();
		if (dragKurs.value === null || dragOverKurs.value === null)
			return range;
		const k1 = toRaw(dragKurs.value);
		const k2 = toRaw(dragOverKurs.value);
		let goon = false;
		const iK1 = kurse.value.indexOf(k1);
		const iK2 = kurse.value.indexOf(k2);
		const kMin = iK1 <= iK2 ? k1 : k2;
		const kMax = iK2 >= iK1 ? k2 : k1;
		for (const k of kurse.value) {
			if (k.id === kMin.id)
				goon = true;
			if (k.id === kMax.id) {
				range.add(k.id);
				break;
			}
			if (goon)
				range.add(k.id);
		}
		return range;
	})

	const isDragging = computed<boolean>(() => dragSchiene.value !== null && dropSchiene.value === null);

	function setDrag(p1: GostBlockungKurs | GostBlockungSchiene, p2?: GostBlockungSchiene, p3?: number) {
		if (p1 instanceof GostBlockungKurs)
			dragKurs.value = p1;
		else
			dragSchiene.value = p1;
		if (p2 instanceof GostBlockungSchiene && p1 instanceof GostBlockungKurs && typeof p3 === 'number') {
			dragSchiene.value = p2;
			dragFachID.value 	= p3;
		}
		else
			throw new Error("Es können keine zwei Schienen übergeben werden");
	}

	function setDragOver(kurs: GostBlockungKurs, schiene: GostBlockungSchiene) {
		if (kurs.id === dragOverKurs.value?.id && dragOverSchiene.value?.id === schiene.id)
			return;
		dragOverKurs.value = kurs;
		dragOverSchiene.value = schiene;
	}

	async function setDrop(p1: GostBlockungKurs | GostBlockungSchiene, p2?: GostBlockungSchiene, p3?: number) {
		if (p1 instanceof GostBlockungKurs)
			dropKurs.value = p1;
		else
			dropSchiene.value = p1;
		if (p2 instanceof GostBlockungSchiene && p1 instanceof GostBlockungKurs && typeof p3 === 'number') {
			dropSchiene.value = p2;
			dropFachID.value 	= p3;
		} else
			throw new Error("Es können keine zwei Schienen übergeben werden");
		if (highlightKursVerschieben(p1).value)
			await setKursVerschieben();
		else if (highlightKursAufAnderenKurs(p1, p2).value)
			setKursAufAnderenKurs();
		else if (highlightRechteck(p1, p2).value)
			setRechteck();
	}

	async function setKursVerschieben() {
		if (dropKurs.value === null || dropSchiene.value === null || dragKurs.value === null || dragSchiene.value === null)
			return;
		if (allow_regeln.value && props.getDatenmanager().kursGetHatFixierungInSchiene(dragKurs.value.id, dragSchiene.value.id))
			await props.regelnUpdate(props.getErgebnismanager().regelupdateRemove_02e_KURS_FIXIERE_IN_EINER_SCHIENE(dragKurs.value.id, dragSchiene.value.id));
		await props.updateKursSchienenZuordnung(dragKurs.value.id, dragSchiene.value.id, dropSchiene.value.id);
	}

	function setKursAufAnderenKurs() {
		if (dropKurs.value === null || dropSchiene.value === null || dragKurs.value === null || dragSchiene.value=== null)
			return;
		const schienen = props.getErgebnismanager().getOfKursSchienenmenge(dropKurs.value.id);
		if (schienen.contains(toRaw(dropSchiene.value)))
			modal_combine_kurse.value.openModal(dragKurs.value, dropKurs.value);
		else
			modal_regel_kurse.value.openModal(dragKurs.value.id, dropKurs.value.id);
		return;
	}

	function setRechteck() {
		dropKurs2.value = dragKurs.value;
		dropSchiene2.value = dragSchiene.value;
		if (dropKurs.value === null || dropSchiene.value === null || dragKurs.value === null || dragSchiene.value === null)
			return;
		showTooltip.value = { kursID: dropKurs.value.id, schieneID: dropSchiene.value.id };
		const s1 = props.getErgebnismanager().getSchieneG(dropSchiene.value.id);
		const s2 = props.getErgebnismanager().getSchieneG(dragSchiene.value.id);
		resetDrop();
		const schienenSet = new HashSet<number>();
		for (let i = Math.min(s1.nummer, s2.nummer); (i < Math.max(s1.nummer, s2.nummer) +1); i++)
			schienenSet.add(i);
		kurseUndSchienenInRechteck.value = [kurseInRechteckSet.value, schienenSet];
	}

	function resetDrag() {
		dragKurs.value = null;
		dragSchiene.value = null;
		dragFachID.value = null;
	}

	function resetDragOver() {
		dragOverKurs.value = null;
		dragOverSchiene.value = null;
	}

	function resetDrop() {
		dropKurs.value = null;
		dropSchiene.value = null;
		dropFachID.value = null;
		dropKurs2.value = null;
		dropSchiene2.value = null;
		showTooltip.value = {kursID: -1, schieneID: -1};
	}

</script>
