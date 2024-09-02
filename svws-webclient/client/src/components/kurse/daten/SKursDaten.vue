<template>
	<div v-if="kursListeManager().hasDaten()" class="page--content">
		<svws-ui-content-card title="Allgemein">
			<template #actions>
				<svws-ui-checkbox v-model="istSichtbar" :disabled="!hatKompetenzUpdate"> Ist sichtbar </svws-ui-checkbox>
			</template>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Kürzel" :disabled="!hatKompetenzUpdate" :model-value="data().kuerzel" @change="kuerzel => patch({ kuerzel: kuerzel ?? '' })" type="text" />
				<svws-ui-select title="Lehrer" :disabled="!hatKompetenzUpdate" v-model="lehrer" :items="kursListeManager().lehrer.list()" :item-text="l => l.kuerzel" :empty-text="() => '---'" removable />
				<svws-ui-select title="Fach" :disabled="!hatKompetenzUpdate" v-model="fach" :items="kursListeManager().faecher.list()" :item-text="f => f.kuerzel + ' (' + f.bezeichnung + ')'" />
				<svws-ui-select title="Kursart" :disabled="!hatKompetenzUpdate" :items="kursarten.keys()" :item-text="k => k + ' (' + (kursarten.get(k) ?? '???') + ')'"
					:model-value="data().kursartAllg" @update:model-value="value => patch({ kursartAllg: value ?? '' })" />
				<svws-ui-input-number placeholder="Wochenstunden" :disabled="!hatKompetenzUpdate" :model-value="data().wochenstunden" @change="wstd => patch({ wochenstunden: wstd ?? 0 })" />
				<svws-ui-multi-select title="Jahrgänge" :disabled="!hatKompetenzUpdate" v-model="jahrgaenge" :items="jahrgangsListe" :item-text="jg => jg?.kuerzel ?? ''" />
				<svws-ui-input-number placeholder="Sortierung" :disabled="!hatKompetenzUpdate" :model-value="data().sortierung" @change="sortierung=> sortierung && patch({ sortierung })" />
				<svws-ui-text-input placeholder="Zeugnisbezeichnung" :disabled="!hatKompetenzUpdate" :model-value="data().bezeichnungZeugnis" @change="b => patch({ bezeichnungZeugnis : b })" type="text" />
				<svws-ui-select title="Fortschreibungsart" :disabled="!hatKompetenzUpdate" :model-value="KursFortschreibungsart.fromID(data().idKursFortschreibungsart)"
					@update:model-value="value => patch({ idKursFortschreibungsart: value?.id ?? 0 })"
					:items="KursFortschreibungsart.values()" :item-text="f => f.beschreibung" />
				<svws-ui-multi-select title="Schienen" :disabled="!hatKompetenzUpdate" v-model="schienen" :items="Array.from({length: 40}, (_, i) => i + 1)" :item-text="s => 'Schiene ' + s" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Kursliste">
			<svws-ui-multi-select v-model="filterSchuelerStatus" title="Status" :items="kursListeManager().schuelerstatus.list()" :item-text="status => status.bezeichnung" class="col-span-full" />
			<svws-ui-table :columns="colsSchueler" :items="kursListeManager().getSchuelerListe()">
				<template #cell(status)="{ value }: { value: number}">
					<span :class="{'opacity-25': value === 2}">{{ SchuelerStatus.fromID(value)?.bezeichnung || "" }}</span>
				</template>
				<template #header(linkToSchueler)>
					<span class="icon i-ri-group-line" />
				</template>
				<template #cell(linkToSchueler)="{ rowData }">
					<button type="button" @click.stop="gotoSchueler(rowData)" class="button button--icon" title="Schüler ansehen">
						<span class="icon i-ri-link" />
					</button>
				</template>
			</svws-ui-table>
		</svws-ui-content-card>
	</div>
	<div v-else>
		<span class="icon i-ri-presentation-line" />
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { JahrgangsDaten, LehrerListeEintrag, List } from "@core";
	import { FachDaten, SchuelerStatus, ZulaessigeKursart, KursFortschreibungsart, ArrayList, BenutzerKompetenz } from "@core";
	import type { DataTableColumn } from "@ui";
	import type { KursDatenProps } from "./SKursDatenProps";

	const props = defineProps<KursDatenProps>();

	// TODO auch UNTERRICHTSVERTEILUNG_PLANUNG_ANSEHEN verwenden und hier unterscheiden zu UNTERRICHTSVERTEILUNG_ANSEHEN
	const hatKompetenzAnsehen = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN));
	// TODO auch UNTERRICHTSVERTEILUNG_FUNKTIONSBEZOGEN_AENDERN berücksichtigen in Bezug auf Abteilungsleitungen / Koordinationen (API muss dafür noch erweitert werden)
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ALLGEMEIN_AENDERN));

	const data = () => props.kursListeManager().daten();

	const lehrer = computed<LehrerListeEintrag | null>({
		get: () => {
			const idLehrer = data().lehrer;
			return (idLehrer === null) ? null : props.kursListeManager().lehrer.get(idLehrer);
		},
		set: (value) => void props.patch({ lehrer: value?.id ?? null })
	});

	const fach = computed<FachDaten>({
		get: () => props.kursListeManager().faecher.get(data().idFach) ?? new FachDaten(),
		set: (value) => void props.patch({ idFach: value.id })
	});


	const jahrgangsListe = computed<List<JahrgangsDaten>>(() => {
		const result = new ArrayList<JahrgangsDaten>();
		for (const jg of props.kursListeManager().jahrgaenge.list()) {
			if (jg.kuerzel !== "E3") // Das dritte Jahr der Schuleingangsphase sollte nicht für einen Jahrgang einer Klasse verwendet werden, da es Schüler-spezifisch ist
				result.add(jg);
		}
		return result;
	});


	const jahrgaenge = computed<JahrgangsDaten[]>({
		get: () => {
			const arr = [];
			for (const id of data().idJahrgaenge) {
				const e = props.kursListeManager().jahrgaenge.get(id);
				if (e !== null)
					arr.push(e);
			}
			return arr;
		},
		set: (value) => {
			const result = new ArrayList<number>();
			value.forEach(j => result.add(j.id));
			void props.patch({ idJahrgaenge: result });
		}
	});

	const schienen = computed<number[]>({
		get: () => {
			return data().schienen.toArray(new Array<number>);
		},
		set: (value) => {
			const result = new ArrayList<number>();
			let changed = false;
			for (const s of value) {
				if (!data().schienen.contains(s))
					changed = true;
				result.add(s);
			}
			if (!changed)
				changed = (data().schienen.size() !== result.size());
			if (changed)
				void props.patch({ schienen : result });
		}
	});

	const kursarten = computed<Map<string, string>>(() => {
		const arten = new Map<string, string>();
		for (const art of ZulaessigeKursart.get(props.schulform)) {
			if (art.daten.kuerzel === "PUK")
				continue;
			if ((art.daten.kuerzelAllg !== null) && (art.daten.bezeichnungAllg !== null))
				arten.set(art.daten.kuerzelAllg, art.daten.bezeichnungAllg);
			else
				arten.set(art.daten.kuerzel, art.daten.bezeichnung);
			if (art.daten.kuerzelAllg === "DK")
				arten.set(art.daten.kuerzel, art.daten.bezeichnung);
		}
		return new Map([...arten.entries()].sort());
	});

	const istSichtbar = computed<boolean>({
		get: () => data().istSichtbar,
		set: (value) => void props.patch({ istSichtbar: value })
	});

	const colsSchueler: DataTableColumn[] = [
		{ key: "linkToSchueler", label: " ", fixedWidth: 1.75, align: "center" },
		{ key: "nachname", label: "Nachname", sortable: true },
		{ key: "vorname", label: "Vorname", sortable: true },
		{ key: "status", label: "Status", sortable: true, span: 0.5 }
	];

	const filterSchuelerStatus = computed<SchuelerStatus[]>({
		get: () => [...props.kursListeManager().schuelerstatus.auswahl()],
		set: (value) => {
			props.kursListeManager().schuelerstatus.auswahlClear();
			for (const v of value)
				props.kursListeManager().schuelerstatus.auswahlAdd(v);
			void props.setFilter();
		}
	});

</script>
