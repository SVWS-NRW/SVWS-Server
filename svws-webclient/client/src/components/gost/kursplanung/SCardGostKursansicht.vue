<template>
	<svws-ui-content-card overflow-scroll :class="{'mr-16': blockungstabelleVisible}">
		<svws-ui-table :items="GostKursart.values()" :columns="cols" disable-footer scroll has-background :style="!blockungstabelleVisible ? 'margin-left: 0; margin-right: 0; opacity: 0;' : ''">
			<template #header>
				<div role="row" class="svws-ui-tr">
					<div role="columnheader" class="svws-ui-td svws-divider" :class="allow_regeln ? 'col-span-7' : 'col-span-6'">
						<div class="flex items-center justify-between w-full -my-2">
							<span>Schiene</span>
							<template v-if="allow_regeln">
								<svws-ui-button type="icon" size="small" @click="add_schiene" title="Schiene hinzufügen">
									<i-ri-add-line />
								</svws-ui-button>
							</template>
						</div>
					</div>
					<div role="columnheader" class="svws-ui-td svws-align-center !overflow-visible !px-0" v-for="(s, index) in schienen" :key="s.id" :class="{'svws-divider': index + 1 < schienen.size()}">
						<div v-if="allow_regeln" class="flex justify-center text-center items-center w-full relative">
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
							<template v-else>
								<span class="cursor-text normal-nums min-w-[1.5ch] h-full inline-flex items-center justify-center border-b border-dotted hover:border-transparent" :title="'Namen bearbeiten (' + s.bezeichnung + ')'" @click="edit_schienenname = s.id">{{ s.nummer }}</span>
								<i-ri-delete-bin-line v-if="allow_del_schiene(s)" @click="del_schiene(s)" class="cursor-pointer absolute w-4 h-4 top-1/2 transform -translate-y-1/2 right-px text-sm opacity-50 hover:opacity-100 hover:text-error" />
							</template>
						</div>
						<template v-else>{{ s.nummer }}</template>
					</div>
				</div>
				<div role="row" class="svws-ui-tr">
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
				<div role="row" class="svws-ui-tr">
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
				<div role="row" class="svws-ui-tr">
					<div role="columnheader" class="svws-ui-td svws-align-center" aria-label="Alle auswählen">
						<svws-ui-checkbox :model-value="getDatenmanager().kursGetAnzahl() === getKursauswahl().size" :indeterminate="getKursauswahl().size > 0 && getKursauswahl().size < getDatenmanager().kursGetAnzahl()"
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
								<div :key="schiene.id" @click="openModalRegelKursartSchiene(schiene)" class="select-none cursor-pointer text-center" :class="{'cursor-grabbing': dragDataKursartSchiene !== undefined}" :draggable="true" @dragstart="dragSchieneStarted(schiene)" @dragend="dragSchieneEnded">
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
								:config="config" :fachwahlen="fachwahlen" :kursart="kursart" :get-kursauswahl="getKursauswahl"
								:faecher-manager="faecherManager" :get-datenmanager="getDatenmanager" :hat-ergebnis="hatErgebnis" :get-ergebnismanager="getErgebnismanager"
								:map-lehrer="mapLehrer" :allow-regeln="allow_regeln" :schueler-filter="schuelerFilter"
								:fachwahlen-anzahl="getAnzahlFachwahlen(fachwahlen, kursart)"
								:add-regel="addRegel" :remove-regel="removeRegel" :patch-regel="patchRegel" :update-kurs-schienen-zuordnung="updateKursSchienenZuordnung"
								:patch-kurs="patchKurs" :add-kurs="addKurs" :remove-kurse="removeKurse" :add-kurs-lehrer="addKursLehrer"
								:remove-kurs-lehrer="removeKursLehrer" :add-schiene-kurs="addSchieneKurs" :remove-schiene-kurs="removeSchieneKurs" :split-kurs="splitKurs" :combine-kurs="combineKurs"
								:drag-data-kurs-schiene="() => dragDataKursSchiene" :drop-data-kurs-schiene="()=>dropDataKursSchiene" :is-selected-kurse="isSelectedKurse"
								:selected-do="selectedDo"
								:on-drag-kurs-schiene="dragKursSchiene" :on-drop-kurs-schiene="dropKursSchiene" />
						</template>
					</template>
				</template>
				<template v-else>
					<template v-for="kursart in GostKursart.values()" :key="kursart.id">
						<template v-for="fachwahlen in mapFachwahlStatistik().values()" :key="fachwahlen.id">
							<s-gost-kursplanung-kursansicht-fachwahl v-if="istFachwahlVorhanden(fachwahlen, kursart).value"
								:config="config" :fachwahlen="fachwahlen" :kursart="kursart" :get-kursauswahl="getKursauswahl"
								:faecher-manager="faecherManager" :get-datenmanager="getDatenmanager" :hat-ergebnis="hatErgebnis" :get-ergebnismanager="getErgebnismanager"
								:map-lehrer="mapLehrer" :allow-regeln="allow_regeln" :schueler-filter="schuelerFilter"
								:fachwahlen-anzahl="getAnzahlFachwahlen(fachwahlen, kursart)"
								:add-regel="addRegel" :remove-regel="removeRegel" :patch-regel="patchRegel" :update-kurs-schienen-zuordnung="updateKursSchienenZuordnung"
								:patch-kurs="patchKurs" :add-kurs="addKurs" :remove-kurse="removeKurse" :add-kurs-lehrer="addKursLehrer"
								:remove-kurs-lehrer="removeKursLehrer" :add-schiene-kurs="addSchieneKurs" :remove-schiene-kurs="removeSchieneKurs" :split-kurs="splitKurs" :combine-kurs="combineKurs"
								:drag-data-kurs-schiene="() => dragDataKursSchiene" :drop-data-kurs-schiene="()=>dropDataKursSchiene" :is-selected-kurse="isSelectedKurse"
								:selected-do="selectedDo"
								:on-drag-kurs-schiene="dragKursSchiene" :on-drop-kurs-schiene="dropKursSchiene" />
						</template>
					</template>
				</template>
			</template>
		</svws-ui-table>
		<s-gost-kursplanung-kursansicht-modal-regel-schienen :add-regel="addRegel" ref="modalRegelKursartSchienen" />
		<s-gost-kursplanung-kursansicht-modal-regel-kurse :get-datenmanager="getDatenmanager" :add-regel="addRegel" ref="modal_regel_kurse" />
		<s-gost-kursplanung-kursansicht-modal-combine-kurse :get-datenmanager="getDatenmanager" :combine-kurs="combineKurs" ref="modal_combine_kurse" />
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { ComputedRef, Ref, WritableComputedRef } from "vue";
	import { computed, onMounted, ref, toRaw } from "vue";
	import type { DataTableColumn } from "@ui";
	import type { GostBlockungKurs, GostBlockungKursLehrer, GostBlockungRegel, GostBlockungSchiene, GostBlockungsdatenManager, GostBlockungsergebnisKurs, GostBlockungsergebnisManager, GostFach, GostFaecherManager, GostHalbjahr, GostStatistikFachwahl, LehrerListeEintrag, List } from "@core";
	import { ArrayList, DeveloperNotificationException, GostKursart, GostStatistikFachwahlHalbjahr, ZulaessigesFach } from "@core";
	import type { SGostKursplanungKursansichtDragData } from "./kursansicht/SGostKursplanungKursansichtFachwahlProps";
	import type { GostKursplanungSchuelerFilter } from "./GostKursplanungSchuelerFilter";
	import type { Config } from "~/components/Config";

	const props = defineProps<{
		getDatenmanager: () => GostBlockungsdatenManager;
		getKursauswahl: () => Set<number>,
		getErgebnismanager: () => GostBlockungsergebnisManager;
		addRegel: (regel: GostBlockungRegel) => Promise<GostBlockungRegel | undefined>;
		patchRegel: (data: GostBlockungRegel, id: number) => Promise<void>;
		removeRegel: (id: number) => Promise<GostBlockungRegel | undefined>;
		addRegeln: (regeln: List<GostBlockungRegel>) => Promise<void>;
		removeRegeln: (regeln: List<GostBlockungRegel>) => Promise<void>;
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
		config: Config;
		hatErgebnis: boolean;
		schuelerFilter: () => GostKursplanungSchuelerFilter | undefined;
		faecherManager: GostFaecherManager;
		halbjahr: GostHalbjahr;
		mapLehrer: Map<number, LehrerListeEintrag>;
		mapFachwahlStatistik: () => Map<number, GostStatistikFachwahl>;
		blockungstabelleVisible: boolean;
		toggleBlockungstabelle: () => void;
	}>();

	const edit_schienenname: Ref<number|undefined> = ref()

	const istFachwahlVorhanden = (fachwahlen: GostStatistikFachwahl, kursart: GostKursart) : ComputedRef<boolean> => computed(() => {
		const anzahl = props.getDatenmanager().kursGetListeByFachUndKursart(fachwahlen.id, kursart.id).size();
		return (anzahl > 0) || (allow_regeln.value && getAnzahlFachwahlen(fachwahlen, kursart) > 0);
	});

	const schienen: ComputedRef<List<GostBlockungSchiene>> = computed(() => props.getDatenmanager().schieneGetListe());

	const allow_regeln: ComputedRef<boolean> = computed(() => (props.getDatenmanager().ergebnisGetListeSortiertNachBewertung().size() === 1));

	function calculateColumns() {
		const cols: DataTableColumn[] = [];
		cols.push({ key: "auswahl", label: "Kursauswahl", fixedWidth: 1.5, align: 'center' });
		if (allow_regeln.value) {
			cols.push({ key: "actions", label: "Actions", fixedWidth: 1.5, align: 'center' });
		}
		cols.push({ key: "kurs", label: "Kurs", span: 1.75, minWidth: 8 },
			{ key: "lehrer", label: "Lehrer", span: 1.5, minWidth: 6 },
			{ key: "koop", label: "Kooperation", align: 'center', fixedWidth: 3.75 },
			{ key: "FW", label: "Fachwahl", align: 'center', fixedWidth: 3.75 },
			{ key: "Diff", label: "Diff", align: 'center', fixedWidth: 3.75 });
		for (let i = 0; i < schienen.value.size(); i++) {
			cols.push({ key: "schiene_" + (i+1), label: "schiene_" + (i+1), fixedWidth: 3.75, align: 'center' });
		}
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
		const allSelected = (props.getDatenmanager().kursGetAnzahl() === auswahl.size);
		if (allSelected) {
			auswahl.clear();
		} else {
			for (const kurs of props.getDatenmanager().kursGetListeSortiertNachFachKursartNummer())
				auswahl.add(kurs.id);
		}
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
	onMounted(() => {
		isMounted.value = true;
	});

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

	const dragDataKursartSchiene = ref<{ schiene: GostBlockungSchiene } | undefined>(undefined);

	const modalRegelKursartSchienen = ref();

	const istDropZoneSchiene = (schiene: GostBlockungSchiene) : ComputedRef<boolean> => computed(() => {
		return (dragDataKursartSchiene.value !== undefined && (dragDataKursartSchiene.value.schiene.id !== schiene.id));
	});

	function openModalRegelKursartSchiene(schiene: GostBlockungSchiene) {
		const andereSchiene = (dragDataKursartSchiene.value === undefined) ? schiene : dragDataKursartSchiene.value.schiene;
		modalRegelKursartSchienen.value.openModal(andereSchiene, schiene);
	}

	function dragSchieneStarted(schiene: GostBlockungSchiene) {
		dragDataKursartSchiene.value = { schiene };
	}

	function dragSchieneEnded() {
		dragDataKursartSchiene.value = undefined;
	}


	const dragDataKursSchiene = ref<SGostKursplanungKursansichtDragData>(undefined);
	const dropDataKursSchiene = ref<SGostKursplanungKursansichtDragData>(undefined);

	const modal_regel_kurse = ref();
	const modal_combine_kurse = ref();


	function dragKursSchiene(data: SGostKursplanungKursansichtDragData) {
		dragDataKursSchiene.value = data;
		dropDataKursSchiene.value = undefined;
	}

	async function dropKursSchiene(zone: SGostKursplanungKursansichtDragData) {
		if ((zone === undefined) || (dragDataKursSchiene.value === undefined)) {
			if (dropDataKursSchiene.value !== undefined)
				dropDataKursSchiene.value = undefined;
			return;
		}
		if (!props.getErgebnismanager().getOfKursOfSchieneIstZugeordnet(dragDataKursSchiene.value.kurs.id, dragDataKursSchiene.value.schiene.id)) {
			dropDataKursSchiene.value = zone;
			return;
		}
		if (dragDataKursSchiene.value.kurs.id !== zone.kurs.id) {
			const schienen = props.getErgebnismanager().getOfKursSchienenmenge(zone.kurs.id);
			if (schienen.contains(zone.schiene))
				modal_combine_kurse.value.openModal(dragDataKursSchiene.value.kurs, zone.kurs);
			else
				modal_regel_kurse.value.openModal(dragDataKursSchiene.value.kurs.id, zone.kurs.id);
			return;
		}
		if ((dragDataKursSchiene.value.kurs.id === zone.kurs.id) && (!props.getErgebnismanager().getOfKursOfSchieneIstZugeordnet(zone.kurs.id, zone.schiene.id)) ) {
			// Entferne potentielle Fixierung beim Verschieben.
			if (allow_regeln.value && props.getDatenmanager().kursGetHatFixierungInSchiene(dragDataKursSchiene.value.kurs.id, zone.schiene.id)) {
				const regel = props.getDatenmanager().kursGetRegelFixierungInSchiene(zone.kurs.id, zone.schiene.id);
				await props.removeRegel(regel.id);
			}
			await props.updateKursSchienenZuordnung(dragDataKursSchiene.value.kurs.id, dragDataKursSchiene.value.schiene.id, zone.schiene.id);
		}
	}

	const isSelectedKurse = computed(() => {
		const k1 = dragDataKursSchiene.value?.kurs;
		const k2 = dropDataKursSchiene.value?.kurs;
		if (k1 === undefined || k2 === undefined)
			return [];
		let goon = false;
		const range = [];
		const list = props.kurssortierung.value === 'fach'
			? props.getDatenmanager().kursGetListeSortiertNachFachKursartNummer()
			: props.getDatenmanager().kursGetListeSortiertNachKursartFachNummer()
		const iK1 = list.indexOf(toRaw(k1));
		const iK2 = list.indexOf(toRaw(k2));
		const kMin = iK1 <= iK2 ? k1 : k2;
		const kMax = iK2 > iK1 ? k2 : k1;
		for (const k of list) {
			if (k.id === kMin.id)
				goon = true;
			if (k.id === kMax.id) {
				range.push(k);
				break;
			}
			if (goon)
				range.push(k);
		}
		return range;
	})

	async function selectedDo(action: 'kurse fixieren'| 'kurse lösen' | 'toggle kurse' | 'schienen sperren' | 'schienen entsperren' | 'toggle schienen' | 'schüler fixieren' | 'schüler lösen' | 'toggle schüler') {
		if (dragDataKursSchiene.value === undefined || dropDataKursSchiene.value === undefined)
			throw new DeveloperNotificationException("Es wurden keine gültigen Daten für diese Aktion gefunden");
		const list = props.kurssortierung.value === 'fach'
			? props.getDatenmanager().kursGetListeSortiertNachFachKursartNummer()
			: props.getDatenmanager().kursGetListeSortiertNachKursartFachNummer()
		const k1 = toRaw(dragDataKursSchiene.value.kurs);
		const k2 = toRaw(dropDataKursSchiene.value.kurs);
		const s1 = props.getErgebnismanager().getSchieneG(dragDataKursSchiene.value.schiene.id);
		const s2 = props.getErgebnismanager().getSchieneG(dropDataKursSchiene.value.schiene.id);
		const regeln: List<GostBlockungRegel> = new ArrayList();
		switch (action) {
			case 'schüler fixieren':
			case 'schüler lösen':
			case 'toggle schüler':
				regeln.addAll(props.getErgebnismanager().regelGetListeToggleSchuelerfixierung(list, k1, k2, s1, s2));
				break;
			case 'kurse fixieren':
			case 'kurse lösen':
			case 'toggle kurse':
				regeln.addAll(props.getErgebnismanager().regelGetListeToggleKursfixierung(list, k1, k2, s1, s2));
				break;
			case 'schienen sperren':
			case 'schienen entsperren':
			case 'toggle schienen':
				regeln.addAll(props.getErgebnismanager().regelGetListeToggleSperrung(list, k1, k2, s1, s2));
				break;
		}
		const add: List<GostBlockungRegel> = new ArrayList();
		const remove: List<GostBlockungRegel> = new ArrayList();
		for (const regel of regeln)
			regel.id > 0 ? remove.add(regel) : add.add(regel);
		if (['schüler fixieren', 'kurse fixieren', 'schienen sperren', 'toggle schüler', 'toggle kurse', 'toggle schienen'].includes(action))
			await props.addRegeln(add);
		if ([ 'kurse lösen', 'schüler lösen', 'schienen entsperren', 'toggle schüler', 'toggle kurse', 'toggle schienen'].includes(action))
			await props.removeRegeln(remove);
		dragDataKursSchiene.value = undefined;
		dropDataKursSchiene.value = undefined;
		return regeln;
	}

</script>
