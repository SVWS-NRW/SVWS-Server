<template>
	<svws-ui-content-card v-if="schueler !== undefined" class="min-w-[30rem]">
		<!-- Anzeige der Umwahlansicht, falls Fächer belegt wurden ... -->
		<div class="flex gap-4 -mt-2" v-if="fachbelegungen.size() > 0">
			<!-- Übersicht über die Fachwahlen des Schülers -->
			<div class="w-1/6 min-w-[9rem]">
				<!-- der Drop-Bereich für den Mülleimer von Kurs-Schülerzuordnung - dieser umfasst auch die Fachwahlliste -->
				<div class="mb-4" @dragover="$event.preventDefault()" @drop="drop_entferne_kurszuordnung">
					<div class="border-2 -m-[2px]" :class="{ 'border-dashed border-error': dragAndDropData !== undefined, 'border-transparent': dragAndDropData === undefined }">
						<!-- Die Liste mit den Fachwahlen -->
						<svws-ui-data-table :items="[]" :no-data="false" :disable-header="true">
							<template #body>
								<div v-for="fach in fachbelegungen" :key="fach.fachID" role="row" class="data-table__tr data-table__thead__tr" :class="{ 'text-error font-medium': (fachwahlKurszuordnung(fach.fachID, schueler.id).value === undefined) }">
									<div role="cell" :key="fachwahlKurszuordnung(fach.fachID, schueler.id).value?.id"
										:draggable="(fachwahlKurszuordnung(fach.fachID, schueler.id).value === undefined) && (!blockung_aktiv)"
										@dragstart="drag_started(fachwahlKurszuordnung(fach.fachID, schueler.id).value?.id, fach.fachID, fachwahlKursart(fach.fachID, schueler.id).value.id)"
										@dragend="drag_ended()"
										class="select-none data-table__td group"
										:class="{
											'bg-white' : (fachwahlKurszuordnung(fach.fachID, schueler.id).value !== undefined),
											'cursor-grab' : (fachwahlKurszuordnung(fach.fachID, schueler.id).value === undefined) && (!blockung_aktiv)
										}"
										:style="{ 'background-color': bgColorFachwahl(fach.fachID, schueler.id).value }">
										<div class="flex items-center justify-between gap-1 w-full">
											<div class="inline-flex items-center gap-1">
												<span class="group-hover:bg-light rounded w-3 -ml-1">
													<i-ri-draggable class="w-5 -ml-1 text-black opacity-40 group-hover:opacity-100 group-hover:text-black" v-if="(fachwahlKurszuordnung(fach.fachID, schueler.id).value === undefined) && (!blockung_aktiv)" />
												</span>
												<span>{{ getFachwahlKursname(fach.fachID, schueler.id) }}</span>
											</div>
											<div class="flex items-center gap-1 cursor-pointer">
												<svws-ui-tooltip v-if="!fachwahlKurszuordnung(fach.fachID, schueler.id).value"> <i-ri-forbid-2-line /> <template #content> <span>Nichtverteilt</span> </template> </svws-ui-tooltip>
											</div>
										</div>
									</div>
								</div>
							</template>
						</svws-ui-data-table>

						<!-- Der "Mülleimer für das Ablegen von Kursen, bei denen die Kurs-Schüler-Zuordnung aufgehoben werden soll. " -->
						<template v-if="!blockung_aktiv">
							<div class="flex items-center py-2 px-3 m-1" :class="{'bg-error text-white': dragAndDropData !== undefined}">
								<div v-if="dragAndDropData !== undefined" class="flex gap-2 items-center w-full h-full">
									<i-ri-delete-bin-line class="w-6 h-6" :class="{ 'bg-error': is_dragging }" />
									<span>Entfernen</span>
								</div>
								<div v-else class="flex gap-2 items-center w-full h-full">
									<i-ri-delete-bin-line class="w-6 h-6 opacity-25" :class="{ 'bg-error': is_dragging }" />
									<span class="opacity-25">Entfernen</span>
								</div>
							</div>
						</template>
					</div>
				</div>

				<!-- Ein Knopf zum Verwerfen der alten Verteilung beim Schüler und für eine Neuzuordnung des Schülers zu den Kursen -->
				<div class="flex flex-col gap-2">
					<svws-ui-button class="w-full justify-center" type="secondary" @click="auto_verteilen" :disabled="apiStatus.pending" title="Automatisch verteilen" v-if="!blockung_aktiv">Verteilen<i-ri-sparkling-line /></svws-ui-button>
					<svws-ui-button class="w-full justify-center" type="secondary" @click="routeLaufbahnplanung()" :title="`Zur Laufbahnplanung von ${schueler.vorname + ' ' + schueler.nachname}`"> Laufbahn <i-ri-group-line /></svws-ui-button>
				</div>
			</div>

			<!-- Übersicht über die Kurs-Schienen-Zuordnung für den Schüler -->
			<div class="flex-grow">
				<svws-ui-data-table :items="[]" :columns="cols" :disable-header="true" :disable-footer="true" :no-data="false">
					<template #header><div /></template>
					<template #body>
						<div v-for="(schiene, index) in getErgebnismanager().getMengeAllerSchienen()" :key="index"
							role="row" class="data-table__tr data-table__thead__tr" :class="{ 'border border-error': hatSchieneKollisionen(schiene.id, schueler.id).value }">
							<!-- Informationen zu der Schiene und der Statistik dazu auf der linken Seite der Tabelle -->
							<div role="cell" class="data-table__td" :class="{ 'text-error': hatSchieneKollisionen(schiene.id, schueler.id).value }">
								<div class="flex flex-col py-1" :title="getErgebnismanager().getSchieneG(schiene.id).bezeichnung">
									<span class="font-medium inline-flex items-center gap-1 text-base">
										<svws-ui-tooltip v-if="hatSchieneKollisionen(schiene.id, schueler.id).value">
											<i-ri-alert-line />
											<template #content>
												<span>Kollision in dieser Schiene</span>
											</template>
										</svws-ui-tooltip>
										<span class="line-clamp-1">{{ getErgebnismanager().getSchieneG(schiene.id).bezeichnung }}</span>
									</span>
									<span class="text-sm font-medium opacity-50">{{ schiene.kurse.size() }} Kurs{{ schiene.kurse.size() === 1 ? '' : 'e' }}</span>
									<span class="text-sm font-medium opacity-50">{{ getErgebnismanager().getOfSchieneAnzahlSchueler(schiene.id) }} Schüler</span>
								</div>
							</div>

							<!-- Die Liste der Schüler-Kurse (von links nach rechts), welche der Schiene zugeordnet sind (stehen ggf. für drag und/oder drop zur Verfügung). -->
							<div role="cell" v-for="kurs of schiene.kurse" :key="kurs.id"
								class="data-table__td data-table__td__align-center data-table__td__no-padding select-none group relative p-0.5"
								:class="{ 'is-drop-zone': is_drop_zone(kurs).value, 'cursor-grab': is_draggable(kurs.id, schueler.id).value }"
								:draggable="is_draggable(kurs.id, schueler.id).value"
								@dragstart="drag_started(kurs.id, kurs.fachID, kurs.kursart)"
								@dragend="drag_ended()">
								<div class="w-full h-full flex flex-col justify-center items-center rounded" :style="{ 'background-color': bgColor(kurs.id, schueler.id) }"
									@dragover="if (is_drop_zone(kurs).value) $event.preventDefault();" @drop="drop_aendere_kurszuordnung(kurs, schueler.id)">
									<span class="group-hover:bg-white rounded w-3 absolute top-1 left-1" v-if="is_draggable(kurs.id, schueler.id).value">
										<i-ri-draggable class="w-5 -ml-1 text-black opacity-25 group-hover:opacity-100 group-hover:text-black" />
									</span>
									<span class="text-sm opacity-50 relative" title="Schriftlich/Insgesamt im Kurs">
										{{ getErgebnismanager().getOfKursAnzahlSchuelerSchriftlich(kurs.id) }}/{{ kurs.schueler.size() }}
									</span>
									<span class="py-0.5 font-medium">{{ getErgebnismanager().getOfKursName(kurs.id) }}</span>
									<span class="inline-flex items-center gap-1">
										<span v-if="(allow_regeln && fach_gewaehlt(schueler.id, kurs).value && !getDatenmanager().daten().istAktiv)">
											<span class="icon cursor-pointer" @click.stop="verbieten_regel_toggle(kurs.id, schueler.id)" :title="verbieten_regel(kurs.id, schueler.id).value ? 'Verboten' : 'Verbieten'">
												<i-ri-forbid-fill v-if="verbieten_regel(kurs.id, schueler.id).value" class="inline-block" />
												<i-ri-forbid-line v-if="!verbieten_regel(kurs.id, schueler.id).value && !fixier_regel(kurs.id, schueler.id).value" class="inline-block" />
											</span>
											<span class="icon cursor-pointer" @click.stop="fixieren_regel_toggle(kurs.id, schueler.id)" :title="fixier_regel(kurs.id, schueler.id).value ? 'Fixiert' : 'Fixieren'">
												<i-ri-pushpin-fill v-if="fixier_regel(kurs.id, schueler.id).value" class="inline-block" />
												<i-ri-pushpin-line v-if="!verbieten_regel(kurs.id, schueler.id).value && !fixier_regel(kurs.id, schueler.id).value" class="inline-block" />
											</span>
										</span>
										<span v-else>
											<span class="icon" title="Verboten"> <i-ri-forbid-fill v-if="verbieten_regel(kurs.id, schueler.id).value" class="inline-block" /> </span>
											<span class="icon" title="Fixiert"> <i-ri-pushpin-fill v-if="fixier_regel(kurs.id, schueler.id).value" class="inline-block" /> </span>
										</span>
									</span>
								</div>
							</div>

							<!-- Auffüllen mit leeren Zellen, falls in der Schiene nicht die maximale Anzahl von Kursen vorliegt. -->
							<div v-for="n in (maxKurseInSchienen - schiene.kurse.size())" :key="n" role="cell" class="data-table__td" />
						</div>
					</template>
				</svws-ui-data-table>
			</div>
		</div>

		<!-- ... ansonsten keine Anzeige der Umwahlansicht, falls keine Fächer belegt wurden -->
		<div v-else class="opacity-50">
			Keine Fachbelegungen vorhanden.
		</div>
	</svws-ui-content-card>
</template>


<script setup lang="ts">

	import type { GostKursart, GostBlockungsergebnisKurs, GostFachwahl, List } from "@core";
	import { ArrayList, ZulaessigesFach, GostKursblockungRegelTyp, GostBlockungRegel } from "@core";
	import type { ComputedRef, Ref} from "vue";
	import { computed, ref } from "vue";
	import type { GostUmwahlansichtProps } from "./SCardGostUmwahlansichtProps";
	import type {DataTableColumn} from "@ui";

	type DndData = { id: number | undefined, fachID: number, kursart: number };

	const props = defineProps<GostUmwahlansichtProps>();

	const is_dragging: Ref<boolean> = ref(false);

	const dragAndDropData: Ref<DndData | undefined> = ref(undefined);

	const allow_regeln: ComputedRef<boolean> = computed(() => props.getDatenmanager().ergebnisGetListeSortiertNachBewertung().size() === 1);

	const blockung_aktiv: ComputedRef<boolean> = computed(() => props.getDatenmanager().daten().istAktiv);

	async function auto_verteilen() {
		if (props.schueler !== undefined)
			await props.autoKursSchuelerZuordnung(props.schueler.id);
	}

	const fachbelegungen: ComputedRef<List<GostFachwahl>> = computed(() => {
		if (props.schueler === undefined)
			return new ArrayList<GostFachwahl>();
		return props.getDatenmanager().schuelerGetListeOfFachwahlen(props.schueler.id);
	});

	function routeLaufbahnplanung() {
		if (props.schueler !== undefined)
			void props.gotoLaufbahnplanung(props.schueler.id);
	}

	const maxKurseInSchienen: ComputedRef<number> = computed(() => {
		return props.getErgebnismanager().getOfSchieneMaxKursanzahl();
	});

	function calculateColumns(): DataTableColumn[] {
		const cols: Array<DataTableColumn> = [{ key: "schiene", label: "Schiene", minWidth: 9, span: 0.1 }];
		for (let i = 0; i < maxKurseInSchienen?.value; i++)
			cols.push({ key: "kurs_" + (i+1), label: "Kurs " + (i+1), align: 'center', minWidth: 6 });
		return cols;
	}

	const cols: ComputedRef<DataTableColumn[]> = computed(() => calculateColumns());

	const hatSchieneKollisionen = (idSchiene: number, idSchueler: number) : ComputedRef<boolean> => computed(() =>
		props.getErgebnismanager().getOfSchuelerOfSchieneHatKollision(idSchueler, idSchiene)
	);


	const is_draggable = (idKurs: number, idSchueler: number) : ComputedRef<boolean> => computed(() => {
		if (props.apiStatus.pending || props.getDatenmanager().daten().istAktiv)
			return false;
		return props.getErgebnismanager().getOfSchuelerOfKursIstZugeordnet(idSchueler, idKurs);
	});

	function dropAllowTrash(e: DragEvent) {
		e.preventDefault();
	}

	const is_drop_zone = (kurs: GostBlockungsergebnisKurs) : ComputedRef<boolean> => computed(() => {
		if (dragAndDropData.value === undefined)
			return false
		const { id, fachID, kursart } = dragAndDropData.value;
		if ((id != undefined) && (id === kurs.id))
			return false;
		if ((fachID !== kurs.fachID) || (kursart !== kurs.kursart))
			return false;
		return true;
	});

	function drag_started(idKurs : number | undefined, idFach: number, kursart: number) {
		dragAndDropData.value = { id: idKurs, fachID: idFach, kursart: kursart };
	}

	function drag_ended() {
		dragAndDropData.value = undefined;
	}

	async function drop_aendere_kurszuordnung(kurs_neu: GostBlockungsergebnisKurs, idSchueler : number) {
		const kurs_alt = dragAndDropData.value;
		if (kurs_alt === undefined)
			return;
		if (!is_drop_zone(kurs_neu).value)
			return;
		await props.updateKursSchuelerZuordnung(idSchueler, kurs_neu.id, kurs_alt.id);
		drag_ended();
	}

	async function drop_entferne_kurszuordnung(e: DragEvent) {
		const schuelerid = props.schueler?.id;
		const obj = dragAndDropData.value;
		if ((schuelerid === undefined) || (obj === undefined) || (obj.id === undefined))
			return;
		await props.removeKursSchuelerZuordnung(schuelerid, obj.id);
	}

	function bgColor(idKurs : number, idSchueler : number) : string {
		if (props.getErgebnismanager().getOfSchuelerOfKursIstZugeordnet(idSchueler, idKurs)) {
			const k = props.getDatenmanager().kursGet(idKurs);
			const f = props.getDatenmanager().faecherManager().get(k.fach_id);
			const zf = ZulaessigesFach.getByKuerzelASD(f?.kuerzel || null)
			return zf.getHMTLFarbeRGB();
		}
		return "";
	}

	const fach_gewaehlt = (idSchueler: number, kurs: GostBlockungsergebnisKurs): ComputedRef<boolean> => computed(() =>
		props.getErgebnismanager().getOfSchuelerHatFachwahl(idSchueler, kurs.fachID, kurs.kursart)
	);

	const fixier_regel = (idKurs: number, idSchueler: number) : ComputedRef<number | undefined> => computed(() => {
		if (props.getDatenmanager().schuelerGetIstFixiertInKurs(idSchueler, idKurs))
			return props.getDatenmanager().schuelerGetRegelFixiertInKurs(idSchueler, idKurs).id;
		return undefined;
	});

	const verbieten_regel = (idKurs: number, idSchueler: number) : ComputedRef<number | undefined> => computed(() => {
		if (props.getDatenmanager().schuelerGetIstVerbotenInKurs(idSchueler, idKurs))
			return props.getDatenmanager().schuelerGetRegelVerbotenInKurs(idSchueler, idKurs).id;
		return undefined;
	});

	const fixieren_regel_toggle = (idKurs: number, idSchueler: number) => fixier_regel(idKurs, idSchueler).value ? fixieren_regel_entfernen(idKurs, idSchueler) : fixieren_regel_hinzufuegen(idKurs, idSchueler);

	const verbieten_regel_toggle = (idKurs: number, idSchueler: number) => verbieten_regel(idKurs, idSchueler).value ? verbieten_regel_entfernen(idKurs, idSchueler) : verbieten_regel_hinzufuegen(idKurs, idSchueler);

	const regel_speichern = async (regel: GostBlockungRegel, idKurs: number, idSchueler: number) => {
		regel.parameter.add(idSchueler);
		regel.parameter.add(idKurs);
		await props.addRegel(regel);
	}

	const fixieren_regel_hinzufuegen = async (idKurs: number, idSchueler: number) => {
		const regel = new GostBlockungRegel();
		regel.typ = GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ;
		await regel_speichern(regel, idKurs, idSchueler);
	}

	const fixieren_regel_entfernen = async (idKurs: number, idSchueler: number) => {
		const idRegel = fixier_regel(idKurs, idSchueler).value;
		if (idRegel === undefined)
			return
		await props.removeRegel(idRegel);
	}

	const verbieten_regel_hinzufuegen = async (idKurs: number, idSchueler: number) => {
		const regel = new GostBlockungRegel();
		regel.typ = GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ;
		await regel_speichern(regel, idKurs, idSchueler);
	}

	const verbieten_regel_entfernen = async (idKurs: number, idSchueler: number) => {
		const idRegel = verbieten_regel(idKurs, idSchueler).value;
		if (idRegel === undefined)
			return
		await props.removeRegel(idRegel);
	}


	const fachwahlKurszuordnung = (idFach: number, idSchueler: number) : ComputedRef<GostBlockungsergebnisKurs | undefined> => computed(() =>
		props.getErgebnismanager().getOfSchuelerOfFachZugeordneterKurs(idSchueler, idFach) || undefined
	);

	const fachwahlKursart = (idFach: number, idSchueler: number) : ComputedRef<GostKursart> => computed(() =>
		props.getErgebnismanager().getOfSchuelerOfFachKursart(idSchueler, idFach)
	);

	const bgColorFachwahl = (idFach: number, idSchueler: number) : ComputedRef<string> =>  computed(() => {
		const fwKurszuordnung = fachwahlKurszuordnung(idFach, idSchueler).value;
		if (fwKurszuordnung !== undefined)
			return "white";
		const f = props.getErgebnismanager().getFach(idFach);
		const zulfach = ZulaessigesFach.getByKuerzelASD(f.kuerzel);
		if (!zulfach)
			return "#ffffff";
		return zulfach.getHMTLFarbeRGB();
	});

	function getFachwahlKursname(idFach: number, idSchueler: number): string {
		const fw = fachwahlKurszuordnung(idFach, idSchueler).value;
		if (fw === undefined) {
			const f = props.getErgebnismanager().getFach(idFach);
			const fwKursart = fachwahlKursart(idFach, idSchueler).value;
			return f.kuerzelAnzeige + "-" + fwKursart.kuerzel || "?";
		}
		return props.getErgebnismanager().getOfKursName(fw.id);
	}

</script>


<style lang="postcss" scoped>

	.is-drop-zone {
		@apply relative bg-primary/5;

		&:before {
			content: '';
			@apply absolute inset-1 border-2 border-dashed border-primary pointer-events-none;
		}
	}

</style>
