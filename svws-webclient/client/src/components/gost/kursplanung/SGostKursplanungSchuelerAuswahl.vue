<template>
	<svws-ui-content-card v-if="hatBlockung && hatErgebnis" class="-mt-0.5 s-gost-kursplanung-schueler-auswahl" overflow-scroll>
		<svws-ui-table :model-value="schuelerFilter().filtered.value" v-model:clicked="selected" clickable scroll :items="undefined" v-model:filter-open="isFilterOpen"
			:filtered="schuelerFilter().kurs_toggle.value === 'kurs' || schuelerFilter().fach_toggle.value === 'fach' || schuelerFilter().radio_filter !== 'alle'"
			:columns="cols" :no-data="schuelerFilter().filtered.value.length <= 0" :disable-footer="schuelerFilter().filtered.value.length <= 0">
			<template #search>
				<svws-ui-text-input type="search" v-model="schuelerFilter().name" placeholder="Suche" removable />
			</template>
			<template #filterAdvanced>
				<svws-ui-radio-group class="radio--row col-span-full">
					<svws-ui-radio-option v-model="schuelerFilter().alle_toggle.value" value="alle" name="Alle" label="&ZeroWidthSpace;">
						<i-ri-filter-off-line />
					</svws-ui-radio-option>
					<svws-ui-radio-option v-model="schuelerFilter().fach_toggle.value" value="fach" name="Fach" label="Fachfilter" :icon="false" />
					<svws-ui-radio-option v-model="schuelerFilter().kurs_toggle.value" value="kurs" name="Kurs" label="Kursfilter" :icon="false" />
				</svws-ui-radio-group>
				<svws-ui-input-wrapper class="col-span-full" v-if="schuelerFilter().kurs_toggle.value === 'kurs'">
					<svws-ui-select v-model="schuelerFilter().kurs" :items="schuelerFilter().getKurse()"
						:item-text="(kurs: GostBlockungKurs) => getErgebnismanager().getOfKursName(kurs.id) ?? ''" />
					<svws-ui-spacing />
				</svws-ui-input-wrapper>
				<svws-ui-input-wrapper :grid="2" class="col-span-full" v-if="schuelerFilter().fach_toggle.value === 'fach'">
					<svws-ui-select title="Fach" v-model="fach" :items="faecherManager.faecher()" :item-text="(fach: GostFach) => fach.bezeichnung ?? ''" />
					<svws-ui-select title="Kursart" v-model="schuelerFilter().kursart" :items="GostKursart.values()" :item-text="(kursart: GostKursart) => kursart.kuerzel" removable />
					<svws-ui-spacing />
				</svws-ui-input-wrapper>
				<svws-ui-input-wrapper class="col-span-full" v-if="schuelerFilter().alle_toggle.value === 'alle'">
					<svws-ui-select disabled :model-value="[]" :items="[]" :item-text="() => ''" />
					<svws-ui-spacing />
				</svws-ui-input-wrapper>
				<svws-ui-radio-group class="radio--row col-span-full">
					<svws-ui-radio-option v-model="schuelerFilter().radio_filter" value="alle" name="AlleA" label="Alle" :icon="false" />
					<svws-ui-tooltip>
						<svws-ui-radio-option v-model="schuelerFilter().radio_filter" value="kollisionen" name="Kollisionen" label="K">
							<i-ri-alert-line />
						</svws-ui-radio-option>
						<template #content>
							Kollision
						</template>
					</svws-ui-tooltip>
					<svws-ui-tooltip>
						<svws-ui-radio-option v-model="schuelerFilter().radio_filter" value="nichtwahlen" name="Nichtwahlen" label="NV">
							<i-ri-spam-3-line />
						</svws-ui-radio-option>
						<template #content>
							Nichtverteilt
						</template>
					</svws-ui-tooltip>
					<svws-ui-tooltip>
						<svws-ui-radio-option v-model="schuelerFilter().radio_filter" value="kollisionen_nichtwahlen" name="Kollisionen_Nichtwahlen" label="K/NV">
							<i-ri-error-warning-fill />
						</svws-ui-radio-option>
						<template #content>
							Kollision und Nichtverteilt
						</template>
					</svws-ui-tooltip>
				</svws-ui-radio-group>
			</template>
			<template #header>
				<div class="svws-ui-tr" role="row">
					<div class="svws-ui-td col-span-full" role="columnheader">
						<svws-ui-tooltip v-if="schuelerFilter().filtered.value.length > 0">
							<i-ri-information-line class="-my-0.5 -ml-0.5" />
							<template #content>
								<ul class="flex flex-col gap-3 py-1">
									<li class="flex flex-col">
										<span class="text-sm font-bold mb-0.5">Status</span>
										<span class="inline-flex gap-0.5 items-center"><i-ri-alert-line /> Kollision</span>
										<span class="inline-flex gap-0.5 items-center"><i-ri-spam3-line /> Nichtverteilung</span>
										<span class="inline-flex gap-0.5 items-center"><i-ri-error-warning-fill /> Kollision und Nichtverteilung</span>
									</li>
								</ul>
							</template>
						</svws-ui-tooltip>
						<span v-if="schuelerFilter().filtered.value.length > 0" class="pl-8">{{ schuelerFilter().filtered.value.length }} {{ weitere }}</span>
						<span>Schüler</span>
					</div>
				</div>
			</template>
			<template #body>
				<div v-for="(s, index) in schuelerFilter().filtered.value" role="row" class="svws-ui-tr" :class="{'svws-clicked': selected === s}" @click="selected = s" :key="index">
					<div role="cell" class="svws-ui-td svws-align-center pr-0">
						<div class="leading-none w-5 -mb-1" :class="{ 'text-error': kollision(s.id).value, 'text-black': !kollision(s.id).value && selected !== s, }">
							<svws-ui-tooltip v-if="kollision(s.id).value && !nichtwahl(s.id).value" color="danger">
								<i-ri-alert-line />
								<template #content>
									Kollisionen:
									<ul>
										<li v-for="k of getErgebnismanager().getOfSchuelerKursmengeMitKollisionen(s.id).toArray(new Array<GostBlockungsergebnisKurs>())" :key="k.id" class="pl-2">{{ getErgebnismanager().getOfKursName(k.id) }}</li>
									</ul>
								</template>
							</svws-ui-tooltip>
							<svws-ui-tooltip v-else-if="!kollision(s.id).value && nichtwahl(s.id).value">
								<i-ri-spam-3-line class="opacity-75" /> <template #content>
									Nichtverteilt
									<ul>
										<li v-for="f of getErgebnismanager().getOfSchuelerFachwahlmengeOhneKurszuordnung(s.id).toArray(new Array<GostFachwahl>())" :key="f.fachID" class="pl-2">{{ getErgebnismanager().getFach(f.fachID).bezeichnung }}</li>
									</ul>
								</template>
							</svws-ui-tooltip>
							<svws-ui-tooltip v-else-if="kollision(s.id).value && nichtwahl(s.id).value" color="danger">
								<i-ri-error-warning-fill /> <template #content>
									<b>Kollision und Nichtverteilt:</b>
									<br>Kollisionen:
									<ul>
										<li v-for="k of getErgebnismanager().getOfSchuelerKursmengeMitKollisionen(s.id).toArray(new Array<GostBlockungsergebnisKurs>())" :key="k.id" class="pl-2">{{ getErgebnismanager().getOfKursName(k.id) }}</li>
									</ul>
									Nichtverteilt:
									<ul>
										<li v-for="f of getErgebnismanager().getOfSchuelerFachwahlmengeOhneKurszuordnung(s.id).toArray(new Array<GostFachwahl>())" :key="f.fachID" class="pl-2">{{ getErgebnismanager().getFach(f.fachID).bezeichnung }}</li>
									</ul>
								</template>
							</svws-ui-tooltip>
						</div>
					</div>
					<div role="cell" class="svws-ui-td svws-align-center" @click.stop="fixieren_regel_toggle(fach?.id ?? schuelerFilter().kurs?.fach_id, s.id)">
						<template v-if="fixierRegeln.get(s.id) === undefined">
							<i-ri-pushpin-line v-if="!((fach === undefined) && (schuelerFilter().kurs === undefined))" class="w-5 -my-0.5 opacity-0 hover:opacity-75" />
						</template>
						<template v-else>
							<template v-if="fach !== undefined">
								<i-ri-pushpin-fill v-if="fixierRegelFach(fach?.id, s.id).value" class="w-5 -my-0.5 hover:opacity-75" />
								<i-ri-pushpin-line v-else class="w-5 -my-0.5 opacity-50 hover:opacity-75" />
							</template>
							<template v-else-if="schuelerFilter().kurs !== undefined">
								<i-ri-pushpin-fill v-if="fixierRegelKurs(schuelerFilter().kurs?.id, s.id).value" class="w-5 -my-0.5 hover:opacity-75" />
								<i-ri-pushpin-line v-else class="w-5 -my-0.5 opacity-50 hover:opacity-75" />
							</template>
							<template v-else>
								<i-ri-pushpin-line class="w-5 -my-0.5 opacity-50" />
							</template>
						</template>
					</div>
					<div role="cell" class="svws-ui-td">
						<div class="flex flex-col">
							{{ `${s.nachname}, ${s.vorname}` }}
							<template v-if="s.status !== 2">
								<span class="mt-0.5 text-sm">({{ SchuelerStatus.fromID(s.status)?.bezeichnung || '' }}{{ s.externeSchulNr ? ` ${s.externeSchulNr}` : '' }})</span>
							</template>
						</div>
					</div>
					<div v-if="showGeschlecht" role="cell" class="svws-ui-td svws-align-center pl-0">
						<span class="w-full text-center">{{ s.geschlecht }}</span>
					</div>
					<div v-if="istSchriftlich(s.id)" role="cell" class="svws-ui-td svws-align-center">
						<span>
							<i-ri-draft-line v-if="istSchriftlich(s.id) === 's'" class="w-5 -my-0.5" />
							<i-ri-chat1-line v-else class="w-5 -my-0.5 opacity-75" />
						</span>
					</div>
				</div>
			</template>
			<template #footer>
				<div role="row" class="svws-ui-tr">
					<div class="svws-ui-td col-span-full w-full">
						<div class="grid grid-cols-4 w-full gap-y-2 text-button font-medium py-1 normal-nums pl-5" :class="fach !== undefined || schuelerFilter().kurs !== undefined ? 'pt-2' : 'py-1'">
							<template v-if="fach !== undefined || schuelerFilter().kurs !== undefined">
								<span class="col-span-2 inline-flex gap-0.5" :class="{'opacity-50 font-medium': !schuelerFilter().statistics.value.schriftlich}">
									<i-ri-draft-line class="w-5 -m-0.5 mr-0.5" />{{ schuelerFilter().statistics.value.schriftlich }} schriftlich
								</span>
								<span class="col-span-2 inline-flex gap-0.5" :class="{'opacity-50 font-medium': !schuelerFilter().statistics.value.muendlich}">
									<i-ri-chat1-line class="w-5 opacity-75 -m-0.5 mr-0.5" />{{ schuelerFilter().statistics.value.muendlich }} mündlich
								</span>
							</template>
							<div class="col-span-full flex items-center gap-1">
								<svws-ui-checkbox type="toggle" v-model="showGeschlecht" :title="showGeschlecht ? 'Geschlecht in der Tabelle ausblenden' : 'Geschlecht in der Tabelle einblenden'">
									<span class="text-button font-medium">Geschlecht: </span>
								</svws-ui-checkbox>
								<span v-if="schuelerFilter().statistics.value.m">{{ schuelerFilter().statistics.value.m }} m<span v-if="schuelerFilter().statistics.value.w || schuelerFilter().statistics.value.d || schuelerFilter().statistics.value.x">, </span></span>
								<span v-if="schuelerFilter().statistics.value.w">{{ schuelerFilter().statistics.value.w }} w<span v-if="schuelerFilter().statistics.value.d || schuelerFilter().statistics.value.x">, </span></span>
								<span v-if="schuelerFilter().statistics.value.d">{{ schuelerFilter().statistics.value.d }} d<span v-if="schuelerFilter().statistics.value.x">, </span></span>
								<span v-if="schuelerFilter().statistics.value.x">{{ schuelerFilter().statistics.value.x }} x</span>
							</div>
						</div>
					</div>
				</div>
			</template>
		</svws-ui-table>
		<s-gost-kursplanung-schueler-auswahl-umkursen-modal v-if="schuelerFilter().kurs !== undefined" :get-ergebnismanager="getErgebnismanager"
			:remove-kurs-schueler-zuordnung="removeKursSchuelerZuordnung" :update-kurs-schueler-zuordnung="updateKursSchuelerZuordnung"
			:schueler-filter="schuelerFilter" v-slot="{ openModal }" :allow-regeln="allowRegeln">
			<svws-ui-button type="secondary" @click="openModal" class="mt-2"><i-ri-group-line /> Kurs-Schüler-Zuordnung ändern </svws-ui-button>
		</s-gost-kursplanung-schueler-auswahl-umkursen-modal>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { GostBlockungKurs, GostBlockungsergebnisKurs, GostFach, GostFachwahl, SchuelerListeEintrag } from "@core";
	import type { KursplanungSchuelerAuswahlProps } from "./SGostKursplanungSchuelerAuswahlProps";
	import type { DataTableColumn } from "@ui";
	import { GostBlockungRegel, GostKursart, GostKursblockungRegelTyp, SchuelerStatus } from "@core";
	import { computed } from "vue";

	const props = defineProps<KursplanungSchuelerAuswahlProps>();

	const allowRegeln = computed<boolean>(() => (props.getDatenmanager().ergebnisGetListeSortiertNachBewertung().size() === 1));

	const isFilterOpen = computed<boolean>({
		get: () => props.config.getValue("gost.kursplanung.schueler.auswahl.filterOpen") === 'true',
		set: (value) => {
			if (value === undefined)
				value = true
			void props.config.setValue("gost.kursplanung.schueler.auswahl.filterOpen", value ? 'true' : 'false');
		}
	});

	const showGeschlecht = computed<boolean>({
		get: () => props.config.getValue("gost.kursplanung.schueler.auswahl.geschlecht") === 'true',
		set: (value) => {
			if (value === undefined)
				value = true
			void props.config.setValue("gost.kursplanung.schueler.auswahl.geschlecht", value ? 'true' : 'false');
		}
	});

	const fach = computed<GostFach|undefined>({
		get: () => {
			for (const fach of props.faecherManager.faecher())
				if (fach.id === props.schuelerFilter().fach)
					return fach;
			return undefined
		},
		set: (value) => props.schuelerFilter().fach = value?.id
	})

	const selected = computed<SchuelerListeEintrag | undefined>({
		get: () => props.schueler,
		set: (value) => { if (value !== undefined) void props.setSchueler(value); }
	});

	const kollision = (idSchueler: number) => computed<boolean>(() => {
		const kursid = props.schuelerFilter().kurs?.id;
		if (kursid === undefined)
			return props.getErgebnismanager().getOfSchuelerHatKollision(idSchueler);
		return props.getErgebnismanager().getOfSchuelerOfKursHatKollision(idSchueler, kursid);
	});

	const nichtwahl = (idSchueler: number) => computed<boolean>(() =>
		props.getErgebnismanager().getOfSchuelerHatNichtwahl(idSchueler)
	);

	const weitere = computed(()=>{
		const kurs = props.schuelerFilter().kurs;
		if (kurs === undefined)
			return '';
		const anzahl = props.getErgebnismanager().getOfKursAnzahlSchuelerDummy(kurs.id);
		return anzahl > 0 ? `+${anzahl} weitere` : '';
	})

	function istSchriftlich(id: number) {
		if (fach.value !== undefined || props.schuelerFilter().kurs !== undefined) {
			const fachId = fach.value?.id || props.schuelerFilter().kurs?.fach_id
			if (fachId !== undefined)
				return props.getErgebnismanager().getParent()?.schuelerGetOfFachFachwahl(id, fachId).istSchriftlich ? 's':'m';
		}
		return undefined;
	}

	function calculateColumns() {
		const cols: DataTableColumn[] = [
			{key: 'status', label: '  ', fixedWidth: 1.75},
			{key: 'fixiert', label: 'F', tooltip: "Kursfixierung", fixedWidth: 2, align: "center"},
			{key: 'schuelerAuswahl', label: 'Schüler', span: 1},
		];
		if (showGeschlecht.value)
			cols.push({key: 'geschlecht', label: 'G', tooltip: "Geschlecht", fixedWidth: 2, align: "center"});
		if (fach.value !== undefined || props.schuelerFilter().kurs !== undefined)
			cols.push({key: 'schriftlichkeit', label: 'W', tooltip: 'Wahl: schriftlich oder mündlich', fixedWidth: 2, align: "center"});
		return cols;
	}

	const fixierRegeln = computed(()=>{
		const regeln = props.getDatenmanager().regelGetListe();
		const map = new Map<number, Set<number>>();
		for (const r of regeln)
			if (r.typ === GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ) {
				const entry = map.get(r.parameter.get(0))
				if (entry !== undefined)
					entry.add(r.parameter.get(1));
				else
					map.set(r.parameter.get(0), new Set([r.parameter.get(1)]));
			}
		return map;
	})

	const fixierRegelKurs = (idKurs: number | undefined, idSchueler: number) => computed<boolean>(() => {
		return (idKurs && props.getDatenmanager().schuelerGetIstFixiertInKurs(idSchueler, idKurs)) ? true : false;
	});

	const fixierRegelFach = (idFach: number | undefined, idSchueler: number) => computed<boolean>(() => {
		if (idFach === undefined)
			return false
		const kurs = props.getErgebnismanager().getOfSchuelerOfFachZugeordneterKurs(idSchueler, idFach);
		let idKurs = kurs?.id;
		if (idKurs === undefined)
			return false;
		return (props.getDatenmanager().schuelerGetIstFixiertInKurs(idSchueler, idKurs)) ? true : false;
	});

	const cols = computed(() => calculateColumns());


	const fixier_regel = (idKurs: number, idSchueler: number) => computed<number | undefined>(() => {
		if (props.getDatenmanager().schuelerGetIstFixiertInKurs(idSchueler, idKurs))
			return props.getDatenmanager().schuelerGetRegelFixiertInKurs(idSchueler, idKurs).id;
		return undefined;
	});

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

	async function fixieren_regel_toggle(fachID: number | undefined, idSchueler: number) {
		if (fachID === undefined)
			return;
		const kurs = props.getErgebnismanager().getOfSchuelerOfFachZugeordneterKurs(idSchueler, fachID);
		if (kurs === null)
			return;
		const idKurs = kurs.id;
		fixier_regel(idKurs, idSchueler).value
			? await fixieren_regel_entfernen(idKurs, idSchueler)
			: await fixieren_regel_hinzufuegen(idKurs, idSchueler)
	}

</script>
