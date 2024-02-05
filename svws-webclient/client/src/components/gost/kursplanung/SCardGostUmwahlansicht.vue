<template>
	<svws-ui-content-card v-if="hatBlockung && hatErgebnis && (schueler !== undefined)" class="min-w-[42rem] w-fit" overflow-scroll>
		<template #title>
			<svws-ui-button type="icon" @click="routeLaufbahnplanung()" :title="`Zur Laufbahnplanung von ${schueler.vorname + ' ' + schueler.nachname}`" size="small" class="-ml-3 mr-0.5"><i-ri-link /></svws-ui-button>
			<span class="text-headline-md">{{ schueler.vorname }} {{ schueler.nachname }}</span>
		</template>
		<!-- Anzeige der Umwahlansicht, falls Fächer belegt wurden ... -->
		<div class="flex gap-12" v-if="fachbelegungen.size() > 0">
			<!-- Übersicht über die Fachwahlen des Schülers -->
			<div class="w-1/6 min-w-[10rem]">
				<!-- der Drop-Bereich für den Mülleimer von Kurs-Schülerzuordnung - dieser umfasst auch die Fachwahlliste -->
				<div class="mb-5" @dragover="$event.preventDefault()" @drop="drop_entferne_kurszuordnung">
					<div>
						<!-- Die Liste mit den Fachwahlen -->
						<svws-ui-table :items="[]" :no-data="false" :disable-header="true" type="navigation" has-background class="mt-1">
							<template #body>
								<div v-for="fach in fachbelegungen" :key="fach.fachID" role="row" class="svws-ui-tr !w-full" :class="{ 'font-medium': (fachwahlKurszuordnung(fach.fachID, schueler.id).value === undefined) }">
									<div role="cell" :key="fachwahlKurszuordnung(fach.fachID, schueler.id).value?.id"
										:draggable="(fachwahlKurszuordnung(fach.fachID, schueler.id).value === undefined)"
										@dragstart="drag_started(fachwahlKurszuordnung(fach.fachID, schueler.id).value?.id, fach.fachID, fachwahlKursart(fach.fachID, schueler.id).value.id)"
										@dragend="drag_ended()"
										class="select-none svws-ui-td svws-no-padding group -my-1 !py-0.5"
										:class="{
											'cursor-grab' : (fachwahlKurszuordnung(fach.fachID, schueler.id).value === undefined),
											'opacity-50' : (fachwahlKurszuordnung(fach.fachID, schueler.id).value !== undefined)
										}"
										:style="{ '--background-color': bgColorFachwahl(fach.fachID, schueler.id).value }">
										<div class="svws-ui-badge w-auto flex-grow -mx-3 py-0.5 !my-0 !h-full items-center">
											<template v-if="fachwahlKurszuordnung(fach.fachID, schueler.id).value === undefined">
												<span class="rounded-sm w-3 -my-0.5 group-hover:bg-white/50">
													<i-ri-draggable class="w-4 -ml-0.5 -mr-1 text-black opacity-50 group-hover:opacity-100" />
												</span>
												<span class="ml-0.5">{{ getFachwahlKursname(fach.fachID, schueler.id) }}</span>
											</template>
											<template v-else>
												<span class="opacity-75 inline-block w-3 text-sm">&nbsp;</span>
												{{ getFachwahlKursname(fach.fachID, schueler.id) }}
											</template>
											<span v-if="getDatenmanager().schuelerGetOfFachFachwahl(schueler.id, fach.fachID).abiturfach !== null">
												<span class="text-sm ml-2 mr-2">—</span>
												<span>AB{{ getDatenmanager().schuelerGetOfFachFachwahl(schueler.id, fach.fachID).abiturfach }}</span>
											</span>
										</div>
									</div>
								</div>
							</template>
						</svws-ui-table>
						<!-- Ein Knopf zum Verwerfen der alten Verteilung beim Schüler und für eine Neuzuordnung des Schülers zu den Kursen -->
						<svws-ui-button type="transparent" @click="auto_verteilen" :disabled="apiStatus.pending" title="Automatisch verteilen und aktuelle Zuordnung verwerfen" class="-ml-3"><i-ri-sparkling-line />Verteilen</svws-ui-button>
						<!-- Der "Mülleimer für das Ablegen von Kursen, bei denen die Kurs-Schüler-Zuordnung aufgehoben werden soll. " -->
						<div class="mt-5 pr-3 py-4 border-2 -mx-3 rounded-xl border-dashed border-black/10 dark:border-white/10" :class="[dragAndDropData === undefined ? 'border-black/10 dark:border-white/10' : 'border-error ring-4 ring-error/10']">
							<div class="flex items-center gap-2 justify-center" :class="[dragAndDropData === undefined ? 'opacity-25' : 'opacity-100']">
								<i-ri-delete-bin-line class="text-headline flex-shrink-0" :class="[dragAndDropData === undefined ? 'opacity-50' : 'text-error']" />
								<span class="text-sm w-2/3">
									<template v-if="dragAndDropData === undefined">Kurse hier zum<br>Löschen ablegen</template>
									<template v-else>Kurs-Zuordnung<br>aufheben</template>
								</span>
							</div>
						</div>
					</div>
				</div>
			</div>

			<!-- Übersicht über die Kurs-Schienen-Zuordnung für den Schüler -->
			<div class="flex-grow">
				<svws-ui-table :items="[]" :columns="cols" :disable-header="true" :no-data="false" class="border-t">
					<template #body>
						<div v-for="(schiene, index) in getErgebnismanager().getMengeAllerSchienen()" :key="index"
							role="row" class="svws-ui-tr">
							<!-- Informationen zu der Schiene und der Statistik dazu auf der linken Seite der Tabelle -->
							<div role="cell" class="svws-ui-td svws-divider">
								<div class="flex flex-col py-1" :title="getErgebnismanager().getSchieneG(schiene.id).bezeichnung">
									<div class="inline-flex items-center" :class="{'text-error': hatSchieneKollisionen(schiene.id, schueler.id).value}">
										<i-ri-alert-line class="-mt-0.5" v-if="hatSchieneKollisionen(schiene.id, schueler.id).value" />
										<span class="mb-0.5 text-button">{{ getErgebnismanager().getSchieneG(schiene.id).bezeichnung }}</span>
									</div>
									<span class="text-sm font-medium opacity-50">{{ schiene.kurse.size() }} Kurs{{ schiene.kurse.size() === 1 ? '' : 'e' }}</span>
									<span class="text-sm font-medium opacity-50">{{ getErgebnismanager().getOfSchieneAnzahlSchueler(schiene.id) }} Schüler</span>
								</div>
							</div>

							<!-- Die Liste der Schüler-Kurse (von links nach rechts), welche der Schiene zugeordnet sind (stehen ggf. für drag und/oder drop zur Verfügung). -->
							<div role="cell" v-for="kurs of getErgebnismanager().getOfSchieneKursmengeSortiert(schiene.id)" :key="kurs.id"
								class="svws-ui-td svws-align-center svws-no-padding select-none group relative !p-[2px] svws-divider last:!border-r-0"
								:class="{ 'is-drop-zone': is_drop_zone(kurs).value, 'cursor-grab': is_draggable(kurs.id, schueler.id).value }"
								:draggable="is_draggable(kurs.id, schueler.id).value"
								@dragstart="drag_started(kurs.id, kurs.fachID, kurs.kursart)"
								@dragend="drag_ended()">
								<div class="w-full h-full flex flex-col justify-center items-center rounded border border-black/10 py-1 px-0.5"
									:style="{ 'background-color': hatSchieneKollisionen(schiene.id, schueler.id).value && is_draggable(kurs.id, schueler.id).value ? 'rgb(var(--color-error))' : bgColor(kurs.id, schueler.id) }"
									:class="{ 'text-white' : hatSchieneKollisionen(schiene.id, schueler.id).value && is_draggable(kurs.id, schueler.id).value}"
									@dragover="if (is_drop_zone(kurs).value) $event.preventDefault();" @drop="drop_aendere_kurszuordnung(kurs, schueler.id)">
									<span class="rounded-sm w-3 absolute top-1 left-1" v-if="is_draggable(kurs.id, schueler.id).value" :class="[hatSchieneKollisionen(schiene.id, schueler.id).value && is_draggable(kurs.id, schueler.id).value ? 'group-hover:bg-white/25 text-white' : 'group-hover:bg-white/75 text-black']">
										<i-ri-draggable class="w-4 -ml-0.5 opacity-50 group-hover:opacity-100" />
									</span>
									<span class="text-sm opacity-50 relative" title="Schriftlich/Insgesamt im Kurs">
										{{ getErgebnismanager().getOfKursAnzahlSchuelerSchriftlich(kurs.id) }}/{{ kurs.schueler.size() }}
									</span>
									<span class="py-0.5 font-medium" :class="{'opacity-50': !getErgebnismanager().getOfSchuelerOfKursIstZugeordnet(schueler.id, kurs.id)}">{{ getErgebnismanager().getOfKursName(kurs.id) }}</span>
									<span class="inline-flex items-center gap-1">
										<span v-if="is_draggable(kurs.id, schueler.id).value && (getDatenmanager().schuelerGetOfFachFachwahl(schueler.id, kurs.fachID).abiturfach !== null)">
											<span class="opacity-75 inline-block w-3 text-sm">&nbsp;</span>
											AB{{ getDatenmanager().schuelerGetOfFachFachwahl(schueler.id, kurs.fachID).abiturfach }}
										</span>
										<span v-if="(allow_regeln && fach_gewaehlt(schueler.id, kurs).value)">
											<span class="icon cursor-pointer" @click.stop="verbieten_regel_toggle(kurs.id, schueler.id)" :title="verbieten_regel(kurs.id, schueler.id).value ? 'Verboten' : 'Verbieten'">
												<i-ri-forbid-fill v-if="verbieten_regel(kurs.id, schueler.id).value" class="inline-block" />
												<i-ri-prohibited-line v-if="!verbieten_regel(kurs.id, schueler.id).value && !fixier_regel(kurs.id, schueler.id).value && !getErgebnismanager().getOfSchuelerOfKursIstZugeordnet(schueler.id, kurs.id)" class="inline-block" />
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
							<div v-for="n in (maxKurseInSchienen - schiene.kurse.size())" :key="n" role="cell" class="svws-ui-td svws-divider last:!border-r-0" />
						</div>
					</template>
				</svws-ui-table>
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
	import type { GostUmwahlansichtProps } from "./SCardGostUmwahlansichtProps";
	import type { DataTableColumn } from "@ui";
	import { ArrayList, ZulaessigesFach, GostKursblockungRegelTyp, GostBlockungRegel, GostBlockungsergebnisKursSchuelerZuordnung } from "@core";
	import { computed, ref } from "vue";

	type DndData = { id: number | undefined, fachID: number, kursart: number };

	const props = defineProps<GostUmwahlansichtProps>();

	const dragAndDropData = ref<DndData | undefined>(undefined);

	const allow_regeln = computed<boolean>(() => props.getDatenmanager().ergebnisGetListeSortiertNachBewertung().size() === 1);

	async function auto_verteilen() {
		if (props.schueler !== undefined)
			await props.autoKursSchuelerZuordnung(props.schueler.id);
	}

	const fachbelegungen = computed<List<GostFachwahl>>(() => {
		if (props.schueler === undefined)
			return new ArrayList<GostFachwahl>();
		try {
			return props.getDatenmanager().schuelerGetListeOfFachwahlen(props.schueler.id);
		} catch (e) {
			return new ArrayList<GostFachwahl>();
		}
	});

	function routeLaufbahnplanung() {
		if (props.schueler !== undefined)
			void props.gotoLaufbahnplanung(props.schueler.id);
	}

	const maxKurseInSchienen = computed<number>(() => {
		return props.getErgebnismanager().getOfSchieneMaxKursanzahl();
	});

	function calculateColumns() {
		const cols: DataTableColumn[] = [{ key: "schiene", label: "Schiene", minWidth: 9, span: 0.1, align: 'left' }];
		for (let i = 0; i < maxKurseInSchienen?.value; i++)
			cols.push({ key: "kurs_" + (i+1), label: "Kurs " + (i+1), align: 'center', minWidth: 6, span: 1 });
		return cols;
	}

	const cols = computed(() => calculateColumns());

	const hatSchieneKollisionen = (idSchiene: number, idSchueler: number) => computed<boolean>(() =>
		props.getErgebnismanager().getOfSchuelerOfSchieneHatKollision(idSchueler, idSchiene)
	);


	const is_draggable = (idKurs: number, idSchueler: number) => computed<boolean>(() => {
		if (props.apiStatus.pending)
			return false;
		return props.getErgebnismanager().getOfSchuelerOfKursIstZugeordnet(idSchueler, idKurs) && !props.getDatenmanager().schuelerGetIstFixiertInKurs(idSchueler, idKurs);
	});

	const is_drop_zone = (kurs: GostBlockungsergebnisKurs) => computed<boolean>(() => {
		if (dragAndDropData.value === undefined)
			return false
		const { id, fachID, kursart } = dragAndDropData.value;
		if ((id !== undefined) && (id === kurs.id))
			return false;
		if ((fachID !== kurs.fachID) || (kursart !== kurs.kursart))
			return false;
		if (props.schueler && props.getErgebnismanager().getOfSchuelerOfKursIstGesperrt(props.schueler.id, kurs.id))
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
		const zuordnung = new GostBlockungsergebnisKursSchuelerZuordnung();
		zuordnung.idSchueler = schuelerid;
		zuordnung.idKurs = obj.id;
		await props.removeKursSchuelerZuordnung([zuordnung]);
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

	const fach_gewaehlt = (idSchueler: number, kurs: GostBlockungsergebnisKurs) => computed<boolean>(() =>
		props.getErgebnismanager().getOfSchuelerHatFachwahl(idSchueler, kurs.fachID, kurs.kursart)
	);

	const fixier_regel = (idKurs: number, idSchueler: number) => computed<number | undefined>(() => {
		if (props.getDatenmanager().schuelerGetIstFixiertInKurs(idSchueler, idKurs))
			return props.getDatenmanager().schuelerGetRegelFixiertInKurs(idSchueler, idKurs).id;
		return undefined;
	});

	const verbieten_regel = (idKurs: number, idSchueler: number) => computed<number | undefined>(() => {
		if (props.getDatenmanager().schuelerGetIstVerbotenInKurs(idSchueler, idKurs))
			return props.getDatenmanager().schuelerGetRegelVerbotenInKurs(idSchueler, idKurs).id;
		return undefined;
	});

	function fixieren_regel_toggle(idKurs: number, idSchueler: number) {
		return fixier_regel(idKurs, idSchueler).value
			? fixieren_regel_entfernen(idKurs, idSchueler)
			: fixieren_regel_hinzufuegen(idKurs, idSchueler);
	}

	function verbieten_regel_toggle(idKurs: number, idSchueler: number) {
		return verbieten_regel(idKurs, idSchueler).value
			? verbieten_regel_entfernen(idKurs, idSchueler)
			: verbieten_regel_hinzufuegen(idKurs, idSchueler);
	}

	async function regel_speichern(regel: GostBlockungRegel, idKurs: number, idSchueler: number) {
		regel.parameter.add(idSchueler);
		regel.parameter.add(idKurs);
		await props.addRegel(regel);
	}

	async function fixieren_regel_hinzufuegen(idKurs: number, idSchueler: number) {
		const regel = new GostBlockungRegel();
		regel.typ = GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ;
		await regel_speichern(regel, idKurs, idSchueler);
	}

	async function fixieren_regel_entfernen(idKurs: number, idSchueler: number) {
		const idRegel = fixier_regel(idKurs, idSchueler).value;
		if (idRegel === undefined)
			return;
		await props.removeRegel(idRegel);
	}

	async function verbieten_regel_hinzufuegen(idKurs: number, idSchueler: number) {
		const regel = new GostBlockungRegel();
		regel.typ = GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ;
		await regel_speichern(regel, idKurs, idSchueler);
	}

	async function verbieten_regel_entfernen(idKurs: number, idSchueler: number) {
		const idRegel = verbieten_regel(idKurs, idSchueler).value;
		if (idRegel === undefined)
			return;
		await props.removeRegel(idRegel);
	}

	const fachwahlKurszuordnung = (idFach: number, idSchueler: number) => computed<GostBlockungsergebnisKurs | undefined>(() =>
		props.getErgebnismanager().getOfSchuelerOfFachZugeordneterKurs(idSchueler, idFach) || undefined
	);

	const fachwahlKursart = (idFach: number, idSchueler: number) => computed<GostKursart>(() =>
		props.getErgebnismanager().getOfSchuelerOfFachKursart(idSchueler, idFach)
	);

	const bgColorFachwahl = (idFach: number, idSchueler: number) =>  computed<string>(() => {
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
			@apply absolute inset-1 border border-svws pointer-events-none rounded-lg ring-4 ring-svws/25;
		}
	}

</style>
